# convertJsonLDToRDF.py

import sys, codecs
sys.path = sys.path + ['.']

# load the library
from elasticsearch import Elasticsearch

#using plugin rdflib-jsonld we easily can transform jsonld in N3
import json
from rdflib import Graph, plugin, ConjunctiveGraph, URIRef
from rdflib.serializer import Serializer
from rdflib.plugins.memory import IOMemory

store = IOMemory()
cGraph = ConjunctiveGraph(store=store)

# tests
#QUERY_STR = "8080808046a193dd0146afe869430016" # annostudy 
#QUERY_STR = "80808080466465c00146674fe5190024" # expert1

# expert1
QUERY_STR = "80808080466465c00146674fe5190024"



#QUERY_STR = None

MAX_RESULTS = 10000
OUT_FILE = None

VERBOSE = 0

if len(sys.argv) > 2:
    QUERY_STR = str(sys.argv[1])
    OUT_FILE = str(sys.argv[2])
    if len(sys.argv) == 4:
        VERBOSE = int(sys.argv[3])
else:
	print "Usage: convertJsonLDToRDF <query string> <output file name> <verbose>(optional 1=True, 0=False (default)) )"
	sys.exit(1)


############################################################
# initialize
es = Elasticsearch()

# learn about the connection
#es.cluster.node_info()

# get all annotations from domeo/devb30
#res = es.get(index="domeo", doc_type='devb30',id=1)

v = es.search(index="domeo",doc_type='devb30', q=QUERY_STR, size=MAX_RESULTS)

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
    jldDict = jld['_source']

    #  required to enable conversion of the body resources to RDF
    #  for type devb30
    if jldDict.has_key("ao_!DOMEO_NS!_item"):
        for i in range(0,len(jldDict["ao_!DOMEO_NS!_item"])):
            if jldDict["ao_!DOMEO_NS!_item"][i].has_key('ao_!DOMEO_NS!_body'):
                for j in range(0,len(jldDict["ao_!DOMEO_NS!_item"][i]['ao_!DOMEO_NS!_body'])):
                    if jldDict["ao_!DOMEO_NS!_item"][i]['ao_!DOMEO_NS!_body'][j].has_key("sets"):
                        jldDict["ao_!DOMEO_NS!_item"][i]['ao_!DOMEO_NS!_body'][j]["domeo:sets"] = jldDict["ao_!DOMEO_NS!_item"][i]['ao_!DOMEO_NS!_body'][j].pop("sets")

    
    # for type devb30
    
    # if jldDict.has_key("ao_!DOMEO_NS!_body"):
    #     print jldDict["ao_!DOMEO_NS!_body"]
    #     for i in range(0,len(jldDict["ao_!DOMEO_NS!_body"])):
    #         if jldDict["ao_!DOMEO_NS!_body"][i].has_key("sets"):
    #             jldDict["ao_!DOMEO_NS!_body"][i]["domeo:sets"] = jldDict["ao_!DOMEO_NS!_body"][i].pop("sets")
              

    jldDict["@context"] = context
    jldJson = json.dumps(jldDict).replace("_!DOMEO_NS!_", ":")
    jldJson = jldJson.replace('ao:prefix": ""','ao:prefix": "<empty>"').replace('ao:suffix": ""','ao:suffix": "<empty>"').replace('statement": ""','statement": "<empty>"').replace('modality": ""','modality": "<empty>"').replace('SIO_000228": ""','SIO_000228": "<empty>"')
    #jldJson = jldJson.replace('": ""','": "EMPTY"')
    jldJson = unicode(jldJson).encode(encoding="utf-8",errors="replace")
    if VERBOSE:
        print jldJson

    g = Graph(store=store,identifier=jld["_id"]).parse(data=jldJson, format='json-ld')
    #print "\n\n\n"
    #print "######################### N3 #########"
    #print(g.serialize(format='xml', indent=4))

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
