import random
import json

class Question:
    
    poemList=[];##诗歌列表
    select_poem_id = -1

    dynasty_set=set()
    dynasty_map={}

    
    def __init__(self):
        # self.initPoem("Data/ccpc_test_v1.0.json")
        self.dynasty_map["tang"]="唐朝"
        self.dynasty_map["Jin"]="金"
        self.dynasty_map["Sui"]="隋朝"
        self.dynasty_map["ming"]="明朝"
        self.dynasty_map["Yuan"]="元朝"
        self.dynasty_map["Song"]="宋朝"
        self.dynasty_map["Tang"]="唐朝"
        self.dynasty_map["NanBei"]="南北朝"
        self.dynasty_map["song"]="宋朝"
        self.dynasty_map["Ming"]="明朝"
        
        
    ##导入诗歌
    def initPoem(self,data_path):
        poemfile=open(data_path ,"r",encoding='utf-8')

        while True:
            line=poemfile.readline()
            if line=="":
                break
            else:
                
                result=json.loads(line)
                self.poemList.append(result)

                #self.dynasty_set.add(result["dynasty"])
                   
        poemfile.close();


    #打印诗歌
    def printPoem(self,result):
        print(result["title"]+'\n');
        print(result["author"]
              +'\n');
        poem=result["content"].split("|")
        for item in poem:
            print(item+'\n');

    def question1(self):
        question = "诗歌的作者是什么年代的?"
        print("问题1 : "+question)
        return question

    def answer1(self,poem):
        answer = self.dynasty_map[poem["dynasty"]]
        print("问题1 : "+answer)
        return answer

    def question2(self):    
        question = "诗歌的关键词有哪些?"
        print("问题2 : "+question)
        return question

    def answer2(self,poem):
        answer = poem["keywords"]
        print("问题2 : "+answer)
        return answer


    #提问的主函数
    def askQuestion(self,poem_id):
        if poem_id >= len(self.poemList) or poem_id < 0 :
            print("Wrong poem id!\n")
            return
        
        self.select_poem_id=poem_id
        self.printPoem(self.poemList[poem_id])

        print("问题：")
        #问题1
        self.question1()

        #问题2
        self.question2()

        print("\n\n")


    #输出问题答案
    def showAnswer(self):
        if self.select_poem_id <0 :
            print("ask questions first!\n")
            return

        print("答案：")
        self.answer1(self.poemList[self.select_poem_id])

        self.answer2(self.poemList[self.select_poem_id])

    def printAllDynasty(self):
        for dynasty in self.dynasty_set:
            print("self.dynasty_map[\""+dynasty+"\"]="+"\"\"")
        
    def getQuestions(self,poem):
        q1 = self.question1()
        q2 = self.question2()
        a1 = self.answer1(poem)
        a2 = self.answer2(poem)
        QA = [{"Question":q1,"Answer":a1},{"Question":q2,"Answer":a2}]
        return QA

if __name__=="__main__":
    question=Question()
    question.askQuestion(1)
    question.showAnswer()
    #question.printAllDynasty()
    


    
