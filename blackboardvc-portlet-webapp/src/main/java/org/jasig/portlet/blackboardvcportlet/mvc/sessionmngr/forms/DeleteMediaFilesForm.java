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
/**
 * @author Brad Leege <leege@doit.wisc.edu>
 * Created on 4/24/13 at 3:05 PM
 */
package org.jasig.portlet.blackboardvcportlet.mvc.sessionmngr.forms;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Arrays;

public class DeleteMediaFilesForm implements Serializable
{
	private static final long serialVersionUID = 1L;

    @NotNull
    private long sessionId;

    @NotNull
    @Size(min = 1)
    private long[] id;

    public long getSessionId() {
        return sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    public long[] getId() {
        return id;
    }

    public void setId(long[] id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "DeleteMediaFilesForm [sessionId=" + sessionId + ", id=" + Arrays.toString(id) + "]";
    }
}

