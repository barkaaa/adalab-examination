package com.adalab.examination.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Levels {

    @TableId(value = "id", type = IdType.AUTO)
    Integer id;
    Integer stage;
    String name;
    Integer type;
}
