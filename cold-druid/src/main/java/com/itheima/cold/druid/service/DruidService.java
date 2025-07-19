package com.itheima.cold.druid.service;

import com.itheima.cold.common.utils.Result;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

public interface DruidService {
    /**
     * 查询Druid数据列表
     */
    Result queryDruidList(@RequestParam Map<String, Object> params);

    /**
     * 查询设备Druid温度湿度（最近10条）
     */
    Result queryDruidTumHum(@RequestParam Map<String, Object> params);
}
