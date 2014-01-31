package org.mindinformatics.gwt.domeo.client.ui.lenses.selectors;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.ui.annotation.actions.IAnnotationEditListener;
import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.model.selectors.MTextQuoteSelector;
import org.mindinformatics.gwt.domeo.model.selectors.MSelector;
import org.mindinformatics.gwt.domeo.model.selectors.SelectorUtils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/*
 *  @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class TPrefixSuffixTextSelectorTile extends ASelectorTileComponent {

	interface Binder extends UiBinder<Widget, TPrefixSuffixTextSelectorTile> { }
	private static final Binder binder = GWT.create(Binder.class);
	
	private boolean _single;
	private MTextQuoteSelector _selector;
	private MAnnotation _annotation;
	
	@UiField VerticalPanel body;
	@UiField HorizontalPanel provenance;
	@UiField FlowPanel content;
	
	public TPrefixSuffixTextSelectorTile(IDomeo domeo, IAnnotationEditListener listener) {
		super(domeo, listener);
		
		initWidget(binder.createAndBindUi(this));
	}
	
	public void initializeLens(MAnnotation annotation, MSelector selector, boolean single) {
		try {
			_single = single;
			_selector = (MTextQuoteSelector) selector;
			_annotation = annotation;
			refresh();
		} catch(Exception e) {
			_domeo.getLogger().exception(this, e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public MSelector getSelector() {
		return _selector;
	}

	public void refresh() {
		try {
			createProvenanceBar(provenance, _annotation, _single);
			injectButtons(content, _annotation);
			
			content.add(new HTML(SelectorUtils.getPrefix(_selector)+
					"<b>"+SelectorUtils.getMatch(_selector)+"</b>"+
					SelectorUtils.getSuffix(_selector)));
		} catch(Exception e) {
			_domeo.getLogger().exception(this, e.getMessage());
		}
	}
}
