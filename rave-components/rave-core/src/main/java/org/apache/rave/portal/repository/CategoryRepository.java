/*
 * Copyright 2011 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.rave.portal.repository;

import org.apache.rave.repository.Repository;
import org.apache.rave.model.Category;


import java.util.List;

public interface CategoryRepository extends Repository<Category>{
    /**
     * Removes a user from the createdBy or modifiedBy fields for any Category they are associated with and assigns
     * the values to null
     *
     * @param userId
     * @return the number of Categories modified
     */
    int removeFromCreatedOrModifiedFields(String userId);
}
