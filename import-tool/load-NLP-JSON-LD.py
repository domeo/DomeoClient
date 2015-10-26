'''
Created 06/05/2014

@authors: Yifan Ning

@summary: For expert study of DDI, load result sets from NLP pipline into elasticsearch 

and then create tracking information into mysql

'''

import json
import uuid 
import MySQLdb, sys
import os
from pprint import pprint
from elasticsearch import Elasticsearch
from sets import Set
from time import gmtime, strftime
import time

DB_CONFIG = "Domeo-DB-config.txt"
SAMPLE_DOMEO = "NLP/Sample-NLP-devb30.json"
NLP_JSON = "NLP/NLP-outputs.json"
DB_USER = None
DB_PWD = None

NAN_LIST = Set(['a 10','a 300','a 7','angiotensinogen','antineoplastic agents','benzodiazepines','esters','glycoprotein','hmg-coa','monoamine oxidase inhibitors','norverapamil','oncovin','penicillins','phenothiazines','thiazide diuretics','triptans'])


# NLP users are: expert1  expert2  nonexpert1  nonexpert2 nonexpert3
#LOCAL_IP = "130.49.206.86"
#PORT = "8080"

if len(sys.argv) > 4:
    NLP_USERNAME = str(sys.argv[1])
    COLLECTION = str(sys.argv[2])
    LOCAL_IP = str(sys.argv[3])
    PORT = str(sys.argv[4])
 
else:
	print "Usage: load-NLP-JSON-LD <nlp username> <collection> <local ip> <port>"
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

    label = ann["label"]
    dict_paras["annotates_url"] = "http://"+LOCAL_IP + ":" + PORT +"/AnnoStudy/package-insert-section-" +  label + ".txt.html"

    prefix = ann["prefix"]
    if len(prefix) > 30:
        index = len(prefix)
        dict_paras["prefix"] = prefix[(index-31):index].replace("\n\n"," ").replace("\n"," ")
    else:
        dict_paras["prefix"] = prefix.replace("\n\n"," ").replace("\n"," ")

    dict_paras["exact"] = ann["exact"].replace("\n\n"," ").replace("\n"," ")

    suffix = ann["suffix"]
    if len(suffix) > 30:
        dict_paras["suffix"] = suffix[:30].replace("\n\n"," ").replace("\n"," ")
    else:
        dict_paras["suffix"] = suffix.replace("\n\n"," ").replace("\n"," ")

    dict_paras["modality"] = ann["modality"]

    dict_paras["drug1"] = ann["drugOneName"]
    dict_paras["drug2"] = ann["drugTwoName"]
    dict_paras["drug1URI"] = ann["drugOneCui"]
    dict_paras["drug2URI"] = ann["drugTwoCui"]
                
    dict_paras["lineage_uri"] = "urn:domeoclient:uuid:" + str(uuid.uuid4())
    dict_paras["last_version_uri"] = "urn:domeoclient:uuid:" + str(uuid.uuid4())
    dict_paras["date_created"] = str(strftime("%Y-%m-%d %H:%M:%S", time.localtime()))
    dict_paras["created_on"]= str(strftime("%Y-%m-%d %H:%M:%S", time.localtime()))
    
    dict_paras["idx"] = str(uuid.uuid4())
    dict_paras["last_version_id"] = str(uuid.uuid4())
    dict_paras["mongo_uuid"] = str(uuid.uuid4())
    dict_paras["selector_uuid"] = str(uuid.uuid4())

    # get creator id

    db = connectMysql()
    cursor = db.cursor()
    sql = "select id from user where username = '" + NLP_USERNAME + "'"
    
    cursor.execute(sql)
   
    result = cursor.fetchall()

    db.close

    if result:
        for row in result:
            dict_paras["created_by_id"] = row[0]
            dict_paras["permission"] = "urn:person:uuid:" + dict_paras["created_by_id"]
        return dict_paras
    else:
        print "no nlp user set up in mysql"
        return None


# load sample annotation and then modifies attributes

