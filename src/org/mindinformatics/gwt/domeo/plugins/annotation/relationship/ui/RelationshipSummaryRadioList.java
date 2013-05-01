package org.mindinformatics.gwt.domeo.plugins.annotation.relationship.ui;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.ui.annotation.actions.IAnnotationEditListener;
import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.framework.component.IInitializableComponent;
import org.mindinformatics.gwt.framework.component.IRefreshableComponent;
import org.mindinformatics.gwt.framework.component.resources.model.MGenericResource;
import org.mindinformatics.gwt.framework.component.resources.model.MRelationshipIdentifiedByUri;
import org.mindinformatics.gwt.framework.component.ui.lenses.entities.LRelationshipPanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class RelationshipSummaryRadioList extends Composite 
	implements IInitializableComponent, IRefreshableComponent{

	private IDomeo _domeo;
	private IAnnotationEditListener _listener;
	private IPredicateSelection _predicateSelectionListener;
	
	@UiField ScrollPanel body;
	@UiField VerticalPanel content;
	
	interface Binder extends UiBinder<Widget, RelationshipSummaryRadioList> { }
	private static final Binder binder = GWT.create(Binder.class);
	
	//public static final AnnotationSummaryTableResources localResources = 
	//	GWT.create(AnnotationSummaryTableResources.class);
	
	public RelationshipSummaryRadioList(IDomeo domeo, IAnnotationEditListener listener, IPredicateSelection predicateSelectionListener) {
		_domeo = domeo;
		_listener = listener;
		_predicateSelectionListener = predicateSelectionListener;
		
		initWidget(binder.createAndBindUi(this));
	}
	
	@Override
	public void init() {		
		content.clear();
		
		VerticalPanel vp = new VerticalPanel();
		vp.add(new HTML("No Annotation has been created yet."));
		content.add(vp);
	}
	
	@Override
	public void refresh() {
		//refreshPanel(_domeo.getAnnotationPersistenceManager().getAllAnnotations());
	}
	
	public void refreshPanel(ArrayList<MRelationshipIdentifiedByUri> relationships) {
		long start = System.currentTimeMillis();
		try {
			content.clear();
			if(relationships!=null && relationships.size()>0) {
				for(MRelationshipIdentifiedByUri ann: relationships) {
					final MRelationshipIdentifiedByUri _ann = ann;
					LRelationshipPanel lens = new LRelationshipPanel(_domeo);
					lens.initializeLens((MGenericResource) _ann, new HashMap<String, String>());

					try {
						RadioButton radio = new RadioButton("predicateList");
						radio.addClickHandler(new ClickHandler() {
							@Override
							public void onClick(ClickEvent event) {
								_predicateSelectionListener.selectedPredicate(_ann);
							}
						});
						//c.initializeLens(ann);
						HorizontalPanel hp = new HorizontalPanel();
						hp.add(radio);
						hp.add(lens);
						content.add(hp);
					} catch(Exception e) {
						// If something goes wrong just display the default tile
						VerticalPanel vp = new VerticalPanel();
						vp.add(new Label(ann.getUrl() + " - " + ann.getClass().getName()));
						content.add(vp);
					}
				}
			}
		} catch(Exception e) {
			_domeo.getLogger().exception(this, e.getMessage());
		}
		_domeo.getLogger().debug(this, "Annotation summary panel with search term refreshed in (ms):" + (System.currentTimeMillis()-start));
	}

	public class SortByVerticalPostion implements Comparator<MAnnotation>{
	    public int compare(MAnnotation o1, MAnnotation o2) {
	        return o1.getY() - o2.getY();
	    }
	}
}
