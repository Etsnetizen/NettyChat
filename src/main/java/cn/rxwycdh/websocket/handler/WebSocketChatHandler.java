package cn.rxwycdh.websocket.handler;

import cn.hutool.core.util.ObjectUtil;
import cn.rxwycdh.common.api.R;
import cn.rxwycdh.param.websocket.MessageBaseParam;
import cn.rxwycdh.param.websocket.MessageFileSingleSendParam;
import cn.rxwycdh.param.websocket.MessageSingleSendParam;
import cn.rxwycdh.service.IChatWebSocketService;
import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.*;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static cn.rxwycdh.common.enums.WebSocketChatTypeEnum.*;
import static cn.rxwycdh.websocket.constant.ChatConstant.WEBSOCKET_HANDSHAKER_MAP;

/**
 * @author ChenDehua  597701764@qq.com
 * @date 2020/8/23 15:33
 */
@Component
@Sharable
@AllArgsConstructor
public class WebSocketChatHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketChatHandler.class);

    private IChatWebSocketService webSocketService;

    /**
     * 描述：读取完连接的消息后，对消息进行处理。
     *      这里主要是处理WebSocket请求
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame msg) throws Exception {
        handlerWebSocketFrame(ctx, msg);
    }

    /**
     * 描述：处理WebSocketFrame
     * @param ctx
     * @param frame
     * @throws Exception
     */
    private void handlerWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {

        // 关闭请求
        if (frame instanceof CloseWebSocketFrame) {
            WebSocketServerHandshaker handshaker =
                    WEBSOCKET_HANDSHAKER_MAP.get(ctx.channel().id().asLongText());
            if (handshaker == null) {
                sendErrorMessage(ctx, "不存在的客户端连接！");
            } else {
                handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
            }
            return;
        }
        // ping请求
        if (frame instanceof PingWebSocketFrame) {
            ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        // 只支持文本格式，不支持二进制消息
        if (frame instanceof TextWebSocketFrame) {

            // 客服端发送过来的消息
            String request = ((TextWebSocketFrame)frame).text();
            LOGGER.info("服务端收到新信息：" + request);
            MessageBaseParam param = null;

            try {
                param = JSONObject.parseObject(request, MessageBaseParam.class);

            } catch (Exception e) {
                sendErrorMessage(ctx, "JSON字符串转换出错！");
                e.printStackTrace();
            }
            if (param != null) {

                String type = param.getType();
                if(REGISTER.value.equals(type)) {
                    webSocketService.register(param, ctx);
                }else if(SINGLE_SENDING.value.equals(type)) {
                    webSocketService.singleSendMessage(JSONObject.parseObject(request, MessageSingleSendParam.class), ctx);
                }else if(FILE_MSG_SINGLE_SENDING.value.equals(type)) {
                    webSocketService.singleSendFileMessage(JSONObject.parseObject(request, MessageFileSingleSendParam.class), ctx);
                }else {
                    webSocketService.sendErrorMessage("消息类型不存在！", ctx);
                }
            }else {
                sendErrorMessage(ctx, "参数为空！");
            }
        }else {
            sendErrorMessage(ctx, "仅支持文本(Text)格式，不支持二进制消息");
        }

    }

    private void sendErrorMessage(ChannelHandlerContext ctx, String errorMsg) {

        String responseJson = JSONObject.toJSONString(R.failed(errorMsg));

        ctx.channel().writeAndFlush(new TextWebSocketFrame(responseJson));
    }
}
