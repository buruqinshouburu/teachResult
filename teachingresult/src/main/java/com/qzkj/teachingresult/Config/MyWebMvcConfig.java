/*
package com.qzkj.teachingresult.Config;

import com.qzkj.teachingresult.Filter.MyInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyWebMvcConfig implements WebMvcConfigurer {
*/
/**
 * 将自定义拦截器作为bean写入配置
 * @return*//*



    @Bean
    public MyInterceptor myInterceptor(){
        return new MyInterceptor();
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //拦截处理操作的匹配路径
        //放开静态拦截
        registry.addInterceptor(myInterceptor())
                .addPathPatterns("/**") //拦截所有路径
                .excludePathPatterns("/","/api/login")//排除路径
                .excludePathPatterns("/xadmin/**");//排除静态资源拦截

    }
}
*/
