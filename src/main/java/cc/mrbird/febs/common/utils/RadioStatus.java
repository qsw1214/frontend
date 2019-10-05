package cc.mrbird.febs.common.utils;

import lombok.Data;

@Data
public class RadioStatus {
    private String url;
    /**
     * 在线状态。1：在线，0：离线  说明：只有在指查询时才可能出现此值为0（离线）的情况。
     */
    private String  status;
    /**
     * 推流开始时间。格式：Y-M-D H:M:S
     */
    private String startDate;
}
