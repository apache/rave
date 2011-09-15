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

set @widget_seq = 'widget';


--- gadget data ---

-- useless knowledge widget
set @widget_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @widget_seq);
insert into widget (entity_id,title, url, type, widget_status)
values(@widget_id,'Useless Knowledge', 'http://www.great-goofy-gadgets.com/humor/uselessknowledge/uselessknowledge.xml', 'OpenSocial', 'PREVIEW');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @widget_seq;
-- end widget data ----