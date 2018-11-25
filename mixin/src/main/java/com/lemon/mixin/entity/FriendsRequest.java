package com.lemon.mixin.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author AnxiangLemon
 * @since 2018-10-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class FriendsRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String sendUserId;

    private String acceptUserId;

    private LocalDateTime requestDataTime;


}
