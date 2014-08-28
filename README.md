#Usage

## Data Harvester for minv.sk

run using python 3.3 in ./data folder

august 2014: 600MB to download.


## Data processing

### input
data downloaded by harvester

### output
4 csv files in different formats + headers names

### processing
processed dataset: podla_druhu

removed rows: t.č. s rasovým motívom,extrémiznus
                                        
removed all worksheets except of "BL","TT","TN","NR","ZA","BB","PO","KE" 
	
SKK values (until 2008 included) changed to € (30.126)