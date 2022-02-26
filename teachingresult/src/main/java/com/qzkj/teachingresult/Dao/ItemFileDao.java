package com.qzkj.teachingresult.Dao;

import com.qzkj.teachingresult.Pojo.ItemFile;
import com.qzkj.teachingresult.Pojo.T_Review;
import com.qzkj.teachingresult.Pojo.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ItemFileDao extends JpaRepository<ItemFile,Integer> {

    List<ItemFile> findAll(Sort sort);

    ItemFile findByFileid(String fileid);
}
