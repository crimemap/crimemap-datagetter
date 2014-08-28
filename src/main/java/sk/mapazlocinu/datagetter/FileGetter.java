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
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import sk.mapazlocinu.datagetter.config.Config;

public class FileGetter {

	public List<File> getFiles(Config config) throws URISyntaxException {
		
		File dir = new File("data/"+config.getDataset().toString());
		
		File[] allFiles = dir.listFiles();
		
		
		List<File> filteredFiles = new ArrayList<File>();
		
		for(File file:allFiles){
			if(file.getName().matches(config.getDateSetting().getRegex())
					&& (Helper.isBlank(config.getCustomRegex()) || file.getName().matches(config.getCustomRegex()))){
				filteredFiles.add(file);
			}
		}
		
		return filteredFiles;
	}
	



	
}
