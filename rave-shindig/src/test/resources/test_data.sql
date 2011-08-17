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

INSERT INTO oauth_token_info(id, access_token, token_secret, session_handle, token_expire_millis, app_url, module_id, token_name, service_name, user_id)
VALUES (set(@token_info_id_1, next value for token_info_id_seq), 'accessToken', 'tokenSecret', 'sessionHandle', 3600000, 'http://localhost:8080/samplecontainer/examples/oauth.xml', 'NOT_USED', 'tokenName', 'serviceName', 'john.doe');

INSERT INTO oauth_consumer_store(id, gadget_uri, service_name, consumer_key, consumer_secret, key_type, key_name, callback_url)
VALUES (set(@consumer_store_id_1, next value for consumer_store_id_seq), 'http://localhost:8080/samplecontainer/examples/oauth.xml', 'Google', 'gadgetConsumer', 'gadgetSecret', 'HMAC_SYMMETRIC', 'keyName', 'http://oauth.gmodules.com/gadgets/oauthcallback');