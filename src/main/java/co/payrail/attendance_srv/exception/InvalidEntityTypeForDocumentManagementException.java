/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package co.payrail.attendance_srv.exception;

/**
 * A {@link RuntimeException} thrown when document management functionality is
 * invoked for invalid Entity Types
 */
public class InvalidEntityTypeForDocumentManagementException extends BranchlessBankingException {

//    public InvalidEntityTypeForDocumentManagementException(final String entityType) {
//        super("error.documentmanagement.entitytype.invalid", "Document Management is not support for the Entity Type: " + entityType,
//                entityType);
//    }

    public InvalidEntityTypeForDocumentManagementException(String message) {
        super(message);
    }

    public InvalidEntityTypeForDocumentManagementException(String message, Throwable cause) {
        super(message, cause);
    }
}