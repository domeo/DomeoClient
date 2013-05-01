package org.mindinformatics.gwt.domeo.plugins.resource.pubmed.search;

import java.util.ArrayList;
import java.util.HashMap;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.lenses.PubMedCitationPainter;
import org.mindinformatics.gwt.framework.model.references.MPublicationArticleReference;
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
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class PublicationResultsListPanel extends ResizeComposite {

	public static final boolean SINGLE_SELECTION = true;
	
	/**
	 * Callback when mail items are selected.
	 */
	public interface Listener {
		void onItemSelected(MPublicationArticleReference term);
	}

	interface Binder extends UiBinder<Widget, PublicationResultsListPanel> {
	}

	interface SelectionStyle extends CssResource {
		String selectedRow();
	}

	private static final Binder binder = GWT.create(Binder.class);
	static final int VISIBLE_EMAIL_COUNT = 20;

	private Boolean PRUNE_ALREADY_SELECTED = true;

	@UiField
	FlexTable header;
	@UiField
	FlexTable table;
	@UiField
	SelectionStyle selectionStyle;

	private CheckBox checkAll;

	private Listener listener;
	private int startIndex, selectedRow = -1;

	ArrayList<MPublicationArticleReference> _references;
	HashMap<String, MPublicationArticleReference> _currentReferences;
	ArrayList<ReferenceCheckBox> _checkBoxes;

	private IDomeo _domeo;
	private PubmedSearchWidget _parentWidget;
	private WidgetUtils _widgetsUtils;
	private boolean _singleSelection;

	public PublicationResultsListPanel(IDomeo domeo, PubmedSearchWidget parentWidget,
			ArrayList<MPublicationArticleReference> references,
			HashMap<String, MPublicationArticleReference> currentReferences,
			boolean singleSelection) {
		_parentWidget = parentWidget;
		_references = references;
		_currentReferences = currentReferences;
		_domeo = domeo;
		_singleSelection = singleSelection;
		_widgetsUtils = new WidgetUtils();

		initWidget(binder.createAndBindUi(this));

		initTable();
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
		if (startIndex >= _references.size()) {
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
	private void initTable() {
		// Initialize the header.
		header.getColumnFormatter().setWidth(0, "28px");
		// header.getColumnFormatter().setWidth(1, "150px");
		//header.getColumnFormatter().setWidth(2, "102px");

		checkAll = new CheckBox();
		checkAll.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				for (ReferenceCheckBox checkBox : _checkBoxes) {
					if (checkAll.getValue()) {
						checkBox.setValue(true);
					} else
						checkBox.setValue(false);
				}
				checkSelection();
			}
		});

		checkAll.setEnabled(!_singleSelection);
		header.setWidget(0, 0, checkAll);
		header.setText(0, 1, "Publication");
		//header.setText(0, 2, "Date");
		//header.getCellFormatter().addStyleName(0, 2, "cs-center");

		// Initialize the table.
		table.getColumnFormatter().setWidth(0, "25px");
		// table.getColumnFormatter().setWidth(1, "150px");
		//table.getColumnFormatter().setWidth(2, "92px");
	}

	/**
	 * Selects the given row (relative to the current page).
	 * 
	 * @param row
	 *            the row to be selected
	 */
	public void selectRow(int row) {
		// When a row (other than the first one, which is used as a header) is
		// selected, display its associated MailItem.
		MPublicationArticleReference item = _references.get(startIndex + row);
		if (item == null) {
			return;
		}
		
		if(_singleSelection && selectedRow!=-1) {
			_checkBoxes.get(selectedRow).setValue(false); // Uncheck previous selection
			styleRow(selectedRow, false); // Removed selected style in previous selection
		}
		selectedRow = row;

		styleRow(selectedRow, false);
		styleRow(row, true);

		/* Deselect previous row
		String style = selectionStyle.selectedRow();
		if(selectedRow<0) table.getRowFormatter().removeStyleName(row, style);
		*/
		
		

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

	public void refreshAttachedBibliographicObjects() {
		_currentReferences.clear();
		ArrayList<MPublicationArticleReference> bos = _parentWidget
				.getBibliographicObjects();
		for (MPublicationArticleReference bo : bos) {
			_currentReferences.put(bo.getPubMedId(), bo);
		}
	}

	public void update() {
		_checkBoxes = new ArrayList<ReferenceCheckBox>();
		table.clear(true);
		table.removeAllRows();

		// Check this one (should be on)
		// refreshAttachedBibliographicObjects();
		
		try {
			for (int i = 0; i < _references.size(); i++) {
				MPublicationArticleReference publication = _references.get(i);

				ReferenceCheckBox tmp = new ReferenceCheckBox(_parentWidget, this, publication, i);
				if (PRUNE_ALREADY_SELECTED
						&& _currentReferences.containsKey(publication
								.getPubMedId())) {
					// deleted.add(item);
					tmp.setEnabled(false);
					// continue;
				}

				_checkBoxes.add(tmp);

				table.setWidget(i, 0, tmp);
				table.setWidget(i, 1, PubMedCitationPainter.getFullCitation(publication, _domeo));
				//table.setText(i, 2, publication.getPublicationDate());
				//table.getCellFormatter().addStyleName(i, 2, "cs-center");
			}

			_widgetsUtils.applyDataRowStyles(table);

			/*
			 * if(PRUNE_ALREADY_SELECTED) { _terms.removeAll(deleted);
			 * _main.updateResultsNumbers(); }
			 */

		} catch (Exception e) {
			Window.alert(e.getMessage());
		}
	}

	public void update(String sourceIdFilter) {
		checkAll.setValue(false);

		_checkBoxes = new ArrayList<ReferenceCheckBox>();
		table.clear(true);
		table.removeAllRows();

		// PublicationLensProvider lens = new PublicationLensProvider();

		try {
			int row = 0;
			for (int i = 0; i < _references.size(); i++) {

				MPublicationArticleReference publication = _references.get(i);

				if (!PubmedSearchBar.ALL_URI.equals(sourceIdFilter)
						&& !publication.getProvidedBy().getUrl()
								.equals(sourceIdFilter))
					continue;

				ReferenceCheckBox tmp = new ReferenceCheckBox(_parentWidget, this, publication,
						row);
				_checkBoxes.add(tmp);

				table.setWidget(row, 0, tmp);
				table.setWidget(row, 1,
						PubMedCitationPainter.getFullCitation(publication, _domeo));
				//table.setText(row, 2, publication.getPublicationDate());

				row++;
			}

			_widgetsUtils.applyDataRowStyles(table);

		} catch (Exception e) {
			Window.alert(e.getMessage());
		}
	}

	private void checkSelection() {
		ArrayList<MPublicationArticleReference> publications = getSelectedPublications();

		for (MPublicationArticleReference _publication : publications) {
			_parentWidget.addPublication(_publication);
			_parentWidget.getTermsResults().remove(_publication);
		}

		this.update();
		checkAll.setValue(false);
	}

	public ArrayList<MPublicationArticleReference> getSelectedPublications() {
		ArrayList<MPublicationArticleReference> publications = new ArrayList<MPublicationArticleReference>();

		for (ReferenceCheckBox checkBox : _checkBoxes) {
			if (checkBox.getValue()) {
				publications.add(checkBox.getPublication());
			}
		}
		return publications;
	}

	public ArrayList<MPublicationArticleReference> getReferences() {
		return _references;
	}

	//
	int rowIndex = 1;
}

class ReferenceCheckBox extends CheckBox {
	private int _row;
	private ReferenceCheckBox _this;
	private PublicationResultsListPanel _parentWidget;
	private MPublicationArticleReference _publication;
	
	private PubmedSearchWidget _searchWidget;

	public ReferenceCheckBox(PubmedSearchWidget searchWidget,
			PublicationResultsListPanel parentWidget,
			MPublicationArticleReference publication, int row) {
		super();
		_this = this;
		_searchWidget = searchWidget;
		_publication = publication;
		_row = row;
		_parentWidget = parentWidget;

		

		this.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (_this.getValue()) {
					_parentWidget.selectRow(_row);
					_searchWidget.addPublication(_publication);
					_searchWidget.getTermsResults().remove(_publication);
					_parentWidget.update(_searchWidget.getFilterValue());
				} else {
					_parentWidget.styleRow(_row, false);
				}
				// _termList.updateList();
			}
		});
	}

	public void setValue(Boolean value) {
		super.setValue(value);
		if (value) {
			_parentWidget.styleRow(_row, true);
		} else {
			_parentWidget.styleRow(_row, false);
		}
	}

	public MPublicationArticleReference getPublication() {
		return _publication;
	}
}