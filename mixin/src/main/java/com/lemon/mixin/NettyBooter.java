package com.lemon.mixin;

import com.lemon.mixin.netty.WSServer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @ Author     : AnxiangLemon
 * @ Date       : Created in 23:47 2018/10/22
 * @ Description: this is class
 */
@Component
public class NettyBooter implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if(contextRefreshedEvent.getApplicationContext().getParent() == null){
            try {
                WSServer.getInstance().start();
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }
}
