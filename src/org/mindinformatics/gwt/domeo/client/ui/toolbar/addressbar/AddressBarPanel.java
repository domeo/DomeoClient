package org.mindinformatics.gwt.domeo.client.ui.toolbar.addressbar;

import org.mindinformatics.gwt.domeo.client.Domeo;
import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.framework.component.ui.toolbar.IToolbarItem;
import org.mindinformatics.gwt.framework.src.Application;
import org.mindinformatics.gwt.framework.src.ApplicationUtils;
import org.mindinformatics.gwt.framework.src.IApplication;
import org.mindinformatics.gwt.utils.src.UrlUtils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.ClosingEvent;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.TextBox;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class AddressBarPanel extends Composite implements IToolbarItem, 
	ValueChangeHandler<String>, SelectionHandler<Suggestion> {
	
	public static final AddressBarResources localResources = 
		GWT.create(AddressBarResources.class);
	
	interface Binder extends UiBinder<HorizontalPanel, AddressBarPanel> { }	
	private static final Binder binder = GWT.create(Binder.class);
	
	@UiField HorizontalPanel main;
	Image goButton;
	
	private boolean activeFlag = false;
	
	private static final String ITEMS_HEIGHT = "22px";
	
	private static final String ADDRESS_BOX_TITLE = "Address box: type here the url of the document to annotate!";
	private static final String LOAD_DOCUMENT_TITLE = "Load the document specified in the address box";
	
	/**
	 * Create a remote service proxy to talk to the server-side Url History service.
	 */
	//private final UrlHistoryServiceAsync urlHistoryService = GWT.create(UrlHistoryService.class);

	private IApplication _application;
	private IDomeo _domeo;
	private ClickHandler _clickHandler;
	private KeyPressHandler _keyPressHandler;
	private SelectionHandler<Suggestion> _selectionHandler;
	private HandlerRegistration addressBoxHandler;
	@SuppressWarnings("unused")
	private HandlerRegistration addressBoxSelectionHandler;
	private HandlerRegistration buttonHandler;
	
/*	Window.addWindowClosingHandler(new Window.ClosingHandler() {

		@Override
		public void onWindowClosing(ClosingEvent event) {

			if (_this.getAnnotationPersistenceManager()
					.isWorskspaceUnsaved()) {
				Window.alert("The workspace contains unsaved annotation.\n\n"
						+ "By selecting 'Leaving the Page', the unsaved annotations will be lost.\n\n"
						+ "By selecting 'Stay on Page', you will have the chance to save the annotation.\n\n");
				event.setMessage("The workspace contains unsaved annotation.");
			}
		}
	});*/
	
	
	public AddressBarPanel(IApplication application) {
		_application = application;
		
		initWidget(binder.createAndBindUi(this)); 
		
		localResources.addressBarCss().ensureInjected();

		main.setStyleName(localResources.addressBarCss().addressBarPanel());
		//main.add(new Image(_images.leftSideAddressBar().getURL()));
		main.add(createAddressBar());

		goButton = new Image(localResources.rightSideAddressBar().getSafeUri().asString());
		goButton.setHeight(ITEMS_HEIGHT);
		goButton.setTitle(LOAD_DOCUMENT_TITLE);
		
		
		main.add(goButton);
	}
	
	public void initializeHandlers(ClickHandler clickHandler, KeyPressHandler keyPressHandler, SelectionHandler<Suggestion> selectionHandler) {
		_clickHandler = clickHandler;
		_keyPressHandler = keyPressHandler; 
		_selectionHandler = selectionHandler;
		enable();
	}

	// Document loading
	private TextBox textBox;
	private SuggestBox _addressBox;
	private SuggestBox.DefaultSuggestionDisplay suggestDisplay;
	private MultiWordSuggestOracle _addressOracle;
	
	public SuggestBox createAddressBar() {	
        suggestDisplay = new SuggestBox.DefaultSuggestionDisplay();
        textBox = new TextBox();
        
		_addressBox = new SuggestBox(getOracleSuggestions(), textBox, suggestDisplay);
		
		_addressBox.setTitle(ADDRESS_BOX_TITLE);
		_addressBox.setWidth("440px");
		_addressBox.addValueChangeHandler(this);
		
		// TODO solve the problem: glass panel not disappearing
		
		return _addressBox;
	}
	
	public void setAddress(String url) {
		_addressBox.setValue(url);
	}
	
	public String getAddress() {
		return _addressBox.getText();
	}
	
	public boolean isSuggestionListShowing() {
		return suggestDisplay.isSuggestionListShowing();
	}
	
	private MultiWordSuggestOracle getOracleSuggestions() {
		_addressOracle = new MultiWordSuggestOracle();
		addOracleSuggestions(_addressOracle);
		/*
		_addressOracle.requestSuggestions(new SuggestOracle.Request(_addressBox.getText()), urlHistoryService.greetServer("",
				new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
				// Show the RPC error message to the user
			}

			public void onSuccess(String result) {
				Window.alert(result);
			}
		});
		*/
		return _addressOracle;
	}

	public void onValueChange(ValueChangeEvent<String> event) {
		//Window.alert("Changed");
		/*
		String textToServer = _addressBox.getText();
		urlHistoryService.greetServer(textToServer,
				new AsyncCallback<String>() {
					public void onFailure(Throwable caught) {
						// Show the RPC error message to the user
					}

					public void onSuccess(String result) {
						//Window.alert(result);
					}
				});
			*/
	}

	public void onSelection(SelectionEvent<Suggestion> event) {
		if(!activeFlag && isValidUrl()) {
			activeFlag = true;
			//_application.attemptContentLoading(getUrl(event.getSelectedItem().getReplacementString()));
		}
	}
	
	public void resetActiveFlag() {
		activeFlag = false;
	}
	
	private String getUrl(String url) {
		String currentUrl;
		if(url.indexOf('|')>0) currentUrl = url.substring(0, url.indexOf('|'));
		else currentUrl = url;
		return currentUrl;
	}
	
	private boolean isValidUrl() {
		if(getUrl(_addressBox.getText()).length()>0)
			return true;
		else
			return false;
	}

	@Override
	public void enable() {
		textBox.setEnabled(true);
		_addressBox.setStyleName(localResources.addressBarCss().addressTextField());
		// Because of a bug I can't attach the event to the SuggestBox
		// or the event will fire twice. I add to its TextBox instead
		addressBoxHandler = _addressBox.getTextBox().addKeyPressHandler(_keyPressHandler);
		buttonHandler = goButton.addClickHandler(_clickHandler);
		// Commented out as it lead to undesired behavior in Chrome
		addressBoxSelectionHandler = _addressBox.addSelectionHandler(_selectionHandler);
		goButton.setStyleName(Application.applicationResources.css().button());
	}
	
	@Override
	public void disable() {
		textBox.setEnabled(false);
		addressBoxHandler.removeHandler();
		buttonHandler.removeHandler();
		goButton.setStyleName(Application.applicationResources.css().disabledButton());
	}
	
	// ==================
	//  TEST DATA
	// ==================
	private void addOracleSuggestions(MultiWordSuggestOracle oracle) {
		if(_application.isHostedMode()) {
//			oracle.add(UrlUtils.getUrlAndRoot() + "tests/index.html");
//			oracle.add(UrlUtils.getUrlAndRoot() + "tests/gene6606.html");
//			oracle.add(UrlUtils.getUrlAndRoot() + "tests/OMIM253300.html");
//			oracle.add(UrlUtils.getUrlAndRoot() + "tests/OMIM600354.html");
//			oracle.add(UrlUtils.getUrlAndRoot() + "tests/PM10679938.html");
//			oracle.add(UrlUtils.getUrlAndRoot() + "tests/PM17561409.html");
//			
//			oracle.add(UrlUtils.getUrlAndRoot() + "tests/PMC3308009_v082012.html");
//			
//			oracle.add(UrlUtils.getUrlAndRoot() + "tests/PMC3639628.html");
//			oracle.add(UrlUtils.getUrlAndRoot() + "tests/PMC2759694.html");
//			oracle.add(UrlUtils.getUrlAndRoot() + "tests/PMC2759694_v062012.html");
//			oracle.add(UrlUtils.getUrlAndRoot() + "tests/PMC2700002.html");
//			oracle.add(UrlUtils.getUrlAndRoot() + "tests/PMC2700002_v062012.html");
//			oracle.add(UrlUtils.getUrlAndRoot() + "tests/PMC2799499.html");
//			oracle.add(UrlUtils.getUrlAndRoot() + "tests/PMC2799499_v062012.html");
//			oracle.add(UrlUtils.getUrlAndRoot() + "tests/PMC2714656.html");
//			oracle.add(UrlUtils.getUrlAndRoot() + "tests/PMC2714656_v062012.html");
//			oracle.add(UrlUtils.getUrlAndRoot() + "tests/NCT000001549.html");
//			oracle.add(UrlUtils.getUrlAndRoot() + "tests/NCT00368199.html");
//			oracle.add(UrlUtils.getUrlAndRoot() + "tests/NCT01136213.html");
//			oracle.add(UrlUtils.getUrlAndRoot() + "tests/ImagesTest.html");
			oracle.add(UrlUtils.getUrlAndRoot() + "tests/SPL-annotation/Warfarin-ab047628-67d0-4a64-8d77-36b054969b44.html");
			oracle.add(UrlUtils.getUrlAndRoot() + "tests/DDI-labels/4e338e89-3cf2-48eb-b6e2-a06c608c6513.html");
						
		} else {
			if(ApplicationUtils.getProvideExamplesUrls()!=null && ApplicationUtils.getProvideExamplesUrls().trim().equals("true")) {
				oracle.add("http://en.wikipedia.org/wiki/Amyloid_precursor_protein");
				oracle.add("http://www.stembook.org/node/514");
				oracle.add("http://www.jbiomedsem.com/content/3/S1/S1");
				oracle.add("http://www.jbiomedsem.com/content/2/S2/S4");
				oracle.add("http://www.ncbi.nlm.nih.gov/pmc/articles/PMC3639628/");
				oracle.add("http://www.ncbi.nlm.nih.gov/pmc/articles/PMC3308009/");
				oracle.add("http://www.ncbi.nlm.nih.gov/pmc/articles/PMC3059018/");
				oracle.add("http://www.ncbi.nlm.nih.gov/pmc/articles/PMC2700002/");
				oracle.add("http://www.ncbi.nlm.nih.gov/pmc/articles/PMC2799499/");
				oracle.add("http://www.ncbi.nlm.nih.gov/pubmed/10679938");
				oracle.add("http://www.ncbi.nlm.nih.gov/pubmed/17561409");
				oracle.add("http://www.ncbi.nlm.nih.gov/pubmed/7758106");
				// Many citations (about 300)
				oracle.add("http://www.ncbi.nlm.nih.gov/pmc/articles/PMC2714656/");
			}
			/*
			if(_application.isStandaloneMode()) {
				// Not sure what to do here for copyright reasons
				//Window.alert(UrlUtils.getUrlBase());
				//Window.alert(UrlUtils.getUrlAndRoot());
			}
			if(_application.isTestFilesOn()) {
				oracle.add(UrlUtils.getUrlBase() + "tests/index.html");
				oracle.add(UrlUtils.getUrlBase() + "tests/gene6606.html");
				oracle.add(UrlUtils.getUrlBase() + "tests/OMIM253300.html");
				oracle.add(UrlUtils.getUrlBase() + "tests/OMIM600354.html");
				oracle.add(UrlUtils.getUrlBase() + "tests/PM10679938.html");
				oracle.add(UrlUtils.getUrlBase() + "tests/PM17561409.html");
				oracle.add(UrlUtils.getUrlBase() + "tests/PMC2759694.html");
				oracle.add(UrlUtils.getUrlBase() + "tests/PMC2759694_v062012.html");
				oracle.add(UrlUtils.getUrlBase() + "tests/PMC2700002.html");
				oracle.add(UrlUtils.getUrlBase() + "tests/PMC2700002_v062012.html");
				oracle.add(UrlUtils.getUrlBase() + "tests/PMC2799499.html");
				oracle.add(UrlUtils.getUrlBase() + "tests/PMC2799499_v062012.html");
				oracle.add(UrlUtils.getUrlBase() + "tests/PMC2714656.html");
				oracle.add(UrlUtils.getUrlBase() + "tests/PMC2714656_v062012.html");
				oracle.add(UrlUtils.getUrlBase() + "tests/NCT000001549.html");
				oracle.add(UrlUtils.getUrlBase() + "tests/NCT00368199.html");
				oracle.add(UrlUtils.getUrlBase() + "tests/NCT01136213.html");
				oracle.add(UrlUtils.getUrlBase() + "tests/ImagesTest.html");
			}
			*/
		}
	}

	@Override
	public void hide() {
		main.setVisible(false);
	}

	@Override
	public void show() {
		main.setVisible(true);
	}
}
