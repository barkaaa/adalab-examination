package com.adalab.examination.Shiro;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;


@ControllerAdvice
public class ErrorControllerAdvice {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @ExceptionHandler({AuthorizationException.class})
    public ModelAndView handlerError(RuntimeException e) {
        logger.error(e.getMessage());
        return new ModelAndView("/api/error/401");
    }
}