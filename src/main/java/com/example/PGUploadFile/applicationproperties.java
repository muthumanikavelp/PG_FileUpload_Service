package com.example.PGUploadFile;

import java.io.IOException;
import java.util.Properties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class applicationproperties {
	public static String getUploadPath() {
		if (myProperties() != null) {
			return myProperties().getProperty("nrlm.file.path");
		} else {
			return "";
		}
	}
	
	
	public static String getUploadConfigPath() {
		
		if (myProperties() != null) {
			return myProperties().getProperty("nrlm.file.pathonboarding");
		} else {
			return "";
			
		}
	}
	public static String getUploadFundReqPath() {
		if (myProperties() != null) {
			return myProperties().getProperty("nrlm.file.pathFundReq");
		} else {
			return "";
		}
	}
	public static String getUploadBusPlanPath() {
		if (myProperties() != null) {
			return myProperties().getProperty("nrlm.file.pathbus");
		} else {
			return "";
		}
	}
public static String getUploadPathMem() {
		
		if (myProperties() != null) {
			return myProperties().getProperty("nrlm.file.pathMem");
		} else {
			return "";
			//return uploadPath;
		}
	}
public static String getUploadPathSQLlite() {
	
	if (myProperties() != null) {
		return myProperties().getProperty("nrlm.file.pathSQLlite");
	} else {
		return "";
		//return uploadPath;
	}
}
	private static Properties myProperties() {
		try {
			Resource resource = new ClassPathResource("/application.properties");
			Properties props = PropertiesLoaderUtils.loadProperties(resource);
			return props;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
	}

}
