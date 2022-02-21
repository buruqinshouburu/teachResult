package com.qzkj.teachingresult.Pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.List;

/**
 * 教学成果项目表
 */
@Entity
@Table(name = "TRItem")
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer" })
public class TRItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private String id ;
    /**
     * 成果名称
     */
    private String cgmc ;
    /**
     * 主要完成人
     */
    private String zywcr ;
    /**
     * 成果内容类别
     */
    private String chnrlb ;
    /**
     * 单位
     */
    private String dw ;
    /**
     * 学院排序
     */
    private String xypx ;
    /**
     * 平均分
     */
    private Double avgScore;
    /**
     * 绝对平均分
     */
    private Double absAvgScore;
    /**
     * 推荐排序
     */
    private Integer itemorder;

    @Transient
    private List<T_Review> reviewList;

    public List<T_Review> getReviewList() {
        return reviewList;
    }

    public Integer getItemorder() {
        return itemorder;
    }

    public void setItemorder(Integer itemorder) {
        this.itemorder = itemorder;
    }

    public void setReviewList(List<T_Review> reviewList) {
        this.reviewList = reviewList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCgmc() {
        return cgmc;
    }

    public void setCgmc(String cgmc) {
        this.cgmc = cgmc;
    }

    public String getZywcr() {
        return zywcr;
    }

    public void setZywcr(String zywcr) {
        this.zywcr = zywcr;
    }

    public String getChnrlb() {
        return chnrlb;
    }

    public void setChnrlb(String chnrlb) {
        this.chnrlb = chnrlb;
    }

    public String getDw() {
        return dw;
    }

    public void setDw(String dw) {
        this.dw = dw;
    }

    public String getXypx() {
        return xypx;
    }

    public void setXypx(String xypx) {
        this.xypx = xypx;
    }

    public Double getAvgScore() {
        return avgScore;
    }

    public void setAvgScore(Double avgScore) {
        this.avgScore = avgScore;
    }

    public Double getAbsAvgScore() {
        return absAvgScore;
    }

    public void setAbsAvgScore(Double absAvgScore) {
        this.absAvgScore = absAvgScore;
    }

    public Integer getOrder() {
        return itemorder;
    }

    public void setOrder(Integer order) {
        this.itemorder = order;
    }

    @Override
    public String toString() {
        return "TRItem{" +
                "id='" + id + '\'' +
                ", cgmc='" + cgmc + '\'' +
                ", zywcr='" + zywcr + '\'' +
                ", chnrlb='" + chnrlb + '\'' +
                ", dw='" + dw + '\'' +
                ", xypx='" + xypx + '\'' +
                ", avgScore=" + avgScore +
                ", absAvgScore=" + absAvgScore +
                ", itemorder=" + itemorder +
                '}';
    }
}
