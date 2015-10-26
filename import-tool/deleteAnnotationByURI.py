'''
Created 06/05/2014

@authors: Yifan Ning

@summary: clean DDI sets in COLLECTION of elasticsearch DB

'''

import MySQLdb, sys
import os
from elasticsearch import Elasticsearch
from subprocess import call

#LOCAL_IP = "130.49.206.86"
#PORT = "8080"
DB_CONFIG = "Domeo-DB-config.txt"

if len(sys.argv) > 3:
    COLLECTION = str(sys.argv[1])
    LOCAL_IP = str(sys.argv[2])
    PORT = str(sys.argv[3])
 
else:
	print "Usage: deleteAnnotationByURI <collection> <local ip> <port>"
	sys.exit(1)


def deleteAnnotationInMySQLAndElastico(annotates_url):

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

        cursor.execute("SET SQL_SAFE_UPDATES =0")
        db.commit()
        cursor.execute("SET FOREIGN_KEY_CHECKS=0")
        db.commit()

        sql0 = "SELECT mongo_uuid FROM annotation_set_index WHERE annotates_url ='" + annotates_url +"'"
    
        cursor.execute(sql0)
        results_mongo = cursor.fetchall()

        if results_mongo:
        
            print "[INFO] begin delete (%s) annotations by url %s" % (str(len(results_mongo)), annotates_url)
            for row in results_mongo:
                print row[0]
                deleteAnnotationInES(row[0])


            # get list of last_version_id to delete anns in annotation_set_permissions
            sql1 = "SELECT id FROM annotation_set_index WHERE annotates_url ='" + annotates_url +"'"
            version_id_list = []

            cursor.execute(sql1)

            results_id = cursor.fetchall()
            for row in results_id:
                #print row
                version_id_list.append(row[0])

            for last_version_id in version_id_list:
                sql2 = "DELETE FROM annotation_set_permissions WHERE annotation_set_id ='" + last_version_id + "'"

                cursor.execute(sql2)
                db.autocommit(True)

            sql3 = "DELETE FROM annotation_set_index WHERE annotates_url = '" + annotates_url +"'"

            cursor.execute(sql3)
            db.autocommit(True)

            # delete in last_annotation_set_index

            sql4 = "DELETE FROM last_annotation_set_index WHERE annotates_url = '" + annotates_url +"'"

            cursor.execute(sql4)

            db.autocommit(True)
            db.close()
        else:
            print "No annotations been found in %s" % (annotates_url)

        print "finished deletion - url " + str(annotates_url)

    except Exception, err:
        print "Exception, roll back"
        sys.stderr.write('ERROR: %s\n' % str(err))
        db.rollback()

        
def deleteAnnotationInES(mongo_uuid):
        print "[INFO] delete mongo_uuid " + mongo_uuid
        #call(['curl' , '-XDELETE' , 'http://'+LOCAL_IP+':9200/domeo/'+COLLECTION+'/'+mongo_uuid])
        call(['curl' , '-XDELETE' , 'http://localhost:9200/domeo/'+COLLECTION+'/'+mongo_uuid])

        print "delete anno in ES"
	

# delete all of annotations that related to annotStudy

