package org.mindinformatics.gwt.framework.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

public class EditableLabel extends Composite implements HasValue<String> {

	private static EditableLabelUiBinder uiBinder = 
		GWT.create(EditableLabelUiBinder.class);

	interface EditableLabelUiBinder extends UiBinder<Widget, EditableLabel> {}

	private boolean readOnly;
	
	@UiField protected Label editLabel;
	@UiField protected DeckPanel deckPanel;
	@UiField protected TextArea editBox;
	@UiField protected FocusPanel focusPanel;

	public EditableLabel() {
		initWidget(uiBinder.createAndBindUi(this));

		deckPanel.showWidget(0);

		focusPanel.addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				switchToEdit();
			}
		});

		editLabel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				switchToEdit();
			}
		});

		editBox.addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				switchToLabel();
			}
		});

		editBox.addKeyPressHandler(new KeyPressHandler() {

			@Override
			public void onKeyPress(KeyPressEvent event) {

				if (event.getCharCode() == KeyCodes.KEY_ENTER) {
					switchToLabel();
				} else if (event.getCharCode() == KeyCodes.KEY_ESCAPE) {
					editBox.setText(editLabel.getText()); // reset to the
															// original value
				}
			}
		});
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	public void switchToEdit() {
		if(readOnly) return;
		int height = editLabel.getOffsetHeight();
		if (deckPanel.getVisibleWidget() == 1)
			return;
		editBox.setText(getValue());
		editBox.setHeight(height + "px");
		deckPanel.showWidget(1);
		editBox.setFocus(true);
	}

	public void switchToLabel() {
		if (deckPanel.getVisibleWidget() == 0)
			return;
		setValue(editBox.getText(), true); // fires events, too
		deckPanel.showWidget(0);
	}

	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<String> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	@Override
	public String getValue() {
		return editLabel.getText();
	}

	@Override
	public void setValue(String value) {
		editLabel.setText(value);
		editBox.setText(value);
	}

	@Override
	public void setValue(String value, boolean fireEvents) {
		if (fireEvents)
			ValueChangeEvent.fireIfNotEqual(this, getValue(), value);
		setValue(value);
	}
}
