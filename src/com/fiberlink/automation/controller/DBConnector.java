package com.fiberlink.automation.controller;

import java.sql.Connection;
import java.sql.DriverManager;

import com.fiberlink.automation.core.DBStore;

/**
 * @author rahuls23
 *
 */
public class DBConnector {

	private String dbUsername;
	private String dbPassword;
	public static DBStore dbInformation;
	public Connection dbConnection;

	public DBConnector(String dbUsername, String dbPassword, String hostName, String port, String serviceName) {
		this.dbUsername = dbUsername;
		this.dbPassword = dbPassword;
		
		Initialize(hostName, port, serviceName);
	}
	
	public void Initialize(String hostName, String port, String serviceName) {
		try {

//			String encryptedPassword = Security.encrypt(this.dbPassword);
//			System.out.println("Encrypted password: " + encryptedPassword);
			dbInformation = new DBStore(hostName, port, serviceName, this.dbUsername, this.dbPassword);
			dbConnection = connect();

		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
	}
	
	public Connection connect() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			System.out.println("Where is your Oracle JDBC Driver?");
			return null;
		}
		Connection connection = null;
		try {
			String hostName = dbInformation.getDbHostName().toString();
			String port = dbInformation.getDbPort().toString();
			String serviceName = dbInformation.getDbServiceName().toString();
			String userName = dbInformation.getDbUsername().toString();
//			String password = Security.decrypt(dbInformation.getDbEncryptedPassword().toString());
			String password = dbInformation.getDbPassword().toString();
			
			connection = DriverManager.getConnection("jdbc:oracle:thin:@"+ hostName + ":" + port + "/" + serviceName + "", userName, password);

		} catch (Exception e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return null;
		}
		return connection;
	}
	
	public void closeConnection(){
		try{
			dbConnection.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
