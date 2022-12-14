package com.adalab.examination.entity;


import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author Karl
 * @since 2022-08-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)

public class Episode implements Serializable {


    private static final long serialVersionUID = 1L;

    private Integer id;

    private String testFileName;

    private String cmd;

    private Integer timeOut;

    private String imgId;

    private Integer type;

    private String markdownUrl;
}
