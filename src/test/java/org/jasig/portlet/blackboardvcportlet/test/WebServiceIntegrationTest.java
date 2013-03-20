/**
 * @author Brad Leege <leege@doit.wisc.edu>
 * Created on 3/18/13 at 2:09 PM
 */
package org.jasig.portlet.blackboardvcportlet.test;


import com.elluminate.sas.BasicAuth;
import com.elluminate.sas.ObjectFactory;
import org.jasig.springframework.ws.client.core.SetSoapActionCallback;
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
import org.springframework.ws.soap.SoapHeader;
import org.springframework.ws.soap.saaj.SaajSoapMessage;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringWriter;

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
/*
		BasicAuth basicAuth = OBJECT_FACTORY.createBasicAuth();
		basicAuth.setName("WISCONSIN-ADMIN");
		basicAuth.setPassword("Wared4aM");
*/
		SetSoapActionCallback soapAction = new SetSoapActionCallback("http://sas.elluminate.com/GetServerQuotas");

		webServicesTemplate.marshalSendAndReceive(OBJECT_FACTORY.createGetServerQuotas(null), new WebServiceMessageCallback() {

			@Override
			public void doWithMessage(WebServiceMessage webServiceMessage) throws IOException, TransformerException
			{
				if (webServiceMessage instanceof SaajSoapMessage)
				{
					final SaajSoapMessage casted = (SaajSoapMessage) webServiceMessage;
					casted.setSoapAction("http://sas.elluminate.com/GetServerQuotas");

					BasicAuth basicAuth = OBJECT_FACTORY.createBasicAuth();
					basicAuth.setName("WISCONSIN-ADMIN");
					basicAuth.setPassword("Wared4aM");

					final StringWriter out = new StringWriter();
					elluminateMarshller.marshal(basicAuth, new StreamResult(out));
					String bax = out.toString();
					logger.info("BAX: '{}'", bax);

					SoapHeader soapHeader = casted.getSoapHeader();

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

/*
					SaajSoapMessageFactory soapMessageFactory = new SaajSoapMessageFactory();
					soapMessageFactory.createWebServiceMessage().
*/

//					element.setText("<sas:Name>WISCONSIN-ADMIN</sas:Name><sas:Password>Wared4aM</sas:Password>");

//					soapHeader.addHeaderElement(new QName(bax));
/*
					QName qName = new QName("CustomHeaderElement");
					SoapHeaderElement headerElement = soapHeader.addHeaderElement(qName);
*/

/*
					SoapEnvelope soapEnvelope = (SoapEnvelope)(webServiceMessage);

					SOAPHeaderElement soapHeaderElement = casted.getSaajMessage().getSOAPHeader().addHeaderElement(soapEnvelope.c.createName("BasicAuth"));
					soapHeaderElement.addAttribute(casted.createName("xmlns"), "");
					soapHeaderElement.addChildElement(casted.createName("Token"));
*/
				}
			}
		});

//		webServicesTemplate.marshalSendAndReceive(OBJECT_FACTORY.createGetServerQuotas(null), soapAction);

//		Object object = webServicesTemplate.marshalSendAndReceive(null, soapAction);
//		assertNotNull(object);
/*
		GetServerQuotasResponseCollection serverQuotasResponseCollection = (GetServerQuotasResponseCollection)webServicesTemplate.marshalSendAndReceive(auth, soapAction);
		assertNotNull(serverQuotasResponseCollection);
		List<ServerQuotasResponse> quotaResult = serverQuotasResponseCollection.getServerQuotasResponses();
		assertNotNull(quotaResult);
		assertTrue(quotaResult.size() > 0);
*/
		logger.info("Finished testConnection().");
	}
}
