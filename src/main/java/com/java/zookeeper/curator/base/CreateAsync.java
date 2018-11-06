package com.java.zookeeper.curator.base;

import com.java.zookeeper.api.Connect;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 〈 异步 〉
 *
 * @author xsh
 * @date 2018/10/26
 * @since 1.0.0
 */
public class CreateAsync {

    static String path = "/zk-test1";

    static CuratorFramework client = CuratorFrameworkFactory.builder()
            .connectString(Connect.connectString).sessionTimeoutMs(5000)
            .retryPolicy(new ExponentialBackoffRetry(1000,3))
            .build();
    static CountDownLatch latch = new CountDownLatch(2);

    static ExecutorService service = Executors.newFixedThreadPool(2);

    public static void main(String[] args) throws Exception {

        client.start();

        System.out.println(" main thread : "+Thread.currentThread().getName());

        client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL)
                .inBackground(new BackgroundCallback() {
                    @Override
                    public void processResult(CuratorFramework curatorFramework, CuratorEvent event) throws Exception {
                        System.out.println("code : "+event.getResultCode() + " , type :"+event.getType());
                        System.out.println(" thread of  processResult  "+ Thread.currentThread().getName());
                        latch.countDown();
                    }
                }, service).forPath(path,"init".getBytes());


        client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL)
                .inBackground(new BackgroundCallback() {
                    public void processResult(CuratorFramework curatorFramework, CuratorEvent event) throws Exception {
                        System.out.println("code : "+event.getResultCode() + " , type :"+event.getType());
                        System.out.println(" thread of  processResult  "+ Thread.currentThread().getName());
                        latch.countDown();
                    }
                }).forPath(path,"init".getBytes());

        latch.await();
        System.out.println( " over ");
        service.shutdown();
    }

}