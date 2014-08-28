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
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import sk.mapazlocinu.datagetter.config.Config;
import sk.mapazlocinu.datagetter.config.Dataset;
import sk.mapazlocinu.datagetter.config.DateSetting;

public class Main {

	public static void main(String[] args) throws URISyntaxException, IOException {

		
		Config config = getConfig(args);
		

//		config.setCustomRegex("^.*\\.2013\\.xml$");
		
		FileGetter fileGetter = new FileGetter();
		List<File> files = fileGetter.getFiles(config);
		
		WorksheetGetter worksheetGetter = new WorksheetGetter();
		List<Worksheet> worksheets = worksheetGetter.getWorksheets(files);
		
		DataTableFiller dataTableFiller = config.getDataset().getDataTableFiller();
		dataTableFiller.fillDataTables(worksheets);
		
		WorksheetChecker worksheetChecker = new WorksheetChecker();
		worksheetChecker.check(worksheets);
		
		CsvCreator csvCreator = new CsvCreator();
		csvCreator.createCsv(worksheets,config);
		
		CsvCreator3dim csvCreator3dim = new CsvCreator3dim();
		csvCreator3dim.createCsv(worksheets,config);
	
		CsvCreatorFiveCols csvCreatorFiveCols = new CsvCreatorFiveCols();
		csvCreatorFiveCols.createCsv(worksheets, config);
		
		/*----*/
		
		List<Worksheet> worksheets2 = worksheetGetter.getWorksheets(files);
		
		DataTableFiller dataTableFiller2 = new DataTableFillerPodlaDruhuTypeCompute();
		dataTableFiller2.fillDataTables(worksheets2);
		
		WorksheetChecker worksheetChecker2 = new WorksheetChecker();
		worksheetChecker2.check(worksheets2);
		
		CsvCreator3dimTypeHierarchy csvCreator3dimTypeHierarchy = new CsvCreator3dimTypeHierarchy();
		csvCreator3dimTypeHierarchy.createCsv(worksheets2,config);
	}

	private static Config getConfig(String[] args) {
		Config config = new Config();
		
		if(args!=null){
			for (int i = 0; i < args.length; i++) {
				if(args[i].equals("-date")){
					i++;
					Helper.isTrue(args.length>i && Helper.isNotBlank(args[i]), "argument after -date have to be one of "+Arrays.asList(DateSetting.values()));
					DateSetting ds = DateSetting.valueOf(args[i]);
					config.setDateSetting(ds);
					continue;
				}
				
				if(args[i].equals("-dataset")){
					i++;
					Helper.isTrue(args.length>i && Helper.isNotBlank(args[i]), "argument after -dataset have to be one of "+Arrays.asList(Dataset.values()));
					Dataset dataset = Dataset.valueOf(args[i]);
					config.setDataset(dataset);
					continue;
				}
				
				if(args[i].equals("-o")){
					i++;
					Helper.isTrue(args.length>i && Helper.isNotBlank(args[i]), "argument after -o not specified ");
					config.setCsvFileName(args[i]);
					continue;
				}
				
//				if(args[i].equals("-customRegex")){
//					i++;
//					Helper.isTrue(args.length>i && Helper.isNotBlank(args[i]), "argument after -customRegex not specified ");
//					config.setCustomRegex(args[i]);
//					continue;
//				}
				
			}
			
		}
		return config;
	}



}
