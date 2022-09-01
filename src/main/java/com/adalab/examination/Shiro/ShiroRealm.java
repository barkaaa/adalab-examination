package com.adalab.examination.Shiro;
import com.adalab.examination.entity.StudentInfo;
import com.adalab.examination.entity.StudentInfoToken;
import com.adalab.examination.service.StudentInfoService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class ShiroRealm extends AuthorizingRealm {

    private final String realPassword = "1919810";
    private final String realUsername = "adalab";
    @Autowired
    StudentInfoService studentInfoService;
    /**
     * 访问授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
        //获取权限对象
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        //获取用户的主身份信息，
        String role =principal.toString();
        simpleAuthorizationInfo.addRole(role);
        //将角色赋值给 simpleAuthorizationInfo 权限对象
        return simpleAuthorizationInfo;
    }

/**
     * 登录认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        StudentInfoToken token = (StudentInfoToken)authenticationToken;
        if (token.getRole().equals("root")){
            if (token.getName().equals(realUsername)){
                return new SimpleAuthenticationInfo("root",realPassword,token.getID());
            }else {
                return null;
            }

        }
        //认证密码 第二个参数本来应该是密码 但是我们这个项目随意
        return new SimpleAuthenticationInfo("student",token.getID(),token.getID());
    }

    /**
     * 重写supports方法，使 Shiro 能够识别自定义的 Token
     * @param token
     * @return
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof StudentInfoToken;
    }

}