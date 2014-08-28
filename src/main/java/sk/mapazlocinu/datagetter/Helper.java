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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class Helper {

	public static XPath getXpath() {
		NamespaceContext context = new NamespaceContextMap
				("urn", "urn:schemas-microsoft-com:office:spreadsheet",
			     "ss","urn:schemas-microsoft-com:office:spreadsheet");
		
		XPath xPath =  XPathFactory.newInstance().newXPath();
		xPath.setNamespaceContext(context);
		return xPath;
	}

	public static Document getDocument(File file) throws ParserConfigurationException, FileNotFoundException, SAXException, IOException {
		DocumentBuilderFactory builderFactory =
		        DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = builderFactory.newDocumentBuilder();
		
		Document document = builder.parse(file);
		return document;
	}
	
	
	public static Date parseDate(String s) throws ParseException{
		Pattern datePattern = Pattern.compile("[0-9]{1,2}\\.[0-9]{1,2}\\.[0-9]{4}");
		Matcher m = datePattern.matcher(s);
		String dateValue = "";
		if(m.find()){
			dateValue = m.group();
		}
		
		Helper.isTrue(Helper.isNotBlank(dateValue) && dateValue.split("\\.").length == 3,"Neocakavana hodnota datumu, neda sa vyparsovat:"+s);
		
		DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
		return df.parse(dateValue);
	}
	
	public static void isTrue(boolean expression, String message) {
        if (expression == false) {
            throw new IllegalArgumentException(message);
        }
    }
	
    public static boolean isNotBlank(String str) {
        return !Helper.isBlank(str);
    }
	
    public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(str.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }
	
}
