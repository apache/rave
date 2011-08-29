 -- Licensed to the Apache Software Foundation (ASF) under one
 -- or more contributor license agreements.  See the NOTICE file
 -- distributed with this work for additional information
 -- regarding copyright ownership.  The ASF licenses this file
 -- to you under the Apache License, Version 2.0 (the
 -- "License"); you may not use this file except in compliance
 -- with the License.  You may obtain a copy of the License at

 --   http://www.apache.org/licenses/LICENSE-2.0

 -- Unless required by applicable law or agreed to in writing,
 -- software distributed under the License is distributed on an
 -- "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 -- KIND, either express or implied.  See the License for the
 -- specific language governing permissions and limitations
 -- under the License.

INSERT INTO person(id, username, display_name, gender, status, profile_url, build, eye_color, hair_color,family_name, given_name )
VALUES (set(@person_id_1, next value for person_id_seq), 'canonical', 'Canonical User', 'male', 'I am alive', 'http://rave.rocks.org/profile', 'skinny', 'orange', 'blue', 'User', 'Canonical');

INSERT INTO person(id, username, display_name, gender)
VALUES (set(@person_id_2, next value for person_id_seq), 'john.doe', 'John Doe', 'male');

INSERT INTO person(id, username, display_name, gender)
VALUES (set(@person_id_3, next value for person_id_seq), 'jane.doe', 'Jane Doe', 'female');

INSERT INTO person(id, username, display_name)
VALUES (set(@person_id_4, next value for person_id_seq), 'george.doe', 'George Doe');

INSERT INTO person(id, username, display_name)
VALUES (set(@person_id_5, next value for person_id_seq), 'mario.rossi', 'Mario Rossi');

INSERT INTO person_association(id, follower_id, followed_id)
VALUES (next value for person_association_id_seq, @person_id_1, @person_id_2);

INSERT INTO person_association(id, follower_id, followed_id)
VALUES (next value for person_association_id_seq, @person_id_1, @person_id_3);

INSERT INTO person_association(id, follower_id, followed_id)
VALUES (next value for person_association_id_seq, @person_id_2, @person_id_4);

INSERT INTO groups(id, title, description)
VALUES (set(@group_id_1, next value for group_id_seq), 'Party', 'Party Group');

INSERT INTO groups(id, title, description)
VALUES (set(@group_id_2, next value for group_id_seq), 'Portal', 'Portal Group');

INSERT INTO group_members(group_id, person_id)
VALUES (@group_id_1, @person_id_1);

INSERT INTO group_members(group_id, person_id)
VALUES (@group_id_1, @person_id_5);
