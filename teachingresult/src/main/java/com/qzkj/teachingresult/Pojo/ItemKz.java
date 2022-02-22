package com.qzkj.teachingresult.Pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * 教学成果项目提交时间控制
 */
@Entity
@Table(name = "Item_Kz")
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer" })
@DynamicInsert(value = true)
@DynamicUpdate(value=true)
public class ItemKz {
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private String id ;
    /**
     * 成果名称
     */
    @Column
    private String tjkssj ;
    /**
     * 主要完成人
     */
    @Column
    private String tjjssj ;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTjkssj() {
        return tjkssj;
    }

    public void setTjkssj(String tjkssj) {
        this.tjkssj = tjkssj;
    }

    public String getTjjssj() {
        return tjjssj;
    }

    public void setTjjssj(String tjjssj) {
        this.tjjssj = tjjssj;
    }
}
