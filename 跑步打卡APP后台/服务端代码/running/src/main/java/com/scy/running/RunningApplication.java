package com.scy.running;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@MapperScan(basePackages = "com.scy.running.mapper")
@EnableSwagger2
/*@EnableScheduling
@EnableAsync*/

/*说明这个类是SpringBoot的主配置类，SpringBoot就应该运行这个类的main方法来启动SpringBoot应用；
@SpringBootConfiguration:Spring Boot的配置类；标注在某个类上，表示这是一个Spring Boot的配置类；
@EnableAutoConfiguration：告诉SpringBoot开启自动配置功能；以前我们需要配置的东西，Spring Boot
帮我们自动配置；这样自动配置才能生效；*/
@SpringBootApplication
public class RunningApplication {

    public static void main(String[] args) {
        SpringApplication.run(RunningApplication.class, args);
    }

}
