package org.mindinformatics.gwt.domeo.client.ui.annotation.actions;

import java.util.ArrayList;
import java.util.List;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.ui.content.AnnotationFrameWrapper;
import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.framework.component.ICommentsRefreshableComponent;
import org.mindinformatics.gwt.framework.component.ui.east.ASidePanel;
import org.mindinformatics.gwt.framework.component.ui.east.ASideTab;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class ActionCommentAnnotation {

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
				domeo.getLogger().command(AnnotationFrameWrapper.LOG_CATEGORY_COMMENT_ANNOTATION, clazz, "Item " + annotation.getClass().getName() + "-"+annotation.getLocalId());
				ASideTab tab = domeo.getLinearCommentsSideTab();//.getDiscussionSideTab();
				ASidePanel panel = domeo.getSidePanelsFacade().getPanelForTab(tab);
				List<MAnnotation> annotations = new ArrayList<MAnnotation>();
				//annotations.add(annotation);
				ArrayList<MAnnotation>  anns= domeo.getPersistenceManager().getAnnotationCascade(annotation, true);
				annotations.addAll(anns);
				((ICommentsRefreshableComponent)panel).refresh(annotations);
				domeo.getSidePanelsFacade().setActive(true);
				domeo.getSidePanelsFacade().selectTab(tab);
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
			final Object clazz, final IAnnotationCommentListener listener, final MAnnotation annotation) {
		ClickHandler ch = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				domeo.getLogger().command(AnnotationFrameWrapper.LOG_CATEGORY_COMMENT_ANNOTATION, clazz, "Item " + annotation.getClass().getName() + "-"+annotation.getLocalId());
				listener.commentAnnotation(annotation);
			}
		};
		return ch;
	}
}
