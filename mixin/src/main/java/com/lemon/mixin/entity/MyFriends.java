package com.lemon.mixin.entity;


import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author AnxiangLemon
 * @since 2018-10-26
 */
@Data

@Accessors(chain = true)
public class MyFriends {

    private static final long serialVersionUID = 1L;
    private String id;

    private String myUserId;

    private String myFriendUserId;


}
