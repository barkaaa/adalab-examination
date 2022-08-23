package com.adalab.examination;

import com.adalab.examination.controller.StudentController;
import com.adalab.examination.entity.Student;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@Component
class AdalabExaminationApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    StudentController studentController;
    @Test
    void testGetRanking(){
        var ranking = studentController.getRanking();
        Assertions.assertEquals(7,ranking.size());
    }
}
