# -*- coding: UTF-8 -*-
import re
import codecs

file2=codecs.open("array.xml","w", encoding='utf-8')

file2.write('<?xml version="1.0" encoding="utf-8"?>\n')
file2.write('<resources>\n')
file2.write('<string-array name="my_array" type="xs:string">\n')


#string_file=raw_input('insert file name\n')

lines = [1,'1-soiree',2,5,6,7,8,9,10,11,12,14,15,16,19,21,22,23,30,31,100]

for line_number in lines:
    for other in [1,2]:
        string_file = str(line_number)+"-"+str(other)+".txt"
        with open(string_file) as f:
            nr_lines = sum(1 for _ in f)
        file=codecs.open(string_file,"r", encoding='utf-8')

        Name=[]

        for i, line in enumerate(file):
            line_splitted = line.split(' ')
            name = []
            times = []
            for ele in line_splitted:
                if not re.search('\d+', ele) and not re.search('-', ele):
                    name.append(ele.lower())
                elif re.search('\d+', ele):
                    times.append(ele)
            Name.append([name, times])
            if i ==0:
                origin = name
            if i == nr_lines-1:
                dest = name
    

        for ele in Name:
            file2.write('<item>\n')
            for i in xrange(len(ele[0])):
                if i<len(ele[0])-1:
                    file2.write(ele[0][i]+'-')
                else:
                    file2.write(ele[0][i]+' ')
            """
            length_origin = len(origin)
            for i,element in enumerate(origin):
                if length_origin == 1 or i==length_origin-1:
                    file2.write(element)
                else:
                    file2.write(element+'+')
            file2.write("-")
            """
            length_dest = len(dest)

            for i, element in enumerate(dest):
                if length_dest == 1 or i==length_dest-1:
                    file2.write(element)
                else:
                    file2.write(element+'+')

            file2.write(" "+str(line_number)+" ")

            for e in ele[1]:
                file2.write(e+' ')



            file2.write('</item>\n')

file2.write('</string-array>\n')
file2.write('</resources>\n')
file2.close()

def replace_all(text, dic):
    for i, j in dic.iteritems():
        text = text.replace(i, j)
    return text

dic1 = {'ô':'o', 'é':'e',' - ':'-', 'è':'e',' -':'-',' -':'-','’':'\''}
    #,
#'cccccccccc':'', 'cccccccccc':'','-c-c-c-c-c-c-c-c-c':'', 'ccccccccc':'','.c.c.c.c.c.c.c.c.c.c':'','.c.c.c.c.c.c.c.c.c':'','+c+c+c+c+c+c+c+c+c':'','-c-c-c-c-c-c-c-c':'','gare-valbonne-sophia-antipolis-c':'gare-valbonne-sophia-antipolis','s+c':'s','-c-c':'','+c+c+c+c+c+c+c+c':'','antipolis-c':'antipolis',
#'gare+valbonne+sophia+antipolis+c':'gare+valbonne+sophia+antipolis','antipolis+c':'antipolis','ant.':'antibes',
#'lis-c':'lis'
#}

dic2 = {'gr.+vsa':'gare+valbonne+sophia+antipolis','gr.+antibes':'gare+antibes','a.+desnos':'albert+desnos',
'hop.+fontonne':'hopital+fontonne','gr.+valbonne':'gare+valbonne','gr+antibes':'gare+antibes',
'chap.':'chapelle'
}


dic3 = {'lyc.-audiberti':'lycee-audiberti', 'chap.-combes':'chapelle-des-combes', 'gr.-vsa':'gare-valbonne-sophia-antipolis','gr.-antibes':'gare-antibes','pôle-echanges-ant.':'pole-echanges-antibes','chap.-des-combes':'chapelle-des-combes','a.-desnos':'albert-desnos','hop.-fontonne':'hopital-fontonne',
'sq.-nabonnand':'square-nabonnand','terr.':'terres','lyc.horticole':'lycee-horticole','lyc.-horticole':'lycee-horticole','pl.-de-gaulle-ot':'place-de-gaulle-ot','ch.-des-soulieres':'chemin-des-soulieres','ch.-soulieres':'chemin-des-soulieres','gr.-valbonne':'gare-valbonne','gr.valbonne':'gare-valbonne',
'gr-antibes':'gare-antibes','gr.vsa-depart':'gare-valbonne-sophia-antipolis-depart','pl.-de-gaulle':'place-de-gaulle','v.loubet':'villeneuve-loubet'
}


dic = {}
dic.update(dic1)
#dic.update(dic2)
#dic.update(dic3)


with open("array.xml",'r') as f:
    newlines = []
    for line in f.readlines():
        newlines.append(replace_all(line, dic))
with open("array.xml", 'w') as f:
    for line in newlines:
        f.write(line)




