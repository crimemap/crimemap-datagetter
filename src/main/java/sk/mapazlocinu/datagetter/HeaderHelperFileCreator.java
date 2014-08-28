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

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class HeaderHelperFileCreator {

	public void createHeaderHelperFile(List<List<String>> rows, String fileName) throws FileNotFoundException, UnsupportedEncodingException {
		
		
		PrintWriter writer = new PrintWriter(fileName, "UTF-8");
		for(List<String> row:rows){
			writer.println(getQuotedList(row));
		}
		
		writer.close();
		
	}

	private List<String> getQuotedList(List<String> list) {
		List<String> quoted = new ArrayList<String>(list.size());
		for(String s:list){
			quoted.add("\""+s+"\"");
		}
		return quoted;
	}
	
	
	

}
