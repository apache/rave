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

--  --- start user data ---
insert into user (user_id, username, password, expired, locked, enabled)
values (set(@user_id_1, next value for user_id_seq), 'canonical', 'b97fd0fa25ba8a504309be2b6651ac6dee167ded', 'N', 'N', 'Y');

insert into user (user_id, username, password, expired, locked, enabled)
values (set(@user_id_2, next value for user_id_seq), 'john.doe', '49e5f5c7c7ae8372af9b3063c493f080d16411f5', 'N', 'N', 'Y');

insert into user (user_id, username, password, expired, locked, enabled)
values (set(@user_id_3, next value for user_id_seq), 'jane.doe', '30dd37e81704bbbd4e235c22990802ae25b187da	', 'N', 'N', 'Y');

insert into user (user_id, username, password, expired, locked, enabled)
values (set(@user_id_4, next value for user_id_seq), 'george.doe', '452802e3f2ff8b7b28785f50dfaaaaf80fc1430f', 'N', 'N', 'Y');

insert into user (user_id, username, password, expired, locked, enabled)
values (set(@user_id_5, next value for user_id_seq),'mario.rossi', '8aadae9f6e73a479cb8a565bcfa6e8de2b074e89', 'N', 'N', 'Y');

insert into user (user_id, username, password, expired, locked, enabled)
values (set(@user_id_6, next value for user_id_seq), 'maija.m', '18550acca1b36af6cfa41c82e1caab12073475a1', 'N', 'N', 'Y');

insert into user (user_id, username, password, expired, locked, enabled)
values (set(@user_id_6, next value for user_id_seq), 'http://rave2011.myopenid.com/', 'cdf15c184b7d2539b0cfc29ee9f10bad62793d50', 'N', 'N', 'Y');

insert into user (user_id, username, password, expired, locked, enabled)
values (set(@user_id_7, next value for user_id_seq), 'one.col', '07acee6193e84ba9ae2f7b2bf26538f2d6e4b0a1', 'N', 'N', 'Y');

insert into user (user_id, username, password, expired, locked, enabled)
values (set(@user_id_8, next value for user_id_seq), 'twown.col', '24159ea43cbcecb50021cd14a1d41a8079fd9714', 'N', 'N', 'Y');

insert into user (user_id, username, password, expired, locked, enabled)
values (set(@user_id_9, next value for user_id_seq), 'three.col', 'c736434430af90772bfd4351bffa3da04cec0403', 'N', 'N', 'Y');

insert into user (user_id, username, password, expired, locked, enabled)
values (set(@user_id_10, next value for user_id_seq), 'threewn.col', 'ad67065a5bc25f86036508971a09a58e9c9131e8', 'N', 'N', 'Y');

insert into user (user_id, username, password, expired, locked, enabled)
values (set(@user_id_11, next value for user_id_seq), 'four.col', 'c875ce4416fc56cd34c01bd366a3af5468137155', 'N', 'N', 'Y');

insert into user (user_id, username, password, expired, locked, enabled)
values (set(@user_id_12, next value for user_id_seq), 'fourwn.col', 'eb0b450eff79a33027a41a05051f5609a83667e8', 'N', 'N', 'Y');
--- end user data ---

--- gadget data ---
-- wikipedia widget
insert into widget (id, title, url, type, description, author)
values(set(@wikipedia_widget_id, next value for widget_id_seq), 'Wikipedia','http://www.widget-dico.com/wikipedia/google/wikipedia.xml', 'OpenSocial', 'A Wikipedia Search and Go widget. Language choice.', 'WidgetMe');

-- translate widget
insert into widget (id, title, url, type, description, author)
values(set(@translate_widget_id, next value for widget_id_seq), 'Translate Gadget', 'http://www.gstatic.com/ig/modules/dictionary/dictionary.xml','OpenSocial' , 'Google Translation gadget.', 'Google Taiwan');

-- nytimes widget
insert into widget (id, title, url, type)
values(set(@nyt_widget_id, next value for widget_id_seq), 'NYTimes.com - Top Stories', 'http://widgets.nytimes.com/packages/html/igoogle/topstories.xml', 'OpenSocial');

