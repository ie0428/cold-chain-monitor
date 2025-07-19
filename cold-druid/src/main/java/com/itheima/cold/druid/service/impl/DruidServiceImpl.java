package com.itheima.cold.druid.service.impl;

import com.itheima.cold.common.netty.entity.MessageEntity;
import com.itheima.cold.common.utils.Result;
import com.itheima.cold.druid.common.DruidHelper;
import com.itheima.cold.druid.service.DruidService;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.*;

@Service("druidService")
public class DruidServiceImpl implements DruidService {
    @Override
    public Result queryDruidList(Map<String, Object> params) {
        //根据参数组装查询条件
        String sql = querySQL(params);

        // 实例化Druid JDBC连接
        DruidHelper helper = new DruidHelper();
        Connection connection = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            List<MessageEntity> resultList = new ArrayList<>();
            connection = helper.getConnection();
            st = connection.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                MessageEntity messageEntity = Rs2Entity(rs);

                resultList.add(messageEntity);
            }

            //设置返回格式
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("items", resultList);

            return Result.ok(map);
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            helper.close(connection, st, rs);
        }

        return Result.error();
    }


    @Override
    public Result queryDruidTumHum(Map<String, Object> params) {
        //查询druid数据列表
        List<MessageEntity> list = (List<MessageEntity>)queryDruidList(params).get("items");

        List<String> times = new ArrayList<>();
        List<Integer> tems = new ArrayList<>();
        List<Integer> hums = new ArrayList<>();

        //给数组赋值
        for (int i = 0; i < list.size(); i++) {
            MessageEntity messageHistoryEntity = (MessageEntity) list.get(i);

            String strTime = messageHistoryEntity.getCurtime();

            times.add(strTime.substring(8, 10) + ":" + strTime.substring(10, 12)+ ":" + strTime.substring(12, 14));
            tems.add(messageHistoryEntity.getTem());
            hums.add(messageHistoryEntity.getHum());
        }

        //按时间重新排序
        Collections.reverse(times);
        Collections.reverse(tems);
        Collections.reverse(hums);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("xAxis", times);
        map.put("tem", tems);
        map.put("hum", hums);
        return Result.ok(map);
    }

    /**
     * 查询条件
     * @param params
     * @return
     */
    private String querySQL(Map<String, Object> params){
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("SELECT * from all_device_message where 1= 1 ");

        //设备编码
        String  metercode = params.get("metercode").toString().trim();
        if(!metercode.equals("")){
            stringBuffer.append(" and metercode = '");
            stringBuffer.append(metercode);
            stringBuffer.append("' ");
        }

        //前台传入开始、结束时间
        String startTime = params.get("stTime").toString().trim();
        if(!startTime.equals("")){
            stringBuffer.append(" and __time > '");
            stringBuffer.append(startTime);
            stringBuffer.append("' ");
        }
        String endTime = params.get("endTime").toString().trim();
        if(!endTime.equals("")){
            stringBuffer.append(" and __time < '");
            stringBuffer.append(endTime);
            stringBuffer.append("' ");
        }

        stringBuffer.append(" order by __time desc");

        //查询条数
        String limit = params.get("limit")!=null? params.get("limit").toString().trim():"10";
        stringBuffer.append(" limit ");
        stringBuffer.append(limit);

        System.out.println(stringBuffer.toString());
        return stringBuffer.toString();
    }

    /**
     * 查询结果转化为对象
     * @param rs
     * @return
     * @throws Exception
     */
    private MessageEntity Rs2Entity(ResultSet rs) throws Exception{
        MessageEntity messageEntity = new MessageEntity();

        messageEntity.setMetercode(rs.getString("metercode"));
        messageEntity.setMeterid(rs.getString("meterid"));
        messageEntity.setMetername(rs.getString("metername"));
        messageEntity.setHostcode(rs.getString("hostcode"));
        messageEntity.setHostid(rs.getString("hostid"));
        messageEntity.setHostname(rs.getString("hostname"));
        messageEntity.setHousecode(rs.getString("housecode"));
        messageEntity.setHouseid(rs.getString("houseid"));
        messageEntity.setHousename(rs.getString("housename"));
        messageEntity.setCompanyname(rs.getString("companyname"));
        messageEntity.setCompanyid(rs.getString("companyid"));
        messageEntity.setTem(Integer.valueOf(rs.getString("tem")));
        messageEntity.setMaxtem(Integer.valueOf(rs.getString("maxtem")));
        messageEntity.setMintem(Integer.valueOf(rs.getString("mintem")));
        messageEntity.setHum(Integer.valueOf(rs.getString("hum")));
        messageEntity.setMaxhum(Integer.valueOf(rs.getString("maxhum")));
        messageEntity.setMinhum(Integer.valueOf(rs.getString("minhum")));
        messageEntity.setTemalert(Integer.valueOf(rs.getString("temalert")));
        messageEntity.setHumalert(Integer.valueOf(rs.getString("humalert")));
        messageEntity.setState(Integer.valueOf(rs.getString("state")));
        messageEntity.setLat(rs.getString("lat"));
        messageEntity.setLon(rs.getString("lon"));
        messageEntity.setCurtime(rs.getString("curtime"));

        return messageEntity;
    }
}
