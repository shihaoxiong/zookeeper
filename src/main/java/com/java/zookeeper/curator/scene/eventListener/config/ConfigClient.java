package com.java.zookeeper.curator.scene.eventListener.config;

import com.java.zookeeper.api.Connect;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
 * 〈todo 〉
 *
 * @author xsh
 * @date 2018/10/27
 * @since 1.0.0
 */
public class ConfigClient {

    static final  String basePth = "/jay";

    static final  String CONFIG_SERVER_PATH = "/jay/config-server";

    static final  String CONFIG_CLIENT_PATH = "/jay/config-client";

    static CuratorFramework client = CuratorFrameworkFactory.builder()
            .connectString(Connect.connectString).sessionTimeoutMs(5000)
            .retryPolicy(new ExponentialBackoffRetry(1000,3))
            .build();

    static   Environment globeEnvironment  = new Environment();

    public static void main(String[] args) throws  Exception{

        client.start();

        client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT)
                .inBackground(((client1, event) -> {
                    System.out.println("code : "+event.getResultCode() + " , type :"+event.getType());
                    System.out.println(" thread of  processResult  "+ Thread.currentThread().getName());

                })).forPath(CONFIG_CLIENT_PATH);


       // System.out.println( " init Environment : "+ globeEnvironment.toString());

        final NodeCache cache = new NodeCache(client,CONFIG_CLIENT_PATH,false);

        cache.start(true);

        cache.getListenable().addListener(new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {
                byte[] result = cache.getCurrentData().getData();
                Environment environment =SerializeUtil. deserialize(result);
                globeEnvironment = environment;
                System.out.println("config-client receive  :  "+globeEnvironment.toString());
            }
        });

        Thread.sleep(Integer.MAX_VALUE);
    }


}