-- google tabbed news widget
insert into widget (id, title, url, type)
values(set(@tabnews_widget_id, next value for widget_id_seq), 'Google News Gadget', 'http://www.gstatic.com/ig/modules/tabnews/tabnews.xml', 'OpenSocial');
-- end widget data ----

-- hamster widget
insert into widget (id, title, url, type)
values(set(@hamster_widget_id, next value for widget_id_seq), 'Pet Hamster', 'http://hosting.gmodules.com/ig/gadgets/file/112581010116074801021/hamster.xml', 'OpenSocial');

-- slideshare widget
insert into widget (id, title, url, type)
values(set(@gifts_widget_id, next value for widget_id_seq), 'Gifts', 'http://opensocial-resources.googlecode.com/svn/samples/tutorial/tags/api-0.8/gifts_1_friends.xml', 'OpenSocial');
-- end widget data ----

--- start page layout data ---
insert into page_layout (id, code,  number_of_regions)
values (set(@one_col_id, next value for page_layout_id_seq), 'columns_1', 1);
insert into page_layout (id, code,  number_of_regions)
values (set(@two_col_id, next value for page_layout_id_seq), 'columns_2', 2);
insert into page_layout (id, code,  number_of_regions)
values (set(@twown_col_id, next value for page_layout_id_seq), 'columns_2wn', 2);
insert into page_layout (id, code,  number_of_regions)
values (set(@three_col_id, next value for page_layout_id_seq), 'columns_3', 3);
insert into page_layout (id, code,  number_of_regions)
values (set(@threewn_col_id, next value for page_layout_id_seq), 'columns_3nwn', 3);
insert into page_layout (id, code,  number_of_regions)
values (next value for page_layout_id_seq, 'columns_4', 4);
insert into page_layout (id, code,  number_of_regions)
values (next value for page_layout_id_seq, 'columns_3nwn_1_bottom', 4);
--- end page layout data ----


--- Layout for user_id_1 ---
INSERT INTO page (id, name, owner_id, render_sequence, page_layout_id)
values (set(@page_1_id, next value for page_id_seq), 'Main', @user_id_1, 1, @two_col_id);

INSERT INTO region(id, page_id)
values (set(@page_1_region_1, next value for region_id_seq), @page_1_id);
INSERT INTO region(id, page_id)
values (set(@page_1_region_2, next value for region_id_seq), @page_1_id);

INSERT INTO region_widget(id, widget_id, region_id, render_order, collapsed)
values (next value for region_widget_id_seq, @wikipedia_widget_id, @page_1_region_1, 0, 'N');
INSERT INTO region_widget(id, widget_id, region_id, render_order, collapsed)
values (next value for region_widget_id_seq, @translate_widget_id, @page_1_region_1, 1, 'N');

INSERT INTO region_widget(id, widget_id, region_id, render_order, collapsed)
values (next value for region_widget_id_seq, @nyt_widget_id, @page_1_region_2, 0, 'N');
INSERT INTO region_widget(id, widget_id, region_id, render_order, collapsed)
values (next value for region_widget_id_seq, @tabnews_widget_id, @page_1_region_2, 1, 'N');

INSERT INTO page (id, name, owner_id, render_sequence, page_layout_id)
values (set(@page_1_id, next value for page_id_seq), 'Social', @user_id_1, 1, @two_col_id);

INSERT INTO region(id, page_id)
values (set(@page_1_region_1, next value for region_id_seq), @page_1_id);
INSERT INTO region(id, page_id)
values (set(@page_1_region_2, next value for region_id_seq), @page_1_id);

INSERT INTO region_widget(id, widget_id, region_id, render_order, collapsed)
values (next value for region_widget_id_seq, @nyt_widget_id, @page_1_region_1, 0, 'N');
INSERT INTO region_widget(id, widget_id, region_id, render_order, collapsed)
values (next value for region_widget_id_seq, @translate_widget_id, @page_1_region_1, 1, 'N');

