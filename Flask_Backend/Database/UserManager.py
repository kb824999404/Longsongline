from Database.DatabaseManager import DatabaseManager

class UserManager:
    Index_ID = 0
    Index_UserID = 1
    Index_UserName = 2
    Index_Password = 3
    def __init__(self):
        self.databaseManager = DatabaseManager()
    # 登录
    def login(self,userId,pwd):
        sql = "select * from User where userId='{}'".format(userId)
        results = self.databaseManager.select(sql)
        if results and len(results)>0:
            user = results[0]
            if user[self.Index_Password]==pwd:
                return {"status":'true',"id":user[self.Index_ID]}
            else:
                return {"status":'false',"message":"密码错误！"}
        else:
            return {"status":'false',"message":"用户名不存在！"}
    # 注册
    def register(self,userId,username,pwd):
        sql = "select * from User where userId='{}'".format(userId)
        results = self.databaseManager.select(sql)
        if results and len(results)>0:
            return {"status":'false',"message":"账号已存在！"}
        else:
            sql = "select * from User"
            results = self.databaseManager.select(sql)
            id=len(results)+1
            sql = "insert into User(id,userId,userName,password) values({},'{}','{}','{}')"\
                .format(id,userId,username,pwd)
            resultCode = self.databaseManager.insert(sql)
            if resultCode==1:
                return {"status":'true'}
            else:
                return {"status":'false',"message":"注册失败！"}


if __name__=='__main__':
    userManager = UserManager()
    userManager.showUsers()