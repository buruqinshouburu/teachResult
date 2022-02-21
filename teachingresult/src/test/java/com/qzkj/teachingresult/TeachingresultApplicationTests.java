package com.qzkj.teachingresult;

import com.qzkj.teachingresult.Dao.operateDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class TeachingresultApplicationTests {

    @Autowired
    private operateDao operateDao;

    @Test
    void contextLoads() {
        try {
            String sql="insert into t_result_item (id,cgmc,zywcr,chnrlb,dw,xypx) values(:id,:cgmc,:zywcr,:chnrlb,:dw,:xypx)  ";
            Map<String,Object> param=new HashMap<>();
            param.put("id","1");
            param.put("cgmc","以“三个面向”为牵引，构建大学数学“四化两性”教育体系");
            param.put("zywcr","李建平,朱建民,杨文强,屈龙江,王晓,黄建华,周敏,刘吉英,刘春林");
            param.put("chnrlb","");
            param.put("dw","文理学院");
            param.put("xypx","1");
            int i = operateDao.operateData(sql, param);
            System.out.println(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
