/**
 * Licensed to Jasig under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Jasig licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jasig.portlet.blackboardvcportlet.service.impl;

import java.util.List;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;

import org.jasig.portlet.blackboardvcportlet.data.ConferenceUser;
import org.jasig.portlet.blackboardvcportlet.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.OrFilter;

/**
 * Service Class for retrieving LDAP user lookups
 * @author Richard Good
 */

public class LdapUserServiceImpl implements UserService
{
	protected Logger logger = LoggerFactory.getLogger(getClass());

	private LdapTemplate ldapTemplate;

	@Autowired
	public void setLdapTemplate(LdapTemplate ldapTemplate)
	{
		this.ldapTemplate = ldapTemplate;
	}

    public class PersonAttributeMapper implements AttributesMapper{

        /**
         * TODO
         * 
         * Maps the basic user attributes 
         * @param attributes
         * @return
         * @throws NamingException 
         */
        @Override
        public Object mapFromAttributes(Attributes attributes) throws NamingException {
            ConferenceUser user = null;//new BlackboardUser();
            
            // Users may not have an email address
            if (attributes.get("mail")!=null)
            {
                String email = (String)attributes.get("mail").get();
                if (email!=null)
                {
                    logger.debug("Setting email:"+email);
//                    user.setEmail(email);
                }
            
            }
                    
            String uid = (String)attributes.get("uid").get();
            if (uid!=null)
            {
                logger.debug("Setting uid:"+uid);
//                user.setUid(uid);
            }
            
            String cn = (String)attributes.get("cn").get();
            if (cn!=null)
            {
                logger.debug("Setting cn:"+cn);
                user.setDisplayName(cn);
            }
            
            return user;
    }

}
    
    /**
     * Gets a User from a passed in searchTerm. Checks for match on cn or uid
     * @param searchTerm
     * @return User
     */
    public ConferenceUser getUserDetails(String searchTerm)
    {       
        logger.debug("getUserDetails called");
        AndFilter andFilter = new AndFilter();
        andFilter.and(new EqualsFilter("objectclass","person"));
        OrFilter orFilter = new OrFilter();
        orFilter.or(new EqualsFilter("uid",searchTerm));
        orFilter.or(new EqualsFilter("cn",searchTerm));
        andFilter.and(orFilter);
        logger.debug("Set up the filter for searchTerm:"+searchTerm);
        List<ConferenceUser> result;
        result = ldapTemplate.search("",andFilter.encode(),new PersonAttributeMapper());
        logger.debug("gotten a result");
        if (result.size()>0)
        {
            logger.debug("returning first result");
            return result.get(0);
        }
        else 
        {
            logger.debug("no-one found, returning null");
            return null;
        }
       
    }
}
