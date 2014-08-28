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
import java.util.List;

import sk.mapazlocinu.datagetter.config.Config;

public class CsvCreator {
	
	private static SimpleDateFormat DF;
	private static final char separator = '\t';
	
	public void createCsv(List<Worksheet> worksheets, Config config) throws IOException {
		
		DF = config.getDateSetting().getDateFormat();
		
		PrintWriter pw = new PrintWriter(new OutputStreamWriter(
				new FileOutputStream(config.getCsvFileName()),
				Charset.forName("UTF-8")));
		
		List<String> csvHeader = createCsvHeader(worksheets.get(0));
		writeNext(pw,csvHeader.toArray(new String[csvHeader.size()]));
		
		for(Worksheet ws:worksheets){
			List<String> csvRow = createCsvRow(ws);
			writeNext(pw,csvRow.toArray(new String[csvRow.size()]));
		}
		
		pw.close();
		
	}

	private List<String> createCsvHeader(Worksheet worksheet) {
		List<String> columnHeaders = worksheet.getDatatable().getColumnHeaders();
		List<String> rowHeaders = worksheet.getDatatable().getRowHeaders();
		
		List<String> csvHeader = new ArrayList<String>(columnHeaders.size()*rowHeaders.size()+10);
				
		csvHeader.add("Kraj");
		csvHeader.add("Dátum");
		
		for(int y=0;y<rowHeaders.size();y++){
			for(int x=0;x<columnHeaders.size();x++){
				String d = rowHeaders.get(y).trim()+" "+columnHeaders.get(x).trim();
				csvHeader.add(d);
			}
		}

		return csvHeader;
	}

	private List<String> createCsvRow(Worksheet ws) {
		
		List<String> csvRow = new ArrayList<String>();
		csvRow.add(ws.getName());
		csvRow.add(DF.format(ws.getDate()));
		
		List<List<String>> rows = ws.getDatatable().getValues();
		
		for(List<String> row:rows){
			for(String value:row){
				if(value==null||value.equals("0")){
					csvRow.add("");
				}else{
					csvRow.add(value);
				}
			}
		}
		
		return csvRow;
		
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
