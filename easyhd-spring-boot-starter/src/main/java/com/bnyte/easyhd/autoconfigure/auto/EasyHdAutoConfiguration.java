package com.bnyte.easyhd.autoconfigure.auto;

import com.bnyte.easyhd.autoconfigure.scanner.EasyHdBeanRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//@Import({EasyHdScanRegister.class}) // 开启后不添加EasyHdScan也会将所有接口添加到IOC容器中
public class EasyHdAutoConfiguration {

    @Autowired
    private ConfigurableApplicationContext applicationContext;

    @Bean
    public EasyHdBeanRegister repostBeanRegister() {
        EasyHdBeanRegister easyHdBeanRegister = new EasyHdBeanRegister(applicationContext);
        easyHdBeanRegister.registerScanner();
        return easyHdBeanRegister;
    }

}
