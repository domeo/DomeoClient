package org.mindinformatics.gwt.utils.src;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;

public class HttpUtils {

	public static boolean verifyUrl(String url) {
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, URL
				.encode(url));
		try {
			Request request = builder.sendRequest(null, new RequestCallback() {
				public void onError(Request request, Throwable exception) {
					Window.alert("error");
				}

				public void onResponseReceived(Request request,
						Response response) {
					if (200 == response.getStatusCode()) {
						Window.alert("response 200");
					} else {
						Window.alert("response " + response.getStatusCode());
					}
				}
			});
		} catch (RequestException e) {
			Window.alert("" + e.getMessage());
			return false;
		}
		return true;
	}
}
