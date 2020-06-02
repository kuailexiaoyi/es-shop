package com.es.shop.zk;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * @Description:
 * @CreateTime: 2020-06-02
 * @Version:v1.0
 */
public class ZookeeperWathch implements Watcher {
    private Logger logger = LoggerFactory.getLogger(ZookeeperWathch.class);

    private CountDownLatch countDownLatch;

    public ZookeeperWathch() {
    }

    public ZookeeperWathch(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        logger.debug("ZookeeperWathch.process ,watchedEvent.status : {}", watchedEvent.getState());
        // 当监听到节点状态发生变化的时候，使CountDownLatch减一，代码继续向下执行，即通知其他等待的线程可以成功获取锁
        if (Event.KeeperState.SyncConnected == watchedEvent.getState()) {
            countDownLatch.countDown();
        }
    }
}
