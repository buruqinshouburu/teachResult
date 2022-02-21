package com.qzkj.teachingresult.Util;

import java.util.List;

public class ConvertUtil {
    public static String valBeginning(String sql) {
        sql = sql.trim();
        if (!sql.startsWith("f") && !sql.startsWith("F")) {
            return sql;
        } else {
            StringBuffer sb = new StringBuffer();
            sb.append("select * ");
            sb.append(sql);
            return sb.toString();
        }
    }

    public static Object[] convertList2Array(List list) {
        if (list != null && list.size() > 0) {
            Object[] obj = new Object[list.size()];
            for (int i = 0; i < list.size(); i++) {
                obj[i] = list.get(i);
            }
            return obj;
        }
        return null;
    }
}
