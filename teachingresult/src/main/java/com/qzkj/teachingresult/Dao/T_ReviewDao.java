package com.qzkj.teachingresult.Dao;


import com.qzkj.teachingresult.Pojo.T_Review;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface T_ReviewDao extends JpaRepository<T_Review,Integer> {

    List<T_Review> findByid(String id);

    List<T_Review> findByTRItemid(String tritemid, Sort sort);

    List<T_Review> findByTRItemid(String tritemid);

    List<T_Review> findByTRItemidAndPsr(String tritemid,String psr);



}
