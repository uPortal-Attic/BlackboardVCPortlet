package org.jasig.portlet.blackboardvcportlet.data;

import java.io.Serializable;


public interface UserSessionUrl extends Serializable {

    long getUrlId();

    Session getSession();

    ConferenceUser getUser();

    String getUrl();

}