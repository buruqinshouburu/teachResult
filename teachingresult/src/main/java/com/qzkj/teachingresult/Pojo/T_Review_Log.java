package com.qzkj.teachingresult.Pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * 教学成果评分表
 */
@Entity
@Table(name = "T_Review_Log")
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer" })
@DynamicInsert(value = true)
@DynamicUpdate(value=true)
public class T_Review_Log {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private String id ;
    /**
     * 教学成果ID
     */
    @Column
    private String TRItemid;
    /**
     * 评分
     */
    @Column
    private Double pf;
    /**
     * 修改意见
     */
    @Column
    private String xgyj;

    /**
     * itemLog的ID
     */
    @Column
    private String logid;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTRItemid() {
        return TRItemid;
    }

    public void setTRItemid(String TRItemid) {
        this.TRItemid = TRItemid;
    }


    public Double getPf() {
        return pf;
    }

    public void setPf(Double pf) {
        this.pf = pf;
    }

    public String getXgyj() {
        return xgyj;
    }

    public void setXgyj(String xgyj) {
        this.xgyj = xgyj;
    }

    public String getLogid() {
        return logid;
    }

    public void setLogid(String logid) {
        this.logid = logid;
    }

}
