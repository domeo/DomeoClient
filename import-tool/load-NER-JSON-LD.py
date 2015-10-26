
'''
Created 06/05/2014

@authors: Yifan Ning

@summary: service of pre annotation loading for domeo

load NER sets in JSON into elasticsearch 

and create tracking information into mysql

'''


import json
import uuid
import MySQLdb, sys
import os
from pprint import pprint
from elasticsearch import Elasticsearch
from time import gmtime, strftime
import time
#import logging
#logging.basicConfig()

SAMPLE_DOMEO = "NER/Highlight-Sample-NER-devb30.json"
NER_JSON = "NER/NER-outputs.json"
DB_CONFIG = "Domeo-DB-config.txt"
DB_USER = None
DB_PWD = None

# NLP users are: expert1  expert2  nonexpert1  nonexpert2 nonexpert3
#LOCAL_IP = "130.49.206.86"
#PORT = "8080"
PREFIX_SUFFIX = 60


if len(sys.argv) > 4:
    NER_USERNAME = str(sys.argv[1])
    COLLECTION = str(sys.argv[2])
    LOCAL_IP = str(sys.argv[3])
    PORT = str(sys.argv[4])

else:
	print "Usage: load-NER-JSON-LD <ner username> <collection> <local ip> <port>"
	sys.exit(1)

# load json file

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

# parse single resource from NLP outputs

def parseSingleResource(ann):

    dict_paras = {}

    ## Label URL for andres ddi study - specified by label Id (1 - 208)
    #fileId = ann["fileId"]
    #dict_paras["fileId"] = fileId
    #dict_paras["annotates_url"] = "http://"+LOCAL_IP + ":" + PORT +"/AnnoStudy/package-insert-section-" +  fileId + ".txt.html"

    
    ## Label URL for proxy of alive dailymed labels 
    #dict_paras["annotates_url"] = "http://"+LOCAL_IP + ":" + PORT +"/proxy/http://dailymed.nlm.nih.gov/dailymed/drugInfo.cfm?setid=" + ann["setId"]

    ## Label URL for development server served dailymed labels 
    dict_paras["annotates_url"] = "http://"+LOCAL_IP + ":" + PORT +"/DDI-labels/" + ann["setId"] + ".html"
    
    dict_paras["exact"] = ann["exact"].replace("\n\n"," ").replace("\n"," ")

    prefix = ann["prefix"]
    if len(prefix) > PREFIX_SUFFIX:
        index = len(prefix)
        dict_paras["prefix"] = prefix[(index-PREFIX_SUFFIX):(index-1)].replace("\n\n"," ").replace("\n"," ")
    else: 
        dict_paras["prefix"] = prefix.replace("\n\n"," ").replace("\n"," ")
    
    suffix = ann["suffix"]
    if len(suffix) > PREFIX_SUFFIX:
        dict_paras["suffix"] = suffix[0:int(PREFIX_SUFFIX) - 1].replace("\n\n"," ").replace("\n"," ")
    else:
        dict_paras["suffix"] = suffix.replace("\n\n"," ").replace("\n"," ")

    dict_paras["drugname"] = ann["name"]
    dict_paras["drugURI"] = ann["fullId"]

    #print dict_paras["annotates_url"] +"|*|"+ dict_paras["prefix"] +"|*|"+dict_paras["exact"] +"|*|"+ dict_paras["suffix"] +"|*|"+ dict_paras["drugname"] +"|*|"+ dict_paras["drugURI"]
                
    dict_paras["lineage_uri"] = "urn:domeoserver:annotationset:" + str(uuid.uuid4())
    dict_paras["last_version_uri"] = "urn:domeoclient:uuid:" + str(uuid.uuid4())
    dict_paras["date_created"] = str(strftime("%Y-%m-%d %H:%M:%S", time.localtime()))
    dict_paras["created_on"]= str(strftime("%Y-%m-%d %H:%M:%S", time.localtime()))
    dict_paras["idx"] = str(uuid.uuid4())
    dict_paras["last_version_id"] = str(uuid.uuid4())
    dict_paras["mongo_uuid"] = str(uuid.uuid4())
    dict_paras["selector_uuid"] = str(uuid.uuid4())

    # get NER id

    db = connectMysql()
    cursor = db.cursor()
    sql = "select id from user where username = '" + NER_USERNAME + "'"
    
    cursor.execute(sql)
   
    result = cursor.fetchall()
    db.close

    if result:
        for row in result:
            dict_paras["created_by_id"] = row[0]
            dict_paras["permission"] = "urn:person:uuid:" + dict_paras["created_by_id"]
        return dict_paras
    else:
        print "no nre user set up in mysql"
        return None


