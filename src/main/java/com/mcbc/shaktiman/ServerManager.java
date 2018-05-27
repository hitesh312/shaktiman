package com.mcbc.shaktiman;

import com.mcbc.shaktiman.common.Constant;
import com.mcbc.shaktiman.game.GameManagerEndpoint;
import com.mcbc.shaktiman.mq.QueueManager;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;

public class ServerManager {
    public static void main(String[] args) {
        ServerManager manager = new ServerManager();
        manager.startRestEndpoint();
        QueueManager.start();
    }

    private Server startRestEndpoint(){
        JAXRSServerFactoryBean factoryBean = new JAXRSServerFactoryBean();
        factoryBean.setResourceClasses(GameManagerEndpoint.class);
        factoryBean.setResourceProvider(
                new SingletonResourceProvider(new GameManagerEndpoint()));
        factoryBean.setAddress(Constant.restAddress);
        return factoryBean.create();
    }
}
