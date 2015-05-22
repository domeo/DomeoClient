import sys, codecs
sys.path = sys.path + ['.']

# load the library
from elasticsearch import Elasticsearch

#using plugin rdflib-jsonld we easily can transform jsonld in N3
import json
from rdflib import Graph, plugin, ConjunctiveGraph, URIRef
from rdflib.serializer import Serializer
from rdflib.plugins.memory import IOMemory
import MySQLdb, sys

store = IOMemory()
cGraph = ConjunctiveGraph(store=store)

# tests
#QUERY_STR = "8080808046a193dd0146afe869430016" # annostudy 
#QUERY_STR = "80808080466465c00146674fe5190024" # expert1
#QUERY_STR = "80808080466465c001466750c6df0025" # expert2

# query examples:
# python convertDDIJsonLDToRDF.py 80808080466465c00146674fe5190024 devb30 domeo-ddi-annotations-in-rdf-07092014.xml 0
# python convertDDIJsonLDToRDF.py 80808080466465c00146674fe5190024 annotation domeo-ddi-annotations-in-rdf-07102014.xml 1

MAX_RESULTS = 10000
OUT_FILE = None
DB_CONFIG = "Domeo-DB-config.txt"
OLD_VERSION = 0
VERBOSE = 0

if len(sys.argv) > 4:
    QUERY_STR = str(sys.argv[1])
    COLLECTION = str(sys.argv[2])
    OUT_FILE = str(sys.argv[3])
    OLD_VERSION = int(sys.argv[4])
    if len(sys.argv) == 6:
        VERBOSE = int(sys.argv[5])
else:
	print "Usage: python convertDDIJsonLDToRDF <query string> <collection> <output file name> <old version>(1: want old versions, 0: only get last version) <verbose>(optional 1=True, 0=False (default)) )"
	sys.exit(1)



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


def isLastVersionSetsId(mongo_uuid):

    db = connectMysql()
    cursor = db.cursor()

    sql = "select * from last_annotation_set_index as last, annotation_set_index as annot where last.last_version_id = annot.id and annot.mongo_uuid = '" + mongo_uuid + "'"
    
    cursor.execute(sql)
   
    result = cursor.fetchall()

    db.close

    if result:
        return True
    else:
        return False


############################################################
# initialize
es = Elasticsearch()

v = es.search(index="domeo",doc_type=COLLECTION, q=QUERY_STR, size=MAX_RESULTS)


# view what was returned
#v['hits']

context = {
    "domeo:resources": "",
    "domeo": "http://www.w3.org/ns/domeo#",
    "rdfs":"http://www.w3.org/2000/01/rdf-schema#",
    "xsd": "http://www.w3.org/2001/XMLSchema#",    
    "ao": "http://www.w3.org/ns/oa#",
    "dct": "http://purl.org/dc/terms/",
    "dctypes":"http://purl.org/dc/dcmitype/",
    "pav":"http://purl.org/pav/",
    "foafx":"http://xmlns.com/foaf/0.1/",
    "cnt":"http://www.w3.org/2011/content#",
    "permissions":"http://vocab.deri.ie/cpo#",
    "dailymed":"http://dbmi-icode-01.dbmi.pitt.edu/linkedSPLs/vocab/resource/",
    "sio":"http://semanticscience.org/resource/",
    "gcds":"http://www.genomic-cds.org/ont/genomic-cds.owl#",
    "siocns":"http://rdfs.org/sioc/ns#",
    "swande":"http://purl.org/swan/1.2/discourse-elements#",
    "ncbit":"http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#",
    "dikbEvidence":"http://dbmi-icode-01.dbmi.pitt.edu/dikb-evidence/DIKB_evidence_ontology_v1.3.owl#",
    "dikbD2R":"http://dbmi-icode-01.dbmi.pitt.edu:2020/vocab/resource/",
    "poc":"http://purl.org/net/nlprepository/spl-pharmgx-annotation-poc#"
  }


for jld in v['hits']['hits']:

    print jld['_id']

    mongo_uuid = jld['_id']

    print OLD_VERSION

    if OLD_VERSION != 1:

        if not isLastVersionSetsId(mongo_uuid):

            print mongo_uuid + " is not last version"
            continue
         
    jldDict = jld['_source']
    #  required to enable conversion of the body resources to RDF
    #  for type devb30
    if jldDict.has_key("ao_!DOMEO_NS!_item"):
        for i in range(0,len(jldDict["ao_!DOMEO_NS!_item"])):
            if jldDict["ao_!DOMEO_NS!_item"][i].has_key('ao_!DOMEO_NS!_body'):
                for j in range(0,len(jldDict["ao_!DOMEO_NS!_item"][i]['ao_!DOMEO_NS!_body'])):
                    if jldDict["ao_!DOMEO_NS!_item"][i]['ao_!DOMEO_NS!_body'][j].has_key("sets"):
                        jldDict["ao_!DOMEO_NS!_item"][i]['ao_!DOMEO_NS!_body'][j]["domeo:sets"] = jldDict["ao_!DOMEO_NS!_item"][i]['ao_!DOMEO_NS!_body'][j].pop("sets")


    jldDict["@context"] = context
    jldJson = json.dumps(jldDict).replace("_!DOMEO_NS!_", ":")
    jldJson = jldJson.replace('ao:prefix": ""','ao:prefix": "xsd:String"').replace('ao:suffix": ""','ao:suffix": "xsd:String"').replace('statement": ""','statement": "xsd:String"').replace('modality": ""','modality": "xsd:String"').replace('SIO_000228": ""','SIO_000228": "xsd:String"').replace('pav:previousVersion": ""','pav:previousVersion": "xsd:String"').replace('assertionType": ""','assertionType": "xsd:String"')

    jldJson = unicode(jldJson).encode(encoding="utf-8",errors="replace")
        #print jldJson

    g = Graph(store=store,identifier=jld["_id"]).parse(data=jldJson, format='json-ld')


    if VERBOSE:
        print jldJson

# enumerate contexts
if VERBOSE:
    print "Graph contexts stored in IO memory"
    for c in cGraph.contexts():
        print("-- %s " % c)

# TODO: add exception handling
s = unicode(cGraph.serialize(format='xml', indent=4), encoding="utf-8",errors="replace")
if VERBOSE:
    print s

# TODO: add exception handling
f = codecs.open(OUT_FILE,'w','utf-8')
f.write(s)
f.close()