# load sample annotation and then modifies attributes

def buildAnnotation(dict_paras, sampledir):

    if dict_paras == None:
        print "dict_paras is null"
        return None
    else:

        ann_to_domeo = loadJsonFromDir(sampledir)

        ann_to_domeo["@id"] = dict_paras["last_version_uri"]

        ann_to_domeo["rdfs_!DOMEO_NS!_label"] = "NER Sets"

        ann_to_domeo["pav_!DOMEO_NS!_lastSavedOn"] = dict_paras["date_created"] + " -0400"

        ann_to_domeo["pav_!DOMEO_NS!_createdOn"] = dict_paras["date_created"] + " -0400"

        ann_to_domeo["pav_!DOMEO_NS!_createdBy"] = "urn:person:uuid:" + dict_paras["created_by_id"]

        ann_to_domeo["domeo_!DOMEO_NS!_agents"][0]["@id"] = "urn:person:uuid:" + dict_paras["created_by_id"]

        ann_to_domeo["pav_!DOMEO_NS!_lineageUri"] = dict_paras["lineage_uri"]


        ann_to_domeo["domeo_!DOMEO_NS!_resources"][0]["url"] = dict_paras["annotates_url"]

        ann_to_domeo["domeo_!DOMEO_NS!_resources"][0]["label"] = dict_paras["drugname"]

        ann_to_domeo["permissions_!DOMEO_NS!_permissions"]["permissions_!DOMEO_NS!_accessType"] = dict_paras["permission"]

        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["@id"] = str(uuid.uuid4())

        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["pav_!DOMEO_NS!_createdOn"] = dict_paras["created_on"] + " -0400"

        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["pav_!DOMEO_NS!_lastSavedOn"] = dict_paras["created_on"] + " -0400"

        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["pav_!DOMEO_NS!_createdBy"] = "urn:person:uuid:" + dict_paras["created_by_id"]

        #ann_to_domeo["ao_!DOMEO_NS!_item"][0]["ao_!DOMEO_NS!_context"][0]["@id"] = "urn:domeoclient:uuid:" + dict_paras["selector_uuid"]

        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["ao_!DOMEO_NS!_context"][0]["@id"] = "urn:domeoclient:uuid:" + str(uuid.uuid4())

        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["ao_!DOMEO_NS!_context"][0]["ao_!DOMEO_NS!_hasSource"] = dict_paras["annotates_url"]

        ann_to_domeo["ao_!DOMEO_NS!_annotatesResource"] = dict_paras["annotates_url"]


        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["pav_!DOMEO_NS!_lineageUri"] = dict_paras["lineage_uri"]

        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["ao_!DOMEO_NS!_context"][0]["@id"] = "urn:domeoserver:uuid:" + str(uuid.uuid4())

        #ann_to_domeo["ao_!DOMEO_NS!_item"][0]["ao_!DOMEO_NS!_context"][0]["ao_!DOMEO_NS!_hasSelector"]["@id"] = "urn:domeoclient:uuid:" + dict_paras["selector_uuid"]

        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["ao_!DOMEO_NS!_context"][0]["ao_!DOMEO_NS!_hasSelector"]["@id"] = "urn:domeoclient:uuid:" + str(uuid.uuid4())

        #ann_to_domeo["ao_!DOMEO_NS!_item"][0]["ao_!DOMEO_NS!_context"][0]["ao_!DOMEO_NS!_hasSelector"]["@id"] = dict_paras["drugURI"]

        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["ao_!DOMEO_NS!_context"][0]["ao_!DOMEO_NS!_hasSelector"]["domeo_!DOMEO_NS!_uuid"] = dict_paras["selector_uuid"]

        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["ao_!DOMEO_NS!_context"][0]["ao_!DOMEO_NS!_hasSelector"]["pav_!DOMEO_NS!_createdOn"] = dict_paras["created_on"] + " -0400"

        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["ao_!DOMEO_NS!_context"][0]["ao_!DOMEO_NS!_hasSelector"]["domeo_!DOMEO_NS!_uuid"] = str(uuid.uuid4())
   
        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["ao_!DOMEO_NS!_context"][0]["ao_!DOMEO_NS!_hasSelector"]["ao_!DOMEO_NS!_exact"] = dict_paras["exact"]

        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["ao_!DOMEO_NS!_context"][0]["ao_!DOMEO_NS!_hasSelector"]["ao_!DOMEO_NS!_prefix"] = dict_paras["prefix"]

        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["ao_!DOMEO_NS!_context"][0]["ao_!DOMEO_NS!_hasSelector"]["ao_!DOMEO_NS!_suffix"] = dict_paras["suffix"]

        

        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["domeo_!DOMEO_NS!_belongsToSet"] = dict_paras["last_version_uri"]


        return ann_to_domeo


