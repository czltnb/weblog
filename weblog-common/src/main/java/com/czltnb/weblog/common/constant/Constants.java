package com.czltnb.weblog.common.constant;

import java.time.format.DateTimeFormatter;

public interface Constants {
    /**
     * 月-日 格式
     */
    DateTimeFormatter MONTH_DAY_FORMATTER = DateTimeFormatter.ofPattern("MM-dd");
}
