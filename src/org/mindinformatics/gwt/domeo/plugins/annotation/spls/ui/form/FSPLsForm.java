package org.mindinformatics.gwt.domeo.plugins.annotation.spls.ui.form;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.mindinformatics.gwt.domeo.client.Domeo;
import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.ui.annotation.forms.AFormComponent;
import org.mindinformatics.gwt.domeo.client.ui.annotation.forms.AFormsManager;
import org.mindinformatics.gwt.domeo.client.ui.annotation.forms.images.ImageAnnotationFormsPanel;
import org.mindinformatics.gwt.domeo.client.ui.annotation.forms.text.TextAnnotationFormsPanel;
import org.mindinformatics.gwt.domeo.client.ui.content.AnnotationFrameWrapper;
import org.mindinformatics.gwt.domeo.component.cache.images.ui.ICachedImages;
import org.mindinformatics.gwt.domeo.model.AnnotationFactory;
import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.model.persistence.AnnotationPersistenceManager;
import org.mindinformatics.gwt.domeo.model.selectors.MTextQuoteSelector;
import org.mindinformatics.gwt.domeo.plugins.annotation.spls.model.MPharmgx;
import org.mindinformatics.gwt.domeo.plugins.annotation.spls.model.MSPLsAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.spls.model.MSPLPharmgxUsage;
import org.mindinformatics.gwt.domeo.plugins.annotation.spls.model.SPLsFactory;
import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;
import org.mindinformatics.gwt.framework.component.resources.model.ResourcesFactory;
import org.mindinformatics.gwt.framework.src.IResizable;
import org.mindinformatics.gwt.framework.widget.ButtonWithIcon;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TabBar;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Richard Boyce <rdb20@pitt.edu>
 */
public class FSPLsForm extends AFormComponent implements IResizable {

	public static final String LABEL = "SPL Annotation";
	public static final String LABEL_EDIT = "EDIT SPL ANNOTATION";
	
	public static final String LOG_CATEGORY_QUALIFIER_CREATE = "CREATING SPL ANNOTATION";
	public static final String LOG_CATEGORY_QUALIFIER_EDIT = "EDITING SPL ANNOTATION";
	
        public static final String SPL_POC_PREFIX = 
		"http://purl.org/net/nlprepository/spl-pharmgx-annotation-poc#";
	
	interface Binder extends UiBinder<VerticalPanel, FSPLsForm> { }
	private static final Binder binder = GWT.create(Binder.class);
	
	private MSPLsAnnotation _item;
	private MPharmgx currentPharmgx;
	
	private ArrayList<Widget> tabs = new ArrayList<Widget>();
	
	@UiField VerticalPanel container;
	@UiField FlowPanel newQualifiers;
	@UiField HorizontalPanel buttonsPanel;
	@UiField ListBox annotationSet;
	@UiField VerticalPanel rightColumn;
	@UiField TabBar tabBar;
	
	//PK Impact
	
	@UiField RadioButton descriptpkdm;
	@UiField RadioButton descriptpkim;
	@UiField RadioButton descriptpkia;
	@UiField RadioButton descriptpkda;
	@UiField RadioButton descriptpkid;
	@UiField RadioButton descriptpkdd;
	@UiField RadioButton descriptpkie;
	@UiField RadioButton descriptpkde;
	@UiField RadioButton descriptpkni;
	
	
	//PD Impact
	
	@UiField RadioButton descriptpddt;
	@UiField RadioButton descriptpdit;
	@UiField RadioButton descriptpdir;
	@UiField RadioButton descriptpdni;
	@UiField RadioButton descriptpdie;
	@UiField RadioButton descriptpdde;
	

	//recommendation drug
	@UiField CheckBox descriptdsca;
	@UiField CheckBox descriptdsal;
	@UiField CheckBox descriptdsam;
	@UiField CheckBox descriptdsnr;
	@UiField CheckBox descriptdsnc;
	
	//recommendation dose
	@UiField CheckBox descriptdrdfb;
	@UiField CheckBox descriptdrifb;
	@UiField CheckBox descriptdrnc;
	@UiField CheckBox descriptdrus;
	@UiField CheckBox descriptdrcs;

