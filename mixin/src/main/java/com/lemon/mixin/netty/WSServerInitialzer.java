package com.lemon.mixin.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @ Author     : AnxiangLemon
 * @ Date       : Created in 10:54 2018/10/22
 * @ Description: this is class
 */
public class WSServerInitialzer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {

        ChannelPipeline pipeline = socketChannel.pipeline();

        //websocket 基于http协议 所以要有http编解码器
        pipeline.addLast(new HttpServerCodec());

        //对写大数据流的支持
        pipeline.addLast(new ChunkedWriteHandler());

        //对httpMessage进行聚合，聚合成FullHttpRequest或FullHttpResponse
        //几乎在netty中的编程 ，都会使用到此handler
        pipeline.addLast(new HttpObjectAggregator(1024*64));

        //====================以上是使用支持http协议===================

        //===================增加心跳===================
        //如果是读写空闲  不处理
        pipeline.addLast(new IdleStateHandler(15,20,60));
       //自定义空闲状态检测
        pipeline.addLast(new HeartBeatHandler());

        /*
        * websocket 服务器处理的协议 ，用于指定给客户端连接访问的路由 :/ws
        * 本handler 会帮你处理一些繁重的复杂的事
        * 会帮你处理握手动作 ：handshaking （close，ping，pong）ping+pong=心跳
        * 对于websocket来讲， 都是以frams进行传输的不同的数据类型对应的frames也不同
        * */
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));

        //自定义handler
        pipeline.addLast(new ChatHandler());

    }
}
