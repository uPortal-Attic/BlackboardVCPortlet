package org.jasig.portlet.blackboardvcportlet.dao.ws;

import javax.xml.bind.JAXBElement;

import com.elluminate.sas.BlackboardSuccessResponse;

public class WSDaoUtils {
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static boolean isSuccessful(Object response) {
		if(response instanceof JAXBElement) {
			if(((JAXBElement)response).getValue() instanceof BlackboardSuccessResponse) {
				return ((JAXBElement<BlackboardSuccessResponse>)response).getValue().isSuccess();
			} else 
				return false;
		} else {
			return (response instanceof BlackboardSuccessResponse && ((BlackboardSuccessResponse)response).isSuccess());
		}
	}
}
