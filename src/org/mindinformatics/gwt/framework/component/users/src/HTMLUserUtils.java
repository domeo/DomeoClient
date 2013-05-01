package org.mindinformatics.gwt.framework.component.users.src;

public class HTMLUserUtils {

	public native static boolean doesUsernameExist() /*-{
		return ($wnd.username!=null && $wnd.username!== 'undefined');
	}-*/;
	
	public native static String getUsername() /*-{
		return $wnd.username;
	}-*/;
	
	public native static boolean doesServerNameExist() /*-{
		return ($wnd.servername!=null && $wnd.servername!== 'undefined');
	}-*/;
	
	public native static String getServerName() /*-{
		return $wnd.servername;
	}-*/;
	
	public native static boolean doesServerVersionExist() /*-{
		return ($wnd.serverversion!=null && $wnd.serverversion!== 'undefined');
	}-*/;
	
	public native static String getServerVersion() /*-{
		return $wnd.serverversion;
	}-*/;
	
	public native static String getStandaloneFlag() /*-{
		// Not sure we need this line
		//if($wnd.standalone!=null && $wnd.standalone!== 'undefined') return "";
		return $wnd.standalone;
	}-*/;
	
	public native static String getTestFilesFlag() /*-{
		// Not sure we need this line
		//if($wnd.standalone!=null && $wnd.standalone!== 'undefined') return "";
		return $wnd.testFiles;
	}-*/;
	
	public native static String getJsonFormatFlag() /*-{
    	// Not sure we need this line
    	//if($wnd.standalone!=null && $wnd.standalone!== 'undefined') return "";
    	return $wnd.jsonformat;
    }-*/;
	
	public native static String getLocalResourcesFlag() /*-{
        // Not sure we need this line
        //if($wnd.standalone!=null && $wnd.standalone!== 'undefined') return "";
        return $wnd.localResources;
    }-*/;
}
