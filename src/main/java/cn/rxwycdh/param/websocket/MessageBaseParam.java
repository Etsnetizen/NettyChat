package cn.rxwycdh.param.websocket;

import cn.rxwycdh.common.enums.WebSocketChatTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ChenDehua  597701764@qq.com
 * @date 2020/8/25 11:46
 */
@Data
@ApiModel("chat webcocket顶层传输对象 向下扩展")
public class MessageBaseParam {

    @ApiModelProperty("发送消息的用户id")
    private Long userId;

    /**
     * 消息类型
     * 见{@link WebSocketChatTypeEnum}
     */
    @ApiModelProperty("消息类型")
    private String type;
}
