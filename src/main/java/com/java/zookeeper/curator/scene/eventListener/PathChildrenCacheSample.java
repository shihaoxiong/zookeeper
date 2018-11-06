package com.java.zookeeper.curator.scene.eventListener;

import com.java.zookeeper.api.Connect;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
 * 〈pth children  cache sample  〉
 *  只能对当前目录子节点事件有效，对当前目录和3级目录都无效
 * @author xsh
 * @date 2018/10/26
 * @since 1.0.0
 */
public class PathChildrenCacheSample {

    static String path = "/zk-childCache";

    static CuratorFramework client = CuratorFrameworkFactory.builder()
            .connectString(Connect.connectString).sessionTimeoutMs(5000)
            .retryPolicy(new ExponentialBackoffRetry(1000,3))
            .build();

    public static void main(String[] args) throws Exception{

        client.start();

        PathChildrenCache childrenCache = new PathChildrenCache(client,path,true);

        childrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);

        childrenCache.getListenable().addListener((client ,event)->{
            switch (event.getType()){
                case CHILD_ADDED:
                    System.out.println("child add "+event.getData().getPath()); break;
                case CHILD_UPDATED:
                    System.out.println("child update "+event.getData().getPath()); break;
                case CHILD_REMOVED:
                    System.out.println("child update "+event.getData().getPath()); break;
                default:break;
            }
        });

        client.create().withMode(CreateMode.PERSISTENT).forPath(path);

        Thread.sleep(1000);

        client.create().withMode(CreateMode.PERSISTENT).forPath(path+"/c1");

        Thread.sleep(1000);

        client.delete().forPath(path+"/c1");

        Thread.sleep(Integer.MAX_VALUE);

    }

}