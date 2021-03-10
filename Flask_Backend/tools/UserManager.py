import sqlite3

class UserManager:
        def __init__(self,sqlFileName):
                self.sqlFileName=sqlFileName
        
        def init(self):
                conn = sqlite3.connect(self.sqlFileName)
                c = conn.cursor()
                c.execute("CREATE TABLE User ( \
                        id INT(6) PRIMARY KEY, \
                        username VARCHAR(30) NOT NULL,\
                        passwd VARCHAR(32) NOT NULL\
                        )")
                conn.commit()
                conn.close()
        def showDatabase(self):
                conn = sqlite3.connect(self.sqlFileName)
                c = conn.cursor()
                cursor = c.execute("select * from user")
                for row in cursor:
                        print("ID:{}\tUsername:{}\tPasswd:{}".format(row[0],row[1],row[2]))
                conn.close()
        def register(self,username,pwd):
                conn = sqlite3.connect(self.sqlFileName)
                c = conn.cursor()
                cursor = c.execute("select * from user where username='{}'".format(username))
                if len(list(cursor))>0:
                        conn.close()
                        return {"status":'false',"message":"用户名已存在！"}
                else:
                        cursor = c.execute("select * from user")
                        id=len(list(cursor))+1
                        c.execute("INSERT INTO user(id,username,passwd)VALUES({},'{}','{}')"
                        .format(id,username,pwd))
                        conn.commit()
                        conn.close()
                        return {"status":'true'}
        def login(self,username,pwd):
                conn = sqlite3.connect(self.sqlFileName)
                c = conn.cursor()
                cursor = c.execute("select * from user where username='{}'".format(username))
                users=list(cursor)
                if len(users)>0:
                        user=users[0]
                        if user[2]==pwd:
                                conn.close()
                                return {"status":'true',"id":user[0]}
                        else:
                                conn.close()
                                return {"status":'false',"message":"密码错误！"}
                else:
                        conn.close()
                        return {"status":'false',"message":"用户名不存在！"}

                

if __name__=='__main__':
        userManager=UserManager("../data/user.db")
        userManager.init()