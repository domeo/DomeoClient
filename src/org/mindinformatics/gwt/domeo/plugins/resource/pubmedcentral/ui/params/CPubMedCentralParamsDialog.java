package org.mindinformatics.gwt.domeo.plugins.resource.pubmedcentral.ui.params;

import java.util.HashMap;
import java.util.Map;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.component.bibliography.src.IBibliographicParameters;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmedcentral.extractors.v2.PubMedCentralExtractionDialogCommand;
import org.mindinformatics.gwt.framework.component.pipelines.src.IParametersCache;
import org.mindinformatics.gwt.framework.component.preferences.src.TextPreference;
import org.mindinformatics.gwt.framework.src.IContainerPanel;
import org.mindinformatics.gwt.framework.src.IContentPanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Provides the standard lens for the annotation type Highlight.
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class CPubMedCentralParamsDialog extends Composite implements IContentPanel {

	public static final String TITLE = "PubMed Central Extractor Pipeline";
	
	interface Binder extends UiBinder<Widget, CPubMedCentralParamsDialog> { }
	private static final Binder binder = GWT.create(Binder.class);
	
	// By contract 
	private IDomeo _domeo;
	private IContainerPanel _glassPanel;
	private PubMedCentralExtractionDialogCommand _command;
	private Map<String, String> _params = new HashMap<String, String>();
	//private Map<String, String> _paramsValues = new HashMap<String, String>();
	
	@UiField VerticalPanel body;
	@UiField VerticalPanel stagesPanel;
	@UiField CheckBox PubMedCentralExtractMetaCommandBox;
	@UiField Label metadataField;
	@UiField CheckBox PubMedCentralExtractSubjectCommandBox;
	@UiField Label selfReferenceField;
	@UiField CheckBox PubMedCentralExtractReferencesCommandBox;
	@UiField Label referencesField;
	@UiField CheckBox PubMedCentralExtractCitationsCommandBox;
	@UiField Label citationsField;
	@UiField Button processButton;
	@UiField CheckBox saveAsDefault;
	
	
	public CPubMedCentralParamsDialog(IDomeo domeo, final PubMedCentralExtractionDialogCommand command, IParametersCache parametersCache) {
		_domeo = domeo;
		_command = command;

		_params.put(IBibliographicParameters.EXTRACT_META, _domeo.getPreferences().getPreferenceItem(IBibliographicParameters.COMPONENT, IBibliographicParameters.EXTRACT_META).toString());
		_params.put(IBibliographicParameters.EXTRACT_SUBJECT, _domeo.getPreferences().getPreferenceItem(IBibliographicParameters.COMPONENT, IBibliographicParameters.EXTRACT_SUBJECT).toString());
		_params.put(IBibliographicParameters.EXTRACT_REFERENCES, _domeo.getPreferences().getPreferenceItem(IBibliographicParameters.COMPONENT, IBibliographicParameters.EXTRACT_REFERENCES).toString());
		_params.put(IBibliographicParameters.EXTRACT_CITATIONS, _domeo.getPreferences().getPreferenceItem(IBibliographicParameters.COMPONENT, IBibliographicParameters.EXTRACT_CITATIONS).toString());
		
		initWidget(binder.createAndBindUi(this));
		
		//for(IStage stage: _stages) {
		//	_paramsValues.put(stage.getCommand().getClass().getName().substring(stage.getCommand().getClass().getName().lastIndexOf(".")+1), stage.getCommand().getClass().getName());
		//}
		
		saveAsDefault.setEnabled(false);
		refreshCheckBoxes();
		
		PubMedCentralExtractSubjectCommandBox.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				_domeo.getPreferences().changePreferenceItemValue(
						IBibliographicParameters.COMPONENT, IBibliographicParameters.EXTRACT_SUBJECT, 
					PubMedCentralExtractSubjectCommandBox.getValue()?IBibliographicParameters.EXECUTE:IBibliographicParameters.SKIP);
				
				//_params.put(IBibliographicParameters.EXTRACT_SUBJECT,
				//		PubMedCentralExtractSubjectCommandBox.getValue()?APipeline.EXECUTE:APipeline.SKIP);
				refreshCheckBoxes();
			}
		});
		PubMedCentralExtractReferencesCommandBox.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				_domeo.getPreferences().changePreferenceItemValue(
						IBibliographicParameters.COMPONENT, IBibliographicParameters.EXTRACT_REFERENCES, 
						PubMedCentralExtractReferencesCommandBox.getValue()?IBibliographicParameters.EXECUTE:IBibliographicParameters.SKIP);
				
				//_params.put(IBibliographicParameters.EXTRACT_REFERENCES,
				//		PubMedCentralExtractReferencesCommandBox.getValue()?IBibliographicParameters.EXECUTE:IBibliographicParameters.SKIP);
				refreshCheckBoxes();
			}
		});
		PubMedCentralExtractCitationsCommandBox.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				_domeo.getPreferences().changePreferenceItemValue(
						IBibliographicParameters.COMPONENT, IBibliographicParameters.EXTRACT_CITATIONS, 
						PubMedCentralExtractCitationsCommandBox.getValue()?IBibliographicParameters.EXECUTE:IBibliographicParameters.SKIP);
				
				//_params.put(IBibliographicParameters.EXTRACT_CITATIONS,
				//		PubMedCentralExtractCitationsCommandBox.getValue()?IBibliographicParameters.EXECUTE:IBibliographicParameters.SKIP);
				refreshCheckBoxes();
			}
		});

		
		processButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				processButton.setEnabled(false);
				processButton.setText("Processing");
				command.completed();
				_glassPanel.hide();
			}
		});
	}
	
	private void refreshCheckBoxes() {
		metadataField.setText("(Extracted)");
		PubMedCentralExtractMetaCommandBox.setValue(true);
		PubMedCentralExtractMetaCommandBox.setEnabled(false);
		
		int level = _domeo.getAnnotationPersistenceManager().getBibliographicSet().getLevel();
		_domeo.getLogger().info(this, "Loading level " + level);
		
		PubMedCentralExtractSubjectCommandBox.setValue(true);
		PubMedCentralExtractSubjectCommandBox.setEnabled(false);

		if(level<1) {
			PubMedCentralExtractReferencesCommandBox.setValue
				(((TextPreference)_domeo.getPreferences().getPreferenceItem(IBibliographicParameters.COMPONENT, 
						IBibliographicParameters.EXTRACT_REFERENCES)).getValue().equals(IBibliographicParameters.SKIP)?false:true);
			
			PubMedCentralExtractCitationsCommandBox.setValue
				(((TextPreference)_domeo.getPreferences().getPreferenceItem(IBibliographicParameters.COMPONENT, 
						IBibliographicParameters.EXTRACT_CITATIONS)).getValue().equals(IBibliographicParameters.SKIP)?false:true);
			PubMedCentralExtractCitationsCommandBox.setEnabled(!(((TextPreference)_domeo.getPreferences().getPreferenceItem(IBibliographicParameters.COMPONENT, 
					IBibliographicParameters.EXTRACT_CITATIONS)).getValue()).equals(IBibliographicParameters.SKIP));
		}
		if(level>1) {
			selfReferenceField.setText("(Retrieved)");
		}
		if(level>=2) {
			PubMedCentralExtractReferencesCommandBox.setValue(true);
			PubMedCentralExtractReferencesCommandBox.setEnabled(false);
			referencesField.setText("(Retrieved)");
			
			if((((TextPreference)_domeo.getPreferences().getPreferenceItem(IBibliographicParameters.COMPONENT, 
					IBibliographicParameters.EXTRACT_CITATIONS)).getValue()).equals(IBibliographicParameters.SKIP)) {
				PubMedCentralExtractCitationsCommandBox.setEnabled(false);
				citationsField.setText("(Disabled)");
			}
			/*
			 // Create a new timer that calls Window.alert().
		    Timer t = new Timer() {
		    	public void run() {
		    		_command.completed();
					_glassPanel.hide();
		    	}
		    };
		    t.schedule(2000);
		    */
			
		}
		if(level>=3) {
			PubMedCentralExtractCitationsCommandBox.setEnabled(false);
			PubMedCentralExtractCitationsCommandBox.setValue(false);
			citationsField.setText("(Retrieved)");
		}
	}
	
	public String getTitle() {
		return TITLE;
	}

	@Override
	public void setContainer(IContainerPanel glassPanel) {
		_glassPanel = glassPanel;
	}

	@Override
	public IContainerPanel getContainer() {
		return _glassPanel;
	}
}
