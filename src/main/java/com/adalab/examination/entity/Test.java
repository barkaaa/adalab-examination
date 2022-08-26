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
 * @since 2022-08-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Test implements Serializable {

    private static final long serialVersionUID=1L;

    private Integer id;

    private String mdUrl;

    private String testFileUrl;

    private String cmd;

    private Integer timeOut;

    private Integer imgId;

    private Integer isTest;

    private Integer next;


}
