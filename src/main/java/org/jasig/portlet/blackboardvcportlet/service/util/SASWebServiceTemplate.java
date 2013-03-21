/**
 * @author Brad Leege <leege@doit.wisc.edu>
 * Created on 3/20/13 at 4:49 PM
 */
package org.jasig.portlet.blackboardvcportlet.service.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.saaj.SaajSoapMessage;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.transform.TransformerException;
import java.io.IOException;

public class SASWebServiceTemplate extends WebServiceTemplate
{
	@Autowired
	private Jaxb2Marshaller elluminateMarshller;

	/**
	 * Default Constructor
	 */
	public SASWebServiceTemplate()
	{
		super();
	}

	public Object marshalSendAndReceiveToSAS(final String soapAction, final Object requestPayload)
	{
		return marshalSendAndReceive(requestPayload, new WebServiceMessageCallback() {

			@Override
			public void doWithMessage(WebServiceMessage webServiceMessage) throws IOException, TransformerException
			{
				if (webServiceMessage instanceof SaajSoapMessage)
				{
					final SaajSoapMessage casted = (SaajSoapMessage) webServiceMessage;
					casted.setSoapAction(soapAction);

					QName qName = new QName(elluminateMarshller.getContextPath(), "BasicAuth", "sas");
					try
					{
						SOAPElement baHeader = casted.getSaajMessage().getSOAPHeader().addChildElement(qName);
						baHeader.addChildElement(new QName("sas", "Name")).addTextNode("WISCONSIN-ADMIN");
						baHeader.addChildElement(new QName("sas", "Password")).addTextNode("Wared4aM");
					}
					catch (Exception e)
					{
						logger.error("Error creating SOAPHeader: ", e);
					}
				}
			}
		});
	}
}
