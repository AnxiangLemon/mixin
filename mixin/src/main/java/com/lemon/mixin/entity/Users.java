package com.lemon.mixin.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author AnxiangLemon
 * @since 2018-10-23
 */
@Data
//@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class Users {

    private static final long serialVersionUID = 1L;
    private String id;
    private String username;
    private String password;
    private String faceImage;
    private String faceImageBig;
    private String nickname;
    private String qrcode;
    private String cid;
}
