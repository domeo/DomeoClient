import MySQLdb, sys
import os

DB_CONFIG = "Domeo-DB-config.txt"

def deleteAnnotationInMySQL():

    dbconfig = file = open(DB_CONFIG)
    if dbconfig:
        for line in dbconfig:
            if "USERNAME" in line:
                DB_USER = line[(line.find("USERNAME=")+len("USERNAME=")):line.find(";")]
            elif "PASSWORD" in line:  
                DB_PWD = line[(line.find("PASSWORD=")+len("PASSWORD=")):line.find(";")]

        db = MySQLdb.connect(host= "localhost",
                  user= DB_USER,
                  passwd= DB_PWD,
                  db="DomeoAlphaDev")
    else:
        print "Mysql config file is not found: " + str(dbconfig)

    cursor = db.cursor()

    try:
        cursor.execute("SET SQL_SAFE_UPDATES = 0")
        cursor.execute("SET FOREIGN_KEY_CHECKS=0")
        cursor.execute("DELETE FROM last_annotation_set_index")
        cursor.execute("DELETE FROM annotation_set_index")        
        cursor.execute("DELETE FROM annotation_set_permissions")

        db.autocommit(True)
        db.close()
    except Exception, err:
        print "Exception, roll back"
        sys.stderr.write('ERROR: %s\n' % str(err))
        db.rollback()

def main():
    
    deleteAnnotationInMySQL()
    

if __name__ == "__main__":
    main()
