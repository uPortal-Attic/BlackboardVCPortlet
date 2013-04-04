package org.jasig.portlet.blackboardvcportlet.dao.impl;

import java.util.Map;
import java.util.Set;

import org.jasig.portlet.blackboardvcportlet.data.BlackboardUser;

public interface BlackboardUserDao {

    BlackboardUser createBlackboardUser(String email, String displayName);

    BlackboardUser updateBlackboardUser(BlackboardUser user);

    void deleteBlackboardUser(BlackboardUser message);

    BlackboardUser getBlackboardUser(String email);

    Set<BlackboardUser> findAllMatchingUsers(String email, Map<String, String> attributes);

}