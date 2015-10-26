'''
Created 10/22/2015

@author: Yifan Ning

@summary: For expert study of DDI, load result sets from pipline into elasticsearch 

and then create tracking information into mysql

'''

import json
import uuid 
import MySQLdb, sys
import os, csv, codecs
from pprint import pprint
from elasticsearch import Elasticsearch
from sets import Set
from time import gmtime, strftime
import time

reload(sys) 
sys.setdefaultencoding('UTF8')


DB_CONFIG = "Domeo-DB-config.txt"
SAMPLE_DOMEO = "PKDDI/Sample-PKDDI-10222015.json"
#ANNOT_CSV = "PKDDI/pkddi-katrina-auto-10202015.csv"
ANNOT_CSV = "PKDDI/debug-1.csv"

DB_USER = None
DB_PWD = None

if len(sys.argv) > 4:
    USERNAME = str(sys.argv[1])
    COLLECTION = str(sys.argv[2])
    LOCAL_IP = str(sys.argv[3])
    PORT = str(sys.argv[4])
 
else:
	print "Usage: load-PKDDI-JSON-LD <username> <collection> <local ip> <port>"
	sys.exit(1)


# load json template
def loadJsonFromDir(path):
    json_data=open(path)
    data = json.load(json_data)
    json_data.close()
    return data

# mysql connection
def connectMysql():

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
        print "Mysql config file is not found: " + dbconfig
    return db


"""
source,date,assertionType,evidenceType,exactText,prefix,suffix,modality,statementType,drug1Lab,drug1Role,drug1Type,drug1rxc,dose1,drug2Lab,drug2Role,drug2Type,drug2rxc,dose2,objectRegimens,objectFormulation,objectDuration,preciptRegimens,preciptFormulation,preciptDuration,numOfParticipants,auc,aucType,aucDirection,cl,clType,clDirection,cmax,cmaxType,cmaxDirection,t12,t12Type,t12Direction
"""

# pre process fields of single annotation from csv
def processSingleRow(ann):

    dict_paras = {}

    label = "testLabel"
    dict_paras["annotates_url"] = ann["source"]

    prefix = ann["prefix"]
    if len(prefix) > 30:
        index = len(prefix)
        dict_paras["prefix"] = prefix[(index-31):index].replace("\n\n"," ").replace("\n"," ")
    else:
        dict_paras["prefix"] = prefix.replace("\n\n"," ").replace("\n"," ")

    dict_paras["exact"] = ann["exactText"].replace("\n\n"," ").replace("\n"," ")

    suffix = ann["suffix"]
    if len(suffix) > 30:
        dict_paras["suffix"] = suffix[:30].replace("\n\n"," ").replace("\n"," ")
    else:
        dict_paras["suffix"] = suffix.replace("\n\n"," ").replace("\n"," ")

    # drug 1 and drug 2
    dict_paras["drug1"] = ann["drug1Lab"]
    dict_paras["drug2"] = ann["drug2Lab"]
    dict_paras["drug1URI"] = ann["drug1rxc"]
    dict_paras["drug2URI"] = ann["drug2rxc"]

    # meta data
    dict_paras["lineage_uri"] = "urn:domeoserver:annotationset:" + str(uuid.uuid4())
    dict_paras["last_version_uri"] = "urn:domeoclient:uuid:" + str(uuid.uuid4())
    dict_paras["date"] = ann["date"].replace(" -0400","")    
    #dict_paras["date_created"] = str(strftime("%Y-%m-%d %H:%M:%S", time.localtime()))
    
    dict_paras["idx"] = str(uuid.uuid4())
    dict_paras["last_version_id"] = str(uuid.uuid4())
    dict_paras["mongo_uuid"] = str(uuid.uuid4())
    dict_paras["selector_uuid"] = str(uuid.uuid4())

    # get creator id
    db = connectMysql()
    cursor = db.cursor()
    sql = "select id from user where username = '" + USERNAME + "'"
    
    cursor.execute(sql)
   
    result = cursor.fetchall()

    db.close

    if result:
        for row in result:
            dict_paras["created_by_id"] = row[0]
            #dict_paras["permission"] = "urn:person:uuid:" + dict_paras["created_by_id"]
            dict_paras["permission"] = "urn:domeo:access:public"
        return dict_paras
    else:
        print "no user " + USERNAME + " exists in mysql"
        return None


