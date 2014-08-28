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
package sk.mapazlocinu.datagetter.config;

import java.text.SimpleDateFormat;

public enum DateSetting {

	YEARS("^.*12\\.[0-9]{4}\\.xml$","yyyy"),
	MONTHS("^.*2009\\.xml$||^.*201[0-9]{1}\\.xml$","MM.yyyy"),
	ALL("^.*xml$","MM.yyyy");
	
	private final String regex;
	private final SimpleDateFormat dateFormat;
	
	private DateSetting(String regex,String dateFormat) {
		this.regex = regex;
		this.dateFormat = new SimpleDateFormat(dateFormat);
	}

	public String getRegex() {
		return regex;
	}

	public SimpleDateFormat getDateFormat() {
		return dateFormat;
	}
	
	
	
}
