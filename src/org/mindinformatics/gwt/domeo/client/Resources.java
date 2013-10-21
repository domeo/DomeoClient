package org.mindinformatics.gwt.domeo.client;

import org.mindinformatics.gwt.framework.component.logging.ui.LogResources;
import org.mindinformatics.gwt.framework.component.ui.glass.GlassResources;
import org.mindinformatics.gwt.framework.src.ApplicationResources;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.resources.client.ImageResource;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public interface Resources extends ClientBundle,
	ApplicationResources, LogResources, GlassResources {
	
	@NotStrict
	@Source("org/mindinformatics/gwt/domeo/client/General.css")
	GeneralCssResource generalCss();
	
	public interface GeneralCssResource extends CssResource {

		@ClassName("af-applyButton")
		String applyButton();
		
		@ClassName("tags")
		String tags();
	}
	
	@Source("org/mindinformatics/gwt/domeo/client/icons/logo_icon16px.png")
	ImageResource domeoLogoIcon();
	
	@Source("org/mindinformatics/gwt/domeo/client/icons/addCommentsIcon_16.png")
	ImageResource adddCommentsIcon();
	
	@Source("org/mindinformatics/gwt/domeo/client/icons/commentsIcon_16.png")
	ImageResource commentsIcon();
	
	@Source("org/mindinformatics/gwt/domeo/client/icons/commentIcon_16.png")
	ImageResource commentIcon();
	
	@Source("org/mindinformatics/gwt/domeo/client/icons/domeo24x24.png")
	ImageResource domeoLittleIcon();
	
	@Source("org/mindinformatics/gwt/domeo/client/icons/domeo24x24green.png")
	ImageResource domeoLittleColorIcon();
	
	@Source("org/mindinformatics/gwt/domeo/client/icons/domeoMultiple24x24.png")
	ImageResource domeoMultipleLittleIcon();
	
	@Source("org/mindinformatics/gwt/domeo/client/icons/multiple16x16.png")
	ImageResource multipleLittleIcon();
	
	@Source("org/mindinformatics/gwt/domeo/client/icons/clip24x24.png")
	ImageResource domeoClipIcon();
	
	@Source("org/mindinformatics/gwt/domeo/client/icons/clipcolor24x24.png")
	ImageResource domeoClipColorIcon();
	
	@Source("org/mindinformatics/gwt/domeo/client/icons/clear24x24.gif")
	ImageResource clearIcon();
	
	@Source("org/mindinformatics/gwt/domeo/client/icons/annotate2_24x24.png")
	ImageResource domeoAnnotateIcon();
	
	@Source("org/mindinformatics/gwt/domeo/client/icons/annotate2color_24x24.png")
	ImageResource domeoAnnotateColorIcon();
	
	@Source("org/mindinformatics/gwt/domeo/client/icons/domeoMultiple24x24green.png")
	ImageResource domeoMultipleLittleColorIcon();
	
	@Source("org/mindinformatics/gwt/domeo/client/icons/highlight24x24.png")
	ImageResource highlightLittleIcon();
	
	@Source("org/mindinformatics/gwt/domeo/client/icons/highlight24x24yellow.png")
	ImageResource highlightLittleColorIcon();
	
	@Source("org/mindinformatics/gwt/domeo/client/icons/pubmed.gif")
	ImageResource pubmedLittleColorIcon();
	
	@Source("org/mindinformatics/gwt/domeo/client/icons/pmc.png")
	ImageResource pmcLittleColorIcon();
	
	@Source("org/mindinformatics/gwt/domeo/client/icons/doi.gif")
	ImageResource doiLittleColorIcon();
	
	@Source("org/mindinformatics/gwt/domeo/client/icons/world16x16.png")
	ImageResource publicLittleIcon();
	
	@Source("org/mindinformatics/gwt/domeo/client/icons/personal16x16.png")
	ImageResource privateLittleIcon();
	
	@Source("org/mindinformatics/gwt/domeo/client/icons/group16x16.png")
	ImageResource friendsLittleIcon();
	
	@Source("org/mindinformatics/gwt/domeo/client/icons/custom16x16.png")
	ImageResource customLittleIcon();
	
	@Source("org/mindinformatics/gwt/domeo/client/icons/visible.png")
	ImageResource visibleLittleIcon();
	
	@Source("org/mindinformatics/gwt/domeo/client/icons/invisible.png")
	ImageResource invisibleLittleIcon();
	
	@Source("org/mindinformatics/gwt/domeo/client/icons/lock16x16.png")
	ImageResource readOnlyLittleIcon();
	
	@Source("org/mindinformatics/gwt/domeo/client/icons/unlocked16x16.png")
	ImageResource readWriteLittleIcon();
	
	@Source("org/mindinformatics/gwt/domeo/client/icons/save16x16.png")
	ImageResource saveLittleIcon();
	
	@Source("org/mindinformatics/gwt/domeo/client/icons/save22x22.png")
	ImageResource saveMediumIcon();
	
	@Source("org/mindinformatics/gwt/domeo/client/icons/link16x16.gif")
	ImageResource linkLittleIcon();
	
	@Source("org/mindinformatics/gwt/domeo/client/icons/graphviz16x16.png")
	ImageResource littleGraphIcon();
	
	@Source("org/mindinformatics/gwt/domeo/client/icons/squareadd16x16blue.png")
	ImageResource littleSquareAddIcon();
	
	@Source("org/mindinformatics/gwt/domeo/client/icons/image16x16blue.png")
	ImageResource littleImageIcon();
	
	@Source("org/mindinformatics/gwt/domeo/client/icons/text16x16.png")
	ImageResource littleTextIcon();
	
	@Source("org/mindinformatics/gwt/domeo/client/icons/comment16x16.png")
	ImageResource littleCommentIcon();
	
	@Source("org/mindinformatics/gwt/domeo/client/icons/comments16x16.png")
	ImageResource littleCommentsIcon();
	
	@Source("org/mindinformatics/gwt/domeo/client/icons/comments_add16x16.png")
	ImageResource addCommentsIcon();
	
	@Source("org/mindinformatics/gwt/domeo/client/icons/comment_add16x16.png")
	ImageResource addCommentIcon();
	
	@Source("org/mindinformatics/gwt/domeo/client/icons/arrow_split16x16.png")
	ImageResource splitCommentIcon();
	
	@Source("org/mindinformatics/gwt/domeo/client/icons/zoom16x16.png")
	ImageResource littleZoomIcon();
	
	@Source("org/mindinformatics/gwt/domeo/client/icons/add16x16.png")
	ImageResource littleAddIcon();
	
	@Source("org/mindinformatics/gwt/domeo/client/icons/threads16x16.gif")
	ImageResource littleThreadIcon();
	
	@Source("org/mindinformatics/gwt/domeo/client/icons/task_add16x16.gif")
	ImageResource littleBookAddIcon();
	
	@Source("org/mindinformatics/gwt/domeo/client/icons/arrow-right.gif")
	ImageResource arrowRightIcon();
	
	@Source("org/mindinformatics/gwt/domeo/client/icons/cross.png")
	ImageResource crossIcon();
	
	@Source("org/mindinformatics/gwt/domeo/client/icons/curation-accept.png")
	ImageResource acceptIcon();
	
	@Source("org/mindinformatics/gwt/domeo/client/icons/curation-accept-exact.png")
	ImageResource acceptExactIcon();
	
	@Source("org/mindinformatics/gwt/domeo/client/icons/curation-accept-broad.png")
	ImageResource acceptBroadIcon();
	
	@Source("org/mindinformatics/gwt/domeo/client/icons/curation-accept-narrow.png")
	ImageResource acceptNarrowIcon();
	
	@Source("org/mindinformatics/gwt/domeo/client/icons/curation-question.png")
	ImageResource discussIcon();
	
	@Source("org/mindinformatics/gwt/domeo/client/icons/curation-reject.png")
	ImageResource rejectIcon();
	
	@Source("org/mindinformatics/gwt/domeo/client/icons/tag-blue.png")
	ImageResource tagIcon();
	
	@Source("org/mindinformatics/gwt/domeo/client/icons/arrow_top_left.png")
	ImageResource annotationIcon();
}
