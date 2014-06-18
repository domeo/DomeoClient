package org.mindinformatics.gwt.framework.src;

import org.mindinformatics.gwt.domeo.client.IDomeo;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.regexp.shared.RegExp;

public class ApplicationUtils {

	public static DateTimeFormat fullfmt2 = DateTimeFormat.getFormat("yyyy-MM-dd'T'HH:mm:ssZ"); // "2012-09-24T19:57:13EST"
	public static DateTimeFormat fullfmt = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss Z"); // "2012-09-24T19:57:13Z"
	public static DateTimeFormat fmt = DateTimeFormat.getFormat("MM/dd/yyyy h:mma");
	public static DateTimeFormat day = DateTimeFormat.getFormat("MM/dd/yyyy Z");
	
	public static String getAnnotationToolLink(IDomeo domeo, String url, String link) {
		String absoluteLink = getAbsoluteLink(domeo, url, link);
		return cleanUrl(getUrlString())+"?url=" + absoluteLink;
	}
	
	public static String getAnnotationToolLink(String link) {
		return cleanUrl(getUrlString())+"?url=" + link;
	}
	
	public static String getAbsoluteLink(IDomeo domeo, String url, String link) {
		if(!link.startsWith("http")) { 
			if(!link.startsWith("/")) {
				String base = url.substring(0, domeo.getPersistenceManager().getCurrentResourceUrl().lastIndexOf('/'));
				return (base + link);
			} else if(link.startsWith("/")) {
				String base = getUrlBase(url).substring(0, getUrlBase(url).length()-1);
				return (base + link);
			}
		}
		return link;
	}
	
	public static boolean isValidUrl(String url, boolean topLevelDomainRequired) {
		RegExp urlValidator = null;
		RegExp urlPlusTldValidator = null;
	    if (urlValidator == null || urlPlusTldValidator == null) {
	        // These require at least a host name after the port 
	        //urlValidator = RegExp.compile("^((ftp|http|https)://[\\w@.\\-\\_]+(:\\d{1,5})?(/[\\w#!:.?+=&%@!\\_\\-/]+)*){1}$");
            //urlPlusTldValidator = RegExp.compile("^((ftp|http|https)://[\\w@.\\-\\_]+\\.[a-zA-Z]{2,}(:\\d{1,5})?(/[\\w#!:.?+=&%@!\\_\\-/]+)*){1}$");

	        urlValidator = RegExp.compile("^((ftp|http|https)://[\\w@.\\-\\_]+(:\\d{1,5})?(/[\\w#!:.?+=&%@!\\_\\-/]*)*){1}$");
	        urlPlusTldValidator = RegExp.compile("^((ftp|http|https)://[\\w@.\\-\\_]+\\.[a-zA-Z]{2,}(:\\d{1,5})?(/[\\w#!:.?+=&%@!\\_\\-/]*)*){1}$");
	    }
	    return (topLevelDomainRequired ? urlPlusTldValidator : urlValidator).exec(url) != null;
	}

	public static String cleanUrl(String url) {
		if(url.indexOf('?')>0)
			url = url.substring(0, url.indexOf('?'));
		return url;
	}
	
	public static native void openUrl(String url) /*-{
	    window.open (url,"mywindow");
	}-*/;
	
	public native static String getDefaultSetPrivacy() /*-{
		return $wnd.defaultSetPrivacy;
	}-*/;
	
	public native static String getProvideExamplesUrls() /*-{
		return $wnd.provideExamplesUrls;
	}-*/;
	
	public native static String getDebugActivated() /*-{
		return $wnd.debugActivated;
	}-*/;
	
	public native static String getProxyProtocol() /*-{
		return $wnd.proxyProtocol;
	}-*/;
	
	public native static String getIsAnnotopiaEnabled() /*-{
		return $wnd.enableAnnotopia;
	}-*/;
	
	public native static String getAnnotopiaLocation() /*-{
		return $wnd.annotopiaLocation;
	}-*/;
	
	public native static String getAnnotopiaOauthToken() /*-{
		return $wnd.annotopiaOauthToken;
	}-*/;
	
	public native static String getAnnotopiaApiKey() /*-{
		return $wnd.annotopiaApiKey;
	}-*/;
	
//  Deprecated
//	public native static String getDocumentUrl() /*-{
//		return $wnd.documentUrl;
//	}-*/;
//	
//	public native static String getLineageId() /*-{
//		return $wnd.lineageId;
//	}-*/;
//	
//	public native static String getAnnotationId() /*-{
//		return $wnd.annotationId;
//	}-*/;
	
	public static native String getUrlString() /*-{
		return $wnd.location.toString();
	}-*/;
	
	public static native String getUrlBase(String url) /*-{
	     if (url.indexOf('http://localhost') != -1) {
	        // Base Url for localhost
	        var url2 = location.href;  // window.location.href;
	        var pathname = location.pathname;  // window.location.pathname;
	        var index1 = url2.indexOf(pathname);
	        var index2 = url2.indexOf("/", index1 + 1);
	        var baseLocalUrl = url2.substr(0, index2);
	        return baseLocalUrl + "/";
	    }
	    else {
	        // Root Url for domain name
	        var firstSlash = url.indexOf('/', 14);
			var strBaseURL = url.substring(0, url.indexOf('/', firstSlash+1));
	        return strBaseURL + "/";
	    }
	}-*/;
	
	public static native String getHostname(String url)
	/*-{
		var re = new RegExp('^(?:f|ht)tp(?:s)?\://([^/]+)', 'im');
		return url.match(re)[1].toString();
	}-*/;
	
	public static native String getUrlParameter(String name)
	/*-{
		return decodeURI((new RegExp('[?|&]' + name + '=' + 
			'([^&;]+?)(&|#|;|$)').exec($wnd.location)||[,""])[1].replace(/\+/g, '%20'));
	}-*/;
	
	public static native String encodeUrlComponent(String component) 
	/*-{
		return encodeURIComponent(component);;
	}-*/;
	
	public static native String decodeURIComponent(String component) 
	/*-{
		return decodeURIComponent(component);;
	}-*/;
	
	public static native void updateUrl(String url)
	/*-{
		var base = $wnd.location.toString();
		var index = $wnd.location.toString().indexOf("?");
		if(index>0) base = base.substring(0, $wnd.location.toString().indexOf("?"));
		//return base;
		$wnd.history.pushState("", "Domeo Annotator", base + "?url=" + encodeURIComponent(url));
	}-*/;
}
