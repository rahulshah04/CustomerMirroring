package com.fiberlink.automation.core;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.fiberlink.automation.controller.DBConnector;
import com.fiberlink.automation.utility.Utils;

/**
 * @author rahuls23
 * 
 * This is a base script which fetch the customer basic info for the given billingID.
 * Return Organization Id and Customer Id for the entered @param billingID.
 * Return database DBConnection instance
 * Return Schema for the entered @param billingID
 * @param billingID
 * @param organizationID
 * @param customerID
 * @param schema
 * @param database
 */

public class CustomerInfo {

	private String billingID;
	private String customerID;
	private String organizationID;
	private String schema;
	
	protected String decryptedPassword;

	public Environment environment;
	
	public DBConnector database;


	public static int status = 0;

	public static String testScriptName;

	public CustomerInfo(String billingID, Environment environment){
		this.billingID = billingID;
		this.environment = environment;

//		database = getDB(environment);
	}


	public CustomerInfo(int i) {
		// TODO Auto-generated constructor stub
	}


	public DBConnector getDatabase() {
		return database;
	}


	public void setDatabase(DBConnector database) {
		this.database = database;
	}

	public String getBillingID() {
		return billingID;
	}

	public void setBillingID(String billingID) {
		this.billingID = billingID;
	}

	public String getCustomerID() {
		return customerID;
	}

	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}

	public String getOrganizationID() {
		return organizationID;
	}

	public void setOrganizationID(String organizationID) {
		this.organizationID = organizationID;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public Environment getEnvironment() {
		return environment;
	}

	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

//	public DBConnector database = new DBConnector(Utils.getExcelData("dbUsername"),"vpn2ro", Utils.getExcelData("dbHostName"), Utils.getExcelData("dbPort"), Utils.getExcelData("dbServiceName"));
	
	//Retrieve the Organization Id and Customer Id for the given billing id
	public String fetchCustomerBasicInfo(String billingID) {
		ResultSet rs = null;
		Statement stmt = null;
//		decryptedPassword = Security.decryptPassword(Utils.getExcelData("encryptedPassword"));
		database = getDB("vpn2ro");
		try {
			String query = "SELECT ORGANIZATION_ID, CUSTOMERID FROM BOMS.CUSTOMER WHERE SECONDARYACCOUNTID = '" + billingID + "'";
			try {
				stmt = database.dbConnection.createStatement();
				rs = stmt.executeQuery(query);
			} catch (SQLException e) {
				System.out.println("Failed to Execute Query");
				e.printStackTrace();
			}
			while (rs.next()) {
				this.setCustomerID(rs.getString("CUSTOMERID"));
				this.setOrganizationID(rs.getString("ORGANIZATION_ID"));
			}
		} catch (Exception E) {
			E.printStackTrace();
		} finally {
			try { rs.close(); } catch (Exception ignore) { }
			try { stmt.close(); } catch (Exception ignore) { }
		}
		return getOrganizationID();
	}
	
	//Return database DBConnection instance 
	public DBConnector getDB(String password) {
		DBConnector database = null;
		try {
			String hostName = Utils.getExcelData("dbHostName");
			String serviceName = Utils.getExcelData("dbServiceName");
			String dbPort = Utils.getExcelData("dbPort");
			
			database = new DBConnector(Utils.getExcelData("dbUsername"),password, hostName, dbPort, serviceName);
		} catch (Exception E) {
			E.printStackTrace();
		}
		return database;
	}
	
	//Retrieve the Schema for the given billing id
	public String findSourceSchema(String billingID) {
		ResultSet rs = null;
		Statement stmt = null;
		database = getDB("vpn2ro");
		try {
			String query = "SELECT PROPERTY_VALUE FROM VPN2.CUSTOMER_PROPERTY WHERE CUSTOMER_ID IN (SELECT CUSTOMERID FROM BOMS.CUSTOMER WHERE SECONDARYACCOUNTID='" + billingID + "') AND PROPERTY_VALUE LIKE 'SCHEMA%'";
			try {
				stmt = database.dbConnection.createStatement();
				rs = stmt.executeQuery(query);
			} catch (SQLException e) {
				System.out.println("Failed to Execute Query");
				e.printStackTrace();
			}
			while (rs.next()) {
				this.schema = rs.getString("PROPERTY_VALUE");
				break;
			}
		} catch (Exception E) {
			E.printStackTrace();
		} finally {
			try { rs.close(); } catch (Exception ignore) { }
			try { stmt.close(); } catch (Exception ignore) { }
		}
		return getSchema();
	}

}
