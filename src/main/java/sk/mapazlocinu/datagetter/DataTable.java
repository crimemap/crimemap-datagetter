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

public class DataTable {

	private List<String> rowHeaders = new ArrayList<String>();
	private List<String> columnHeaders = new ArrayList<String>();
	private List<List<String>> values = new ArrayList<List<String>>();
	
	
	public List<String> getRowHeaders() {
		return rowHeaders;
	}
	
	public List<String> getColumnHeaders() {
		return columnHeaders;
	}
	public void setColumnHeaders(List<String> columnHeaders) {
		this.columnHeaders = columnHeaders;
	}
	public List<List<String>> getValues() {
		return values;
	}
	
	
	public void addRow(List<String> row){
		addRow(row.get(0), row.subList(1, row.size()));
	}
	
	public void addRow(String[] row){
		addRow(Arrays.asList(row));
	}
	
	public void addRow(String rowHeader,String[] row){
		addRow(rowHeader,Arrays.asList(row));
	}
	
	public void addRow(String rowHeader,List<String> row){
		rowHeaders.add(rowHeader);
		values.add(row);
	}
	
	
	public void replaceRow(String rowHeader,List<String> row){
		int index = rowHeaders.indexOf(rowHeader);
		
		if(index<0){
			throw new IllegalArgumentException("Datatable does not contain row with header"+rowHeader);
		}
		
		values.set(index, row);
	}

	
	
}
