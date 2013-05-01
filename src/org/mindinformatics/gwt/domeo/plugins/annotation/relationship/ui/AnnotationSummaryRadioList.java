package org.mindinformatics.gwt.domeo.plugins.annotation.relationship.ui;

import java.util.Comparator;
import java.util.List;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.ui.annotation.actions.IAnnotationEditListener;
import org.mindinformatics.gwt.domeo.client.ui.annotation.tiles.ITileComponent;
import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.qualifier.model.MQualifierAnnotation;
import org.mindinformatics.gwt.framework.component.IInitializableComponent;
import org.mindinformatics.gwt.framework.component.IRefreshableComponent;

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
public class AnnotationSummaryRadioList extends Composite 
	implements IInitializableComponent, IRefreshableComponent{

	private IDomeo _domeo;
	private IAnnotationEditListener _listener;
	private ISubjectSelection _subjectSelectionListener;
	
	@UiField ScrollPanel body;
	@UiField VerticalPanel content;
	
	interface Binder extends UiBinder<Widget, AnnotationSummaryRadioList> { }
	private static final Binder binder = GWT.create(Binder.class);
	
	//public static final AnnotationSummaryTableResources localResources = 
	//	GWT.create(AnnotationSummaryTableResources.class);
	
	public AnnotationSummaryRadioList(IDomeo domeo, IAnnotationEditListener listener, ISubjectSelection subjectSelectionListener) {
		_domeo = domeo;
		_listener = listener;
		_subjectSelectionListener = subjectSelectionListener;
		
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
		refreshPanel(_domeo.getAnnotationPersistenceManager().getAllAnnotations());
	}
	
	public void refreshPanel(List<MAnnotation> annotations) {
		long start = System.currentTimeMillis();
		try {
			content.clear();
			if(annotations!=null && annotations.size()>0) {
				for(MAnnotation ann: annotations) {
					if(!(ann instanceof MQualifierAnnotation)) continue; 
					final MAnnotation _ann = ann;
					ITileComponent c = _domeo.getAnnotationTailsManager().getAnnotationTile(ann.getClass().getName(), _listener);
					if(c==null) {
						VerticalPanel vp = new VerticalPanel();
						vp.add(new Label(ann.getLocalId() + " - " + ann.getClass().getName() + " - " + ann.getY()));
						content.add(vp);
					} else {
						try {
							RadioButton radio = new RadioButton("subjectList");
							radio.addClickHandler(new ClickHandler() {
								@Override
								public void onClick(ClickEvent event) {
									_subjectSelectionListener.selectedSubject(_ann);
								}
							});
							c.initializeLens(ann);
							HorizontalPanel hp = new HorizontalPanel();
							hp.add(radio);
							hp.add(c.getTile());
							content.add(hp);
						} catch(Exception e) {
							// If something goes wrong just display the default tile
							VerticalPanel vp = new VerticalPanel();
							vp.add(new Label(ann.getLocalId() + " - " + ann.getClass().getName() + " - " + ann.getY()));
							content.add(vp);
						}
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
