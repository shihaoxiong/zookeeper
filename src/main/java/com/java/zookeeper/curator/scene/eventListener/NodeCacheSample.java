package com.java.zookeeper.curator.scene.eventListener;

import com.java.zookeeper.api.Connect;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.util.concurrent.CountDownLatch;

/**
 * 〈nodeCache   example  〉
 *
 * @author xsh
 * @date 2018/10/26
 * @since 1.0.0
 */
public class NodeCacheSample {

    static String path = "/zk-test2/c1";

    static CuratorFramework client = CuratorFrameworkFactory.builder()
            .connectString(Connect.connectString).sessionTimeoutMs(5000)
            .retryPolicy(new ExponentialBackoffRetry(1000,3))
            .build();
    static CountDownLatch latch = new CountDownLatch(2);

    public static void main(String[] args) throws Exception{

        client.start();

        client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT)
                .forPath(path,"init".getBytes());

        final NodeCache cache = new NodeCache(client,path,false);

        cache.start(true);

        cache.getListenable().addListener(new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {
                System.out.println(" ndoe  update  new data"+ new String(cache.getCurrentData().getData()));
            }
        });
        client.setData().forPath(path,"new Data".getBytes());
        Thread.sleep(1000);
        client.delete().deletingChildrenIfNeeded().forPath(path);
        Thread.sleep(Integer.MAX_VALUE);
    }

}