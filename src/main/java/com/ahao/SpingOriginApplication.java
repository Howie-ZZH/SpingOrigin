package com.ahao;

import com.ahao.eventModel.ChangePasswordEventPublish;
import org.apache.naming.factory.BeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;

@SpringBootApplication
public class SpingOriginApplication {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, IOException {
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


        /*
            3.ApplicationContext比BeanFactory多点啥
        */

        //可以用ConfigurableApplicationContext获取资源文件
        Resource[] resources = context.getResources("classpath:application.properties");
        for (Resource resource : resources) {
            System.out.println(resource);
        }

        //获取一些配置信息，像环境变量，propertys的配置啥的
        String java_home = context.getEnvironment().getProperty("java_home");
        String property = context.getEnvironment().getProperty("spring.zhangzh");
        System.out.println(java_home);
        System.out.println(property);

        //事件发布,然后在任何一个Spring管理的类中监听都行，记得加上@EventListener的注解
        context.publishEvent(new ChangePasswordEventPublish(context));

    }

}
