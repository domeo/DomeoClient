package org.mindinformatics.gwt.domeo.client.ui.annotation.forms.text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.ui.annotation.forms.AFormComponent;
import org.mindinformatics.gwt.domeo.client.ui.annotation.forms.IFormGenerator;
import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.model.buffers.HighlightedTextBuffer;
import org.mindinformatics.gwt.domeo.model.selectors.SelectorUtils;
import org.mindinformatics.gwt.domeo.plugins.annotation.expertstudy_pDDI.model.Mexpertstudy_pDDIAnnotation;
import org.mindinformatics.gwt.framework.component.resources.model.MGenericResource;
import org.mindinformatics.gwt.framework.src.IContainerPanel;
import org.mindinformatics.gwt.framework.src.IContentPanel;
import org.mindinformatics.gwt.framework.src.IResizable;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.TabLayoutPanel;

public class TextAnnotationFormsPanel extends ATextFormsManager implements
		IContentPanel, IResizable {

	private static final String TITLE = "Manual Annotation Creation";
	private static final String TITLE_EDIT = "Manual Annotation Editing";

	interface Binder extends UiBinder<FlowPanel, TextAnnotationFormsPanel> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	private IDomeo _domeo;
	private IContainerPanel _containerPanel;
	private String _title;

	private HighlightedTextBuffer highlightBuffer;
	private ArrayList<AFormComponent> forms = new ArrayList<AFormComponent>();

	@UiField
	FlowPanel main;
	@UiField
	HorizontalPanel header;
	@UiField
	SpanElement prefixText;
	@UiField
	SpanElement matchText;
	@UiField
	SpanElement suffixText;
	@UiField
	Image leftBottomSide;
	@UiField
	Image rightBottomSide;
	@UiField
	Image leftTopSide;
	@UiField
	Image rightTopSide;
	@UiField
	TabLayoutPanel tabToolsPanel;
	@UiField
	SpanElement footerSpan;

	public TextAnnotationFormsPanel(IDomeo domeo) {
		_domeo = domeo;

		initWidget(binder.createAndBindUi(this));
	}

	// ------------------------------------------------------------------------
	// EDITING OF ANNOTATIONS OF VARIOUS KIND
	// ------------------------------------------------------------------------
	public TextAnnotationFormsPanel(IDomeo domeo, final MAnnotation annotation,
			ArrayList<MAnnotation> existingAnnotationInTheTextSpan) {
		_domeo = domeo;
		_title = TITLE_EDIT;

		//System.out.println("TextAnnotationFormsPanel - EDITING OF ANNOTATIONS OF VARIOUS KIND");

		// Buffer the potential highlighted text

		highlightBuffer = new HighlightedTextBuffer(
				SelectorUtils.getMatch(annotation.getSelector()),
				SelectorUtils.getPrefix(annotation.getSelector()),
				SelectorUtils.getSuffix(annotation.getSelector()), null);

		/*
		 * high light drug mentions for DDI plugin
		 */
		String exact = highlightBuffer.getExact();
		String styledText = highlightDrugMentionsForDDI(annotation, exact);
		
		//System.out.println("added style to sentence span:" + styledText);
		highlightBuffer.setExact(styledText);
		
		

		initWidget(binder.createAndBindUi(this));
		this.setWidth((Window.getClientWidth() - 140) + "px");

		refreshHighlightedText();
		
		// removes CSS style in highlightBuffer
		highlightBuffer.setExact(exact);

		tabToolsPanel.setWidth((Window.getClientWidth() - 140) + "px");
		tabToolsPanel.setHeight((Window.getClientHeight() - 240) + "px");

		IFormGenerator formGenerator = _domeo.getAnnotationFormsManager()
				.getAnnotationForm(annotation.getClass().getName());

		if (formGenerator != null) {
			AFormComponent form = formGenerator.getForm(this, annotation);
			tabToolsPanel.add(form, form.getTitle());
			forms.add(form);
		}

		// leftBottomSide.setVisible(false);
		leftBottomSide.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				int counter = 0;
				String prefix = highlightBuffer.getPrefix();

				while (counter < prefix.length()
						&& prefix
								.substring(prefix.length() - counter - 2,
										prefix.length() - counter - 1).trim()
								.length() == 0) {
					counter++;
				}

				String buffer = prefix.substring(prefix.length() - counter - 1);
				highlightBuffer.setPrefix(prefix.substring(0, prefix.length()
						- counter - 1));
				highlightBuffer.setExact(buffer + highlightBuffer.getExact());

				refreshHighlightedText();
			}
		});

		// rightBottomSide.setVisible(false);
		rightBottomSide.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				int counter = 0;
				String prefix = highlightBuffer.getPrefix();
				String exact = highlightBuffer.getExact();

				while (counter < exact.length()
						&& exact.substring(0, counter).trim().length() == 0) {
					counter++;
				}

				String buffer = exact.substring(0, counter);
				highlightBuffer.setPrefix(prefix + buffer);
				highlightBuffer.setExact(exact.substring(counter));

				refreshHighlightedText();
			}
		});
		// leftTopSide.setVisible(false);
		leftTopSide.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				int counter = 0;
				String exact = highlightBuffer.getExact();

				while (counter < exact.length()
						&& exact.substring(exact.length() - counter - 2,
								exact.length() - counter - 1).trim().length() == 0) {
					counter++;
				}

				String buffer = exact.substring(exact.length() - counter - 1);
				highlightBuffer.setExact(exact.substring(0, exact.length()
						- counter - 1));
				highlightBuffer.setSuffix(buffer + highlightBuffer.getSuffix());

				refreshHighlightedText();
			}
		});
		// rightTopSide.setVisible(false);
		rightTopSide.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				int counter = 0;
				String suffix = highlightBuffer.getSuffix();
				String exact = highlightBuffer.getExact();

				while (counter < suffix.length()
						&& suffix.substring(0, counter).trim().length() == 0) {
					counter++;
				}

				String buffer = suffix.substring(0, counter);
				highlightBuffer.setExact(exact + buffer);
				highlightBuffer.setSuffix(suffix.substring(counter));

				refreshHighlightedText();
			}
		});

		Timer t = new Timer() {
			@Override
			public void run() {
				resized();
			}
		};

		t.schedule(200);
	}

	// ------------------------------------------------------------------------
	// CREATION OF ANNOTATIONS OF VARIOUS KIND
	// ------------------------------------------------------------------------
	public TextAnnotationFormsPanel(IDomeo domeo, final String exact,
			final String prefix, final String suffix, final Node node,
			ArrayList<MAnnotation> existingAnnotationInTheTextSpan) {
		_domeo = domeo;
		_title = TITLE;

		System.out.println("TextAnnotationFormsPanel - CREATION OF ANNOTATIONS OF VARIOUS KIND");
		// matchText.setId("exactmatch");

		// Buffer the potential highlighted text
		highlightBuffer = new HighlightedTextBuffer(exact, prefix, suffix, node);
		
		
		// Create layout
		initWidget(binder.createAndBindUi(this));

		this.setWidth((Window.getClientWidth() - 140) + "px");

		refreshHighlightedText();

		tabToolsPanel.setWidth((Window.getClientWidth() - 180) + "px");
		tabToolsPanel.setHeight((Window.getClientHeight() - 240) + "px");
		try {
			Collection<IFormGenerator> formGenerators = _domeo
					.getAnnotationFormsManager().getAnnotationFormGenerators();
			Iterator<IFormGenerator> it = formGenerators.iterator();
			while (it.hasNext()) {
				String pluginName = "";
				try {
					IFormGenerator g = it.next();
					pluginName = g.getClass().getName();
					AFormComponent form = g.getForm(this);
					forms.add(form);
					tabToolsPanel.add(form, form.getTitle());
				} catch (Exception e) {
					_domeo.getLogger().exception(this,
							"Exception while loading form: " + pluginName);
				}
			}
		} catch (Exception e) {
			_domeo.getLogger().exception(this,
					"Exception while loading form generators.");
			throw new RuntimeException(e);
		}

		leftBottomSide.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				int counter = 0;
				String prefix = highlightBuffer.getPrefix();

				while (counter < prefix.length()
						&& prefix
								.substring(prefix.length() - counter - 2,
										prefix.length() - counter - 1).trim()
								.length() == 0) {
					counter++;
				}

				String buffer = prefix.substring(prefix.length() - counter - 1);
				highlightBuffer.setPrefix(prefix.substring(0, prefix.length()
						- counter - 1));
				highlightBuffer.setExact(buffer + highlightBuffer.getExact());

				refreshHighlightedText();
			}
		});
		_domeo.getLogger().debug(this, "5");
		rightBottomSide.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				int counter = 0;
				String prefix = highlightBuffer.getPrefix();
				String exact = highlightBuffer.getExact();

				while (counter < exact.length()
						&& exact.substring(0, counter).trim().length() == 0) {
					counter++;
				}

				String buffer = exact.substring(0, counter);
				highlightBuffer.setPrefix(prefix + buffer);
				highlightBuffer.setExact(exact.substring(counter));

				refreshHighlightedText();
			}
		});
		leftTopSide.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				int counter = 0;
				String exact = highlightBuffer.getExact();

				while (counter < exact.length()
						&& exact.substring(exact.length() - counter - 2,
								exact.length() - counter - 1).trim().length() == 0) {
					counter++;
				}

				String buffer = exact.substring(exact.length() - counter - 1);
				highlightBuffer.setExact(exact.substring(0, exact.length()
						- counter - 1));
				highlightBuffer.setSuffix(buffer + highlightBuffer.getSuffix());

				refreshHighlightedText();
			}
		});
		rightTopSide.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				int counter = 0;
				String suffix = highlightBuffer.getSuffix();
				String exact = highlightBuffer.getExact();

				while (counter < suffix.length()
						&& suffix.substring(0, counter).trim().length() == 0) {
					counter++;
				}

				String buffer = suffix.substring(0, counter);
				highlightBuffer.setExact(exact + buffer);
				highlightBuffer.setSuffix(suffix.substring(counter));

				refreshHighlightedText();
			}
		});

		// Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
		// public void execute() {
		// resized();
		// }
		// });

		Timer t = new Timer() {
			@Override
			public void run() {
				resized();
			}
		};
		t.schedule(100);
	}

	public void refreshHighlightedText() {

		setHighlightedText(highlightBuffer.getPrefix(),
				highlightBuffer.getExact(), highlightBuffer.getSuffix());
	}

	public HighlightedTextBuffer getHighlight() {
		return highlightBuffer;
	}

	private void setHighlightedText(String prefix, String text, String suffix) {

		matchText.setId("exactmatch");

		prefixText.setInnerHTML(prefix + " ");

		matchText.setInnerHTML(text);

		suffixText.setInnerHTML(" " + suffix);
	}

	public String getTitle() {
		return _title;
	}

	@Override
	public void setContainer(IContainerPanel containerPanel) {
		_containerPanel = containerPanel;
	}

	@Override
	public IContainerPanel getContainer() {
		return _containerPanel;
	}

	public void displayMessage(String message) {
		footerSpan.setInnerHTML(message);
	}

	public void clearMessage() {
		footerSpan.setInnerHTML("");
	}

	@Override
	public void resized() {
		this.setWidth((Window.getClientWidth() - 140) + "px");
		tabToolsPanel.setHeight((Window.getClientHeight() - 240) + "px");
		tabToolsPanel.setWidth((Window.getClientWidth() - 140) + "px");

		for (AFormComponent form : forms) {
			((IResizable) form).resized();
		}
	}

	@Override
	public MGenericResource getResource() {
		return _domeo.getPersistenceManager().getCurrentResource();
	}

	@Override
	public void hideContainer() {
		_containerPanel.hide();
	}

	@Override
	public ArrayList<MAnnotation> getTargets() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getContainerWidth() {
		return main.getOffsetWidth();
	}

	public String highlightDrugMentionsForDDI(MAnnotation annotation,
			String exactText) {

		if (annotation.getAnnotationType().equals("ao:expertstudyAnnotation")) {
			Mexpertstudy_pDDIAnnotation annExpert = (Mexpertstudy_pDDIAnnotation) annotation;
			String drug1 = annExpert.getDrug1().getLabel();
			String drug2 = annExpert.getDrug2().getLabel();

			if (drug1 != null && !drug1.trim().equals("")
					&& exactText.contains(drug1)) {
				int index1 = exactText.indexOf(
						drug1);

				exactText = exactText.substring(0, index1)
						+ "<span style='background-color: #FFFF00'>" + drug1
						+ "</span>"
						+ exactText.substring(index1 + drug1.length());
			}

			if (drug2 != null && !drug2.trim().equals("")
					&& exactText.contains(drug2)) {
				int index2 = exactText.indexOf(
						drug2);

				exactText = exactText.substring(0, index2)
						+ "<span style='background-color: #FFFF00'>" + drug2
						+ "</span>"
						+ exactText.substring(index2 + drug2.length());
			}

		}

		return exactText;

	}
}
