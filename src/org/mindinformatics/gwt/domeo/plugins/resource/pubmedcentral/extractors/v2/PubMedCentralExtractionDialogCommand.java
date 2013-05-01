package org.mindinformatics.gwt.domeo.plugins.resource.pubmedcentral.extractors.v2;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.component.bibliography.src.IBibliographicParameters;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmedcentral.ui.params.CPubMedCentralParamsDialog;
import org.mindinformatics.gwt.framework.component.pipelines.src.IParametersCache;
import org.mindinformatics.gwt.framework.component.ui.glass.EnhancedGlassPanel;
import org.mindinformatics.gwt.framework.src.ICommand;
import org.mindinformatics.gwt.framework.src.ICommandCompleted;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class PubMedCentralExtractionDialogCommand implements ICommand, ClickHandler {

	public static final String FUNCTION = IBibliographicParameters.EXTRACT_REFERENCES;
	public static final String LABEL = "References extractor";
	public static final String UNRECOGNIZED = "UNRECOGNIZED";
	
	private IDomeo _domeo;
	private IParametersCache _parametersCache;
	private ICommandCompleted _completionCallback;
	
	
	public PubMedCentralExtractionDialogCommand(IDomeo domeo, IParametersCache parametersCache, ICommandCompleted completionCallback) {
		_completionCallback = completionCallback;
		_parametersCache = parametersCache;
		_domeo = domeo;		
	}
	
	public void completed() {
		_completionCallback.notifyStageCompletion();
	}

	@Override
	public void execute() {		
		CPubMedCentralParamsDialog lwp = new CPubMedCentralParamsDialog(_domeo, this, _parametersCache);
		new EnhancedGlassPanel(_domeo, lwp, lwp.getTitle(), 300, false, false, false, this);
	}
	

	@Override
	public void onClick(ClickEvent event) {
		completed();
	}
}
