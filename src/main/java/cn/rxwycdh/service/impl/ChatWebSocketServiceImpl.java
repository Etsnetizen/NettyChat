package cn.rxwycdh.service.impl;

import cn.rxwycdh.common.api.R;
import cn.rxwycdh.param.websocket.MessageBaseParam;
import cn.rxwycdh.param.websocket.MessageFileSingleSendParam;
import cn.rxwycdh.param.websocket.MessageSingleSendParam;
import cn.rxwycdh.service.IChatWebSocketService;
import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static cn.rxwycdh.common.enums.WebSocketChatTypeEnum.REGISTER;
import static cn.rxwycdh.websocket.constant.ChatConstant.ONLINE_USER_MAP;

/**
 * @author ChenDehua  597701764@qq.com
 * @date 2020/8/23 18:42
 */
@Service
@AllArgsConstructor
public class ChatWebSocketServiceImpl implements IChatWebSocketService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChatWebSocketServiceImpl.class);

    @Override
    public void register(MessageBaseParam param, ChannelHandlerContext context) {

        Long userId = param.getUserId();
        ONLINE_USER_MAP.put(userId, context);
        Map<String, Object> data = new HashMap<>();

        data.put("type", REGISTER);

        sendMessage(R.success(data), context);
        LOGGER.info("userId :{}用户上线，当前在线人数:{}", userId, ONLINE_USER_MAP.size());
    }

    @Override
    public void sendMessage(String jsonResponse, ChannelHandlerContext context) {
        context.channel().writeAndFlush(new TextWebSocketFrame(jsonResponse));
    }

    @Override
    public void sendMessage(R<?> result, ChannelHandlerContext context) {
        sendMessage(JSONObject.toJSONString(result), context);
    }

    @Override
    public void sendErrorMessage(String errorMsg, ChannelHandlerContext context) {
        sendMessage(JSONObject.toJSONString(R.failed(errorMsg)), context);
    }

    @Override
    public void singleSendMessage(MessageSingleSendParam param, ChannelHandlerContext context) {
        // 将聊天存到数据库 如果接受用户在线则发送消息
        // TODO 此聊天消息保存到数据库中

        ChannelHandlerContext toUserCtx = ONLINE_USER_MAP.get(param.getReceiverUserId());
        if(toUserCtx != null) {
            sendMessage(R.success(param), context);
        }
    }

    @Override
    public void singleSendFileMessage(MessageFileSingleSendParam param, ChannelHandlerContext context) {

        ChannelHandlerContext toUserCtx = ONLINE_USER_MAP.get(param.getReceiverUserId());
        // TODO 此聊天消息保存到数据库中
        if(toUserCtx != null) {
            sendMessage(R.success(param), context);
        }
    }
}
