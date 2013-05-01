package org.mindinformatics.gwt.domeo.client.ui.annotation.forms.text;

import org.mindinformatics.gwt.domeo.client.ui.annotation.forms.AFormsManager;
import org.mindinformatics.gwt.domeo.model.buffers.HighlightedTextBuffer;

public abstract class ATextFormsManager extends AFormsManager {

	public abstract HighlightedTextBuffer getHighlight();
}
