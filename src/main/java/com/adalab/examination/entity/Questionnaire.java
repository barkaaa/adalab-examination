package com.adalab.examination.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;


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
public class Questionnaire implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 问题类型，填空1、选择2
     */
    private Integer questionType;

    /**
     * 题干
     */
    private String theme;

    /**
     * 选项
     */
    private String options;

    /**
     * 是否多选
     */
    private String isMultiple;

    /**
     * 是否选项有其他
     */
    private String isAddtional;

    /**
     * 第几关
     */
    private Integer missionNumber;

    /**
     * 第几关的第几个
     */
    private Integer questionNumber;


}
