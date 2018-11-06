package com.java.zookeeper.api.node.create;

import com.java.zookeeper.api.Connect;
import org.apache.zookeeper.*;

import java.util.concurrent.CountDownLatch;

/**
 * 〈同步创建 node  〉
 *
 * @author xsh
 * @date 2018/10/25
 * @since 1.0.0
 */
public class SyncCreate implements Watcher {

    private static CountDownLatch latch = new CountDownLatch(1);

    public static void main(String[] args) throws  Exception{

        ZooKeeper zooKeeper = new ZooKeeper(Connect.connectString,5000,new SyncCreate());

        latch.await();

        String nodePath = zooKeeper.create("/zk-test-ephemeral-","test".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE
        , CreateMode.EPHEMERAL);

        System.out.println(nodePath);

        String nodePath2 = zooKeeper.create("/zk-test-ephemeral-","test".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE
                , CreateMode.EPHEMERAL_SEQUENTIAL);

        System.out.println(nodePath2);

    }

    public void process(WatchedEvent watchedEvent)   {
        System.out.println(" receive watcher event  "+ watchedEvent);
        if (Event.KeeperState.SyncConnected == watchedEvent.getState()){
            latch.countDown();
        }
    }

}