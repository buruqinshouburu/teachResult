/*
package com.qzkj.teachingresult.Filter;

import com.qzkj.teachingresult.Pojo.User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

*/
/**
 * 拦截器
 * *//*



public class MyInterceptor implements HandlerInterceptor {
    @Override
    @CrossOrigin
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取登录时保存到session的用户信息
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            //拦截未登录请求,跳转到登录页面
        } else {
            request.getSession().setAttribute("user", user);
            request.getSession().setMaxInactiveInterval(3600);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}
*/
