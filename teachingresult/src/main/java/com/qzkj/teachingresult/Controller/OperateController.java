package com.qzkj.teachingresult.Controller;

import com.alibaba.fastjson.JSON;
import com.qzkj.teachingresult.Pojo.*;
import com.qzkj.teachingresult.Result.Result;
import com.qzkj.teachingresult.Service.impl.TeachResultServiceImpl;
import com.qzkj.teachingresult.Util.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/imp")
@CrossOrigin
public class OperateController {
    @Autowired
    private TeachResultServiceImpl teachResultService;
    @Autowired
    private HttpServletRequest request;

    /**
     * 成果项目导入
     * @param multipartFile
     * @param request
     * @return
     */
    @CrossOrigin
    @PostMapping(value = "/impTRData" , consumes = { "multipart/mixed", "multipart/form-data" }, produces = { "application/json;charset=UTF-8" })
    @ResponseBody
    public Result impTRData(@RequestPart(value = "file",required = false) MultipartFile multipartFile,  HttpServletRequest request) {
       Result result=new Result(200,"");
        try {
            if(multipartFile==null||!multipartFile.isEmpty()){
                 result = teachResultService.impTRData(multipartFile);
            }else{
                return new Result(400);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  result;
    }

    /**
     * 成果项目模版下载
     * @param request
     * @param response
     */
    @PostMapping(value = "/downloadTemplate" )
    @ResponseBody
    @CrossOrigin
    public void downloadTemplate(HttpServletRequest request, HttpServletResponse response){
        try {
            teachResultService.downloadTemplate(request,response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取评分项目
     * @return
     */
    @PostMapping(value = "/checkItem" )
    @ResponseBody
    @CrossOrigin
    public String checkItem(String userid){
        String data="";
        try {
            List<Map> trItems = teachResultService.checkItem(userid);
            data=JSON.toJSONString(trItems);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * 评分保存
     * @param review
     * @return
     */
    @PostMapping(value = "/saveScore" )
    @ResponseBody
    @CrossOrigin
    public Result saveScore(T_Review review){
        try {
            teachResultService.saveReview(review);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(400);
        }
        return new Result(200);
    }
    /**
     * 删除教学成果项目
     */
    @PostMapping(value = "/delTRItem" )
    @ResponseBody
    @CrossOrigin
    public Result delTRItem(String TRItemid){
        try {
            return  teachResultService.delTRItem(TRItemid);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(400);
        }
    }
    /**
     * 评分提交
     */
    @PostMapping(value = "/toSend" )
    @ResponseBody
    @CrossOrigin
    public Result toSend(String psr){
        try {
            return  teachResultService.toSend(psr);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(400);
        }
    }
    /**
     * 分数统计
     * @return
     */
    @PostMapping(value = "/staticScore" )
    @ResponseBody
    @CrossOrigin
    public Result staticScore(){
        String data="";
        try {
            List<TRItem> trItems = teachResultService.staticScore();
            data=JSON.toJSONString(trItems);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(200,"统计成功");
    }
    /**
     * 查询分数统计
     * @return
     */
    @PostMapping(value = "/checkStatic" )
    @ResponseBody
    @CrossOrigin
    public String checkStatic(String psr){
        String type = request.getParameter("type");
        String data="";
        try {
            ArrayList<TRItem> trItemsList = new ArrayList<>();
            List<TRItem> trItems = teachResultService.checkStatic(psr);
            for (TRItem trItem : trItems) {
                String cgnrlb = trItem.getChnrlb();
                if(!StringUtils.nullOrBlank(type)&&!type.equals(cgnrlb.substring(cgnrlb.length()-1,cgnrlb.length()))){
                    continue;
                }
                trItemsList.add(trItem);
            }
            data=JSON.toJSONString(trItemsList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * 教学成果评价统计打印
     * @return
     */
    @PostMapping(value = "/printItem" )
    @ResponseBody
    @CrossOrigin
    public void printItem(HttpServletRequest request,HttpServletResponse response){
        try {
            teachResultService.print(request,response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 教学成果评价批量删除
     * @return
     */
    @PostMapping(value = "/delAllTRItem" )
    @ResponseBody
    @CrossOrigin
    public Result delAllTRItem(String tritemids){
        Result result = new Result(200);
        try {
            result =teachResultService.delAllTritem(tritemids);
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(400);
        }
        return result;
    }

    /**
     * 教学成果评价提交时间控制
     * @return
     */
    @PostMapping(value = "/saveControlTime" )
    @ResponseBody
    @CrossOrigin
    public Result saveControlTime(ItemKz itemKz){
        Result result = new Result(200);
        try {
            result =teachResultService.saveItemKz(itemKz);
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(400);
        }
        return result;
    }

    /**
     * 教学成果评价提交时间查询
     * @return
     */
    @PostMapping(value = "/checkItemKz" )
    @ResponseBody
    @CrossOrigin
    public Result checkItemKz(){
        Result result = new Result(200);
        try {
            result = teachResultService.checkItemKz();
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(400);
        }
        return result;
    }

    /**
     * 教学成果评价提交取消
     * @return
     */
    @PostMapping(value = "/toCancelSend" )
    @ResponseBody
    @CrossOrigin
    public Result toCancelSend(String psr){
        Result result = new Result(200);
        try {
            result = teachResultService.cancelSend(psr);
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(400);
        }
        return result;
    }

    /**
     * 教学成果材料上传
     * @return
     */
    @PostMapping(value = "/uploadFile" )
    @ResponseBody
    @CrossOrigin
    public Result uploadFile(@RequestPart(value = "file",required = false) MultipartFile multipartFile,  HttpServletRequest request,HttpServletResponse response){
        Result result=new Result(200,"");
        try {
            if(multipartFile==null||!multipartFile.isEmpty()){
                result = teachResultService.uploadFile(multipartFile,request,response);
            }else{
                return new Result(400);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(400);
        }
        return result;
    }

    /**
     * 教学成果材料查询
     * @return
     */
    @PostMapping(value = "/checkFile" )
    @ResponseBody
    @CrossOrigin
    public List<ItemFile> checkFile(){
        try {
            return teachResultService.checkFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 教学成果材料下载
     * @return
     */
    @PostMapping(value = "/downloadFile" )
    @ResponseBody
    @CrossOrigin
    public void downloadFile(String fileid,HttpServletRequest request, HttpServletResponse response){
        try {
             teachResultService.downloadFile(fileid, request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
