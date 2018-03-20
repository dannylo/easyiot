package org.ufrn.framework.util;

import org.teleal.cling.model.action.ActionInvocation;
import org.teleal.cling.model.meta.Action;

public class ActionUpnpDefault extends ActionInvocation {

	public ActionUpnpDefault(Action action, String outputArgument, String newValue) {
		super(action);
		setInput(outputArgument, newValue);
	}
	
	

}
