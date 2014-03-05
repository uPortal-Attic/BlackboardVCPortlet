package org.jasig.portlet.blackboardvcportlet.data;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class BasicUserImpl implements BasicUser {
    private static final long serialVersionUID = 1L;
    
    private final String uniqueId;
    private final String email;
    private final Set<String> additionalEmails;
    private final String displayName;
    
    public BasicUserImpl(String uniqueId, String email, String displayName) {
        this(uniqueId, email, displayName, Collections.<String>emptySet());
    }

    public BasicUserImpl(String uniqueId, String email, String displayName, Set<String> additionalEmails) {
        this.uniqueId = uniqueId;
        this.email = email;
        this.displayName = displayName;
        this.additionalEmails = additionalEmails != null ? additionalEmails : new LinkedHashSet<String>();
    }

    @Override
    public String getUniqueId() {
        return this.uniqueId;
    }

    @Override
    public String getEmail() {
        return this.email;
    }

    @Override
    public Set<String> getAdditionalEmails() {
        return this.additionalEmails;
    }

    @Override
    public String getDisplayName() {
        return this.displayName;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((uniqueId == null) ? 0 : uniqueId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BasicUserImpl other = (BasicUserImpl) obj;
        if (uniqueId == null) {
            if (other.uniqueId != null)
                return false;
        }
        else if (!uniqueId.equals(other.uniqueId))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "BasicUserImpl [uniqueId=" + uniqueId + ", email=" + email + ", displayName=" + displayName
                + ", additionalEmails=" + additionalEmails + "]";
    }
}
