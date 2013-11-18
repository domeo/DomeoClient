package org.mindinformatics.gwt.domeo.client.ui.annotation.cards;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.mindinformatics.gwt.domeo.client.Domeo;
import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.Resources;
import org.mindinformatics.gwt.domeo.client.ui.annotation.actions.ActionCommentAnnotation;
import org.mindinformatics.gwt.domeo.client.ui.annotation.actions.ActionDeleteAnnotation;
import org.mindinformatics.gwt.domeo.client.ui.annotation.actions.ActionEditAnnotation;
import org.mindinformatics.gwt.domeo.client.ui.annotation.tiles.ATileComponent;
import org.mindinformatics.gwt.domeo.client.ui.annotation.tiles.TileResources;
import org.mindinformatics.gwt.domeo.client.ui.popup.CurationPopup;
import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.model.MAnnotationSet;
import org.mindinformatics.gwt.domeo.model.MOnlineImage;
import org.mindinformatics.gwt.domeo.model.selectors.SelectorUtils;
import org.mindinformatics.gwt.domeo.plugins.annotation.curation.model.CurationFactory;
import org.mindinformatics.gwt.domeo.plugins.annotation.curation.model.MCurationToken;
import org.mindinformatics.gwt.domeo.plugins.annotation.curation.src.CurationUtils;
import org.mindinformatics.gwt.domeo.plugins.annotation.highlight.model.MHighlightAnnotation;
import org.mindinformatics.gwt.framework.component.preferences.src.BooleanPreference;
import org.mindinformatics.gwt.framework.component.styles.src.StylesManager;
import org.mindinformatics.gwt.framework.widget.CustomButton;
import org.mindinformatics.gwt.framework.widget.WidgetUtilsResources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public abstract class ACardComponent extends Composite implements ICardComponent {

	// By contract 
	protected IDomeo _domeo;
	protected CurationPopup _curationPopup;
	protected Element _span;
	
	public static final TileResources tileResources = GWT.create(TileResources.class);
	
	public static final WidgetUtilsResources widgetUtilsResources = 
			GWT.create(WidgetUtilsResources.class);
	
	public ACardComponent(IDomeo domeo) {
		_domeo = domeo;
	}
	
	public abstract MAnnotation getAnnotation();
	
	public void injectButtons(String plugin, FlowPanel content, final MAnnotation annotation) {
		try {
			if(!((BooleanPreference)_domeo.getPreferences().getPreferenceItem(Domeo.class.getName(), Domeo.PREF_DISPLAY_PROVENANCE)).getValue() ||
					!(((BooleanPreference)_domeo.getPreferences().getPreferenceItem(Domeo.class.getName(), Domeo.PREF_DISPLAY_USER_PROVENANCE)).getValue())) {
				
				Resources resource = Domeo.resources;
				
				Image commentIcon = null;
				if(((BooleanPreference)_domeo.getPreferences().getPreferenceItem(Domeo.class.getName(), 
						Domeo.PREF_ALLOW_COMMENTING)).getValue()) { 
					commentIcon = new Image(resource.littleCommentIcon());
					commentIcon.setTitle("Comment on Item");
					commentIcon.setStyleName(ATileComponent.tileResources.css().button());
					commentIcon.addClickHandler(ActionCommentAnnotation.getClickHandler(_domeo, this, annotation));
					commentIcon.addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							_curationPopup.hide();
						}
					});
				}
				
				Image editIcon = new Image(resource.editLittleIcon());
				editIcon.setTitle("Edit Item");
				if(_domeo.getProfileManager().getUserCurrentProfile().isPluginEnabled(plugin)) {
					editIcon.setStyleName(ATileComponent.tileResources.css().button());
					editIcon.addClickHandler(ActionEditAnnotation.getClickHandler(_domeo, this, annotation));
					editIcon.addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							_curationPopup.hide();
						}
					});
				}
	
				Image deleteIcon = new Image(resource.deleteLittleIcon());
				deleteIcon.setTitle("Delete Item");
				deleteIcon.setStyleName(ATileComponent.tileResources.css().button());
				deleteIcon.addClickHandler(ActionDeleteAnnotation.getClickHandler(_domeo, this, annotation));
				deleteIcon.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						_curationPopup.hide();
					}
				});
				
				if(((BooleanPreference)_domeo.getPreferences().getPreferenceItem(Domeo.class.getName(), 
						Domeo.PREF_ALLOW_COMMENTING)).getValue()) { 
					content.add(commentIcon);
				}
				
				content.add(editIcon);
				content.add(deleteIcon);
			}
		} catch (Exception e) {
			_domeo.getLogger().exception(this,"injectButtons: " + e.getMessage());
		}
	}
	
	public void createProvenanceBar(String plugin, int index, HorizontalPanel provenance, final MAnnotation annotation) {
		try {
			Resources resource = Domeo.resources;

			Image commentIcon = null;
			if(((BooleanPreference)_domeo.getPreferences().getPreferenceItem(Domeo.class.getName(), 
					Domeo.PREF_ALLOW_COMMENTING)).getValue()) { 
			    commentIcon = new Image(resource.littleCommentIcon());
				commentIcon.setTitle("Comment on Item");
				commentIcon.setStyleName(ATileComponent.tileResources.css().button());
				commentIcon.addClickHandler(ActionCommentAnnotation.getClickHandler(_domeo, this, annotation));
				commentIcon.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						_curationPopup.hide();
					}
				});
			}
			
			Image editIcon = new Image(resource.editLittleIcon());
			editIcon.setTitle("Edit Item");
			if(_domeo.getProfileManager().getUserCurrentProfile().isPluginEnabled(plugin)) {
				editIcon.setStyleName(ATileComponent.tileResources.css().button());
				editIcon.addClickHandler(ActionEditAnnotation.getClickHandler(_domeo, this, annotation));
				editIcon.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						_curationPopup.hide();
					}
				});
			}

			Image deleteIcon = new Image(resource.deleteLittleIcon());
			deleteIcon.setTitle("Delete Item");
			deleteIcon.setStyleName(ATileComponent.tileResources.css().button());
			deleteIcon.addClickHandler(ActionDeleteAnnotation.getClickHandler(_domeo, this, annotation));
			deleteIcon.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					_curationPopup.hide();
				}
			});
			
			// TODO move to an abstract tile class
			// TODO Optimize: code dupication below
			if(((BooleanPreference)_domeo.getPreferences().getPreferenceItem(Domeo.class.getName(), Domeo.PREF_DISPLAY_PROVENANCE)).getValue()) {
				if(annotation.getCreator().getUri().equals(_domeo.getAgentManager().getUserPerson().getUri())) {
					if(((BooleanPreference)_domeo.getPreferences().getPreferenceItem(Domeo.class.getName(), Domeo.PREF_DISPLAY_USER_PROVENANCE)).getValue()) {
						provenance.clear();
	
						// TODO Externalize the icon management to the plugins
						if(SelectorUtils.isOnMultipleTargets(annotation.getSelectors())) { 
							Image ic = new Image(Domeo.resources.domeoAnnotateIcon());
							ic.setTitle("Annotation on multiple targets");
							provenance.add(ic);
							provenance.setCellWidth(ic, "18px");
						} else if(annotation.getSelector()!=null && annotation.getSelector().getTarget() instanceof MOnlineImage) {
							Image ic = new Image(Domeo.resources.littleImageIcon());
							ic.setTitle("Annotation on image");
							provenance.add(ic);
							provenance.setCellWidth(ic, "18px");
						} else {
							Image ic = new Image(Domeo.resources.littleTextIcon());
							ic.setTitle("Annotation on text");
							provenance.add(ic);
							provenance.setCellWidth(ic, "18px");
						}

						provenance.add(new Label("By Me on " + annotation.getFormattedCreationDate()));
						if(((BooleanPreference)_domeo.getPreferences().getPreferenceItem(Domeo.class.getName(), 
								Domeo.PREF_ALLOW_COMMENTING)).getValue()) provenance.add(commentIcon);
						provenance.add(editIcon);
						provenance.setCellHorizontalAlignment(editIcon, HasHorizontalAlignment.ALIGN_CENTER);
						provenance.setCellWidth(editIcon, "22px");
						provenance.add(deleteIcon);
						provenance.setCellHorizontalAlignment(deleteIcon, HasHorizontalAlignment.ALIGN_CENTER);
						provenance.setCellWidth(deleteIcon, "22px");
					} else {
						provenance.setVisible(false);
					}
				} else {
					provenance.clear();

					// TODO Externalize the icon management to the plugins
					if(SelectorUtils.isOnMultipleTargets(annotation.getSelectors())) { 
						Image ic = new Image(Domeo.resources.domeoAnnotateIcon());
						ic.setTitle("Annotation on multiple targets");
						provenance.add(ic);
						provenance.setCellWidth(ic, "18px");
					} else if(annotation.getSelector()!=null && annotation.getSelector().getTarget() instanceof MOnlineImage) {
						Image ic = new Image(Domeo.resources.littleImageIcon());
						ic.setTitle("Annotation on image");
						provenance.add(ic);
						provenance.setCellWidth(ic, "18px");
					} else {
						Image ic = new Image(Domeo.resources.littleTextIcon());
						ic.setTitle("Annotation on text");
						provenance.add(ic);
						provenance.setCellWidth(ic, "18px");
					}

					provenance.add(new Label("By " + annotation.getCreator().getName() + " on " + annotation.getFormattedCreationDate()));
					if(((BooleanPreference)_domeo.getPreferences().getPreferenceItem(Domeo.class.getName(), 
							Domeo.PREF_ALLOW_COMMENTING)).getValue()) provenance.add(commentIcon);
					provenance.add(editIcon);
					provenance.add(deleteIcon);
				}
			} else {
				provenance.setVisible(false);
			}
		} catch (Exception e) {
			_domeo.getLogger().exception(this, "createProvenanceBar1: " + e.getMessage());
		}
	}
	
	public void createProvenanceBar(String plugin, HorizontalPanel provenance, final MAnnotation annotation) {
		try {
			Resources resource = Domeo.resources;
			
			Image commentIcon = null;
			if(((BooleanPreference)_domeo.getPreferences().getPreferenceItem(Domeo.class.getName(), 
					Domeo.PREF_ALLOW_COMMENTING)).getValue()) {
				commentIcon = new Image(resource.littleCommentIcon());
				commentIcon.setTitle("Comment on Item");
				commentIcon.setStyleName(ATileComponent.tileResources.css().button());
				commentIcon.addClickHandler(ActionCommentAnnotation.getClickHandler(_domeo, this, annotation));
				commentIcon.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						_curationPopup.hide();
					}
				});
			}
			
			Image editIcon = new Image(resource.editLittleIcon());
			editIcon.setTitle("Edit Item");
			if(_domeo.getProfileManager().getUserCurrentProfile().isPluginEnabled(plugin)) {
				editIcon.setStyleName(ATileComponent.tileResources.css().button());
				editIcon.addClickHandler(ActionEditAnnotation.getClickHandler(_domeo, this, annotation));
				editIcon.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						_curationPopup.hide();
					}
				});
			}

			Image deleteIcon = new Image(resource.deleteLittleIcon());
			deleteIcon.setTitle("Delete Item");
			deleteIcon.setStyleName(ATileComponent.tileResources.css().button());
			deleteIcon.addClickHandler(ActionDeleteAnnotation.getClickHandler(_domeo, this, annotation));
			deleteIcon.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					_curationPopup.hide();
				}
			});

			// TODO move to an abstract tile class
			if(((BooleanPreference)_domeo.getPreferences().getPreferenceItem(Domeo.class.getName(), Domeo.PREF_DISPLAY_PROVENANCE)).getValue()) {
				if(annotation.getCreator().getUri().equals(_domeo.getAgentManager().getUserPerson().getUri())) {
					if(((BooleanPreference)_domeo.getPreferences().getPreferenceItem(Domeo.class.getName(), Domeo.PREF_DISPLAY_USER_PROVENANCE)).getValue()) {
						provenance.clear();

						// TODO Externalize the icon management to the plugins
						if(SelectorUtils.isOnMultipleTargets(annotation.getSelectors())) { 
							Image ic = new Image(Domeo.resources.multipleLittleIcon());
							ic.setTitle("Annotation on multiple targets");
							provenance.add(ic);
							provenance.setCellWidth(ic, "18px");
						} else if(annotation.getSelector()!=null && annotation.getSelector().getTarget() instanceof MOnlineImage) {
							Image ic = new Image(Domeo.resources.littleImageIcon());
							ic.setTitle("Annotation on image");
							provenance.add(ic);
							provenance.setCellWidth(ic, "18px");
						} else {
							Image ic = new Image(Domeo.resources.littleTextIcon());
							ic.setTitle("Annotation on text");
							provenance.add(ic);
							provenance.setCellWidth(ic, "18px");
						}

						provenance.add(new Label("By Me on " + annotation.getFormattedCreationDate()));
						
						if(((BooleanPreference)_domeo.getPreferences().getPreferenceItem(Domeo.class.getName(), 
								Domeo.PREF_ALLOW_COMMENTING)).getValue()) {
							provenance.add(commentIcon);
							provenance.setCellWidth(commentIcon, "20px");
							provenance.setCellHorizontalAlignment(commentIcon, HasHorizontalAlignment.ALIGN_RIGHT);
							
						}
						
						if(SelectorUtils.isOnMultipleTargets(annotation.getSelectors()) || !(annotation instanceof MHighlightAnnotation)) {
							provenance.add(editIcon);
							provenance.setCellWidth(editIcon, "20px");
							provenance.setCellHorizontalAlignment(editIcon, HasHorizontalAlignment.ALIGN_RIGHT);
						}
						provenance.add(deleteIcon);
						provenance.setCellWidth(deleteIcon, "20px");
						provenance.setCellHorizontalAlignment(deleteIcon, HasHorizontalAlignment.ALIGN_RIGHT);
					} else {
						provenance.setVisible(false);
					}
				} else {
					provenance.clear();

					// TODO Externalize the icon management to the plugins
					if(SelectorUtils.isOnMultipleTargets(annotation.getSelectors())) { 
						Image ic = new Image(Domeo.resources.multipleLittleIcon());
						ic.setTitle("Annotation on multiple targets");
						provenance.add(ic);
						provenance.setCellWidth(ic, "18px");
					} else if(annotation.getSelector()!=null && annotation.getSelector().getTarget() instanceof MOnlineImage) {
						Image ic = new Image(Domeo.resources.littleImageIcon());
						ic.setTitle("Annotation on image");
						provenance.add(ic);
						provenance.setCellWidth(ic, "18px");
					} else {
						Image ic = new Image(Domeo.resources.littleTextIcon());
						ic.setTitle("Annotation on text");
						provenance.add(ic);
						provenance.setCellWidth(ic, "18px");
					}

					provenance.add(new Label("By " + annotation.getCreator().getName() + " on " + annotation.getFormattedCreationDate()));
					
					if(((BooleanPreference)_domeo.getPreferences().getPreferenceItem(Domeo.class.getName(), 
							Domeo.PREF_ALLOW_COMMENTING)).getValue()) {
						provenance.add(commentIcon);
						provenance.setCellWidth(commentIcon, "20px");
						provenance.setCellHorizontalAlignment(commentIcon, HasHorizontalAlignment.ALIGN_RIGHT);
					}
					
					provenance.add(editIcon);
					provenance.setCellWidth(editIcon, "20px");
					provenance.setCellHorizontalAlignment(editIcon, HasHorizontalAlignment.ALIGN_RIGHT);
					provenance.add(deleteIcon);
					provenance.setCellWidth(deleteIcon, "20px");
					provenance.setCellHorizontalAlignment(deleteIcon, HasHorizontalAlignment.ALIGN_RIGHT);
				}
			} else {
				provenance.setVisible(false);
			}
		} catch (Exception e) {
			_domeo.getLogger().exception(this, "createProvenanceBar2" +  e.getMessage());
		}
	}
	
	public void createSocialBar(HorizontalPanel socialBar, final MAnnotation annotation) {
		try {			
			socialBar.clear();
			
			if(((BooleanPreference)_domeo.getPreferences().getPreferenceItem(Domeo.class.getName(), 
					Domeo.PREF_ALLOW_COMMENTING)).getValue()) {
				Resources resource = Domeo.resources;
				
				int counter = _domeo.getAnnotationPersistenceManager().getCommentsOnAnnotationCounter(annotation);
				if(counter>0) {
					Image commentIcon = new Image(resource.littleCommentIcon());
					commentIcon.setTitle("Comment on Item");
					commentIcon.setStyleName(ATileComponent.tileResources.css().button());
					commentIcon.addClickHandler(ActionCommentAnnotation.getClickHandler(_domeo, this, annotation));
					commentIcon.addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							_curationPopup.hide();
						}
					});
					socialBar.add(commentIcon);
					socialBar.add(new Label("(" + counter + ")"));
				}
				
				/*
				Image usersIcon = new Image(resource.usersIcon());
				usersIcon.setTitle("Involved users");
				usersIcon.setStyleName(ATileComponent.tileResources.css().button());
				usersIcon.addClickHandler(ActionCommentAnnotation.getClickHandler(_domeo, this, annotation));
				usersIcon.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						_curationPopup.hide();
					}
				});
				socialBar.add(usersIcon);
				socialBar.setCellWidth(usersIcon, "24px");
				socialBar.setCellHorizontalAlignment(usersIcon, HasHorizontalAlignment.ALIGN_RIGHT);
				socialBar.add(new Label("(2)"));
				*/
			}
		} catch (Exception e) {
			_domeo.getLogger().exception(this, "create social bar" +  e.getMessage());
		}
	}
	
	private void modifyCurationToken(final MAnnotation annotation, String status) {
	    MAnnotationSet set = _domeo.getAnnotationPersistenceManager().getSetByAnnotationId(annotation.getLocalId());
	    
	    // Check if there is already a curation token
	    MAnnotation alreadyExisting = null;
	    for(MAnnotation token: annotation.getAnnotatedBy()) {
	        if(token instanceof MCurationToken && 
	                token.getCreator().getUri().equals(_domeo.getAgentManager().getUserPerson().getUri())) {
	            alreadyExisting = token;
	        }
	    }
	    if(alreadyExisting!=null) {
	        annotation.getAnnotatedBy().remove(alreadyExisting);
	        _domeo.getPersistenceManager().removeAnnotation(alreadyExisting, false); 
	    }
        
        MCurationToken ann = CurationFactory.createCurationToken(_domeo, 
            _domeo.getAgentManager().getUserPerson(),
            _domeo.getAgentManager().getSoftware(), 
            annotation, status, "");
        
        _domeo.getAnnotationPersistenceManager().addAnnotationOfAnnotation(ann, annotation, _domeo.getAnnotationPersistenceManager().getCurrentSet());
        _domeo.refreshAnnotationComponents();   
	}
	
	private Map<String, Integer> countCurationToken(final MAnnotation annotation) {
	    Map<String, Integer> tokens = new HashMap<String, Integer>();
	    for(MAnnotation token: annotation.getAnnotatedBy()) {
            if(token instanceof MCurationToken
                    && !token.getCreator().getUri().equals(_domeo.getAgentManager().getUserPerson().getUri())) {
                String status = ((MCurationToken)token).getStatus();
                if(tokens.containsKey(status)) {
                    Integer count = tokens.get(status);
                    count++;
                    tokens.put(status, count);
                } else {
                    tokens.put(status, 1);
                }
            }
        }
	    return tokens;
	}
	
	public void createCurationBar(HorizontalPanel curationBar, final MAnnotation annotation) {
		try {
			curationBar.clear();
			
			String myToken = CurationUtils.countCurationTokens(_domeo, annotation);
			
			if(((BooleanPreference)_domeo.getPreferences().getPreferenceItem(Domeo.class.getName(), 
					Domeo.PREF_ALLOW_CURATION)).getValue()) {
				Resources resource = Domeo.resources;
				
				HorizontalPanel curationPanel = new HorizontalPanel();
				
				int maxCount=0;
				String maximum="";
				Map<String, Integer> counts = countCurationToken(annotation);
				Set<String> keys = counts.keySet();
				for(String key: keys) {
				    if(counts.containsKey(key)) {
				        if(counts.get(key)>maxCount) {
				            maximum = key;
				            maxCount = counts.get(key);
				        }
				    }
				}
				
				final CustomButton correctButton = new CustomButton();
				final CustomButton broadButton = new CustomButton();
				final CustomButton incorrectButton = new CustomButton();
				
				if(counts.containsKey(MCurationToken.CORRECT)) {
				    correctButton.setText("Correct [" + counts.get(MCurationToken.CORRECT)+ "]");
				} else correctButton.setText("Correct");
				
				
				if(maximum.equals(MCurationToken.CORRECT)) {
				    if(myToken.equals(MCurationToken.CORRECT)) correctButton.setStyleName(widgetUtilsResources.widgetCss().curationButtonBoldGreen());
	                else correctButton.setStyleName(widgetUtilsResources.widgetCss().curationButtonBold());
				} else {
				    if(myToken.equals(MCurationToken.CORRECT)) correctButton.setStyleName(widgetUtilsResources.widgetCss().curationButtonGreen());
	                else correctButton.setStyleName(widgetUtilsResources.widgetCss().curationButton());
				}	
				
				correctButton.setResource(resource.acceptIcon());
				correctButton.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
                        correctButton.setStyleName(widgetUtilsResources.widgetCss().curationButtonBoldGreen());
                        broadButton.setStyleName(widgetUtilsResources.widgetCss().curationButton());
                        incorrectButton.setStyleName(widgetUtilsResources.widgetCss().curationButton());
                        
                        if(_span!=null) {
                            _span.setClassName(StylesManager.LIGHTGREEN_HIGHLIGHT);
                            _span.setAttribute("annotationdefcss", StylesManager.LIGHTGREEN_HIGHLIGHT);
                        }
					    
                        modifyCurationToken(annotation, MCurationToken.CORRECT);
                        
//						MAnnotationSet set = _domeo.getAnnotationPersistenceManager().getSetByAnnotationId(annotation.getLocalId());
//						
//						MCurationToken ann = CurationFactory.createCurationToken(_domeo, 
//							_domeo.getAgentManager().getUserPerson(),
//							_domeo.getAgentManager().getSoftware(), 
//							annotation, MCurationToken.CORRECT, "");
//						
//						_domeo.getAnnotationPersistenceManager().addAnnotationOfAnnotation(ann, annotation, _domeo.getAnnotationPersistenceManager().getCurrentSet());
//						_domeo.refreshAnnotationComponents();
//						if(!_annotator.getPreferencesManager().isDontOpenCurationPanelWhenConfirming() &&
//								_annotator.getPreferencesManager().isAutomaticallyOpenCurationPanel()) {
//							_this.hide();
//							_annotator.showAnnotationItemHistory(info,  CurationTokenDTO.CORRECT, currentItems, currentSpans);
//						} else curateAnnotationItem(span, info, CurationTokenDTO.CORRECT);
					}
				});
				curationPanel.add(correctButton);
				
				if(counts.containsKey(MCurationToken.CORRECT_BROAD)) {
				    broadButton.setText("Broad [" + counts.get(MCurationToken.CORRECT_BROAD)+ "]");
                } else broadButton.setText("Broad");
				
                if(maximum.equals(MCurationToken.CORRECT_BROAD)) {
                    if(myToken.equals(MCurationToken.CORRECT_BROAD)) broadButton.setStyleName(widgetUtilsResources.widgetCss().curationButtonBoldGreen());
                    else broadButton.setStyleName(widgetUtilsResources.widgetCss().curationButtonBold());
                } else {
                    if(myToken.equals(MCurationToken.CORRECT_BROAD)) broadButton.setStyleName(widgetUtilsResources.widgetCss().curationButtonGreen());
                    else broadButton.setStyleName(widgetUtilsResources.widgetCss().curationButton());
                }
				
				broadButton.setResource(resource.acceptBroadIcon());
				broadButton.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
					    
					    broadButton.setStyleName(widgetUtilsResources.widgetCss().curationButtonBoldGreen());
					    correctButton.setStyleName(widgetUtilsResources.widgetCss().curationButton());
                        incorrectButton.setStyleName(widgetUtilsResources.widgetCss().curationButton());
					    
                        if(_span!=null) {
                            _span.setClassName(StylesManager.LIGHTGREEN_HIGHLIGHT);
                            _span.setAttribute("annotationdefcss", StylesManager.LIGHTGREEN_HIGHLIGHT);
                        }
                        
                        modifyCurationToken(annotation, MCurationToken.CORRECT_BROAD);
					    
