package org.mindinformatics.gwt.domeo.plugins.annotation.spls.ui.tile;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.mindinformatics.gwt.domeo.client.Domeo;
import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.Resources;
import org.mindinformatics.gwt.domeo.client.ui.annotation.actions.ActionCommentAnnotation;
import org.mindinformatics.gwt.domeo.client.ui.annotation.actions.ActionDeleteAnnotation;
import org.mindinformatics.gwt.domeo.client.ui.annotation.actions.ActionEditAnnotation;
import org.mindinformatics.gwt.domeo.client.ui.annotation.actions.ActionShowAnnotation;
import org.mindinformatics.gwt.domeo.client.ui.annotation.actions.IAnnotationEditListener;
import org.mindinformatics.gwt.domeo.client.ui.annotation.tiles.ATileComponent;
import org.mindinformatics.gwt.domeo.client.ui.annotation.tiles.ITileComponent;
import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.model.MOnlineImage;
import org.mindinformatics.gwt.domeo.model.selectors.MAnnotationSelector;
import org.mindinformatics.gwt.domeo.model.selectors.MTargetSelector;
import org.mindinformatics.gwt.domeo.model.selectors.SelectorUtils;
import org.mindinformatics.gwt.domeo.plugins.annotation.highlight.model.MHighlightAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.spls.info.SPLsPlugin;
import org.mindinformatics.gwt.domeo.plugins.annotation.spls.model.MPharmgx;
import org.mindinformatics.gwt.domeo.plugins.annotation.spls.model.MSPLsAnnotation;
import org.mindinformatics.gwt.framework.component.preferences.src.BooleanPreference;
import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Provides the standard lens for the annotation type Highlight.
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class TSPLsTile extends ATileComponent implements ITileComponent {

	interface Binder extends UiBinder<Widget, TSPLsTile> {
	}

	private static final Binder binder = GWT.create(Binder.class);
	private static int MAX_STATEMENT = 65;
	// By contract
	private MSPLsAnnotation _annotation;

	@UiField
	VerticalPanel body;
	@UiField
	HorizontalPanel provenance;
	// @UiField FlowPanel content;
	// @UiField HTML icon;
	@UiField
	HTML description;

	public TSPLsTile(IDomeo domeo, IAnnotationEditListener listener) {
		super(domeo, listener);

		initWidget(binder.createAndBindUi(this));

		tileResources.css().ensureInjected();
	}

	public MAnnotation getAnnotation() {
		return _annotation;
	}

	@Override
	public void initializeLens(MAnnotation annotation) {
		try {
			_annotation = (MSPLsAnnotation) annotation;
			refresh();
		} catch (Exception e) {
			_domeo.getLogger().exception(this, e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public Widget getTile() {
		return this;
	}

	class Label {
		private String name;
		private String value;

		Label(String name, String value) {
			this.name = name;
			this.value = value;
		}

		Label() {
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

	}

	@Override
	public void refresh() {
		try {

			String annoType = _annotation.getAnnotationType();
			int colon = annoType.indexOf(":");
			annoType = annoType.substring(colon + 1);

			createProvenanceBar(SPLsPlugin.getInstance().getPluginName(),
					provenance, annoType, _annotation);

			StringBuffer sb2 = new StringBuffer();

			MPharmgx pharmgx = _annotation.getPharmgxUsage().getPharmgx();

			ArrayList<Label> labels = new ArrayList<Label>();

			String html1 = "<html><head></head> <body><table>";

			String html2 = "</table></body></html>";

			String biomarkerStr;
			if (pharmgx.getBiomarkers() != null) {
				biomarkerStr = pharmgx.getBiomarkers().getLabel();
				Label l = new Label("bio", biomarkerStr);
				labels.add(l);
			}

			String pkimpactStr;
			if (pharmgx.getPkImpactResource() != null) {
				pkimpactStr = pharmgx.getPkImpactResource().getLabel();
				if (!pkimpactStr.equals("None")) {
					Label l = new Label("pk", pkimpactStr);
					labels.add(l);
				}
			}

			String pdimpackStr;
			if (pharmgx.getPdImpactResource() != null) {
				pdimpackStr = pharmgx.getPdImpactResource().getLabel();
				if (!pdimpackStr.equals("None")) {
					Label l = new Label("pd", pdimpackStr);
					labels.add(l);
				}
			}

			String drugRecStr;
			if (pharmgx.getDrugRecResource() != null) {
				drugRecStr = pharmgx.getDrugRecResource().getLabel();
				if (!drugRecStr.equals("None")) {
					Label l = new Label("drug", drugRecStr);
					labels.add(l);
				}
			}

			String doseRecStr;
			if (pharmgx.getDoseRecResource() != null) {
				doseRecStr = pharmgx.getDoseRecResource().getLabel();
				if (!doseRecStr.equals("None")) {
					Label l = new Label("dose", doseRecStr);
					labels.add(l);
				}
			}

			String monRecStr;
			if (pharmgx.getMonitRecResource() != null) {
				monRecStr = pharmgx.getMonitRecResource().getLabel();
				if (!monRecStr.equals("None")) {
					Label l = new Label("Monit", monRecStr);
					labels.add(l);
				}
			}

			String testRecStr;
			if (pharmgx.getTestRecResource() != null) {
				testRecStr = pharmgx.getTestRecResource().getLabel();
				if (!testRecStr.equals("None")) {
					Label l = new Label("testRec", testRecStr);
					labels.add(l);
				}
			}

			String variantStr;
			if (pharmgx.getVarient() != null) {
				variantStr = pharmgx.getVarient().getLabel();
				Label l = new Label("Variant", variantStr);
				labels.add(l);
			}

			String testStr;
			if (pharmgx.getTest() != null) {
				testStr = pharmgx.getTest().getLabel();
				Label l = new Label("test", testStr);
				labels.add(l);
			}

			String allelesStr = "";
			if (pharmgx.getAlleles() != null) {
				allelesStr = pharmgx.getAlleles().getLabel();
			}

			String medicalStr = "";
			if (pharmgx.getMedicalCondition() != null) {
				medicalStr = pharmgx.getMedicalCondition().getLabel();
			}

			String statementsStr = "";
			if (_annotation.getPharmgxUsage().getStatements() != null) {
				int count = 2;
				for (MLinkedResource mr : _annotation.getPharmgxUsage()
						.getStatements()) {
					statementsStr += mr.getLabel().toString() + " ";
					if (count <= 0)
						break;
					count++;
				}

				Label l = new Label("state", statementsStr);
				labels.add(l);
			}

			StringBuffer tdHtml = new StringBuffer();

			tdHtml.append("<tr bgcolor='#F0F8FF'>");
			for (Label label : labels) {
				tdHtml.append("<td>" + label.getName() + "</td>");
			}
			if ((!allelesStr.equals("") && allelesStr != null)
					|| (!medicalStr.equals("") && medicalStr != null)) {
				tdHtml.append("<td>others</td>");
			}
			tdHtml.append("</tr>");

			sb2.append(html1);

			sb2.append(tdHtml);

			sb2.append(html2);
			System.out.println(sb2.toString());

			description.setHTML(sb2.toString());

		} catch (Exception e) {
			_domeo.getLogger().exception(this, e.getMessage());
		}
	}

	public void createProvenanceBar(String plugin, HorizontalPanel provenance,
			String prefix, final MAnnotation annotation) {
		int step = 0;
		try {
			Resources resource = Domeo.resources;
			Image editIcon = new Image(resource.editLittleIcon());
			editIcon.setTitle("Edit Item");
			if (_domeo.getProfileManager().getUserCurrentProfile()
					.isPluginEnabled(plugin)) {
				editIcon.setStyleName(ATileComponent.tileResources.css()
						.button());
				editIcon.addClickHandler(ActionEditAnnotation.getClickHandler(
						_domeo, this, _listener, getAnnotation()));
			}
			step = 1;

			Image commentIcon = null;
			if (((BooleanPreference) _domeo.getPreferences().getPreferenceItem(
					Domeo.class.getName(), Domeo.PREF_ALLOW_COMMENTING))
					.getValue()) {
				commentIcon = new Image(resource.littleCommentIcon());
				commentIcon.setTitle("Comment on Item");
				commentIcon.setStyleName(ATileComponent.tileResources.css()
						.button());
				commentIcon.addClickHandler(ActionCommentAnnotation
						.getClickHandler(_domeo, this, annotation));
			}
			step = 2;

			Image showIcon = new Image(resource.showLittleIcon());
			showIcon.setTitle("Show Item in Context");
			showIcon.setStyleName(ATileComponent.tileResources.css().button());
			showIcon.addClickHandler(ActionShowAnnotation.getClickHandler(
					_domeo, this, getAnnotation()));
			step = 3;

			Image deleteIcon = new Image(resource.deleteLittleIcon());
			deleteIcon.setTitle("Delete Item");
			deleteIcon
					.setStyleName(ATileComponent.tileResources.css().button());
			deleteIcon.addClickHandler(ActionDeleteAnnotation.getClickHandler(
					_domeo, this, getAnnotation()));
			step = 4;

			// TODO move to an abstract tile class
			if (((BooleanPreference) _domeo.getPreferences().getPreferenceItem(
					Domeo.class.getName(), Domeo.PREF_DISPLAY_PROVENANCE))
					.getValue()) {
				if (annotation
						.getCreator()
						.getUri()
						.equals(_domeo.getAgentManager().getUserPerson()
								.getUri())) {
					if (((BooleanPreference) _domeo.getPreferences()
							.getPreferenceItem(Domeo.class.getName(),
									Domeo.PREF_DISPLAY_USER_PROVENANCE))
							.getValue()) {
						provenance.clear();
						step = 5;
						// TODO Externalize the icon management to the plugins
						if (SelectorUtils.isOnMultipleTargets(annotation
								.getSelectors())) {
							Image ic = new Image(
									Domeo.resources.multipleLittleIcon());
							ic.setTitle("Annotation on multiple targets");
							provenance.add(ic);
							provenance.setCellWidth(ic, "18px");
						} else if (annotation.getSelector() != null
								&& annotation.getSelector().getTarget() instanceof MOnlineImage) {
							Image ic = new Image(
									Domeo.resources.littleImageIcon());
							ic.setTitle("Annotation on image");
							provenance.add(ic);
							provenance.setCellWidth(ic, "18px");
						} else {
							if (SelectorUtils.isOnAnnotation(annotation
									.getSelectors())) {
								Image ic = new Image(
										Domeo.resources.littleCommentIcon());
								ic.setTitle("Annotation on annotation");
								provenance.add(ic);
								provenance.setCellWidth(ic, "18px");
							} else if (SelectorUtils
									.isOnResourceTarget(annotation
											.getSelectors())) {
								Image ic = new Image(
										Domeo.resources.littleCommentsIcon());
								ic.setTitle("Annotation on annotation");
								provenance.add(ic);
								provenance.setCellWidth(ic, "18px");
							} else {
								Image ic = new Image(
										Domeo.resources.littleTextIcon());
								ic.setTitle("Annotation on text");
								provenance.add(ic);
								provenance.setCellWidth(ic, "18px");
							}
						}
						step = 6;

						provenance
								.add(new HTML(
										"<span style='font-weight: bold; font-size: 12px; color: #696969'>"
												+ prefix
												+ " by "
												+ annotation.getCreator()
														.getName()
												+ "</span>  <span style='padding-left:5px; font-size: 12px; color: #696969' title='"
												+ annotation
														.getFormattedCreationDate()
												+ "'>"
												+ elaspedTime((new Date())
														.getTime()
														- annotation
																.getCreatedOn()
																.getTime())
												+ " ago</span>"));

						provenance.add(commentIcon);
						provenance.setCellHorizontalAlignment(commentIcon,
								HasHorizontalAlignment.ALIGN_LEFT);
						provenance.setCellWidth(commentIcon, "22px");

						if (!(annotation.getSelector() instanceof MTargetSelector)
								&& !(annotation.getSelector() instanceof MAnnotationSelector)) {
							if (!SelectorUtils.isOnMultipleTargets(annotation
									.getSelectors())) {
								provenance.add(showIcon);
								provenance.setCellWidth(showIcon, "22px");
							}
							if (SelectorUtils.isOnMultipleTargets(annotation
									.getSelectors())
									|| !(annotation instanceof MHighlightAnnotation)) {
								provenance.add(editIcon);
								provenance.setCellWidth(editIcon, "22px");
							}
						}

						provenance.add(deleteIcon);
						provenance.setCellHorizontalAlignment(deleteIcon,
								HasHorizontalAlignment.ALIGN_LEFT);
						provenance.setCellWidth(deleteIcon, "22px");
					} else {
						provenance.setVisible(false);
					}
				} else {
					provenance.clear();
					step = 8;
					if (SelectorUtils.isOnMultipleTargets(annotation
							.getSelectors())) {
						Image ic = new Image(
								Domeo.resources.multipleLittleIcon());
						ic.setTitle("Annotation on multiple targets");
						provenance.add(ic);
						provenance.setCellWidth(ic, "18px");
					} else if (annotation.getSelector() != null
							&& annotation.getSelector().getTarget() instanceof MOnlineImage) {
						Image ic = new Image(Domeo.resources.littleImageIcon());
						ic.setTitle("Annotation on image");
						provenance.add(ic);
						provenance.setCellWidth(ic, "18px");
					} else {
						Image ic = new Image(Domeo.resources.littleTextIcon());
						ic.setTitle("Annotation on text");
						provenance.add(ic);
						provenance.setCellWidth(ic, "18px");
					}

					step = 9;
					provenance
							.add(new HTML(
									"<span style='font-weight: bold; font-size: 12px; color: #696969'>"
											+ prefix
											+ " by "
											+ annotation.getCreator().getName()
											+ "</span>  <span style='padding-left:5px; font-size: 12px; color: #696969' title='"
											+ annotation
													.getFormattedCreationDate()
											+ "'>"
											+ elaspedTime((new Date())
													.getTime()
													- annotation.getCreatedOn()
															.getTime())
											+ " ago</span>"));
					// provenance.add(new Label("By " +
					// annotation.getCreator().getName() + " on " +
					// annotation.getFormattedCreationDate()));

					provenance.add(commentIcon);
					provenance.setCellHorizontalAlignment(commentIcon,
							HasHorizontalAlignment.ALIGN_LEFT);
					provenance.setCellWidth(commentIcon, "22px");
					if (!SelectorUtils.isOnMultipleTargets(annotation
							.getSelectors()))
						provenance.add(showIcon);
					provenance.add(editIcon);
					provenance.add(deleteIcon);
				}
			} else {
				provenance.setVisible(false);
			}
		} catch (Exception e) {
			_domeo.getLogger().exception(
					this,
					"Provenance bar generation exception @" + step + " "
							+ e.getMessage());
		}
	}

	// generate each statements for variables
	private static String addRecInHTML(String title, String label) {
		// System.out.println("****" + title + " | " + label);
		String str = "<tr bgcolor='#DEDEDE'><td>clinical statements from SPLs</td></tr>";
		str += "<tr><td>" + title;
		if (label.length() >= MAX_STATEMENT)
			label = label.substring(0, MAX_STATEMENT);
		str += label + "</td></tr>";
		return str;
	}
}
