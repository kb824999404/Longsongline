from Database.DatabaseManager import DatabaseManager
import datetime

class MusicManager:
    Index_ID = 0
    Index_UserID = 1
    Index_Name = 2
    Index_MusicPath = 3
    Index_Time = 4
    Index_Content = 5
    def __init__(self):
        self.databaseManager = DatabaseManager()

    #获取用户合成的音乐
    def getAllMusic(self,userId):
        sql = "select * from Music where userId={}".format(userId)
        results = self.databaseManager.select(sql)
        if not results==None:
            return {"status":'true',"musics":results}
        else:
            return {"status":'false',"message":"找不到音乐!"}
    #合成好的音乐插入数据库
    def synthesisMusic(self,userId,name,content):
        sql = "select * from Music"
        results = self.databaseManager.select(sql)
        id = -1
        if not results==None:
            id=len(results)+1
            time = datetime.datetime.now().strftime('%Y-%m-%d %H:%M:%S')
            sql = "insert into Music(id,userId,name,synthesisTime,content,status) values({},{},'{}','{}','{}',0)"\
                    .format(id,userId,name,time,content)
            resultCode = self.databaseManager.insert(sql)
            if resultCode==1:
                return True,id
            else:
                return False,id
        return False,id
    def synthesisDone(self,id,musicPath):
        sql = "update Music set status=1,musicPath='{}' where id={}".format(musicPath,id)
        resultCode = self.databaseManager.update(sql)
        if resultCode == 1:
            return True
        else:
            return False
        