# insert into mysql

def insert_annotation (dict_paras):

    db = connectMysql()

    cursor = db.cursor()

    sql1 = "INSERT INTO last_annotation_set_index(id, version, annotates_url, date_created, is_deleted, last_updated, last_version_id, last_version_uri,  lineage_uri) VALUES ('"+dict_paras["idx"]+"','0','"+dict_paras["annotates_url"]+"','"+dict_paras["date_created"]+"',b'0','"+dict_paras["date_created"]+"','"+dict_paras["last_version_id"]+"','"+dict_paras["last_version_uri"]+"','"+dict_paras["lineage_uri"]+"')"

    sql2 = "INSERT INTO annotation_set_index(id, version, annotates_url, created_by_id, created_on , description, individual_uri , is_deleted, label, last_saved_on, lineage_uri, mongo_uuid, previous_version, size, type, version_number) VALUES ('"+dict_paras["last_version_id"]+"','1','"+dict_paras["annotates_url"]+"','"+dict_paras["created_by_id"]+"','"+dict_paras["created_on"]+"','The default set is created automatically by Domeo','"+dict_paras["last_version_uri"]+"',b'0','Default Set','"+dict_paras["date_created"]+"','"+dict_paras["lineage_uri"]+"','"+dict_paras["mongo_uuid"]+"','','1','ao:AnnotationSet','1')"

    sql3 = "INSERT INTO annotation_set_permissions(id, version, annotation_set_id , is_locked, lineage_uri, permission_type) VALUES ('','0','"+dict_paras["last_version_id"]+"','false','"+dict_paras["lineage_uri"]+"','"+dict_paras["permission"]+"')"

    try:
       #print sql1
       cursor.execute(sql1)
       #print 'successfully insert sql1'

       #print sql2
       cursor.execute(sql2)
       #print 'successfully insert sql2'

       #print sql3
       cursor.execute(sql3)
       #print 'successfully insert sql3'

       db.commit()
       db.close()
    except Exception, err:

       sys.stderr.write('ERROR: %s\n' % str(err))
       db.rollback()


# load NER outputs into elasticsearch and mysql

def loadNerOutputs(anndir):

    #setIds = ["Citalopram-4259d9b1-de34-43a4-85a8-41dd214e9177","Escitalopram-13bb8267-1cab-43e5-acae-55a4d957630a","Fluoxetine-5f356c1b-96bd-4ef1-960c-91cf4905e6b1"]
    setIds = ["55816042-946d-4bec-9461-bd998628ff45","c00d1607-ac36-457b-a34b-75ad74f9cf0a","70b079e2-a1f7-4a93-8685-d60a4d7c1280","38642D80-AAA6-4196-A033-3977FF35B48A"]

    ann_ner = loadJsonFromDir(anndir)
    #print len(ann_ner)

    #idx = 1
    for ann in ann_ner:
        
        if ann["setId"] in setIds:
            dict_paras = parseSingleResource(ann)
            ann_domeo = buildAnnotation(dict_paras, SAMPLE_DOMEO)

            # load all annotations
            if ann_domeo:

                # load 11 - 208
                #if ann_domeo and (int(dict_paras["fileId"]) > 10):

                es = Elasticsearch()
                es.index(index="domeo", doc_type=COLLECTION, id=dict_paras["mongo_uuid"], body=json.dumps(ann_domeo))

                insert_annotation(dict_paras)
                print "[INFO] load annotations:" +str(ann["setId"]) 
                #print "load annotations for " + dict_paras["annotates_url"]

                #idx = idx + 1
            else:
                print "[ERROR] annotation empty"


# ----------------------Main--------------------------------------


def main():
   loadNerOutputs(NER_JSON)
    
    

if __name__ == "__main__":
    main()
    

## "urn:domeoclient:uuid:7f910d24-f1a8-411d-8ae5-d31f1eea0b84","urn:domeoclient:uuid:5cb3ef05-9d02-4426-8a72-c4a92d4f43c6
