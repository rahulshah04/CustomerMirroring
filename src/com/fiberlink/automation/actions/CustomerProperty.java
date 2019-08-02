package com.fiberlink.automation.actions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import com.fiberlink.automation.controller.Config;
import com.fiberlink.automation.core.CustomerInfo;
import com.fiberlink.automation.utility.Utils;

/**
 * @author rahuls23
 * 
 * Read the customer property details for the customer from the DB.
 * Write the data to {CustomerProperty.properties} file
 * Fetched data from {VPN2.CUSTOMER_PROPERTY} and {VPN2.WORLD_PROPERTY} table. 
 * Details of PROPERTY_NAME & PROPERTY_VALUE is retrieved
 * @param userName
 * @param password
 * @param billingId
 * 
 */

public class CustomerProperty extends CustomerInfo{

	public ResultSet rs = null;

	public String propertyName;
	public String propertyValue;

	File filepathName = new File(Config.getData("filepath_Customerproperty"));

	Properties properties = new Properties();

	int status = 0;

	public CustomerProperty(int i) {
		super(i);
		// TODO Auto-generated constructor stub
	}

	public int runTest() throws ClassNotFoundException, SQLException, IOException{
		String testScriptName = CustomerProperty.class.getSimpleName();
		System.out.println(testScriptName);
		fetchCustomerPropertyInfo(Utils.getExcelData("dbUsername"),Utils.passwordUtils(), Config.getData("billingID"));

		return status;	
	}

	public static void main (String args[]) throws ClassNotFoundException, SQLException, IOException{
		System.out.println("Result"+":"+ new CustomerProperty(0).runTest());
	}


	public void fetchCustomerPropertyInfo(String userName, String password, String billingId) throws ClassNotFoundException, SQLException, IOException{
		
		//Make JDBC connection
		database = getDB(password);
		Statement stmt=database.dbConnection.createStatement(); 
		
		//Execute sql query for Customer property
		String sqlQuery = "SELECT cp.property_id,wp.property_name,wp.property_desc,cp.property_value FROM vpn2.customer_property cp,vpn2.world_property wp WHERE cp.customer_id = (SELECT customerid FROM boms.customer WHERE secondaryaccountid = '" + billingId + "') AND cp.property_id = wp.property_id";
		rs = stmt.executeQuery(sqlQuery);
		
		//Retrieve data form the PROPERTY_NAME & PROPERTY_VALUE column and write it to the properties file
		while(rs.next()){
			propertyName = rs.getString("PROPERTY_NAME");
			propertyValue = rs.getString("PROPERTY_VALUE");
			properties.setProperty(propertyName, propertyValue);
			System.out.println(propertyName+":"+propertyValue);			
		}
		FileOutputStream fileOut = new FileOutputStream(filepathName);
		properties.store(fileOut, "Customer Property");
		fileOut.close();	
	}	
}