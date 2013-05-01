package org.mindinformatics.gwt.domeo.client.ui.annotation.actions;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.ui.content.AnnotationFrameWrapper;
import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.model.selectors.MSelector;
import org.mindinformatics.gwt.utils.src.HtmlUtils;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class ActionDeleteSelector {

	public static final ClickHandler getClickHandler(final IDomeo domeo, final Object clazz, final MAnnotation annotation, final MSelector selector) {
		ClickHandler ch = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				domeo.getLogger().command(AnnotationFrameWrapper.LOG_CATEGORY_DELETE_ANNOTATION, clazz, "Item " + annotation.getClass().getName() + "-"+annotation.getLocalId());
				// TODO manage deletion and undo???
				annotation.getSelectors().remove(selector);
				HtmlUtils.removeSpansWithAnnotationId(domeo.getContentPanel().getAnnotationFrameWrapper().getFrame().getElement(), annotation.getLocalId()+":"+selector.getLocalId());
				domeo.refreshAnnotationComponents();
			}
		};
		return ch;
	}
}
