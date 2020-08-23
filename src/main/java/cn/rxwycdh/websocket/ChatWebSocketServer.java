package cn.rxwycdh.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author ChenDehua  597701764@qq.com
 * @date 2020/8/22 13:45
 */
@Component
public class ChatWebSocketServer implements Runnable{

    private final Logger logger = LoggerFactory.getLogger(ChatWebSocketServer.class);

    @Resource
    private EventLoopGroup masterGroup;

    @Resource
    private EventLoopGroup workerGroup;

    @Resource
    private ServerBootstrap serverBootstrap;

    @Value("${websocket.port}")
    private int port;

    @Resource(name = "childChannelHandler")
    private ChannelHandler childChannelHandler;

    private ChannelFuture serverChannelFuture;

    public ChatWebSocketServer() {

    }

    @Override
    public void run() {
        build();
    }

    /**
     * 描述：启动Netty Websocket服务器
     */
    public void build() {
        try {
            long begin = System.currentTimeMillis();
            // master负责客户端的tcp连接请求  worker负责与客户端之前的读写操作
            serverBootstrap.group(masterGroup, workerGroup)
                    // 配置客户端的channel类型
                    .channel(NioServerSocketChannel.class)
                    // 配置TCP参数，握手字符串长度设置
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    // TCP_NODELAY算法，尽可能发送大块数据，减少充斥的小块数据
                    .option(ChannelOption.TCP_NODELAY, true)
                    // 开启心跳包活机制，就是客户端、服务端建立连接处于ESTABLISHED状态，超过2小时没有交流，机制会被启动
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    // 配置固定长度接收缓存区分配器
                    .childOption(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(592048))
                    // 绑定I/O事件的处理类,WebSocketChildChannelHandler中定义
                    .childHandler(childChannelHandler);
            long end = System.currentTimeMillis();
            logger.info("Netty Chat Websocket聊天服务器启动完成，耗时 " + (end - begin)
                    + " ms，已绑定端口 " + port + " 等候客户端连接");

            serverChannelFuture = serverBootstrap.bind(port).sync();
        } catch (Exception e) {
            logger.info(e.getMessage());
            masterGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            e.printStackTrace();
        }
    }


    /**
     * 描述：关闭Netty Websocket服务器，主要是释放连接
     *     连接包括：服务器连接serverChannel，
     *     客户端TCP处理连接bossGroup，
     *     客户端I/O操作连接workerGroup
     *
     *     若只使用
     *         bossGroupFuture = bossGroup.shutdownGracefully();
     *         workerGroupFuture = workerGroup.shutdownGracefully();
     *     会造成内存泄漏。
     */
    public void close(){
        serverChannelFuture.channel().close();
        Future<?> bossGroupFuture = masterGroup.shutdownGracefully();
        Future<?> workerGroupFuture = workerGroup.shutdownGracefully();

        try {
            bossGroupFuture.await();
            workerGroupFuture.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public ChannelHandler getChildChannelHandler() {
        return childChannelHandler;
    }

    public void setChildChannelHandler(ChannelHandler childChannelHandler) {
        this.childChannelHandler = childChannelHandler;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
