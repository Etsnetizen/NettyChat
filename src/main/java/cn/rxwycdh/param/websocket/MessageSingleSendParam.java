package cn.rxwycdh.param.websocket;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author ChenDehua  597701764@qq.com
 * @date 2020/8/26 10:04
 */
@Setter
@Getter
@ToString
@ApiModel("消息单对单发送参数")
public class MessageSingleSendParam extends MessageBaseParam{

    @ApiModelProperty("接收消息的用户id")
    private Long receiverUserId;

    @ApiModelProperty("消息内容")
    private String content;
}
