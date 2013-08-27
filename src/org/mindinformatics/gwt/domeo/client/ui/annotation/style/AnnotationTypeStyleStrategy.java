package org.mindinformatics.gwt.domeo.client.ui.annotation.style;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.highlight.model.MHighlightAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.nif.antibodies.model.MAntibodyAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.qualifier.model.MQualifierAnnotation;
import org.mindinformatics.gwt.framework.component.styles.src.IStyleStrategy;
import org.mindinformatics.gwt.framework.component.styles.src.StylesManager;

public class AnnotationTypeStyleStrategy implements IStyleStrategy {

	public IDomeo _domeo;
	
	public AnnotationTypeStyleStrategy(IDomeo domeo) {
		_domeo = domeo;
	}
	
	public String getObjectStyleClass(MAnnotation annotation) {
		if(annotation.getAnnotationType().equals(MHighlightAnnotation.TYPE)) {
			return StylesManager.HIGHLIGHT;
		} else if(annotation.getAnnotationType().equals(MQualifierAnnotation.TYPE)) {
			return StylesManager.LIGHTGRAY_HIGHLIGHT;
		} else if(annotation.getAnnotationType().equals(MAntibodyAnnotation.TYPE)) {
			return StylesManager.LIGHTBROWN_HIGHLIGHT;
		} else return StylesManager.ANNOTATION;
	}
	
	@Override
	public String getObjectStyleClass(Object object) {
		if(object instanceof MAnnotation) return getObjectStyleClass((MAnnotation)object);
		return StylesManager.NEUTRAL;
	}
}
