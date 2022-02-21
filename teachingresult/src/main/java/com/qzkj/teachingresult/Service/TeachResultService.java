package com.qzkj.teachingresult.Service;

import com.qzkj.teachingresult.Pojo.TRItem;
import com.qzkj.teachingresult.Pojo.T_Review;
import com.qzkj.teachingresult.Result.Result;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface TeachResultService {
    public Result impTRData(MultipartFile file) throws Exception;

    public void downloadTemplate(HttpServletRequest request, HttpServletResponse response) throws Exception;

    public List<Map> checkItem(String psr) throws Exception;

    public void saveReview(T_Review reviews) throws Exception;

    public List<TRItem> staticScore() throws Exception;

    public List<TRItem> findStatic() throws Exception;

    public List<TRItem> checkStatic() throws Exception;

    public Result checkLogin(String username, String userpassword, HttpSession session) throws Exception;

    public Result delTRItem(String trItemid) throws Exception;

    public Result toSend(String psr) throws Exception;

    public void print(HttpServletRequest request,HttpServletResponse response) throws Exception;
}
