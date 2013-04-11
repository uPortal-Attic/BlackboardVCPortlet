package org.jasig.portlet.blackboardvcportlet.security;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;

import org.apache.commons.lang.StringUtils;
import org.jasig.portlet.blackboardvcportlet.dao.ConferenceUserDao;
import org.jasig.portlet.blackboardvcportlet.data.ConferenceUser;
import org.jasig.springframework.security.portlet.authentication.PreAuthenticatedGrantedAuthoritiesPortletAuthenticationDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedGrantedAuthoritiesUserDetailsService;

public class ConferenceUserPreAuthenticatedGrantedAuthoritiesUserDetailsService extends
        PreAuthenticatedGrantedAuthoritiesUserDetailsService {
    
    private ConferenceUserDao conferenceUserDao;

    @Autowired
    public void setConferenceUserDao(ConferenceUserDao conferenceUserDao) {
        this.conferenceUserDao = conferenceUserDao;
    }

    @Override
    protected UserDetails createuserDetails(Authentication token, Collection<? extends GrantedAuthority> authorities) {
        final PreAuthenticatedGrantedAuthoritiesPortletAuthenticationDetails details = (PreAuthenticatedGrantedAuthoritiesPortletAuthenticationDetails)token.getDetails();
        
//        details.getUserInfo()
        
        // TODO Auto-generated method stub
        return super.createuserDetails(token, authorities);
    }
    
    /*
    protected ConferenceUser getConferenceUser(PortletRequest request) {
        final String mail = getAttribute(request, "emailAttributeName", true, "mail");
        final String displayName = getAttribute(request, "displayNameAttributeName", false, "displayName");
        
        ConferenceUser user = this.blackboardUserDao.getBlackboardUser(mail);
        if (user == null) {
            user = this.blackboardUserDao.createBlackboardUser(mail, displayName);
        }
        
        //Update with current display name
        user.setDisplayName(displayName);
        
        //Update all key user attributes
        final PortletPreferences prefs = request.getPreferences();
        final String[] keyUserAttributeNames = prefs.getValues("keyUserAttributeNames", new String[0]);
        @SuppressWarnings("unchecked")
        final Map<String, String> userInfo = (Map<String, String>) request.getAttribute(PortletRequest.USER_INFO);
        for (final String keyUserAttributeName : keyUserAttributeNames) {
            final String value = userInfo.get(keyUserAttributeName);
            final Map<String, String> userAttributes = user.getAttributes();
            if (StringUtils.isNotEmpty(value)) {
                userAttributes.put(keyUserAttributeName, value);
            }
            else {
                userAttributes.remove(keyUserAttributeName);
            }
        }
        
        //Persist modification
        this.blackboardUserDao.updateBlackboardUser(user);
        
        return user;
    }

    private String getAttribute(PortletRequest request, String preferenceName, boolean required, String... defaultValues) {
        final PortletPreferences prefs = request.getPreferences();
        final String[] attributeNames = prefs.getValues(preferenceName, defaultValues);
        
        @SuppressWarnings("unchecked")
        final Map<String, String> userInfo = (Map<String, String>) request.getAttribute(PortletRequest.USER_INFO);
        
        for (final String mailAttributeName : attributeNames) {
            final String value = userInfo.get(mailAttributeName);
            if (value != null) {
                return value;
            }
        }
        
        if (required) {
            throw new IllegalStateException("Could not find required user attribute value for " + Arrays.toString(attributeNames));
        }
        
        return null;
    }
     */
}
