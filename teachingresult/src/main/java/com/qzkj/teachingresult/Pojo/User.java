package com.qzkj.teachingresult.Pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * 用户表
 */
@Entity
@Table(name = "User")
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer" })
@DynamicInsert(value = true)
@DynamicUpdate(value=true)
public class User {
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private String id;
    /**
     * 用户名
     */
    @Column
    private String username;
    /**
     * 用户密码
     */
    @Column
    private String password;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
