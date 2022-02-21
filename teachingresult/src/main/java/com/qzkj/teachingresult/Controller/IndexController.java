package com.qzkj.teachingresult.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *  页面跳转
 * @author xuhao
 * @date 2022/2/20
 */
@Controller
public class IndexController {

   /* @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String go2Index(){
        return "index";
    }*/

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String root() {
        return "index.html";
    }
}
