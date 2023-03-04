package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication							//应用启动类
@EnableTransactionManagement					//允许开启数据库事务。开启后，如果进行数据的增、改、删操作时发生非检查异常，数据将回滚
@ServletComponentScan							//将支持Servlet类。在案例ch13.2_goods_MOSM中，生成验证码图片时使用了Servlet
public class AppStarter extends SpringBootServletInitializer {	//SpringBoot的启动类

	public static void main(String[] args) {
		
		SpringApplication.run(AppStarter.class, args);			//只扫描相同父目录下的包和类
	}
	
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {

		return builder.sources(AppStarter.class);				//Spring Boot项目，若打包成war包，则使用外置的tomcat启动
	}
}
