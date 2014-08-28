/* Copyright (C) 2014 Miroslav Bimbo
*
* This file is part of Crimemap.
*
* Crimemap is free software: you can redistribute it and/or modify
* it under the terms of the GNU Affero General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* Crimemap is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU Affero General Public License for more details.
*
* You should have received a copy of the GNU Affero General Public License
* along with Crimemap. If not, see <http://www.gnu.org/licenses/>.
*/
package sk.mapazlocinu.datagetter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sk.mapazlocinu.datagetter.config.Config;

public class CsvCreator3dim {
	
	private static SimpleDateFormat DF;
	private static final char separator = '\t';
	
	public void createCsv(List<Worksheet> worksheets, Config config) throws IOException {
		
		DF = config.getDateSetting().getDateFormat();
		
		PrintWriter pw = new PrintWriter(new OutputStreamWriter(
				new FileOutputStream(config.getCsvFileName()+"_3dim.csv"),
				Charset.forName("UTF-8")));
		
		List<String> csvHeader = createCsvHeader(worksheets.get(0));
		writeNext(pw,csvHeader.toArray(new String[csvHeader.size()]));
		
		Map<String,Integer> yearHeaders = new HashMap<String, Integer>();
		Map<String,Integer> regionHeaders = new HashMap<String, Integer>();

		for(Worksheet ws:worksheets){
			List<List<String>> rows = ws.getDatatable().getValues();
			for (int y = 0; y < rows.size(); y++) {
				List<String> csvrow = new ArrayList<String>();
				csvrow.add(getHeaderIndex(ws.getName(),regionHeaders));
				csvrow.add(getHeaderIndex(DF.format(ws.getDate()),yearHeaders));
				csvrow.add(Integer.toString(y));
				for (int x = 0; x < rows.get(y).size(); x++) {
					csvrow.add(getValue(rows.get(y).get(x)));
				}
				writeNext(pw,csvrow.toArray(new String[csvrow.size()]));
			}
		}
		
		pw.close();
		
	}

	private List<String> createCsvHeader(Worksheet worksheet) {
		List<String> columnHeaders = worksheet.getDatatable().getColumnHeaders();
		
		List<String> csvHeader = new ArrayList<String>(columnHeaders.size()+3);
				
		csvHeader.add("region");
		csvHeader.add("year");
		csvHeader.add("type");
		
			for(int x=0;x<columnHeaders.size();x++){
//				String d = columnHeaders.get(x).trim();
//				csvHeader.add(d);
				csvHeader.add("value"+x);
		}

		return csvHeader;
	}
	
	private String getHeaderIndex(String name, Map<String,Integer> map) {
		if(!map.containsKey(name)){
			map.put(name, map.keySet().size());
		}
		return Integer.toString(map.get(name));
	}
	
	private String getValue(String value) {
		if(value==null||value.equals("0")){
			return "";
		}else{
			return value;
		}
	}
	
	private List<String> getList(Map<String, Integer> map) {
		String[] arr = new String[map.size()];
		for(String s:map.keySet()){
			arr[map.get(s)] = s;
		}
		
		return Arrays.asList(arr);
	}
	
	
	
	 public void writeNext(PrintWriter pw ,String[] nextLine) {
	    	
	    	if (nextLine == null)
	    		return;
	    	
	        StringBuilder sb = new StringBuilder(128);
	        for (int i = 0; i < nextLine.length; i++) {

	            if (i != 0) {
	                sb.append(separator);
	            }

	            String nextElement = nextLine[i];
	            if (nextElement == null)
	                continue;
	            
	            sb.append(nextElement);

	        }
	        
	        pw.println(sb.toString());

	    }
	 	 

}
