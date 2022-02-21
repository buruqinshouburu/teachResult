package com.qzkj.teachingresult.Dao.impl;

import com.qzkj.teachingresult.Dao.operateDao;
import com.qzkj.teachingresult.Util.ConvertUtil;
import com.qzkj.teachingresult.Util.MapRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Transactional
@Repository
public class OperateDaoimpl implements operateDao {
    @Autowired
    private NamedParameterJdbcTemplate template;
    @Override
    public int operateData(String sql, Map<String,Object> param)  throws Exception{
        sql = updateSqlTszf(sql);
        int i = template.update(sql, param);
        return i;
    }

    @Override
    public List execSqlQueryToMap(String sqlStr) throws Exception{
        sqlStr = ConvertUtil.valBeginning(sqlStr);
        Date startTime = new Date();
        List l = this.template.query(sqlStr, new MapRowMapper());
        Date endTime = new Date();
        long hssj = endTime.getTime() - startTime.getTime();
        return l;
    }
    @Override
    public List execSqlQueryToMap(String sqlStr,Map<String,Object> paramt) {
        sqlStr = ConvertUtil.valBeginning(sqlStr);
        Date startTime = new Date();
        List l = this.template.query(sqlStr,paramt, new MapRowMapper());
        Date endTime = new Date();
        long hssj = endTime.getTime() - startTime.getTime();
        return l;
    }

    private String updateSqlTszf(String sql)throws Exception{
        Map map = this.getTszfMap();
        Set<String> keySet = map.keySet();
        Iterator<String> keyIt = keySet.iterator();
        while (keyIt.hasNext()) {
            String key = keyIt.next();
            String values = (String)map.get(key);
            if(sql.contains(key)){
                Pattern p = Pattern.compile(key, Pattern.CASE_INSENSITIVE);
                Matcher m = p.matcher(sql);
                sql = m.replaceAll(values);
            }
        }
        return sql;

    }

    public Map getTszfMap() throws Exception{
        Map<String, String> map = new HashMap<String, String>();
        List<Map> listtszf = this.execSqlQueryToMap("select dmmc,dmmc1 from zzdtszf");
        if(listtszf!=null&&listtszf.size()>0){
            for(int i=0;i<listtszf.size();i++){
                Map maptszf = listtszf.get(i);
                String dmmc = (String)maptszf.get("dmmc");
                String dmmc1 = (String)maptszf.get("dmmc1");
                map.put(dmmc,dmmc1);
            }
        }
        return map;
    }
}
