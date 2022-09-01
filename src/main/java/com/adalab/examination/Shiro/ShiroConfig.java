package com.adalab.examination.Shiro;

import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {
    /**
     * ShiroFilterFactoryBean
     * 获取shiro过滤器
     * 主要配置需要拦截的路径
     */
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Autowired DefaultWebSecurityManager defaultWebSecurityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean=new ShiroFilterFactoryBean();

        //关联DefaultWeSecurityManager 安全管理器对象
        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager);
        /*
            添加shiro的内置过滤器
            用于添加需要拦截的路径,并设置拦截级别
            拦截级别：，
              anon :无需认证就可以访问
              authc：必须认证了才能访问,
              user： 必须拥有 remeberMe（记住我）功能才能访问
              perms：拥有对某个资源的权限才能访问
              roles： 拥有某个角色才能访问
         */
        Map<String,String> interceptMap=new LinkedHashMap<>();
        //例如 拦截 修改个人资料 功能，设置为需要登录认证才能访问
//        interceptMap.put("/api/manage/login","anon");
//        interceptMap.put("/callback","anon");
//        interceptMap.put("/api/studentInfo/me","anon");
//        interceptMap.put("/**","authc");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(interceptMap);
        return shiroFilterFactoryBean;
    }


    //DefaultWeSecurityManager
    @Bean
    public DefaultWebSecurityManager defaultWebSecurityManager(@Autowired ShiroRealm shiroRealm){
        DefaultWebSecurityManager webSecurityManager=new DefaultWebSecurityManager();

        //关联自定义realm对象
        webSecurityManager.setRealm(shiroRealm);

        return webSecurityManager;
    }

    //创建 realm 对象，需要自定义类继承 AuthorizingRealm抽象类，并实现其抽象方法
    @Bean
    public ShiroRealm getDhiroRealm(){
        return new ShiroRealm();
    }



    /**
     * 开启Shiro的注解，
     * (如@RequiresRoles,@RequiresPermissions),需借助SpringAOP扫描使用Shiro注解的类,
     * 并在必要时进行安全逻辑验证 * 配置以下两个bean(DefaultAdvisorAutoProxyCreator(可选)
     * 和AuthorizationAttributeSourceAdvisor)即可实现此功能
     *
     * @return
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator proxyCreator = new DefaultAdvisorAutoProxyCreator();
        proxyCreator.setProxyTargetClass(true);
        return proxyCreator;
    }

    /**
     * 开启 shiro aop注解支持.
     *
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

}