# load json template, populate attributes from dataset
def buildAnnotation(dict_paras, sampledir, annotation):

    if dict_paras == None:
        print "dict_paras is null"
        return None

    else:
        ann_to_domeo = loadJsonFromDir(sampledir)

        # document meta data
        ann_to_domeo["@id"] = dict_paras["last_version_uri"]
        ann_to_domeo["pav_!DOMEO_NS!_createdBy"] = "urn:person:uuid:" + dict_paras["created_by_id"]
        ann_to_domeo["pav_!DOMEO_NS!_lastSavedOn"] = dict_paras["date"] + " -0400"
        ann_to_domeo["pav_!DOMEO_NS!_createdOn"] = dict_paras["date"] + " -0400"
        ann_to_domeo["pav_!DOMEO_NS!_lineageUri"] = dict_paras["lineage_uri"]

        # source url and permission
        ann_to_domeo["ao_!DOMEO_NS!_annotatesResource"] = dict_paras["annotates_url"]
        ann_to_domeo["permissions_!DOMEO_NS!_permissions"]["permissions_!DOMEO_NS!_accessType"] = dict_paras["permission"]

        # item meta data
        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["@id"] = "urn:domeoclient:uuid:" + str(uuid.uuid4())
        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["pav_!DOMEO_NS!_createdOn"] = dict_paras["date"] + " -0400"
        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["pav_!DOMEO_NS!_lastSavedOn"] = dict_paras["date"] + " -0400"
        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["pav_!DOMEO_NS!_lineageUri"] = "urn:domeoserver:annotation:" + str(uuid.uuid4())
        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["pav_!DOMEO_NS!_createdBy"] = "urn:person:uuid:" + dict_paras["created_by_id"]
        ann_to_domeo["domeo_!DOMEO_NS!_agents"][0]["@id"] = "urn:person:uuid:" + dict_paras["created_by_id"]
        ann_to_domeo["domeo_!DOMEO_NS!_resources"][0]["url"] = dict_paras["annotates_url"]
        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["domeo_!DOMEO_NS!_belongsToSet"] = dict_paras["last_version_uri"]

        ## evidence type and assertion type
        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["dikbD2R_!DOMEO_NS!_assertionType"] = annotation["assertionType"]
        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["dikbD2R_!DOMEO_NS!_evidenceType"] = annotation["evidenceType"]


        # context
        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["ao_!DOMEO_NS!_context"][0]["@id"] = "urn:domeoclient:uuid:" + str(uuid.uuid4())
        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["ao_!DOMEO_NS!_context"][0]["ao_!DOMEO_NS!_hasSource"] = dict_paras["annotates_url"]
        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["ao_!DOMEO_NS!_context"][0]["domeo_!DOMEO_NS!_uuid"] = str(uuid.uuid4())

        # selector
        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["ao_!DOMEO_NS!_context"][0]["ao_!DOMEO_NS!_hasSelector"]["@id"] = "urn:domeoclient:uuid:" + str(uuid.uuid4())
        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["ao_!DOMEO_NS!_context"][0]["ao_!DOMEO_NS!_hasSelector"]["pav_!DOMEO_NS!_createdOn"] = dict_paras["date"] + " -0400"
        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["ao_!DOMEO_NS!_context"][0]["ao_!DOMEO_NS!_hasSelector"]["domeo_!DOMEO_NS!_uuid"] = str(uuid.uuid4())
        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["ao_!DOMEO_NS!_context"][0]["ao_!DOMEO_NS!_hasSelector"]["ao_!DOMEO_NS!_exact"] = dict_paras["exact"]
        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["ao_!DOMEO_NS!_context"][0]["ao_!DOMEO_NS!_hasSelector"]["ao_!DOMEO_NS!_prefix"] = dict_paras["prefix"]
        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["ao_!DOMEO_NS!_context"][0]["ao_!DOMEO_NS!_hasSelector"]["ao_!DOMEO_NS!_suffix"] = dict_paras["suffix"]


        # # body
        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["ao_!DOMEO_NS!_body"][0]["sets"]["@id"] = "urn:pddi:uuid:" + str(uuid.uuid4())

        # drug 1
        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["ao_!DOMEO_NS!_body"][0]["sets"]["sio_!DOMEO_NS!_SIO_000628"][0]["@id"] = "urn:pddi:uuid:" + str(uuid.uuid4())
        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["ao_!DOMEO_NS!_body"][0]["sets"]["sio_!DOMEO_NS!_SIO_000628"][0]["sio_!DOMEO_NS!_SIO_000132"][0]["@id"] = str(uuid.uuid4())
        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["ao_!DOMEO_NS!_body"][0]["sets"]["sio_!DOMEO_NS!_SIO_000628"][0]["sio_!DOMEO_NS!_SIO_000132"][0]["rdfs_!DOMEO_NS!_label"] = dict_paras["drug1"]
        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["ao_!DOMEO_NS!_body"][0]["sets"]["sio_!DOMEO_NS!_SIO_000628"][0]["sio_!DOMEO_NS!_SIO_000132"][0]["sio_!DOMEO_NS!_SIO_000228"] = annotation["drug1Role"]
        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["ao_!DOMEO_NS!_body"][0]["sets"]["sio_!DOMEO_NS!_SIO_000628"][0]["sio_!DOMEO_NS!_SIO_000132"][0]["@type"] = annotation["drug1Type"]
        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["ao_!DOMEO_NS!_body"][0]["sets"]["sio_!DOMEO_NS!_SIO_000628"][0]["sio_!DOMEO_NS!_SIO_000132"][0]["dailymed_!DOMEO_NS!_activeMoietyRxCUI"] = dict_paras["drug1URI"]
        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["ao_!DOMEO_NS!_body"][0]["sets"]["sio_!DOMEO_NS!_SIO_000628"][0]["sio_!DOMEO_NS!_SIO_000132"][0]["dikbD2R_!DOMEO_NS!_dose"] = annotation["dose1"]

        # drug 2
        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["ao_!DOMEO_NS!_body"][0]["sets"]["sio_!DOMEO_NS!_SIO_000628"][0]["sio_!DOMEO_NS!_SIO_000132"][1]["@id"] = str(uuid.uuid4())
        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["ao_!DOMEO_NS!_body"][0]["sets"]["sio_!DOMEO_NS!_SIO_000628"][0]["sio_!DOMEO_NS!_SIO_000132"][1]["rdfs_!DOMEO_NS!_label"] = dict_paras["drug2"]
        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["ao_!DOMEO_NS!_body"][0]["sets"]["sio_!DOMEO_NS!_SIO_000628"][0]["sio_!DOMEO_NS!_SIO_000132"][1]["sio_!DOMEO_NS!_SIO_000228"] = annotation["drug2Role"]
        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["ao_!DOMEO_NS!_body"][0]["sets"]["sio_!DOMEO_NS!_SIO_000628"][0]["sio_!DOMEO_NS!_SIO_000132"][1]["@type"] = annotation["drug2Type"]
        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["ao_!DOMEO_NS!_body"][0]["sets"]["sio_!DOMEO_NS!_SIO_000628"][0]["sio_!DOMEO_NS!_SIO_000132"][1]["dailymed_!DOMEO_NS!_activeMoietyRxCUI"] = dict_paras["drug2URI"]
        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["ao_!DOMEO_NS!_body"][0]["sets"]["sio_!DOMEO_NS!_SIO_000628"][0]["sio_!DOMEO_NS!_SIO_000132"][1]["dikbD2R_!DOMEO_NS!_dose"] = annotation["dose2"]

        # statement & modality
        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["ao_!DOMEO_NS!_body"][0]["sets"]["dikbD2R_!DOMEO_NS!_modality"] = annotation["modality"]
        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["ao_!DOMEO_NS!_body"][0]["sets"]["dikbD2R_!DOMEO_NS!_statement"] = annotation["statementType"]

        # number of participants
        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["ao_!DOMEO_NS!_body"][0]["sets"]["sio_!DOMEO_NS!_SIO_000628"][0]["dikbD2R_!DOMEO_NS!_numOfParticipants"] = annotation["numOfParticipants"]
        
        # object information
        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["ao_!DOMEO_NS!_body"][0]["sets"]["sio_!DOMEO_NS!_SIO_000628"][0]["dikbD2R_!DOMEO_NS!_objectDuration"] = annotation["objectDuration"]
        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["ao_!DOMEO_NS!_body"][0]["sets"]["sio_!DOMEO_NS!_SIO_000628"][0]["dikbD2R_!DOMEO_NS!_objectRegimens"] = annotation["objectRegimens"]
        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["ao_!DOMEO_NS!_body"][0]["sets"]["sio_!DOMEO_NS!_SIO_000628"][0]["dikbD2R_!DOMEO_NS!_objectFormulation"] = annotation["objectFormulation"]

        # precipitant information
        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["ao_!DOMEO_NS!_body"][0]["sets"]["sio_!DOMEO_NS!_SIO_000628"][0]["dikbD2R_!DOMEO_NS!_preciptDuration"] = annotation["preciptDuration"]
        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["ao_!DOMEO_NS!_body"][0]["sets"]["sio_!DOMEO_NS!_SIO_000628"][0]["dikbD2R_!DOMEO_NS!_preciptFormulation"] = annotation["preciptFormulation"]
        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["ao_!DOMEO_NS!_body"][0]["sets"]["sio_!DOMEO_NS!_SIO_000628"][0]["dikbD2R_!DOMEO_NS!_preciptRegimens"] = annotation["preciptRegimens"]

        # cmax
        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["ao_!DOMEO_NS!_body"][0]["sets"]["sio_!DOMEO_NS!_SIO_000628"][0]["dikbD2R_!DOMEO_NS!_cmax"] = annotation["cmax"]
        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["ao_!DOMEO_NS!_body"][0]["sets"]["sio_!DOMEO_NS!_SIO_000628"][0]["dikbD2R_!DOMEO_NS!_cmaxType"] = annotation["cmaxType"]
        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["ao_!DOMEO_NS!_body"][0]["sets"]["sio_!DOMEO_NS!_SIO_000628"][0]["dikbD2R_!DOMEO_NS!_cmaxDirection"] = annotation["cmaxDirection"]

        # t12
        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["ao_!DOMEO_NS!_body"][0]["sets"]["sio_!DOMEO_NS!_SIO_000628"][0]["dikbD2R_!DOMEO_NS!_t12"] = annotation["t12"]
        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["ao_!DOMEO_NS!_body"][0]["sets"]["sio_!DOMEO_NS!_SIO_000628"][0]["dikbD2R_!DOMEO_NS!_t12Type"] = annotation["t12Type"]
        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["ao_!DOMEO_NS!_body"][0]["sets"]["sio_!DOMEO_NS!_SIO_000628"][0]["dikbD2R_!DOMEO_NS!_t12Direction"] = annotation["t12Direction"]

        # auc
        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["ao_!DOMEO_NS!_body"][0]["sets"]["sio_!DOMEO_NS!_SIO_000628"][0]["dikbD2R_!DOMEO_NS!_auc"] = annotation["auc"]
        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["ao_!DOMEO_NS!_body"][0]["sets"]["sio_!DOMEO_NS!_SIO_000628"][0]["dikbD2R_!DOMEO_NS!_aucType"] = annotation["aucType"]
        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["ao_!DOMEO_NS!_body"][0]["sets"]["sio_!DOMEO_NS!_SIO_000628"][0]["dikbD2R_!DOMEO_NS!_aucDirection"] = annotation["aucDirection"]

        # cl
        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["ao_!DOMEO_NS!_body"][0]["sets"]["sio_!DOMEO_NS!_SIO_000628"][0]["dikbD2R_!DOMEO_NS!_cl"] = annotation["cl"]
        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["ao_!DOMEO_NS!_body"][0]["sets"]["sio_!DOMEO_NS!_SIO_000628"][0]["dikbD2R_!DOMEO_NS!_clDirection"] = annotation["clDirection"]
        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["ao_!DOMEO_NS!_body"][0]["sets"]["sio_!DOMEO_NS!_SIO_000628"][0]["dikbD2R_!DOMEO_NS!_clType"] = annotation["clType"]


        return ann_to_domeo


