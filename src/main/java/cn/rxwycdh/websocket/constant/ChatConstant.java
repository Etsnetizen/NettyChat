package cn.rxwycdh.websocket.constant;

import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ChenDehua  597701764@qq.com
 * @date 2020/8/23 15:28
 */
public interface ChatConstant {

    Map<String, WebSocketServerHandshaker> webSocketHandshakerMap = new ConcurrentHashMap<String, WebSocketServerHandshaker>();

}
