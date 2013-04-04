package org.jasig.portlet.blackboardvcportlet.data;

import java.io.Serializable;


public interface UserSessionUrl extends Serializable {

    long getUrlId();

    BlackboardSession getSession();

    BlackboardUser getUser();

    String getUrl();

}