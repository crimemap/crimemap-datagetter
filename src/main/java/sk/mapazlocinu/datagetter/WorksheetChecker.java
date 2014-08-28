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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.xpath.XPathExpressionException;

public class WorksheetChecker {

	private static List<String> SAMPLE_COLUMNS = null;
	private static List<String> SAMPLE_ROWS = null;
	private static Map<Integer,Integer> YEAR_COUNTER = new HashMap<Integer, Integer>();
	
	public void check(List<Worksheet> worksheets) {
		int ok = 0;
		List<Worksheet> wrong = new ArrayList<Worksheet>();
		for(Worksheet w:worksheets){
			try{
				check(w);
				ok++;
			}catch(Exception e){
				System.out.println("Error during checking worksheet:"+w+" "+e);
				wrong.add(w);
			}
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(w.getDate());
			Integer year = calendar.get(Calendar.YEAR);
			if(YEAR_COUNTER.containsKey(year)){
				YEAR_COUNTER.put(year, YEAR_COUNTER.get(year)+1);
			}else{
				YEAR_COUNTER.put(year, 1);
			}
		}
		
		SAMPLE_COLUMNS = null;
		SAMPLE_ROWS = null;
		
		System.out.println(YEAR_COUNTER);
		System.out.println("---------------Summary-----------------");
		System.out.println("OK:"+ok);
		System.out.println("Wrong:"+wrong.size());
		
	}

	
	public void check(Worksheet ws) throws XPathExpressionException {

		checkSource(ws);
		checkDate(ws);
		checkColumnHeaders(ws);
		checkRowHeaders(ws);
		chceckValues(ws);
	}



	private void chceckValues(Worksheet ws) {
		
		for(List<String> row:ws.getDatatable().getValues()){
			for(String value: row){
				Helper.isTrue(Helper.isBlank(value) || Integer.decode(value)>=0, "Negative value found!");
			}
		}
		
	}


	/** Zatial iba Velke pismena*/
	private void checkRowHeaders(Worksheet ws) {
		
		List<String> rowHeaders = ws.getDatatable().getRowHeaders();
		
		if(SAMPLE_ROWS==null){
			SAMPLE_ROWS=rowHeaders;
			System.out.println("Row headers:"+rowHeaders);	
		}
		
		
		Helper.isTrue(SAMPLE_ROWS.equals(rowHeaders),"V tomto worksheete nesedia headre riadkov!"+rowHeaders);
	}


	private void checkColumnHeaders(Worksheet ws) {
		List<String> columnHeaders = ws.getDatatable().getColumnHeaders();
		if(SAMPLE_COLUMNS==null){
			SAMPLE_COLUMNS=columnHeaders;
			System.out.println("Columns:"+columnHeaders);	
		}
		
		Helper.isTrue(SAMPLE_COLUMNS.equals(columnHeaders),"V tomto worksheete nesedia stlpce!"+columnHeaders);
	}


	private void checkDate(Worksheet ws) {
		
		String contentDateFiled = ws.getXY('A',2);
		Date contentDate = null;
		try {
			contentDate = Helper.parseDate(contentDateFiled);
		} catch (ParseException e) {
			throw new RuntimeException("Can not parse date from content",e);
		}
		
		Helper.isTrue(contentDate.equals(ws.getDate()),"Datum v mene suboru a v jeho obsahu sa nezhoduje!");

	}


	private void checkSource(Worksheet ws) {
		Helper.isTrue("Zdroj Ministerstvo vnútra SR".equals(ws.getXY(0,0)),"Na pozicii A1 nie je ocakavana hodnota");
	}
	
	


}
