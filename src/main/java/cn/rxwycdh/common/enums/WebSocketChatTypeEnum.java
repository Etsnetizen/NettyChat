package cn.rxwycdh.common.enums;

/**
 * @author ChenDehua  597701764@qq.com
 * @date 2020/8/25 12:01
 */
public enum  WebSocketChatTypeEnum {
    /**
     * 上线通知类型
     */
    REGISTER("REGISTER"),
    /**
     * 单发类型
     */
    SINGLE_SENDING("SINGLE_SENDING"),
    /**
     * 单对单文件传输类型
     */
    FILE_MSG_SINGLE_SENDING("FILE_MSG_SINGLE_SENDING");

    public String value;

    WebSocketChatTypeEnum(String value) {
        this.value = value;
    }
}
