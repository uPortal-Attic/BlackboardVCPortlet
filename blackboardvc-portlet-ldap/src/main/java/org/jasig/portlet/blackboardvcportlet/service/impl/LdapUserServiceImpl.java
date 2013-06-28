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

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;

import org.apache.commons.lang.StringUtils;
import org.jasig.portlet.blackboardvcportlet.data.BasicUser;
import org.jasig.portlet.blackboardvcportlet.data.BasicUserImpl;
import org.jasig.portlet.blackboardvcportlet.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapOperations;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.LikeFilter;
import org.springframework.ldap.filter.OrFilter;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;

/**
 * Service Class for retrieving LDAP user lookups
 * @author Richard Good
 */

public class LdapUserServiceImpl implements UserService {
    private static final Pattern NAME_SPLIT = Pattern.compile("\\s+");
    
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private final BasicUserAttributeMapper basicUserAttributeMapper = new BasicUserAttributeMapper();
    private LdapOperations ldapOperations;
    private String uniqueIdAttributeName = "uid";
    private String displayNameAttributeName = "cn";
    private String firstNameAttributeName = "givenName";
    private String lastNameAttributeName = "sn";
    private String mailAttributeName = "mail";
    
    public void setUniqueIdAttributeName(String uniqueIdAttributeName) {
        this.uniqueIdAttributeName = uniqueIdAttributeName;
    }

    public void setDisplayNameAttributeName(String displayNameAttributeName) {
        this.displayNameAttributeName = displayNameAttributeName;
    }

    public void setFirstNameAttributeName(String firstNameAttributeName) {
        this.firstNameAttributeName = firstNameAttributeName;
    }

    public void setLastNameAttributeName(String lastNameAttributeName) {
        this.lastNameAttributeName = lastNameAttributeName;
    }

    public void setMailAttributeName(String mailAttributeName) {
        this.mailAttributeName = mailAttributeName;
    }

    @Autowired
    public void setLdapOperations(LdapOperations ldapOperations) {
        this.ldapOperations = ldapOperations;
    }
    
    @Override
    public BasicUser findUser(String uniqueId) {
        final AndFilter andFilter = createBaseFilter();
        andFilter.and(new EqualsFilter(uniqueIdAttributeName, uniqueId));
        
        final String searchFilter = andFilter.encode();
        @SuppressWarnings("unchecked")
        final List<BasicUser> results = ldapOperations.search("", searchFilter, basicUserAttributeMapper);
        return DataAccessUtils.uniqueResult(results);
    }

    @Override
    public Set<BasicUser> searchForUserByName(String name) {
        final List<String> nameParts = getNameParts(name);
        
        //Nothing useful to search on return an empty set
        if (nameParts.isEmpty()) {
            return Collections.emptySet();
        }
        
        final AndFilter andFilter = createBaseFilter();

        final OrFilter orFilter = new OrFilter();
        
        final String namePartZero = nameParts.get(0);
        if (nameParts.size() == 1) {
            orFilter.or(new LikeFilter(firstNameAttributeName, namePartZero + "*"));
            orFilter.or(new LikeFilter(lastNameAttributeName, namePartZero + "*"));
            orFilter.or(new LikeFilter(displayNameAttributeName, namePartZero + "*"));
        }
        else {
            final AndFilter firstLastFilter = new AndFilter();
            firstLastFilter.and(new LikeFilter(firstNameAttributeName, namePartZero + "*"));
            firstLastFilter.and(new LikeFilter(lastNameAttributeName, nameParts.get(nameParts.size() - 1) + "*"));
            orFilter.or(firstLastFilter);
            
            final String displayNameSearch = NAME_SPLIT.matcher(name.trim()).replaceAll("*") + "*";
            orFilter.or(new LikeFilter(displayNameAttributeName, displayNameSearch));
        }
        andFilter.and(orFilter);
        
        final String searchFilter = andFilter.encode();
        @SuppressWarnings("unchecked")
        final List<BasicUser> results = ldapOperations.search("", searchFilter, basicUserAttributeMapper);
        
        return Collections.unmodifiableSet(new LinkedHashSet<BasicUser>(results));
    }

    @Override
    public Set<BasicUser> searchForUserByEmail(String email) {
        email = StringUtils.trimToNull(email);
        if (email == null) {
            return Collections.emptySet();
        }
        
        final AndFilter andFilter = createBaseFilter();
        andFilter.and(new LikeFilter(mailAttributeName, email + "*"));
        
        final String searchFilter = andFilter.encode();
        @SuppressWarnings("unchecked")
        final List<BasicUser> results = ldapOperations.search("", searchFilter, basicUserAttributeMapper);
        
        return Collections.unmodifiableSet(new LinkedHashSet<BasicUser>(results));
    }

    protected AndFilter createBaseFilter() {
        final AndFilter andFilter = new AndFilter();
        andFilter.and(new EqualsFilter("objectclass", "person"));
        return andFilter;
    }

    protected List<String> getNameParts(String name) {
        final String[] nameParts = NAME_SPLIT.split(name);
        final List<String> usefulNameParts = new ArrayList<String>(nameParts.length);
        for (String namePart : nameParts) {
            namePart = StringUtils.trimToNull(namePart);
            if (namePart != null) {
                usefulNameParts.add(namePart);
            }
        }
        return usefulNameParts;
    }
    
    private class BasicUserAttributeMapper implements AttributesMapper {
        @Override
        public Object mapFromAttributes(Attributes attributes) throws NamingException {
            final String uniqueId = getAttribute(uniqueIdAttributeName, attributes);
            if (uniqueId == null) {
                throw new IncorrectResultSizeDataAccessException("'" + uniqueIdAttributeName + "' is a required attribute", 1, 0);
            }
            
            String primaryEmail = null;
            final Builder<String> additionalEmailsBuilder = ImmutableSet.builder();
            
            final Attribute emailAddressAttr = attributes.get(mailAttributeName);
            if (emailAddressAttr != null) {
                for (final NamingEnumeration<?> allEmailsEnum = emailAddressAttr.getAll(); allEmailsEnum.hasMore(); ) {
                    final Object email = allEmailsEnum.next();
                    if (email == null) {
                        continue;
                    }
                    
                    final String emailStr = email.toString();
                    if (primaryEmail == null) {
                        primaryEmail = emailStr;
                    }
                    else {
                        additionalEmailsBuilder.add(emailStr);
                    }
                }
            }
            
            final String displayName = getAttribute(displayNameAttributeName, attributes);
            
            return new BasicUserImpl(uniqueId, primaryEmail, displayName, additionalEmailsBuilder.build());
        }

        private String getAttribute(String attributeName, Attributes attributes) throws NamingException {
            final Attribute attrValue = attributes.get(attributeName);
            if (attrValue == null) {
                return null;
            }
            
            final Object value = attrValue.get();
            return value.toString();
        }
    }
}
