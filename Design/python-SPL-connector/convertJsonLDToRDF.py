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

QUERY_STR = "annostudy"
MAX_RESULTS = 10000
OUT_FILE = "domeo-annotations-in-rdf.xml"

############################################################
# initialize
es = Elasticsearch()

# learn about the connection
#es.cluster.node_info()

# get all annotations
v = es.search(q=QUERY_STR, size=MAX_RESULTS)

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
    "dikbEvidence":"http://dbmi-icode-01.dbmi.pitt.edu/dikb-evidence/DIKB_evidence_ontology_v1.3.owl#"
  }


for jld in v['hits']['hits']:
    jldDict = jld['_source']

    # required to enable conversion of the body resources to RDF
    if jldDict.has_key("ao_!DOMEO_NS!_item"):
        for i in range(0,len(jldDict["ao_!DOMEO_NS!_item"])):
            if jldDict["ao_!DOMEO_NS!_item"][i].has_key('ao_!DOMEO_NS!_body'):
                for j in range(0,len(jldDict["ao_!DOMEO_NS!_item"][i]['ao_!DOMEO_NS!_body'])):
                    if jldDict["ao_!DOMEO_NS!_item"][i]['ao_!DOMEO_NS!_body'][j].has_key("sets"):
                        jldDict["ao_!DOMEO_NS!_item"][i]['ao_!DOMEO_NS!_body'][j]["domeo:sets"] = jldDict["ao_!DOMEO_NS!_item"][i]['ao_!DOMEO_NS!_body'][j].pop("sets")


    jldDict["@context"] = context
    jldJson = json.dumps(jldDict).replace("_!DOMEO_NS!_", ":")
    jldJson = unicode(jldJson).encode(encoding="utf-8",errors="replace")
    print jldJson

    g = Graph(store=store,identifier=jld["_id"]).parse(data=jldJson, format='json-ld')
    #print "\n\n\n"
    #print "######################### N3 #########"
    #print(g.serialize(format='xml', indent=4))

# enumerate contexts
print "Graph contexts stored in IO memory"
for c in cGraph.contexts():
    print("-- %s " % c)

# TODO: add exception handling
s = unicode(cGraph.serialize(format='xml', indent=4), encoding="utf-8",errors="replace")
print s

# TODO: add exception handling
f = codecs.open(OUT_FILE,'w','utf-8')
f.write(s)
f.close()




################################################################################
# SCRATCH CODE
################################################################################



# jldJson = '''
# {
#   "@context": {
#     "xsd": "http://www.w3.org/2001/XMLSchema#",
#     "domeo:resources": "http://www.w3.org/ns/domeo/resources#",
#     "domeo": "http://www.w3.org/ns/domeo#",
#     "ao": "http://www.w3.org/ns/oa#"

#   },

#  "ao:hasSelector":{"ao:suffix":" for experiencing a hypersensitivity reaction to abacavir. ","ao:exact":"HLA-B*5701 allele are at high risk","pav:createdOn":"2014-06-13 10:22:00 -0400","@type":"ao:PrefixSuffixTextSelector","@id":"urn:domeoclient:uuid:C4BE09C5-6DB0-4C9D-B243-DCE26C88D1EE","domeo:uuid":"C4BE09C5-6DB0-4C9D-B243-DCE26C88D1EE","domeo_temp_localId":"5","ao:prefix":"Patients who carry the "}
# }
# '''

