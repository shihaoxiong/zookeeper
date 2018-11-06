package com.java.zookeeper.api.node.create;

import com.java.zookeeper.api.Connect;
import org.apache.zookeeper.*;

import java.util.concurrent.CountDownLatch;

/**
 * 〈 异步创建 node 〉
 *
 * @author xsh
 * @date 2018/10/25
 * @since 1.0.0
 */
public class AyncCreate implements Watcher {

    private static CountDownLatch latch = new CountDownLatch(1);


    public static void main(String[] args) throws Exception{

        ZooKeeper zooKeeper = new ZooKeeper(Connect.connectString,
                5000,new AyncCreate());

        latch.await();

         zooKeeper.create("/zk-test-ephemeral-",
                "test".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL
        , new IStringCallback() , "I am context ");


        zooKeeper.create("/zk-test-ephemeral-",
                "test".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL
                , new IStringCallback() , "I am context ");

        zooKeeper.create("/zk-test-ephemeral-",
                "test".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL_SEQUENTIAL
                , new IStringCallback() , "I am context ");

        Thread.sleep(Integer.MAX_VALUE);

    }


    public void process(WatchedEvent watchedEvent) {
        System.out.println(" receive watcher event  "+ watchedEvent);
        if (Event.KeeperState.SyncConnected == watchedEvent.getState()){
            latch.countDown();
        }
    }
}
class   IStringCallback implements AsyncCallback.StringCallback {
    //  i 为异步回调的响应码  0 ok  -4  断开连接  -110 节点已存在  -112 会话已过期
    public void processResult(int i, String s, Object o, String s1) {
        System.out.println("create path result : ["+i + "," + s +","+o +", real path name + "+s1);
    }
}