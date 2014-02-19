package org.mindinformatics.gwt.domeo.plugins.annotation.persistence.service;

import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology;
import org.mindinformatics.gwt.domeo.plugins.annotation.persistence.model.JsAnnotationSetSummary;
import org.mindinformatics.gwt.domeo.plugins.annotation.persistence.model.JsDocumentAnnotationSummary;
import org.mindinformatics.gwt.domeo.plugins.persistence.json.marshalling.JsoAnnotationSetSummary;
import org.mindinformatics.gwt.domeo.plugins.persistence.json.model.JsAnnotationSet;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.i18n.client.DateTimeFormat;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class AnnotationPersistenceServiceFacade {

	public static final String ALLOW_MINE = "Only my annotation";
	public static final String ALLOW_GROUPS = "Annotation from my groups";
	public static final String ALLOW_PUBLIC = "Public annotation";
	
	DateTimeFormat fmt = DateTimeFormat.getFormat("yyyy.MM.dd HH:mm:ss Z");
	
	private final native JsArray<JsAnnotationSetSummary> asArrayOfAnnotationSetSummaries(String json) /*-{
	    return eval(json);
	}-*/;
	
	private final native JsArray<JsAnnotationSet> asArrayOfAnnotationSets(String json) /*-{
	    return JSON.parse(json);
	}-*/;
	
	private final native JsArray<JsoAnnotationSetSummary> asArrayOfAnnotationSetSummaries2(String json) /*-{
	    return JSON.parse(json);
	}-*/;
	
	private final native JsArray<JsDocumentAnnotationSummary> asArrayOfDocumentAnnotationSummaries(String json) /*-{
	    return eval(json);
	}-*/;
	
	/**
	 * Given a specific url, it returns all the available accessible
	 * annotation for the given user
	 * @param url		Url of the document of interest
	 * @param username	User requesting the annotation
	 */
	public JsArray<JsAnnotationSetSummary> retrieveAnnotationByDocumentUrl(String url, String allowed, String username) {
		return retrieveAnnotationByDocumentUrl(url, allowed, false, username);
	}
	
	/*
	public JsArray<JsoAnnotationSetSummary> retrieveAnnotationSets(String url) {
		String json = "{}";
		
		JsArray<JsoAnnotationSetSummary> sets = asArrayOfAnnotationSetSummaries2("[" + json+ "]");
		return sets;
	}
	*/
	
	public JsArray<JsoAnnotationSetSummary> retrieveBibliographyByDocumentUrl(String url) {
		String json = "{}";
		
		json = 
		"{" +
		    "\"@type\": \"" + IDomeoOntology.annotationSet + "\"," +
		    "\"@id\": \"urn:domeoclient:uuid:20FEAAC7-9A44-41F6-8EF4-FC18EBCC35DE\","+
		    "\"rdfs:label\": \"Bibliography\"," +
		    "\"dct:description\": \"Bibliographic Set\"," +
		    "\"pav:lineageUri\": \"\"," +
		    "\"domeo_localId\": \"2\", " +
		    "\"ao:annotatesResource\": \"http://www.ncbi.nlm.nih.gov/pubmed/10679938\"," +
		    "\"pav:versionNumber\": \"\"," +
		    "\"pav:previousVersion\": \"\"," +
		    "\"pav:createdOn\": \"2012-09-25 16:32:15 -0400\"," +
		    "\"pav:createdBy\": \"http://localhost:8080/Domeo/user/8a7036ee39fe0e590139fe0e98550000\"," +
		    "\"pav:lastUpdatedOn\": \"\"," +
		    "\"domeo_isLocked\": \"false\"," +
		    "\"ao:item\": [" +
		        "{" +
		            "\"@type\": \"ao:Reference\"," +
		            "\"@id\": \"urn:domeoclient:uuid:3043285E-EFD3-4445-BA43-B6A4AF1BEBF3\"," +
		            "\"rdfs:label\": \"Reference\"," +
		            "\"domeo:uuid\": \"3043285E-EFD3-4445-BA43-B6A4AF1BEBF3\"," +
		            "\"domeo_localId\": \"0\"," +
		            "\"domeo_saveAsNewVersion\": \"false\"," +
		            "\"pav:versionNumber\": \"\"," +
		            "\"pav:previousVersion\": \"\"," +
		            "\"pav:createdOn\": \"09/25/12 4:32PM\"," +
		            "\"ao:context\": [" +
		                "\"http://www.ncbi.nlm.nih.gov/pubmed/10679938\""+
		            "],"+
		            "\"domeo:content\": {" +
		                "\"@type\": \"org.mindinformatics.gwt.framework.model.references.MPublicationArticleReference\"," +
		                "\"@id\": \"10.1002/(SICI)1098-1004(200003)15:3<228::AID-HUMU3>3.0.CO;2-9\"," +
		                "\"title\": \"An update of the mutation spectrum of the survival motor neuron gene (SMN1) in autosomal recessive spinal muscular atrophy (SMA).\"," +
		                "\"authorNames\": \"Wirth B\"," +
		                "\"doi\": \"10.1002/(SICI)1098-1004(200003)15:3<228::AID-HUMU3>3.0.CO;2-9\"," +
		                "\"pmid\": \"10679938\"," +
		                "\"pmcid\": \"<unknown>\"," +
		                "\"publicationInfo\": \"Human mutation. 2000 ;Vol 15 Issue 3 :228-37\"," +
		                "\"creationDate\": \"<unknown>\"," +
		                "\"publisher\": \"<unknown>\"" +
		            "}" +
		        "}" +
		    "]" +
		"}";
		
		JsArray<JsoAnnotationSetSummary> sets = asArrayOfAnnotationSetSummaries2("[" + json+ "]");
		return sets;
	}
	
	public JsArray<JsoAnnotationSetSummary> retrieveAnnotationByDocumentUrl(String url) {
		String json = "";

		if(true) { //url.equals("http://www.ncbi.nlm.nih.gov/pubmed/10679938")) {
//			json =
//			    "{" +
//			    "\"pav:versionNumber\": \"1\"," +
//			    "\"domeo_number_items\": 1," +
//			    "\"pav:lineageUri\": \"urn:domeoserver:uuid:06DF988C-33C0-453C-8BFC-D23E383CFE88\"," +
//			    "\"pav:createdOn\": \"2012-09-25 11:34:26 -0400\"," +
//			    "\"rdfs:label\": \"Default Set\"," +
//			    "\"pav:createdBy\": { " +
//			            "\"screenname\": \"Dr. John Doe\"," +
//			            "\"foaf_first_name\": \"John\"," +
//			            "\"foaf_last_name\": \"Doe\"" +
//			        "}," +
//			        "\"pav:lastSavedOn\": \"2012-09-25 11:34:27 -0400\"," +
//			        "\"@id\": \"urn:domeoclient:uuid:07895920-4A47-42DE-9F0A-9A79AAA33CC7\"," +
//			        "\"dct:description\": \"The default set is created automatically by Domeo when no other set is existing.\"" +
//			    "}";	
			
			json = "[{\"rdfs:label\":\"EuroPMC \",\"domeo:mongoUuid\":\"uNUDohDhR7KMorsRSLR2GQ\",\"@type\":\"domeo:DiscussionSet\",\"ao:numberItems\":3,\"pav:createdBy\":{\"screenname\":\"Dr. John Doe\",\"foaf_first_name\":\"John\",\"uri\":\"4028808d4185489401418548e3ca0000\",\"foaf_last_name\":\"Doe\"},\"pav:versionNumber\":\"2\",\"pav:lastSavedOn\":\"2013-10-04 17:05:15 -0400\",\"pav:lineageUri\":\"urn:domeoserver:annotationset:8B90099D-4C8C-45F5-ADBA-83D5999F5FD8\",\"pav:previousVersion\":\"urn:domeoclient:uuid:166EDE94-E8A4-4DDF-AA61-CF584F4721ED\",\"dct:description\":\"asdfsdfa\",\"permissions:accessType\":\"urn:domeo:access:public\",\"@id\":\"urn:domeoserver:annotationset:76B1FE2E-5663-4C77-9A17-5A5BDCC08407\",\"pav:createdOn\":\"2013-10-04 17:04:59 -0400\"},{\"rdfs:label\":\"Default Set\",\"domeo:mongoUuid\":\"GSQFXihqRpeY7-QbihmzGw\",\"@type\":\"ao:AnnotationSet\",\"ao:numberItems\":3,\"pav:createdBy\":{\"screenname\":\"Dr. John Doe\",\"foaf_first_name\":\"John\",\"uri\":\"4028808d4185489401418548e3ca0000\",\"foaf_last_name\":\"Doe\"},\"pav:versionNumber\":\"2\",\"pav:lastSavedOn\":\"2013-10-04 17:04:41 -0400\",\"pav:lineageUri\":\"urn:domeoserver:annotationset:DF0ED7B2-7594-4F9E-AF16-0C0DD8DE7935\",\"pav:previousVersion\":\"urn:domeoclient:uuid:580BCB7B-3A2F-410A-B106-A64B7589C7A7\",\"dct:description\":\"The default set is created automatically by Domeo when no other set is existing.\",\"permissions:accessType\":\"urn:domeo:access:public\",\"@id\":\"urn:domeoserver:annotationset:386FCC96-3486-46EF-A06E-2472084610C1\",\"pav:createdOn\":\"2013-10-04 17:04:32 -0400\"}, " 
				   + "{\"rdfs:label\":\"EuroPMC \",\"domeo:mongoUuid\":\"uNUDohDhR7KMorsRSLR2GQ\",\"@type\":\"domeo:DiscussionSet\",\"ao:numberItems\":3,\"pav:createdBy\":{\"screenname\":\"Dr. John Doe\",\"foaf_first_name\":\"John\",\"uri\":\"4028808d4185489401418548e3ca0002\",\"foaf_last_name\":\"Doe\"},\"pav:versionNumber\":\"2\",\"pav:lastSavedOn\":\"2013-10-04 17:05:15 -0400\",\"pav:lineageUri\":\"urn:domeoserver:annotationset:8B90099D-4C8C-45F5-ADBA-83D5999F5FD8\",\"pav:previousVersion\":\"urn:domeoclient:uuid:166EDE94-E8A4-4DDF-AA61-CF584F4721ED\",\"dct:description\":\"asdfsdfa\",\"permissions:accessType\":\"urn:domeo:access:public\",\"@id\":\"urn:domeoserver:annotationset:76B1FE2E-5663-4C77-9A17-5A5BDCC08408\",\"pav:createdOn\":\"2013-10-04 17:04:59 -0400\"},{\"rdfs:label\":\"Default Set\",\"domeo:mongoUuid\":\"GSQFXihqRpeY7-QbihmzGw\",\"@type\":\"ao:AnnotationSet\",\"ao:numberItems\":3,\"pav:createdBy\":{\"screenname\":\"Dr. John Doe\",\"foaf_first_name\":\"John\",\"uri\":\"4028808d4185489401418548e3ca0000\",\"foaf_last_name\":\"Doe\"},\"pav:versionNumber\":\"2\",\"pav:lastSavedOn\":\"2013-10-04 17:04:41 -0400\",\"pav:lineageUri\":\"urn:domeoserver:annotationset:DF0ED7B2-7594-4F9E-AF16-0C0DD8DE7935\",\"pav:previousVersion\":\"urn:domeoclient:uuid:580BCB7B-3A2F-410A-B106-A64B7589C7A7\",\"dct:description\":\"The default set is created automatically by Domeo when no other set is existing.\",\"permissions:accessType\":\"urn:domeo:access:public\",\"@id\":\"urn:domeoserver:annotationset:386FCC96-3486-46EF-A06E-2472084610C2\",\"pav:createdOn\":\"2013-10-04 17:04:32 -0400\"}]";
		}
			
		JsArray<JsoAnnotationSetSummary> sets = asArrayOfAnnotationSetSummaries2(json);
		return sets;
	}

	/**
	 * Given a specific url, it returns all the available accessible
	 * annotation for the given user. This call also allows to extend
	 * the request to documents with other urls that however match
	 * some of the available document identifiers.
	 * @param url		Url of the document of interest
	 * @param extend    If true, the method will extend the request to 
	 * 					other documents that share an id with the requested one
	 * @param username	User requesting the annotation
	 */
	JsArray<JsAnnotationSetSummary> retrieveAnnotationByDocumentUrl(String url, String allowed, boolean extend, String username) {
		String json = "{}";
		
		if(allowed.equals(AnnotationPersistenceServiceFacade.ALLOW_MINE)) { 
			json = 
		  "{"+
		    "\"uuid\": \"ABC1\"," +
		    "\"versionNumber\": \"1\"," +
		    "\"label\": \"Set di prova\","+
		    "\"description\": \"My description\","+
		    "\"createdOn\": \"1996.07.10 15:08:56 -0500\","+
		    "\"createdBy\": \" Dr. Paolo Ciccarese\","+
		    "\"createdByUrl\": \" http://www.paolociccarese.info \","+
		    "\"lastSavedOn\": \"1996.07.10 15:08:56 -0500\","+
		    "\"isLocked\": true,"+
		    "\"numberOfAnnotationItems\": 4"+
		  "},"+
		  "{"+
		    "\"uuid\": \"ABC2\"," +
		    "\"versionNumber\": \"2\"," +
		    "\"previousVersion\": \"ABC3\"," +
		    "\"label\": \"Set di prova 2\","+
		    "\"description\": \"My description 2\","+
		    "\"createdOn\": \"1998.07.10 15:08:56 -0500\","+
		    "\"createdBy\": \" Dr. Paolo Ciccarese\","+
		    "\"createdByUrl\": \" http://www.paolociccarese.info \","+
		    "\"lastSavedOn\": \"1998.07.10 15:08:56 -0500\","+
		    "\"isLocked\": false,"+
		    "\"numberOfAnnotationItems\": 4"+
		  "},";
		} else if(allowed.equals(AnnotationPersistenceServiceFacade.ALLOW_GROUPS)) {
			json = 
					  "{"+
					    "\"uuid\": \"ABC1\"," +
					    "\"versionNumber\": \"1\"," +
					    "\"label\": \"Set di prova\","+
					    "\"description\": \"My description\","+
					    "\"createdOn\": \"1996.07.10 15:08:56 -0500\","+
					    "\"createdBy\": \" John Doe\","+
					    "\"createdByUrl\": \" http://www.example.com \","+
					    "\"lastSavedOn\": \"1996.07.10 15:08:56 -0500\","+
					    "\"isLocked\": true,"+
					    "\"numberOfAnnotationItems\": 3"+
					  "},"+
					  "{"+
					    "\"uuid\": \"ABC1\"," +
					    "\"versionNumber\": \"1\"," +
					    "\"label\": \"Set di prova\","+
					    "\"description\": \"My description\","+
					    "\"createdOn\": \"1996.07.10 15:08:56 -0500\","+
					    "\"createdBy\": \" Jenny Doe\","+
					    "\"createdByUrl\": \" http://www.example.com \","+
					    "\"lastSavedOn\": \"1996.07.10 15:08:56 -0500\","+
					    "\"isLocked\": true,"+
					    "\"numberOfAnnotationItems\": 4"+
					  "},"+
					  "{"+
					    "\"uuid\": \"ABC2\"," +
					    "\"versionNumber\": \"2\"," +
					    "\"previousVersion\": \"ABC3\"," +
					    "\"label\": \"Set di prova 2\","+
					    "\"description\": \"My description 2\","+
					    "\"createdOn\": \"1998.07.10 15:08:56 -0500\","+
					    "\"createdBy\": \" John Doe\","+
					    "\"createdByUrl\": \" http://www.example.com \","+
					    "\"lastSavedOn\": \"1998.07.10 15:08:56 -0500\","+
					    "\"isLocked\": false,"+
					    "\"numberOfAnnotationItems\": 10"+
					  "},";
		} else if(allowed.equals(AnnotationPersistenceServiceFacade.ALLOW_PUBLIC)) {
			json = 
					  "{"+
					    "\"uuid\": \"ABC1\"," +
					    "\"versionNumber\": \"1\"," +
					    "\"label\": \"Euro PMC\","+
					    "\"description\": \"My description\","+
					    "\"createdOn\": \"1996.07.10 15:08:56 -0500\","+
					    "\"createdBy\": \" Euro PMC\","+
					    "\"createdByUrl\": \" http://www.example.com \","+
					    "\"lastSavedOn\": \"1996.07.10 15:08:56 -0500\","+
					    "\"isLocked\": true,"+
					    "\"numberOfAnnotationItems\": 1"+
					  "},"+
					  "{"+
					    "\"uuid\": \"ABC2\"," +
					    "\"versionNumber\": \"2\"," +
					    "\"previousVersion\": \"ABC3\"," +
					    "\"label\": \"Set di prova 2\","+
					    "\"description\": \"My description 2\","+
					    "\"createdOn\": \"1998.07.10 15:08:56 -0500\","+
					    "\"createdBy\": \" Johnny Deep\","+
					    "\"createdByUrl\": \" http://www.example.com \","+
					    "\"lastSavedOn\": \"1998.07.10 15:08:56 -0500\","+
					    "\"isLocked\": false,"+
					    "\"numberOfAnnotationItems\": 8"+
					  "},"+
					  "{"+
					    "\"uuid\": \"ABC1\"," +
					    "\"versionNumber\": \"1\"," +
					    "\"label\": \"Set di prova\","+
					    "\"description\": \"My description\","+
					    "\"createdOn\": \"1996.07.10 15:08:56 -0500\","+
					    "\"createdBy\": \" Johnny Smith\","+
					    "\"createdByUrl\": \" http://www.example.com \","+
					    "\"lastSavedOn\": \"1996.07.10 15:08:56 -0500\","+
					    "\"isLocked\": true,"+
					    "\"numberOfAnnotationItems\": 2"+
					  "},"+
					  "{"+
					    "\"uuid\": \"ABC2\"," +
					    "\"versionNumber\": \"2\"," +
					    "\"previousVersion\": \"ABC3\"," +
					    "\"label\": \"Set di prova 2\","+
					    "\"description\": \"My description 2\","+
					    "\"createdOn\": \"1998.07.10 15:08:56 -0500\","+
					    "\"createdBy\": \" Johnny Smith\","+
					    "\"createdByUrl\": \" http://www.example.com \","+
					    "\"lastSavedOn\": \"1998.07.10 15:08:56 -0500\","+
					    "\"isLocked\": false,"+
					    "\"numberOfAnnotationItems\": 8"+
					  "},"+
					  "{"+
					    "\"uuid\": \"ABC1\"," +
					    "\"versionNumber\": \"1\"," +
					    "\"label\": \"Set di prova\","+
					    "\"description\": \"My description\","+
					    "\"createdOn\": \"1996.07.10 15:08:56 -0500\","+
					    "\"createdBy\": \" Johnny Deep\","+
					    "\"createdByUrl\": \" http://www.example.com \","+
					    "\"lastSavedOn\": \"1996.07.10 15:08:56 -0500\","+
					    "\"isLocked\": true,"+
					    "\"numberOfAnnotationItems\": 2"+
					  "},"+
					  "{"+
					    "\"uuid\": \"ABC2\"," +
					    "\"versionNumber\": \"2\"," +
					    "\"previousVersion\": \"ABC3\"," +
					    "\"label\": \"Set di prova 2\","+
					    "\"description\": \"My description 2\","+
					    "\"createdOn\": \"1998.07.10 15:08:56 -0500\","+
					    "\"createdBy\": \" Johnny Deep\","+
					    "\"createdByUrl\": \" http://www.example.com \","+
					    "\"lastSavedOn\": \"1998.07.10 15:08:56 -0500\","+
					    "\"isLocked\": false,"+
					    "\"numberOfAnnotationItems\": 8"+
					  "},"+
					  "{"+
					    "\"uuid\": \"ABC1\"," +
					    "\"versionNumber\": \"1\"," +
					    "\"label\": \"Set di prova\","+
					    "\"description\": \"My description\","+
					    "\"createdOn\": \"1996.07.10 15:08:56 -0500\","+
					    "\"createdBy\": \" Johnny Smith\","+
					    "\"createdByUrl\": \" http://www.example.com \","+
					    "\"lastSavedOn\": \"1996.07.10 15:08:56 -0500\","+
					    "\"isLocked\": true,"+
					    "\"numberOfAnnotationItems\": 2"+
					  "},"+
					  "{"+
					    "\"uuid\": \"ABC2\"," +
					    "\"versionNumber\": \"2\"," +
					    "\"previousVersion\": \"ABC3\"," +
					    "\"label\": \"Set di prova 2\","+
					    "\"description\": \"My description 2\","+
					    "\"createdOn\": \"1998.07.10 15:08:56 -0500\","+
					    "\"createdBy\": \" Johnny Smith\","+
					    "\"createdByUrl\": \" http://www.example.com \","+
					    "\"lastSavedOn\": \"1998.07.10 15:08:56 -0500\","+
					    "\"isLocked\": false,"+
					    "\"numberOfAnnotationItems\": 8"+
					  "},";
		}
		
		JsArray<JsAnnotationSetSummary> sets = asArrayOfAnnotationSetSummaries("[" + json+ "]");
		//for(int i=0; i<sets.length(); i++) {
		//	Window.alert(""+fmt.format(sets.get(i).getCreatedOn()));
		//}
		return sets;
	}
	
	
	/**
	 * Given a specific id, it returns all the available accessible
	 * annotation for the given user.
	 * @param id
	 * @param idType
	 * @param username
	 */
	public JsArray<JsAnnotationSet> retrieveAnnotationById(String id, String idType, String allowed, String username) {
		return retrieveAnnotationById(id, idType, allowed, false, username);
	}
	
	/**
	 * Given a specific id, it returns all the available accessible
	 * annotation for the given user.
	 * @param id		The id of the document
	 * @param idType	The type of id (Pubmed, Pmc....)
	 * @param extend    If true, the method will extend the request to 
	 * 					other documents that share an id with the requested one
	 * @param username
	 */
	JsArray<JsAnnotationSet> retrieveAnnotationById(String id, String idType, String allowed, boolean extend, String username) {
		//String json = "[{\"pav:createdWith\": \"urn:domeo:software:id:Domeo-2.0alpha-040\",\"ao:item\": [{\"pav:createdWith\": \"urn:domeo:software:id:Domeo-2.0alpha-040\",\"rdfs:label\": \"Highlight\", \"pav:previousVersion\": \"\",  \"@type\": \"ao:Qualifier\",\"domeo:belongsToSet\": \"urn:domeoclient:uuid:E7779F6D-4FFD-428C-82CF-42ED72740B06\",\"pav:createdBy\": \"urn:person:uuid:4028808d42d966140142d9666ece0000\", \"pav:lastSavedOn\": \"2013-12-12 14:43:49 -0500\",\"pav:versionNumber\": \"1\", \"@id\": \"urn:domeoclient:uuid:76CC060A-193D-45DF-B3EC-A5F10B159282\", \"ao:context\": [ { \"ao:hasSource\": \"http://en.wikipedia.org/wiki/Amyloid_precursor_protein\", \"@type\": \"ao:SpecificResource\", \"@id\": \"urn:domeoclient:uuid:3AF2A533-58BA-4A9C-88B2-83AF57B8E85E\",\"domeo_temp_localId\": \"1\", \"ao:hasSelector\": { \"ao:prefix\": \"An important but largely unmet challenge in understanding the mechanisms that govern the \",\"@type\": \"ao:PrefixSuffixTextSelector\", \"@id\": \"urn:domeoclient:uuid:3AF2A533-58BA-4A9C-88B2-83AF57B8E85E\",\"domeo_temp_localId\": \"1\",  \"pav:createdOn\": \"2013-12-12 14:43:40 -0500\",\"ao:exact\": \"formation\", \"ao:suffix\": \" of specific organs is to decipher the complex and dynamic genetic programs exhibited by the diversity of cell types within the tissue of interest. \", \"domeo:uuid\": \"3AF2A533-58BA-4A9C-88B2-83AF57B8E85E\" } } ], \"ao:hasTopic\": [{ \"@id\": \"http://www.ebi.ac.uk/QuickGO/GTerm?id=GO:0009058\",\"rdfs:label\": \"biosynthetic process\",  \"dct:description\": \"\",  \"dct:source\": {  \"@id\": \"http://www.ebi.ac.uk/QuickGO/\",\"rdfs:label\": \"Biological Process\" } } ], \"pav:createdOn\": \"2013-12-12 14:43:40 -0500\", \"pav:lineageUri\": \"urn:domeoserver:annotation:75EAD458-85A3-4879-887E-FD034E5FD52A\"} ], \"rdfs:label\": \"Default Set\",\"domeo:deleted\": \"false\", \"ao:annotatesResource\": \"http://www.ncbi.nlm.nih.gov/pmc/articles/PMC1366495/?report=classic\", \"domeo:resources\": [  { \"label\": \"An Integrated Strategy for Analyzing the Unique Developmental Programs of Different Myoblast Subtypes\", \"url\": \"http://www.ncbi.nlm.nih.gov/pmc/articles/PMC1366495/?report=classic\" }], \"@type\": \"ao:AnnotationSet\", \"pav:createdBy\": \"urn:person:uuid:4028808d42d966140142d9666ece0000\", \"pav:lastSavedOn\": \"2013-12-12 14:43:48 -0500\", \"pav:versionNumber\": \"1\",\"permissions:permissions\": {\"permissions:isLocked\": \"false\",\"permissions:accessType\": \"urn:domeo:access:public\" }, \"pav:lineageUri\": \"urn:domeoserver:annotationset:43A7C6D5-3884-4729-9E18-759752463598\", \"domeo:agents\": [{\"rdfs:label\": \"EuroPMC\",  \"foafx:middlename\": \"\", \"foafx:lastname\": \"Doe\", \"@type\": \"foafx:Person\", \"foafx:homepage\": \"\",\"foafx:picture\": \"http://www.hcklab.org/images/me/paolo%20ciccarese-boston.jpg\",\"foafx:email\": \"admin@commonsemantics.org\",\"foafx:firstname\": \"John\",\"foafx:title\": \"\",\"@id\": \"urn:person:uuid:4028808d42d966140142d9666ece0000\", \"foafx:name\": \"Euro PMC\" },{ \"foafx:build\": \"040\" ,\"rdfs:label\": \"Domeo Annotation Toolkit\", \"@type\": \"foafx:Software\",  \"foafx:homepage\": \"\",\"@id\": \"urn:domeo:software:id:Domeo-2.0alpha-040\",\"foafx:name\": \"Domeo Annotation Toolkit\",\"foafx:version\": \"2.0alpha\" } ],\"pav:previousVersion\": \"\", \"dct:description\": \"The default set is created automatically by Domeo when no other set is existing.\", \"@id\": \"urn:domeoclient:uuid:E7779F6D-4FFD-428C-82CF-42ED72740B06\", \"pav:createdOn\": \"2013-12-12 14:43:40 -0500\" } ]";
		//String json = "[{\"pav:createdWith\":\"urn:domeo:software:id:Domeo-2.0alpha-040\",\"ao:item\":[{\"pav:createdWith\":\"urn:domeo:software:id:Domeo-2.0alpha-040\",\"rdfs:label\":\"SPL Annotation\",\"@type\":\"ao:SPLAnnotation\",\"domeo:belongsToSet\":\"urn:domeoclient:uuid:B2CBE747-9368-48D3-8AB8-0ECDA72850D1\",\"pav:createdBy\":\"urn:person:uuid:080e1430434f70e501434f711b6e0000\",\"pav:versionNumber\":\"1\",\"pav:lastSavedOn\":\"2014-01-22 17:35:57 -0500\",\"ao:context\":[{\"ao:hasSource\":\"http://127.0.0.1:8888/tests/SPL-annotation/Warfarin-ab047628-67d0-4a64-8d77-36b054969b44.html\",\"@type\":\"ao:SpecificResource\",\"@id\":\"urn:domeoclient:uuid:54CDC1E5-65E1-480A-A1C4-957BD32F7308\",\"domeo_temp_localId\":\"1\",\"ao:hasSelector\":{\"ao:prefix\":\"These highlights do not include all the information needed to use\",\"@type\":\"ao:PrefixSuffixTextSelector\",\"@id\":\"urn:domeoclient:uuid:54CDC1E5-65E1-480A-A1C4-957BD32F7308\",\"domeo_temp_localId\":\"1\",\"pav:createdOn\":\"2014-01-22 17:35:52 -0500\",\"ao:exact\":\" warfarin sodium safely and effectively. See full \",\"ao:suffix\":\"prescribing information for warfarin sodium.\",\"domeo:uuid\":\"54CDC1E5-65E1-480A-A1C4-957BD32F7308\"}}],\"ao:body\":[{\"sio:SIO_000563\":\"poc:PharmacokineticImpact\",\"@type\":\"poc:PharmacogenomicsStatement\",\"@id\":\"urn:linkedspls:uuid:38ACB655-54F2-43F3-8463-7AA1833725B5\",\"poc:PharmacokineticImpact\":\"http://purl.org/net/nlprepository/spl-pharmgx-annotation-poc#distribution-increase\"}],\"pav:lineageUri\":\"urn:domeoserver:annotation:A3653D48-231D-44CE-A309-B2C2F4311404\",\"pav:previousVersion\":\"\",\"@id\":\"urn:domeoclient:uuid:8E11483E-4C21-4188-AC79-B5BADA60ECE9\",\"pav:createdOn\":\"2014-01-22 17:35:52 -0500\"}],\"rdfs:label\":\"Default Set\",\"ao:annotatesResource\":\"http://127.0.0.1:8888/tests/SPL-annotation/Warfarin-ab047628-67d0-4a64-8d77-36b054969b44.html\",\"domeo:resources\":[{\"label\":\"WARFARIN SODIUM TABLET [AMERICAN HEALTH PACKAGING]\",\"url\":\"http://127.0.0.1:8888/tests/SPL-annotation/Warfarin-ab047628-67d0-4a64-8d77-36b054969b44.html\"}],\"@type\":\"ao:AnnotationSet\",\"pav:createdBy\":\"urn:person:uuid:080e1430434f70e501434f711b6e0000\",\"pav:lastSavedOn\":\"2014-01-22 17:35:57 -0500\",\"pav:versionNumber\":\"1\",\"permissions:permissions\":{\"permissions:isLocked\":\"false\",\"permissions:accessType\":\"urn:domeo:access:public\"},\"pav:lineageUri\":\"urn:domeoserver:annotationset:65B8CECA-8AFA-47C2-B14C-60ECB6FEC4F2\",\"domeo:agents\":[{\"rdfs:label\":\"boycer\",\"foafx:middlename\":\"\",\"foafx:lastname\":\"Boyce\",\"@type\":\"foafx:Person\",\"foafx:homepage\":\"\",\"foafx:picture\":\" http://www.hcklab.org/images/me/paolo%20ciccarese-boston.jpg\",\"foafx:email\":\"rdb20@pitt.edu\",\"foafx:firstname\":\"Richard\",\"foafx:title\":\"\",\"@id\":\"urn:person:uuid:080e1430434f70e501434f711b6e0000\",\"foafx:name\":\"boycer\"},{\"foafx:build\":\"040\",\"rdfs:label\":\"Domeo Annotation Toolkit\",\"@type\":\"foafx:Software\",\"foafx:homepage\":\"\",\"@id\":\"urn:domeo:software:id:Domeo-2.0alpha-040\",\"foafx:name\":\"Domeo Annotation Toolkit\",\"foafx:version\":\"2.0alpha\"}],\"pav:previousVersion\":\"\",\"dct:description\":\"The default set is created automatically by Domeo when no other set is existing.\",\"@id\":\"urn:domeoclient:uuid:B2CBE747-9368-48D3-8AB8-0ECDA72850D1\",\"pav:createdOn\":\"2014-01-22 17:35:44 -0500\"}]"; 
		
/*		String json = "[{\"pav:createdWith\":\"urn:domeo:software:id:Domeo-2.0alpha-040\",\"ao:item\":[{\"pav:createdWith\":\"urn:domeo:software:id:Domeo-2.0alpha-040\",\"rdfs:label\":\"SPL Annotation\",\"@type\":\"ao:SPLAnnotation\",\"domeo:belongsToSet\":\"urn:domeoclient:uuid:B2CBE747-9368-48D3-8AB8-0ECDA72850D1\",\"pav:createdBy\":\"urn:person:uuid:080e1430434f70e501434f711b6e0000\",\"pav:versionNumber\":\"1\",\"pav:lastSavedOn\":\"2014-01-22 17:35:57 -0500\",\"ao:context\":[{\"ao:hasSource\":\"http://127.0.0.1:8888/tests/SPL-annotation/Warfarin-ab047628-67d0-4a64-8d77-36b054969b44.html\",\"@type\":\"ao:SpecificResource\",\"@id\":\"urn:domeoclient:uuid:54CDC1E5-65E1-480A-A1C4-957BD32F7308\",\"domeo_temp_localId\":\"1\",\"ao:hasSelector\":{\"ao:prefix\":\"These highlights do not include all the information needed to use\",\"@type\":\"ao:PrefixSuffixTextSelector\",\"@id\":\"urn:domeoclient:uuid:54CDC1E5-65E1-480A-A1C4-957BD32F7308\",\"domeo_temp_localId\":\"1\",\"pav:createdOn\":\"2014-01-22 17:35:52 -0500\",\"ao:exact\":\" warfarin sodium safely and effectively. See full \",\"ao:suffix\":\"prescribing information for warfarin sodium.\",\"domeo:uuid\":\"54CDC1E5-65E1-480A-A1C4-957BD32F7308\"}}],\"ao:body\":" +
				"[{\"@id\":\"urn:linkedspls:uuid:F8735ED1-A5DB-43F3-9B69-A54A835D8919\", \"@type\":\"poc:PharmacogenomicsStatement\", \"sio:SIO_000563\":\"poc:MedicalCondition\", \"poc:MedicalCondition\":\"http://purl.org/net/nlprepository/spl-pharmgx-annotation-poc#concomitant-medication-concern\", \"rdfs:label\":\"Concomitant medication concern\", \"dct:description\":\"A concomitant medication of concern is mentioned.\"}," +
				"{\"@id\":\"urn:linkedspls:uuid:A229674C-9899-474B-96D3-C37E8759C341\", \"@type\":\"poc:PharmacogenomicsStatement\", \"sio:SIO_000563\":\"poc:VariantFrequency\", \"poc:VariantFrequency\":\"http://purl.org/net/nlprepository/spl-pharmgx-annotation-poc#population-frequency-mentioned\", \"rdfs:label\":\"Variant Frequency\", \"dct:description\":\"The frequency or proportion at which a variant occurs in a specific population is mentioned.\"}," +
				"{\"@id\":\"urn:linkedspls:uuid:8FFA514D-50B2-4FE2-9F52-A2A873373DEF\", \"@type\":\"poc:PharmacogenomicsStatement\", \"sio:SIO_000628\":\"dailymed:pharmgxBiomarker\", \"dailymed:pharmgxBiomarker\":\"http://fakeuri.org/CD25\", \"rdfs:label\":\"CD20_antigen\", \"dct:description\":\"The selected biomarker.\"}," +
				"{\"@id\":\"urn:linkedspls:uuid:14FC9A6F-F105-4BF7-B270-D4CB3A9FD966\", \"@type\":\"poc:PharmacogenomicsStatement\", \"sio:SIO_000563\":\"poc:PharmacokineticImpact\", \"poc:PharmacokineticImpact\":\"http://purl.org/net/nlprepository/spl-pharmgx-annotation-poc#absorption-increase\", \"rdfs:label\":\"Absorption Increase\", \"dct:description\":\"The pharmacogenomic biomarker is associated with a increase in absorption of the drug.\"}],\"pav:lineageUri\":\"urn:domeoserver:annotation:A3653D48-231D-44CE-A309-B2C2F4311404\",\"pav:previousVersion\":\"\",\"@id\":\"urn:domeoclient:uuid:8E11483E-4C21-4188-AC79-B5BADA60ECE9\",\"pav:createdOn\":\"2014-01-22 17:35:52 -0500\"}],\"rdfs:label\":\"Default Set\",\"ao:annotatesResource\":\"http://127.0.0.1:8888/tests/SPL-annotation/Warfarin-ab047628-67d0-4a64-8d77-36b054969b44.html\",\"domeo:resources\":[{\"label\":\"WARFARIN SODIUM TABLET [AMERICAN HEALTH PACKAGING]\",\"url\":\"http://127.0.0.1:8888/tests/SPL-annotation/Warfarin-ab047628-67d0-4a64-8d77-36b054969b44.html\"}],\"@type\":\"ao:AnnotationSet\",\"pav:createdBy\":\"urn:person:uuid:080e1430434f70e501434f711b6e0000\",\"pav:lastSavedOn\":\"2014-01-22 17:35:57 -0500\",\"pav:versionNumber\":\"1\",\"permissions:permissions\":{\"permissions:isLocked\":\"false\",\"permissions:accessType\":\"urn:domeo:access:public\"},\"pav:lineageUri\":\"urn:domeoserver:annotationset:65B8CECA-8AFA-47C2-B14C-60ECB6FEC4F2\",\"domeo:agents\":[{\"rdfs:label\":\"boycer\",\"foafx:middlename\":\"\",\"foafx:lastname\":\"Boyce\",\"@type\":\"foafx:Person\",\"foafx:homepage\":\"\",\"foafx:picture\":\" http://www.hcklab.org/images/me/paolo%20ciccarese-boston.jpg\",\"foafx:email\":\"rdb20@pitt.edu\",\"foafx:firstname\":\"Richard\",\"foafx:title\":\"\",\"@id\":\"urn:person:uuid:080e1430434f70e501434f711b6e0000\",\"foafx:name\":\"boycer\"},{\"foafx:build\":\"040\",\"rdfs:label\":\"Domeo Annotation Toolkit\",\"@type\":\"foafx:Software\",\"foafx:homepage\":\"\",\"@id\":\"urn:domeo:software:id:Domeo-2.0alpha-040\",\"foafx:name\":\"Domeo Annotation Toolkit\",\"foafx:version\":\"2.0alpha\"}],\"pav:previousVersion\":\"\",\"dct:description\":\"The default set is created automatically by Domeo when no other set is existing.\",\"@id\":\"urn:domeoclient:uuid:B2CBE747-9368-48D3-8AB8-0ECDA72850D1\",\"pav:createdOn\":\"2014-01-22 17:35:44 -0500\"}]";
		*/
		
		String json = "[{\"pav:createdWith\":\"urn:domeo:software:id:Domeo-2.0alpha-040\",\"ao:item\":[{\"pav:createdWith\":\"urn:domeo:software:id:Domeo-2.0alpha-040\",\"rdfs:label\":\"SPL Annotation\",\"@type\":\"ao:SPLAnnotation\",\"domeo:belongsToSet\":\"urn:domeoclient:uuid:B2CBE747-9368-48D3-8AB8-0ECDA72850D1\",\"pav:createdBy\":\"urn:person:uuid:080e1430434f70e501434f711b6e0000\",\"pav:versionNumber\":\"1\",\"pav:lastSavedOn\":\"2014-01-22 17:35:57 -0500\",\"ao:context\":[{\"ao:hasSource\":\"http://127.0.0.1:8888/tests/SPL-annotation/Warfarin-ab047628-67d0-4a64-8d77-36b054969b44.html\",\"@type\":\"ao:SpecificResource\",\"@id\":\"urn:domeoclient:uuid:54CDC1E5-65E1-480A-A1C4-957BD32F7308\",\"domeo_temp_localId\":\"1\",\"ao:hasSelector\":{\"ao:prefix\":\"These highlights do not include all the information needed to use\",\"@type\":\"ao:PrefixSuffixTextSelector\",\"@id\":\"urn:domeoclient:uuid:54CDC1E5-65E1-480A-A1C4-957BD32F7308\",\"domeo_temp_localId\":\"1\",\"pav:createdOn\":\"2014-01-22 17:35:52 -0500\",\"ao:exact\":\" warfarin sodium safely and effectively. See full \",\"ao:suffix\":\"prescribing information for warfarin sodium.\",\"domeo:uuid\":\"54CDC1E5-65E1-480A-A1C4-957BD32F7308\"}}],\"ao:body\":" +
				"[{\"@id\":\"urn:linkedspls:uuid:A229674C-9899-474B-96D3-C37E8759C341\", \"@type\":\"poc:PharmacogenomicsStatement\", \"sio:SIO_000563\":\"poc:VariantFrequency\", \"poc:VariantFrequency\":\"http://purl.org/net/nlprepository/spl-pharmgx-annotation-poc#population-frequency-mentioned\", \"rdfs:label\":\"Variant Frequency\", \"dct:description\":\"The frequency or proportion at which a variant occurs in a specific population is mentioned.\"}," +
				"{\"@id\":\"urn:linkedspls:uuid:3F5EFB36-7B8B-4A3F-BFBA-725C36A1D74F\", \"@type\":\"poc:PharmacogenomicsStatement\", \"sio:SIO_000628\":\"dailymed:pharmgxDrug\", \"dailymed:pharmgxDrug\":\"http://fakeuri.org/Atomoxetine\", \"rdfs:label\":\"Arsenic_Trioxide\", \"dct:description\":\"The drug of interest.\"},{\"@id\":\"urn:linkedspls:uuid:F2224CEF-E0DB-418F-ACF3-D3988D26775B\", \"@type\":\"poc:PharmacogenomicsStatement\", \"sio:SIO_000628\":\"dailymed:pharmgxBiomarker\", \"dailymed:pharmgxBiomarker\":\"http://fakeuri.org/CD25\", \"rdfs:label\":\"CD20_antigen\", \"dct:description\":\"The selected biomarker.\"}, " +
				"{\"@id\":\"urn:linkedspls:uuid:1AA7F9B3-F775-4397-BD31-F570BFAA582F\", \"@type\":\"poc:PharmacogenomicsStatement\", \"sio:SIO_000338\":\"poc:TestResult\", \"poc:TestResult\":\"http://purl.org/net/nlprepository/spl-pharmgx-annotation-poc#gene-SNP-positive\", \"rdfs:label\":\"gene-SNP-positive\", \"dct:description\":\"A test result that is somehow related to the biomarker.\"}," +
				"{\"@id\":\"urn:linkedspls:uuid:F12CFED5-0FB6-4715-85C0-280731AD2389\", \"@type\":\"poc:PharmacogenomicsStatement\", \"sio:SIO_000628\":\"poc:Variant\", \"poc:Variant\":\"http://purl.org/net/nlprepository/spl-pharmgx-annotation-poc#intermediate-metabolizer\", \"rdfs:label\":\"intermediate-metabolizer\", \"dct:description\":\"A specific variant of a gene, including the wild-type allele, or a patient phenotype\"}," +
				"{\"@id\":\"urn:linkedspls:uuid:CCB5DD68-8EBF-4B2A-BDA8-7AEE0076F6F2\", \"@type\":\"poc:PharmacogenomicsStatement\", \"sio:SIO_000563\":\"poc:MonitoringRecommendation\", \"poc:MonitoringRecommendation\":\"http://purl.org/net/nlprepository/spl-pharmgx-annotation-poc#recommended\", \"rdfs:label\":\"Recommended\", \"dct:description\":\"A recommended monitoring recommendation is related to the pharmacogenomic biomarker.\"}," +
				"{\"@id\":\"urn:linkedspls:uuid:02D1951E-D2F7-4564-8BED-C9B61C47AC69\", \"@type\":\"poc:PharmacogenomicsStatement\", \"sio:SIO_000563\":\"poc:TestRecommendation\", \"poc:TestRecommendation\":\"http://purl.org/net/nlprepository/spl-pharmgx-annotation-poc#required\", \"rdfs:label\":\"Required\", \"dct:description\":\"A required test is related to the biomarker.\"}," +
				"{\"@id\":\"urn:linkedspls:uuid:110D5162-B9B5-4F3F-B01A-506756CDEE5F\", \"@type\":\"poc:PharmacogenomicsStatement\", \"sio:SIO_000563\":\"poc:DoseSelectionRecommendation\", \"poc:DoseSelectionRecommendation\":\"http://purl.org/net/nlprepository/spl-pharmgx-annotation-poc#increase-from-recommended-baseline\", \"rdfs:label\":\"Increase from baseline\", \"dct:description\":\"The pharmacogenomic biomarker is related to a recommendation to increase the dose of the drug from the recommended baseline.\"}," +
				"{\"@id\":\"urn:linkedspls:uuid:03B92CE6-A8F5-4AF9-8A68-10F48D1542EB\", \"@type\":\"poc:PharmacogenomicsStatement\", \"sio:SIO_000563\":\"poc:DrugSelectionRecommendation\", \"poc:DrugSelectionRecommendation\":\"http://purl.org/net/nlprepository/spl-pharmgx-annotation-poc#alternative-recommended\", \"rdfs:label\":\"Alternative Recommended\", \"dct:description\":\"The pharmacogenomic biomarker is related to a recommendation to use an alternative drug.\"}," +
				"{\"@id\":\"urn:linkedspls:uuid:14FC9A6F-F105-4BF7-B270-D4CB3A9FD966\", \"@type\":\"poc:PharmacogenomicsStatement\", \"sio:SIO_000563\":\"poc:PharmacokineticImpact\", \"poc:PharmacokineticImpact\":\"http://purl.org/net/nlprepository/spl-pharmgx-annotation-poc#absorption-increase\", \"rdfs:label\":\"Absorption Increase\", \"dct:description\":\"The pharmacogenomic biomarker is associated with a increase in absorption of the drug.\"}," +
				"{\"@id\":\"urn:linkedspls:uuid:47B1922D-DAF0-430C-84E9-4EAB3766AF76\", \"@type\":\"poc:PharmacogenomicsStatement\", \"sio:SIO_000563\":\"poc:PharmacodynamicImpact\", \"poc:PharmacodynamicImpact\":\"http://purl.org/net/nlprepository/spl-pharmgx-annotation-poc#drug-efficacy-increased-from-baseline\", \"rdfs:label\":\"Increased Efficacy\", \"dct:description\":\"The pharmacogenomic biomarker is associated with an increase in the efficacy of the drug. \"}]" +
				",\"pav:lineageUri\":\"urn:domeoserver:annotation:A3653D48-231D-44CE-A309-B2C2F4311404\",\"pav:previousVersion\":\"\",\"@id\":\"urn:domeoclient:uuid:8E11483E-4C21-4188-AC79-B5BADA60ECE9\",\"pav:createdOn\":\"2014-01-22 17:35:52 -0500\"}],\"rdfs:label\":\"Default Set\",\"ao:annotatesResource\":\"http://127.0.0.1:8888/tests/SPL-annotation/Warfarin-ab047628-67d0-4a64-8d77-36b054969b44.html\",\"domeo:resources\":[{\"label\":\"WARFARIN SODIUM TABLET [AMERICAN HEALTH PACKAGING]\",\"url\":\"http://127.0.0.1:8888/tests/SPL-annotation/Warfarin-ab047628-67d0-4a64-8d77-36b054969b44.html\"}],\"@type\":\"ao:AnnotationSet\",\"pav:createdBy\":\"urn:person:uuid:080e1430434f70e501434f711b6e0000\",\"pav:lastSavedOn\":\"2014-01-22 17:35:57 -0500\",\"pav:versionNumber\":\"1\",\"permissions:permissions\":{\"permissions:isLocked\":\"false\",\"permissions:accessType\":\"urn:domeo:access:public\"},\"pav:lineageUri\":\"urn:domeoserver:annotationset:65B8CECA-8AFA-47C2-B14C-60ECB6FEC4F2\",\"domeo:agents\":[{\"rdfs:label\":\"boycer\",\"foafx:middlename\":\"\",\"foafx:lastname\":\"Boyce\",\"@type\":\"foafx:Person\",\"foafx:homepage\":\"\",\"foafx:picture\":\" http://www.hcklab.org/images/me/paolo%20ciccarese-boston.jpg\",\"foafx:email\":\"rdb20@pitt.edu\",\"foafx:firstname\":\"Richard\",\"foafx:title\":\"\",\"@id\":\"urn:person:uuid:080e1430434f70e501434f711b6e0000\",\"foafx:name\":\"boycer\"},{\"foafx:build\":\"040\",\"rdfs:label\":\"Domeo Annotation Toolkit\",\"@type\":\"foafx:Software\",\"foafx:homepage\":\"\",\"@id\":\"urn:domeo:software:id:Domeo-2.0alpha-040\",\"foafx:name\":\"Domeo Annotation Toolkit\",\"foafx:version\":\"2.0alpha\"}],\"pav:previousVersion\":\"\",\"dct:description\":\"The default set is created automatically by Domeo when no other set is existing.\",\"@id\":\"urn:domeoclient:uuid:B2CBE747-9368-48D3-8AB8-0ECDA72850D1\",\"pav:createdOn\":\"2014-01-22 17:35:44 -0500\"}]";
	
		
		
		JsArray<JsAnnotationSet> sets = asArrayOfAnnotationSets(json);
		return sets;
	}
}
