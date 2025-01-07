package com.itwray.iw.web.exception;

import com.itwray.iw.common.IwException;

/**
 * IW Web 服务异常
 * <p>属于编码期导致的bug异常, 不应该把该异常信息返回给客户端, 需要在测试阶段解决这类异常.</p>
 *
 * @author wray
 * @since 2024/4/15
 */
public class IwWebException extends IwException {

    /**
     * 同步到钉钉告警
     */
    private boolean syncDingTalk = false;

    public IwWebException() {
        super();
    }

    public IwWebException(Throwable cause) {
        super(cause);
    }

    public IwWebException(String message) {
        super(message);
    }

    public IwWebException(String message, boolean syncDingTalk) {
        super(message);
        this.syncDingTalk = syncDingTalk;
    }

    public IwWebException(Integer code, String message) {
        super(code, message);
    }

    public static IwWebException withoutDingTalk(String message) {
        return new IwWebException(message, false);
    }

    public boolean isSyncDingTalk() {
        return syncDingTalk;
    }

    public void setSyncDingTalk(boolean syncDingTalk) {
        this.syncDingTalk = syncDingTalk;
    }
}
