import MySQLdb
from configs import config

class DatabaseManager:
    def __init__(self):
        pass
    # 连接数据库
    def _connect(self):
        db = MySQLdb.connect(config.DATABASE_HOST, config.DATABASE_USERNAME, 
        config.DATABASE_PASSWORD, config.DATABASE_SCHEMA, charset='utf8' )
        return db
    # 查询语句
    def select(self,statement):
        db  = self._connect()
        cursor = db.cursor()
        results = None
        try:
            cursor.execute(statement)
            results = list(cursor.fetchall())
        except:
            print("Error: unable to fecth data")
        db.close()
        return results
    # 插入语句
    def insert(self,statement):
        resultCode = 0
        db  = self._connect()
        cursor = db.cursor() 
        try:
            cursor.execute(statement)
            db.commit()
            resultCode = 1
        except:
            db.rollback()
        db.close()
        return resultCode
    # 更新语句
    def update(self,statement):
        resultCode = 0
        db  = self._connect()
        cursor = db.cursor() 
        try:
            cursor.execute(statement)
            db.commit()
            resultCode = 1
        except:
            db.rollback()
        db.close()
        return resultCode

    # 删除语句        
    def delete(self,statement):
        resultCode = 0
        db  = self._connect()
        cursor = db.cursor() 
        try:
            cursor.execute(statement)
            db.commit()
            resultCode = 1
        except:
            db.rollback()
        db.close()
        return resultCode
    



if __name__=='__main__':
    databaseManager = DatabaseManager()
    print(databaseManager._connect())