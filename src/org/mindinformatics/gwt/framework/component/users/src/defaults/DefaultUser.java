package org.mindinformatics.gwt.framework.component.users.src.defaults;

import java.util.Date;

import org.mindinformatics.gwt.framework.component.users.model.MUser;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
@SuppressWarnings("serial")
public class DefaultUser extends MUser  {

	public DefaultUser() {
		super("Guest", "http://www.commonsemantics.com/account/Guest", "Guest", new Date());
	}
}
