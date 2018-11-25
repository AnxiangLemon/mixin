package com.lemon.mixin.enums;

/**
 * 
 * @Description: 添加好友前置状态 枚举
 */
public enum SearchFriendsStatusEnum {
	
	SUCCESS(0, "OK"),
	USER_NOT_EXIST(1, "无此用户"),
	NOT_YOURSELF(2, "不能添加你自己"),
	ALREADY_FRIENDS(3, "该用户已经是你的好友");
	
	private final Integer status;
	private final String msg;
	
	SearchFriendsStatusEnum(Integer status, String msg){
		this.status = status;
		this.msg = msg;
	}
	
	public Integer getStatus() {
		return status;
	}  
	
	public static String getMsgByKey(Integer status) {
		for (SearchFriendsStatusEnum type : SearchFriendsStatusEnum.values()) {
			if (type.getStatus() == status) {
				return type.msg;
			}
		}
		return null;
	}
	
}
