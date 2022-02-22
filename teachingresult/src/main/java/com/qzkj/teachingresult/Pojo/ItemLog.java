package com.qzkj.teachingresult.Pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * 教学成果项目提交日志
 */
@Entity
@Table(name = "Item_Log")
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer" })
@DynamicInsert(value = true)
@DynamicUpdate(value=true)
public class ItemLog {
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private String id;
    /**
     * 提交时间
     */
    @Column
    private String tjsj;
    /**
     * 提交人
     */
    @Column
    private String tjr;
    /**
     * 撤回时间
     */
    @Column
    private String chsj;
    /**
     * 撤回人
     */
    @Column
    private String chr;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTjsj() {
        return tjsj;
    }

    public void setTjsj(String tjsj) {
        this.tjsj = tjsj;
    }

    public String getTjr() {
        return tjr;
    }

    public void setTjr(String tjr) {
        this.tjr = tjr;
    }

    public String getChsj() {
        return chsj;
    }

    public void setChsj(String chsj) {
        this.chsj = chsj;
    }

    public String getChr() {
        return chr;
    }

    public void setChr(String chr) {
        this.chr = chr;
    }
}
