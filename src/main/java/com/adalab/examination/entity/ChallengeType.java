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
 * @since 2022-08-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ChallengeType implements Serializable {

    private static final long serialVersionUID=1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer stage;

    /**
     * 1:问卷调查，2：markdown
     */
    private Integer type;


}