def buildAnnotation(dict_paras, sampledir):

    if dict_paras == None:
        print "dict_paras is null"
        return None

    else:
        ann_to_domeo = loadJsonFromDir(sampledir)

        ann_to_domeo["pav_!DOMEO_NS!_createdBy"] = "urn:person:uuid:" + dict_paras["created_by_id"]

        ann_to_domeo["pav_!DOMEO_NS!_lastSavedOn"] = dict_paras["date_created"] + " -0400"

        ann_to_domeo["pav_!DOMEO_NS!_createdOn"] = dict_paras["date_created"] + " -0400"

        ann_to_domeo["ao_!DOMEO_NS!_annotatesResource"] = dict_paras["annotates_url"]

        ann_to_domeo["permissions_!DOMEO_NS!_permissions"]["permissions_!DOMEO_NS!_accessType"] = dict_paras["permission"]

        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["@id"] = "urn:domeoclient:uuid:" + str(uuid.uuid4())

        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["pav_!DOMEO_NS!_createdOn"] = dict_paras["created_on"] + " -0400"

        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["pav_!DOMEO_NS!_lastSavedOn"] = dict_paras["created_on"] + " -0400"

        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["pav_!DOMEO_NS!_lineageUri"] = "urn:domeoserver:annotation:" + str(uuid.uuid4())

        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["pav_!DOMEO_NS!_createdBy"] = "urn:person:uuid:" + dict_paras["created_by_id"]

        ann_to_domeo["domeo_!DOMEO_NS!_agents"][0]["@id"] = "urn:person:uuid:" + dict_paras["created_by_id"]

        ann_to_domeo["domeo_!DOMEO_NS!_resources"][0]["url"] = dict_paras["annotates_url"]

        #ann_to_domeo["ao_!DOMEO_NS!_item"][0]["ao_!DOMEO_NS!_context"][0]["@id"] = "urn:domeoclient:uuid:" + dict_paras["selector_uuid"]

        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["ao_!DOMEO_NS!_context"][0]["@id"] = "urn:domeoclient:uuid:" + str(uuid.uuid4())

        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["ao_!DOMEO_NS!_context"][0]["ao_!DOMEO_NS!_hasSource"] = dict_paras["annotates_url"]

        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["ao_!DOMEO_NS!_context"][0]["ao_!DOMEO_NS!_hasSelector"]["pav_!DOMEO_NS!_createdOn"] = dict_paras["created_on"] + " -0400"

        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["ao_!DOMEO_NS!_context"][0]["ao_!DOMEO_NS!_hasSelector"]["@id"] = "urn:domeoclient:uuid:" + str(uuid.uuid4())

       #ann_to_domeo["ao_!DOMEO_NS!_item"][0]["ao_!DOMEO_NS!_context"][0]["ao_!DOMEO_NS!_hasSelector"]["@id"] = "urn:domeoclient:uuid:" + dict_paras["selector_uuid"]

        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["ao_!DOMEO_NS!_context"][0]["ao_!DOMEO_NS!_hasSelector"]["domeo_!DOMEO_NS!_uuid"] = str(uuid.uuid4())

        #ann_to_domeo["ao_!DOMEO_NS!_item"][0]["ao_!DOMEO_NS!_context"][0]["ao_!DOMEO_NS!_hasSelector"]["domeo_!DOMEO_NS!_uuid"] = dict_paras["selector_uuid"]

        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["ao_!DOMEO_NS!_context"][0]["ao_!DOMEO_NS!_hasSelector"]["ao_!DOMEO_NS!_exact"] = dict_paras["exact"]

        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["ao_!DOMEO_NS!_context"][0]["ao_!DOMEO_NS!_hasSelector"]["ao_!DOMEO_NS!_prefix"] = dict_paras["prefix"]

        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["ao_!DOMEO_NS!_context"][0]["ao_!DOMEO_NS!_hasSelector"]["ao_!DOMEO_NS!_suffix"] = dict_paras["suffix"]

        ann_to_domeo["pav_!DOMEO_NS!_lineageUri"] = dict_paras["lineage_uri"]

        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["domeo_!DOMEO_NS!_belongsToSet"] = dict_paras["last_version_uri"]

        ann_to_domeo["@id"] = dict_paras["last_version_uri"]

