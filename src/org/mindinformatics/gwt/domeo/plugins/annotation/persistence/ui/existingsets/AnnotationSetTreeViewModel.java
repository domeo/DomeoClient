/*
 * Copyright 2013 Massachusetts General Hospital
 * 
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.mindinformatics.gwt.domeo.plugins.annotation.persistence.ui.existingsets;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.mindinformatics.gwt.domeo.client.Domeo;
import org.mindinformatics.gwt.domeo.plugins.persistence.json.marshalling.JsoAnnotationSetSummary;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.CompositeCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.TreeViewModel;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class AnnotationSetTreeViewModel implements TreeViewModel {

	/**
	 * The images used for this example.
	 */
	static interface Images extends ClientBundle {
		ImageResource commentsIcon_32();
		ImageResource annotationIcon_32();
		ImageResource publicAccess_24();
		ImageResource privateAccess();
		ImageResource groupsAccess();
	}
	
	private ExistingAnnotationViewerPanel2 parent;
	private static Images images;
	private final ListDataProvider<Category> categoryDataProvider;
	private final Cell<AnnotationSetInfo> contactCell;
	private final DefaultSelectionEventManager<AnnotationSetInfo> selectionManager =
		      DefaultSelectionEventManager.createCheckboxManager();
	private final SelectionModel<AnnotationSetInfo> selectionModel;
	
	public AnnotationSetTreeViewModel(final SelectionModel<AnnotationSetInfo> selectionModel, final ExistingAnnotationViewerPanel2 parent) {
		this.parent = parent;
		this.selectionModel = selectionModel;
		if (images == null) {
			images = GWT.create(Images.class);
		}

		// Create a data provider that provides categories.
		categoryDataProvider = new ListDataProvider<Category>();
		List<Category> categoryList = categoryDataProvider.getList();
		for (Category category : parent.getCategories()) {
			categoryList.add(category);
		}

		// Construct a composite cell for contacts that includes a checkbox.
		List<HasCell<AnnotationSetInfo, ?>> hasCells = new ArrayList<HasCell<AnnotationSetInfo, ?>>();
		hasCells.add(new HasCell<AnnotationSetInfo, Boolean>() {

			private CheckboxCell cell = new CheckboxCell(true, false);

			public Cell<Boolean> getCell() {
				return cell;
			}

			public FieldUpdater<AnnotationSetInfo, Boolean> getFieldUpdater() {
				return null;
			}

			public Boolean getValue(AnnotationSetInfo object) {
				return selectionModel.isSelected(object);
			}
		});
		hasCells.add(new HasCell<AnnotationSetInfo, AnnotationSetInfo>() {

			private AnnotationSetCell cell = new AnnotationSetCell(selectionModel, parent);

			public Cell<AnnotationSetInfo> getCell() {
				return cell;
			}

			public FieldUpdater<AnnotationSetInfo, AnnotationSetInfo> getFieldUpdater() {
				return null;
			}

			public AnnotationSetInfo getValue(AnnotationSetInfo object) {
				return object;
			}
		});
		contactCell = new CompositeCell<AnnotationSetInfo>(hasCells) {
			@Override
			public void render(Context context, AnnotationSetInfo value,
					SafeHtmlBuilder sb) {
				sb.appendHtmlConstant("<table><tbody><tr>");
				super.render(context, value, sb);
				sb.appendHtmlConstant("</tr></tbody></table>");
			}

			@Override
			protected Element getContainerElement(Element parent) {
				// Return the first TR element in the table.
				return parent.getFirstChildElement().getFirstChildElement()
						.getFirstChildElement();
			}

			@Override
			protected <X> void render(Context context, AnnotationSetInfo value,
					SafeHtmlBuilder sb, HasCell<AnnotationSetInfo, X> hasCell) {
				Cell<X> cell = hasCell.getCell();
				sb.appendHtmlConstant("<td>");
				cell.render(context, hasCell.getValue(value), sb);
				sb.appendHtmlConstant("</td>");
			}
		};
	}

	@Override
	public <T> NodeInfo<?> getNodeInfo(T value) {
		if (value == null) {
			// Return top level categories.
			return new DefaultNodeInfo<Category>(categoryDataProvider,
					new CategoryCell());
		} else if (value instanceof Category) {
			// Return the first letters of each first name.
			Category category = (Category) value;
			List<AnnotationSetInfo> contacts = parent.queryContactsByCategory(category);
			
			ListDataProvider<AnnotationSetInfo> dataProvider = new ListDataProvider<AnnotationSetInfo>(
			          contacts, AnnotationSetInfo.KEY_PROVIDER);
			
			return new DefaultNodeInfo<AnnotationSetInfo>(
					dataProvider,
					contactCell, selectionModel, selectionManager, null);
		} 
		
		// Unhandled type.
		String type = value.getClass().getName();
		throw new IllegalArgumentException("Unsupported object type: " + type);
	}

	@Override
	public boolean isLeaf(Object value) {
		return value instanceof AnnotationSetInfo;
	}

	/**
	 * Information about a contact.
	 */
	public static class AnnotationSetInfo implements Comparable<AnnotationSetInfo> {

		/**
		 * The key provider that provides the unique ID of a contact.
		 */
		public static final ProvidesKey<AnnotationSetInfo> KEY_PROVIDER = new ProvidesKey<AnnotationSetInfo>() {
			@Override
			public Object getKey(AnnotationSetInfo item) {
				return item == null ? null : item.getId();
			}
		};

		private Category category;
		private JsoAnnotationSetSummary annotationSet;

		public AnnotationSetInfo(Category category, JsoAnnotationSetSummary annotationSet) {
			this.annotationSet = annotationSet;
			setCategory(category);
		}

		@Override
		public int compareTo(AnnotationSetInfo o) {
			return (o == null || o.annotationSet.getFormattedLastSaved() == null) ? -1 : -o.annotationSet.getFormattedLastSaved()
					.compareTo(annotationSet.getFormattedLastSaved());
		}

		@Override
		public boolean equals(Object o) {
			if (o instanceof AnnotationSetInfo) {
				return annotationSet.getId() == ((AnnotationSetInfo) o).annotationSet.getId();
			}
			return false;
		}

		/**
		 * @return the contact's birthday
		 */
		public Date getLastSavedOn() {
			return annotationSet.getFormattedLastSaved();
		}
		
		public String getLabel() {
			return annotationSet.getLabel();
		}
		
		public String getDescription() {
			return annotationSet.getDescription();
		}
		
		public String getType() {
			return annotationSet.getType();
		}
		
		public String getTypeName() {
			return annotationSet.getType().substring(annotationSet.getType().indexOf(":"));
		}

		public String getDisplayName() {
			return annotationSet.getType().substring(annotationSet.getType().indexOf(":"));
		}

		/**
		 * @return the unique ID of the contact
		 */
		public String getId() {
			return annotationSet.getId();
		}

	    public Category getCategory() {
	        return category;
	      }


//		@Override
//		public int hashCode() {
//			return id;
//		}

		public void setCategory(Category category) {
			assert category != null : "category cannot be null";
			this.category = category;
		}
		
		public JsoAnnotationSetSummary getAnnotationSet() {
			return annotationSet;
		}
	}
	
    public static final ProvidesKey<AnnotationSetInfo> KEY_PROVIDER = new ProvidesKey<AnnotationSetInfo>() {
        @Override
        public Object getKey(AnnotationSetInfo item) {
          return item == null ? null : item.getId();
        }
      };
	
	public static class Category {

		private int counter = 0;
		private final String permission;
		private final ImageResource icon;

		public Category(String permission, ImageResource icon) {
			this.permission = permission;
			this.icon = icon;
		}
		
		public String getDisplayName() {
			return permission;
		}
		
		public void incrementCounter() {
			counter++;
		}
		
		public String getCounter() {
			return Integer.toString(counter);
		}
		
		public ImageResource getIcon() {
			return icon;
		}
	}

	public static class AnnotationSetCell extends
			AbstractCell<AnnotationSetInfo> {
		
		SelectionModel<AnnotationSetInfo> selectionModel;
		ExistingAnnotationViewerPanel2 parentPanel;

		/**
		 * The html of the image used for contacts.
		 */
		private String imageHtml;

		public AnnotationSetCell(SelectionModel<AnnotationSetInfo> selectionModel, ExistingAnnotationViewerPanel2 parentPanel) {
			super("mousedown");
			// http://stackoverflow.com/questions/18599619/gwt-cell-tree-right-click-selection
			this.selectionModel = selectionModel;
			this.parentPanel = parentPanel;
		}

		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context,
				AnnotationSetInfo value, SafeHtmlBuilder sb) {
			// Value can be null, so do a null check..
			if (value == null) {
				return;
			}
			
			if(value.getType().equals("domeo:DiscussionSet"))
				this.imageHtml = AbstractImagePrototype.create(images.commentsIcon_32()).getHTML();
			else
				this.imageHtml = AbstractImagePrototype.create(images.annotationIcon_32()).getHTML();

			//JsAnnotationSetSummaryLens lens = new JsAnnotationSetSummaryLens();
			
			sb.appendHtmlConstant("<table>");

			// Add the contact image.
			sb.appendHtmlConstant("<tr><td rowspan='3'>");
			sb.appendHtmlConstant(imageHtml);
			sb.appendHtmlConstant("</td>");

			// Add the name and address.
			sb.appendHtmlConstant("<td style='font-size:95%;'>");
			sb.appendEscaped(value.getLabel());
			sb.appendHtmlConstant("</td><td width='70px' align='right'>");
			sb.appendEscaped(value.annotationSet.getNumberOfAnnotationItems() +(value.annotationSet.getNumberOfAnnotationItems()==1?" item":" items"));
			sb.appendHtmlConstant("</td></tr><tr><td>");
			sb.appendHtmlConstant("By " + value.annotationSet.getCreatedBy().getScreenName() +" on " + value.annotationSet.getFormattedCreatedOn());
			sb.appendHtmlConstant("</td><td align='right'>");
			//sb.appendHtmlConstant("<a target='_blank' href='" + value.annotationSet.getCreatedBy().getUri() + "'><img src='"+ 
			//		Domeo.resources.browseLittleIcon().getSafeUri().asString() +"'></a>");
			sb.appendHtmlConstant("</td></tr></table>");
		}
		
		@Override
	    public void onBrowserEvent(com.google.gwt.cell.client.Cell.Context context, Element parent, AnnotationSetInfo value, NativeEvent event, ValueUpdater<AnnotationSetInfo> valueUpdater) {
			//Window.alert(" click");
			/*
	        if (event.getButton() == NativeEvent.BUTTON_RIGHT) {
	            Window.alert("right click");
	        }
	        */
			parentPanel.showAnnotationSetPreview(value.getId());
			
	        super.onBrowserEvent(context, parent, value, event, valueUpdater);
	    }
	}
	
	/**
	 * The cell used to render categories.
	 */
	private static class CategoryCell extends AbstractCell<Category> {

		/**
		 * The html of the image used for contacts.
		 */
		private String imageHtml;

		public CategoryCell() {
		}

		@Override
		public void render(Context context, Category value, SafeHtmlBuilder sb) {
			if (value != null) {
				this.imageHtml = AbstractImagePrototype.create(value.getIcon())
						.getHTML();
				sb.appendHtmlConstant(imageHtml).appendEscaped(" ");
				sb.appendHtmlConstant("<span style='font-size:120%;'>");
				sb.appendEscaped(value.getDisplayName() + " (" + value.getCounter() + ")");
				sb.appendHtmlConstant("</span>");
			}
		}
	}
}
