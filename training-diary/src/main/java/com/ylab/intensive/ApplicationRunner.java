package com.ylab.intensive;

import com.ylab.intensive.di.context.ApplicationContext;
import com.ylab.intensive.di.factory.BeanFactory;

public class ApplicationRunner {
    public static void  run() {
        ApplicationRunner application = new ApplicationRunner();
        ApplicationContext applicationContext = new ApplicationContext();
        BeanFactory beanFactory = new BeanFactory(applicationContext);
        applicationContext.setBeanFactory(beanFactory);



    }
}
