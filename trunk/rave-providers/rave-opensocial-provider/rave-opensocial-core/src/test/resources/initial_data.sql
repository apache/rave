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

CREATE TABLE IF NOT EXISTS RAVE_SHINDIG_SEQUENCES (seq_name VARCHAR(255) PRIMARY KEY NOT NULL, seq_count BIGINT(19));
INSERT INTO RAVE_SHINDIG_SEQUENCES(seq_name, seq_count) values (@token_info_seq, 1);
INSERT INTO RAVE_SHINDIG_SEQUENCES(seq_name, seq_count) values (@oauth_consumer_store_seq, 1);
INSERT INTO RAVE_SHINDIG_SEQUENCES(seq_name, seq_count) values (@application_data_seq, 1);