# jldJson = '''
# { 
#   "@context": {
#     "domeo:resources": "",
#     "domeo": "http://www.w3.org/ns/domeo#",
#     "rdfs":"http://www.w3.org/2000/01/rdf-schema#",
#     "xsd": "http://www.w3.org/2001/XMLSchema#",    
#     "ao": "http://www.w3.org/ns/oa#",
#     "dct": "http://purl.org/dc/terms/",
#     "dctypes":"http://purl.org/dc/dcmitype/",
#     "pav":"http://purl.org/pav",
#     "foafx":"http://xmlns.com/foaf/0.1/",
#     "cnt":"http://www.w3.org/2011/content#",
#     "permissions":"http://vocab.deri.ie/cpo#",
#     "dailymed":"http://dbmi-icode-01.dbmi.pitt.edu/linkedSPLs/vocab/resource/",
#     "sio":"http://semanticscience.org/resource/",
#     "gcds":"http://www.genomic-cds.org/ont/genomic-cds.owl#",
#     "siocns":"http://rdfs.org/sioc/ns#",
#     "swande":"http://purl.org/swan/1.2/discourse-elements#",
#     "ncbit":"http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#",
#     "dikbEvidence":"http://dbmi-icode-01.dbmi.pitt.edu/dikb-evidence/DIKB_evidence_ontology_v1.3.owl#",
#     "poc":"http://purl.org/net/nlprepository/spl-ddi-annotation-poc#"
#   },
#  "domeo:source": {"rdfs_!DOMEO_NS!_label": "NLP Set", "pav_!DOMEO_NS!_versionNumber": 1, "domeo_!DOMEO_NS!_resources": [{"url": "http://130.49.206.86:8080/AnnoStudy/package-insert-section-19.txt.html", "label": ""}], "pav_!DOMEO_NS!_previousVersion": "", "pav_!DOMEO_NS!_createdOn": "2014-05-20 11:23:39 -0400", "pav_!DOMEO_NS!_createdWith": "urn:domeo:software:id:Domeo-2.1alpha-050", "dct_!DOMEO_NS!_description": "The default set is created automatically by Domeo when no other set is existing.", "permissions_!DOMEO_NS!_permissions": {"permissions_!DOMEO_NS!_accessType": "urn:domeo:access:public", "permissions_!DOMEO_NS!_isLocked": "false"}, "pav_!DOMEO_NS!_lineageUri": "urn:domeoclient:uuid:c36b60c0-f2ae-45e5-8691-4bc011835e78", "ao_!DOMEO_NS!_annotatesResource": "http://130.49.206.86:8080/AnnoStudy/package-insert-section-19.txt.html", "ao_!DOMEO_NS!_item": [{"rdfs_!DOMEO_NS!_label": "expertstudy Annotation", "pav_!DOMEO_NS!_versionNumber": 1, "pav_!DOMEO_NS!_previousVersion": "", "pav_!DOMEO_NS!_createdOn": "2014-05-20 11:24:11 -0400", "pav_!DOMEO_NS!_createdWith": "urn:domeo:software:id:Domeo-2.1alpha-050", "pav_!DOMEO_NS!_lineageUri": "urn:domeoserver:annotation:988636C9-1203-4AEF-80C0-38ED8A5790C8", "domeo_!DOMEO_NS!_belongsToSet": "urn:domeoclient:uuid:361e580f-1e43-4e02-9c24-e92b6339a8d1", "ao_!DOMEO_NS!_body": [{"domeo:sets": {"dikbD2R_!DOMEO_NS!_modality": "ncit:negative", "sio_!DOMEO_NS!_SIO_000628": [{"sio_!DOMEO_NS!_SIO_000132": [{"rdfs_!DOMEO_NS!_label": "sertraline", "dct_!DOMEO_NS!_description": "Referred to the drug in the interaction.", "sio_!DOMEO_NS!_SIO_000228": "", "@id": "http://purl.bioontology.org/ontology/RXNORM/36437", "@type": ""}, {"rdfs_!DOMEO_NS!_label": "digoxin", "dct_!DOMEO_NS!_description": "Referred to the drug in the interaction.", "sio_!DOMEO_NS!_SIO_000228": "", "@id": "http://purl.bioontology.org/ontology/MSH/D004077", "@type": ""}], "@id": "urn:pddi:uuid:DFC5470D-4558-470D-9902-4DA40AC260D1", "@type": "dikbD2R:PK_DDI"}], "dikbD2R_!DOMEO_NS!_statement": "", "sio_!DOMEO_NS!_SIO_000563": "dikbD2R:modality", "@id": "urn:pddi:uuid:71C15273-53F4-4A2A-B931-CBE98CF575C8", "@type": "poc:DrugDrugInteractionStatement", "sio_!DOMEO_NS!_SIO_000205": "dikbD2R:statement"}}], "pav_!DOMEO_NS!_lastSavedOn": "2014-05-20 11:24:14 -0400", "pav_!DOMEO_NS!_createdBy": "urn:person:uuid:80808080466465c00146674fe5190024", "ao_!DOMEO_NS!_context": [{"ao_!DOMEO_NS!_hasSelector": {"domeo_!DOMEO_NS!_uuid": "76061A02-E7B3-463A-B089-4E9761E294A2", "domeo_temp_localId": "23", "pav_!DOMEO_NS!_createdOn": "2014-05-20 11:24:11 -0400", "ao_!DOMEO_NS!_exact": "In a placebo-controlled trial in normal volunteers, administration of sertraline for 17 days (including 200 mg/day for the last 10 days) did not change serum digoxin levels or digoxin renal clearance.", "ao_!DOMEO_NS!_prefix": "", "ao_!DOMEO_NS!_suffix": "", "@id": "urn:domeoclient:uuid:76061A02-E7B3-463A-B089-4E9761E294A2", "@type": "ao:PrefixSuffixTextSelector"}, "@id": "urn:domeoclient:uuid:76061A02-E7B3-463A-B089-4E9761E294A2", "ao_!DOMEO_NS!_hasSource": "http://130.49.206.86:8080/AnnoStudy/package-insert-section-19.txt.html", "domeo_temp_localId": "23", "@type": "ao:SpecificResource"}], "@id": "urn:domeoclient:uuid:8F94AACE-535C-4A3E-8945-35002E18DF76", "@type": "ao:expertstudyAnnotation"}], "pav_!DOMEO_NS!_lastSavedOn": "2014-05-20 11:24:14 -0400", "pav_!DOMEO_NS!_createdBy": "urn:person:uuid:80808080466465c00146674fe5190024", "domeo_!DOMEO_NS!_deleted": "false", "domeo_!DOMEO_NS!_agents": [{"rdfs_!DOMEO_NS!_label": "NLP Sets", "foafx_!DOMEO_NS!_email": "", "foafx_!DOMEO_NS!_lastname": "Sets", "foafx_!DOMEO_NS!_firstname": "NLP", "foafx_!DOMEO_NS!_name": "NLP Sets", "foafx_!DOMEO_NS!_middlename": "", "foafx_!DOMEO_NS!_title": "", "foafx_!DOMEO_NS!_picture": "", "foafx_!DOMEO_NS!_homepage": "", "@id": "urn:person:uuid:80808080466465c00146674fe5190024", "@type": "foafx:Person"}, {"rdfs_!DOMEO_NS!_label": "Domeo Annotation Toolkit", "foafx_!DOMEO_NS!_build": "050", "foafx_!DOMEO_NS!_name": "Domeo Annotation Toolkit", "foafx_!DOMEO_NS!_version": "2.1alpha", "foafx_!DOMEO_NS!_homepage": "", "@id": "urn:domeo:software:id:Domeo-2.1alpha-050", "@type": "foafx:Software"}], "@id": "urn:domeoclient:uuid:361e580f-1e43-4e02-9c24-e92b6339a8d1", "@type": "ao:AnnotationSet"}} 
# '''
# #jldJson = json.dumps(jldJson.replace("_!DOMEO_NS!_", ":"))
# jldJson =  jldJson.replace("_!DOMEO_NS!_", ":")
# print jldJson
# g = Graph().parse(data=jldJson, format='json-ld')
# print "\n\n\n"
# print "######################### N3 #########"
# print(g.serialize(format='xml', indent=4))

