package org.mindinformatics.gwt.domeo.client.ui.sets;

import org.mindinformatics.gwt.domeo.client.Domeo;
import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.ui.east.annotation.view.AnnotationForSetSidePanel;
import org.mindinformatics.gwt.domeo.model.MAnnotationSet;
import org.mindinformatics.gwt.framework.component.ui.lenses.ILensComponent;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
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
public class AnnotationSetSummaryLens extends Composite implements ILensRefresh, ILensComponent {

	interface Binder extends UiBinder<Widget, AnnotationSetSummaryLens> { }
	private static final Binder binder = GWT.create(Binder.class);
	
	private IDomeo _domeo;
	private MAnnotationSet _set;
	private AnnotationSetSummaryLens _this;
	private AnnotationForSetSidePanel _parent;
	
	@UiField VerticalPanel body;
	@UiField Label nameEditableField;
	@UiField SimplePanel itemsNumberLabel;
	@UiField SimplePanel emphasizeCheckBox;
	@UiField SimplePanel accessPolicy;
	@UiField SimplePanel lockingPolicy;
	@UiField SimplePanel visibilityPolicy; 
	@UiField HorizontalPanel provenanceLabel;

	public AnnotationSetSummaryLens(IDomeo domeo, final AnnotationForSetSidePanel parent, MAnnotationSet set) {
		_domeo = domeo;
		_this = this;
		_set = set;
		_parent = parent;
		
		initWidget(binder.createAndBindUi(this));
						
		refreshLens();
	}
	
	public void refresh() {
		_parent.refresh();
	}

	public void refreshLens() {
		try {			
			nameEditableField.setText(_set.getLabel());
			
			
			itemsNumberLabel.clear();
			if(_set.getAnnotations().size()>0) {
				itemsNumberLabel.add( new Label(_set.getAnnotations().size() +(_set.getAnnotations().size()==1?" item":" items")));
			} else {
				itemsNumberLabel.add(new HTML("No items"));
			}
			
			// Emphasize
			// --------------
			
			CheckBox checkHighlight = new CheckBox();
			checkHighlight.setValue(_set.isEmphasized());
			checkHighlight.setEnabled(false);
			emphasizeCheckBox.clear();
			emphasizeCheckBox.add(checkHighlight);
			
		
			// Provenence
			// ----------
			provenanceLabel.clear();
			provenanceLabel.add(new HTML("By " + _set.getCreatedBy().getName() + " on " + _set.getFormattedCreationDate()));
			
			// Access control
			// --------------
			final Image accessIcon = new Image();
			if(_domeo.getAnnotationAccessManager().isAnnotationSetPublic(_set)) {
				accessIcon.setResource(Domeo.resources.publicLittleIcon());
			} else if(_domeo.getAnnotationAccessManager().isAnnotationSetGroups(_set)) {
				accessIcon.setResource(Domeo.resources.friendsLittleIcon());
			} else if(_domeo.getAnnotationAccessManager().isAnnotationSetPrivate(_set)) {
				accessIcon.setResource(Domeo.resources.privateLittleIcon());
			}
	
			accessPolicy.clear();
			accessPolicy.add(accessIcon);
	
			// Locking
			// -------
			final Image rightsIcon = new Image();
			if(_set.getIsLocked()) {
				rightsIcon.setResource(Domeo.resources.readOnlyLittleIcon());
			} else {
				rightsIcon.setResource(Domeo.resources.readWriteLittleIcon());
			}
			lockingPolicy.clear();
			lockingPolicy.add(rightsIcon);
			
			// Visibility
			// ----------
			final Image visibilityIcon = new Image();
			if(_set.getIsVisible()) {
				visibilityIcon.setResource(Domeo.resources.visibleLittleIcon());
			} else {
				visibilityIcon.setResource(Domeo.resources.invisibleLittleIcon());
			}	
			visibilityPolicy.clear();
			visibilityPolicy.add(visibilityIcon);
						
		} catch(Exception e) {
			GWT.log(e.getMessage());
			_domeo.getLogger().exception(this, "Visualization of the set " + _set.getLabel() + " failed");
		}
	}
}
