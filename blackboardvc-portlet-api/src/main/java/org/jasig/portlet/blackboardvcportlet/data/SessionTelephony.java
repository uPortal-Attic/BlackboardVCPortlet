package org.jasig.portlet.blackboardvcportlet.data;

import java.io.Serializable;

public interface SessionTelephony  extends Serializable{
	Session getSession();

    String getChairPhone();
    String getChairPIN();
	String getNonChairPhone();
	String getNonChairPIN();
    String getSessionSIPPhone();
    String getSessionPIN();

	boolean isPhone();
	
	long getTelephonyId();
}
