package com.adalab.examination.entity;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author Karl
 * @since 2022-08-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class QuestionnaireReply implements Serializable {

    private static final long serialVersionUID=1L;

    private Integer studentId;

    private Integer missionId;

    private String reply;

    private Integer questionId;


}
