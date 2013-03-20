/**
 * @author Brad Leege <leege@doit.wisc.edu>
 * Created on 3/18/13 at 2:09 PM
 */
package org.jasig.portlet.blackboardvcportlet.test;

import com.elluminate.sas.GetServerQuotasResponseCollection;
import com.elluminate.sas.ObjectFactory;
import com.elluminate.sas.ServerQuotasResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.saaj.SaajSoapMessage;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.List;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/test-applicationContext.xml")
public class WebServiceIntegrationTest
{
	private static final Logger logger = LoggerFactory.getLogger(WebServiceIntegrationTest.class);

	@Autowired
	private WebServiceTemplate webServicesTemplate;

	@Autowired
	private Jaxb2Marshaller elluminateMarshller;

	private static final ObjectFactory OBJECT_FACTORY = new ObjectFactory();

	@Test
	public void testConnection() throws Exception
	{
		logger.info("Starting testConnection()...");

		GetServerQuotasResponseCollection serverQuotasResponseCollection = (GetServerQuotasResponseCollection)webServicesTemplate.marshalSendAndReceive(OBJECT_FACTORY.createGetServerQuotas(null), new WebServiceMessageCallback() {

			@Override
			public void doWithMessage(WebServiceMessage webServiceMessage) throws IOException, TransformerException
			{
				if (webServiceMessage instanceof SaajSoapMessage)
				{
					final SaajSoapMessage casted = (SaajSoapMessage) webServiceMessage;
					casted.setSoapAction("http://sas.elluminate.com/GetServerQuotas");

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

		assertNotNull(serverQuotasResponseCollection);
		List<ServerQuotasResponse> quotaResult = serverQuotasResponseCollection.getServerQuotasResponses();
		assertNotNull(quotaResult);
		assertTrue(quotaResult.size() > 0);
		logger.info("Finished testConnection().");
	}
}
