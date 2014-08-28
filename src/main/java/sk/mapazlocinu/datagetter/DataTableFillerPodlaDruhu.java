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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataTableFillerPodlaDruhu implements DataTableFiller  {

	public void fillDataTables(List<Worksheet> worksheets){
		for(Worksheet ws : worksheets){
			fillDataTable(ws);
		}
	}
	
	public void fillDataTable(Worksheet worksheet) {

		addColumnHeaders(worksheet);
		addRows(worksheet);
		transformCurrency(worksheet);
		
	}

	protected void transformCurrency(Worksheet ws) {
		transformCurrency(ws,3);
	}
	
	protected void transformCurrency(Worksheet ws,int column) {
		DataTable dataTable = ws.getDatatable();
		String currency = getCurrency(ws);
		
		if(currency.equals("SKK")){
//			int column = 2;
			for(List<String> row: dataTable.getValues()){
				String val = row.get(column);
				val = String.valueOf(Math.round((Integer.decode(val.trim()) / 30.126)));
				row.set(column, val);
			}
		}
	}
	
	


	protected String getCurrency(Worksheet ws) {
		String currencyField = ws.getXY('B', 4);
		if(currencyField.contains("€")){
			return "EUR";
		}
		Helper.isTrue(currencyField.contains("SKK"),"Failed to parse currency from:"+currencyField);
//		System.out.println("SKK v "+ws.getName()+" : "+ws.getFile().getName());
		return "SKK";
	}


	protected DataTable addRows(Worksheet worksheet) {
		
		DataTable dataTable = worksheet.getDatatable();
		int width = dataTable.getColumnHeaders().size();
		String[][] data = worksheet.getData();
		
		//start on 6th line
		for (int y = 5; y < data.length; y++) {
			if(Helper.isNotBlank(data[y][0]) && data[y][0].matches("^[^\\s].*")){
				if(data[y][0].startsWith("t.č. s rasovým motívom") 
						|| data[y][0].startsWith("extrémiznus")
						|| data[y][0].startsWith("s rasovým mot.,extrémizmus")
						|| data[y][0].startsWith("t.č. s rasovým motívom")){

					//DO NOTHING

				}else{
					dataTable.addRow(data[y][0].trim(),Arrays.copyOfRange(data[y], 1, width+1));	
				}
			}
		}
		return dataTable;
	}
	
	
	
	protected void addColumnHeaders(Worksheet worksheet) {
		List<String[]> rawColumnHeadersList = worksheet.getRowsByRegex("^Druh kriminality.*");
		Helper.isTrue(rawColumnHeadersList.size()==1,"Nemozno urcit na ktorom riadku su nazvy stlpcov");
		String[] rawColumnHeaders = rawColumnHeadersList.get(0);
		List<String> columnHeaders = new ArrayList<String>();
		for(int i=1;i<rawColumnHeaders.length;i++){
			if(Helper.isNotBlank(rawColumnHeaders[i])){
				if(i==3){
					continue;
				}
				if(i==4){
					columnHeaders.add(rawColumnHeaders[i]+" (tisíc €)");
					continue;
				}
				if(i>=6 && i<=9){
					columnHeaders.add(rawColumnHeaders[i]+" (počet činov)");
					continue;
				}
				if(i>=11){
					columnHeaders.add(rawColumnHeaders[i]+" (počet osôb)");
					continue;
				}
				columnHeaders.add(rawColumnHeaders[i]);
			}
		}
		worksheet.getDatatable().setColumnHeaders(columnHeaders);
	}

	

	
}
