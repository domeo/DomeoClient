package org.mindinformatics.gwt.domeo.client.feature.clipboard;

import java.util.ArrayList;

import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.framework.component.IInitializableComponent;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class ClipboardManager implements IInitializableComponent {

	ArrayList<MAnnotation> buffer = new ArrayList<MAnnotation>();
	
	public boolean addAnnotation(MAnnotation annotation) {
		buffer.add(annotation);
		return true;
	}
	
	public void removeAnnotation(MAnnotation annotation) {
		buffer.remove(annotation);
	}
	
	public ArrayList<MAnnotation> getBufferedAnnotation() {
		return buffer;
	}

	@Override
	public void init() {
		buffer = new ArrayList<MAnnotation>();
	}
	
	/*
	 ArrayList<MSelectionAnnotation> buffer = new ArrayList<MSelectionAnnotation>();
	
	public boolean addAnnotation(MSelectionAnnotation annotation) {
		buffer.add(annotation);
		return true;
	}
	
	public void removeAnnotation(MSelectionAnnotation annotation) {
		buffer.remove(annotation);
	}
	
	public ArrayList<MSelectionAnnotation> getBufferedAnnotation() {
		return buffer;
	}

	@Override
	public void init() {
		buffer = new ArrayList<MSelectionAnnotation>();
	}
	 */
}
