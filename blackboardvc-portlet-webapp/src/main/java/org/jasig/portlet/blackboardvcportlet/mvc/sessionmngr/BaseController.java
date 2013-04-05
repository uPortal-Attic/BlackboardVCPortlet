package org.jasig.portlet.blackboardvcportlet.mvc.sessionmngr;

import java.util.Map;

import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;

import org.apache.commons.lang.StringUtils;
import org.jasig.portlet.blackboardvcportlet.dao.BlackboardUserDao;
import org.jasig.portlet.blackboardvcportlet.data.BlackboardUser;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseController {
    protected BlackboardUserDao blackboardUserDao;

    @Autowired
    public void setBlackboardUserDao(BlackboardUserDao blackboardUserDao) {
        this.blackboardUserDao = blackboardUserDao;
    }

    protected BlackboardUser getBlackboardUser(PortletRequest request) {
        final String mail = getAttribute(request, "emailAttributeName", "mail");
        final String displayName = getAttribute(request, "displayNameAttributeName", "displayName");
        
        BlackboardUser user = this.blackboardUserDao.getBlackboardUser(mail);
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

    private String getAttribute(PortletRequest request, String preferenceName, String... defaultValues) {
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
        
        return null;
    }

}