package com.java.zookeeper.api.node.list;

import com.java.zookeeper.api.Connect;
import com.java.zookeeper.api.domain.User;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 〈aync  〉
 *
 * @author xsh
 * @date 2018/10/26
 * @since 1.0.0
 */
public class AyncList implements Watcher {

    private static User user = null;

    private static CountDownLatch latch = new CountDownLatch(1);

    private static ZooKeeper zooKeeper =  null;

    public static void main(String[] args) throws Exception{

        user = new User();
        user.setId(1);
        user.setName("xsh");

        String path = "/zk-test";

        zooKeeper = new ZooKeeper(Connect.connectString,
                5000 , new AyncList());

        latch.await();

        String nodePath = zooKeeper.create(path,"test".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE
                , CreateMode.PERSISTENT);

        String nodePath1 = zooKeeper.create(path+"/c1","test".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE
                , CreateMode.EPHEMERAL);

        zooKeeper.getChildren(path,true,new IChildrenCallback() ,user);

        zooKeeper.create(path+"/c2","test".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE
                , CreateMode.EPHEMERAL);

        Thread.sleep(Integer.MAX_VALUE);
    }

    public void process(WatchedEvent watchedEvent) {
        System.out.println(" receive watcher event  "+ watchedEvent);
        if (Watcher.Event.KeeperState.SyncConnected == watchedEvent.getState()){
            if(Watcher.Event.EventType.None ==watchedEvent.getType() && null == watchedEvent.getPath()){
                latch.countDown();
            }else if (watchedEvent.getType() == Event.EventType.NodeChildrenChanged){
                try{
                    System.out.println(" get children : "+ zooKeeper.getChildren(watchedEvent.getPath(),true));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}
class  IChildrenCallback implements AsyncCallback.Children2Callback{
    // i 相应状态码
    public void processResult(int i, String s, Object o, List<String> list, Stat stat) {
        System.out.println( " get children list  " + list + " user :  " +o +"resp code  "+i
        + " path  "+ s   + " stat " + stat);
    }
}