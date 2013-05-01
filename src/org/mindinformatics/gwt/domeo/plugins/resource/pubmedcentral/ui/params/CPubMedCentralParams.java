package org.mindinformatics.gwt.domeo.plugins.resource.pubmedcentral.ui.params;

import java.util.HashMap;
import java.util.Map;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.component.bibliography.src.IBibliographicParameters;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmedcentral.extractors.v2.PubMedCentralExtractCitationsCommand;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmedcentral.extractors.v2.PubMedCentralExtractMetaCommand;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmedcentral.extractors.v2.PubMedCentralExtractReferencesCommand;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmedcentral.extractors.v2.PubMedCentralExtractSubjectCommand;
import org.mindinformatics.gwt.domeo.services.extractors.IExtractorsManager;
import org.mindinformatics.gwt.framework.component.preferences.src.TextPreference;
import org.mindinformatics.gwt.framework.src.IContainerPanel;
import org.mindinformatics.gwt.framework.src.IContentPanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Provides the standard lens for the annotation type Highlight.
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class CPubMedCentralParams extends Composite implements IContentPanel {

	public static final String TITLE = "PubMed Central Extractor Pipeline";
	
	interface Binder extends UiBinder<Widget, CPubMedCentralParams> { }
	private static final Binder binder = GWT.create(Binder.class);
	
	// By contract 
	private IDomeo _domeo;
	private IExtractorsManager _manager;
	private IContainerPanel _glassPanel;
	private Map<String, String> _params = new HashMap<String, String>();
	//private Map<String, String> _paramsValues = new HashMap<String, String>();
	
	@UiField VerticalPanel body;
	@UiField VerticalPanel stagesPanel;
	@UiField CheckBox PubMedCentralExtractMetaCommandBox;
	@UiField CheckBox PubMedCentralExtractSubjectCommandBox;
	@UiField CheckBox PubMedCentralExtractReferencesCommandBox;
	@UiField CheckBox PubMedCentralExtractCitationsCommandBox;
	@UiField Button processButton;
	@UiField CheckBox saveAsDefault;
	
	
	public CPubMedCentralParams(IDomeo domeo, IExtractorsManager manager, Map<String, String> params) {
		_domeo = domeo;
		_manager = manager;
		_params = params;
		
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
				
				_params.put(PubMedCentralExtractMetaCommand.class.getName(), Boolean.toString(((TextPreference) _domeo.getPreferences().getPreferenceItem(IBibliographicParameters.COMPONENT, IBibliographicParameters.EXTRACT_META)).getValue().equals(IBibliographicParameters.EXECUTE)));
				_params.put(PubMedCentralExtractSubjectCommand.class.getName(), Boolean.toString(((TextPreference) _domeo.getPreferences().getPreferenceItem(IBibliographicParameters.COMPONENT, IBibliographicParameters.EXTRACT_SUBJECT)).getValue().equals(IBibliographicParameters.EXECUTE)));
				_params.put(PubMedCentralExtractReferencesCommand.class.getName(), Boolean.toString(((TextPreference) _domeo.getPreferences().getPreferenceItem(IBibliographicParameters.COMPONENT, IBibliographicParameters.EXTRACT_REFERENCES)).getValue().equals(IBibliographicParameters.EXECUTE)));
				_params.put(PubMedCentralExtractCitationsCommand.class.getName(),  Boolean.toString(((TextPreference) _domeo.getPreferences().getPreferenceItem(IBibliographicParameters.COMPONENT, IBibliographicParameters.EXTRACT_CITATIONS)).getValue().equals(IBibliographicParameters.EXECUTE)));
				
				_manager.processDocumentExtraction(_params);
				_glassPanel.hide();
			}
		});
	}
	
	private void refreshCheckBoxes() {
		PubMedCentralExtractMetaCommandBox.setValue(true);
		PubMedCentralExtractMetaCommandBox.setEnabled(false);
		PubMedCentralExtractSubjectCommandBox.setValue(true);
		PubMedCentralExtractSubjectCommandBox.setEnabled(false);

		/*
		if(_paramsValues.get("PubMedCentralExtractSubjectCommand")!=null) {
			PubMedCentralExtractSubjectCommand.setValue(_params.get(_paramsValues.get("PubMedCentralExtractSubjectCommand")).equals(APipeline.SKIP)?false:true);
			if(_params.get(_paramsValues.get("PubMedCentralExtractSubjectCommand")).equals(APipeline.SKIP)) {
				PubMedCentralExtractReferencesCommand.setEnabled(false);
				PubMedCentralExtractReferencesCommand.setValue(false);
				_params.put(_paramsValues.get("PubMedCentralExtractReferencesCommand"),APipeline.SKIP);
				PubMedCentralExtractCitationsCommand.setEnabled(false);
				PubMedCentralExtractCitationsCommand.setValue(false);
				_params.put(_paramsValues.get("PubMedCentralExtractCitationsCommand"),APipeline.SKIP);
				return;
			}
		}
		*/

		PubMedCentralExtractReferencesCommandBox.setValue
			(((TextPreference)_domeo.getPreferences().getPreferenceItem(IBibliographicParameters.COMPONENT, IBibliographicParameters.EXTRACT_REFERENCES)).getValue().equals(IBibliographicParameters.SKIP)?false:true);
		if((((TextPreference)_domeo.getPreferences().getPreferenceItem(IBibliographicParameters.COMPONENT, IBibliographicParameters.EXTRACT_REFERENCES)).getValue()).equals(IBibliographicParameters.SKIP)) {
			PubMedCentralExtractCitationsCommandBox.setEnabled(false);
			PubMedCentralExtractCitationsCommandBox.setValue(false);
			return;
		} else {
			PubMedCentralExtractCitationsCommandBox.setEnabled(true);
		}

		PubMedCentralExtractCitationsCommandBox.setValue
			(((TextPreference)_domeo.getPreferences().getPreferenceItem(IBibliographicParameters.COMPONENT, IBibliographicParameters.EXTRACT_CITATIONS)).getValue().equals(IBibliographicParameters.SKIP)?false:true);
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
