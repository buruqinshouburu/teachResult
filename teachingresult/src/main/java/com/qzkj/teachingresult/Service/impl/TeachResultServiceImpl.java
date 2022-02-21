package com.qzkj.teachingresult.Service.impl;

import com.alibaba.fastjson.JSONArray;
import com.qzkj.teachingresult.Dao.TRItemDao;
import com.qzkj.teachingresult.Dao.T_ReviewDao;
import com.qzkj.teachingresult.Dao.UserDao;
import com.qzkj.teachingresult.Dao.impl.OperateDaoimpl;
import com.qzkj.teachingresult.Pojo.TRItem;
import com.qzkj.teachingresult.Pojo.T_Review;
import com.qzkj.teachingresult.Pojo.User;
import com.qzkj.teachingresult.Result.Result;
import com.qzkj.teachingresult.Service.TeachResultService;
import com.qzkj.teachingresult.Util.Sequence;
import jxl.Sheet;
import jxl.Workbook;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;

@Service
public class TeachResultServiceImpl implements TeachResultService {
    @Autowired
    private OperateDaoimpl OrgDao;
    @Autowired
    private TRItemDao trItemDao;
    @Autowired
    private T_ReviewDao T_ReviewDao;
    @Autowired
    private UserDao userdao;
    @Override
    public Result impTRData(MultipartFile file) throws Exception {
        String msg="";
        try {
            InputStream inputStream = file.getInputStream();
            StringBuffer result = new StringBuffer();
            Workbook workbook = Workbook.getWorkbook(inputStream);
            Sheet sheet = workbook.getSheet(0);
            int rows = sheet.getRows();
            ArrayList<TRItem> params = new ArrayList<>();
            for (int i = 2; i < rows; i++) {
                TRItem trItem = new TRItem();
                String cgmc = sheet.getCell(1, i).getContents().trim();//成果名称
                String zywcr = sheet.getCell(2, i).getContents().trim();//主要完成人
                String chnrlb = sheet.getCell(3, i).getContents().trim();//成果内容类别
                String dw = sheet.getCell(4, i).getContents().trim();//单位
                String xypx = sheet.getCell(5, i).getContents().trim();//学院排序
                Map<String,Object> paramList = new HashMap<String,Object>();
                paramList.put("dw",dw);
                paramList.put("xypx",xypx);
                List list = OrgDao.execSqlQueryToMap("select * from tritem where dw=:dw and xypx=:xypx", paramList);
                if(list.size()>0&&list!=null){
                    msg+="第"+i+"行导入失败，该学院排名出现冲突";
                    continue;
                }
                trItem.setId(Sequence.getInstance().getSequence(32));
                trItem.setCgmc(cgmc);
                trItem.setZywcr(zywcr);
                trItem.setChnrlb(chnrlb);
                trItem.setDw(dw);
                trItem.setXypx(xypx);
                params.add(trItem);
            }
            List<TRItem> trItems = trItemDao.saveAll(params);
            msg+="导入成功，共导入"+trItems.size()+"条待评审数据";
        } catch (Exception e) {
            e.printStackTrace();
            msg="导入失败，请检查导入文件是否正确，或联系开发人员";
            throw e;
        }
        return new Result(200,msg);
    }

