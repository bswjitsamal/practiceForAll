package restassured.automation.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class DataSources_Read_Utils {
	
	public Map<String, String> Json_File_Reader(String TestcaseName , String FolderName, String FileName) throws JsonIOException, JsonSyntaxException, FileNotFoundException{
		
		String dataFile = System.getProperty("user.dir")+File.separator+"resources"+File.separator+FolderName+File.separator+FileName;
		 
		 JsonElement jsonElement = ( new JsonParser()).parse(new FileReader(dataFile));
		 
		 JsonObject jsonObject = jsonElement.getAsJsonObject();
		 
		 JsonElement organizationsAdd = jsonObject.get(TestcaseName);
		 
		 JsonObject organizationsAddObject = organizationsAdd.getAsJsonObject();
		 
		 Map<String,String> dataMapObject = new HashMap<String,String>();
		 Iterator iterator = organizationsAddObject.entrySet().iterator();
		
		 while(iterator.hasNext()) {
			 Entry KeyValue = (Entry) iterator.next();
			 dataMapObject.put(KeyValue.getKey().toString(), KeyValue.getValue().toString()) ;
		 }
		 System.out.println("== DATA MAP OBJECT =="+dataMapObject);
				return dataMapObject;
				
	 }
	
}
	
	
	

