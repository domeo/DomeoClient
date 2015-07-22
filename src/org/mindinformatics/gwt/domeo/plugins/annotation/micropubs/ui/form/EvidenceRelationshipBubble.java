package org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.ui.form;

import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.IMicroPublicationsOntology;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.MMicroPublication;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.MMpData;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.MMpElement;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.MMpReference;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.MMpRelationship;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.MMpStatement;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.ui.IMpResources;
import org.mindinformatics.gwt.framework.src.IApplication;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 * 
 * This pop-up allows to edit the relationship connecting a swan element
 * and a piece of evidence.
 */
public class EvidenceRelationshipBubble extends PopupPanel {
	
	//private static final String POPUP_CSS = "af-CurationPopup";
	
	public static final IMpResources resources = GWT.create(IMpResources.class);
	
	private static final String GROUP = "EvidenceRelationshipGroup";
	
	private RadioButton supportiveEvidenceRadioButton;
	//private RadioButton relevantEvidenceRadioButton;
	private RadioButton inconsistentEvidenceRadioButton;
	
	public EvidenceRelationshipBubble(final IEvidenceRelationshipChangeListener changeListener, 
			final IApplication annotator, final MMicroPublication item, final MMpRelationship evidence, final String originalType) {
		super(true); // auto-hide on
		resources.pluginCss().ensureInjected();
		this.setAnimationEnabled(true);
		this.setStyleName(resources.pluginCss().curationPopup());
//		
//		final ReferenceDTO reference = evidence.getReference();
//		SwanVocabularyProvider swanVocabularyProvider = SwanVocabularyProvider.instance();
//
	
//		Window.alert(evidence.getName());
		
		supportiveEvidenceRadioButton = new RadioButton(GROUP, IMicroPublicationsOntology.mpSupportedBy);
		if(evidence.getName().equals(IMicroPublicationsOntology.mpSupportedBy)) {
			supportiveEvidenceRadioButton.setValue(true);
		}
		supportiveEvidenceRadioButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(supportiveEvidenceRadioButton.getValue()) {
					final MMpElement element = evidence.getObjectElement();
					if(element instanceof MMpReference) {
						changeListener.updateEvidence(item, evidence, originalType, IMicroPublicationsOntology.mpSupportedBy);
						hide();
					} else if(element instanceof MMpData) {
						changeListener.updateEvidence(item, evidence, originalType, IMicroPublicationsOntology.mpSupportedBy);
						hide();
					} else if(element instanceof MMpStatement) {
						changeListener.updateEvidence(item, evidence, originalType, IMicroPublicationsOntology.mpSupportedBy);
						hide();
					}
				}			
			}
		});
		HorizontalPanel supportivePanel = new HorizontalPanel();
		supportivePanel.add(new Image(resources.mpSupportiveIcon()));
		supportivePanel.add(supportiveEvidenceRadioButton);
		
		inconsistentEvidenceRadioButton = new RadioButton(GROUP, IMicroPublicationsOntology.mpChallengedBy);
		if(evidence.getName().equals(IMicroPublicationsOntology.mpChallengedBy)) {
			inconsistentEvidenceRadioButton.setValue(true);
		}
		inconsistentEvidenceRadioButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(inconsistentEvidenceRadioButton.getValue()) {
					final MMpElement element = evidence.getObjectElement();
					if(element instanceof MMpReference) {
						changeListener.updateEvidence(item, evidence, originalType, IMicroPublicationsOntology.mpChallengedBy);
						hide();
					} else if(element instanceof MMpData) {
						changeListener.updateEvidence(item, evidence, originalType, IMicroPublicationsOntology.mpChallengedBy);
						hide();
					} else if(element instanceof MMpStatement) {
						changeListener.updateEvidence(item, evidence, originalType, IMicroPublicationsOntology.mpChallengedBy);
						hide();
					}
				}			
			}
		});
		HorizontalPanel inconsistentPanel = new HorizontalPanel();
		inconsistentPanel.add(new Image(resources.mpChallengingIcon()));
		inconsistentPanel.add(inconsistentEvidenceRadioButton);

		VerticalPanel hp1 = new VerticalPanel();
		hp1.add(supportivePanel);
		hp1.add(inconsistentPanel);
		
