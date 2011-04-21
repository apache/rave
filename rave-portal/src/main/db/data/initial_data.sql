 -- Licensed to the Apache Software Foundation (ASF) under one
 -- or more contributor license agreements.  See the NOTICE file
 -- distributed with this work for additional information
 -- regarding copyright ownership.  The ASF licenses this file
 -- to you under the Apache License, Version 2.0 (the
 -- "License"); you may not use this file except in compliance
 -- with the License.  You may obtain a copy of the License at
  ~
 --   http://www.apache.org/licenses/LICENSE-2.0
  ~
 -- Unless required by applicable law or agreed to in writing,
 -- software distributed under the License is distributed on an
 -- "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 -- KIND, either express or implied.  See the License for the
 -- specific language governing permissions and limitations
 -- under the License.

--  --- start person data ---
insert into person (user_id) values (set(@user_id_1, 'canonical'));
insert into person (user_id) values (set(@user_id_2, 'john.doe'));
insert into person (user_id) values (set(@user_id_3, 'jane.doe'));
insert into person (user_id) values (set(@user_id_4, 'george.doe'));
insert into person (user_id) values (set(@user_id_5, 'mario.rossi'));
insert into person (user_id) values (set(@user_id_6, 'maija.m'));
--- end person data ---

--- gadget data ---
-- wikipedia widget
insert into widget (id, title, url)
values(set(@wikipedia_widget_id, next value for widget_id_seq), 'Wikipedia','http://www.google.com/ig/modules/wikipedia.xml');

-- translate widget
insert into widget (id, title, url)
values(set(@translate_widget_id, next value for widget_id_seq), 'Translate Gadget', 'http://www.google.com/ig/modules/dictionary.xml');

-- nytimes widget
insert into widget (id, title, url)
values(set(@nyt_widget_id, next value for widget_id_seq), 'NYTimes.com - Top Stories', 'http://widgets.nytimes.com/packages/html/igoogle/topstories.xml');

-- google tabbed news widget
insert into widget (id, title, url)
values(set(@tabnews_widget_id, next value for widget_id_seq), 'Google News Gadget', 'http://www.gstatic.com/ig/modules/tabnews/tabnews.xml');

-- end widget data ----


--- start page layout data ---
insert into page_layout (id, code,  number_of_regions)
values (next value for page_layout_id_seq, 'columns_1', 1);
insert into page_layout (id, code,  number_of_regions)
values (set(@two_col_id, next value for page_layout_id_seq), 'columns_2', 2);
insert into page_layout (id, code,  number_of_regions)
values (next value for page_layout_id_seq, 'columns_2wn', 2);
insert into page_layout (id, code,  number_of_regions)
values (next value for page_layout_id_seq, 'columns_3', 3);
insert into page_layout (id, code,  number_of_regions)
values (next value for page_layout_id_seq, 'columns_3nwn', 3);
insert into page_layout (id, code,  number_of_regions)
values (next value for page_layout_id_seq, 'columns_4', 4);
insert into page_layout (id, code,  number_of_regions)
values (next value for page_layout_id_seq, 'columns_3nwn_1_bottom', 4);
--- end page layout data ----
