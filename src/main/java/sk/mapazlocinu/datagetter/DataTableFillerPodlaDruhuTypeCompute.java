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
import java.util.List;

public class DataTableFillerPodlaDruhuTypeCompute extends DataTableFillerPodlaDruhu implements DataTableFiller  {

	@Override
	protected void transformCurrency(Worksheet ws) {
		transformCurrency(ws,2);
	}

	@Override
	protected DataTable addRows(Worksheet worksheet) {
		
		DataTable dataTable = worksheet.getDatatable();
		String[][] data = worksheet.getData();
		
		String specialRowHeader = null;
		List<Integer> specialRow = null;
		
		
		
		//start on 6th line
		for (int y = 5; y < data.length; y++) {
			if(Helper.isNotBlank(data[y][0]) && data[y][0].matches("^[^\\s].*")){
				//DO NOTHING
				if(data[y][0].startsWith("t.č. s rasovým motívom") 
						|| data[y][0].startsWith("extrémiznus")
						|| data[y][0].startsWith("s rasovým mot.,extrémizmus")
						|| data[y][0].startsWith("t.č. s rasovým motívom")
						|| data[y][0].startsWith("VŠEOBECNÁ  KRIMINALITA  SPOLU")
						|| data[y][0].startsWith("CELKOVÁ  KRIMINALITA")){
				
					//remove this rows, do nothing
					
				}else if(data[y][0].startsWith("organizovaný zločin")){
					// create it's own category
					dataTable.addRow(
							data[y][0].trim()+"-"+data[y][0].trim().toUpperCase(),
							getBasicRow(getSpecialRow(data[y]))
						);
				}else if(data[y][0].matches("^[A-Z].*")){
					//write old category's supplement
					if(specialRowHeader!=null){
						
						dataTable.addRow(
								"doplnok"+"-"+specialRowHeader,
								getBasicRow(specialRow)
							);
							
					}
					//create new category
					specialRowHeader = data[y][0].trim();
					specialRow = getSpecialRow(data[y]);
				}else{
					//create a record with selected category
					Helper.isTrue(specialRow!=null, "Row have none category");
					Helper.isTrue(specialRowHeader!=null, "Row have none category");
					dataTable.addRow(
							data[y][0].trim()+"-"+specialRowHeader,
							getBasicRow(getSpecialRow(data[y]))
						);	
					specialRow = subtractRows(specialRow, getSpecialRow(data[y]));
				}
			}
		}
		
		//write last category
		dataTable.addRow(
				"doplnok"+"-"+specialRowHeader,
				getBasicRow(specialRow)
			);
		return dataTable;
	}
	
	private List<Integer> subtractRows(List<Integer> specialRow, List<Integer> specialRow2) {
		Helper.isTrue(specialRow.size()==specialRow2.size(),"Can not subtract unequal rows");
		List<Integer> result = new ArrayList<Integer>();
		for (int i = 0; i < specialRow.size(); i++) {
			result.add(specialRow.get(i)-specialRow2.get(i));
		}
		return result;
	}

	private String[] getBasicRow(List<Integer> specialRow){
		String[] basicRow = new String[specialRow.size()];
		for (int i = 0; i < specialRow.size(); i++) {
			basicRow[i] = specialRow.get(i).toString();	
		}
		return basicRow;
	}

	private List<Integer> getSpecialRow(String[] row) {
		List<Integer> specialRow = new ArrayList<Integer>(row.length);
		for(int i = 1;i<row.length;i++){
			if(i==3){
				//odstranit objasnenost, neda sa odpocitavat, da sa ale doratat dodatocne v JS
				continue;
			}
			if(Helper.isNotBlank(row[i])){
				specialRow.add(Integer.valueOf(row[i]));
			}else{
				specialRow.add(new Integer(0));
			}
		}
		
		return specialRow;
	}
	

	
}
