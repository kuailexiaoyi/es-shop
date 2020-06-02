package com.es.shop.zk;


import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * @Description:
 * @CreateTime: 2020-06-02
 * @Version:v1.0
 */
public class ZookeeperSession {

    private Logger logger = LoggerFactory.getLogger(ZookeeperSession.class);

    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    private ZooKeeper zooKeeper;

    public ZookeeperSession() {
        try {
            // 去连接zookeeper server，创建会话的时候，是异步去进行的
            // 所以监听器，说告诉我们什么时候才是真正的跟 zk server 连接。
            this.zooKeeper = new ZooKeeper("192.168.0.102:2181,192.168.0.103:2181,192.168.0.104:2181", 50000, new ZookeeperWathch(countDownLatch));

            // 给一个状态CONNECTING，连接中
            System.out.println(this.zooKeeper.getState());

            // CountDownLatch
            // Java多线程并发同步的一个工具类
            // 会传递一些数字，比如1,2,3
            // 然后await，如果数字不是0，那么就卡住，等待数字为0，继续向下执行。
            // 其他的线程可以调用await(),减1
            // 如果数字减到0，那么之前所有在awaith的线程，都会继续向下执行。
            countDownLatch.await();
        } catch (Exception e) {
            logger.error("ZookeeperSession.ZookeeperSession error,", e);
        }

        logger.info("ZookeeperSession.ZookeeperSession end, Zookeeper session established");
    }

    /**
     * @Desc: 尝试获取zk锁
     * @Param productId
     * @Return void
     * @Date: 2020/6/2
     */
    public void tryAcquire(int productId) {
        String path = "/product-lock-" + productId;

        try {
            this.zooKeeper.create(path, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            logger.info("ZookeeperSession.tryAcquire proces, success to acquire to lock for product, productId : {}", productId);
        } catch (Exception e) {
            e.printStackTrace();
            int count = 0;
            // 如果 productId 对应的锁的node已经存在，表示该商品已经有线程获取锁了，当前线程需等待。
            while (true) {
                // 先睡1秒中，尝试是不是可以重新创建锁，如
                try {
                    Thread.sleep(1000);
                    this.zooKeeper.create(path, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
                } catch (Exception e1) {
                    e1.printStackTrace();
                    count++;
                    // 如果依旧创建临时节点失败，继续下一次等待。
                    logger.info("ZookeeperSession.tryAcquire process,{} times to acquire lock, productId : {}", count, productId);
                    continue;
                }
                // 如果创建临时节点成功，表示获取锁成功，跳出循环
                logger.info("ZookeeperSession.tryAcquire process, {} times success to acquire lock for this product, productId : {}， ", count, productId);
                break;
            }
        }
    }

    /**
     * @Desc: 释放锁
     * @Param productId
     * @Return void
     * @Date: 2020/6/2
     */
    public void tryRelease(int productId) {
        String path = "/product-lock-" + productId;
        try {
            // 删除临时节点，表示释放锁；当其他线程过来尝试获取锁时，发现productId对应锁的节点不存在，就可以成功获取锁
            this.zooKeeper.delete(path, -1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }


    /**
     * @Desc: 获取加锁对象
     * @Param
     * @Return com.es.shop.zk.ZookeeperSession
     * @Date: 2020/6/2
     */
    public static ZookeeperSession getInstance() {
        return Singleton.getSingleton();
    }

    static class Singleton {
        private static ZookeeperSession zookeeperSession;

        static {
            zookeeperSession = new ZookeeperSession();
        }

        public static ZookeeperSession getSingleton() {
            return zookeeperSession;
        }
    }
}
