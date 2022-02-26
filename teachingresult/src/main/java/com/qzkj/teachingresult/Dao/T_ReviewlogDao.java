package com.qzkj.teachingresult.Dao;


import com.qzkj.teachingresult.Pojo.T_Review;
import com.qzkj.teachingresult.Pojo.T_Review_Log;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface T_ReviewlogDao extends JpaRepository<T_Review_Log,Integer> {


}
