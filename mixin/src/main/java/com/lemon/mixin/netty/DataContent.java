package com.lemon.mixin.netty;

import lombok.Data;

/**
 * @ Author     : AnxiangLemon
 * @ Date       : Created in 15:21 2018/10/30
 * @ Description: this is class
 */
@Data
public class DataContent {

    private Integer action; //动作类型
    private MixinMsg mixinMsg;
    private String extand; //拓展字段
}
