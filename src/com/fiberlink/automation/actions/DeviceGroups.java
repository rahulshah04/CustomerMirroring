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
 * Read the device group details for the customer from the DB.
 * Write the data to {DeviceGroup.properties} file
 * Fetched data from {SAVED_SEARCH} table. 
 * Details of SEARCH_NM, SEARCH_QUERY & SEARCH_TYPE is retrieved
 * @param userName
 * @param password
 * @param orgID
 * @param schema
 * 
 */

public class DeviceGroups extends CustomerInfo{
	
	public ResultSet rs = null;
	public ResultSet rsug = null;
	public ResultSet rsga = null;
	
	private String searchName;
	private String searchQuery;
	private String searchType;
	private String savedSearchID;
	private String displayName;
	private String completeAdID;
	private String maasManaged;
	private String buAdmin;
	private String entityType;
	private String entityValue;
	private String entityID;
	private String assocID;
	
	private int status = 0;
	
	
	static File filepathName = new File(Config.getData("filepath_Devicegroup"));
	
	public DeviceGroups(int i) {
		// TODO Auto-generated constructor stub
		super(i);
	}

	public int runTest() throws ClassNotFoundException, SQLException, IOException{
		String testScriptName = DeviceGroups.class.getSimpleName();
		System.out.println(testScriptName);
		String orgID = fetchCustomerBasicInfo(Config.getData("billingID"));
		String schema = findSourceSchema(Config.getData("billingID"));
		fetchDeviceGroupDetails(Utils.getExcelData("dbUsername"),Utils.passwordUtils(),orgID,schema);

		return status;	
	}

	public static void main(String args[]) throws ClassNotFoundException, SQLException, IOException{
		System.out.println("Result"+":"+ new DeviceGroups(0).runTest());
	}

	public void fetchDeviceGroupDetails(String userName, String password, String orgID, String schema)throws ClassNotFoundException, SQLException, IOException{
		BufferedWriter bw = Utils.writeDataToFile(filepathName);
		
		//Make DB connection
		database = getDB(password);
		Statement stmt=database.dbConnection.createStatement(); 
		
		//Execute the Saved Search Query
		String sqlSearchQuery = "SELECT * FROM "+schema+".SAVED_SEARCH WHERE organization_id= '" + orgID + "'";
		rs = stmt.executeQuery(sqlSearchQuery);

//		readJsonData readData = new readJsonData();
		
		//Fetch the Saved Search Details and write it to the properties file
		bw.write("### Get Saved Search Details ###");
		bw.newLine();
		while(rs.next()){
			searchName = rs.getString("SEARCH_NM");
			searchQuery = rs.getString("SEARCH_QUERY");
//			readData.readJsonForSearchquery(searchQuery);
			searchType = rs.getString("SEARCH_TYPE");
			System.out.println(searchName +"\n"+searchQuery+"\n"+searchType);
			bw.write("Search Name=" + searchName);
			bw.newLine();
			bw.write("Search Type=" + searchType);
			bw.newLine();
			bw.write(searchQuery);
			bw.newLine();
		}

		String sqlQueryUserGroups = "SELECT * FROM "+schema+".USER_GROUP WHERE ORGANIZATION_ID= '" + orgID + "'";
		rsug = stmt.executeQuery(sqlQueryUserGroups);

		bw.newLine();
		bw.write("### Get User Group Details ###");
		bw.newLine();
		while(rsug.next()){
			savedSearchID = rsug.getString("SAVED_SEARCH_ID");
			displayName = rsug.getString("DISPLAY_NM");
			completeAdID = rsug.getString("COMPLETE_AD_ID");
			maasManaged = rsug.getString("MAAS_MANAGED");
			buAdmin = rsug.getString("HAS_BU_ADMIN");
			System.out.println(savedSearchID +"\n"+displayName+"\n"+completeAdID+"\n"+maasManaged+"\n"+buAdmin);
			bw.write("Saved Search Id=" + savedSearchID);
			bw.newLine();
			bw.write("Dsiplay Name=" + displayName);
			bw.newLine();
			bw.write("Complete AD Id=" + completeAdID);
			bw.newLine();
			bw.write("Maas Managed=" + maasManaged);
			bw.newLine();
			bw.write("Bu Admin Enabled =" + buAdmin);
			bw.newLine();
		}

		String sqlQueryGroupAssoc = "SELECT * FROM "+schema+".GROUPS_ASSOCIATION WHERE ORGANIZATION_ID= '" + orgID + "'";
		rsga = stmt.executeQuery(sqlQueryGroupAssoc);

		bw.newLine();
		bw.write("### Get Group Association Details ###");
		bw.newLine();
		while(rsga.next()){
			savedSearchID = rsga.getString("SAVED_SEARCH_ID");
			entityType = rsga.getString("ENTITY_TYPE");
			entityValue = rsga.getString("ENTITY_VALUE");
			entityID = rsga.getString("ENTITY_ID");
			assocID = rsga.getString("ASSOC_ID");
			System.out.println(savedSearchID +"\n"+entityType+"\n"+entityValue+"\n"+entityID+"\n"+assocID);
			bw.write("Saved Search Id=" + savedSearchID);
			bw.newLine();
			bw.write("Entity Type=" + entityType);
			bw.newLine();
			bw.write("Entity Value=" + entityValue);
			bw.newLine();
			bw.write("Entity Id=" + entityID);
			bw.newLine();
			bw.write("Assoc Id=" + assocID);
			bw.newLine();
		}

		bw.flush();
		bw.close();		
	}

}
