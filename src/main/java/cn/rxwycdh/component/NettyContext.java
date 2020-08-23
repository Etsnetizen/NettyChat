package cn.rxwycdh.component;

import cn.rxwycdh.websocket.ChatWebSocketServer;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.concurrent.*;

/**
 * @author ChenDehua  597701764@qq.com
 * @date 2020/8/22 13:44
 */
@Component
@Scope("singleton")
public class NettyContext {

    private final Logger logger = LoggerFactory.getLogger(NettyContext.class);

    @Resource
    private ChatWebSocketServer chatWebSocketServer;

    private ScheduledExecutorService nettyThread;

    /**
     * 描述：Tomcat加载完ApplicationContext-main和netty文件后：
     *      1. 启动Netty WebSocket服务器；
     *      2. 加载用户数据；
     *      3. 加载用户交流群数据。
     */
    @PostConstruct
    public void init() {

        logger.info("开启独立线程，启动Netty Chat WebSocket服务器...");

        nettyThread = new ScheduledThreadPoolExecutor(1,
                new BasicThreadFactory.Builder().namingPattern("example-schedule-pool-%d").daemon(true).build());
//        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        nettyThread.execute(chatWebSocketServer);
//        nettyThread = new Thread(chatWebSocketServer);
//        logger.info("加载用户数据...");
//        userInfoDao.loadUserInfo();
//        logger.info("加载用户交流群数据...");
//        groupDao.loadGroupInfo();
    }

    /**
     * 描述：Tomcat服务器关闭前需要手动关闭Netty Websocket相关资源，否则会造成内存泄漏。
     *      1. 释放Netty Websocket相关连接；
     *      2. 关闭Netty Websocket服务器线程。（强行关闭，是否有必要？）
     */
    @PreDestroy
    public void close() {

        logger.info("正在释放Netty Chat Websocket相关连接...");
        chatWebSocketServer.close();
        logger.info("正在关闭Netty Chat Websocket服务器线程...");

        // Disable new tasks from being submitted
        nettyThread.shutdown();
        try {
            // Wait a while for existing tasks to terminate
            if (!nettyThread.awaitTermination(60, TimeUnit.SECONDS)) {
                // Cancel currently executing tasks
                nettyThread.shutdownNow();
            }
            // Wait a while for tasks to respond to being cancelled
            if (!nettyThread.awaitTermination(60, TimeUnit.SECONDS)) {
                System.err.println("Pool did not terminate");
            }
        }
        catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            nettyThread.shutdownNow();// Preserve interrupt status
            Thread.currentThread().interrupt();
        }

        logger.info("系统成功关闭！");
    }
}

