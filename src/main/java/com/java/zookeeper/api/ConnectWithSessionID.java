package com.java.zookeeper.api;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * 〈ConnectWithSessionID  〉
 *
 * @author xsh
 * @date 2018/10/25
 * @since 1.0.0
 */
public class ConnectWithSessionID implements Watcher {

    private static CountDownLatch latch = new CountDownLatch(1);

    public static void main(String[] args) throws Exception {

        ZooKeeper zooKeeper = new ZooKeeper(Connect.connectString,
                5000,new ConnectWithSessionID());

        latch.await();

        long sessionId = zooKeeper.getSessionId();
        byte[] passwd = zooKeeper.getSessionPasswd();

        // use illegal  sessionId and password
        zooKeeper = new ZooKeeper(Connect.connectString,
                5000,new ConnectWithSessionID(),
                1L,"test".getBytes());


        zooKeeper= new ZooKeeper(Connect.connectString,
                5000,new ConnectWithSessionID(),
                sessionId,passwd);

        Thread.sleep(Integer.MAX_VALUE);

    }


    public void process(WatchedEvent watchedEvent) {
        System.out.println(" receive watcher event  "+ watchedEvent);
        if (Event.KeeperState.SyncConnected == watchedEvent.getState()){
            latch.countDown();
        }
    }
}