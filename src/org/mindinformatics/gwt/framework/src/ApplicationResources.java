package org.mindinformatics.gwt.framework.src;

import org.mindinformatics.gwt.framework.component.logging.ui.LogResources;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.resources.client.ImageResource;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public interface ApplicationResources extends ClientBundle,
		LogResources {

	@NotStrict
	@Source("org/mindinformatics/gwt/framework/src/styles/Application.css")
	ApplicationCssResource css();
	
	public interface ApplicationCssResource extends CssResource {

		@ClassName("fw_Button")
		String button();
		
		@ClassName("fw_disabledButton")
		String disabledButton();
		
		@ClassName("fw_Label")
		String label();
		
		@ClassName("fw_disabledLabel")
		String disabledLabel();
		
		@ClassName("fw_selectedLabel")
		String selectedLabel();
		
		@ClassName("fw_noWrapText")
		String noWrapText();
		
		@ClassName("fw_noWrapActionText")
		String noWrapActionText();
		
		@ClassName("fw_actionIcon")
		String actionIcon();
	}
	
	@Source("org/mindinformatics/gwt/framework/icons/ajax.gif")
	ImageResource spinningIcon();
	
	@Source("org/mindinformatics/gwt/framework/icons/ajax-loader24x24.gif")
	ImageResource spinningIcon2();
	
	@Source("org/mindinformatics/gwt/framework/icons/home24x24.png")
	ImageResource homeLittleIcon();
	
	@Source("org/mindinformatics/gwt/framework/icons/print16x16.png")
	ImageResource printLittleIcon();
	
	@Source("org/mindinformatics/gwt/framework/icons/export16x16.gif")
	ImageResource exportLittleIcon();
	
	@Source("org/mindinformatics/gwt/framework/icons/send16x16.png")
	ImageResource sendLittleIcon();
	
	@Source("org/mindinformatics/gwt/framework/icons/execute24x24.png")
	ImageResource runLittleIcon();
	
	@Source("org/mindinformatics/gwt/framework/icons/settings16x16.png")
	ImageResource settingsLittleIcon();
	
	@Source("org/mindinformatics/gwt/framework/icons/computer16x16.png")
	ImageResource computerLittleIcon();
	
	@Source("org/mindinformatics/gwt/framework/icons/preferences24x24.png")
	ImageResource preferencesLittleIcon();
	
	@Source("org/mindinformatics/gwt/framework/icons/help18x18.png")
	ImageResource helpLittleIcon();
	
	@Source("org/mindinformatics/gwt/framework/icons/user-bw24x24.png")
	ImageResource userLittleIcon();
	
	@Source("org/mindinformatics/gwt/framework/icons/plugins24x24.png")
	ImageResource pluginsLittleIcon();
	
	@Source("org/mindinformatics/gwt/framework/icons/plugins16x16gray.png")
	ImageResource pluginsGrayLittleIcon();
	
	@Source("org/mindinformatics/gwt/framework/icons/question16x16.gif")
	ImageResource questionLittleIcon();
	
	@Source("org/mindinformatics/gwt/framework/icons/question48x48.png")
	ImageResource questionIcon();
	
	@Source("org/mindinformatics/gwt/framework/icons/progress48x48.gif")
	ImageResource progressIcon();
	
	@Source("org/mindinformatics/gwt/framework/icons/progress16x16.gif")
	ImageResource littleProgressIcon();
	
	@Source("org/mindinformatics/gwt/framework/icons/check16x16.png")
	ImageResource checkIcon();
	
	@Source("org/mindinformatics/gwt/framework/icons/warning16x16.png")
	ImageResource warningIcon();
	
	@Source("org/mindinformatics/gwt/framework/icons/link16x16.png")
	ImageResource externalLinkIcon();
	
	@Source("org/mindinformatics/gwt/framework/icons/info16x16.gif")
	ImageResource infoLittleIcon();
	
	@Source("org/mindinformatics/gwt/framework/icons/little_edit.png")
	ImageResource editLittleIcon();
	
	@Source("org/mindinformatics/gwt/framework/icons/little_show.png")
	ImageResource showLittleIcon();
	
	@Source("org/mindinformatics/gwt/framework/icons/little_trash16x16.png")
	ImageResource deleteLittleIcon();
	
	@Source("org/mindinformatics/gwt/framework/icons/add16x16.png")
	ImageResource addLittleIcon();
	
	@Source("org/mindinformatics/gwt/framework/icons/tick16x16.png")
	ImageResource acceptLittleIcon();
	
	@Source("org/mindinformatics/gwt/framework/icons/link16x16.gif")
	ImageResource editLinkIcon();
	
	@Source("org/mindinformatics/gwt/framework/icons/close16x16.gif")
	ImageResource closeLittleIcon();
	
	@Source("org/mindinformatics/gwt/framework/icons/progress-icon.gif")
	ImageResource progressBarIcon();
	
	@Source("org/mindinformatics/gwt/framework/icons/browse16x16.png")
	ImageResource browseLittleIcon();
	
	@Source("org/mindinformatics/gwt/framework/icons/unknown-person.gif")
	ImageResource unknownPersonIcon();
	
	@Source("org/mindinformatics/gwt/framework/icons/users-color16x16.png")
	ImageResource usersIcon();
}
