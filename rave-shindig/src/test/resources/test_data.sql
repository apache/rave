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