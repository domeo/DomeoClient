package org.mindinformatics.gwt.utils.src;

import com.google.gwt.user.client.Window;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class UrlUtils {
	
	protected native String getHostname(String url)
	/*-{
		var re = new RegExp('^(?:f|ht)tp(?:s)?\://([^/]+)', 'im');
		return url.match(re)[1].toString();
	}-*/;
	
	public static String getAbsoluteLink(String url, String link) {
		if(!link.startsWith("http")) { 
			if(!link.startsWith("/")) {
				String base = url.substring(0, url.lastIndexOf('/'));
				return (base + link);
			} else if(link.startsWith("/")) {
				String base = UrlUtils.getUrlBase(url).substring(0, UrlUtils.getUrlBase(url).length()-1);
				return (base + link);
			}
		}
		return link;
	}
	
	public static String getUrlBase() {
		return UrlUtils.getUrlBase(Window.Location.getHref());
	}
	
	public static String getUrlAndRoot(String url) {
		return url.substring(0,url.lastIndexOf("/")+1);
	}
	
	public static String getUrlAndRoot() {
		return getUrlAndRoot(UrlUtils.getUrlBase().substring(0, UrlUtils.getUrlBase().length()-1) + Window.Location.getPath());
	}

	public static native String getUrlBase(String url) /*-{
	    var baseURL = url.substring(0, url.indexOf('/', 14));
	
	    if (baseURL.indexOf('http://localhost') != -1) {
	        // Base Url for localhost
	        var url = location.href;  // window.location.href;
	        var pathname = location.pathname;  // window.location.pathname;
	        var index1 = url.indexOf(pathname);
	        var index2 = url.indexOf("/", index1 + 1);
	        var baseLocalUrl = url.substr(0, index2);
	
	        return baseLocalUrl + "/";
	    }
	    else {
	        // Root Url for domain name
	        return baseURL + "/";
	    }
	}-*/;
}
