package com.java.zookeeper.curator.scene.eventListener.config;

import com.java.zookeeper.api.Connect;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.io.*;
import java.util.Map;

/**
 * 〈config  server  〉
 *
 * @author xsh
 * @date 2018/10/27
 * @since 1.0.0
 */
public class ConfigServer {

    static final  String basePth = "/jay";

    static final  String CONFIG_SERVER_PATH = "/jay/config-server";

    static final  String CONFIG_CLIENT_PATH = "/jay/config-client";

    static CuratorFramework client = CuratorFrameworkFactory.builder()
            .connectString(Connect.connectString).sessionTimeoutMs(5000)
            .retryPolicy(new ExponentialBackoffRetry(1000,3))
            .build();

    public static void main(String[] args) throws  Exception{

        Environment environment = initConfig();

        client.start();

        final NodeCache cache = new NodeCache(client,CONFIG_SERVER_PATH,false);

        cache.start(true);

        client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT)
                .inBackground(((client1, event) -> {
                        System.out.println("code : "+event.getResultCode() + " , type :"+event.getType());
                        System.out.println(" thread of  processResult  "+ Thread.currentThread().getName());

                })).forPath(CONFIG_SERVER_PATH);


        cache.getListenable().addListener(new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {

                byte[] result = cache.getCurrentData().getData();
                Environment environment =SerializeUtil. deserialize(result);
                System.out.println(" config-server  update  new data"+ environment.toString());
                client.setData().forPath(CONFIG_CLIENT_PATH,SerializeUtil.serialize(environment));
            }
        });

        Thread.sleep(5000);

        client.setData().forPath(CONFIG_SERVER_PATH,SerializeUtil.serialize(environment));

        updateJDBCConfig( environment );

        Thread.sleep(2000);

        client.setData().forPath(CONFIG_SERVER_PATH,SerializeUtil.serialize(environment));

        Thread.sleep(Integer.MAX_VALUE);

    }

    private static void  updateJDBCConfig( Environment environment ){
        environment.setValue("C3P0");
    }

    private static Environment   initConfig( ){
        Environment environment = new Environment();
        environment.setName("JDBC");
        environment.setValue("DBCP");
        return environment;
    }

}