package com.itheima.cold.druid.controller;

import java.util.*;

import com.itheima.cold.common.utils.Result;
import com.itheima.cold.druid.service.DruidService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("apache-druid/query")
public class DruidController {
	@Autowired
	private DruidService druidService;

	private Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * 查询Druid中数据列表
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/select")
	public Result Select(@RequestParam Map<String, Object> params) {
		return druidService.queryDruidList(params);
	}

	/**
	 * 查询Druid中温度湿度数据（设备最近10条数据）
	 * @param params
	 * @return
	 */
	@RequestMapping("/temhum")
	public Result temhumLine(@RequestParam Map<String, Object> params){
		return druidService.queryDruidTumHum(params);
	}
}
