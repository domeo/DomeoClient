package org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.ui.form;

import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.MMicroPublication;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.MMpRelationship;

public interface IEvidenceRelationshipChangeListener {

	public void updateEvidence(MMicroPublication element, MMpRelationship evidence, String originalType, String newType);
}
