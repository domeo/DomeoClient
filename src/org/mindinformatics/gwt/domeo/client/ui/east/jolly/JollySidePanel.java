package org.mindinformatics.gwt.domeo.client.ui.east.jolly;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.framework.component.IAnnotationRefreshableComponent;
import org.mindinformatics.gwt.framework.component.IInitializableComponent;
import org.mindinformatics.gwt.framework.component.IRefreshableComponent;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class JollySidePanel extends Composite implements IInitializableComponent, 
		IRefreshableComponent, IAnnotationRefreshableComponent {

	interface Binder extends UiBinder<Widget, JollySidePanel> { }
	private static final Binder binder = GWT.create(Binder.class);
	
	@UiField VerticalPanel body;
	
	private Widget _content;
	
	// By contract 
	@SuppressWarnings("unused")
	private IDomeo _domeo;
	
	public JollySidePanel(IDomeo domeo, Widget content) {
		_domeo = domeo;
		_content = content;
		
		initWidget(binder.createAndBindUi(this));
		
		if(!(content instanceof IAnnotationRefreshableComponent)) {
			body.add(new HTML("Added component is not refreshable"));
		}
		
		body.add(content);
	}
	
	public void init() {
		
	}
	
	public void refresh() {
		((IAnnotationRefreshableComponent)_content).refresh();
	}
}