	//recommendation monitoring
	@UiField CheckBox descriptmreq;
	@UiField CheckBox descriptmrec;
	@UiField CheckBox descriptmnc;
	@UiField CheckBox descriptmcms;
	
	
		
	//@UiField RadioButton Drug, Dose, Monitoring, test_Re;
	
	@UiField TextArea commentBody;
	
	public Set<MLinkedResource> getMethods() {
		Set<MLinkedResource> sioDescriptions = new HashSet<MLinkedResource>();
		
		//Pharmacokinetic impact  PK
		if(descriptpkia.getValue()) 
			sioDescriptions.add(ResourcesFactory.createTrustedTypedResource(
					SPL_POC_PREFIX + "absorption-increase", 
					"Absorption Increase", 
					"The pharmacogenomic biomarker is associated with a increase in absorption of the drug.", 
					SPL_POC_PREFIX + "PharmacokineticImpact", 
					SPL_POC_PREFIX, 
					"U of Pitt SPL Pharmgx Annotation"));

		if(descriptpkda.getValue()) 
			sioDescriptions.add(ResourcesFactory.createTrustedTypedResource(
					SPL_POC_PREFIX + "absorption-decrease", 
					"Absorption Decrease", 
					"The pharmacogenomic biomarker is associated with an decrease in absorption of the drug.", 
					SPL_POC_PREFIX + "PharmacokineticImpact", 
					SPL_POC_PREFIX, 
					"U of Pitt SPL Pharmgx Annotation"));
		
		
		if(descriptpkid.getValue()) 
			sioDescriptions.add(ResourcesFactory.createTrustedTypedResource(
					SPL_POC_PREFIX + "distribution-increase", 
					"Distribution Increase", 
					"The pharmacogenomic biomarker is associated with a increase in distribution of the drug.", 
					SPL_POC_PREFIX + "PharmacokineticImpact", 
					SPL_POC_PREFIX, 
					"U of Pitt SPL Pharmgx Annotation"));

		if(descriptpkdd.getValue()) 
			sioDescriptions.add(ResourcesFactory.createTrustedTypedResource(
					SPL_POC_PREFIX + "distribution-decrease", 
					"Distribution Increase", 
					"The pharmacogenomic biomarker is associated with an decrease in distribution of the drug.", 
					SPL_POC_PREFIX + "PharmacokineticImpact", 
					SPL_POC_PREFIX, 
					"U of Pitt SPL Pharmgx Annotation"));
		
		
		if(descriptpkie.getValue()) 
			sioDescriptions.add(ResourcesFactory.createTrustedTypedResource(
					SPL_POC_PREFIX + "excretion-increase", 
					"Excretion Increase", 
					"The pharmacogenomic biomarker is associated with a increase in excretion of the drug.", 
					SPL_POC_PREFIX + "PharmacokineticImpact", 
					SPL_POC_PREFIX, 
					"U of Pitt SPL Pharmgx Annotation"));
		
		if(descriptpkde.getValue()) 
			sioDescriptions.add(ResourcesFactory.createTrustedTypedResource(
					SPL_POC_PREFIX + "excretion-decrease", 
					"Excretion Decrease", 
					"The pharmacogenomic biomarker is associated with an decrease in excretion of the drug.", 
					SPL_POC_PREFIX + "PharmacokineticImpact", 
					SPL_POC_PREFIX, 
					"U of Pitt SPL Pharmgx Annotation"));

		
		if(descriptpkim.getValue()) 
			sioDescriptions.add(ResourcesFactory.createTrustedTypedResource(
					SPL_POC_PREFIX + "metabolism-increase", 
					"Metabolism Increase", 
					"The pharmacogenomic biomarker is associated with an increase in metabolism of the drug.", 
					SPL_POC_PREFIX + "PharmacokineticImpact", 
					SPL_POC_PREFIX, 
					"U of Pitt SPL Pharmgx Annotation"));
		
		if(descriptpkdm.getValue()) 
			sioDescriptions.add(ResourcesFactory.createTrustedTypedResource(
					SPL_POC_PREFIX + "metabolism-decrease", 
					"Metabolism Decrease", 
					"The pharmacogenomic biomarker is associated with a decrease in metabolism of the drug.", 
					SPL_POC_PREFIX + "PharmacokineticImpact", 
					SPL_POC_PREFIX, 
					"U of Pitt SPL Pharmgx Annotation"));
		
		if(descriptpkni.getValue()) 
			sioDescriptions.add(ResourcesFactory.createTrustedTypedResource(
					SPL_POC_PREFIX + "not-important", 
					"Not Important", 
					"The pharmacogenomic biomarker is not associated any clinically relevant pharmacokinetic with respect to the drug.", 
					SPL_POC_PREFIX + "PharmacokineticImpact", 
					SPL_POC_PREFIX, 
					"U of Pitt SPL Pharmgx Annotation"));

		//Pharmacodynamic impact  PD
		
		if(descriptpddt.getValue()) 
			sioDescriptions.add(ResourcesFactory.createTrustedTypedResource(
					SPL_POC_PREFIX + "drug-toxicity-risk-decreased", 
					"Decreased Toxicity Risk", 
					"The pharmacogenomic biomarker is associated with an decreased risk of toxicity.", 
					SPL_POC_PREFIX + "PharmacodynamicImpact", 
					SPL_POC_PREFIX, 
					"U of Pitt SPL Pharmgx Annotation"));

		if(descriptpdit.getValue()) 
			sioDescriptions.add(ResourcesFactory.createTrustedTypedResource(
					SPL_POC_PREFIX + "drug-toxicity-risk-increased", 
					"Increased Toxicity Risk", 
					"The pharmacogenomic biomarker is associated with an increased risk of toxicity.", 
					SPL_POC_PREFIX + "PharmacodynamicImpact", 
					SPL_POC_PREFIX, 
					"U of Pitt SPL Pharmgx Annotation"));

		if(descriptpdir.getValue()) 
			sioDescriptions.add(ResourcesFactory.createTrustedTypedResource(
					SPL_POC_PREFIX + "influences-drug-response", 
					"Influences Drug Response", 
					"The pharmacogenomic biomarker influences drug response", 
					SPL_POC_PREFIX + "PharmacodynamicImpact", 
					SPL_POC_PREFIX, 
					"U of Pitt SPL Pharmgx Annotation"));

		if(descriptpdni.getValue()) 
			sioDescriptions.add(ResourcesFactory.createTrustedTypedResource(
					SPL_POC_PREFIX + "not-important", 
					"Not Important", 
					"The pharmacogenomic biomarker is not associated with clinically relevant pharmacodynamic effect", 
					SPL_POC_PREFIX + "PharmacodynamicImpact", 
					SPL_POC_PREFIX, 
					"U of Pitt SPL Pharmgx Annotation"));
		
		if(descriptpdie.getValue()) 
			sioDescriptions.add(ResourcesFactory.createTrustedTypedResource(
					SPL_POC_PREFIX + "drug-efficacy-increased-from-baseline", 
					"Increased Efficacy", 
					"The pharmacogenomic biomarker is associated with an increase in the efficacy of the drug. ", 
					SPL_POC_PREFIX + "PharmacodynamicImpact", 
					SPL_POC_PREFIX, 
					"U of Pitt SPL Pharmgx Annotation"));

		if(descriptpdde.getValue()) 
			sioDescriptions.add(ResourcesFactory.createTrustedTypedResource(
					SPL_POC_PREFIX + "drug-efficacy-decreased-from-baseline", 
					"Decreased Efficacy", 
					"The pharmacogenomic biomarker is associated with an decrease in the efficacy of the drug.", 
					SPL_POC_PREFIX + "PharmacodynamicImpact", 
					SPL_POC_PREFIX, 
					"U of Pitt SPL Pharmgx Annotation"));

		
		//Recommendation drug

		/*
		if(descriptdsal.getValue()) 
			sioDescriptions.add(ResourcesFactory.createTrustedTypedResource(
					SPL_POC_PREFIX + "alternative", 
					"Alternative", 
					"The pharmacogenomic biomarker is related to a recommendation to alternates the drug from the recommended baseline.", 
					SPL_POC_PREFIX + "DrugRecommendation", 
					SPL_POC_PREFIX, 
					"U of Pitt SPL Pharmgx Annotation"));

		if(descriptdsca.getValue()) 
			sioDescriptions.add(ResourcesFactory.createTrustedTypedResource(
					SPL_POC_PREFIX + "change-administration", 
					"Change Administration", 
					"The pharmacogenomic biomarker is related to a recommendation to increase the dose of the drug from the recommended baseline.", 
					SPL_POC_PREFIX + "DrugRecommendation", 
					SPL_POC_PREFIX, 
					"U of Pitt SPL Pharmgx Annotation"));

		if(descriptdsam.getValue()) 
			sioDescriptions.add(ResourcesFactory.createTrustedTypedResource(
					SPL_POC_PREFIX + "add-medication", 
					"Add medication", 
					"The pharmacogenomic biomarker is related to a recommendation to not change the dose of the drug from the recommended baseline.", 
					SPL_POC_PREFIX + "DoseSelectionRecommendation", 
					SPL_POC_PREFIX, 
					"U of Pitt SPL Pharmgx Annotation"));

		if(descriptdsnr.getValue()) 
			sioDescriptions.add(ResourcesFactory.createTrustedTypedResource(
					SPL_POC_PREFIX + "not-restart", 
					"Not-restart", 
					"The pharmacogenomic biomarker is related to a recommendation to use specific dose of the drug from the recommended baseline.", 
					SPL_POC_PREFIX + "DoseSelectionRecommendation", 
					SPL_POC_PREFIX, 
					"U of Pitt SPL Pharmgx Annotation"));
		
		if(descriptdsnc.getValue()) 
			sioDescriptions.add(ResourcesFactory.createTrustedTypedResource(
					SPL_POC_PREFIX + "Not change", 
					"Not change", 
					"The pharmacogenomic biomarker is related to a recommendation to change schedule of the dose of the drug from the recommended baseline.", 
					SPL_POC_PREFIX + "DoseSelectionRecommendation", 
					SPL_POC_PREFIX, 
					"U of Pitt SPL Pharmgx Annotation"));
		
		*/
		
		//Recommendation Dose
		
		if(descriptdrdfb.getValue()) 
			sioDescriptions.add(ResourcesFactory.createTrustedTypedResource(
					SPL_POC_PREFIX + "decrease-from-recommended-baseline", 
					"Decrease from baseline", 
					"The pharmacogenomic biomarker is related to a recommendation to decrease the dose of the drug from the recommended baseline.", 
					SPL_POC_PREFIX + "DoseSelectionRecommendation", 
					SPL_POC_PREFIX, 
					"U of Pitt SPL Pharmgx Annotation"));

		if(descriptdrifb.getValue()) 
			sioDescriptions.add(ResourcesFactory.createTrustedTypedResource(
					SPL_POC_PREFIX + "increase-from-recommended-baseline", 
					"Increase from baseline", 
					"The pharmacogenomic biomarker is related to a recommendation to increase the dose of the drug from the recommended baseline.", 
					SPL_POC_PREFIX + "DoseSelectionRecommendation", 
					SPL_POC_PREFIX, 
					"U of Pitt SPL Pharmgx Annotation"));

		if(descriptdrnc.getValue()) 
			sioDescriptions.add(ResourcesFactory.createTrustedTypedResource(
					SPL_POC_PREFIX + "not-change-from-recommended baseline", 
					"Not change from baseline", 
					"The pharmacogenomic biomarker is related to a recommendation to not change the dose of the drug from the recommended baseline.", 
					SPL_POC_PREFIX + "DoseSelectionRecommendation", 
					SPL_POC_PREFIX, 
					"U of Pitt SPL Pharmgx Annotation"));

		if(descriptdrus.getValue()) 
			sioDescriptions.add(ResourcesFactory.createTrustedTypedResource(
					SPL_POC_PREFIX + "use-specific", 
					"Use specific", 
					"The pharmacogenomic biomarker is related to a recommendation to use specific dose of the drug from the recommended baseline.", 
					SPL_POC_PREFIX + "DoseSelectionRecommendation", 
					SPL_POC_PREFIX, 
					"U of Pitt SPL Pharmgx Annotation"));
		
		if(descriptdrcs.getValue()) 
			sioDescriptions.add(ResourcesFactory.createTrustedTypedResource(
					SPL_POC_PREFIX + "change-schedule", 
					"Change schedule", 
					"The pharmacogenomic biomarker is related to a recommendation to change schedule of the dose of the drug from the recommended baseline.", 
					SPL_POC_PREFIX + "DoseSelectionRecommendation", 
					SPL_POC_PREFIX, 
					"U of Pitt SPL Pharmgx Annotation"));
		
		
		
		//Recommendation Monitoring
		
		if(descriptmreq.getValue()) 
			sioDescriptions.add(ResourcesFactory.createTrustedTypedResource(
					SPL_POC_PREFIX + "required", 
					"Required", 
					"A required monitoring recommendation is related to the pharmacogenomic biomarker.", 
					SPL_POC_PREFIX + "MonitoringRecommendation", 
					SPL_POC_PREFIX, 
					"U of Pitt SPL Pharmgx Annotation"));

  	    if(descriptmrec.getValue()) 
		    sioDescriptions.add(ResourcesFactory.createTrustedTypedResource(
					SPL_POC_PREFIX + "recommended", 
					"Recommended", 
					"A recommended monitoring recommendation is related to the pharmacogenomic biomarker.", 
					SPL_POC_PREFIX + "MonitoringRecommendation", 
					SPL_POC_PREFIX, 
					"U of Pitt SPL Pharmgx Annotation"));
  	        
  	    if(descriptmnc.getValue()) 
  			sioDescriptions.add(ResourcesFactory.createTrustedTypedResource(
  					SPL_POC_PREFIX + "not-necessary", 
  					"Not necessary", 
  					"A not necessary monitoring recommendation is related to the pharmacogenomic biomarker.", 
  					SPL_POC_PREFIX + "MonitoringRecommendation", 
  					SPL_POC_PREFIX, 
  					"U of Pitt SPL Pharmgx Annotation"));

        if(descriptmcms.getValue()) 
  			sioDescriptions.add(ResourcesFactory.createTrustedTypedResource(
  					SPL_POC_PREFIX + "change-monitoring-strategy", 
  					"Change monitoring strategy", 
  					"A strategy changed monitoring recommendation is related to the pharmacogenomic biomarker.", 
  					SPL_POC_PREFIX + "MonitoringRecommendation", 
  					SPL_POC_PREFIX, 
  					"U of Pitt SPL Pharmgx Annotation"));

		return sioDescriptions;
	}
	
	
	public FSPLsForm(IDomeo domeo, final AFormsManager manager) {
		super(domeo);
		_manager = manager;
		
		initWidget(binder.createAndBindUi(this));
		
		refreshAnnotationSetFilter(annotationSet, null);

		ButtonWithIcon yesButton = new ButtonWithIcon(Domeo.resources.generalCss().applyButton());
		yesButton.setWidth("78px");
		yesButton.setHeight("22px");
		yesButton.setResource(Domeo.resources.acceptLittleIcon());
		yesButton.setText("Apply");
		yesButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(isContentInvalid()) return;
			
				try { 
					if(_item == null) {
						if(_manager instanceof TextAnnotationFormsPanel) {
							MTextQuoteSelector selector = AnnotationFactory.createPrefixSuffixTextSelector(
									_domeo.getAgentManager().getUserPerson(), 
									_domeo.getPersistenceManager().getCurrentResource(), ((TextAnnotationFormsPanel)_manager).getHighlight().getExact(), 
									((TextAnnotationFormsPanel)_manager).getHighlight().getPrefix(), ((TextAnnotationFormsPanel)_manager).getHighlight().getSuffix());
							
							
							// TODO Register coordinate of the selection.
							MSPLsAnnotation annotation = SPLsFactory.createSPLsAnnotation(
									((AnnotationPersistenceManager)_domeo.getPersistenceManager()).getCurrentSet(), 
									_domeo.getAgentManager().getUserPerson(), _domeo.getAgentManager().getSoftware(),
									_manager.getResource(), selector);
							// TODO Register coordinate of highlight.
							
							MSPLPharmgxUsage pharmgxUsage = SPLsFactory.createSPLPharmgxUsage();
							annotation.setPharmgxUsage(pharmgxUsage);
								
							_domeo.getLogger().command(getLogCategoryCreate(), this, " with term " + currentPharmgx.getLabel());
	
							// TODO: figure out how to set the subject up properly
							annotation.setSubject(ResourcesFactory.createTrustedTypedResource(SPL_POC_PREFIX + "TEST-SUBJECT", "TEST-SUBJECT", "A dummy subject to test the model", SPL_POC_PREFIX + "TEST", SPL_POC_PREFIX, "U of Pitt SPL Pharmgx Annotation"));
							annotation.setComment(commentBody.getText());
							annotation.getSioDescriptions().clear();
							annotation.addSioDescriptions(getMethods());
								
							if(getSelectedSet(annotationSet)==null) {
								_domeo.getAnnotationPersistenceManager().addAnnotation(annotation, true);
							} else {
								_domeo.getAnnotationPersistenceManager().addAnnotation(annotation, getSelectedSet(annotationSet));
							}
							_domeo.getContentPanel().getAnnotationFrameWrapper().performAnnotation(annotation, ((TextAnnotationFormsPanel)_manager).getHighlight());
							_manager.hideContainer();
							
						} 
					} else {
						//_item.setType(PostitType.findByName(postitTypes.getItemText(postitTypes.getSelectedIndex())));
						//_item.setText(getPostItBody());
					}
				} catch (Exception e) {
					_domeo.getLogger().exception(this, e.getMessage());
				}
			}
		});
		buttonsPanel.add(yesButton);
		
		this.setHeight("100px");

	   
		
		//rightColumn.add(tabs.get(0));
		//resized();
	}
	
	// ------------------------
	//  EDITING OF ANNOTATION
	// ------------------------
	public FSPLsForm(IDomeo domeo, final AFormsManager manager, final MSPLsAnnotation annotation) {
		super(domeo);
		_manager = manager;
		_item = annotation;
		
		initWidget(binder.createAndBindUi(this));
		
		try {
			if(_item.getComment()!=null) 
			    commentBody.setText(_item.getComment());

			if(_item.getSioDescriptions()!=null) {
				for(MLinkedResource descript:_item.getSioDescriptions()) {
					if(descript.getUrl().equals(SPL_POC_PREFIX +"metabolism-decrease"))
						descriptpkdm.setValue(true);
					if(descript.getUrl().equals(SPL_POC_PREFIX +"metabolism-increase"))
						descriptpkim.setValue(true);
					if(descript.getUrl().equals(SPL_POC_PREFIX +"drug-toxicity-risk-decreased"))
						descriptpddt.setValue(true);
					if(descript.getUrl().equals(SPL_POC_PREFIX +"drug-toxicity-risk-increased"))
						descriptpdit.setValue(true);
					if(descript.getUrl().equals(SPL_POC_PREFIX +"decrease-from-recommended-baseline"))
						descriptdrdfb.setValue(true);
					if(descript.getUrl().equals(SPL_POC_PREFIX +"increase-from-recommended-baseline"))
						descriptdrifb.setValue(true);
					if(descript.getUrl().equals(SPL_POC_PREFIX +"required"))
						descriptmreq.setValue(true);
					if(descript.getUrl().equals(SPL_POC_PREFIX +"recommended"))
						descriptmrec.setValue(true);
				}
			} 
		} catch(Exception e) {
			_domeo.getLogger().exception(this, "Failed to display current annotation " + annotation.getLocalId());
			displayDialog("Failed to properly display existing annotation " + e.getMessage(), true);
		}
		
		try {
			refreshAnnotationSetFilter(annotationSet, annotation);
			currentPharmgx = annotation.getPharmgxUsage().getPharmgx();
		} catch(Exception e) {
			_domeo.getLogger().exception(AnnotationFrameWrapper.LOG_CATEGORY_EDIT_ANNOTATION, this, "Failed to display current annotation " + annotation.getLocalId());
			displayDialog("Failed to properly display existing annotation " + e.getMessage(), true);
		}
		
		ButtonWithIcon sameVersionButton = new ButtonWithIcon();
		sameVersionButton.setStyleName(Domeo.resources.generalCss().applyButton());
		sameVersionButton.setWidth("78px");
		sameVersionButton.setHeight("22px");
		sameVersionButton.setResource(Domeo.resources.acceptLittleIcon());
		sameVersionButton.setText("Apply");
		sameVersionButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				try {
					if(isContentInvalid()) return;
					if(!isContentChanged(_item)) {
						_domeo.getLogger().debug(this, "No changes to save for annotation " + _item.getLocalId());
						_manager.getContainer().hide();
						return;
					}
					_item.setComment(commentBody.getText());
					_item.setSubject(ResourcesFactory.createTrustedTypedResource(SPL_POC_PREFIX + "TEST-SUBJECT", "TEST-SUBJECT", "A dummy subject to test the model", SPL_POC_PREFIX + "TEST", SPL_POC_PREFIX, "U of Pitt SPL Pharmgx Annotation"));
					_item.getSioDescriptions().clear();
					_item.addSioDescriptions(getMethods());
					_item.getPharmgxUsage().setPharmgx(currentPharmgx);
					
					_domeo.getContentPanel().getAnnotationFrameWrapper().updateAnnotation(_item, getSelectedSet(annotationSet));
					_manager.hideContainer();
				} catch(Exception e) {
					_domeo.getLogger().exception(AnnotationFrameWrapper.LOG_CATEGORY_EDIT_ANNOTATION, this, "Failed to apply modified anntoation " + annotation.getLocalId());
					displayDialog("Failed to apply modified annotation " + e.getMessage(), true);
				}
			}
		});
		buttonsPanel.add(sameVersionButton);
	
		this.setHeight("100px");
	    		
		//rightColumn.add(tabs.get(0));
		//resized();
	}
	
	public String getTitle() {
		return LABEL;
	}

	@Override
	public String getLogCategoryCreate() {
		return LOG_CATEGORY_QUALIFIER_CREATE;
	}

	@Override
	public String getLogCategoryEdit() {
		return LOG_CATEGORY_QUALIFIER_EDIT;
	}

	@Override
	public boolean isContentInvalid() {
		if(currentPharmgx==null) {
			_manager.displayMessage("The body of the annotation cannot be empty!");
			Timer timer = new Timer() {
				public void run() {
					_manager.clearMessage();
				}
			};
			timer.schedule(2000);
			return true;
		}
		return false;
	}
	
	
	public boolean areMethodsChanged() {
		if(_item.getSioDescriptions().size()!=getMethods().size()) return true;
		//TODO check items
		return false;
	}

	@Override
	public boolean isContentChanged(MAnnotation annotation) {
		// TODO just checking the size is not right.
		if(_item!=null) {
			if(!_item.getPharmgxUsage().getPharmgx().getUrl().equals(currentPharmgx.getUrl())
					|| !commentBody.getText().equals(_item.getPharmgxUsage().getComment())) {
				return true;
			}
		}	
		return false;
	}

	
	@Override
	public void resized() {
		this.setWidth((Window.getClientWidth() - 340) + "px");
		tabBar.setWidth((Window.getClientWidth() - 615) + "px");
		for(Widget tab:tabs) {
			//if(tab instanceof IResizable) ((IResizable)tab).resized();
			tab.setWidth((Window.getClientWidth() - 615) + "px");
		}
	}



	
	// @Override
	// public void addPharmgx(MPharmgx pharmgx) {
	// 	addAssociatedAntibody(pharmgx);
	// }
}
