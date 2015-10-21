/*
 * Copyright 2014 IBM Corp. All Rights Reserved
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.utc.vat;

import com.ibm.mobile.services.data.IBMDataObject;
import com.ibm.mobile.services.data.IBMDataObjectSpecialization;

@IBMDataObjectSpecialization("session")
public class Session extends IBMDataObject {
    public static final String CLASS_NAME = "session";
    private static final String NAME = "name";
    private static final String USERID = "userId";

    /**
     * gets the name of the session.
     * @return String sessionName
     */
    public String getName() {
        return (String) getObject(NAME);
    }

    /**
     * sets the name of a list session, as well as calls setCreationTime()
     * @param String sessionName
     */
    public void setName(String sessionName) {
        setObject(NAME, (sessionName != null) ? sessionName : "");
    }

    /**
     * gets the userId associated with the session.
     * @return String userId
     */
    public String getUserId() {
        return (String) getObject(USERID);
    }

    /**
     * sets the email address of the user who is maintain list sessions
     * @param String userId
     */
    public void setUserId(String userId) {
        setObject(USERID, (userId != null) ? userId : "");
    }

    /**
     * when calling toString() for an session, we'd really only want the name.
     * @return String thesessionName
     */
    public String toString() {
        String thesessionName = "";
        thesessionName = getName();
        return thesessionName;
    }
}
