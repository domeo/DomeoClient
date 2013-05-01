package org.mindinformatics.gwt.domeo.client.ui.annotation.actions;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.ui.content.AnnotationFrameWrapper;
import org.mindinformatics.gwt.domeo.model.MAnnotation;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class ActionShowAnnotation {
	
	public static final ClickHandler getClickHandler(final IDomeo domeo, final Object clazz, final MAnnotation annotation) {
		ClickHandler ch = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				domeo.getLogger().command(AnnotationFrameWrapper.LOG_CATEGORY_SHOW_ANNOTATION, clazz, "Item " + annotation.getClass().getName() + "-"+annotation.getLocalId());
				domeo.getContentPanel().getAnnotationFrameWrapper().showAnnotationLocation(annotation);
			}
		};
		return ch;
	}
	
	public static final ClickHandler getClickHandler(final IDomeo domeo, final String annotationId) {
		ClickHandler ch = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				domeo.getLogger().command(AnnotationFrameWrapper.LOG_CATEGORY_SHOW_ANNOTATION, ActionShowAnnotation.class.getName(), "Item " + annotationId);
				domeo.getContentPanel().getAnnotationFrameWrapper().showLocationAnnotationById(annotationId);
			}
		};
		return ch;
	}
}
