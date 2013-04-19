package org.jasig.portlet.blackboardvcportlet.security;

import java.util.Collection;

import org.jasig.portlet.blackboardvcportlet.data.ConferenceUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class ConferenceSecurityUser extends User {
    private static final long serialVersionUID = 1L;
    
    private final String uniqueId;

    public ConferenceSecurityUser(String username, ConferenceUser conferenceUser, Collection<? extends GrantedAuthority> authorities) {
        super(username, "", authorities);
        
        this.uniqueId = conferenceUser.getUniqueId();
    }

    public String getUniqueId() {
        return uniqueId;
    }
}