//		if(originalType.equals(DISCOURSE_RELATIONSHIPS.referencesAsSupportiveEvidence)) 
//			supportiveEvidenceRadioButton.setValue(true);
//		
//		HorizontalPanel hp1 = new HorizontalPanel();
//		
//		if(reference instanceof PublicationDTO) {
//			hp1.add(new Image(SwanVocabularyProvider.instance().REFERENCES_AS_SUPPORTIVE_EVIDENCE.getIcon()));
//		} else if(reference instanceof DataCitationDTO) {
//			hp1.add(swanVocabularyProvider.getIconWithTitle(DISCOURSE_RELATIONSHIPS.referencesAsSupportiveEvidence, reference));
//		}
//		hp1.add(supportiveEvidenceRadioButton);
//
//		relevantEvidenceRadioButton = new RadioButton(GROUP, SwanVocabularyProvider.instance().REFERENCES_AS_RELEVANT_EVIDENCE.getShortLabel());
//		relevantEvidenceRadioButton.addClickHandler(new ClickHandler() {
//			@Override
//			public void onClick(ClickEvent event) {
//				final ReferenceDTO reference = evidence.getReference();
//				if(reference instanceof PublicationDTO) {
//					changeListener.updateEvidence(element, evidence, originalType, DISCOURSE_RELATIONSHIPS.referencesAsRelevantEvidence);
//					hide();
//				} else if(reference instanceof DataCitationDTO) {
//					changeListener.updateEvidence(element, evidence, originalType,  DISCOURSE_RELATIONSHIPS.referencesAsRelevantEvidence);
//					hide();
//				}	
//			}	
//		});
//		
//		if(originalType.equals(DISCOURSE_RELATIONSHIPS.referencesAsRelevantEvidence)) 
//			relevantEvidenceRadioButton.setValue(true);
//		
//		HorizontalPanel hp2 = new HorizontalPanel();
//		
//		if(reference instanceof PublicationDTO) {
//			hp2.add(new Image(SwanVocabularyProvider.instance().REFERENCES_AS_RELEVANT_EVIDENCE.getIcon()));
//		} else if(reference instanceof DataCitationDTO) {
//			hp2.add(swanVocabularyProvider.getIconWithTitle(DISCOURSE_RELATIONSHIPS.referencesAsRelevantEvidence, reference));
//		}
//		hp2.add(relevantEvidenceRadioButton);
//
//		inconsistentEvidenceRadioButton = new RadioButton(GROUP, SwanVocabularyProvider.instance().REFERENCES_AS_INCONSISTENT_EVIDENCE.getShortLabel());
//		inconsistentEvidenceRadioButton.addClickHandler(new ClickHandler() {
//			@Override
//			public void onClick(ClickEvent event) {
//				if(inconsistentEvidenceRadioButton.getValue()) {
//					final ReferenceDTO reference = evidence.getReference();
//					if(reference instanceof PublicationDTO) {
//						changeListener.updateEvidence(element, evidence, originalType, DISCOURSE_RELATIONSHIPS.referencesAsInconsistentEvidence);
//						hide();
//					} else if(reference instanceof DataCitationDTO) {
//						changeListener.updateEvidence(element, evidence, originalType,  DISCOURSE_RELATIONSHIPS.referencesAsInconsistentEvidence);
//						hide();
//					}
//				}			
//			}	
//		});
//		
//		if(originalType.equals(DISCOURSE_RELATIONSHIPS.referencesAsInconsistentEvidence)) 
//			inconsistentEvidenceRadioButton.setValue(true);
//		
//		HorizontalPanel hp3 = new HorizontalPanel();
//		if(reference instanceof PublicationDTO) {
//			hp3.add(new Image(SwanVocabularyProvider.instance().REFERENCES_AS_INCONSISTENT_EVIDENCE.getIcon()));
//		} else if(reference instanceof DataCitationDTO) {
//			hp3.add(swanVocabularyProvider.getIconWithTitle(DISCOURSE_RELATIONSHIPS.referencesAsInconsistentEvidence, reference));
//		}
//		hp3.add(inconsistentEvidenceRadioButton);
//		
		VerticalPanel vp = new VerticalPanel();
		vp.add(hp1);
//		vp.add(hp2);
//		vp.add(hp3);

		setWidget(vp);
	}
	
	public void show(int x, int y) {
		this.setPopupPosition(x, y);
		this.show();
	}
}
