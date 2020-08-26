package cn.rxwycdh.param.websocket;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author ChenDehua  597701764@qq.com
 * @date 2020/8/26 10:18
 */
@Setter
@Getter
@ToString
@ApiModel("消息单对单文件发送参数")
public class MessageFileSingleSendParam extends MessageBaseParam{

    @ApiModelProperty("接收消息的用户id")
    private Long receiverUserId;

    @ApiModelProperty("文件名称")
    private String originalFilename;

    @ApiModelProperty("文件url")
    private String fileUrl;

    @ApiModelProperty("文件大小")
    private Long fileSize;

}
