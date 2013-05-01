package org.mindinformatics.gwt.domeo.client.ui.annotation.actions;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.ui.content.AnnotationFrameWrapper;
import org.mindinformatics.gwt.domeo.model.MAnnotation;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class ActionEditAnnotation {

	/**
	 * Returns the edit click handler for an annotation so that the event is
	 * processed by the main wrapper.
	 * @param domeo	The main application	
	 * @param clazz The requesting class.
	 * @param annotation	The annotation to edit
	 * @return The click handler
	 */
	public static final ClickHandler getClickHandler(final IDomeo domeo, 
			final Object clazz, final MAnnotation annotation) {
		ClickHandler ch = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				domeo.getLogger().command(AnnotationFrameWrapper.LOG_CATEGORY_EDIT_ANNOTATION, clazz, "Item " + annotation.getClass().getName() + "-"+annotation.getLocalId());
				domeo.getContentPanel().getAnnotationFrameWrapper().editAnnotation(annotation);
			}
		};
		return ch;
	}
	
	/**
     * Returns the edit click handler for an annotation so that the event is
	 * processed by the declared listener.
	 * @param domeo	The main application	
	 * @param clazz The requesting class.
	 * @param listener	The container that will process the event
	 * @param annotation	The annotation to edit	
	 * @return	
	 */
	public static final ClickHandler getClickHandler(final IDomeo domeo, 
			final Object clazz, final IAnnotationEditListener listener, final MAnnotation annotation) {
		ClickHandler ch = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				domeo.getLogger().command(AnnotationFrameWrapper.LOG_CATEGORY_EDIT_ANNOTATION, clazz, "Item " + annotation.getClass().getName() + "-"+annotation.getLocalId());
				listener.editAnnotation(annotation);
			}
		};
		return ch;
	}
}
