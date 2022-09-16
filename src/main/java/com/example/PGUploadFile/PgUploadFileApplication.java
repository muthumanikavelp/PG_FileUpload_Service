package com.example.PGUploadFile;

import javax.servlet.MultipartConfigElement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
@ServletComponentScan
@SpringBootApplication
public class PgUploadFileApplication extends SpringBootServletInitializer {

//	public static void main(String[] args) {
//		SpringApplication.run(PgUploadFileApplication.class, args);
//	}
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		    return application.sources(PgUploadFileApplication.class);
		  }

	
		public static void main(String[] args) {
		    SpringApplication.run(PgUploadFileApplication.class, args);
		  }
}
