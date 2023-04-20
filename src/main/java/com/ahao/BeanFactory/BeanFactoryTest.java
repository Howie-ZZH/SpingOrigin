package com.ahao.BeanFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class BeanFactoryTest {


    public static void main(String[] args) {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        //定义BeanDefinitionBuilder来定义一个BeanDefinition，也就是bean的描述信息，可以指定单例啥的
        AbstractBeanDefinition beanDefinition =
                BeanDefinitionBuilder.genericBeanDefinition(Config.class).setScope("singleton").getBeanDefinition();
        //注册BeanDefinition
        beanFactory.registerBeanDefinition("config", beanDefinition);
        //此时，下方打印的类是不包括 bean1和bean2的，显然在bean1和bean2上的@Bean注解对于这个beandefinition是不生效的
        for (String beanDefinitionName : beanFactory.getBeanDefinitionNames()) {
            System.out.println("beanDefinition Name is: " + beanDefinitionName);
        }
        System.out.println("beanDefinition count: " + beanFactory.getBeanDefinitionCount());

        //给beanFactory添加一些后处理器，处理器有一些功能 例如给beanFactory增加识别@Bean注解的能力
        AnnotationConfigUtils.registerAnnotationConfigProcessors(beanFactory);
        //此时，加上后处理器后，多出了一些bean都是一些后处理器
        System.out.println("------------------registerAnnotationConfigProcessors---------------------");
        for (String beanDefinitionName : beanFactory.getBeanDefinitionNames()) {
            System.out.println("beanDefinition Name is: " + beanDefinitionName);
            /*
             * beanDefinition Name is: org.springframework.context.annotation.internalConfigurationAnnotationProcessor
             * beanDefinition Name is: org.springframework.context.annotation.internalAutowiredAnnotationProcessor
             * beanDefinition Name is: org.springframework.context.annotation.internalCommonAnnotationProcessor
             * beanDefinition Name is: org.springframework.context.event.internalEventListenerProcessor
             * beanDefinition Name is: org.springframework.context.event.internalEventListenerFactory
             */
        }
        // 为什么此时打印出来的不包括 bean1 和 bean2 呢？因为后处理器还没开始运行
        System.out.println("beanDefinition count: " + beanFactory.getBeanDefinitionCount());

        //运行所有后处理器的postProcessBeanFactory()方法
        beanFactory.getBeansOfType(BeanFactoryPostProcessor.class).values().forEach(beanFactoryPostProcessor -> {
            beanFactoryPostProcessor.postProcessBeanFactory(beanFactory);
        });


        //此时，后处理器运行后，后处理器和bean1 bean2都被打印出来
        System.out.println("------------------postProcessBeanFactory---------------------");
        for (String beanDefinitionName : beanFactory.getBeanDefinitionNames()) {
            System.out.println("beanDefinition Name is: " + beanDefinitionName);
            /*
             * beanDefinition Name is: org.springframework.context.annotation.internalConfigurationAnnotationProcessor
             * beanDefinition Name is: org.springframework.context.annotation.internalAutowiredAnnotationProcessor
             * beanDefinition Name is: org.springframework.context.annotation.internalCommonAnnotationProcessor
             * beanDefinition Name is: org.springframework.context.event.internalEventListenerProcessor
             * beanDefinition Name is: org.springframework.context.event.internalEventListenerFactory
             */
        }
        System.out.println("beanDefinition count: " + beanFactory.getBeanDefinitionCount());

        /*Bean1 bean1 = beanFactory.getBean(Bean1.class);
        Bean2 bean2 = bean1.getBean2();
        //此时获取到的bean2是null的，因为@Autowire注解的功能是由 Bean 后处理器完成的，需要启用Bean后处理器增强功能
        System.out.println("bean1:" + bean1);
        System.out.println("bean1.bean2:" + bean2);*/

        //bean的后处理器BeanPostProcessor.class，针对bean的生命周期各个阶段提供扩展功能例如 @Autowire @Resource....
        beanFactory.getBeansOfType(BeanPostProcessor.class).values().forEach(beanFactory::addBeanPostProcessor);

        Bean1 bean1 = beanFactory.getBean(Bean1.class);
        Bean2 bean2 = bean1.getBean2();
        //此时可以获取到bean2
        System.out.println("bean1:" + bean1);
        System.out.println("bean1.bean2:" + bean2);

        //由日志记录可以了解到，beanFactory并不会第一时间把bean全部创建，而是先记录beanDefinition信息在beanFactory中，
        //当需要使用bean的时候，才会创建bean。
        //若想要beanFactory预先实例化bean，可以调用一个方法preInstantiateSingletons()，让beanFactory提前创建单例
        beanFactory.preInstantiateSingletons();

        /*
            小结：
            a. beanFactory 不会做的事情：
                1.不会主动调用后处理器
                2.不会主动添加bean后处理器
                3.不会主动实例化单例bean
            b.bean 后处理器排序的逻辑？还未学到
         */




    }

    @Configuration
    static class Config {
        @Bean
        public Bean1 bean1(){
            return new Bean1();
        }
        @Bean
        public Bean2 bean2() {
            return new Bean2();
        }
    }

    static class Bean1 {

        public static final Logger log = LoggerFactory.getLogger(Bean1.class);

        public Bean1 () {
            log.info("Bean1构造");
        }

        @Autowired
        Bean2 bean2;

        public Bean2 getBean2() {
            return bean2;
        }
    }

    static class Bean2 {

        public static final Logger log = LoggerFactory.getLogger(Bean2.class);

        public Bean2 () {
            log.info("Bean2构造");
        }


    }


}
