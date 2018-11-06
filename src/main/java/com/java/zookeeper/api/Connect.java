package com.java.zookeeper.api;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * 〈connect 〉
 *
 * @author xsh
 * @date 2018/10/25
 * @since 1.0.0
 */
public class Connect implements Watcher {

    private static CountDownLatch latch = new CountDownLatch(1);

    public   static final String connectString = "192.168.2.190:2181";

    public static void main(String[] args)throws IOException {

        ZooKeeper zooKeeper =
                new ZooKeeper(connectString,5000,new Connect());

        System.out.println("zookeeper state :"+zooKeeper.getState());
        try{
            latch.await();
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println( "  zookeeper session established ");
    }

    public void process(WatchedEvent watchedEvent) {
        System.out.println(" receive watcher event  "+ watchedEvent);
        if (Event.KeeperState.SyncConnected == watchedEvent.getState()){
            latch.countDown();
        }
    }

}