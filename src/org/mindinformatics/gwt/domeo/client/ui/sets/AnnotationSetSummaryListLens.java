package org.mindinformatics.gwt.domeo.client.ui.sets;

import org.mindinformatics.gwt.domeo.client.Domeo;
import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.ui.east.sets.AnnotationSetsSidePanel;
import org.mindinformatics.gwt.domeo.model.MAnnotationSet;
import org.mindinformatics.gwt.framework.component.ui.lenses.ILensComponent;
import org.mindinformatics.gwt.framework.model.users.IUserGroup;
import org.mindinformatics.gwt.framework.widget.EditableLabel;
import org.mindinformatics.gwt.framework.widget.IEditLabelUpdateHandler;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class AnnotationSetSummaryListLens extends Composite implements ILensRefresh, ILensComponent, IEditLabelUpdateHandler {

	interface Binder extends UiBinder<Widget, AnnotationSetSummaryListLens> { }
	private static final Binder binder = GWT.create(Binder.class);
	
	private IDomeo _domeo;
	private MAnnotationSet _set;
	private AnnotationSetSummaryListLens _this;
	private AnnotationSetsSidePanel _parent;
	
	@UiField VerticalPanel body;
	@UiField FocusPanel wrapper;
	@UiField EditableLabel nameEditableField;
	@UiField SimplePanel itemsNumberLabel;
	@UiField SimplePanel emphasizeCheckBox;
	@UiField SimplePanel accessPolicy;
	@UiField SimplePanel lockingPolicy;
	@UiField SimplePanel visibilityPolicy; 
	@UiField HorizontalPanel provenanceLabel;
	@UiField RadioButton selectionButton;

	public AnnotationSetSummaryListLens(IDomeo domeo, final AnnotationSetsSidePanel parent, MAnnotationSet set) {
		_domeo = domeo;
		_this = this;
		_set = set;
		_parent = parent;
		
		initWidget(binder.createAndBindUi(this));
		
		wrapper.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				parent.displayAannotationSetInfo(_set);
			}
		});
		
		nameEditableField.setLabelUpdateHandler(this);
		nameEditableField.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				_set.setLabel(event.getValue());
				_set.setHasChanged(true);
				_domeo.refreshAnnotationComponents();
			}
		});
		
		selectionButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				parent.setSelectedAnnotaitonSet(_set);	
				event.stopPropagation();
			}
		});
		
		refreshLens();
	}
	
	public void refresh() {
		_parent.refresh();
	}

	public void refreshLens() {
		try {
			if(_parent.isAnnotationSetSelected(_set)) {
				selectionButton.setValue(true);
			}
			
			nameEditableField.setValue(_set.getLabel());
			
			itemsNumberLabel.clear();
			if(_set.getAnnotations().size()>0) {
				Label itemsNumberText = new Label(_set.getAnnotations().size() +(_set.getAnnotations().size()==1?" item":" items"));
				itemsNumberText.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						_domeo.displayAnnotationOfSet(_set);
					}
				});
				itemsNumberLabel.add(itemsNumberText);
			} else {
				itemsNumberLabel.add(new HTML("No items"));
			}
			
			// Emphasize
			// --------------
			
			final CheckBox checkHighlight = new CheckBox();
			checkHighlight.setValue(_set.isEmphasized());
			
			checkHighlight.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					_domeo.getLogger().command(_this, "Emphasis on " + checkHighlight.getValue());
					_domeo.getContentPanel().getAnnotationFrameWrapper()
						.manageSetEmphasis(_set, checkHighlight.getValue());
					_domeo.getComponentsManager().updateObjectLenses(_set);
					_parent.refresh();
				}
			});
			
			emphasizeCheckBox.clear();
			emphasizeCheckBox.add(checkHighlight);
			
			// Provenence
			// ----------
			provenanceLabel.clear();
			provenanceLabel.add(new HTML("By " + _set.getCreatedBy().getName() + " on " + _set.getFormattedCreationDate()));
			
			// Access control
			// --------------
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
				
				int counter = 0;
				StringBuffer sb = new StringBuffer();
				for(IUserGroup group: _domeo.getAnnotationAccessManager().getAnnotationSetGroups(_set)) {
					sb.append(group.getName());
					counter++;
					if(counter<_domeo.getAnnotationAccessManager().getAnnotationSetGroups(_set).size()) sb.append(", ");					
				}
				accessIcon.setTitle(sb.toString());
				
			} else if(_domeo.getAnnotationAccessManager().getAnnotationSetAccess(_set).equals(_domeo.getAgentManager().getUserPerson().getUri())) {
				accessIcon.setResource(Domeo.resources.privateLittleIcon());
				addSimpleButton(accessPolicy, accessIcon, accessHandler);
			}
			
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
		} catch(Exception e) {
			_domeo.getLogger().exception(this, "Visualization of the set " + _set.getLabel() + " failed");
		}
	}
	
	public MAnnotationSet getSet() {
		return _set;
	}

	private void addSimpleButton(SimplePanel panel, Image img, ClickHandler clickHandler) {
		panel.clear();
		img.addClickHandler(clickHandler);
		panel.add(img);
	}

	@Override
	public void labelUpdated() {
		_domeo.getComponentsManager().updateObjectLenses(_set);
		_parent.refresh();
	}
}
