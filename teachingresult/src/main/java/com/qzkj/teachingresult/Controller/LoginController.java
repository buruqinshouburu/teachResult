package com.qzkj.teachingresult.Controller;

import com.alibaba.fastjson.JSONArray;
import com.qzkj.teachingresult.Pojo.User;
import com.qzkj.teachingresult.Result.Result;
import com.qzkj.teachingresult.Service.impl.TeachResultServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Objects;

@Controller
public class LoginController {
    @Autowired
    private TeachResultServiceImpl teachResultService;
    @Autowired
    private HttpServletRequest request;


    @CrossOrigin
    @PostMapping(value = "/login")
    @ResponseBody
    public Result Login(@RequestBody User requestUser){
        // 对 html 标签进行转义，防止 XSS 攻击
        String username = requestUser.getUsername();
        String userpassword = requestUser.getPassword();
        username = HtmlUtils.htmlEscape(username);
        userpassword = HtmlUtils.htmlEscape(userpassword);
        Result result = new Result(200);
        HttpSession session = request.getSession();
        session.setMaxInactiveInterval(3600);

        if (!Objects.equals("admin", username)) {
            try {
                result = teachResultService.checkLogin(username, userpassword,session);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if(!Objects.equals("admin123", requestUser.getPassword())){
                result.setCode(400);
                result.setMsg("管理员密码不正确,请检查后重新输入");
            }else{
                User user = new User();
                user.setUsername(username);
                user.setId(username);
                result.setMsg(JSONArray.toJSONString(user));
                result.setCode(200);
            }
        }
        return result;
    }

}
