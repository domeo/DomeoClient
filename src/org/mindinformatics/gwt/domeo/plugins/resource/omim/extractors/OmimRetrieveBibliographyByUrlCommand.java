package org.mindinformatics.gwt.domeo.plugins.resource.omim.extractors;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.component.bibliography.src.IBibliographicParameters;
import org.mindinformatics.gwt.domeo.plugins.annotation.persistence.src.IRetrieveExistingBibliographySetHandler;
import org.mindinformatics.gwt.domeo.plugins.persistence.json.unmarshalling.JsonUnmarshallingManager;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.extractors.APubMedBibliograhyExtractorCommand;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.service.PubMedManager;
import org.mindinformatics.gwt.framework.src.ICommand;
import org.mindinformatics.gwt.framework.src.ICommandCompleted;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.user.client.Window;

/**
 * Stage for retrieving existing bibliography for the current document identified by the URL.
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class OmimRetrieveBibliographyByUrlCommand extends APubMedBibliograhyExtractorCommand implements ICommand, IRetrieveExistingBibliographySetHandler {

	public static final String FUNCTION = IBibliographicParameters.EXTRACT_REFERENCES;
	public static final String LABEL = "References extractor";
	public static final String UNRECOGNIZED = "UNRECOGNIZED";
	
	private IDomeo _domeo;
	private ICommandCompleted _completionCallback;

	public OmimRetrieveBibliographyByUrlCommand(IDomeo domeo, ICommandCompleted completionCallback) {
		_domeo = domeo;		
		_completionCallback = completionCallback;
	}
	
	@Override
	public void execute() {
		String url = _domeo.getPersistenceManager().getCurrentResourceUrl();
		if(url!=null) {
			try {
				Window.alert("Retrieve existing bibliography");
				//_completionCallback.notifyStageCompletion();
				
				PubMedManager pubMedManager = PubMedManager.getInstance();
				pubMedManager.selectPubMedConnector(_domeo, null);
				pubMedManager.retrieveExistingBibliographySetByUrl(_domeo, this, url);
			} catch (Exception e) {
				_domeo.getLogger().exception(this, "While retrieving bibliography " + e.getMessage());
				_completionCallback.notifyStageCompletion();
			}
		} else {
			_domeo.getLogger().exception(this, "No url defined for performing extraction");
			_completionCallback.notifyStageCompletion();
		}
	}

	@Override
	public void setExistingBibliographySetList(JsArray responseOnSets, boolean isVirtual) {
		JsonUnmarshallingManager manager = _domeo.getUnmarshaller();
		manager.unmarshallBibliography(responseOnSets, true, 0);
		_completionCallback.notifyStageCompletion();
	}

	@Override
	public void bibliographySetListNotCreated() {
		_completionCallback.notifyStageCompletion();
	}
	
	@Override
	public void bibliographySetListNotCreated(String message) {
		_completionCallback.notifyStageCompletion();
	}
}
