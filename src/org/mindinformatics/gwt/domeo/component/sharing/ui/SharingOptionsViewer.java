package org.mindinformatics.gwt.domeo.component.sharing.ui;

import java.util.ArrayList;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.model.MAnnotationSet;
import org.mindinformatics.gwt.framework.src.IContainerPanel;
import org.mindinformatics.gwt.framework.src.IContentPanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SharingOptionsViewer  extends Composite implements IContentPanel {

	private static final String TITLE = "Sharing options viewer";
	
	// UI BInder
	interface Binder extends UiBinder<VerticalPanel, SharingOptionsViewer> {}
	private static final Binder binder = GWT.create(Binder.class);	
	
	// By contract 
	private IDomeo _domeo;
	private IContainerPanel _container;
	
	@UiField TextArea shareAllBox;
	

	public SharingOptionsViewer(IDomeo domeo) {
		_domeo = domeo;
	
		initWidget(binder.createAndBindUi(this)); // Necessary for initializing Composite
		
		StringBuffer sb = new StringBuffer();
		sb.append(getBaseUrl());
		sb.append("?url=" + _domeo.getPersistenceManager().getCurrentResourceUrl());
		
		ArrayList<MAnnotationSet>  sets = _domeo.getPersistenceManager().getAllUserSets();
		if(sets.size()==1) sb.append("&setId=");
		else if(sets.size()>1) sb.append("&setIds=");
		for(int i=0; i<sets.size(); i++) {
			sb.append(sets.get(i).getIndividualUri());
			if(i<sets.size()-1) sb.append(",");
		}
		
		shareAllBox.setText(sb.toString());
	}
	
	public static native String getBaseUrl()
	/*-{
		var base = $wnd.location.toString();
		var index = $wnd.location.toString().indexOf("?");
		if(index>0) base = base.substring(0, $wnd.location.toString().indexOf("?"));
		return base;
	}-*/;
	
	public static native String encodeUrlComponent(String component) 
	/*-{
		return encodeURIComponent(component);;
	}-*/;
	
	@Override
	public void setContainer(IContainerPanel glassPanel) {
		_container = glassPanel;
	}

	@Override
	public IContainerPanel getContainer() {
		return _container;
	}
	
	public String getTitle() {
		return TITLE;
	}
}
