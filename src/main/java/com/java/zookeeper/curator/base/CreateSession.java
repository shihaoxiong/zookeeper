package com.java.zookeeper.curator.base;

import com.java.zookeeper.api.Connect;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

/**
 * 〈curator  创建 session  〉
 * @author xsh
 * @date 2018/10/26
 * @since 1.0.0
 */
public class CreateSession {

    public static void main(String[] args) throws  Exception{

        String path = "/zk-test/curator" ;

        RetryPolicy policy = new ExponentialBackoffRetry(1000,3);

        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(Connect.connectString).sessionTimeoutMs(5000)
                .retryPolicy(policy).build();

        client.start();

        client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL)
                .forPath(path,"init".getBytes());

        Stat stat = new Stat();

        client.getData().storingStatIn(stat).forPath(path);

        System.out.println(String.valueOf(stat.getVersion() )+ String.valueOf(stat.getDataLength() ));

        client.delete().deletingChildrenIfNeeded().withVersion(stat.getVersion()).forPath(path);

        Thread.sleep(Integer.MAX_VALUE);

    }

}