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
package org.jasig.portlet.blackboardvcportlet.data;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Entity class holding external participants to a session
 * @author Richard Good
 */
@Entity
@Table(name="VC_SESSION_EXTPARTICIPANT")
public class SessionExtParticipant implements Serializable {
    
    @EmbeddedId
    protected SessionExtParticipantId sessionExtParticipantId;
    
    @Column(name="DISPLAY_NAME")
    String display_name;

    public SessionExtParticipant()
    {
        sessionExtParticipantId = new SessionExtParticipantId();
    }
    
    public SessionExtParticipantId getSessionExtParticipantId() {
        return sessionExtParticipantId;
    }

    public void setSessionExtParticipantId(SessionExtParticipantId sessionExtParticipantId) {
        this.sessionExtParticipantId = sessionExtParticipantId;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }
      
}
