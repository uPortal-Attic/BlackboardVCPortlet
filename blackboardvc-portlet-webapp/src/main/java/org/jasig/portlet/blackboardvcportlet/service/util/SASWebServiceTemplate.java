/**
 * Licensed to Apereo under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Apereo licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License.  You may obtain a
 * copy of the License at the following location:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
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

public class SASWebServiceTemplate extends WebServiceTemplate implements SASWebServiceOperations
{
	private Jaxb2Marshaller elluminateMarshller;

	private String username;
	private String password;

    @Autowired
	public void setElluminateMarshller(Jaxb2Marshaller elluminateMarshller)
	{
		this.elluminateMarshller = elluminateMarshller;
	}

	/**
	 * Default Constructor
	 * @param username String SAS Web Service Username credential
	 * @param password String SAS Web Service Password credential
	 */
	public SASWebServiceTemplate(final String username, final String password)
	{
		super();
		this.username = username;
		this.password = password;
	}

	@Override
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
						baHeader.addChildElement(new QName("sas", "Name")).addTextNode(username);
						baHeader.addChildElement(new QName("sas", "Password")).addTextNode(password);
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
