package org.mindinformatics.gwt.domeo.client.ui.east.debug;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.ui.east.annotation.AnnotationSummaryTableResources;
import org.mindinformatics.gwt.domeo.model.MAnnotation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class AnnotationDebugSummaryTable extends Composite {

	private IDomeo _domeo;
	
	@UiField ScrollPanel body;
	@UiField VerticalPanel content;
	
	interface Binder extends UiBinder<Widget, AnnotationDebugSummaryTable> { }
	private static final Binder binder = GWT.create(Binder.class);
	
	public static final AnnotationSummaryTableResources localResources = 
		GWT.create(AnnotationSummaryTableResources.class);
	
	public AnnotationDebugSummaryTable(IDomeo domeo) {
		_domeo = domeo;
		
		initWidget(binder.createAndBindUi(this));
	}
	
	public void initializePanel() {		
		content.clear();
		
		VerticalPanel vp = new VerticalPanel();
		vp.add(new HTML("No Annotation has been created yet."));
		content.add(vp);
	}

	public void refreshPanel() {
		long start = System.currentTimeMillis();
		try {
			ArrayList<MAnnotation> anns = _domeo.getAnnotationPersistenceManager().getAllAnnotations();
			if(anns!=null && anns.size()>0) {
				content.clear();
				//content.add(getToolbar());
				Collections.sort(anns, new SortByVerticalPostion());
				for(MAnnotation ann: anns) {
					VerticalPanel vp = new VerticalPanel();
					vp.add(new Label(ann.getLocalId() + " - " + ann.getClass().getName() + " - " + ann.getY() +
							" - " + (ann.getHasChanged()?"changed":"unchanged") + 
							(ann.getHasChanged()?(" - " + ((ann.getHasChanged()&&ann.getNewVersion())?"new version":"minor update")):"") +
							" annotated by " + ann.getAnnotatedBy().size()
							));
					content.add(vp);
				}
			}
		} catch(Exception e) {
			_domeo.getLogger().exception(this, e.getMessage());
		}
		content.add(new HTML("Registered components " + _domeo.getComponentsManager().getNumberComponents()));
		content.add(new HTML("Registered qualifiers " + _domeo.getAnnotationPersistenceManager().getAllTermsUrls().size()));
		_domeo.getLogger().debug(this, "Annotation debug summary panel refreshed in (ms):" + (System.currentTimeMillis()-start));
	}

	public class SortByVerticalPostion implements Comparator<MAnnotation>{
	    public int compare(MAnnotation o1, MAnnotation o2) {
	        return o1.getY() - o2.getY();
	    }
	}
}
