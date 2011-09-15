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

set @token_info_seq = 'token_info';
set @oauth_consumer_store_seq = 'oauth_consumer_store';
set @person_seq = 'person';
set @person_association_seq = 'person_association';
set @groups_seq = 'groups';
set @group_members_seq = 'group_members';

CREATE TABLE IF NOT EXISTS RAVE_SHINDIG_SEQUENCES (seq_name VARCHAR(255) PRIMARY KEY NOT NULL, seq_count BIGINT(19));
INSERT INTO RAVE_SHINDIG_SEQUENCES(seq_name, seq_count) values (@token_info_seq, 1);
INSERT INTO RAVE_SHINDIG_SEQUENCES(seq_name, seq_count) values (@oauth_consumer_store_seq, 1);
INSERT INTO RAVE_SHINDIG_SEQUENCES(seq_name, seq_count) values (@person_seq, 1);
INSERT INTO RAVE_SHINDIG_SEQUENCES(seq_name, seq_count) values (@person_association_seq, 1);
INSERT INTO RAVE_SHINDIG_SEQUENCES(seq_name, seq_count) values (@groups_seq, 1);
INSERT INTO RAVE_SHINDIG_SEQUENCES(seq_name, seq_count) values (@group_members_seq, 1);

set @person_id_1 = (SELECT seq_count FROM RAVE_SHINDIG_SEQUENCES WHERE seq_name = @person_seq);
INSERT INTO person(entity_id, username, display_name, gender, status, profile_url, build, eye_color, hair_color,family_name, given_name )
VALUES (@person_id_1, 'canonical', 'Canonical User', 'male', 'I am alive', 'http://rave.rocks.org/profile', 'skinny', 'orange', 'blue', 'User', 'Canonical');
UPDATE RAVE_SHINDIG_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @person_seq;

set @person_id_2 = (SELECT seq_count FROM RAVE_SHINDIG_SEQUENCES WHERE seq_name = @person_seq);
INSERT INTO person(entity_id, username, display_name, gender)
VALUES (@person_id_2, 'john.doe', 'John Doe', 'male');
UPDATE RAVE_SHINDIG_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @person_seq;

set @person_id_3 = (SELECT seq_count FROM RAVE_SHINDIG_SEQUENCES WHERE seq_name = @person_seq);
INSERT INTO person(entity_id, username, display_name, gender)
VALUES (@person_id_3, 'jane.doe', 'Jane Doe', 'female');
UPDATE RAVE_SHINDIG_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @person_seq;

set @person_id_4 = (SELECT seq_count FROM RAVE_SHINDIG_SEQUENCES WHERE seq_name = @person_seq);
INSERT INTO person(entity_id, username, display_name)
VALUES (@person_id_4, 'george.doe', 'George Doe');
UPDATE RAVE_SHINDIG_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @person_seq;

set @person_id_5 = (SELECT seq_count FROM RAVE_SHINDIG_SEQUENCES WHERE seq_name = @person_seq);
INSERT INTO person(entity_id, username, display_name)
VALUES (@person_id_5, 'mario.rossi', 'Mario Rossi');
UPDATE RAVE_SHINDIG_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @person_seq;

set @next_person_association = (SELECT seq_count FROM RAVE_SHINDIG_SEQUENCES WHERE seq_name = @person_association_seq);
INSERT INTO person_association(entity_id, follower_id, followed_id)
VALUES (@next_person_association, @person_id_1, @person_id_2);
UPDATE RAVE_SHINDIG_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @person_association_seq;

set @next_person_association = (SELECT seq_count FROM RAVE_SHINDIG_SEQUENCES WHERE seq_name = @person_association_seq);
INSERT INTO person_association(entity_id, follower_id, followed_id)
VALUES (@next_person_association, @person_id_1, @person_id_3);
UPDATE RAVE_SHINDIG_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @person_association_seq;

set @next_person_association = (SELECT seq_count FROM RAVE_SHINDIG_SEQUENCES WHERE seq_name = @person_association_seq);
INSERT INTO person_association(entity_id, follower_id, followed_id)
VALUES (@next_person_association, @person_id_2, @person_id_4);
UPDATE RAVE_SHINDIG_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @person_association_seq;

set @group_id_1 = (SELECT seq_count FROM RAVE_SHINDIG_SEQUENCES WHERE seq_name = @groups_seq);
INSERT INTO groups(entity_id, title, description)
VALUES (@group_id_1, 'Party', 'Party Group');
UPDATE RAVE_SHINDIG_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @groups_seq;

set @group_id_2 = (SELECT seq_count FROM RAVE_SHINDIG_SEQUENCES WHERE seq_name = @groups_seq);
INSERT INTO groups(entity_id, title, description)
VALUES (@group_id_2, 'Portal', 'Portal Group');
UPDATE RAVE_SHINDIG_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @groups_seq;

INSERT INTO group_members(group_id, person_id)
VALUES (@group_id_1, @person_id_1);

INSERT INTO group_members(group_id, person_id)
VALUES (@group_id_1, @person_id_5);
