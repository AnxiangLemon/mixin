package com.lemon.mixin.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @ Author     : AnxiangLemon
 * @ Date       : Created in 12:50 2018/10/31
 * @ Description: 用于检测channel的心跳handler  我是不太明白 用户出发事件在什么时候触发
 */
public class HeartBeatHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.READER_IDLE) {
                System.out.println("进入读空闲。。。");
            }else if(state == IdleState.WRITER_IDLE){
                System.out.println("进入写空闲。。。");
            }else if(state == IdleState.ALL_IDLE){
                System.out.println("channel关闭前，users的数量为："+ChatHandler.clients.size());
                //关闭无用的channel 以防资源浪费
                Channel channel = ctx.channel();
                channel.close();
                System.out.println("channel关闭后，users的数量为："+ChatHandler.clients.size());
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }


}
