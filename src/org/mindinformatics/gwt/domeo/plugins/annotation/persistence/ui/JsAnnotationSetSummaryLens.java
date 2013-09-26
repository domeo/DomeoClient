package org.mindinformatics.gwt.domeo.plugins.annotation.persistence.ui;

import org.mindinformatics.gwt.domeo.client.Domeo;
import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology;
import org.mindinformatics.gwt.domeo.plugins.persistence.json.marshalling.JsoAnnotationSetSummary;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class JsAnnotationSetSummaryLens extends Composite {

	interface Binder extends UiBinder<Widget, JsAnnotationSetSummaryLens> { }
	private static final Binder binder = GWT.create(Binder.class);
	
	private IDomeo _domeo;
	private JsoAnnotationSetSummary _set;
	private JsAnnotationSetSummaryLens _this;
	private ExistingAnnotationSetsListPanel _parent;
	
	@UiField VerticalPanel body;
	@UiField FocusPanel wrapper;
	@UiField SimplePanel nameEditableField;
	@UiField SimplePanel itemsNumberLabel;
	//@UiField SimplePanel descriptionField;
	@UiField HorizontalPanel provenanceLabel;
	@UiField CheckBox selectionButton;
	@UiField SimplePanel typeField;
	@UiField SimplePanel arrow;

	public JsAnnotationSetSummaryLens(IDomeo domeo, final ExistingAnnotationSetsListPanel parent, JsoAnnotationSetSummary set) {
		_domeo = domeo;
		_this = this;
		_set = set;
		_parent = parent;
		
		initWidget(binder.createAndBindUi(this));

		if(set.getType().equals(IDomeoOntology.discussionSet)) {
			typeField.add(new Image(Domeo.resources.littleCommentsIcon()));
		} else {
			typeField.setVisible(false);
		}
		
		wrapper.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				parent.displayAannotationSetInfo(_set);
			}
		});

		selectionButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				//parent.setSelectedAnnotaitonSet(_set);	
				parent.refresh();
				event.stopPropagation();
			}
		});
		
		refreshLens();
	}
	
	public void setSelected(boolean selection) {
		selectionButton.setValue(selection);
	}
	
	public boolean isSelected() {
		return selectionButton.getValue().booleanValue();
	}

	public void refresh() {
		_parent.refresh();
	}

	public void refreshLens() {
		try {
			/*
			if(_parent.isAnnotationSetSelected(_set)) {
				selectionButton.setValue(true);
			}
			*/
			
			nameEditableField.add(new HTML(_set.getLabel()));
			//descriptionField.add(new HTML(_set.getDescription()));
			
			itemsNumberLabel.clear();
			if(_set.getNumberOfAnnotationItems()>0) {
				Label itemsNumberText = new Label(_set.getNumberOfAnnotationItems() +(_set.getNumberOfAnnotationItems()==1?" item":" items"));
				itemsNumberText.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						//_domeo.displayAnnotationOfSet(_set);
					}
				});
				itemsNumberLabel.add(itemsNumberText);
			} else {
				itemsNumberLabel.add(new HTML("No items"));
			}
			
			// Emphasize
			// --------------
			
			final CheckBox checkHighlight = new CheckBox();
			//checkHighlight.setValue(_set.isEmphasized());
			checkHighlight.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					_domeo.getLogger().command(_this, "Emphasis on " + checkHighlight.getValue());
					//_domeo.getContentPanel().getAnnotationFrameWrapper()
					//	.manageSetEmphasis(_set, checkHighlight.getValue());
					_domeo.getComponentsManager().updateObjectLenses(_set);
					_parent.refresh();
				}
			});
			//emphasizeCheckBox.clear();
			//emphasizeCheckBox.add(checkHighlight);
			
			// Provenence
			// ----------
			provenanceLabel.clear();
			provenanceLabel.add(new HTML("By <a target='_blank' href='" + _set.getCreatedBy().getUri() + "'>" + _set.getCreatedBy().getScreenName() +"</a> on " + _set.getFormattedCreatedOn()));
			
			
			// Access control
			// --------------
			/*
			final Image accessIcon = new Image();
			ClickHandler accessHandler = new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					AnnotationAccessPopup popup = new AnnotationAccessPopup(_domeo, _this, _set);
					popup.show(accessIcon.getAbsoluteLeft(), accessIcon.getAbsoluteTop());
					event.stopPropagation();
				}
			};
			if(_domeo.getAnnotationAccessManager().isAnnotationSetPublic(_set)) {
				accessIcon.setResource(Domeo.resources.publicLittleIcon());
				addSimpleButton(accessPolicy, accessIcon, accessHandler);
			} else if(_domeo.getAnnotationAccessManager().isAnnotationSetGroups(_set)) {
				accessIcon.setResource(Domeo.resources.friendsLittleIcon());
				addSimpleButton(accessPolicy, accessIcon, accessHandler);
			} else if(_domeo.getAnnotationAccessManager().getAnnotationSetAccess(_set).equals(_domeo.getAgentManager().getUserPerson().getUrl())) {
				accessIcon.setResource(Domeo.resources.privateLittleIcon());
				addSimpleButton(accessPolicy, accessIcon, accessHandler);
			}
			*/
	
			// Locking
			// -------
			/*
			final Image rightsIcon = new Image();
			ClickHandler lockHandler = new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					AnnotationRightsPopup popup = new AnnotationRightsPopup(_domeo, _this, _set);
					popup.show(rightsIcon.getAbsoluteLeft(), rightsIcon.getAbsoluteTop());
					event.stopPropagation();
				}
			};
			if(_set.getIsLocked()) {
				rightsIcon.setResource(Domeo.resources.readOnlyLittleIcon());
				addSimpleButton(lockingPolicy, rightsIcon, lockHandler);
			} else {
				rightsIcon.setResource(Domeo.resources.readWriteLittleIcon());
				addSimpleButton(lockingPolicy, rightsIcon, lockHandler);
			}
			*/
			
			// Visibility
			// ----------
			/*
			final Image visibilityIcon = new Image();
			ClickHandler visibilityHandler = new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					AnnotationVisibilityPopup popup = new AnnotationVisibilityPopup(_domeo, _this, _set);
					popup.show(visibilityIcon.getAbsoluteLeft(), visibilityIcon.getAbsoluteTop());
					event.stopPropagation();
				}
			};
			if(_set.getIsVisible()) {
				visibilityIcon.setResource(Domeo.resources.visibleLittleIcon());
				addSimpleButton(visibilityPolicy, visibilityIcon, visibilityHandler);
			} else {
				visibilityIcon.setResource(Domeo.resources.invisibleLittleIcon());
				addSimpleButton(visibilityPolicy, visibilityIcon, visibilityHandler);
			}	
			*/		
		} catch(Exception e) {
			_domeo.getLogger().exception(this, "Visualization of the set " + _set.getLabel() + " failed");
		}
	}
	
	public JsoAnnotationSetSummary getSet() {
		return _set;
	}

	private void addSimpleButton(SimplePanel panel, Image img, ClickHandler clickHandler) {
		panel.clear();
		img.addClickHandler(clickHandler);
		panel.add(img);
	}
}