## body

        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["ao_!DOMEO_NS!_body"][0]["sets"]["@id"] = "urn:pddi:uuid:" + str(uuid.uuid4())

        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["ao_!DOMEO_NS!_body"][0]["sets"]["dikbD2R_!DOMEO_NS!_modality"] = "ncit:" + dict_paras["modality"]

        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["ao_!DOMEO_NS!_body"][0]["sets"]["sio_!DOMEO_NS!_SIO_000628"][0]["@id"] = "urn:pddi:uuid:" + str(uuid.uuid4())

        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["ao_!DOMEO_NS!_body"][0]["sets"]["sio_!DOMEO_NS!_SIO_000628"][0]["sio_!DOMEO_NS!_SIO_000132"][0]["@id"] = str(uuid.uuid4())

        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["ao_!DOMEO_NS!_body"][0]["sets"]["sio_!DOMEO_NS!_SIO_000628"][0]["sio_!DOMEO_NS!_SIO_000132"][0]["rdfs_!DOMEO_NS!_label"] = dict_paras["drug1"]

        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["ao_!DOMEO_NS!_body"][0]["sets"]["sio_!DOMEO_NS!_SIO_000628"][0]["sio_!DOMEO_NS!_SIO_000132"][0]["dailymed_!DOMEO_NS!_activeMoietyRxCUI"] = dict_paras["drug1URI"]

        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["ao_!DOMEO_NS!_body"][0]["sets"]["sio_!DOMEO_NS!_SIO_000628"][0]["sio_!DOMEO_NS!_SIO_000132"][1]["@id"] = str(uuid.uuid4())

        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["ao_!DOMEO_NS!_body"][0]["sets"]["sio_!DOMEO_NS!_SIO_000628"][0]["sio_!DOMEO_NS!_SIO_000132"][1]["rdfs_!DOMEO_NS!_label"] = dict_paras["drug2"]

        ann_to_domeo["ao_!DOMEO_NS!_item"][0]["ao_!DOMEO_NS!_body"][0]["sets"]["sio_!DOMEO_NS!_SIO_000628"][0]["sio_!DOMEO_NS!_SIO_000132"][1]["dailymed_!DOMEO_NS!_activeMoietyRxCUI"] = dict_paras["drug2URI"]


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


# load nlp outputs into elasticsearch and mysql

def loadNlpOutputs(anndir):
    index = 1
    ann_nlp = loadJsonFromDir(anndir)
    print len(ann_nlp)

    for ann in ann_nlp:

        # load 20 - 208
        #if (int(ann["label"])>20) and ann["drugOneName"].lower() not in NAN_LIST and ann["drugTwoName"].lower() not in NAN_LIST:

        # load 11 - 20
        #if (int(ann["label"])>10) and (int(ann["label"])<21) and ann["drugOneName"].lower() not in NAN_LIST and ann["drugTwoName"].lower() not in NAN_LIST:

        # load 1 - 208
        #if ann["drugOneName"].lower() not in NAN_LIST and ann["drugTwoName"].lower() not in NAN_LIST:

        # load 11 - 208
        if (int(ann["label"])>10) and ann["drugOneName"].lower() not in NAN_LIST and ann["drugTwoName"].lower() not in NAN_LIST:

            dict_paras = parseSingleResource(ann)
            ann_domeo = buildAnnotation(dict_paras, SAMPLE_DOMEO)

            #pprint(ann_domeo)
            if ann_domeo:
                es = Elasticsearch()
                es.index(index="domeo", doc_type=COLLECTION, id=dict_paras["mongo_uuid"], body=json.dumps(ann_domeo))

                insert_annotation (dict_paras)
                print "load annotations for " + dict_paras["annotates_url"]
            else:
                print "annotation empty"
            #index = index + 1
            #if index >3:
            #    break
        else:
            print "skip to DDI at " + ann["label"]


# ----------------------main--------------------------------------


def main():
   loadNlpOutputs(NLP_JSON)
    
    

if __name__ == "__main__":
    main()
    