    @Override
    public void downloadTemplate(HttpServletRequest request,HttpServletResponse response) throws Exception {
        OutputStream output = null;
        InputStream fis = null;
        String filename="待评审项目导入模板.xls";
        try {
            String contentType = response.getContentType();
            Resource resource = new DefaultResourceLoader().getResource("classpath:template/jxcg.xls");
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
            fis = resource.getInputStream();
            output = response.getOutputStream();
            IOUtils.copy(fis, output);
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null!=fis) {
                fis.close();
            }
            if (null!=output) {
                output.close();
            }
        }
    }

    public List<Map> checkItem(String psr) throws Exception {
        return OrgDao.execSqlQueryToMap("select  t.*,r.pf,r.xgyj,r.istj from tritem t  left join t_review r on t.id=r.tritemid and r.psr='"+psr+"'");
    }

    @Override
    public void saveReview(T_Review review) throws Exception {
        // 查找该条项目是否已被评分
        List<T_Review> oldList = T_ReviewDao.findByTRItemidAndPsr(review.getTRItemid(), review.getPsr());
        if(oldList.size()>0&&oldList!=null){
            T_Review t_review = oldList.get(0);
            t_review.setPf(review.getPf());
            t_review.setXgyj(review.getXgyj());
            T_ReviewDao.save(t_review);
        }else{
            review.setId(Sequence.getInstance().getSequence(32));
            T_ReviewDao.save(review);
        }
    }

    @Override
    public List<TRItem> staticScore() throws Exception {
        //查询所有教学成果项目
        List<TRItem> trItems = trItemDao.findAll();
        for (TRItem trItem : trItems) {
            //查询对应成果项目的评分
            List<T_Review> reviews = T_ReviewDao.findByTRItemid(trItem.getId());
            ArrayList<T_Review> t_reviewTj = new ArrayList<>();
            for (T_Review tReview : reviews) {
                if("1".equals(tReview.getIstj())){
                    t_reviewTj.add(tReview);
                }
            }
            //计算平均分与绝对平均分
            staticAvg(t_reviewTj,trItem);
        }
        //计算推荐排序
        return staticOrder(trItems);
    }

    @Override
    public List<TRItem> findStatic() throws Exception {
        return null;
    }

    @Override
    public List<TRItem> checkStatic() throws Exception {
        //查询所有教学成果项目
        List<TRItem> tritemAll = trItemDao.findAll(Sort.by(Sort.Direction.ASC,"dw","xypx"));
            for (TRItem trItem : tritemAll) {
                //查询项目下所有评分
                ArrayList<T_Review> t_reviewTj = new ArrayList<>();
                List<T_Review> t_review = T_ReviewDao.findByTRItemid(trItem.getId(),Sort.by("psr"));
                for (T_Review tReview : t_review) {
                    if("1".equals(tReview.getIstj())){
                        t_reviewTj.add(tReview);
                    }
                }
                trItem.setReviewList(t_reviewTj);
            }

        return tritemAll;
    }

    /**
     * 登录信息检查
     * @param username
     * @param userpassword
     * @param session
     * @throws Exception
     */
    @Override
    public Result checkLogin(String username, String userpassword, HttpSession session) throws Exception {
        //检查用户是否已注册
        Map<String,Object> param=new HashMap<>();
        Result result = new Result(200);
        List<User> list = userdao.findByUsername(username);
        if(list.size()>0&&list!=null){
            //帐号存在
            User user = list.get(0);
            if(!userpassword.equals(user.getPassword())){
                result.setCode(400);
                result.setMsg("帐号密码错误,请重新输入");
            }else{
                result.setMsg(JSONArray.toJSONString(user));
            }
        }else{
            //帐号不存在
            User user = new User();
            user.setId(Sequence.getInstance().getSequence(32));
            user.setUsername(username);
            user.setPassword(userpassword);
            user = userdao.save(user);
            result.setMsg(JSONArray.toJSONString(user));
        }
        return result;
    }

    @Override
    @Transactional
    public Result delTRItem(String trItemid) throws Exception {
        Result result = new Result(200);
        //检查该成果是否已有评分
        List<T_Review> reviewsList = T_ReviewDao.findByTRItemid(trItemid);
        if (reviewsList.size()>0&&reviewsList!=null){
            result.setCode(200);
            result.setMsg("改教学成果已经开始评价,无法删除!");
        }else{
            trItemDao.deleteById(trItemid);
            result.setCode(200);
            result.setMsg("删除成功");
        }

        return result;
    }

    @Override
    public Result toSend(String psr) throws Exception {
        Result result = new Result(200);
        //检查是否全部评分
        List list = OrgDao.execSqlQueryToMap("select  t.*,r.pf,r.xgyj,r.istj from tritem t  left join t_review r on t.id=r.tritemid and r.psr='" + psr + "' where r.pf is null");
        if(list.size()>0&&list!=null){
            result.setCode(200);
            result.setMsg("您还有"+list.size()+"项未评完,请全部评价结束后再提交");
        }else{
            Map<String,Object> param=new HashMap<>();
            OrgDao.operateData("update t_review set istj='1' where psr='"+psr+"'",param);
            result.setCode(200);
            result.setMsg("提交成功");
        }
        return result;
}

    /**
     * 教学成果评价统计打印
     * @throws Exception
     */
    @Override
    public void print(HttpServletRequest request,HttpServletResponse response) throws Exception {
        //获取数据
        List<TRItem> trItems = this.checkStatic();
        int psrCount=trItems.get(0).getReviewList().size();

        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("教学成果评价统计");
        HSSFPrintSetup ps = sheet.getPrintSetup();
        ps.setPaperSize(ps.A4_PAPERSIZE);



        //表头字体
        HSSFFont font1 = wb.createFont();
        font1.setColor(HSSFColor.BLACK.index);
        font1.setFontHeight((short)300);
        font1.setFontName("宋体");
        //正文字体
        HSSFFont font2 = wb.createFont();
        font2.setColor(HSSFColor.BLACK.index);
        font2.setFontHeight((short)200);
        font2.setFontName("宋体");

        //表头样式
        HSSFCellStyle cellstyle1=wb.createCellStyle();
        cellstyle1.setFont(font1);
        cellstyle1.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellstyle1.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellstyle1.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellstyle1.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellstyle1.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        //正文样式
        HSSFCellStyle cellstyle2=wb.createCellStyle();
        cellstyle2.setFont(font2);
        cellstyle2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellstyle2.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellstyle2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellstyle2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellstyle2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellstyle2.setWrapText(true);


        sheet.setColumnWidth(0, 7000);
        sheet.setColumnWidth(1, 3000);

        HSSFRow row0 =  sheet.createRow(0);
        HSSFCell cell0 = row0.createCell(0);
        cell0.setCellStyle(cellstyle1);
        cell0.setCellValue("成果名称");

        HSSFCell cell1 = row0.createCell(1);
        cell1.setCellValue("学院排序");
        cell1.setCellStyle(cellstyle1);

        for (int i = 0; i < psrCount; i++) {
            sheet.setColumnWidth(i+2, 3000);

            HSSFCell cell2 = row0.createCell(i+2);
            cell2.setCellValue("***");
            cell2.setCellStyle(cellstyle1);
        }
        sheet.setColumnWidth(2+psrCount, 3000);
        HSSFCell cell3 = row0.createCell(2+psrCount);
        cell3.setCellValue("平均分");
        cell3.setCellStyle(cellstyle1);

        sheet.setColumnWidth(3+psrCount, 3000);
        HSSFCell cell4 = row0.createCell(3+psrCount);
        cell4.setCellValue("推荐排序");
        cell4.setCellStyle(cellstyle1);

        sheet.setColumnWidth(4+psrCount, 5000);
        HSSFCell cell5 = row0.createCell(4+psrCount);
        cell5.setCellValue("绝对平均分");
        cell5.setCellStyle(cellstyle1);


        HashMap<Integer, Double> personAvg = new HashMap<>();
        for (int i = 0; i < trItems.size(); i++) {
            TRItem trItem = trItems.get(i);
            HSSFRow row = sheet.createRow(i + 1);

            HSSFCell cell6 = row.createCell(0);
            cell6.setCellValue(trItem.getCgmc());
            cell6.setCellStyle(cellstyle2);

            HSSFCell cell7 = row.createCell(1);
            cell7.setCellValue(trItem.getXypx());
            cell7.setCellStyle(cellstyle2);

            for (int k = 0; k < trItem.getReviewList().size(); k++) {
                T_Review t_review = trItem.getReviewList().get(k);
                if(personAvg.containsKey(k+2)){
                    Double score = personAvg.get(k+2);
                    score+=t_review.getPf();
                    personAvg.put(k+2,score);
                }else{
                    personAvg.put(k+2,t_review.getPf());
                }
                HSSFCell cell2 = row.createCell(k+2);
                cell2.setCellValue(t_review.getPf());
                cell2.setCellStyle(cellstyle2);
            }


            HSSFCell cell8 = row.createCell(2+psrCount);
            cell8.setCellValue(trItem.getAvgScore());
            cell8.setCellStyle(cellstyle2);

            HSSFCell cell9 = row.createCell(3+psrCount);
            cell9.setCellValue(trItem.getOrder());
            cell9.setCellStyle(cellstyle2);

            HSSFCell cell10 = row.createCell(4+psrCount);
            cell10.setCellValue(trItem.getAbsAvgScore());
            cell10.setCellStyle(cellstyle2);
        }
        HSSFRow row1 = sheet.createRow(trItems.size()+1);
        Set<Integer> columns = personAvg.keySet();
        for (Integer column : columns) {
            HSSFCell cell11 = row1.createCell(column);
            Double personScore = personAvg.get(column);
            cell11.setCellValue(personScore/trItems.size());
            cell11.setCellStyle(cellstyle2);
        }


        OutputStream output = null;
        InputStream fis = null;
        try {
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("教学成果评价统计", "UTF-8")+".xls");
            output = response.getOutputStream();
            wb.write(output);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (null!=fis) {
                fis.close();
            }
            if (null!=output) {
                output.close();
            }
        }
    }

    private List<TRItem> staticOrder(List<TRItem> trItems) throws Exception {
        Collections.sort(trItems, new Comparator<TRItem>() {
            @Override
            public int compare(TRItem o1, TRItem o2) {
                if(o1.getAvgScore()>o2.getAvgScore()){
                    return -1;
                }else if (o1.getAvgScore()==o2.getAvgScore()){
                     return 0;
                }else{
                    return 1;
                }
            }
        });
        int sort=1;
        for (TRItem trItem : trItems) {
            trItem.setOrder(sort);
            sort++;
        }
        trItemDao.saveAll(trItems);
        return trItems;
    }

    /**
     * 计算平均成绩
     * @param reviews
     * @param trItem
     */
    private void staticAvg(List<T_Review> reviews, TRItem trItem) {
        if(reviews!=null&&reviews.size()>0){
            Double minScore=reviews.get(0).getPf();
            Double maxScore=minScore;
            Double totalScore=0.0;
            for (T_Review review : reviews) {
                totalScore+=review.getPf();
                if(minScore>review.getPf()){
                    minScore=review.getPf();
                }
                if(maxScore<review.getPf()){
                    maxScore=review.getPf();
                }
            }
            if(reviews.size()>3){
                Double lessScore=totalScore-minScore-maxScore;
                trItem.setAvgScore(lessScore/(reviews.size()-2));
            }else{
                trItem.setAvgScore(totalScore/reviews.size());
            }
            trItem.setAbsAvgScore(totalScore/reviews.size());
        }
    }
}
