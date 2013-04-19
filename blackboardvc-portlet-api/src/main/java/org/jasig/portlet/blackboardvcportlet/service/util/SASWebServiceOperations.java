package org.jasig.portlet.blackboardvcportlet.service.util;


public interface SASWebServiceOperations {

    Object marshalSendAndReceiveToSAS(String soapAction, Object requestPayload);

}