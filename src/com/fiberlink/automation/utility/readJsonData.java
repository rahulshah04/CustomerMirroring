package com.fiberlink.automation.utility;

import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * @author rahuls23
 *
 */

public class readJsonData {


	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void readJsonForSearchquery(String searchQuery) throws FileNotFoundException{

		JSONParser jsonParser = new JSONParser();

		try
		{
			//Read JSON file
			Object obj = jsonParser.parse(searchQuery);
			JSONObject jsonobject = (JSONObject) obj;

			Iterator<Map.Entry> itr1 = jsonobject.entrySet().iterator(); 
			while (itr1.hasNext()) { 
				Map.Entry pair = itr1.next(); 

				if(pair.getKey().equals("condition")|pair.getKey().equals("expression")){
					String searchQueryVal = (String) jsonobject.get(pair.getKey());
					System.out.println(searchQueryVal);
				}
				else{
					Map searchQueryVal = (Map) jsonobject.get(pair.getKey());
					Iterator<Map.Entry> itr2 = searchQueryVal.entrySet().iterator(); 
					while (itr2.hasNext()) { 
						Map.Entry pairNext = itr2.next(); 
						System.out.println(pairNext.getKey() + " : " + pairNext.getValue());
					}
				}

			}

		}catch (ParseException e) {
			e.printStackTrace();
		}
	}

}
