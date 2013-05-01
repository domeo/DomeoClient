package org.mindinformatics.gwt.domeo.plugins.annotation.comment.ui.lens;

import org.mindinformatics.gwt.domeo.client.Domeo;
import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.ui.sets.ILensRefresh;
import org.mindinformatics.gwt.domeo.model.MAnnotationSet;
import org.mindinformatics.gwt.domeo.plugins.annotation.comment.model.MCommentAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.comment.ui.east.CommentSidePanel;
import org.mindinformatics.gwt.framework.component.ui.lenses.ILensComponent;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Provides the standard lens for the annotation type Highlight.
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class LCommentThreadSummaryLens extends Composite implements ILensRefresh, ILensComponent {

	interface Binder extends UiBinder<Widget, LCommentThreadSummaryLens> { }
	private static final Binder binder = GWT.create(Binder.class);
	
	private IDomeo _domeo;
	private MCommentAnnotation _comment;
	private MAnnotationSet _set;
	private LCommentThreadSummaryLens _this;
	private CommentSidePanel _parent;
	
	@UiField VerticalPanel body;
	@UiField Label nameEditableField;
	@UiField SimplePanel itemsNumberLabel;
	@UiField HorizontalPanel actionLabel; 
	@UiField HorizontalPanel provenanceLabel;

	public LCommentThreadSummaryLens(IDomeo domeo, final CommentSidePanel parent, MCommentAnnotation annotation) {
		_domeo = domeo;
		_this = this;
		_comment = annotation;
		_set = _domeo.getAnnotationPersistenceManager().getSetByAnnotationId(_comment.getLocalId());
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
			
			/*
			itemsNumberLabel.clear();
			if(_set.getAnnotations().size()>0) {
				itemsNumberLabel.add( new Label(_set.getAnnotations().size() +(_set.getAnnotations().size()==1?" item":" items")));
			} else {
				itemsNumberLabel.add(new HTML("No items"));
			}
			*/
			
		
			// Provenence
			// ----------
			provenanceLabel.clear();
			provenanceLabel.add(new HTML("By " + _comment.getCreator().getName() + " on " + _comment.getFormattedCreationDate()));
			
			actionLabel.clear();
			actionLabel.add(new Image(Domeo.resources.littleCommentIcon()));
			actionLabel.add(new HTML(""+_set.getAnnotations().size()));
			actionLabel.add(new Image(Domeo.resources.usersIcon()));
			actionLabel.add(new HTML("?"));
			
			Image browse = new Image(Domeo.resources.browseLittleIcon());
			browse.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					//Window.alert("Browse thred " + _comment.getLocalId());
					_parent.displayThread(_comment.getLocalId());
				}
			});
			actionLabel.add(browse);
			
			/*
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
			
			//} else if(_domeo.getAnnotationAccessManager().getAnnotationSetAccess(_set).equals(_domeo.getAgentManager().getUserPerson().getUri())) {

			
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
			*/
						
		} catch(Exception e) {
			GWT.log(e.getMessage());
			_domeo.getLogger().exception(this, "Visualization of the set " + _set.getLabel() + " failed");
		}
	}
}
