package com.fiberlink.automation.actions;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.fiberlink.automation.controller.Config;
import com.fiberlink.automation.core.CustomerInfo;
import com.fiberlink.automation.utility.Utils;


/**
 * @author rahuls23
 * 
 * Read the services enabled data for the customer from the DB.
 * Write the data to {Services.properties} file
 * Fetched data from {SERVICE_KEY_METADATA} and {CUSTOMER_PROPERTY} table. 
 * Details of SERVICE_KEY_SUFFIX, PARENT_SERVICE_KEY_ID, PROPERTY_ID, SERVICE_DISPLAY_NAME, DEVICE_SERVICE_KEY & DESCRIPTION is retrieved
 * @param userName
 * @param password
 * @param billingID
 * 
 */

public class Services extends CustomerInfo {
	
	public ResultSet rs = null;
	
	private String serviceKeySuffix;
	private String parentServiceKeyId;
	private String propertyId;
	private String servicesDescription;
	private String serviceDisplayName;
	private String deviceServiceKey;
	
	
	static File filepathName = new File(Config.getData("filepath_Services"));

	public Services(int i) {
		super(i);
		// TODO Auto-generated constructor stub
	}
	
	public int runTest() throws ClassNotFoundException, SQLException, IOException{
		String testScriptName = Services.class.getSimpleName();
		System.out.println(testScriptName);
		fetchServicesEnabledDetails(Utils.getExcelData("dbUsername"),Utils.passwordUtils(),Config.getData("billingID"));

		return status;	
	}

	public static void main(String args[]) throws ClassNotFoundException, SQLException, IOException{
		System.out.println("Result"+":"+ new Services(0).runTest());
	}

	public void fetchServicesEnabledDetails(String userName, String password, String billingId)throws ClassNotFoundException, SQLException, IOException{
		
		BufferedWriter bw = Utils.writeDataToFile(filepathName);
		
		//Make JDBC Connection
//		Class.forName("oracle.jdbc.driver.OracleDriver");
//		connection  = DriverManager.getConnection("jdbc:oracle:thin:@"+ Utils.getExcelData("dbHostName") + ":" + Utils.getExcelData("dbPort") + "/" + Utils.getExcelData("dbServiceName")+"", userName, password);
		database = getDB(password);
		Statement stmt=database.dbConnection.createStatement();
		
		//SQL Query for Services and fetch data from SERVICE_KEY_METADATA table
		String sqlQuery = "SELECT * FROM COMMANDPORTAL.SERVICE_KEY_METADATA skm, VPN2.CUSTOMER_PROPERTY cp WHERE cp.PROPERTY_ID = skm.PROPERTY_ID AND cp.CUSTOMER_ID = (SELECT customerid FROM BOMS.CUSTOMER WHERE SECONDARYACCOUNTID = '"+billingId+"') and cp.PROPERTY_VALUE = 'Yes'";
		rs = stmt.executeQuery(sqlQuery);
		
		//Retrieve the data from SERVICE_KEY_SUFFIX, NAME, PARENT_SERVICE_KEY_ID, PROPERTY_ID, SERVICE_DISPLAY_NAME & DESCRIPTION column and write it to properties file.
		bw.write("### Get Enabled Services Details ###");
		bw.newLine();
		while(rs.next()){
			serviceKeySuffix = rs.getString("SERVICE_KEY_SUFFIX");
			parentServiceKeyId = rs.getString("PARENT_SERVICE_KEY_ID");
			propertyId = rs.getString("PROPERTY_ID");
			serviceDisplayName = rs.getString("SERVICE_DISPLAY_NAME");
			deviceServiceKey = rs.getString("DEVICE_SERVICE_KEY");
			servicesDescription = rs.getString("DESCRIPTION");
			System.out.println(serviceKeySuffix+"\n"+parentServiceKeyId+"\n"+propertyId+"\n"+serviceDisplayName+"\n"+deviceServiceKey+"\n"+servicesDescription);
			bw.write("ServiceKey=" + serviceKeySuffix);
			bw.newLine();
			bw.write("ParentServiceKey Id=" + parentServiceKeyId);
			bw.newLine();
			bw.write("PropertyID=" + propertyId);
			bw.newLine();
			bw.write("Service Display Name=" + serviceDisplayName);
			bw.newLine();
			bw.write("Device Key Service="+ deviceServiceKey);
			bw.newLine();
			bw.write("Description = " + servicesDescription);
			bw.newLine();
		}
		bw.flush();
		bw.close();
	}

}