//						MAnnotationSet set = _domeo.getAnnotationPersistenceManager().getSetByAnnotationId(annotation.getLocalId());
//						
//						MCurationToken ann = CurationFactory.createCurationToken(_domeo, 
//								_domeo.getAgentManager().getUserPerson(),
//								_domeo.getAgentManager().getSoftware(), 
//								annotation, MCurationToken.CORRECT_BROAD, "");
//						
//						_domeo.getAnnotationPersistenceManager().addAnnotationOfAnnotation(ann, annotation, _domeo.getAnnotationPersistenceManager().getCurrentSet());
//						_domeo.refreshAnnotationComponents();
						
//						if(_annotator.getPreferencesManager().isAutomaticallyOpenCurationPanel()) {
//							_this.hide();
//							_annotator.showAnnotationItemHistory(info,  CurationTokenDTO.CORRECT_BROAD, currentItems, currentSpans);
//						} else curateAnnotationItem(span, info, CurationTokenDTO.CORRECT_BROAD);
					}
				});
				curationPanel.add(broadButton);
				
			    if(counts.containsKey(MCurationToken.INCORRECT)) {
			        incorrectButton.setText("Wrong [" + counts.get(MCurationToken.INCORRECT)+ "]");
                } else incorrectButton.setText("Wrong");
				
			    if(maximum.equals(MCurationToken.INCORRECT)) {
                    if(myToken.equals(MCurationToken.INCORRECT)) incorrectButton.setStyleName(widgetUtilsResources.widgetCss().curationButtonBoldRed());
                    else incorrectButton.setStyleName(widgetUtilsResources.widgetCss().curationButtonBold());
                } else {
                    if(myToken.equals(MCurationToken.INCORRECT)) incorrectButton.setStyleName(widgetUtilsResources.widgetCss().curationButtonRed());
                    else incorrectButton.setStyleName(widgetUtilsResources.widgetCss().curationButton());
                }
                
				incorrectButton.setResource(resource.rejectIcon());
				incorrectButton.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
					    
					    incorrectButton.setStyleName(widgetUtilsResources.widgetCss().curationButtonBoldRed());
					    correctButton.setStyleName(widgetUtilsResources.widgetCss().curationButton());
                        broadButton.setStyleName(widgetUtilsResources.widgetCss().curationButton());
					    
					    if(_span!=null) {
					        _span.setClassName(StylesManager.LIGHTRED_HIGHLIGHT);
					        _span.setAttribute("annotationdefcss", StylesManager.LIGHTRED_HIGHLIGHT);
					    }
					    
					    modifyCurationToken(annotation, MCurationToken.INCORRECT);
					    
