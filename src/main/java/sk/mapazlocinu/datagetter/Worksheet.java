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

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import sk.mapazlocinu.datagetter.config.Dataset;

public class Worksheet {

	private File file;
	private String name;
	
	//raw data parsed from worksheet
	private String[][] data;
	private Date date;
	private Dataset dataset;

	//selected parts of data
	private DataTable datatable = new DataTable();
	
	public Worksheet(File file, String name, String[][] data,Date date) {
		this.file = file;
		this.name = name;
		this.data = data;
		this.date = date;
		this.dataset = Dataset.valueOf(file.getParentFile().getName());
	}
	

	public String getXY(char x, int y){
		return getXY(x-'A', y-1);
	}

	public String getXY(int x, int y) {
		return data[y][x];
	}
	
	public List<String[]> getRowsByRegex(String regex){

		List<String[]> rows = new ArrayList<String[]>();
		
		for(int y=0;y<data.length-1;y++){
			if(data[y][0]!=null && data[y][0].matches(regex)){
				rows.add(data[y]);
			}
		}
		
		return rows;
	}
	

	
	public File getFile() {
		return file;
	}
	public String getName() {
		return name;
	}
	public String[][] getData() {
		return data;
	}
	
	public Date getDate() {
		return date;
	}

	public Dataset getDataset() {
		return dataset;
	}

	public DataTable getDatatable() {
		return datatable;
	}

	public void setDatatable(DataTable datatable) {
		this.datatable = datatable;
	}
	
	@Override
	public String toString() {
		return "Worksheet [file=" + file + ", name=" + name + "]";
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dataset == null) ? 0 : dataset.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Worksheet other = (Worksheet) obj;
		if (dataset != other.dataset)
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}


	
	

}

