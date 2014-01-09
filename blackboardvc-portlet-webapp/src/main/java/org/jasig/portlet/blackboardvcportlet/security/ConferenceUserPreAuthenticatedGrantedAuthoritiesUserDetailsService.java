package org.jasig.portlet.blackboardvcportlet.security;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.jasig.portlet.blackboardvcportlet.dao.ConferenceUserDao;
import org.jasig.portlet.blackboardvcportlet.data.ConferenceUser;
import org.jasig.springframework.security.portlet.authentication.PortletAuthenticationDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedGrantedAuthoritiesUserDetailsService;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionOperations;

import com.google.common.collect.ImmutableList;


public class ConferenceUserPreAuthenticatedGrantedAuthoritiesUserDetailsService extends
        PreAuthenticatedGrantedAuthoritiesUserDetailsService {
    private static final Pattern ATTRIBUTE_NAME_SEPERATOR = Pattern.compile(",");
    
    private ConferenceUserDao conferenceUserDao;
    private TransactionOperations transactionOperations;
    private List<String> uniqueIdAttributeName;
    private List<String> emailAttributeName;
    private List<String> displayNameAttributeName;

    @Autowired
    public void setConferenceUserDao(ConferenceUserDao conferenceUserDao) {
        this.conferenceUserDao = conferenceUserDao;
    }

    @Autowired
    public void setTransactionOperations(TransactionOperations transactionOperations) {
        this.transactionOperations = transactionOperations;
    }

    @Value("${emailAttributeName:mail}")
    public void setEmailAttributeName(String emailAttributeName) {
        this.emailAttributeName = ImmutableList.copyOf(ATTRIBUTE_NAME_SEPERATOR.split(emailAttributeName));
    }

    @Value("${displayNameAttributeName:displayName}")
    public void setDisplayNameAttributeName(String displayNameAttributeName) {
        this.displayNameAttributeName = ImmutableList.copyOf(ATTRIBUTE_NAME_SEPERATOR.split(displayNameAttributeName));
    }

    @Value("${uniqueIdAttributeName:uid}")
    public void setUniqueIdAttributeName(String uniqueIdAttributeName) {
        this.uniqueIdAttributeName = ImmutableList.copyOf(ATTRIBUTE_NAME_SEPERATOR.split(uniqueIdAttributeName));
    }

    @Override
    protected UserDetails createuserDetails(Authentication token, Collection<? extends GrantedAuthority> authorities) {
        final PortletAuthenticationDetails authenticationDetails = (PortletAuthenticationDetails)token.getDetails();
        
        final ConferenceUser conferenceUser = this.setupConferenceUser(authenticationDetails);
        
        return new ConferenceSecurityUser(token.getName(), conferenceUser, authorities);
    }

    protected ConferenceUser setupConferenceUser(final PortletAuthenticationDetails authenticationDetails) {
        final String uniqueId = getAttribute(authenticationDetails, this.uniqueIdAttributeName, true);
        final String mail = getAttribute(authenticationDetails, this.emailAttributeName, true);
        final String displayName = getAttribute(authenticationDetails, this.displayNameAttributeName, false);
        
        return this.transactionOperations.execute(new TransactionCallback<ConferenceUser>() {
            @Override
            public ConferenceUser doInTransaction(TransactionStatus status) {
                ConferenceUser user = conferenceUserDao.getUserByUniqueId(uniqueId);
                if (user == null) {
                    user = conferenceUserDao.createInternalUser(uniqueId);
                }
                
                boolean modified = false;
                
                //Update with current display name
                if (!StringUtils.equals(user.getDisplayName(), displayName)) {
                    modified = true;
                    user.setDisplayName(displayName);
                }
                
                //Update with current email
                if (!StringUtils.equals(user.getEmail(), mail)) {
                    modified = true;
                    user.setEmail(mail);
                }
                
                if (modified) {
                    //Persist modification
                    conferenceUserDao.updateUser(user);
                }
                
                return user;
            }
        });
    }
    
    private String getAttribute(PortletAuthenticationDetails authenticationDetails, List<String> attributeNames, boolean required) {
        final Map<String, String> userInfo = authenticationDetails.getUserInfo();
        
        for (final String mailAttributeName : attributeNames) {
            final String value = userInfo.get(mailAttributeName);
            if (value != null) {
                return value;
            }
        }
        
        if (required) {
            throw new IllegalStateException("Could not find required user attribute value in attributes: " + attributeNames);
        }
        
        return null;
    }
}
