/**
 *
 */
package com.fiberlink.automation.core;

/**
 * @author rahuls23
 *
 */
public class Environment {
	
	private String environmentName;
	private String hostName;
	private String dbHostName;
	private String dbPort;
	private String dbServiceName;
	private String adminUserName;
	private String adminPassword;
	private String dbUserName;
	private String dbPassword;
	
	public Environment(String environmentName, String hostName, String dbHostName, String dbPort, String dbServiceName, String adminUserName, String adminPassword, String dbUserName, String dbPassword){
		this.environmentName = environmentName;
		this.hostName = hostName;
		this.dbHostName = dbHostName;
		this.dbPort = dbPort;
		this.dbServiceName = dbServiceName;
		this.adminUserName = adminUserName;
		this.adminPassword = adminPassword;
		this.dbUserName = dbUserName;
		this.dbPassword = dbPassword;
	}

	public String getDbUserName() {
		return dbUserName;
	}


	public void setDbUserName(String dbUserName) {
		this.dbUserName = dbUserName;
	}


	public String getDbPassword() {
		return dbPassword;
	}


	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}


	public String getEnvironmentName() {
		return environmentName;
	}

	public void setEnvironmentName(String environmentName) {
		this.environmentName = environmentName;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getDbHostName() {
		return dbHostName;
	}

	public void setDbHostName(String dbHostName) {
		this.dbHostName = dbHostName;
	}

	public String getDbPort() {
		return dbPort;
	}

	public void setDbPort(String dbPort) {
		this.dbPort = dbPort;
	}

	public String getDbServiceName() {
		return dbServiceName;
	}

	public void setDbServiceName(String dbServiceName) {
		this.dbServiceName = dbServiceName;
	}

	public String getAdminUserName() {
		return adminUserName;
	}

	public void setAdminUserName(String adminUserName) {
		this.adminUserName = adminUserName;
	}

	public String getAdminPassword() {
		return adminPassword;
	}

	public void setAdminPassword(String adminPassword) {
		this.adminPassword = adminPassword;
	}
	
}
