package com.fiberlink.automation.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

/**
 * @author rahuls23
 * 
 * It is a utility code to fetch the keyvalue from the SourceCustomerConfig.properties file
 * @param key
 * @param value
 * 
 */

public class Config {
	
	public static String value;
	
	public static String getData(String key){
		try {
			File file = new File("./resource/SourceCustomerConfig.properties");
			FileInputStream fileInput = new FileInputStream(file);
			Properties properties = new Properties();
			properties.load(fileInput);
			fileInput.close();

			Enumeration<Object> enuKeys = properties.keys();
			while (enuKeys.hasMoreElements()) {
				String keydata = (String) enuKeys.nextElement();
				if(keydata.toString().equals(key)){
					value= properties.getProperty(key);
					System.out.println(value);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return value;
	}

}
