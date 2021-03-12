from Database.DatabaseManager import DatabaseManager
import datetime

class MusicManager:
    Index_ID = 0
    Index_UserID = 1
    Index_MusicPath = 2
    Index_Time = 3
    def __init__(self):
        self.databaseManager = DatabaseManager()

    #获取用户合成的音乐
    def getAllMusic(self,userId):
        sql = "select * from Music where userId={}".format(userId)
        results = self.databaseManager.select(sql)
        if not results==None and len(results)>0:
            return {"status":'true',"musics":results}
        else:
            return {"status":'false',"message":"找不到音乐!"}
    #合成好的音乐插入数据库
    def synthesisMusic(self,userId,musicPath):
        sql = "select * from Music"
        results = self.databaseManager.select(sql)
        if not results==None:
            id=len(results)+1
            time = datetime.datetime.now().strftime('%Y-%m-%d %H:%M:%S')
            sql = "insert into Music(id,userId,musicPath,synthesisTime) values({},{},'{}','{}')"\
                    .format(id,userId,musicPath,time)
            resultCode = self.databaseManager.insert(sql)
            if resultCode==1:
                return True
            else:
                return False
        return False
        