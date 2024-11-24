package com.example.verityx;

import com.example.verityx.mapper.UserLoginMapper;
import com.example.verityx.service.UserLoginServiceImpl;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@SpringBootApplication
@MapperScan("com.example.verityx.mapper")
public class VerityxApplication {

	private static final Logger logger = LoggerFactory.getLogger(UserLoginServiceImpl.class);


	public static void main(String[] args) {

		SpringApplication.run(VerityxApplication.class, args);
	}

}
