package com.qzkj.teachingresult.Service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.qzkj.teachingresult.Dao.*;
import com.qzkj.teachingresult.Dao.ItemFileDao;
import com.qzkj.teachingresult.Dao.impl.OperateDaoimpl;
import com.qzkj.teachingresult.Dao.T_ReviewlogDao;
import com.qzkj.teachingresult.Pojo.*;
import com.qzkj.teachingresult.Result.Result;
import com.qzkj.teachingresult.Service.TeachResultService;
import com.qzkj.teachingresult.Util.Sequence;
import com.qzkj.teachingresult.Util.StringUtils;
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
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    @Autowired
    private ItemKzDao itemkzdao;
    @Autowired
    private ItemLogDao itemlogdao;
    @Autowired
    private T_ReviewlogDao t_reviewlogdao;
    @Autowired
    private ItemFileDao itemfiledao;

    @Override
    public Result impTRData(MultipartFile file) throws Exception {
        String msg = "";
        try {
            InputStream inputStream = file.getInputStream();
            StringBuffer result = new StringBuffer();
            Workbook workbook = Workbook.getWorkbook(inputStream);
            Sheet sheet = workbook.getSheet(0);
            int rows = sheet.getRows();
            ArrayList<TRItem> params = new ArrayList<>();
            for (int i = 2; i < rows; i++) {
                TRItem trItem = new TRItem();
                String cgmc = sheet.getCell(1, i).getContents().trim();//????????????
                String zywcr = sheet.getCell(2, i).getContents().trim();//???????????????
                String chnrlb = sheet.getCell(3, i).getContents().trim();//??????????????????
                String dw = sheet.getCell(4, i).getContents().trim();//??????
                String xypx = sheet.getCell(5, i).getContents().trim();//????????????
                Map<String, Object> paramList = new HashMap<String, Object>();
                paramList.put("dw", dw);
                paramList.put("xypx", xypx);
                List list = OrgDao.execSqlQueryToMap("select * from tritem where dw=:dw and xypx=:xypx", paramList);
                if (list.size() > 0 && list != null) {
                    msg += "???" + i + "?????????????????????????????????????????????";
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
            msg += "????????????????????????" + trItems.size() + "??????????????????";
        } catch (Exception e) {
            e.printStackTrace();
            msg = "????????????????????????????????????????????????????????????????????????";
            throw e;
        }
        return new Result(200, msg);
    }

    @Override
    public void downloadTemplate(HttpServletRequest request, HttpServletResponse response) throws Exception {
        OutputStream output = null;
        InputStream fis = null;
        String filename = "???????????????????????????.xls";
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
            if (null != fis) {
                fis.close();
            }
            if (null != output) {
                output.close();
            }
        }
    }

    public List<Map> checkItem(String psr) throws Exception {
        List<Map> list = OrgDao.execSqlQueryToMap("select  t.*,r.pf,r.xgyj,r.istj," +
                "nvl((select pjzf from item_kz),0) as yqScore,nvl((select xms from item_kz),0) as xms" +
                " from tritem t  left join t_review r  on t.id=r.tritemid and r.psr='" + psr + "'");
        return list;
    }

    @Override
    public void saveReview(T_Review review) throws Exception {
        // ????????????????????????????????????
        List<T_Review> oldList = T_ReviewDao.findByTRItemidAndPsr(review.getTRItemid(), review.getPsr());
        if (oldList.size() > 0 && oldList != null) {
            T_Review t_review = oldList.get(0);
            t_review.setPf(review.getPf());
            t_review.setXgyj(review.getXgyj());
            T_ReviewDao.save(t_review);
        } else {
            review.setId(Sequence.getInstance().getSequence(32));
            T_ReviewDao.save(review);
        }
    }

    @Override
    public List<TRItem> staticScore() throws Exception {
        //??????????????????????????????
        List<TRItem> trItems = trItemDao.findAll();
        for (TRItem trItem : trItems) {
            //?????????????????????????????????
            List<T_Review> reviews = T_ReviewDao.findByTRItemid(trItem.getId());
            ArrayList<T_Review> t_reviewTj = new ArrayList<>();
            for (T_Review tReview : reviews) {
                if ("1".equals(tReview.getIstj())) {
                    t_reviewTj.add(tReview);
                }
            }
            //?????????????????????????????????
            staticAvg(t_reviewTj, trItem);
        }
        //??????????????????
        return staticOrder(trItems);
    }

    @Override
    public List<TRItem> findStatic() throws Exception {
        return null;
    }

    @Override
    public List<TRItem> checkStatic(String psr) throws Exception {
        //??????????????????????????????
        List<TRItem> tritemAll = trItemDao.findAll(Sort.by(Sort.Direction.ASC, "dw", "xypx"));
        for (TRItem trItem : tritemAll) {
            //???????????????????????????
            ArrayList<T_Review> t_reviewTj = new ArrayList<>();
            List<T_Review> t_review = new ArrayList<>();
            if (!"admin".equals(psr)) {
                t_review = T_ReviewDao.findByTRItemidAndPsr(trItem.getId(), psr, Sort.by("psr"));
            } else {
                t_review = T_ReviewDao.findByTRItemid(trItem.getId(), Sort.by("psr"));
            }

            for (T_Review tReview : t_review) {
                if ("1".equals(tReview.getIstj())) {
                    User user = userdao.findByid(tReview.getPsr());
                    tReview.setUser(user);
                    t_reviewTj.add(tReview);
                }
            }
            trItem.setReviewList(t_reviewTj);
        }

        return tritemAll;
    }

    /**
     * ??????????????????
     *
     * @param username
     * @param userpassword
     * @param session
     * @throws Exception
     */
    @Override
    public Result checkLogin(String username, String userpassword, HttpSession session) throws Exception {
        //???????????????????????????
        Map<String, Object> param = new HashMap<>();
        Result result = new Result(200);
        List<User> list = userdao.findByUsername(username);
        if (list.size() > 0 && list != null) {
            //????????????
            User user = list.get(0);
            if (StringUtils.nullOrBlank(user.getPassword())) {
                user.setPassword(userpassword);
                user = userdao.save(user);
                result.setMsg(JSONArray.toJSONString(user));
            } else {
                if (!userpassword.equals(user.getPassword())) {
                    result.setCode(400);
                    result.setMsg("??????????????????,???????????????");
                } else {
                    result.setMsg(JSONArray.toJSONString(user));
                }
            }
        } else {
            //???????????????
            /*User user = new User();
            user.setId(Sequence.getInstance().getSequence(32));
            user.setUsername(username);
            user.setPassword(userpassword);
            user = userdao.save(user);
            result.setMsg(JSONArray.toJSONString(user));*/
            result.setCode(400);
            result.setMsg("??????????????????,???????????????");
        }
        return result;
    }

    @Override
    @Transactional
    public Result delTRItem(String trItemid) throws Exception {
        Result result = new Result(200);
        //?????????????????????????????????
        List<T_Review> reviewsList = T_ReviewDao.findByTRItemid(trItemid);
        if (reviewsList.size() > 0 && reviewsList != null) {
            result.setCode(200);
            result.setMsg("?????????????????????????????????,????????????!");
        } else {
            trItemDao.deleteById(trItemid);
            result.setCode(200);
            result.setMsg("????????????");
        }

        return result;
    }

    @Override
    @Transactional
    public Result toSend(String psr) throws Exception {
        Result result = new Result(200);
        List<ItemKz> ItemKzList = itemkzdao.findAll();
        if (ItemKzList.size() > 0 && ItemKzList != null) {
            if (checkTime(ItemKzList)) {
                //????????????????????????
                List list = OrgDao.execSqlQueryToMap("select  t.*,r.pf,r.xgyj,r.istj from tritem t  left join t_review r on t.id=r.tritemid and r.psr='" + psr + "' where r.pf is null");
                if (list.size() > 0 && list != null) {
                    result.setCode(200);
                    result.setMsg("?????????" + list.size() + "????????????,?????????????????????????????????");
                } else {
                    Result result1 = checkKz(psr, ItemKzList.get(0));
                    if (result1 != null) {
                        return result1;
                    }
                    Map<String, Object> param = new HashMap<>();
                    OrgDao.operateData("update t_review set istj='1' where psr='" + psr + "'", param);
                    result.setCode(200);
                    result.setMsg("????????????");
                    //????????????
                    ItemLog itemLog = new ItemLog();
                    itemLog.setId(Sequence.getInstance().getSequence(32));
                    itemLog.setTjr(psr);
                    DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String time = fmt.format(new Date());
                    itemLog.setTjsj(time);
                    ItemLog itemlog = itemlogdao.save(itemLog);
                    HashMap<String, Object> params = new HashMap<>();
                    OrgDao.operateData("insert into T_Review_Log(TRItemid,pf,xgyj,logid) (select TRItemid,pf,xgyj,'" + itemlog.getId() + "' from t_review where psr='" + psr + "')", params);
                }
            } else {
                return new Result(200, "?????????????????????????????????");
            }
        } else {
            return new Result(200, "?????????????????????????????????,????????????");
        }

        return result;
    }

    private Result checkKz(String psr, ItemKz itemKz) {
        //??????????????????????????????
        List<T_Review> reviewList = T_ReviewDao.findByPsr(psr);
        Collections.sort(reviewList, new Comparator<T_Review>() {
            @Override
            public int compare(T_Review o1, T_Review o2) {
                if (o1.getPf() > o2.getPf()) {
                    return -1;
                } else if (o1.getPf() == o2.getPf()) {
                    if (StringUtils.nullOrBlank(o1.getXgyj()) && !StringUtils.nullOrBlank(o2.getXgyj())) {
                        return -1;
                    } else {
                        return 1;
                    }
                } else {
                    return 1;
                }
            }
        });
        Double sumScore = 0.0;
        int Xms = Integer.parseInt(itemKz.getXms());
        boolean sameScore = true;
        int index = 1;
        String pf = "0";
        for (T_Review t_review : reviewList) {
            if (index <= Xms && StringUtils.nullOrBlank(t_review.getXgyj())) {
                return new Result(200, "????????????,???????????????" + Xms + "?????????????????????????????????");
            }
            if (index == Xms) {
                pf = t_review.getPf() + "";
            }
            if (index > Xms && sameScore && pf.equals(t_review.getPf() + "")) {
                if (StringUtils.nullOrBlank(t_review.getXgyj())) {
                    return new Result(200, "????????????,???????????????" + Xms + "??????????????????????????????,???" + Xms + "???????????????????????????");
                } else {
                    sameScore = false;
                }
            }
            sumScore += t_review.getPf();
            index++;
        }
        if (!(sumScore == Double.parseDouble(itemKz.getPjzf()))) {
            return new Result(200, "????????????,??????????????????" + itemKz.getPjzf());
        }
        return null;
    }

    /**
     * ??????????????????????????????
     *
     * @throws Exception
     */
    @Override
    public void print(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String type = request.getParameter("type");
        //????????????
        List<TRItem> trItems = this.checkStatic("admin");
        int psrCount = trItems.get(0).getReviewList().size();
        if (trItems.size() == 0 || trItems == null) {
            return;
        }

        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("????????????????????????");
        HSSFPrintSetup ps = sheet.getPrintSetup();
        ps.setPaperSize(ps.A4_PAPERSIZE);


        //????????????
        HSSFFont font1 = wb.createFont();
        font1.setColor(HSSFColor.BLACK.index);
        font1.setFontHeight((short) 300);
        font1.setFontName("??????");
        //????????????
        HSSFFont font2 = wb.createFont();
        font2.setColor(HSSFColor.BLACK.index);
        font2.setFontHeight((short) 200);
        font2.setFontName("??????");

        //????????????
        HSSFCellStyle cellstyle1 = wb.createCellStyle();
        cellstyle1.setFont(font1);
        cellstyle1.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellstyle1.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellstyle1.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellstyle1.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellstyle1.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        //????????????
        HSSFCellStyle cellstyle2 = wb.createCellStyle();
        cellstyle2.setFont(font2);
        cellstyle2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellstyle2.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellstyle2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellstyle2.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellstyle2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellstyle2.setWrapText(true);


        sheet.setColumnWidth(0, 7000);
        sheet.setColumnWidth(1, 4000);
        sheet.setColumnWidth(2, 3000);
        sheet.setColumnWidth(3, 7000);

        HSSFRow row0 = sheet.createRow(0);
        HSSFCell cell0 = row0.createCell(0);
        cell0.setCellStyle(cellstyle1);
        cell0.setCellValue("????????????");

        HSSFCell cell1 = row0.createCell(1);
        cell1.setCellStyle(cellstyle1);
        cell1.setCellValue("???????????????");

        HSSFCell cell2 = row0.createCell(2);
        cell2.setCellStyle(cellstyle1);
        cell2.setCellValue("????????????");

        HSSFCell cell3 = row0.createCell(3);
        cell3.setCellValue("????????????/????????????");
        cell3.setCellStyle(cellstyle1);

        TRItem trItem0 = trItems.get(0);
        for (int i = 0; i < trItem0.getReviewList().size(); i++) {
            T_Review t_review = trItem0.getReviewList().get(i);
            User user = userdao.findByid(t_review.getPsr());
            sheet.setColumnWidth(i + 4, 3000);
            HSSFCell cell4 = row0.createCell(i + 4);
            if ("check".equals(type)) {
                cell4.setCellValue("***");
            } else {
                cell4.setCellValue(user.getUsername());
            }
            cell4.setCellStyle(cellstyle1);
        }
        sheet.setColumnWidth(4 + psrCount, 3000);
        HSSFCell cell5 = row0.createCell(4 + psrCount);
        cell5.setCellValue("?????????");
        cell5.setCellStyle(cellstyle1);

        sheet.setColumnWidth(5 + psrCount, 3000);
        HSSFCell cell6 = row0.createCell(5 + psrCount);
        cell6.setCellValue("????????????");
        cell6.setCellStyle(cellstyle1);

        sheet.setColumnWidth(6 + psrCount, 5000);
        HSSFCell cell7 = row0.createCell(6 + psrCount);
        cell7.setCellValue("???????????????");
        cell7.setCellStyle(cellstyle1);


        HashMap<Integer, Double> personAvg = new HashMap<>();
        for (int i = 0; i < trItems.size(); i++) {
            TRItem trItem = trItems.get(i);
            HSSFRow row = sheet.createRow(i + 1);

            HSSFCell cell8 = row.createCell(0);
            cell8.setCellValue(trItem.getCgmc());
            cell8.setCellStyle(cellstyle2);

            HSSFCell cell9 = row.createCell(1);
            cell9.setCellValue(trItem.getZywcr());
            cell9.setCellStyle(cellstyle2);

            HSSFCell cell10 = row.createCell(2);
            cell10.setCellValue(trItem.getDw());
            cell10.setCellStyle(cellstyle2);

            HSSFCell cell11 = row.createCell(3);
            cell11.setCellValue(trItem.getXypx());
            cell11.setCellStyle(cellstyle2);

            for (int k = 0; k < trItem.getReviewList().size(); k++) {
                T_Review t_review = trItem.getReviewList().get(k);
                if (personAvg.containsKey(k + 2)) {
                    Double score = personAvg.get(k + 2);
                    score += t_review.getPf();
                    personAvg.put(k + 2, score);
                } else {
                    personAvg.put(k + 2, t_review.getPf());
                }
                HSSFCell cell12 = row.createCell(k + 4);
                cell12.setCellValue(t_review.getPf());
                cell12.setCellStyle(cellstyle2);
            }


            HSSFCell cell13 = row.createCell(4 + psrCount);
            cell13.setCellValue(trItem.getAvgScore() == null ? 0.0 : trItem.getAvgScore());
            cell13.setCellStyle(cellstyle2);

            HSSFCell cell14 = row.createCell(5 + psrCount);
            cell14.setCellValue(trItem.getOrder() == null ? 0 : trItem.getOrder());
            cell14.setCellStyle(cellstyle2);

            HSSFCell cell15 = row.createCell(6 + psrCount);
            cell15.setCellValue(trItem.getAbsAvgScore() == null ? 0.0 : trItem.getAbsAvgScore());
            cell15.setCellStyle(cellstyle2);
        }
       /* HSSFRow row1 = sheet.createRow(trItems.size()+1);
        Set<Integer> columns = personAvg.keySet();
        for (Integer column : columns) {
            HSSFCell cell11 = row1.createCell(column);
            Double personScore = personAvg.get(column);
            cell11.setCellValue(personScore/trItems.size());
            cell11.setCellStyle(cellstyle2);
        }*/


        OutputStream output = null;
        InputStream fis = null;
        try {
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("????????????????????????", "UTF-8") + ".xls");
            output = response.getOutputStream();
            wb.write(output);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != fis) {
                fis.close();
            }
            if (null != output) {
                output.close();
            }
        }
    }

    /**
     * ????????????
     *
     * @param tritemids
     * @return
     */
    @Override
    @Transactional
    public Result delAllTritem(String tritemids) throws Exception {
        String[] splitId = tritemids.split(",");

        ArrayList<String> ids = new ArrayList<>();
        for (int i = 0; i < splitId.length; i++) {
            String id = splitId[i];
            ids.add(id);
        }
        Integer integer = this.trItemDao.deleteTRItemById(ids);

        return new Result(200, "??????????????????");
    }

    @Override
    public Result saveItemKz(ItemKz itemKz) throws Exception {
        //????????????????????????
        itemkzdao.deleteAll();
        itemKz.setId(Sequence.getInstance().getSequence(32));
        itemkzdao.save(itemKz);
        return new Result(200, "??????????????????????????????");
    }

    @Override
    public Result checkItemKz() throws Exception {
        List<ItemKz> ItemKzList = itemkzdao.findAll();
        if (ItemKzList.size() > 0 && ItemKzList != null) {
            String msg = JSON.toJSONString(ItemKzList.get(0));
            return new Result(200, msg);
        } else {
            ItemKz itemKz = new ItemKz();
            DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = fmt.format(new Date());
            itemKz.setTjkssj(time);
            itemKz.setTjjssj(time);
            String msg = JSON.toJSONString(itemKz);
            return new Result(200, msg);
        }
    }

    @Override
    public Result cancelSend(String psr) throws Exception {
        List<ItemKz> ItemKzList = itemkzdao.findAll();
        if (ItemKzList.size() > 0 && ItemKzList != null) {
            if (checkTime(ItemKzList)) {
                Map<String, Object> param = new HashMap<>();
                OrgDao.operateData("update t_review set istj='0' where psr='" + psr + "'", param);
                //????????????
                ItemLog itemLog = new ItemLog();
                itemLog.setId(Sequence.getInstance().getSequence(32));
                itemLog.setChr(psr);
                DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String time = fmt.format(new Date());
                itemLog.setChsj(time);
                itemlogdao.save(itemLog);
                return new Result(200, "??????????????????");
            } else {
                return new Result(200, "???????????????????????????");
            }
        } else {
            return new Result(200, "???????????????????????????,??????????????????");
        }
    }

    @Override
    public Result uploadFile(MultipartFile multipartFile, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ItemFile itemFile = new ItemFile();

        String fileName = multipartFile.getOriginalFilename();
        String dirPath = "D:/teachResult/file";
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String[] split = fileName.split("\\.");
        String suffix = split[split.length - 1];
        itemFile.setFileid(Sequence.getInstance().getSequence(32));
        itemFile.setFilename(fileName);
        itemFile.setCjsj(fmt.format(new Date()));


        String filepath = dirPath + "/" + itemFile.getFileid() + "." + suffix;
        itemFile.setFilepath(filepath);

        File file = new File(dirPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        File dest = new File(filepath);

        try {
            multipartFile.transferTo(dest);
            itemfiledao.save(itemFile);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(400,"??????????????????");
        }
        return new Result(200,"??????????????????");
    }

    @Override
    public List<ItemFile> checkFile() throws Exception {
        return itemfiledao.findAll(Sort.by(Sort.Direction.DESC, "cjsj"));

    }

    @Override
    public void downloadFile(String fileid, HttpServletRequest request, HttpServletResponse response) throws Exception {
        //??????????????????
        ItemFile itemfile = itemfiledao.findByFileid(fileid);
        if(itemfile==null||StringUtils.nullOrBlank(itemfile.getFilepath())){
            return;
        }

        InputStream fis=null;
        OutputStream output=null;
        try {
            File file = new File(itemfile.getFilepath());
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(itemfile.getFilename(), "UTF-8"));
            fis = new FileInputStream(file);
            output = response.getOutputStream();
            IOUtils.copy(fis, output);
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != fis) {
                fis.close();
            }
            if (null != output) {
                output.close();
            }
        }

    }

    private Boolean checkTime(List<ItemKz> itemKzList) throws ParseException {
        ItemKz itemKz = itemKzList.get(0);
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date tjkssj = fmt.parse(itemKz.getTjkssj());
        Date tjjssj = fmt.parse(itemKz.getTjjssj());
        Date date = new Date();
        if (date.after(tjkssj) && date.before(tjjssj)) {
            return true;
        } else {
            return false;
        }
    }

    private List<TRItem> staticOrder(List<TRItem> trItems) throws Exception {
        Collections.sort(trItems, new Comparator<TRItem>() {
            @Override
            public int compare(TRItem o1, TRItem o2) {
                if (o1.getAvgScore() > o2.getAvgScore()) {
                    return -1;
                } else if (o1.getAvgScore() == o2.getAvgScore()) {
                    return 0;
                } else {
                    return 1;
                }
            }
        });
        int sort = 1;
        for (TRItem trItem : trItems) {
            trItem.setOrder(sort);
            sort++;
        }
        trItemDao.saveAll(trItems);
        return trItems;
    }

    /**
     * ??????????????????
     *
     * @param reviews
     * @param trItem
     */
    private void staticAvg(List<T_Review> reviews, TRItem trItem) {
        DecimalFormat df = new DecimalFormat("0.00");
        df.setMaximumFractionDigits(2);
        if (reviews != null && reviews.size() > 0) {
            Double minScore = reviews.get(0).getPf();
            Double maxScore = minScore;
            Double totalScore = 0.0;
            for (T_Review review : reviews) {
                totalScore += review.getPf();
                if (minScore > review.getPf()) {
                    minScore = review.getPf();
                }
                if (maxScore < review.getPf()) {
                    maxScore = review.getPf();
                }
            }
            if (reviews.size() > 3) {
                Double lessScore = totalScore - minScore - maxScore;
                trItem.setAvgScore(Double.parseDouble(df.format(lessScore / (reviews.size() - 2))));
            } else {
                trItem.setAvgScore(Double.parseDouble(df.format(totalScore / (reviews.size() - 2))));
            }

            trItem.setAbsAvgScore(Double.parseDouble(df.format(totalScore / reviews.size())));
        }
    }
}
