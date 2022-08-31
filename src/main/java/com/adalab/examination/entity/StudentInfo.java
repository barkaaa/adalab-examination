package com.adalab.examination.entity;

import java.time.LocalDateTime;
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
 * @since 2022-08-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class StudentInfo implements Serializable {

    private static final long serialVersionUID=1L;

    private Integer id;

    private String name;

    private String tel;

    private String schedule;

    private String webPage;

    private String status;

    private LocalDateTime createdDate;

    private LocalDateTime daysNeeded;

    private Integer actualHours;

    private LocalDateTime lastEdited;

    private String type;

    private LocalDateTime beginDate;

    private Integer episode;

    private String avatar;

    private Integer age;

    private String email;


}
