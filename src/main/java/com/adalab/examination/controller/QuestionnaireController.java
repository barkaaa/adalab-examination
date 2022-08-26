package com.adalab.examination.controller;


import com.adalab.examination.entity.Questionnaire;
import com.adalab.examination.service.QuestionnaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Karl
 * @since 2022-08-26
 */
@RestController
@RequestMapping("/api/questionnaire")
public class QuestionnaireController {
    @Autowired
    QuestionnaireService questionnaireService;

    @PutMapping("/addorupdate")
    public String add(@RequestBody Questionnaire questionnaire){
        boolean success = questionnaireService.saveOrUpdate(questionnaire);
        if(success){
            return "success";
        }else{
            throw new ResponseStatusException(HttpStatus.REQUEST_TIMEOUT);
        }
    }

}

