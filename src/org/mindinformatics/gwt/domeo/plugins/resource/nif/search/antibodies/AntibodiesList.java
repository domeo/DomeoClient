package org.mindinformatics.gwt.domeo.plugins.resource.nif.search.antibodies;

import java.util.ArrayList;
import java.util.HashMap;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.Resources;
import org.mindinformatics.gwt.domeo.plugins.annotation.nif.antibodies.model.MAntibody;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A composite that displays a list of emails that can be selected.
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class AntibodiesList extends ResizeComposite {	
	
  /**
   * Callback when mail items are selected. 
   */
  public interface Listener {
    void onItemSelected(MAntibody term);
  }

  interface Binder extends UiBinder<Widget, AntibodiesList> { }
  interface SelectionStyle extends CssResource {
    String selectedRow();
  }

  private static final Binder binder = GWT.create(Binder.class);
  static final int VISIBLE_EMAIL_COUNT = 20;
  
  	private Boolean PRUNE_ALREADY_SELECTED = true;

  @UiField FlexTable header;
  @UiField FlexTable table;
  @UiField SelectionStyle selectionStyle;
  //@UiField ScrollPanel scroll;
  
  private CheckBox checkAll;

  private Listener listener;
  private int startIndex, selectedRow = -1;

  ArrayList<MAntibody> _antibodies;
  HashMap<String, MAntibody> _currentAntibodies;
  ArrayList<TermCheckBox> _checkBoxes;
  
    private IDomeo _domeo;
  	private IAntibodiesWidget _main;
  	private Resources _resources;
  
	public AntibodiesList(IDomeo domeo, Resources resources, IAntibodiesWidget main, ArrayList<MAntibody> antibodies, HashMap<String, MAntibody> currentAntibodies) {
		_domeo = domeo;
		_main = main;
		_antibodies = antibodies;
		_currentAntibodies = currentAntibodies;
		_resources = resources;
		
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
		if (startIndex >= _antibodies.size()) {
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
		try {
			// Initialize the header.
			header.getColumnFormatter().setWidth(0, "25px");
			//header.getColumnFormatter().setWidth(1, "150px");
			header.getColumnFormatter().setWidth(2, "120px");
	
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
	
			//header.setWidget(0, 0, checkAll);
			header.setText(0, 1, "Antibody Name");
			header.setText(0, 2, "Catalog#");
	
			//header.getCellFormatter().addStyleName(0, 2, "cs-center");
	
			// Initialize the table.
			table.getColumnFormatter().setWidth(0, "25px");
			//table.getColumnFormatter().setWidth(1, "150px");
			//table.getColumnFormatter().setWidth(1, "250px");
			table.getColumnFormatter().setWidth(2, "120px");
		} catch(Exception e) {
			_domeo.getLogger().debug(this, "Problems in initTable: " + e.getMessage());
		}
	}

	/**
	 * Selects the given row (relative to the current page).
	 * 
	 * @param row the row to be selected
	 */
	private void selectRow(int row) {
		// When a row (other than the first one, which is used as a header) is
		// selected, display its associated MailItem.
		MAntibody item = _antibodies.get(startIndex + row);
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
		
//		AntibodyLensProvider lens = new AntibodyLensProvider(_domeo, _resources);
//		
//		//ArrayList<AnnotationTermDTO> deleted = new ArrayList<AnnotationTermDTO>();
		try {
			for (int i = 0; i < _antibodies.size(); i++) {
				MAntibody antibody = _antibodies.get(i);
				TermCheckBox tmp = new TermCheckBox(_main, this, antibody, i);
				//if(PRUNE_ALREADY_SELECTED && _currentAntibodies.containsKey(antibody.getUrl())) {
					//deleted.add(item);
				//	tmp.setEnabled(false);
					//continue;
				//}

				_checkBoxes.add(tmp);

				displayRow(i, antibody, tmp);
			}
			
			applyDataRowStyles();
			
			/*
			if(PRUNE_ALREADY_SELECTED) {
				_terms.removeAll(deleted);
				_main.updateResultsNumbers();
			}
			*/
			
		} catch (Exception e) {
			_domeo.getLogger().debug(this, "Problems in update(): " + e.getMessage());
		}
	}
  
	public void update(String sourceIdFilter) {
		
		checkAll.setValue(false);

		_checkBoxes = new ArrayList<TermCheckBox>();
		table.clear(true);
		table.removeAllRows();

//		AntibodyLensProvider lens = new AntibodyLensProvider(_domeo, _resources);
		
		try {
			int row = 0;
			for (int i = 0; i < _antibodies.size(); i++) {

				MAntibody antibody = _antibodies.get(i);

				if (!AntibodiesSearch.ALL_URI.equals(sourceIdFilter)
						&& !antibody.getVendor().equals(sourceIdFilter))
					continue;

				TermCheckBox tmp = new TermCheckBox(_main, this, antibody, row);
				_checkBoxes.add(tmp);

				displayRow(row, antibody, tmp);
				
				row++;
			}
			
			applyDataRowStyles();

		} catch (Exception e) {
			_domeo.getLogger().debug(this, "Problems in update: " + e.getMessage());
		}
	}
	
	private void displayRow(int row, MAntibody item, TermCheckBox chk) {
		
		StringBuffer sb = new StringBuffer();

		//List<ILinkedDataDigester> digesters = _domeo.getLinkedDataDigestersManager().getLnkedDataDigesters(item);
		//_domeo.getLogger().debug(this,"5 " + digesters);
		//for(ILinkedDataDigester digester: digesters) {
		//	sb.append("<a target=\"_blank\"href=\""+digester.getLinkUrl(item)+"\">@"+digester.getLinkLabel(item)+"</a>&nbsp;");
		//}

		table.setWidget(row, 0, chk);
		//table.setWidget(row, 1, new HTML("<a target=\"_blank\"href=\""+item.getSource().getUrl()+"\">"+item.getSource().getLabel()+"</a>"));
		table.setWidget(row, 1, new HTML("<a target='_blank' href='" + item.getUrl() + "'><b>" + item.getLabel() + "</b></a>" + ((item.getVendor()!=null)? ": "+item.getVendor():"") + "; " + sb.toString()));
		table.setWidget(row, 2, new HTML(item.getCatalog()));
	}
  
  	private void checkSelection() {
  		ArrayList<MAntibody> antibodies = getSelectedPublications();
  		
  		for(MAntibody _publication: antibodies) {
	  		_main.addAntibody(_publication);
			_main.getAntibodiesResults().remove(_publication);
  		}
  		
  		this.update();
  		checkAll.setValue(false);
  	}
  	
  	public ArrayList<MAntibody> getSelectedPublications() {
  		ArrayList<MAntibody> antibodies = new ArrayList<MAntibody>();
  		
  		for(TermCheckBox checkBox: _checkBoxes) {
			if(checkBox.getValue()) {
				antibodies.add(checkBox.getAntibody());
			}
  		}
  		return antibodies;
  	}

	public ArrayList<MAntibody> getTerms() {
		return _antibodies;
	}
	
	//
	int rowIndex = 1;

	private void applyDataRowStyles() {
		HTMLTable.RowFormatter rf = table.getRowFormatter();
		for (int row = 0; row < table.getRowCount(); ++row) {
			if ((row % 2) != 0) {
				rf.addStyleName(row, "FlexTable-OddRow");
			} else {
				rf.addStyleName(row, "FlexTable-EvenRow");
			}
		}
	}
}

class TermCheckBox extends CheckBox {
	private int _row;
	private MAntibody _publication;
	private TermCheckBox _this;
	private AntibodiesList _publicationList;
	private IAntibodiesWidget _main;
	
	public TermCheckBox(IAntibodiesWidget main, AntibodiesList termList, MAntibody publication, int row) {
		super();
		_main = main;
		_publication = publication;
		_row = row;
		_publicationList = termList;
		
		_this = this;
		
		
		this.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(_this.getValue()) {
					_publicationList.styleRow(_row, true);
					_main.addAntibody(_publication);
					_main.getAntibodiesResults().remove(_publication);
					_publicationList.update(_main.getFilterValue());
				} else {
					_publicationList.styleRow(_row, false);
				}
				//_termList.updateList();
			}
		});
	}
	
	public void setValue(Boolean value) {
		super.setValue(value);
		if(value) {
			_publicationList.styleRow(_row, true);
		} else {
			_publicationList.styleRow(_row, false);
		}
	}
	
	public MAntibody getAntibody() {
		return _publication;
	}
}

