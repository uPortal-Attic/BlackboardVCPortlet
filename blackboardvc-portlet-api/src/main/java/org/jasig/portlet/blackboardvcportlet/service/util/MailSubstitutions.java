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
package org.jasig.portlet.blackboardvcportlet.service.util;

public enum MailSubstitutions {
	
	DISPLAY_NAME("displayName"),
	SESSION_BOUNDARY_TIME("boundaryTime"),
	SESSION_CREATOR_NAME("creatorName"),
	SESSION_CREATOR_EMAIL("creatorEmail"),
	SESSION_END_TIME("sessionEndTime"),
	SESSION_GUEST_URL("guestURL"),
	SESSION_NAME("sessionName"),
	SESSION_START_TIME("sessionStartTime"),
	SESSION_TYPE("sessionType"),
	SESSION_UPDATE_TEXT("sessionUpdateText"),
	SESSION_USER_URL("userURL");
	
	
	private String substitution;
	
	private MailSubstitutions(String substitution) {
		this.substitution = substitution;
	}

	public String getSubstitution() {
		return substitution;
	}

	public void setSubstitution(String substitution) {
		this.substitution = substitution;
	}
	
	@Override
	public String toString() {
		return substitution;
	}
	
	
}
