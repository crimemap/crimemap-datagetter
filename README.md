#Usage

## Data Harvester for minv.sk

### install
arch install (python 3.3+):
pacman -S python
pacman -S python-lxml

### run
cd ./data
python harvester.py

august 2014: 600MB to download.

## Data processing

### install
java 1.7+

### build
mvn clean package

### run
java -jar ./target/datagetter-0.1.jar 

### processing

processed dataset: podla_druhu

removed rows: t.č. s rasovým motívom,extrémiznus
                                        
removed all worksheets except of "BL","TT","TN","NR","ZA","BB","PO","KE" 
	
SKK values (until 2008 included) changed to € (30.126)


#### input
data downloaded by harvester

#### output
4 csv files in different formats + headers names