# insert into mysql
def insert_annotation (dict_paras):

    db = connectMysql()
    cursor = db.cursor()

    sql1 = "INSERT INTO last_annotation_set_index(id, version, annotates_url, date_created, is_deleted, last_updated, last_version_id, last_version_uri,  lineage_uri) VALUES ('"+dict_paras["idx"]+"','0','"+dict_paras["annotates_url"]+"','"+dict_paras["date"]+"',b'0','"+dict_paras["date"]+"','"+dict_paras["last_version_id"]+"','"+dict_paras["last_version_uri"]+"','"+dict_paras["lineage_uri"]+"')"

    sql2 = "INSERT INTO annotation_set_index(id, version, annotates_url, created_by_id, created_on , description, individual_uri , is_deleted, label, last_saved_on, lineage_uri, mongo_uuid, previous_version, size, type, version_number) VALUES ('"+dict_paras["last_version_id"]+"','1','"+dict_paras["annotates_url"]+"','"+dict_paras["created_by_id"]+"','"+dict_paras["date"]+"','The default set is created automatically by Domeo','"+dict_paras["last_version_uri"]+"',b'0','Default Set','"+dict_paras["date"]+"','"+dict_paras["lineage_uri"]+"','"+dict_paras["mongo_uuid"]+"','','1','ao:AnnotationSet','1')"

    sql3 = "INSERT INTO annotation_set_permissions(id, version, annotation_set_id , is_locked, lineage_uri, permission_type) VALUES (default,'0','"+dict_paras["last_version_id"]+"','false','"+dict_paras["lineage_uri"]+"','"+dict_paras["permission"]+"')"


    try:
        cursor.execute("SET SQL_SAFE_UPDATES=0")
        cursor.execute("SET FOREIGN_KEY_CHECKS=0")
        cursor.execute(sql1)
        cursor.execute(sql2)
        cursor.execute(sql3)
        cursor.execute("SET SQL_SAFE_UPDATES=1")
        cursor.execute("SET FOREIGN_KEY_CHECKS=1")

        db.commit()
        db.close()
    except Exception, err:
        sys.stderr.write('ERROR: %s\n' % str(err))
        db.rollback()

# load annotation set into elasticsearch and mysql

def loadPKDDIDataset(anndir):
    index = 1
    ann_pkddi = csv.DictReader(codecs.open(ANNOT_CSV,"rb","utf-8"), delimiter=',')
    
    for ann in ann_pkddi:
        
        dict_paras = processSingleRow(ann)
        #print ann

        ann_domeo = buildAnnotation(dict_paras, SAMPLE_DOMEO, ann)

        #pprint(ann_domeo)
        if ann_domeo:
            es = Elasticsearch()
            es.index(index="domeo", doc_type=COLLECTION, id=dict_paras["mongo_uuid"], body=json.dumps(ann_domeo))

            insert_annotation (dict_paras)
            print "load annotations for " + dict_paras["annotates_url"]
        else:
            print "annotation empty"

        # index = index + 1
        # if index > 6:
        #     break



# ----------------------main--------------------------------------


def main():
   loadPKDDIDataset(ANNOT_CSV)
    

if __name__ == "__main__":
    main()
    

