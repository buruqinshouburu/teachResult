package com.qzkj.teachingresult.Dao;

import com.qzkj.teachingresult.Pojo.TRItem;
import com.qzkj.teachingresult.Pojo.T_Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TRItemDao extends JpaRepository<TRItem,Integer> {

    @Modifying
    Integer deleteById(String Id);
    @Modifying
    @Transactional
    @Query("delete  from TRItem s  where  s.id in (:ids) ")
    Integer deleteTRItemById(@Param("ids") List<String> ids);
}