def clearAllAnnotations():

    ## clean expertStudy labels
    # index = 0
    # # #index = 21
    # while index<=208:
    #     #http://dbmi-icode-01.dbmi.pitt.edu:2020/AnnoStudy/package-insert-section-171.txt.html
    #     URL = "http://"+LOCAL_IP + ":" + PORT +"/AnnoStudy/package-insert-section-"+str(index)+".txt.html"
    #     index = index + 1
    #     deleteAnnotationInMySQLAndElastico(URL)
    #     print "delete annotation: " + URL

    ## clean PK_DDI labels

    #testSetIdL = ["ea35caf8-c8c8-481a-97a2-25f68cbc240b","55816042-946d-4bec-9461-bd998628ff45","326e8ab0-6886-4749-9544-885b37070051","4650d12c-b9c8-4525-b07f-a2d773eca155"]

    #testSetIdL = ["ac387aa0-3f04-4865-a913-db6ed6f4fdc5","513a41d0-37d4-4355-8a6d-a2c643bce6fa","2e7350bd-ab32-4619-a3f9-12fdf56fc5e2","b891bd9f-fdb8-4862-89c5-ecdd700398a3","4259d9b1-de34-43a4-85a8-41dd214e9177","13bb8267-1cab-43e5-acae-55a4d957630a","5f356c1b-96bd-4ef1-960c-91cf4905e6b1","53664f8d-3a93-9f2b-daee-380707e4062c","326e8ab0-6886-4749-9544-885b37070051","c25f968b-7037-4f60-9a02-2189769b0cbf","a9ff32c4-3bcf-4e51-ae8c-81ff0191ec35","829a4f51-c882-4b64-81f3-abfb03a52ebe","f371258d-91b3-4b6a-ac99-434a1964c3af","f371258d-91b3-4b6a-ac99-434a1964c3af","4c401522-0108-49cb-8a41-fb5ad4dd0fb1","bdf9701e-fac5-48ae-935f-b9d3cfecbd00","010f9162-9f7f-4b6d-a6e4-4f832f26f38e","ea35caf8-c8c8-481a-97a2-25f68cbc240b","a16297df-3158-48db-85e5-5cd506885556","1809bcb4-5fcc-45ca-986a-4fa5edcd4b5e","3e593725-3fc9-458e-907d-19d51d5a7f9c","c66a11c1-3093-45ef-b348-3b196c05ba0c","75bf3473-2d70-4d41-93cd-afa1015e45bb","e17dc299-f52d-414d-ab6e-e809bd6f8acb","b8881a81-75d7-43e8-825f-37c352c146dc","0177d783-773c-41bf-9db9-eb7e5c64474a","a435da9d-f6e8-4ddc-897d-8cd2bf777b21","51ff7db5-aaf9-4c3c-86e6-958ebf16b60f","4b0700c9-b417-4c3a-b36f-de461e125bd3","43452bf8-76e7-47a9-a5d8-41fe84d061f0","49c4105b-e518-481c-a248-6684135f5bc1","036db1f2-52b3-42a0-acf9-817b7ba8c724","afad3051-9df2-4c54-9684-e8262a133af8","5429f134-839f-4ffc-9944-55f51238def8","5f0c6f5f-b906-4c8f-8580-3939a476a1c1","b074f950-246a-41f0-aedf-32f38998a4b1","d5051fbc-846b-4946-82df-341fb1216341","4e1e5249-1cb7-4cc2-ad5a-cdeeee2f494f","e86adbc4-a03e-4f43-a88c-b9015d18ff9f","b580471f-44a1-4da0-96ad-2f537eabec3e","d78e9639-6fab-4a78-8b29-6991a18ae6c6","a971ea18-40cf-4a01-b696-180ccc3fddb5","9c4bedb4-2d59-4fcd-aad7-fce988cd96d8","1a5a93be-7bc3-4714-9308-2fbfb8260e23","7d1a9689-23c8-44ef-a474-8c607e13d794","ee49f3b1-1650-47ff-9fb1-ea53fe0b92b6","827cf470-485d-4925-85f2-8933a6dea830","39a5dae2-49f7-4662-9eac-aa7b4c7807a4","efef4846-9497-41af-8dd6-518f86eca7f2","d76d5540-7bae-4719-8166-f2a9106338df","50914a46-eab6-4c83-97cf-6ab0234c8126","2f2db2f5-49d3-4d47-a08a-628df49d2120","309f8ac3-a3fd-4313-96aa-7f21fa9cd646","a4ee3907-45d4-41b9-af8a-39a9966cd533","a0da0dba-a56d-486b-a45b-e8a7cdfbeac6","c6e131fe-e7df-4876-83f7-9156fc4e8228","aad8b373-0aec-4efb-8e61-3d8114b31127","520428f1-2cd5-447f-8782-c8505ce65b72","44dcbf97-99ec-427c-ba50-207e0069d6d2","ad386ed4-a284-4da5-b79a-3c0f4165057a","1fd0ba23-962e-427f-8b9d-2cf8f64d0f95","fdbfe194-b845-42c5-bb87-a48118bc72e7","d2f6d2d0-1b32-4caf-9fce-6b798c94204b","ba74e3cd-b06f-4145-b284-5fd6b84ff3c9","e9481622-7cc6-418a-acb6-c5450daae9b0","10db92f9-2300-4a80-836b-673e1ae91610"]


    for setid in testSetIdL:

        ## proxied URLs
        #URL = "http://130.49.206.86:" + PORT + "/proxy/http://dailymed.nlm.nih.gov/dailymed/drugInfo.cfm?setid=" + setid

        ## development server served URLs
        URL = "http://" + LOCAL_IP+ ":" + PORT + "/DDI-labels/" + setid + ".html"

        deleteAnnotationInMySQLAndElastico(URL)
        print "delete annotation: " + URL

        call(['curl' , '-XDELETE' , 'http://localhost:9200/domeo/annotation1/'])
        #call(['curl' , '-XDELETE' , 'http://'+LOCAL_IP+':9200/domeo/annotation1/'])
        #call(['curl' , '-XDELETE' , 'http://'+LOCAL_IP+':9200/domeo/devb301/'])
        #call(['curl' , '-XDELETE' , 'http://'+LOCAL_IP+':9200/domeo/devb30/'])

# ----------------------main--------------------------------------

URL=""

def main():

    ## delete specific annotation by url
    # URL = "http://130.49.206.86:8080/SPL-PDDI-Label/fdbfe194-b845-42c5-bb87-a48118bc72e7-drugInteractions.txt"
    # deleteAnnotationInMySQLAndElastico(URL)
    # print "delete annotation: " + URL

    
    clearAllAnnotations()
    

if __name__ == "__main__":
    main()
