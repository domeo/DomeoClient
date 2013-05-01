package org.mindinformatics.gwt.framework.component.reporting.src;

import org.mindinformatics.gwt.framework.component.reporting.src.testing.GwtReportManager;
import org.mindinformatics.gwt.framework.src.IApplication;
import org.mindinformatics.gwt.framework.src.ICommandCompleted;

import com.google.gwt.dom.client.BodyElement;
import com.google.gwt.user.client.ui.Widget;

/**
 * The reports manager is taking care of communicating issues related to 
 * page scraping or to positioning of the annotation in context. Collecting
 * these info on the fly will allow to better react to changes in the
 * publishing format and in the published content.
 * 
 * Reporting can be performed through 
 * - email (when possible)
 * - popup window
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public abstract class AReportsManager implements IReportsManager {
	
	public static final String LOG_ANOMALY = "REPORTED ANOMALY";
	public static final String LOG_ANOMALY_NOT_REPORTED = "ANOMALY NOT REPORTED";

	protected IApplication _application;
	private ICommandCompleted _callbackCompleted;
	
	public AReportsManager(IApplication application, ICommandCompleted callbackCompleted) {
		_application = application;
		_callbackCompleted = callbackCompleted;
	}
	
	public void displayWidget(Widget widget) {
		BodyElement bdElement = GwtReportManager.getBodyElement();
		bdElement.getOwnerDocument().getElementById("mainbody").appendChild((widget).getElement());
	}
	
	public void sendWidgetAsEmail(String sourceComponent, String title, Widget message, String url) {
		_application.getLogger().info(LOG_ANOMALY, sourceComponent, message.getElement().getInnerText());
		sendEmail(title,  sourceComponent, message.getElement().getInnerText(), url);
	}
	
	public static native void sendEmail(String title, String sourceComponent, String message, String url) /*-{
		alert('mailto:test@example.com?subject=[Domeo Test Instance] '+title+'&body={"component":"' + sourceComponent + '", "resource":"' + url + '", "message": "'+ message+ '"}');
		window.open('mailto:test@example.com?subject=[Domeo Test Instance] '+title+'&body={"component":"' + sourceComponent + '", "resource":"' + url + '", "message": "'+ message+ '"}');
	}-*/;
	
	public static native BodyElement getBodyElement() /*-{
		var win = window.open("", "win", "width=940,height=400,status=1,resizeable=1,scrollbars=1"); // a window object
		win.document.open("text/html", "replace");
		
		win.document.write("<HTML><HEAD></HEAD><BODY><div class=\"reportpanel\"><div style=\"width: 100%; height: 54px;\"><div id=\"mainbody\"class=\"mainbody\" style=\"width: 100%;\"></div></div></div></BODY></HTML>");
	    win.document.close(); 
	    win.focus();
	    return win.document.body;
	}-*/;
	
	public void stageCompleted() {
		_callbackCompleted.notifyStageCompletion();
	}
}
