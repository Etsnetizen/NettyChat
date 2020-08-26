package cn.rxwycdh.websocket.constant;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ChenDehua  597701764@qq.com
 * @date 2020/8/23 15:28
 */
public interface ChatConstant {

    /**
     * 用channelId为键，存放握手实例。用来响应CloseWebSocketFrame的请求
     */
    Map<String, WebSocketServerHandshaker> WEBSOCKET_HANDSHAKER_MAP = new ConcurrentHashMap<>();

    /**
     * 用userId为键，存放在线的客户端连接上下文
     */
    Map<Long, ChannelHandlerContext> ONLINE_USER_MAP = new ConcurrentHashMap<>();
}
