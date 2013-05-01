package org.mindinformatics.gwt.framework.model.users;

import java.util.Date;

public interface IUser {

	String getUri();
	String getUserName();
	String getScreenName();
	Date getUserSince();
}