INSERT INTO region_widget(id, widget_id, region_id, render_order, collapsed)
values (next value for region_widget_id_seq, @wikipedia_widget_id, @page_1_region_2, 0, 'N');
INSERT INTO region_widget(id, widget_id, region_id, render_order, collapsed)
values (next value for region_widget_id_seq, @tabnews_widget_id, @page_1_region_2, 1, 'N');

--- End canonical user_id_1 layout ---

--- Layout for user_id_2 ---
INSERT INTO page (id, name, owner_id, render_sequence, page_layout_id)
values (set(@page_1_id, next value for page_id_seq), 'Main', @user_id_2, 1, @two_col_id);

INSERT INTO region(id, page_id)
values (set(@page_1_region_1, next value for region_id_seq), @page_1_id);
INSERT INTO region(id, page_id)
values (set(@page_1_region_2, next value for region_id_seq), @page_1_id);

--INSERT INTO region_widget(id, widget_id, region_id, render_order, collapsed)
--values (next value for region_widget_id_seq, @hamster_widget_id, @page_1_region_1, 0, 'N');
INSERT INTO region_widget(id, widget_id, region_id, render_order, collapsed)
values (next value for region_widget_id_seq, @translate_widget_id, @page_1_region_1, 0, 'N');
INSERT INTO region_widget(id, widget_id, region_id, render_order, collapsed)
values (next value for region_widget_id_seq, @wikipedia_widget_id, @page_1_region_1, 1, 'N');

INSERT INTO region_widget(id, widget_id, region_id, render_order, collapsed)
values (next value for region_widget_id_seq, @nyt_widget_id, @page_1_region_2, 0, 'N');
INSERT INTO region_widget(id, widget_id, region_id, render_order, collapsed)
values (next value for region_widget_id_seq, @tabnews_widget_id, @page_1_region_2, 1, 'N');
--- End canonical user_id_2 layout ---

--- Layout for user_id_3 ---
INSERT INTO page (id, name, owner_id, render_sequence, page_layout_id)
values (set(@page_1_id, next value for page_id_seq), 'Main', @user_id_3, 1, @two_col_id);

INSERT INTO region(id, page_id)
values (set(@page_1_region_1, next value for region_id_seq), @page_1_id);
INSERT INTO region(id, page_id)
values (set(@page_1_region_2, next value for region_id_seq), @page_1_id);

INSERT INTO region_widget(id, widget_id, region_id, render_order, collapsed)
values (next value for region_widget_id_seq, @wikipedia_widget_id, @page_1_region_1, 0, 'N');
INSERT INTO region_widget(id, widget_id, region_id, render_order, collapsed)
values (next value for region_widget_id_seq, @translate_widget_id, @page_1_region_1, 1, 'N');

INSERT INTO region_widget(id, widget_id, region_id, render_order, collapsed)
values (next value for region_widget_id_seq, @hamster_widget_id, @page_1_region_2, 0, 'N');
INSERT INTO region_widget(id, widget_id, region_id, render_order, collapsed)
values (next value for region_widget_id_seq, @tabnews_widget_id, @page_1_region_2, 1, 'N');
--- End canonical user_id_3 layout ---

--- Layout for user_id_6 ---
INSERT INTO page (id, name, owner_id, render_sequence, page_layout_id)
values (set(@page_1_id, next value for page_id_seq), 'Main', @user_id_6, 1, @two_col_id);

INSERT INTO region(id, page_id)
values (set(@page_1_region_1, next value for region_id_seq), @page_1_id);
INSERT INTO region(id, page_id)
values (set(@page_1_region_2, next value for region_id_seq), @page_1_id);

INSERT INTO region_widget(id, widget_id, region_id, render_order, collapsed)
values (next value for region_widget_id_seq, @wikipedia_widget_id, @page_1_region_1, 0, 'N');
INSERT INTO region_widget(id, widget_id, region_id, render_order, collapsed)
values (next value for region_widget_id_seq, @translate_widget_id, @page_1_region_1, 1, 'N');