//						MAnnotationSet set = _domeo.getAnnotationPersistenceManager().getSetByAnnotationId(annotation.getLocalId());
						
//						MCurationToken ann = CurationFactory.createCurationToken(_domeo, 
//								_domeo.getAgentManager().getUserPerson(),
//								_domeo.getAgentManager().getSoftware(), 
//								annotation, MCurationToken.INCORRECT, "");
//						
//					
//						
////						if(((BooleanPreference)_domeo.getPreferences().getPreferenceItem(Domeo.class.getName(), Domeo.PREF_ALLOW_CURATION_MOTIVATION)).getValue()) {
////						    _curationPopup.hide();
////						    CurationViewer lwp = new CurationViewer(_domeo, annotation, null);
////			                new EnhancedGlassPanel(_domeo, lwp, lwp.getTitle(), false, false, false);
////						} else {
//						    _domeo.getAnnotationPersistenceManager().addAnnotationOfAnnotation(ann, annotation, _domeo.getAnnotationPersistenceManager().getCurrentSet());
//	                        _domeo.refreshAnnotationComponents();
//						//}
						
//						if(_annotator.getPreferencesManager().isAutomaticallyOpenCurationPanel()) {
//							_this.hide();
//							_annotator.showAnnotationItemHistory(info,  CurationTokenDTO.INCORRECT, currentItems, currentSpans);
//						} else curateAnnotationItem(span, info, CurationTokenDTO.INCORRECT);
					}
				});
				curationPanel.add(incorrectButton);
				
				curationBar.add(curationPanel);
				
				//curationBar.add(new HTML("WIP: curation bar"));
				
			}
		} catch (Exception e) {
			_domeo.getLogger().exception(this, "create curation bar" +  e.getMessage());
		}
	}
}
