package com.qzkj.teachingresult.Dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface operateDao {
    public int operateData(String sql, Map<String, Object> param) throws Exception;

    List execSqlQueryToMap(String var1) throws Exception;

    public List execSqlQueryToMap(String sqlStr,Map<String,Object> param) throws Exception;

}