INSERT INTO region_widget(id, widget_id, region_id, render_order, collapsed)
values (next value for region_widget_id_seq, @hamster_widget_id, @page_1_region_2, 0, 'N');
INSERT INTO region_widget(id, widget_id, region_id, render_order, collapsed)
values (next value for region_widget_id_seq, @tabnews_widget_id, @page_1_region_2, 1, 'N');
--- End canonical user_id_6 layout ---

--- Layout for user_id_7 ---
INSERT INTO page (id, name, owner_id, render_sequence, page_layout_id)
values (set(@page_7_id, next value for page_id_seq), 'Main', @user_id_7, 1, @one_col_id);

INSERT INTO region(id, page_id)
values (set(@page_7_region_1, next value for region_id_seq), @page_7_id);

INSERT INTO region_widget(id, widget_id, region_id, render_order, collapsed)
values (next value for region_widget_id_seq, @hamster_widget_id, @page_7_region_1, 0, 'N');
INSERT INTO region_widget(id, widget_id, region_id, render_order, collapsed)
values (next value for region_widget_id_seq, @tabnews_widget_id, @page_7_region_1, 1, 'N');
--- End asgoyal.one user_id_7 layout ---

--- Layout for user_id_8 ---
INSERT INTO page (id, name, owner_id, render_sequence, page_layout_id)
values (set(@page_8_id, next value for page_id_seq), 'Main', @user_id_8, 1, @twown_col_id);

INSERT INTO region(id, page_id)
values (set(@page_8_region_1, next value for region_id_seq), @page_8_id);
INSERT INTO region(id, page_id)
values (set(@page_8_region_2, next value for region_id_seq), @page_8_id);

INSERT INTO region_widget(id, widget_id, region_id, render_order, collapsed)
values (next value for region_widget_id_seq, @hamster_widget_id, @page_8_region_1, 0, 'N');
INSERT INTO region_widget(id, widget_id, region_id, render_order, collapsed)
values (next value for region_widget_id_seq, @tabnews_widget_id, @page_8_region_1, 1, 'N');

INSERT INTO region_widget(id, widget_id, region_id, render_order, collapsed)
values (next value for region_widget_id_seq, @wikipedia_widget_id, @page_8_region_2, 0, 'N');
INSERT INTO region_widget(id, widget_id, region_id, render_order, collapsed)
values (next value for region_widget_id_seq, @translate_widget_id, @page_8_region_2, 1, 'N');
--- End asgoyal.twown user_id_8 layout ---

--- Layout for user_id_9 ---
INSERT INTO page (id, name, owner_id, render_sequence, page_layout_id)
values (set(@page_9_id, next value for page_id_seq), 'Main', @user_id_9, 1, @three_col_id);

INSERT INTO region(id, page_id)
values (set(@page_9_region_1, next value for region_id_seq), @page_9_id);
INSERT INTO region(id, page_id)
values (set(@page_9_region_2, next value for region_id_seq), @page_9_id);
INSERT INTO region(id, page_id)
values (set(@page_9_region_3, next value for region_id_seq), @page_9_id);

INSERT INTO region_widget(id, widget_id, region_id, render_order, collapsed)
values (next value for region_widget_id_seq, @hamster_widget_id, @page_9_region_2, 0, 'N');
INSERT INTO region_widget(id, widget_id, region_id, render_order, collapsed)
values (next value for region_widget_id_seq, @tabnews_widget_id, @page_9_region_3, 1, 'N');

INSERT INTO region_widget(id, widget_id, region_id, render_order, collapsed)
values (next value for region_widget_id_seq, @wikipedia_widget_id, @page_9_region_1, 0, 'N');
INSERT INTO region_widget(id, widget_id, region_id, render_order, collapsed)
values (next value for region_widget_id_seq, @translate_widget_id, @page_9_region_1, 1, 'N');
--- End asgoyal.three user_id_9 layout ---

--- Layout for user_id_10 ---
INSERT INTO page (id, name, owner_id, render_sequence, page_layout_id)
values (set(@page_10_id, next value for page_id_seq), 'Main', @user_id_10, 1, @threewn_col_id);

