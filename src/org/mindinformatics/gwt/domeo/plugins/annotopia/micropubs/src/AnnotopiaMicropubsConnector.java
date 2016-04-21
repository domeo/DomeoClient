package org.mindinformatics.gwt.domeo.plugins.annotopia.micropubs.src;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.service.IMicroPublicationsConnector;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.service.IRetrieveMicropublicationsHandler;
import org.mindinformatics.gwt.domeo.plugins.annotopia.base.src.AnnotopiaUtils;
import org.mindinformatics.gwt.framework.src.IApplication;
import org.mindinformatics.gwt.framework.src.Utils;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.http.client.URL;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.Properties;
import com.google.gwt.query.client.js.JsUtils;
import com.google.gwt.query.client.plugins.ajax.Ajax;
import com.google.gwt.user.client.Window;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class AnnotopiaMicropubsConnector implements IMicroPublicationsConnector {

	private static final String SERVICE_FRAGMENT = "cn/micropubs";
	private static final String RETRIEVE_FRAGMENT = "retrieve/";
	private static final String SEARCH_FRAGMENT = "search";
	
	private static final String URL_DEF = Utils.DEFAULT_URL;
	
	protected IApplication _application;

	public AnnotopiaMicropubsConnector(IApplication application) {
		_application = application;
	}
	
	@Override
	public void retrieveMicropublicationByUrn(final IRetrieveMicropublicationsHandler handler, final String urn) throws IllegalArgumentException {
		_application.getLogger().debug(this, "Retrieving micropublication by urn [" + urn + "]");
		_application.getProgressPanelContainer().setProgressMessage("Retrieving micropublication from Annotopia...");

		final String failureMsg = "Couldn't complete micropublication retrieval process";
		try {
			Ajax.ajax(Ajax.createSettings()
				.setUrl(URL_DEF + SERVICE_FRAGMENT + RETRIEVE_FRAGMENT + URL.encode(urn))
				/*.setHeaders(AnnotopiaUtils.getAnnotopiaHeaders())*/
		        .setDataType("json") 
		        .setType("get")      
		        .setTimeout(10000)
		        .setSuccess(new Function(){ // callback to be run if the request success
		    		public void f() {
		    			IDomeo _domeo = ((IDomeo)_application);

		    			JavaScriptObject jsSet = Utils.parseJson(getDataProperties().toJsonString());
		    			Window.alert("AnnotopiaMicropubsConnector:" + getDataProperties().toJsonString());
		    			
//		    			AnnotopiaConverter unmarshaller = new AnnotopiaConverter(_domeo);
//		    			
//		    			MAnnotationSet set = unmarshaller.unmarshallBasicAnnotationSet(jsSet, true);	    			
//		    			if(set==null) {
//		    				// TODO message no annotation found
//		    				_application.getLogger().info(this, "No annotation set found");
//		    				_application.getProgressPanelContainer().setCompletionMessage("Annotation Set not found");
//		    			} else {	
//		    				((AnnotationPersistenceManager) _domeo.getPersistenceManager()).loadAnnotationSet(set);
//		    				_application.getProgressPanelContainer().hide();
//		    				_application.getLogger().debug(this, "Completed Execution of retrieveExistingAnnotationSets() in " + (System.currentTimeMillis()-((IDomeo)_application).getDocumentPipelineTimer())+ "ms");
//		    				_domeo.refreshAllComponents();
//		    			}
		    		}
		        })
		        .setError(new Function(){ 
		        	public void f() {		        		
		        		_application.getLogger().exception(this, failureMsg);
		        		_application.getProgressPanelContainer().setErrorMessage(failureMsg);
		        		handler.resourceNotFound(null);
		        	}
		        })
		     );
		} catch (Exception e) {
			_application.getLogger().exception(this, failureMsg + ": " + e.getMessage());
			handler.exception(failureMsg + ": " + e.getMessage());
		}
	}

	@Override
	public void retrieveMicropublicationsByDocumentUrl(IRetrieveMicropublicationsHandler handler, String documentUrl) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void searchMicropublications(final IRetrieveMicropublicationsHandler handler, String typeQuery, String textQuery, int maxResults, int offset) throws IllegalArgumentException {
		_application.getLogger().debug(this, "Searching micropublication by query [" + textQuery + "]");
		_application.getProgressPanelContainer().setProgressMessage("Searching micropublications in Annotopia...");

		final String failureMsg = "Couldn't complete micropublication search process";
		try {
			JsUtils.JsUtilsImpl utils = new JsUtils.JsUtilsImpl();
			Properties v = utils.parseJSON("{\"typeQuery\":\"" + typeQuery + "\",\"textQuery\":\"" + textQuery + "\"}");
			Ajax.ajax(Ajax.createSettings()
				.setUrl(URL_DEF + SERVICE_FRAGMENT)
				.setHeaders(AnnotopiaUtils.getAnnotopiaHeaders()) // Needs key as the backend is annotopia
		        .setDataType("json") 
		        .setType("get")      // post, get
		        .setData(GQuery.$$("q:"+textQuery + "," + "typeQuery:" + typeQuery + "," + "max:" + maxResults + "," + "offset:" + offset) ) // parameters for the query-string
		        .setTimeout(10000)
		        .setSuccess(new Function(){ // callback to be run if the request success
		    		public void f() {
		    			IDomeo _domeo = ((IDomeo)_application);
		    			_application.getLogger().debug(this, "1");
		    			_application.getLogger().debug(this, "1 " + getDataProperties().toJsonString());
		    			JavaScriptObject jsSet = Utils.parseJson(getDataProperties().toJsonString());
		    			
		    			_application.getLogger().debug(this, "2");
		    			Window.alert("AnnotopiaMicropubsConnector:" + getDataProperties().toJsonString());
		    			
//		    			AnnotopiaConverter unmarshaller = new AnnotopiaConverter(_domeo);
//		    			
//		    			MAnnotationSet set = unmarshaller.unmarshallBasicAnnotationSet(jsSet, true);	    			
//		    			if(set==null) {
//		    				// TODO message no annotation found
//		    				_application.getLogger().info(this, "No annotation set found");
//		    				_application.getProgressPanelContainer().setCompletionMessage("Annotation Set not found");
//		    			} else {	
//		    				((AnnotationPersistenceManager) _domeo.getPersistenceManager()).loadAnnotationSet(set);
//		    				_application.getProgressPanelContainer().hide();
//		    				_application.getLogger().debug(this, "Completed Execution of retrieveExistingAnnotationSets() in " + (System.currentTimeMillis()-((IDomeo)_application).getDocumentPipelineTimer())+ "ms");
//		    				_domeo.refreshAllComponents();
//		    			}
		    		}
		        })
		        .setError(new Function(){ 
		        	public void f() {	
		        		Window.alert("AnnotopiaMicropubsConnector:error"); 
		        		_application.getLogger().exception(this, failureMsg);
		        		_application.getProgressPanelContainer().setErrorMessage(failureMsg);
		        		handler.resourceNotFound(null);
		        	}
		        })
		     );
		} catch (Exception e) {
			_application.getLogger().exception(this, failureMsg + ": " + e.getMessage());
			handler.exception(failureMsg + ": " + e.getMessage());
		}
	}
}
