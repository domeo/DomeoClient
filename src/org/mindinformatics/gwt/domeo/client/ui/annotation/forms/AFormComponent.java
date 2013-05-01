package org.mindinformatics.gwt.domeo.client.ui.annotation.forms;

import java.util.ArrayList;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.model.MAnnotationSet;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;

public abstract class AFormComponent extends Composite implements IFormComponent {

	protected IDomeo _domeo;
	protected AFormsManager _manager;
	
	public AFormComponent(IDomeo domeo) {
		_domeo = domeo;
	}
	
	public abstract String getLogCategoryCreate();
	public abstract String getLogCategoryEdit();
	
	public abstract boolean isContentInvalid();
	public abstract boolean isContentChanged(MAnnotation annotation);
	
	ArrayList<MAnnotationSet>  setsBuffer;
	protected void refreshAnnotationSetFilter(ListBox annotationSet, MAnnotation _item) {
		annotationSet.clear();
		setsBuffer = new ArrayList<MAnnotationSet>();
		int counter = 0;
		int selection = 0;
		
		// This line allows Domeo to create a new 'Default set' if no sets have been
		// created yet
		MAnnotationSet currentSet = _domeo.getAnnotationPersistenceManager().getCurrentSet();
		ArrayList<MAnnotationSet>  sets = _domeo.getAnnotationPersistenceManager().getAllUserSets();
			//_annotator.getAnnotationPersistenceManager()
			//.getUnlockedManuallyAnnotatedSets(true);

		for (MAnnotationSet set : sets) {
			//if (set.isLocked())
			//	continue;
			if(_item==null) {
				if(currentSet.getLocalId()==set.getLocalId()) {
					selection = counter;
				}
			} else {
				if(set.getLocalId().equals(_domeo.getAnnotationPersistenceManager().getSetByAnnotationId(_item.getLocalId()).getLocalId())) {
					selection = counter;
				} 
			}
			if(set.getCreatedBy().getUri().equals(_domeo.getAgentManager().getUserPerson().getUri())) {
				annotationSet.addItem(set.getLabel() + " [by Me]");
			} else {
				annotationSet.addItem(set.getLabel());
			}
			setsBuffer.add(set);
			counter++; 
		}
		
		annotationSet.addItem("Create new set");
		setsBuffer.add(null);
		
		annotationSet.setSelectedIndex(selection);
		annotationSet.setWidth("200px");
	}
	
	protected MAnnotationSet getSelectedSet(ListBox annotationSet) {
		return setsBuffer.get(annotationSet.getSelectedIndex());
	}
	
	protected void displayMessage(String message) {
		_manager.displayMessage(message);
		Timer timer = new Timer() {
			public void run() {
				_manager.clearMessage();
			}
		};
		timer.schedule(2000);
	}
	
	protected void displayDialog(String message, boolean close) {
		Window.alert(message + " - If the problem persist it should be reported!");
		if(close) _manager.getContainer().hide();
	}
}