INSERT INTO region(id, page_id)
values (set(@page_10_region_1, next value for region_id_seq), @page_10_id);
INSERT INTO region(id, page_id)
values (set(@page_10_region_2, next value for region_id_seq), @page_10_id);
INSERT INTO region(id, page_id)
values (set(@page_10_region_3, next value for region_id_seq), @page_10_id);

INSERT INTO region_widget(id, widget_id, region_id, render_order, collapsed)
values (next value for region_widget_id_seq, @hamster_widget_id, @page_10_region_2, 0, 'N');
INSERT INTO region_widget(id, widget_id, region_id, render_order, collapsed)
values (next value for region_widget_id_seq, @tabnews_widget_id, @page_10_region_3, 1, 'N');

INSERT INTO region_widget(id, widget_id, region_id, render_order, collapsed)
values (next value for region_widget_id_seq, @wikipedia_widget_id, @page_10_region_1, 0, 'N');
INSERT INTO region_widget(id, widget_id, region_id, render_order, collapsed)
values (next value for region_widget_id_seq, @translate_widget_id, @page_10_region_1, 1, 'N');
--- End asgoyal.threewn user_id_10 layout ---

--- Layout for user_id_11 ---
INSERT INTO page (id, name, owner_id, render_sequence, page_layout_id)
values (set(@page_11_id, next value for page_id_seq), 'Main', @user_id_11, 1, @four_col_id);

INSERT INTO region(id, page_id)
values (set(@page_11_region_1, next value for region_id_seq), @page_11_id);
INSERT INTO region(id, page_id)
values (set(@page_11_region_2, next value for region_id_seq), @page_11_id);
INSERT INTO region(id, page_id)
values (set(@page_11_region_3, next value for region_id_seq), @page_11_id);
INSERT INTO region(id, page_id)
values (set(@page_11_region_4, next value for region_id_seq), @page_11_id);

INSERT INTO region_widget(id, widget_id, region_id, render_order, collapsed)
values (next value for region_widget_id_seq, @hamster_widget_id, @page_11_region_4, 0, 'N');
INSERT INTO region_widget(id, widget_id, region_id, render_order, collapsed)
values (next value for region_widget_id_seq, @tabnews_widget_id, @page_11_region_3, 1, 'N');

INSERT INTO region_widget(id, widget_id, region_id, render_order, collapsed)
values (next value for region_widget_id_seq, @wikipedia_widget_id, @page_11_region_2, 0, 'N');
INSERT INTO region_widget(id, widget_id, region_id, render_order, collapsed)
values (next value for region_widget_id_seq, @translate_widget_id, @page_11_region_1, 1, 'N');
--- End asgoyal.four user_id_11 layout ---

--- Layout for user_id_12 ---
INSERT INTO page (id, name, owner_id, render_sequence, page_layout_id)
values (set(@page_12_id, next value for page_id_seq), 'Main', @user_id_12, 1, @fourwn_col_id);

INSERT INTO region(id, page_id)
values (set(@page_12_region_1, next value for region_id_seq), @page_12_id);
INSERT INTO region(id, page_id)
values (set(@page_12_region_2, next value for region_id_seq), @page_12_id);
INSERT INTO region(id, page_id)
values (set(@page_12_region_3, next value for region_id_seq), @page_12_id);
INSERT INTO region(id, page_id)
values (set(@page_12_region_4, next value for region_id_seq), @page_12_id);

INSERT INTO region_widget(id, widget_id, region_id, render_order, collapsed)
values (next value for region_widget_id_seq, @hamster_widget_id, @page_12_region_4, 0, 'N');
INSERT INTO region_widget(id, widget_id, region_id, render_order, collapsed)
values (next value for region_widget_id_seq, @tabnews_widget_id, @page_12_region_3, 1, 'N');

INSERT INTO region_widget(id, widget_id, region_id, render_order, collapsed)
values (next value for region_widget_id_seq, @wikipedia_widget_id, @page_12_region_2, 0, 'N');
INSERT INTO region_widget(id, widget_id, region_id, render_order, collapsed)
values (next value for region_widget_id_seq, @translate_widget_id, @page_12_region_1, 1, 'N');
--- End asgoyal.fourwn user_id_12 layout ---