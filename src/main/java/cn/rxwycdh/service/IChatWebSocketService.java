package cn.rxwycdh.service;

import cn.rxwycdh.common.api.R;
import cn.rxwycdh.param.websocket.MessageBaseParam;
import cn.rxwycdh.param.websocket.MessageFileSingleSendParam;
import cn.rxwycdh.param.websocket.MessageSingleSendParam;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author ChenDehua  597701764@qq.com
 * @date 2020/8/23 18:41
 */
public interface IChatWebSocketService {

    /**
     * 通用发送方法1
     * @param jsonResponse json格式字符串
     * @param context channel上下文
     */
    void sendMessage(String jsonResponse, ChannelHandlerContext context);

    /**
     * 通用发送方法2
     * @param result 通用返回结果
     * @param context channel上下文
     */
    void sendMessage(R<?> result, ChannelHandlerContext context);

    /**
     * 发送错误信息
     * @param errorMsg 错误提示
     * @param context channel上下文
     */
    void sendErrorMessage(String errorMsg, ChannelHandlerContext context);

    /**
     * 发送上线通知
     * @param param   基础参数
     * @param context channel上下文
     */
    void register(MessageBaseParam param, ChannelHandlerContext context);

    void singleSendMessage(MessageSingleSendParam param, ChannelHandlerContext context);

    void singleSendFileMessage(MessageFileSingleSendParam param, ChannelHandlerContext context);
}
