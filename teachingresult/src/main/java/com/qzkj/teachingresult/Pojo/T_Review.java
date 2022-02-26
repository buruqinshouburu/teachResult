package com.qzkj.teachingresult.Pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * 教学成果评分表
 */
@Entity
@Table(name = "T_Review")
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer" })
@DynamicInsert(value = true)
@DynamicUpdate(value=true)
public class T_Review {
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private String id ;
    /**
     * 教学成果ID
     */
    @Column
    private String TRItemid;
    /**
     * 评审人ID
     */
    @Column
    private String psr;
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
     * 是否提交 1:是 2:否
     */
    @Column
    private String istj;

    @Transient
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getIstj() {
        return istj;
    }

    public void setIstj(String istj) {
        this.istj = istj;
    }

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

    public String getPsr() {
        return psr;
    }

    public void setPsr(String psr) {
        this.psr = psr;
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

    @Override
    public String toString() {
        return "T_Review{" +
                "id='" + id + '\'' +
                ", TRItemid='" + TRItemid + '\'' +
                ", psr='" + psr + '\'' +
                ", pf=" + pf +
                ", xgyj='" + xgyj + '\'' +
                '}';
    }
}
