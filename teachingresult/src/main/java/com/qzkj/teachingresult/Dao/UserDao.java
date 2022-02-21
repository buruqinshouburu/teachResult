package com.qzkj.teachingresult.Dao;

import com.qzkj.teachingresult.Pojo.T_Review;
import com.qzkj.teachingresult.Pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface UserDao extends JpaRepository<User,Integer> {
    User findByid(String id);
    List<User> findByUsername(String username);

}
