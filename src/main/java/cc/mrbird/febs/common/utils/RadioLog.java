package cc.mrbird.febs.common.utils;

import lombok.Data;

/**
 * 推流日志记录
 */
@Data
public class RadioLog {
    private String logId;
    private String logTime;
    private String content;
}
