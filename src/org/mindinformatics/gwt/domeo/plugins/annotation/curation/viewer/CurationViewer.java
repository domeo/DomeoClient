package org.mindinformatics.gwt.domeo.plugins.annotation.curation.viewer;

import java.util.ArrayList;
import java.util.List;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.framework.src.IContainerPanel;
import org.mindinformatics.gwt.framework.src.IContentPanel;
import org.mindinformatics.gwt.framework.src.IRefresh;
import org.mindinformatics.gwt.framework.src.IResizable;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class CurationViewer extends Composite implements IContentPanel, IRefresh, IResizable {

    public static final String TITLE = "Curation Viewer";
    public String getTitle() { return TITLE; }
    
    interface Binder extends UiBinder<HorizontalPanel, CurationViewer> { } 
    private static final Binder binder = GWT.create(Binder.class);

    @UiField HorizontalPanel mainPanel;
    @UiField VerticalPanel menuPanel;
    @UiField VerticalPanel leftPanel;
    @UiField VerticalPanel contextContainer;
    @UiField HTMLPanel contextPanel;
    @UiField HTMLPanel prefixText;
    @UiField HTMLPanel matchText;
    @UiField HTMLPanel suffixText;
    @UiField VerticalPanel curationPanel;
    @UiField TextArea curationComment;
    @UiField HorizontalPanel curationCommands;
    @UiField HorizontalPanel curationActions;
    @UiField VerticalPanel annotationItemPanel;
    @UiField VerticalPanel existingCurationPanel;
    @UiField SimplePanel existingCurationTablePanel;
    
    private IDomeo _domeo;
    private IContainerPanel _containerPanel;
    
    private MAnnotation _item;
    private ArrayList<? extends MAnnotation> _items;
    private Element _span;
    private ArrayList<Element> _spans;
    private String _curationAction;
    
    public CurationViewer(IDomeo domeo, MAnnotation item, Element span) {
        _domeo = domeo; _item = item; _span = span;
        _curationAction = "";

        initWidget(binder.createAndBindUi(this));
//      this.setWidth((Window.getClientWidth() - 140) + "px");
      
        refresh();
    }
    
    public CurationViewer(IDomeo domeo, MAnnotation item, String curationAction, 
            ArrayList<? extends MAnnotation> items, ArrayList<Element> spans) {
        _domeo = domeo; _items = items; _spans = spans; _item = item;
        _curationAction = curationAction;
        _span = null;
        
        initWidget(binder.createAndBindUi(this));
        
        refresh();
    }
    
    public void refreshItemsList() {
        try {
            menuPanel.clear();
            List<VerticalPanel> l = new ArrayList<VerticalPanel>();
            
//            // Organize text mining results
//            ProvenanceElementAnnotationManager provenanceElementAutomaticAnnotationManager = 
//                new ProvenanceElementAnnotationManager();
//            for (int i = 0; i < _items.size(); i++) {
//                if (_items.get(i) instanceof AnnotationItemDTO && _items.get(i).getCreator() instanceof AgentSoftwareDTO) {
//                    provenanceElementAutomaticAnnotationManager.addItem(_items.get(i), _spans.get(i));
//                }
//            }
//            // Display header
//            ProveananceLensProvider plp = new ProveananceLensProvider(_annotator, (org.mindinformatics.gwt.annotator.client.ui.lenses.GenericAnnotationLensProvider.Resources) _resources);
//            AnnotationQualifierLensProvider lqp = new AnnotationQualifierLensProvider(_annotator, (org.mindinformatics.gwt.annotator.client.ui.lenses.AnnotationQualifierLensProvider.Resources) _resources, 
//                    new AnnotationTermLensProvider((org.mindinformatics.gwt.annotator.client.ui.lenses.AnnotationTermLensProvider.Resources) _resources));
//            Set<Provenance> keys = provenanceElementAutomaticAnnotationManager.keySet();
//            for(Provenance p: keys) {
//                l.add(plp.getCurationHeader(p, false));
//                ArrayList<ElementAnnotation> ar = provenanceElementAutomaticAnnotationManager.get(p);
//                for(ElementAnnotation ac: ar) {
//                    l.add(getTextMiningAnnotationSkinnyElementPanel((AnnotationItemDTO) ac.getAnnotation(), ac.getSpan(), (ac.getAnnotation()==_item)));
//                }
//            }
//            Iterator<VerticalPanel> itEs = l.iterator();
//            while (itEs.hasNext()) {
//                menuPanel.add(itEs.next());
//            }
            
//            HorizontalPanel hp = new HorizontalPanel();
//            hp.setWidth("100%");
//            
//            ClickHandler ch = new ClickHandler() {
//                @Override
//                public void onClick(ClickEvent event) {
//                    Window.alert("Add qualifier not yet implemented!");
//                }
//            };
            /*
            Label label = new Label("Add qualifier");
            label.addClickHandler(ch);
            Image image = new Image(((Annotator.Resources)_resources).addIcon());
            image.addClickHandler(ch);
            hp.add(label);
            hp.setCellHorizontalAlignment(label, HasHorizontalAlignment.ALIGN_RIGHT);
            hp.add(image);
            hp.setCellWidth(image, "20px");
            menuPanel.add(hp);
            */
        } catch (Exception e) {
            Window.alert("refreshLeftSide()");
            Window.alert(e.getMessage());
        }
    }
    
    
    @Override
    public void refresh() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setContainer(IContainerPanel containerPanel) {
        _containerPanel = containerPanel;
    }

    @Override
    public IContainerPanel getContainer() {
        return _containerPanel;
    }

    @Override
    public void resized() {
        // TODO Auto-generated method stub
    }

//	public interface Resources extends ClientBundle {
//
//		@Source("org/mindinformatics/gwt/annotator/icons/ui/cross.png")
//		ImageResource crossIcon();
//		
//		@Source("org/mindinformatics/gwt/annotator/icons/ui/curation-accept.png")
//		ImageResource acceptIcon();
//		
//		@Source("org/mindinformatics/gwt/annotator/icons/ui/curation-accept-exact.png")
//		ImageResource acceptExactIcon();
//		
//		@Source("org/mindinformatics/gwt/annotator/icons/ui/curation-accept-broad.png")
//		ImageResource acceptBroadIcon();
//		
//		@Source("org/mindinformatics/gwt/annotator/icons/ui/curation-accept-narrow.png")
//		ImageResource acceptNarrowIcon();
//		
//		@Source("org/mindinformatics/gwt/annotator/icons/ui/curation-question.png")
//		ImageResource discussIcon();
//		
//		@Source("org/mindinformatics/gwt/annotator/icons/ui/curation-reject.png")
//		ImageResource rejectIcon();
//	}
//
//	private Resources _resources;
//	private IApplication _annotator;
//	private AnnotationCoreDTO _item;
//	private ArrayList<? extends AnnotationCoreDTO> _items;
//	private ArrayList<Element> _spans;
//	private Element _span;
//	private String _curationAction;
//	
//	private IGlassPanel _glassPanel;
//	public void setContainer(IGlassPanel glassPanel) { _glassPanel = glassPanel; }
//	public IGlassPanel getGlassPanel() { return _glassPanel; }
	

//	
//	private AnnotationCurationListTable table;
//	
//	public CurationViewer(IApplication annotator, Resources resources, AnnotationCoreDTO item, Element span) {
//		_annotator = annotator;
//		_resources = resources;
//		_item = item;
//		_span = span;
//		_curationAction = "";
//
//		initWidget(binder.createAndBindUi(this));
//		this.setWidth((Window.getClientWidth() - 140) + "px");
//		
//		refresh();
//	}
//	
//	public CurationViewer(IApplication annotator, Resources resources, AnnotationCoreDTO item, String curationAction, ArrayList<? extends AnnotationCoreDTO> items, ArrayList<Element> spans) {
//		_annotator = annotator;
//		_resources = resources;
//		_items = items;
//		_spans = spans;
//		_item = item;
//		_span = null;
//		_curationAction = curationAction;
//		
//		curationType = curationAction;
//
//		initWidget(binder.createAndBindUi(this));
//		this.setWidth((Window.getClientWidth() - 140) + "px");
//		
//		refresh();
//	}
//	
//	public CurationViewer(IApplication annotator, Resources resources, AnnotationCoreDTO item) {
//		_annotator = annotator;
//		_resources = resources;
//		_item = item;
//		_span = null;
//		_curationAction = "";
//		curationType = CurationTokenDTO.INCORRECT;
//
//		initWidget(binder.createAndBindUi(this));
//		this.setWidth((Window.getClientWidth() - 140) + "px");
//		
//		refresh();
//	}
//
//	
//	public void refresh() {
//		refreshLeftSide();
//		refreshRightSide(_item);
//	}
//	

//	
//	private void refreshRightSide(final AnnotationCoreDTO item) {
//		try {
//			prefixText.clear();
//			prefixText.getElement().setInnerHTML("... " + ((PrefixPostfixTextSelectorDTO)item.getSelector()).getPrefix() + " ");
//			
//			matchText.clear();
//			matchText.getElement().setInnerHTML(((PrefixPostfixTextSelectorDTO)item.getSelector()).getExact());
//			
//			suffixText.clear();
//			suffixText.getElement().setInnerHTML(" " + ((PrefixPostfixTextSelectorDTO)item.getSelector()).getSuffix() + " ...");
//			
//			curationComment.setText("");
//			annotationItemPanel.clear();
//			
//			createAnnotationSummary(item);
//			
//			table = new AnnotationCurationListTable(_annotator, (org.mindinformatics.gwt.annotator.client.Annotator.Resources)_resources, null, item);
//			table.setWidth("720px");
//			table.initializePanel();
//			table.updateTable();
//			existingCurationTablePanel.clear();
//			existingCurationTablePanel.add(table);
//			
//			curationActions.clear();
//			
//			final RadioButton rb0 = new RadioButton("myRadioGroup", "Selected instance only");
//			curationActions.add(rb0);
//			curationActions.setCellWidth(rb0, "160px");
//			rb0.setValue(true);
//			final RadioButton rb1 = new RadioButton("myRadioGroup", "All instances with same text");
//		    curationActions.add(rb1);
//			curationActions.setCellWidth(rb1, "200px");
//			final RadioButton rb2 = new RadioButton("myRadioGroup", "All instances");
//		    curationActions.add(rb2);
//			curationActions.setCellWidth(rb2, "120px");
//			
//			/*
//			final CheckBox checkBox = new CheckBox();
//			checkBox.setText("Apply to all with same text");
//			curationActions.add(checkBox);
//			curationActions.setCellWidth(checkBox, "200px");
//			
//			final CheckBox checkBox2 = new CheckBox();
//			checkBox2.setText("Apply to all");
//			curationActions.add(checkBox2);
//			curationActions.setCellWidth(checkBox2, "140px");
//			*/
//			
//			CustomButton applyButton = new CustomButton();
//			applyButton.setStyleName("cs-applyButton");
//			applyButton.setWidth("78px");
//			applyButton.setHeight("22px");
//			applyButton.setResource(((org.mindinformatics.gwt.annotator.client.Annotator.Resources)_resources).applyIcon());
//			applyButton.setText("Apply");
//			applyButton.addClickHandler(new ClickHandler() {
//				public void onClick(ClickEvent event) { 
//					if(rb2.getValue()) curateAllAnnotationItems(null, item, getCurationType(), curationComment.getText());
//					else if(rb1.getValue())  curateAllAnnotationItemsSameText(null, item, getCurationType(), curationComment.getText());
//					else curateAnnotationItem(null, item, getCurationType(), curationComment.getText());
//					refreshLeftSide();
//					table.updateTable();
//				}
//			});
//			curationActions.add(applyButton);
//			
//			CustomButton applyAndCloseButton = new CustomButton();
//			applyAndCloseButton.setStyleName("cs-applyButton");
//			applyAndCloseButton.setWidth("130px");
//			applyAndCloseButton.setHeight("22px");
//			applyAndCloseButton.setResource(((org.mindinformatics.gwt.annotator.client.Annotator.Resources)_resources).applyIcon());
//			applyAndCloseButton.setText("Apply and Close");
//			applyAndCloseButton.addClickHandler(new ClickHandler() {
//				public void onClick(ClickEvent event) { 
//					if(rb2.getValue()) curateAllAnnotationItems(null, item, getCurationType(), curationComment.getText());
//					else if(rb1.getValue())  curateAllAnnotationItemsSameText(null, item, getCurationType(), curationComment.getText());
//					else curateAnnotationItem(null, item, getCurationType(), curationComment.getText());
//					table.updateTable();
//					_glassPanel.hide();
//				}
//			});
//			curationActions.add(applyAndCloseButton);
//		} catch (Exception e) {
//			Window.alert("refreshRightSide()");
//			Window.alert(e.getMessage());
//		}
//	}
//	
//	private VerticalPanel getTextMiningAnnotationSkinnyElementPanel(final AnnotationItemDTO info,final Element span, boolean isPicked) {	
//		
//		AnnotationQualifierLensProvider l = new AnnotationQualifierLensProvider(_annotator, (org.mindinformatics.gwt.annotator.client.ui.lenses.AnnotationQualifierLensProvider.Resources) _resources, 
//				new AnnotationTermLensProvider((org.mindinformatics.gwt.annotator.client.ui.lenses.AnnotationTermLensProvider.Resources) _resources));
//		
//		VerticalPanel vp = new VerticalPanel();
//		vp.setWidth("100%");
//		vp.setStyleName("cs-bubbleContainer");
//		
//		HorizontalPanel hp = new HorizontalPanel();
//		hp.setWidth("100%");
//		
//		RadioButton rb = new RadioButton("itemsGroup");
//		rb.setValue(isPicked);
//		rb.addClickHandler(new ClickHandler() {
//			@Override
//			public void onClick(ClickEvent event) {
//				_item = info;
//				refreshRightSide(info);
//			}
//		});
//		
//		Image icon = null;
//		CurationTokenDTO ct = getLastCurationTokenByCurrentUser(info);
//		if(ct!=null) {
//			if(ct.getStatus().equals(ct.CORRECT)) {
//				icon = new Image(_resources.acceptIcon());
//			} else if(ct.getStatus().equals(ct.INCORRECT)) {
//				icon = new Image(_resources.rejectIcon());
//			} else if(ct.getStatus().equals(ct.CORRECT_BROAD)) {
//				icon = new Image(_resources.acceptBroadIcon());
//			} else if(ct.getStatus().equals(ct.CORRECT_NARROW)) {
//				icon = new Image(_resources.acceptNarrowIcon());
//			} else if(ct.getStatus().equals(ct.CORRECT_EXACT)) {
//				icon = new Image(_resources.acceptExactIcon());
//			} else if(ct.getStatus().equals(ct.UNCLEAR)) {
//				icon = new Image(_resources.discussIcon());
//			}
//		}
//		
//		hp.add(rb);
//		hp.setCellWidth(rb, "20px");
//		hp.add(l.getCurationSkinnyRow(info, icon));
//		
//		vp.add(hp);
//		return vp;
//	}
//	
//	private void curateAnnotationItem(String spanId, AnnotationCoreDTO item, String curationType, String comment) {
//		_annotator.curateAnnotationItem(spanId, item, curationType, comment);
//	}
//	
//	private void curateAllAnnotationItemsSameText(String spanId, AnnotationCoreDTO item, String curationType, String comment) {
//		// Find all the similar occurrences
//		AnnotationSetDTO set = item.getAnnotationSet();
//		for(AnnotationItemDTO it:set.getAnnotationItems()) {
//			if(((AnnotationItemDTO)item).getTopic().getUri().equals(((AnnotationItemDTO)it).getTopic().getUri()) &&
//					((TextSelectorDTO)item.getSelector()).getExact()
//					.equals(((TextSelectorDTO)it.getSelector()).getExact())) {
//				_annotator.curateAnnotationItem(spanId, it, curationType, comment);
//			}
//		}
//	}
//	
//	private void curateAllAnnotationItems(String spanId, AnnotationCoreDTO item, String curationType, String comment) {
//		// Find all the similar occurrences
//		AnnotationSetDTO set = item.getAnnotationSet();
//		for(AnnotationItemDTO it:set.getAnnotationItems()) {
//			if(((AnnotationItemDTO)item).getTopic().getUri().equals(((AnnotationItemDTO)it).getTopic().getUri())) {
//				_annotator.curateAnnotationItem(spanId, it, curationType, comment);
//			}
//		}
//	}
//	
//	private void createAnnotationSummary(final AnnotationCoreDTO item) {
//		if(item instanceof AnnotationItemDTO) { 
//			AnnotationTermLensProvider termLp = new AnnotationTermLensProvider((org.mindinformatics.gwt.annotator.client.ui.lenses.AnnotationTermLensProvider.Resources)_resources);
//			AnnotationQualifierLensProvider lp = new AnnotationQualifierLensProvider(_annotator, (org.mindinformatics.gwt.annotator.client.Annotator.Resources)_resources, termLp);
//			annotationItemPanel.add(lp.getSummaryRowWithUrl((AnnotationItemDTO)item, true));
//			
//			if(item.getCreator() instanceof AgentSoftwareDTO)  {
//				addSoftwareAnntotationCurationCommand();
//			}
//		}
//	}
//	
//	public String getCurationType() {
//		return curationType;
//	}
//	
//	private String curationType;
//	
//	private void addSoftwareAnntotationCurationCommand() {
//		curationCommands.clear();
//		
//		HorizontalPanel hp = new HorizontalPanel();
//		curationCommands.add(hp);
//		
//		if (_item.getCurationTokens() == null || _item.getCurationTokens().size() == 0) {
//			
//			boolean typeFlag = false;
//			
//			RadioButton rb1 = new RadioButton("curation");
//			if(_curationAction.equals(CurationTokenDTO.INCORRECT)) {
//				rb1.setValue(true);
//				curationComment.setFocus(true);
//				typeFlag = true;
//			}
//			rb1.addClickHandler(new ClickHandler() {
//
//				@Override
//				public void onClick(ClickEvent event) {
//					curationType = CurationTokenDTO.INCORRECT;
//				}
//					
//			});
//			CustomLabel b1 = new CustomLabel();
//			b1.setText("Wrong");
//			b1.setResource(_resources.rejectIcon());
//			hp.add(rb1);
//			hp.add(b1);
//			
//			
//			
//			RadioButton rb2 = new RadioButton("curation");
//			if(_curationAction.equals(CurationTokenDTO.CORRECT)) {
//				rb2.setValue(true);
//				curationComment.setFocus(true);
//				typeFlag = true;
//			}
//			rb2.addClickHandler(new ClickHandler() {
//
//				@Override
//				public void onClick(ClickEvent event) {
//					curationType = CurationTokenDTO.CORRECT;
//				}
//					
//			});
//			CustomLabel b2 = new CustomLabel();
//			b2.setText("Right");
//			b2.setResource(_resources.acceptIcon());
//			hp.add(rb2);
//			hp.add(b2);
//			
//			RadioButton rb4 = new RadioButton("curation");
//			if(_curationAction.equals(CurationTokenDTO.CORRECT_BROAD)) {
//				rb4.setValue(true);
//				curationComment.setFocus(true);
//				typeFlag = true;
//			}
//			rb4.addClickHandler(new ClickHandler() {
//
//				@Override
//				public void onClick(ClickEvent event) {
//					curationType = CurationTokenDTO.CORRECT_BROAD;
//				}
//					
//			});
//			CustomLabel b4 = new CustomLabel();
//			b4.setText("Too broad");
//			b4.setResource(_resources.acceptBroadIcon());
//			hp.add(rb4);
//			hp.add(b4);
//			
//			if(_annotator.getPreferencesManager().isAllowComplexCuration()) {
//				RadioButton rb3 = new RadioButton("curation");
//				if(_curationAction.equals(CurationTokenDTO.CORRECT_EXACT)) {
//					rb3.setValue(true);
//					curationComment.setFocus(true);
//					typeFlag = true;
//				}
//				rb3.addClickHandler(new ClickHandler() {
//
//					@Override
//					public void onClick(ClickEvent event) {
//						curationType = CurationTokenDTO.CORRECT_EXACT;
//					}
//						
//				});
//				CustomLabel b3 = new CustomLabel();
//				b3.setText("Exact Match");
//				b3.setResource(_resources.acceptExactIcon());
//				hp.add(rb3);
//				hp.add(b3);
//				
//				RadioButton rb5 = new RadioButton("curation");
//				if(_curationAction.equals(CurationTokenDTO.CORRECT_NARROW)) {
//					rb5.setValue(true);
//					curationComment.setFocus(true);
//					typeFlag = true;
//				}
//				rb5.addClickHandler(new ClickHandler() {
//
//					@Override
//					public void onClick(ClickEvent event) {
//						curationType = CurationTokenDTO.CORRECT_NARROW;
//					}
//						
//				});
//				CustomLabel b5 = new CustomLabel();
//				b5.setText("Narrow Match");
//				b5.setResource(_resources.acceptNarrowIcon());
//				hp.add(rb5);
//				hp.add(b5);
//			}
//			
//			RadioButton rb6 = new RadioButton("curation");
//			if(_curationAction.equals(CurationTokenDTO.UNCLEAR)) {
//				rb6.setValue(true);
//				curationComment.setFocus(true);
//				typeFlag = true;
//			}
//			rb6.addClickHandler(new ClickHandler() {
//
//				@Override
//				public void onClick(ClickEvent event) {
//					curationType = CurationTokenDTO.UNCLEAR;
//				}
//					
//			});
//			CustomLabel b6 = new CustomLabel();
//			b6.setText("Unclear");
//			b6.setWidth("80px");
//			b6.setResource(_resources.discussIcon());
//			hp.add(rb6);
//			hp.add(b6);
//			
//			if(!typeFlag) curationType = CurationTokenDTO.INCORRECT;
//		} else {
//			boolean typeFlag = false;
//			
//			RadioButton rb1 = new RadioButton("curation");
//			if(_curationAction.equals(CurationTokenDTO.INCORRECT)) {
//				rb1.setValue(true);
//				curationComment.setFocus(true);
//				typeFlag = true;
//			}
//			rb1.addClickHandler(new ClickHandler() {
//
//				@Override
//				public void onClick(ClickEvent event) {
//					curationType = CurationTokenDTO.INCORRECT;
//				}
//					
//			});
//			CustomLabel b1 = new CustomLabel();
//			b1.setText("Incorrect");
//			b1.setResource(_resources.rejectIcon());
//			hp.add(rb1);
//			hp.add(b1);
//			
//			if(getLastCurationToken(_item).getStatus().equals(CurationTokenDTO.INCORRECT))  
//				rb1.setEnabled(false);
//			
//			RadioButton rb2 = new RadioButton("curation");
//			if(_curationAction.equals(CurationTokenDTO.CORRECT)) {
//				rb2.setValue(true);
//				curationComment.setFocus(true);
//				typeFlag = true;
//			}
//			rb2.addClickHandler(new ClickHandler() {
//
//				@Override
//				public void onClick(ClickEvent event) {
//					curationType = CurationTokenDTO.CORRECT;
//				}
//					
//			});
//			CustomLabel b2 = new CustomLabel();
//			b2.setText("Correct");
//			b2.setResource(_resources.acceptIcon());
//			hp.add(rb2);
//			hp.add(b2);
//			
//			if(getLastCurationToken(_item).getStatus().equals(CurationTokenDTO.CORRECT))  
//				rb2.setEnabled(false);
//			
//			RadioButton rb4 = new RadioButton("curation");
//			if(_curationAction.equals(CurationTokenDTO.CORRECT_BROAD)) {
//				rb4.setValue(true);
//				curationComment.setFocus(true);
//				typeFlag = true;
//			}
//			rb4.addClickHandler(new ClickHandler() {
//
//				@Override
//				public void onClick(ClickEvent event) {
//					curationType = CurationTokenDTO.CORRECT_BROAD;
//				}
//					
//			});
//			CustomLabel b4 = new CustomLabel();
//			b4.setText("Too broad");
//			b4.setResource(_resources.acceptBroadIcon());
//			hp.add(rb4);
//			hp.add(b4);
//			
//			if(getLastCurationToken(_item).getStatus().equals(CurationTokenDTO.CORRECT_BROAD))  
//				rb4.setEnabled(false);
//		
//			if(_annotator.getPreferencesManager().isAllowComplexCuration()) {
//				RadioButton rb3 = new RadioButton("curation");
//				if(_curationAction.equals(CurationTokenDTO.CORRECT_EXACT)) {
//					rb3.setValue(true);
//					curationComment.setFocus(true);
//					typeFlag = true;
//				}
//				rb3.addClickHandler(new ClickHandler() {
//
//					@Override
//					public void onClick(ClickEvent event) {
//						curationType = CurationTokenDTO.CORRECT_EXACT;
//					}
//						
//				});
//				CustomLabel b3 = new CustomLabel();
//				b3.setText("Exact Match");
//				b3.setResource(_resources.acceptExactIcon());
//				hp.add(rb3);
//				hp.add(b3);
//				
//				if(getLastCurationToken(_item).getStatus().equals(CurationTokenDTO.CORRECT_EXACT))  
//					rb3.setEnabled(false);
//				
//				RadioButton rb5 = new RadioButton("curation");
//				if(_curationAction.equals(CurationTokenDTO.CORRECT_NARROW)) {
//					rb5.setValue(true);
//					curationComment.setFocus(true);
//					typeFlag = true;
//				}
//				
//				rb5.addClickHandler(new ClickHandler() {
//
//					@Override
//					public void onClick(ClickEvent event) {
//						curationType = CurationTokenDTO.CORRECT_NARROW;
//					}
//						
//				});
//				CustomLabel b5 = new CustomLabel();
//				b5.setText("Narrow Match");
//				b5.setResource(_resources.acceptNarrowIcon());
//				hp.add(rb5);
//				hp.add(b5);
//				
//				if(getLastCurationToken(_item).getStatus().equals(CurationTokenDTO.CORRECT_NARROW))  
//					rb5.setEnabled(false);
//			}
//			
//			RadioButton rb6 = new RadioButton("curation");
//			if(_curationAction.equals(CurationTokenDTO.UNCLEAR)) {
//				rb6.setValue(true);
//				curationComment.setFocus(true);
//				typeFlag = true;
//			}
//			rb6.addClickHandler(new ClickHandler() {
//
//				@Override
//				public void onClick(ClickEvent event) {
//					curationType = CurationTokenDTO.UNCLEAR;
//				}
//					
//			});
//			CustomLabel b6 = new CustomLabel();
//			b6.setText("Unclear");
//			b6.setWidth("80px");
//			b6.setResource(_resources.discussIcon());
//			hp.add(rb6);
//			hp.add(b6);	
//			
//			if(getLastCurationToken(_item).getStatus().equals(CurationTokenDTO.DISCUSSED))  
//				rb6.setEnabled(false);
//			
//			if(!typeFlag) curationType = CurationTokenDTO.INCORRECT;
//		}
//		
//		 
//	}
//	
//	private CurationTokenDTO getLastCurationTokenByCurrentUser(AnnotationCoreDTO info) {
//		CurationTokenDTO curation = null;
//		for (CurationTokenDTO token : info.getCurationTokens()) {
//			if (curation == null)
//				curation = token;
//			else {
//				if (token.getCuratedOn().compareTo(curation.getCuratedOn()) >= 0) {
//					curation = token;
//				}
//			}
//		}
//		return curation;
//	}
//	
//	private CurationTokenDTO getLastCurationToken(AnnotationCoreDTO info) {
//		CurationTokenDTO curation = null;
//		for (CurationTokenDTO token : info.getCurationTokens()) {
//			if (curation == null)
//				curation = token;
//			else {
//				if (token.getCuratedOn().compareTo(curation.getCuratedOn()) >= 0) {
//					curation = token;
//				}
//			}
//		}
//		return curation;
//	}
}
