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

import sk.mapazlocinu.datagetter.DataTableFiller;
import sk.mapazlocinu.datagetter.DataTableFillerPodlaDruhu;
import sk.mapazlocinu.datagetter.DataTableFillerPodlaDruhuTypeCompute;

public enum Dataset {

	podla_druhu(new DataTableFillerPodlaDruhu()),
	podla_nazvov(null),
	podla_paragrafov(null),
	mladez_druhu(null),
	mladez_paragrafov(null),
	cudzinci(null);
	
	private final DataTableFiller dataTableFiller;
	
	private Dataset(DataTableFiller dataTableFiller) {
		this.dataTableFiller = dataTableFiller;
	}

	public DataTableFiller getDataTableFiller() {
		return dataTableFiller;
	}
	
	
	
}
