package com.java.zookeeper.api.node.list;

import com.java.zookeeper.api.Connect;
import com.java.zookeeper.api.domain.User;
import org.apache.zookeeper.*;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 〈同步  list child 〉
 *
 * @author xsh
 * @date 2018/10/26
 * @since 1.0.0
 */
public class SyncList  implements Watcher {

    private static User  user = null;

    private static CountDownLatch latch = new CountDownLatch(1);

    public static void main(String[] args) throws Exception{

        String path = "/zk-test1";

        ZooKeeper zooKeeper = new ZooKeeper(Connect.connectString,
                5000 , new SyncList());

        String nodePath = zooKeeper.create(path,"test".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE
                , CreateMode.PERSISTENT);

        String nodePath1 = zooKeeper.create(path+"/c1","test".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE
                , CreateMode.PERSISTENT);

        List<String> childrenList = zooKeeper.getChildren(path,true);
        System.out.println(childrenList);

         zooKeeper.create(path+"/c2","test".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE
                , CreateMode.PERSISTENT);

        Thread.sleep(Integer.MAX_VALUE);
    }


    public void process(WatchedEvent watchedEvent) {
        System.out.println(" receive watcher event  "+ watchedEvent);
        if (Event.KeeperState.SyncConnected == watchedEvent.getState()){
            latch.countDown();
        }
    }
}