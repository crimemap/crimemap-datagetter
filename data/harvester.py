import urllib.request
import re
import os
from lxml import html
from lxml import etree
import sys
from collections import OrderedDict
import itertools

def getFiles(start,end,address,pattern,dirname):

    if(not os.path.exists(dirname)):
        os.mkdir(dirname)

    for rok in range(start,end+1):

        print("------%s------" % str(rok))
        
        url = address % str(rok)
        page = html.fromstring(urllib.request.urlopen(url).read())

        p = re.compile(pattern)
        suffix = re.compile("\\.xml$")

        counter = 0

        for link in page.xpath("//a"):
            filename = link.get("href").split("/")[-1]
            if(p.findall(filename) and suffix.findall(filename)):
                counter+=1
                file = dirname+"/"+filename
                if(os.path.exists(file)):
                    print("exists:"+str(counter)+")"+link.get("href"))
                else:
                    try:
                        print("downloading:%(number)02d)%(link)s" % {"number":counter,"link":link.get("href")})
                        urllib.request.urlretrieve(("http://www.minv.sk/"+link.get("href")).replace(" ","%20"),file)
                    except (ConnectionResetError,urllib.error.ContentTooShortError):
                        print("Failed to get file"+link.get("href"))

                try:
                    doc = etree.parse(file)
                except (etree.XMLSyntaxError):
                    print("canot parse, try to redownload:%(number)02d)%(link)s" % {"number":counter,"link":link.get("href")})
                    os.remove(file)
                    urllib.request.urlretrieve(("http://www.minv.sk/"+link.get("href")).replace(" ","%20"),file)

        if(counter!=12):
            print("Warning: mozno chybne nacitanie suborov pre rok %s (nie je 12 suborov)" % str(rok))


def getAll(pattern,dirname):
    print("-------------------------------%s--------------------------" % dirname)
    getFiles(3,3,"http://www.minv.sk/?statistika-kriminality-v-slovenskej-republike-kopia-%s",pattern,dirname)
    getFiles(2009,2012,"http://www.minv.sk/?statistika-kriminality-v-slovenskej-republike-za-rok-%s",pattern,dirname)
    getFiles(2013,2013,"http://www.minv.sk/?statistika_kriminality_v_slovenskej_republike_za_rok_%s",pattern,dirname)
    getFiles(2014,2014,"http://www.minv.sk/?kriminalita_%s_xml",pattern,dirname)

def is_number(s):
    if s is None:
        return False
    try:
        int(s)
        return True
    except ValueError:
        return False

patterns = {
            "podla_druhu":"druhu|kriminalitasr_1",
            "podla_nazvov":"nazvov|kriminalitasr_2",
            "podla_paragrafov":"paragraf|kriminalitasr_3",
            "mladez_druhu":"4-|_4|4_|4xml|4%20",
            "mladez_paragrafov":"5-|_5|5_|5xml|5%20",
            "cudzinci":"6-|_6|6_|6xml|6%20"
            }


for name in patterns.keys():
    getAll(patterns[name],name)

#toto su jedine subory co nesedia podla patternov, najjednoduchsie riesenie je ich zmazat (pravdepodobne uz sa podobna vynimka nezopakuje)
if(os.path.exists("podla_paragrafov/TC spachane na mladezi podla paragrafov_5_31.12.2008.xml")):
    os.remove("podla_paragrafov/TC spachane na mladezi podla paragrafov_5_31.12.2008.xml")

if(os.path.exists("podla_druhu/Statistika%20podla%20druhu%20kriminality_1_31.12.2008.xml")):
    os.remove("podla_druhu/Statistika%20podla%20druhu%20kriminality_1_31.12.2008.xml")


