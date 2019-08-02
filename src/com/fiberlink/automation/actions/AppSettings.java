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

public class AppSettings extends CustomerInfo{
	
	public ResultSet rs = null;
	public ResultSet rspv = null;
	
	private String appAutoUpdateEnabled;
	private String propertyName;
	private String propertyValue;
	
	static File filepathName = new File(Config.getData("filepath_AppSettings"));
	
	public AppSettings(int i) {
		super(i);
		// TODO Auto-generated constructor stub
	}
	
	public int runTest() throws ClassNotFoundException, SQLException, IOException{
		String testScriptName = AppSettings.class.getSimpleName();
		System.out.println(testScriptName);
		String orgID = fetchCustomerBasicInfo(Config.getData("billingID"));
		String schema = findSourceSchema(Config.getData("billingID"));
		fetchAppSettingDetails(Utils.getExcelData("dbUsername"),Utils.passwordUtils(),orgID,schema,Config.getData("billingID"));

		return status;	
	}

	public static void main(String args[]) throws ClassNotFoundException, SQLException, IOException{
		System.out.println("Result"+":"+ new AppSettings(0).runTest());
	}

	public void fetchAppSettingDetails(String userName, String password, String orgID, String schema, String billingID) throws SQLException, IOException{

		BufferedWriter bw = Utils.writeDataToFile(filepathName);
		
		//Make JDBC Connection
		database = getDB(password);
		Statement stmt=database.dbConnection.createStatement(); 
		
		//SQL Query for Rules and fetch data from CUSTOMER_PROPERTY, WORLD_PROPERTY and APP_CATALOG_SETTINGS table
		String sqlQueryPropertyValue = "SELECT cp.PROPERTY_VALUE, wp.PROPERTY_NAME from VPN2.CUSTOMER_PROPERTY cp, VPN2.WORLD_PROPERTY wp WHERE cp.CUSTOMER_ID=(SELECT CUSTOMERID FROM BOMS.CUSTOMER WHERE SECONDARYACCOUNTID = '" + billingID + "') AND cp.PROPERTY_ID = wp.PROPERTY_ID";
		String sqlQuery = "SELECT * from "+schema+".APP_CATALOG_SETTINGS WHERE ORGANIZATION_ID = '" + orgID + "'";
		rs = stmt.executeQuery(sqlQuery);
		
		//Retrieve the data from PROPERTY_VALUE, PROPERTY_NAME & CUSTOMER_PROPERTY_ID column and write it to properties file.
		bw.write("### Get App Setting Details ###");
		bw.newLine();
		while(rs.next()){
			appAutoUpdateEnabled = rs.getString("ALLOW_APPS_AUTO_UPDATE");
			System.out.println(appAutoUpdateEnabled);
			bw.write("AppAutoUpdateStatus=" + appAutoUpdateEnabled);
			bw.newLine();
		}
		
		rspv = stmt.executeQuery(sqlQueryPropertyValue);
		bw.write("### Get Property Value Details for App Settings ###");
		bw.newLine();
		while(rspv.next()){
			propertyName = rspv.getString("PROPERTY_NAME");
			if(propertyName.equals("APP_CATALOG_3.0_MODE") || propertyName.equals("AUTO_UPDATE_FOR_IOS_APPS") || propertyName.equals("ENABLE_APP_APPROVAL_PROCESS")){
				propertyValue = rspv.getString("PROPERTY_VALUE");
				System.out.println(propertyName +":"+propertyValue);
				bw.write(propertyName+"="+ propertyValue);
				bw.newLine();
			}
			
		}
		
		bw.flush();
		bw.close();
	}
}
