package com.lemon.mixin.enums;

/**
 * 
 * @Description: 消息签收状态 枚举
 */
public enum MsgSignFlagEnum {
	
	unsign(0, "未签收"),
	signed(1, "已签收");	
	
	private final Integer type;
	private final String content;
	
	MsgSignFlagEnum(Integer type, String content){
		this.type = type;
		this.content = content;
	}
	
	public Integer getType() {
		return type;
	}  
}
