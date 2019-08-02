package com.fiberlink.automation.utility;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;

import org.apache.commons.net.util.Base64;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @author rahuls23
 *
 */

public class Utils {

	public static String cellKeyValue;
	
	private static XSSFWorkbook workbook;
	private static XSSFSheet sheet;


	public static String getExcelData(String cellKey)
	{
		try
		{
			FileInputStream file = new FileInputStream(new File("./resource/exceltestdata/Environment.xlsx"));

			//Create Workbook instance holding reference to .xlsx file
			workbook = new XSSFWorkbook(file);

			//Get first/desired sheet from the workbook
			sheet = workbook.getSheetAt(0);

			//Iterate through each rows one by one
			Iterator<Row> rowIterator = sheet.iterator();
			while (rowIterator.hasNext())
			{
				Row row = rowIterator.next();

				//For each row, iterate through all the columns
				Iterator<Cell> cellIterator = row.cellIterator();
				Cell cell = cellIterator.next();
				String cellValue = cell.toString();

				//compare the keyValue
				if(cellValue.equals(cellKey)){
					while (cellIterator.hasNext())
					{	
						Cell getCellValue = cellIterator.next();
						cellKeyValue = getCellValue.getStringCellValue();
					}
				}

			}

			file.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return cellKeyValue;
	}
	
	
	public static BufferedWriter writeDataToFile(File filePath) throws IOException{

		FileWriter fw = null;
		try {
			fw = new FileWriter(filePath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedWriter bw = new BufferedWriter(fw);

		return bw;
	}
	
	
	@SuppressWarnings("resource")
	public static String passwordUtils(){
		String password; 
		Scanner input = new Scanner (System.in);
		System.out.println("Enter DB Password");
		password = input.next(); 
		System.out.println("The password entered is : " + password);

		return password;

	}


	public static String getEncryptedPassword(){
		String encodedDbPassword = null; 

		String encryptedPassword = passwordUtils();

		try {
			byte[] encodedPassword = Base64.encodeBase64(encryptedPassword.getBytes());
			encodedDbPassword = new String (encodedPassword);
			System.out.println("Encrypted Password is:" +encodedDbPassword);

		}
		catch(Exception e){
			e.getMessage();

		}

		return encodedDbPassword;
	}

	//This method is to write in the Excel cell, Row num and Col num are the parameters

	public static void WriteDataToExcel(String cellValue, String encrypteddbPassword) throws Exception {
		Cell column = null;
		Row row = null;
//		Security td= new Security();
//		String encrypteddbPassword = td.encryptPassword(Utils.passwordUtils());
			try
			{	
				FileInputStream file = new FileInputStream("./resource/exceltestdata/Environment.xlsx");

				//Create Workbook instance holding reference to .xlsx file
				workbook = new XSSFWorkbook(file);

				//Get first/desired sheet from the workbook
				sheet = workbook.getSheetAt(0);

				//Iterate through each rows one by one
				Iterator<Row> rowIterator = sheet.iterator();
				
				while (rowIterator.hasNext())
				{
					row = rowIterator.next();

					//For each row, iterate through all the columns
					Iterator<Cell> cellIterator = row.cellIterator();
					Cell cell = cellIterator.next();
					String cellData = cell.toString();
					
					//compare the keyValue
					if(cellData.equals(cellValue)){
						column = row.getCell(0);
						System.out.println("Column Value:"+column);
						row.createCell(1).setCellValue(encrypteddbPassword);
						System.out.println(column.toString());
					}

				}
				
				// Constant variables Test Data path and Test Data file name
				FileOutputStream fileOut = new FileOutputStream("./resource/exceltestdata/Environment.xlsx");
				workbook.write(fileOut);
				fileOut.flush();
				fileOut.close();

		}catch(Exception e){

			throw (e);

		}

	}
	
//	public static void main (String args[]) throws Exception{
//		WriteDataToExcel("encryptedPassword");
//	}
//	
}
