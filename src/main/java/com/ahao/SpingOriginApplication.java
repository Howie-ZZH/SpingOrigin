package com.ahao;

import org.apache.naming.factory.BeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.lang.reflect.Field;
import java.util.Map;

@SpringBootApplication
public class SpingOriginApplication {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        ConfigurableApplicationContext context = SpringApplication.run(SpingOriginApplication.class, args);

        /*
          1. 什么是beanFactory？
                - BeanFactory是ApplicationContext的父接口
                - BeanFactory才是Spring的核心容器ApplicationContext的实现组合了它的功能
         */
        System.out.println(context);

        /*
            2.BeanFactory能干啥，进入BeanFactory中查看，表面上只有getBean
         */
        Field singletonObjects = DefaultSingletonBeanRegistry.class.getDeclaredField("singletonObjects");
        // 使反射对象的私有成员可以访问
        singletonObjects.setAccessible(true);
        // context中的beanFactory
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
        // singletonObjects中获取beanFactory，此时是个map集合
        Map<String, Object> beanFactoryMap = (Map<String, Object>) singletonObjects.get(beanFactory);
        beanFactoryMap.entrySet().stream().filter( i -> {
            return i.getKey().contains("human");
        }).forEach( result -> {
            System.out.println(result.getKey() + "=" + result.getValue());
        });



    }

}
