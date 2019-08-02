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
 * Read the rules set details for the customer from the DB.
 * Write the data to {Rules.properties} file
 * Fetched data from {EVT_RULE_ATTR_CONFIG} and {EVT_RULE} table. 
 * Details of CODE, NAME, RULE_TYPE, DESCRIPTION is retrieved.
 * @param userName
 * @param password
 * @param orgID
 * 
 */

public class Rules extends CustomerInfo {
	
	public ResultSet rs = null;
	
	private String ruleCode;
	private String ruleName;
	private String ruleType;
	private String ruleDescription;
	
	static File filepathName = new File(Config.getData("filepath_Rules"));

	public Rules(int i) {
		super(i);
		// TODO Auto-generated constructor stub
	}
	
	public int runTest() throws ClassNotFoundException, SQLException, IOException{
		String testScriptName = Rules.class.getSimpleName();
		System.out.println(testScriptName);
		String orgID = fetchCustomerBasicInfo(Config.getData("billingID"));
		fetchRuleSetDetails(Utils.getExcelData("dbUsername"),Utils.passwordUtils(),orgID);

		return status;	
	}

	public static void main(String args[]) throws ClassNotFoundException, SQLException, IOException{
		System.out.println("Result"+":"+ new Rules(0).runTest());
	}

	public void fetchRuleSetDetails(String userName, String password, String orgID)throws ClassNotFoundException, SQLException, IOException{
		BufferedWriter bw = Utils.writeDataToFile(filepathName);
		
		//Make JDBC Connection
		database = getDB(password);
		Statement stmt=database.dbConnection.createStatement(); 
		
		//SQL Query for Rules and fetch data from EVT_RULE_ATTR_CONFIG and EVT_RULE table
		String sqlQuery = "SELECT er.* FROM MDM.EVT_RULE_ATTR_CONFIG erac INNER JOIN MDM.EVT_RULE er ON erac.RULE_ID= er.ID WHERE erac.ORGANIZATION_ID='" + orgID + "'";
		rs = stmt.executeQuery(sqlQuery);
		
		//Retrieve the data from CODE, NAME, RULE_TYPE & DESCRIPTION column and write it to properties file.
		bw.write("### Get Rule Set Details ###");
		bw.newLine();
		while(rs.next()){
			ruleCode = rs.getString("CODE");
			ruleName = rs.getString("NAME");
			ruleType = rs.getString("RULE_TYPE");
			ruleDescription = rs.getString("DESCRIPTION");
			System.out.println(ruleCode +"\n"+ruleName+"\n"+ruleType+"\n"+ruleDescription);
			bw.write("RuleCode=" + ruleCode);
			bw.newLine();
			bw.write("RuleName=" + ruleName);
			bw.newLine();
			bw.write("RuleType=" + ruleType);
			bw.newLine();
			bw.write(ruleDescription);
			bw.newLine();
		}
		bw.flush();
		bw.close();
	}

}
