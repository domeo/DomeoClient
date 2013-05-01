package org.mindinformatics.gwt.domeo.plugins.annotation.nif.antibodies.ui;


/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
//public class AntibodyLensProvider extends GenericAnnotationLensProvider {
//
//	public static final String SWAN_ELEMENT_ROW_CSS = "cs-SwanSummaryRow";
//	public static final String SWAN_ELEMENT_ICON_CSS = "cs-SwanElementIcon";
//	public static final String SWAN_ELEMENT_DESCRIPTION_CSS = "cs-SwanElementDescription";
//	public static final String SWAN_ELEMENT_QUALIFIERS_CSS = "cs-SwanElementQualifiers";
//	
//	public static final String ACTION_ICON_CSS = "cs-ActionIcon";
//	
//	public static final String ICON_WIDTH = "16px";
//
//	AnnotationTermLensProvider _tremsLp;
//	
//
//	private PopupPanel _popup;
//	
//
//	
//	public AntibodyLensProvider(IApplication annotator, Resources resources) {
//		super(annotator,  resources);
//		_tremsLp = new AnnotationTermLensProvider((Annotator.Resources)_resources);
//	}
//	
//	public VerticalPanel getAntibodySummaryRow(final AntibodyAnnotationDTO element) {
//
//		int counter = 0;
//		StringBuffer sb = new StringBuffer();
//		Set<AnnotationTermDTO> protocols = element.getProtocols();
//		for(AnnotationTermDTO protocol: protocols) {
//			sb.append(_tremsLp.getTermLabel(protocol));
//			if(counter++<protocols.size()-1) sb.append(", ");
//		}
//		
//		String subject = "";
//		if(element.getSubject()!=null) {
//			subject = "; <b>Subject</b>:&nbsp;<a target=\"_blank\" href=\""+ element.getSubject().getUri() +"\">" + element.getSubject().getLabel() + "</a> ";
//		}
//		
//		String vendor = "";
//		if(element.getAntibodyUsage().getAntibody().getVendor()!=null) {
//			if(element.getAntibodyUsage().getAntibody().getVendorUrl()!=null) {
//				vendor = " (" + element.getAntibodyUsage().getAntibody().getVendor() + " #<a target='_blank' href='" + element.getAntibodyUsage().getAntibody().getVendorUrl()
//				+ "'>" + element.getAntibodyUsage().getAntibody().getCatalogNumber() + "</a>)";
//			} else {
//				vendor = " (" + element.getAntibodyUsage().getAntibody().getVendor() + " #" + element.getAntibodyUsage().getAntibody().getCatalogNumber() + ")";
//			}
//		}
//		
//		HTML l = new HTML(
//				"<b>Antibody</b>:&nbsp;<a target=\"_blank\" href=\"" + element.getAntibodyUsage().getAntibody().getUri() + "\">" +  getId(element.getAntibodyUsage().getAntibody().getUri()) +  "</a> - " 
//				+  element.getAntibodyUsage().getAntibody().getLabel() + vendor +
//				"; <b>Protocol(s)</b>:&nbsp; " + sb.toString() +
//				subject +
//				((element.getComment()!=null&&element.getComment().length()>0)?("; <b>Note</b>: <img src='" + (((Annotator.Resources)_resources).noteIcon().getURL()) + "' title='" + element.getComment() + "'/>"):""));
//		l.setStyleName("cs-annotationItem");
//		
//		FlowPanel fp = new FlowPanel();		
//		fp.add(getCurationHeader(element));
//		fp.add(l);
//		
//		VerticalPanel dataTable = new VerticalPanel();
//		dataTable.setStyleName(SWAN_ELEMENT_ROW_CSS);		
//		dataTable.add(fp);
//		return dataTable;
//	}
//	
//	private String getId(String uri) {
//		return uri.substring(uri.lastIndexOf('=')+1);
//	}
//	
//	public VerticalPanel getCurationSummaryRow(final AntibodyAnnotationDTO element, PopupPanel popup) {
//		
//		_popup = popup;
//		
//		int counter = 0;
//		StringBuffer sb = new StringBuffer();
//		Set<AnnotationTermDTO> protocols = element.getProtocols();
//		for(AnnotationTermDTO protocol: protocols) {
//			sb.append(_tremsLp.getTermLabel(protocol));
//			if(counter++<protocols.size()-1) sb.append(", ");
//		}
//		
//		String subject = "";
//		if(element.getSubject()!=null) {
//			subject = "; <b>Subject</b>:&nbsp;<a target=\"_blank\" href=\""+ element.getSubject().getUri() +"\">" + element.getSubject().getLabel() + "</a> ";
//		}
//		
//		String vendor = "";
//		if(element.getAntibodyUsage().getAntibody().getVendor()!=null) {
//			if(element.getAntibodyUsage().getAntibody().getVendorUrl()!=null) {
//				vendor = " (" + element.getAntibodyUsage().getAntibody().getVendor() + " #<a target='_blank' href='" + element.getAntibodyUsage().getAntibody().getVendorUrl()
//				+ "'>" + element.getAntibodyUsage().getAntibody().getCatalogNumber() + "</a>)";
//			} else {
//				vendor = " (" + element.getAntibodyUsage().getAntibody().getVendor() + " #" + element.getAntibodyUsage().getAntibody().getCatalogNumber() + ")";
//			}
//		}
//		
//		HTML l = new HTML(
//				"<b>Antibody</b>:&nbsp;<a target=\"_blank\" href=\"" + element.getAntibodyUsage().getAntibody().getUri() + "\">" +  getId(element.getAntibodyUsage().getAntibody().getUri()) +  "</a> - " 
//				+  element.getAntibodyUsage().getAntibody().getLabel() + vendor +
//				"; <b>Protocol(s)</b>:&nbsp; " + sb.toString() +
//				subject +
//				((element.getComment()!=null&&element.getComment().length()>0)?("; <b>Note</b>: <img src='" + (((Annotator.Resources)_resources).noteIcon().getURL()) + "' title='" + element.getComment() + "'/>"):""));
//		l.setStyleName("cs-annotationItem");
//		
//		FlowPanel fp = new FlowPanel();		
//		fp.add(getCurationHeader(element));
//		fp.add(l);
//		
//		VerticalPanel dataTable = new VerticalPanel();
//		dataTable.setStyleName(SWAN_ELEMENT_ROW_CSS);		
//		dataTable.add(fp);
//		return dataTable;
//	}
//	
//	public VerticalPanel getVerticalPanel(final AntibodyDTO element) {
//		VerticalPanel vp = new VerticalPanel();
//		vp.add(new HTML(element.label + " (" + element.vendor + " #" + element.catalogNumber + ")"));
//		return vp;
//	}
//	
//	public VerticalPanel getVerticalPanelName(final AntibodyDTO element) {
//		VerticalPanel vp = new VerticalPanel();
//		vp.add(new HTML("<a target='_blank' href='" + element.getUri() + "'>" + element.label + "</a>"));
//		return vp;
//	}
//	
//	public VerticalPanel getVerticalPanelVendor(final AntibodyDTO element) {
//		VerticalPanel vp = new VerticalPanel();
//		vp.add(new HTML(" (" + element.vendor + " <a target='_blank' href='" + element.vendorUrl + "'>#" + element.catalogNumber + "</a>)"));
//		return vp;
//	}
//	
//	/*
//	public VerticalPanel getDiscourseSummaryRow(final AntibodyAnnotationDTO element) {
//
//		String iconUrl=null;
//		if(element.getType().equals(SwanElementType.SWAN_CLAIM))
//			iconUrl = (((org.mindinformatics.gwt.annotator.client.Annotator.Resources) _resources).claimIcon().getURL());
//		else if(element.getType().equals(SwanElementType.SWAN_HYPOTHESIS))
//			iconUrl = (((org.mindinformatics.gwt.annotator.client.Annotator.Resources) _resources).hypothesisIcon().getURL());
//		else if(element.getType().equals(SwanElementType.SWAN_QUESTION))
//			iconUrl = (((org.mindinformatics.gwt.annotator.client.Annotator.Resources) _resources).rQuestionIcon().getURL());
//
//		String prefix=null;
//		if(element.getType().equals(SwanElementType.SWAN_CLAIM))
//			prefix = "laim";
//		else if(element.getType().equals(SwanElementType.SWAN_HYPOTHESIS))
//			prefix = "ypothesis";
//		else if(element.getType().equals(SwanElementType.SWAN_QUESTION))
//			prefix = "uestion";
//		
//		HTML l = new HTML(
//				"<img style='vertical-align: bottom;' src='" + iconUrl +"' height='16px'/><b>" + prefix + "</b>:&nbsp;" +
//				element.getDescription());
//		l.setStyleName("cs-annotationItem");
//		
//		//Label l = new Label(element.getDescription());
//		//l.setStyleName(SWAN_ELEMENT_DESCRIPTION_CSS);
//		
//		FlowPanel fp = new FlowPanel();		
//		fp.add(getHeader(element));
//		fp.add(l);
//		
//		VerticalPanel dataTable = new VerticalPanel();
//		dataTable.setStyleName(SWAN_ELEMENT_ROW_CSS);		
//		dataTable.add(fp);
//		return dataTable;
//	}
//	
//	public VerticalPanel getDiscourseViewerRow(final AntibodyAnnotationDTO element, final IRefresh refresh) {
//
//		String iconUrl=null;
//		if(element.getType().equals(SwanElementType.SWAN_CLAIM))
//			iconUrl = (((org.mindinformatics.gwt.annotator.client.Annotator.Resources) _resources).claimIcon().getURL());
//		else if(element.getType().equals(SwanElementType.SWAN_HYPOTHESIS))
//			iconUrl = (((org.mindinformatics.gwt.annotator.client.Annotator.Resources) _resources).hypothesisIcon().getURL());
//		else if(element.getType().equals(SwanElementType.SWAN_QUESTION))
//			iconUrl = (((org.mindinformatics.gwt.annotator.client.Annotator.Resources) _resources).rQuestionIcon().getURL());
//
//		String prefix=null;
//		if(element.getType().equals(SwanElementType.SWAN_CLAIM))
//			prefix = "laim";
//		else if(element.getType().equals(SwanElementType.SWAN_HYPOTHESIS))
//			prefix = "ypothesis";
//		else if(element.getType().equals(SwanElementType.SWAN_QUESTION))
//			prefix = "uestion";
//
//		
//		HTML l = new HTML(
//				"<img style='vertical-align: bottom;' src='" + iconUrl +"' height='16px'/><b>" + prefix + "</b>:&nbsp;" +
//				element.getDescription());
//		l.setStyleName("cs-annotationItem");
//		
//		FlowPanel fp = new FlowPanel();		
//		fp.add(getViewerHeader(element, refresh));
//		fp.add(l);
//		
//		VerticalPanel dataTable = new VerticalPanel();
//		dataTable.setStyleName(SWAN_ELEMENT_ROW_CSS);		
//		dataTable.add(fp);
//		return dataTable;
//	}
//	*/
//	
//	
//	
//	private FlexTable getCurationHeader(final AntibodyAnnotationDTO info) {
//		FlexTable ft = new FlexTable();
//		CellFormatter cellFormatter = ft.getCellFormatter();
//		
//		setIcon(info, ft, cellFormatter);
//		setBarCss(info, ft);
//		setProvenance(info, ft);
//		
//		
//		Image show = new Image(_resources.showIcon());
//		show.setStyleName(ACTION_ICON_CSS);
//		show.addClickHandler(new ClickHandler() {
//			public void onClick(ClickEvent event) {
//				_annotator.showAnnotation(Event.getCurrentEvent(), info);
//			}
//		});
//		ft.setWidget(0, 4, show);
//		cellFormatter.setWidth(0, 4, ICON_WIDTH);
//		
//		if(info.getAnnotationSet().isLocked()) {
//			Image delete = new Image(((Annotator.Resources)_resources).locked());
//			//delete.setStyleName(ACTION_ICON_CSS);
//			delete.setTitle("Annotation item is locked");
//			ft.setWidget(0, 5, delete);
//			cellFormatter.setWidth(0, 5, ICON_WIDTH);
//		} else {
//			if (_annotator.getCurrentUser().getId().equals(info.getCreator().getId())) {
//				if(info.getCurationTokens() == null || info
//							.getCurationTokens().size() == 0) {
//					Image edit = new Image(_resources.editIcon());
//					edit.setStyleName(ACTION_ICON_CSS);
//					edit.setTitle("Edit Swan Element");
//					edit.addClickHandler(new ClickHandler() {
//						public void onClick(ClickEvent event) {
//							_annotator.editAnnotation(info);
//							_popup.hide();
//						}
//					});
//					ft.setWidget(0, 5, edit);
//					cellFormatter.setWidth(0, 5, ICON_WIDTH);
//					
//					Image delete = new Image(_resources.deleteIcon());
//					delete.setStyleName(ACTION_ICON_CSS);
//					delete.setTitle("Delete Swan Element");
//					delete.addClickHandler(new ClickHandler() {
//						public void onClick(ClickEvent event) {
//							_annotator.removeAnnotationItem(info.getAnnotationSet(), info);
//							_popup.hide();
//						}
//					});
//					ft.setWidget(0, 6, delete);
//					cellFormatter.setWidth(0, 6, ICON_WIDTH);
//				} else {
//					Image edit = new Image(_resources.editIcon());
//					edit.setStyleName(ACTION_ICON_CSS);
//					edit.setTitle("Edit Swan Element");
//					edit.addClickHandler(new ClickHandler() {
//						public void onClick(ClickEvent event) {
//							_annotator.editAnnotation(info);
//							_popup.hide();
//						}
//					});
//					ft.setWidget(0, 4, edit);
//					cellFormatter.setWidth(0, 4, ICON_WIDTH);
//					
//					Image history = new Image(_resources.showHistory());
//					history.setStyleName(ACTION_ICON_CSS);
//					history.setTitle("Swan Element History");
//					history.addClickHandler(new ClickHandler() {
//						public void onClick(ClickEvent event) {
//							_annotator.showAnnotationItemHistory((AnnotationCoreDTO) info);
//							_popup.hide();
//						}
//					});
//					ft.setWidget(0, 5, history);
//					cellFormatter.setWidth(0, 5, ICON_WIDTH);					
//				}
//			} else {
//				Image history = new Image(_resources.showHistory());
//				history.setStyleName(ACTION_ICON_CSS);
//				history.setTitle("Swan Element History");
//				history.addClickHandler(new ClickHandler() {
//					public void onClick(ClickEvent event) {
//						_annotator.showAnnotationItemHistory((AnnotationCoreDTO) info);
//						_popup.hide();
//					}
//				});
//				ft.setWidget(0, 5, history);
//				cellFormatter.setWidth(0, 5, ICON_WIDTH);
//				ft.setText(0, 6, "(" + info.getCurationTokens().size() + ")");
//				cellFormatter.setWidth(0, 6, ICON_WIDTH);
//			}
//		}
//		
//		return ft;
//	}
//	
//	private FlexTable getViewerHeader(final SwanElementDTO info, final IRefresh _refresh) {
//		FlexTable ft = new FlexTable();
//		CellFormatter cellFormatter = ft.getCellFormatter();
//		//DOM.setStyleAttribute(hp1.getElement(), "display", "block");
//		
//		setIcon(info, ft, cellFormatter);
//		setBarCss(info, ft);
//		setProvenance(info, ft);
//		
//
//		if(info.getAnnotationSet().isLocked()) {
//			
//			Image show = new Image(_resources.showIcon());
//			show.setStyleName(ACTION_ICON_CSS);
//			show.setTitle("Show Swan Element in Document");
//			show.addClickHandler(new ClickHandler() {
//				public void onClick(ClickEvent event) {
//					_annotator.showAnnotation(Event.getCurrentEvent(), info);
//				}
//			});
//			ft.setWidget(0, 4, show);
//			cellFormatter.setWidth(0, 4, ICON_WIDTH);
//			
//			Image lock = new Image(((Annotator.Resources)_resources).locked());
//			//delete.setStyleName(ACTION_ICON_CSS);
//			lock.setTitle("Annotation item is locked");
//			ft.setWidget(0, 5, lock);
//			cellFormatter.setWidth(0, 5, ICON_WIDTH);
//		} else {
//			if (_annotator.getCurrentUser().getId().equals(info.getCreator().getId())
//					&& (info.getCurationTokens() == null || info
//							.getCurationTokens().size() == 0)) {
//		
//				Image show = new Image(_resources.showIcon());
//				show.setStyleName(ACTION_ICON_CSS);
//				show.setTitle("Show Swan Element in Document");
//				show.addClickHandler(new ClickHandler() {
//					public void onClick(ClickEvent event) {
//						_annotator.showAnnotation(Event.getCurrentEvent(), info);
//					}
//				});
//				ft.setWidget(0, 3, show);
//				cellFormatter.setWidth(0, 3, ICON_WIDTH);
//				
//				Image edit = new Image(_resources.editIcon());
//				edit.setStyleName(ACTION_ICON_CSS);
//				edit.setTitle("Edit Swan Element");
//				edit.addClickHandler(new ClickHandler() {
//					public void onClick(ClickEvent event) {
//						_annotator.editAnnotation(info, _refresh);
//					}
//				});
//				ft.setWidget(0, 4, edit);
//				cellFormatter.setWidth(0, 4, ICON_WIDTH);
//				
//				
//				Image delete = new Image(_resources.deleteIcon());
//				delete.setStyleName(ACTION_ICON_CSS);
//				delete.setTitle("Delete Swan Element");
//				delete.addClickHandler(new ClickHandler() {
//					public void onClick(ClickEvent event) {
//						_annotator.removeAnnotationItem(info.getAnnotationSet(), info);
//					}
//				});
//				ft.setWidget(0, 5, delete);
//				cellFormatter.setWidth(0, 5, ICON_WIDTH);
//			} else {
//				Image show = new Image(_resources.showIcon());
//				show.setStyleName(ACTION_ICON_CSS);
//				show.setTitle("Show Swan Element in Document");
//				show.addClickHandler(new ClickHandler() {
//					public void onClick(ClickEvent event) {
//						_annotator.showAnnotation(Event.getCurrentEvent(), info);
//					}
//				});
//				ft.setWidget(0, 3, show);
//				cellFormatter.setWidth(0, 3, ICON_WIDTH);
//				
//				Image edit = new Image(_resources.editIcon());
//				edit.setTitle("Edit Swan Element");
//				edit.setStyleName(ACTION_ICON_CSS);
//				edit.addClickHandler(new ClickHandler() {
//					public void onClick(ClickEvent event) {
//						_annotator.editAnnotation(info);
//					}
//				});
//				ft.setWidget(0, 4, edit);
//				cellFormatter.setWidth(0, 4, ICON_WIDTH);
//				
//				Image history = new Image(_resources.showHistory());
//				history.setStyleName(ACTION_ICON_CSS);
//				history.setTitle("Swan Element History");
//				history.addClickHandler(new ClickHandler() {
//					public void onClick(ClickEvent event) {
//						_annotator.showAnnotationItemHistory((AnnotationCoreDTO) info);
//						_popup.hide();
//					}
//				});
//				ft.setWidget(0, 5, history);
//				cellFormatter.setWidth(0, 5, ICON_WIDTH);
//				ft.setText(0, 6, "(" + info.getCurationTokens().size() + ")");
//				cellFormatter.setWidth(0, 6, ICON_WIDTH);
//				
//				/*
//				Image delete = new Image(_resources.deleteIcon());
//				delete.setStyleName(ACTION_ICON_CSS);
//				delete.setTitle("Delete Swan Element");
//				delete.addClickHandler(new ClickHandler() {
//					public void onClick(ClickEvent event) {
//						_annotator.removeAnnotationItem(info.getAnnotationSet(), info);
//					}
//				});
//				ft.setWidget(0, 6, delete);
//				cellFormatter.setWidth(0, 6, ICON_WIDTH);
//				*/
//			}
//		}
//		
//		return ft;
//	}
//	
//	private void setIcon(AnnotationCoreDTO info, FlexTable ft, CellFormatter cellFormatter) {
//		
//		Image elementIcon = new Image();
//		if(info.getCreator() instanceof AgentSoftwareDTO)
//			elementIcon.setUrl(((org.mindinformatics.gwt.annotator.client.Annotator.Resources) _resources).computerIcon().getURL());
//		else 
//			elementIcon.setUrl(((org.mindinformatics.gwt.annotator.client.Annotator.Resources) _resources).userIcon().getURL());
//		
//		elementIcon.setStyleName(SWAN_ELEMENT_ICON_CSS);
//		elementIcon.setHeight("16px");
//		ft.setWidget(0, 0, elementIcon);
//		cellFormatter.setWidth(0, 0, ICON_WIDTH);
//	}
//	
//
//}
