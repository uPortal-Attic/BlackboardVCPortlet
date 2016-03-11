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
package org.jasig.portlet.blackboardvcportlet.data;


public interface ConferenceUser extends BasicUser {
	
	public static enum Roles {
		CHAIR,
		NONCHAIR
	}
	
	
    public static final String EXTERNAL_USERID_PREFIX = "@EXT@";

    long getUserId();
    
    String getBlackboardUniqueId();
    
    /**
     * If the user is an external user, if true {@link #getInvitationKey()} will return a value
     */
    boolean isExternal();
    
    /**
     * Set the current email address for the user, cannot be null
     */
    void setEmail(String email);
    
    /**
     * Set the current display name for the user, cannot be null
     */
    void setDisplayName(String displayName);
    
    /**
     * Optional unique id for a user invite, null if {@link #isExternal()} is false
     */
    String getInvitationKey();
}