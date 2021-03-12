import json
import re
import jieba
import jieba.analyse
from PoetrySearch.question import Question
from configs import config

poemList=[];##诗歌列表
table={};##关键词表
synonymList=[];##同义词表
synonymTable={}##同义词索引表

data_path = config.PoetrySearch['data']
synonym_path = config.PoetrySearch['synonym'] 


def addToTable(s,id):
    if s in table:
        table[s].append(id)
    else:
        table[s]=[id,]
    
##初始化诗歌
def initPoem():
    poemfile=open(data_path,"r",encoding='utf-8')

    id=-1;
    while True:
        line=poemfile.readline();
        if line=="":
            break
        else:
            id=id+1
            result=json.loads(line);
            poemList.append(result)
            key=result["keywords"].split(" ")
            for i in range(0,len(key)):
                addToTable(key[i],id);
                if len(key[i])>1:
                    for s in key[i]:
                        addToTable(s,id)

            poem=result["content"].split("|")
            for item in poem:
                for s in item:
                    addToTable(s,id)
               

    poemfile.close();

##初始化同义词表
def initSynonym():
    synonymfile=open(synonym_path,"r",encoding='utf-8')

    id=-1;
    while True:
        line=synonymfile.readline();
        if line=="":
            break
        else:
            id=id+1
            line=line.replace('\n','')
            item=line.split(" ")
            synonymList.append(item);
            for i in range(1,len(item)):
                synonymTable[item[i]]=id
    ##print(synonymList[0])
    synonymfile.close();


def search(s):
    if s in table:
        return table[s]
    else:
        return [];


WIDTH=5##往两边搜索的宽度



##获得近义词列表
def getSynonymWord(s):
    symList=[]
    if s in synonymTable:
        w=synonymTable[s]
        l=max(0,w-WIDTH)
        r=min(len(synonymList)-1,w+WIDTH)
        for i in range(l,r+1):
            symList+=synonymList[i][1:]     
    return symList


def getSimilarPoemId(description):
    value={}##每个词的权重
    count={}
    main_word=[]##可能有关系的词
    symList=[]
    fullmode_list=jieba.cut(description,cut_all=True)
    keyword_list=jieba.analyse.extract_tags(description,topK=5)
    for s in keyword_list:
        value[s]=100;
        symList=getSynonymWord(s)
        main_word+=symList
        
    for s in fullmode_list:
        if s not in value:
            value[s]=3
            symList=getSynonymWord(s)
            main_word+=symList
      

    for s in main_word:
        ##print(s+'\n')
        w=1
        if s in value:
            w=value[s];
        s_list=search(s)
        for s_id in s_list:
            if s_id in count:
                count[s_id]+=w
            else :
                count[s_id]=w;
    mx=0
    id=0
    for key,value in count.items():
        if value>mx:
            mx=value
            id=key

    return id
 
class Searcher:
    def __init__(self):
        initPoem()
        initSynonym()
        self.question=Question()

    def solve(self,description):
        id=getSimilarPoemId(description)
        ##poemText.insert("insert",id);
        return poemList[id]
    def getPoem(self,index):
        poem = poemList[index]
        if poem["dynasty"] in self.question.dynasty_map.keys():
            poem["dynasty"]=self.question.dynasty_map[poem["dynasty"]]
        return poem
    
    def getPoemList(self,start,num):
        tmpList = poemList[start:start+num]
        tmpList = [{"title":poem["title"],"author":poem["author"],"content":poem["content"]}
          for poem in tmpList ]
        return tmpList
    def getQuestion(self,index):
        return self.question.getQuestions(poemList[index])
        
    
    
if __name__=='__main__':
    data_path="ccpc_test_v1.0.json"
    synonym_path="synonym.txt"
    searcher=Searcher()
    while True:
        description = input("Type in description >> ")
        poem=searcher.solve(description)
        print(poem)

    


    
