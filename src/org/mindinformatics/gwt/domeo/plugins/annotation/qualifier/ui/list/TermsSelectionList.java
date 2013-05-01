package org.mindinformatics.gwt.domeo.plugins.annotation.qualifier.ui.list;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.component.linkeddata.digesters.ITrustedResourceDigester;
import org.mindinformatics.gwt.domeo.plugins.resource.bioportal.search.terms.TermsSearch;
import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;
import org.mindinformatics.gwt.framework.widget.WidgetUtils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;

/**
 * A composite that displays a list of terms that can be selected.
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class TermsSelectionList extends ResizeComposite  {	
	
  /**
   * Callback when mail items are selected. 
   */
  public interface Listener {
    void onItemSelected(MLinkedResource term);
  }

  interface Binder extends UiBinder<Widget, TermsSelectionList> { }
  interface SelectionStyle extends CssResource {
    String selectedRow();
  }

  private static final Binder binder = GWT.create(Binder.class);
  static final int VISIBLE_EMAIL_COUNT = 20;
  
  	private Boolean PRUNE_ALREADY_SELECTED = true;

  	protected @UiField FlexTable header;
  protected @UiField FlexTable table;
  @UiField SelectionStyle selectionStyle;
  
  protected CheckBox checkAll;

  private Listener listener;
  private int startIndex, selectedRow = -1;

  ArrayList<MLinkedResource> _terms;
  HashMap<String, MLinkedResource> _currentTerms;
  protected ArrayList<TermCheckBox> _checkBoxes;
  private WidgetUtils _widgetsUtils;
  
  protected IDomeo _domeo;
  	private ITermsSelectionConsumer _main;
  
	public TermsSelectionList(IDomeo domeo, ITermsSelectionConsumer main, ArrayList<MLinkedResource> terms, HashMap<String, MLinkedResource> currentTerms) {
		_domeo = domeo;
		_main = main;
		_terms = terms;
		_currentTerms = currentTerms;
		_widgetsUtils = new WidgetUtils();
		
		initWidget(binder.createAndBindUi(this));

		initTable();
		update();
	}

	public void initTermsList( ArrayList<MLinkedResource> terms) {
	    _terms = terms;
	    update();
	}
	
	/**
	 * Sets the listener that will be notified when an item is selected.
	 */
	public void setListener(Listener listener) {
		this.listener = listener;
	}

	@Override
	protected void onLoad() {
		// Select the first row if none is selected.
		if (selectedRow == -1) {
			// selectRow(0);
		}
	}

	void newer() {
		// Move back a page.
		startIndex -= VISIBLE_EMAIL_COUNT;
		if (startIndex < 0) {
			startIndex = 0;
		} else {
			styleRow(selectedRow, false);
			selectedRow = -1;
			update();
		}
	}

	void older() {
		// Move forward a page.
		startIndex += VISIBLE_EMAIL_COUNT;
		if (startIndex >= _terms.size()) {
			startIndex -= VISIBLE_EMAIL_COUNT;
		} else {
			styleRow(selectedRow, false);
			selectedRow = -1;
			update();
		}
	}

	/**
	 * Initializes the table so that it contains enough rows for a full page of
	 * emails. Also creates the images that will be used as 'read' flags.
	 */
	protected void initTable() {
		// Initialize the header.
		header.getColumnFormatter().setWidth(0, "25px");
		header.getColumnFormatter().setWidth(1, "180px");
		//header.getColumnFormatter().setWidth(2, "300px");
		

		checkAll = new CheckBox();
		checkAll.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				for (TermCheckBox checkBox : _checkBoxes) {
					if (checkAll.getValue()) {
						checkBox.setValue(true);
					} else
						checkBox.setValue(false);
				}
				checkSelection();
			}
		});

		header.setWidget(0, 0, checkAll);
		header.setText(0, 1, "Vocabulary");
		header.setText(0, 2, "Term & Description");

		// Initialize the table.
		table.getColumnFormatter().setWidth(0, "25px");
		table.getColumnFormatter().setWidth(1, "180px");
		//table.getColumnFormatter().setWidth(2, "300px");
	}

	/**
	 * Selects the given row (relative to the current page).
	 * 
	 * @param row the row to be selected
	 */
	@SuppressWarnings("unused")
	protected void selectRow(int row) {
		// When a row (other than the first one, which is used as a header) is
		// selected, display its associated MailItem.
		MLinkedResource item = _terms.get(startIndex + row);
		if (item == null) {
			return;
		}

		styleRow(selectedRow, false);
		styleRow(row, true);

		selectedRow = row;

		if (listener != null) {
			listener.onItemSelected(item);
		}
	}

	public void styleRow(int row, boolean selected) {
		if (row != -1) {
			String style = selectionStyle.selectedRow();

			if (selected) {
				table.getRowFormatter().addStyleName(row, style);
			} else {
				table.getRowFormatter().removeStyleName(row, style);
			}
		}
	}

	public void update() {
		_checkBoxes = new ArrayList<TermCheckBox>();
		table.clear(true);
		table.removeAllRows();
		
		//ArrayList<AnnotationTermDTO> deleted = new ArrayList<AnnotationTermDTO>();
		try {
			for (int i = 0; i < _terms.size(); i++) {
				MLinkedResource item = _terms.get(i);
				TermCheckBox tmp = new TermCheckBox(_main, this, item, i);
				if(PRUNE_ALREADY_SELECTED && _currentTerms.containsKey(item.getUrl())) {
					//deleted.add(item);
					tmp.setEnabled(false);
					//continue;
				}
				_checkBoxes.add(tmp);
				displayRow(i, item, tmp);
			}
			_widgetsUtils.applyDataRowStyles(table);
			
		} catch (Exception e) {
		    _domeo.getLogger().exception(this, e.getMessage());
		}
	}
  
	public void update(String sourceIdFilter) {
		checkAll.setValue(false);

		_checkBoxes = new ArrayList<TermCheckBox>();
		table.clear(true);
		table.removeAllRows();

		try {
			int row = 0;
			for (int i = 0; i < _terms.size(); i++) {

				MLinkedResource item = _terms.get(i);

				if (!TermsSearch.ALL_URI.equals(sourceIdFilter)
						&& !item.getSource().getUrl().equals(sourceIdFilter))
					continue;
					

				TermCheckBox tmp = new TermCheckBox(_main, this, item, row);
				_checkBoxes.add(tmp);

				displayRow(row, item, tmp);
				row++;
			}
			
			_widgetsUtils.applyDataRowStyles(table);

		} catch (Exception e) {
			Window.alert(e.getMessage());
		}
	}
	
	protected void displayRow(int row, MLinkedResource item, CheckBox chk) {
		StringBuffer sb = new StringBuffer();
		List<ITrustedResourceDigester> digesters = _domeo.getLinkedDataDigestersManager().getLnkedDataDigesters(item);
		for(ITrustedResourceDigester digester: digesters) {
			sb.append("<a target=\"_blank\"href=\""+digester.getLinkUrl(item)+"\">@"+digester.getLinkLabel(item)+"</a>&nbsp;");
		}
		table.setWidget(row, 0, chk);
		table.setWidget(row, 1, new HTML("<a target=\"_blank\"href=\""+item.getSource().getUrl()+"\">"+item.getSource().getLabel()+"</a>"));
		table.setWidget(row, 2, new HTML("<b>" + item.getLabel() + "</b>" + ((item.getDescription()!=null)? ": "+item.getDescription():"") + "; " + sb.toString()));
	}
  
	protected void checkSelection() {
  		ArrayList<MLinkedResource> terms = getSelectedTerms();
  		
  		for(MLinkedResource _term: terms) {
	  		_main.addTerm(_term);
			_main.getTermsList().remove(_term);
  		}
  		
  		this.update();
  		checkAll.setValue(false);
  	}
  	
  	public ArrayList<MLinkedResource> getSelectedTerms() {
  		ArrayList<MLinkedResource> terms = new ArrayList<MLinkedResource>();
  		
  		for(TermCheckBox checkBox: _checkBoxes) {
			if(checkBox.getValue()) {
				terms.add(checkBox.getTerm());
			}
  		}
  		return terms;
  	}

	public ArrayList<MLinkedResource> getTerms() {
		return _terms;
	}
	
	//
	int rowIndex = 1;
}

class TermCheckBox extends CheckBox {
	private int _row;
	private MLinkedResource _term;
	private TermCheckBox _this;
	private TermsSelectionList _termList;
	private ITermsSelectionConsumer _main;
	
	public TermCheckBox(ITermsSelectionConsumer main, TermsSelectionList termList, MLinkedResource term, int row) {
		super();
		_main = main;
		_term = term;
		_row = row;
		_termList = termList;
		
		_this = this;
		
		
		this.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(_this.getValue()) {
					_termList.styleRow(_row, true);
					_main.addTerm(_term);
					_main.getTermsList().remove(_term);
					_termList.update(_main.getFilterValue());
				} else {
					_termList.styleRow(_row, false);
				}
				//_termList.updateList();
			}
		});
	}
	
	public void setValue(Boolean value) {
		super.setValue(value);
		if(value) {
			_termList.styleRow(_row, true);
		} else {
			_termList.styleRow(_row, false);
		}
	}
	
	public MLinkedResource getTerm() {
		return _term;
	}
}

