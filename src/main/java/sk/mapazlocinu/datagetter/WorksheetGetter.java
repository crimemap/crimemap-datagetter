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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class WorksheetGetter {
	
	
	public static final Set<String> KRAJE = new HashSet<String>(Arrays.asList(new String[]{"BL","TT","TN","NR","ZA","BB","PO","KE"}));
	
	private final XPath XPATH = Helper.getXpath();

	public List<Worksheet> getWorksheets(List<File> files){
		
		List<Worksheet> worksheets = new ArrayList<Worksheet>(files.size()*10);
		
		for(File file:files){
			System.out.println("Loading worksheets from file:"+file);
			List<Worksheet> fileWorksheets = getWorksheets(file);
			if(KRAJE.size()!=fileWorksheets.size()){
				throw new RuntimeException("ERROR, pravdepodobne chyba nejaky kraj:"+file);
			}
			worksheets.addAll(fileWorksheets);
			
		}
		
		return worksheets;
	}

	public List<Worksheet> getWorksheets(File file) {
		
		List<Worksheet> worksheets = new ArrayList<Worksheet>();
		
		
		Document document;
		try {
			document = Helper.getDocument(file);
		} catch (Exception e1) {
			System.out.println("CANNOT PARSE FILE:"+file+e1);
			return worksheets;
		}
		
		try {
			XPathExpression expression = XPATH.compile("/Workbook/Worksheet");
			NodeList nodeList = (NodeList)expression.evaluate(document,XPathConstants.NODESET);
			for(int i=0; i<nodeList.getLength(); i++){
				Node node = nodeList.item(i);
				
				Worksheet ws = getWorksheet(node,file);
				if(ws!=null){
					worksheets.add(ws);
				}
						
			}
		} catch (Exception e) {
			System.out.println("Parsing error in file:"+file+"; "+e);
			e.printStackTrace();
		}
		
		return worksheets;
		
	}
	

	private Worksheet getWorksheet(Node node,File file) throws XPathExpressionException, ParseException {
		
		String name = ((Element)node).getAttribute("ss:Name");
		if(Helper.isBlank(name) || !KRAJE.contains(name)){
			return null;
		}
		
		XPathExpression expression = XPATH.compile("Table/Row");
		NodeList nodeList = (NodeList)expression.evaluate(node,XPathConstants.NODESET);
		
		String[][] data = getData(nodeList);
		
		
		Date datum = Helper.parseDate(file.getName());

		Worksheet ws = new Worksheet(file,name,data,datum);

		return ws;
	}

	private String[][] getData(NodeList nodes) {
		int xmax = 0;
		for(int y=0;y<nodes.getLength();y++){
			NodeList row = nodes.item(y).getChildNodes();
			if(row.getLength()/2>xmax){
				xmax=row.getLength();
			}
		}
		
		String[][] data = new String[nodes.getLength()][xmax];
		
		for(int y=0;y<nodes.getLength();y++){
			NodeList row = nodes.item(y).getChildNodes();
			
			for(int x=1;x<row.getLength();x=x+2){
				if(row.item(x).hasChildNodes()){
					data[y][x/2] = row.item(x).getChildNodes().item(0).getChildNodes().item(0).getNodeValue();
				}else{
				}
			}
			
		}
		return data;
	}



	
}
