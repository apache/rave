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
set @application_data_seq = 'application_data';
set @person_seq = 'person';
set @person_association_seq = 'person_association';
set @groups_seq = 'groups';
set @group_members_seq = 'group_members';

set @token_info_id_1 = (SELECT seq_count FROM RAVE_SHINDIG_SEQUENCES WHERE seq_name = @token_info_seq);
INSERT INTO oauth_token_info(entity_id, access_token, token_secret, session_handle, token_expire_millis, app_url, module_id, token_name, service_name, user_id)
VALUES (@token_info_id_1, 'accessToken', 'tokenSecret', 'sessionHandle', 3600000, 'http://localhost:8080/samplecontainer/examples/oauth.xml', 'NOT_USED', 'tokenName', 'serviceName', 'john.doe');
UPDATE RAVE_SHINDIG_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @token_info_seq;

set @consumer_store_id_1 = (SELECT seq_count FROM RAVE_SHINDIG_SEQUENCES WHERE seq_name = @oauth_consumer_store_seq);
INSERT INTO oauth_consumer_store(entity_id, gadget_uri, service_name, consumer_key, consumer_secret, key_type, key_name, callback_url)
VALUES (@consumer_store_id_1, 'http://localhost:8080/samplecontainer/examples/oauth.xml', 'Google', 'gadgetConsumer', 'gadgetSecret', 'HMAC_SYMMETRIC', 'keyName', 'http://oauth.gmodules.com/gadgets/oauthcallback');
UPDATE RAVE_SHINDIG_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @oauth_consumer_store_seq;

set @application_data_id_1 = (SELECT seq_count FROM RAVE_SHINDIG_SEQUENCES WHERE seq_name = @application_data_seq);
INSERT INTO application_data(entity_id, user_id, app_url, serialized_data, dtype)
VALUES (@application_data_id_1, '12345', 'http://example.com/gadget.xml', '{"color":"blue","speed":"fast","state":"MA"}', 'JpaApplicationDataRepository$JpaSerializableApplicationData');
UPDATE RAVE_SHINDIG_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @application_data_seq;

CREATE TABLE IF NOT EXISTS RAVE_PORTAL_SEQUENCES (seq_name VARCHAR(255) PRIMARY KEY NOT NULL, seq_count BIGINT(19));
INSERT INTO RAVE_PORTAL_SEQUENCES(seq_name, seq_count) values (@person_seq, 1);
INSERT INTO RAVE_PORTAL_SEQUENCES(seq_name, seq_count) values (@person_association_seq, 1);
INSERT INTO RAVE_PORTAL_SEQUENCES(seq_name, seq_count) values (@groups_seq, 1);
INSERT INTO RAVE_PORTAL_SEQUENCES(seq_name, seq_count) values (@group_members_seq, 1);

set @person_id_1 = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @person_seq);
INSERT INTO person(entity_id, username, display_name, status, family_name, given_name )
VALUES (@person_id_1, 'canonical', 'Canonical User', 'I am alive', 'User', 'Canonical');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @person_seq;

set @person_id_2 = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @person_seq);
INSERT INTO person(entity_id, username, display_name)
VALUES (@person_id_2, 'john.doe', 'John Doe');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @person_seq;

set @person_id_3 = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @person_seq);
INSERT INTO person(entity_id, username, display_name)
VALUES (@person_id_3, 'jane.doe', 'Jane Doe');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @person_seq;

set @person_id_4 = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @person_seq);
INSERT INTO person(entity_id, username, display_name)
VALUES (@person_id_4, 'george.doe', 'George Doe');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @person_seq;

set @person_id_5 = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @person_seq);
INSERT INTO person(entity_id, username, display_name)
VALUES (@person_id_5, 'mario.rossi', 'Mario Rossi');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @person_seq;

set @next_person_association = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @person_association_seq);
INSERT INTO person_association(entity_id, follower_id, followed_id)
VALUES (@next_person_association, @person_id_1, @person_id_2);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @person_association_seq;

set @next_person_association = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @person_association_seq);
INSERT INTO person_association(entity_id, follower_id, followed_id)
VALUES (@next_person_association, @person_id_1, @person_id_3);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @person_association_seq;

set @next_person_association = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @person_association_seq);
INSERT INTO person_association(entity_id, follower_id, followed_id)
VALUES (@next_person_association, @person_id_2, @person_id_4);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @person_association_seq;

set @group_id_1 = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @groups_seq);
INSERT INTO groups(entity_id, title, description)
VALUES (@group_id_1, 'Party', 'Party Group');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @groups_seq;

set @group_id_2 = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @groups_seq);
INSERT INTO groups(entity_id, title, description)
VALUES (@group_id_2, 'Portal', 'Portal Group');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @groups_seq;

INSERT INTO group_members(group_id, person_id)
VALUES (@group_id_1, @person_id_1);

INSERT INTO group_members(group_id, person_id)
VALUES (@group_id_1, @person_id_5);
