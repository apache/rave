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



 -- ***********************************************************************************
 -- The initial data is used for demo and test purposes with an in memory H2 database.
 -- It is not guaranteed that the queries will work on other database systems.
 -- ***********************************************************************************

 -- ***********************************************************************************
-- Manually create table indices
set @page_seq = 'page';
set @page_layout_seq = 'page_layout';
set @region_seq = 'region';
set @region_widget_seq = 'region_widget';
set @user_seq = 'person';
set @widget_seq = 'widget';
set @granted_authority_seq = 'granted_authority';
set @widget_comment_seq = 'widget_comment';
set @widget_rating_seq = 'widget_rating';
set @portal_preference_seq = 'portal_preference';

CREATE TABLE IF NOT EXISTS RAVE_PORTAL_SEQUENCES (seq_name VARCHAR(255) PRIMARY KEY NOT NULL, seq_count BIGINT(19));
INSERT INTO RAVE_PORTAL_SEQUENCES(seq_name, seq_count) values (@page_seq, 1);
INSERT INTO RAVE_PORTAL_SEQUENCES(seq_name, seq_count) values (@page_layout_seq, 1);
INSERT INTO RAVE_PORTAL_SEQUENCES(seq_name, seq_count) values (@region_seq, 1);
INSERT INTO RAVE_PORTAL_SEQUENCES(seq_name, seq_count) values (@region_widget_seq, 1);
INSERT INTO RAVE_PORTAL_SEQUENCES(seq_name, seq_count) values ('region_widget_preference', 1);
INSERT INTO RAVE_PORTAL_SEQUENCES(seq_name, seq_count) values (@user_seq, 1);
INSERT INTO RAVE_PORTAL_SEQUENCES(seq_name, seq_count) values (@widget_seq, 1);
INSERT INTO RAVE_PORTAL_SEQUENCES(seq_name, seq_count) values (@widget_comment_seq, 1);
INSERT INTO RAVE_PORTAL_SEQUENCES(seq_name, seq_count) values (@widget_rating_seq, 1);
INSERT INTO RAVE_PORTAL_SEQUENCES(seq_name, seq_count) values (@granted_authority_seq, 1);
INSERT INTO RAVE_PORTAL_SEQUENCES(seq_name, seq_count) values (@portal_preference_seq, 1);

  -- ***********************************************************************************
  -- start page layout data, required to make the portal work ---
set @one_col_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @page_layout_seq);
insert into page_layout (entity_id, code,  number_of_regions)
values (@one_col_id, 'columns_1', 1);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_layout_seq;

set @two_col_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @page_layout_seq);
insert into page_layout (entity_id, code,  number_of_regions)
values (@two_col_id, 'columns_2', 2);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_layout_seq;

set @twown_col_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @page_layout_seq);
insert into page_layout (entity_id, code,  number_of_regions)
values (@twown_col_id, 'columns_2wn', 2);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_layout_seq;

set @three_col_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @page_layout_seq);
insert into page_layout (entity_id, code,  number_of_regions)
values (@three_col_id, 'columns_3', 3);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_layout_seq;

set @threewn_col_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @page_layout_seq);
insert into page_layout (entity_id, code,  number_of_regions)
values (@threewn_col_id, 'columns_3nwn', 3);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_layout_seq;

set @four_col_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @page_layout_seq);
insert into page_layout (entity_id, code,  number_of_regions)
values (@four_col_id, 'columns_4', 4);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_layout_seq;

set @fourwn_col_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @page_layout_seq);
insert into page_layout (entity_id, code,  number_of_regions)
values (@fourwn_col_id, 'columns_3nwn_1_bottom', 4);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_layout_seq;
--- end page layout data ----

  -- ***********************************************************************************
  --- start user data ---
set @user_id_1 = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @user_seq);
insert into person (entity_id, username, password, expired, locked, enabled, email, default_page_layout_id, dtype)
values (@user_id_1, 'canonical', 'b97fd0fa25ba8a504309be2b6651ac6dee167ded', FALSE, FALSE, TRUE,'canonical@example.com', @three_col_id, 'User');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @user_seq;

set @user_id_2 = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @user_seq);
insert into person (entity_id, username, password, expired, locked, enabled, email, default_page_layout_id, dtype)
values (@user_id_2, 'john.doe', '49e5f5c7c7ae8372af9b3063c493f080d16411f5', FALSE, FALSE, TRUE,'john.doe@example.com', @three_col_id, 'User');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @user_seq;

set @user_id_3 = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @user_seq);
insert into person (entity_id, username, password, expired, locked, enabled, email, default_page_layout_id, dtype)
values (@user_id_3, 'jane.doe', '30dd37e81704bbbd4e235c22990802ae25b187da', FALSE, FALSE, TRUE,'jane.doe@example.net', @three_col_id, 'User');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @user_seq;

set @user_id_4 = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @user_seq);
insert into person (entity_id, username, password, expired, locked, enabled, email, default_page_layout_id, dtype)
values (@user_id_4, 'george.doe', '452802e3f2ff8b7b28785f50dfaaaaf80fc1430f', FALSE, FALSE, TRUE,'george.doe@example.org', @three_col_id, 'User');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @user_seq;

set @user_id_5 = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @user_seq);
insert into person (entity_id, username, password, expired, locked, enabled, email, default_page_layout_id, dtype)
values (@user_id_5,'mario.rossi', '8aadae9f6e73a479cb8a565bcfa6e8de2b074e89', FALSE, FALSE, TRUE,'mario.rossi@example.com', @three_col_id, 'User');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @user_seq;

set @user_id_6 = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @user_seq);
insert into person (entity_id, username, password, expired, locked, enabled, email, default_page_layout_id, dtype)
values (@user_id_6, 'maija.m', '18550acca1b36af6cfa41c82e1caab12073475a1', FALSE, FALSE, TRUE,'maijam@example.com', @three_col_id, 'User');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @user_seq;

set @user_id_7 = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @user_seq);
insert into person (entity_id, username, password, expired, locked, enabled, email, default_page_layout_id, dtype)
values (@user_id_7, 'one.col', '07acee6193e84ba9ae2f7b2bf26538f2d6e4b0a1', FALSE, FALSE, TRUE,'one.col@example.com', @three_col_id, 'User');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @user_seq;

set @user_id_8 = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @user_seq);
insert into person (entity_id, username, password, expired, locked, enabled, email, default_page_layout_id, dtype)
values (@user_id_8, 'twown.col', '24159ea43cbcecb50021cd14a1d41a8079fd9714', FALSE, FALSE, TRUE,'twown.col@example.com', @three_col_id, 'User');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @user_seq;

set @user_id_9 = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @user_seq);
insert into person (entity_id, username, password, expired, locked, enabled, email, default_page_layout_id, dtype)
values (@user_id_9, 'three.col', 'c736434430af90772bfd4351bffa3da04cec0403', FALSE, FALSE, TRUE,'three.col@example.com', @three_col_id, 'User');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @user_seq;

set @user_id_10 = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @user_seq);
insert into person (entity_id, username, password, expired, locked, enabled, email, default_page_layout_id, dtype)
values (@user_id_10, 'threewn.col', 'ad67065a5bc25f86036508971a09a58e9c9131e8', FALSE, FALSE, TRUE,'threewn.col@example.com', @three_col_id, 'User');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @user_seq;

set @user_id_11 = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @user_seq);
insert into person (entity_id, username, password, expired, locked, enabled, email, default_page_layout_id, dtype)
values (@user_id_11, 'four.col', 'c875ce4416fc56cd34c01bd366a3af5468137155', FALSE, FALSE, TRUE,'four.col@example.com', @three_col_id, 'User');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @user_seq;

set @user_id_12 = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @user_seq);
insert into person (entity_id, username, password, expired, locked, enabled, email, default_page_layout_id, dtype)
values (@user_id_12, 'fourwn.col', 'eb0b450eff79a33027a41a05051f5609a83667e8', FALSE, FALSE, TRUE,'fourwn.col@example.com', @three_col_id, 'User');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @user_seq;

-- duplicate user id!!
set @user_id_13 = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @user_seq);
insert into person (entity_id, username, password, expired, locked, enabled, email, default_page_layout_id, dtype)
values (@user_id_13, 'http://rave2011.myopenid.com/', 'cdf15c184b7d2539b0cfc29ee9f10bad62793d50', FALSE, FALSE, TRUE,'rave2011_openid@example.org', @three_col_id, 'User');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @user_seq;

--- end user data ---

--- gadget data ---
-- wikipedia widget
set @wikipedia_widget_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @widget_seq);
insert into widget (entity_id, title, url, type, description, author, widget_status, owner_id)
values(@wikipedia_widget_id, 'Wikipedia','http://www.widget-dico.com/wikipedia/google/wikipedia.xml', 'OpenSocial', 'A Wikipedia Search and Go widget. Language choice.', 'WidgetMe', 'PUBLISHED', @user_id_2);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @widget_seq;

-- translate widget
set @translate_widget_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @widget_seq);
insert into widget (entity_id, title, url, type, description, author, widget_status, owner_id)
values(@translate_widget_id, 'Translate Gadget', 'http://www.gstatic.com/ig/modules/dictionary/dictionary.xml','OpenSocial' , 'Google Translation gadget.', 'Google Taiwan', 'PUBLISHED', @user_id_1);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @widget_seq;

-- nytimes widget
set @nyt_widget_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @widget_seq);
insert into widget (entity_id, title, url, type, widget_status, owner_id)
values(@nyt_widget_id, 'NYTimes.com - Top Stories', 'http://widgets.nytimes.com/packages/html/igoogle/topstories.xml', 'OpenSocial', 'PUBLISHED', @user_id_1);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @widget_seq;

-- google tabbed news widget
set @tabnews_widget_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @widget_seq);
insert into widget (entity_id, title, url, type, widget_status, owner_id)
values(@tabnews_widget_id, 'Google News Gadget', 'http://www.gstatic.com/ig/modules/tabnews/tabnews.xml', 'OpenSocial', 'PUBLISHED', @user_id_1);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @widget_seq;

-- hamster widget
set @hamster_widget_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @widget_seq);
insert into widget (entity_id, title, url, type, widget_status, thumbnail_url, owner_id)
values(@hamster_widget_id, 'Pet Hamster', 'http://hosting.gmodules.com/ig/gadgets/file/112581010116074801021/hamster.xml', 'OpenSocial', 'PUBLISHED', 'http://hosting.gmodules.com/ig/gadgets/file/112581010116074801021/hamsterThumb.png', @user_id_1);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @widget_seq;

-- another hamster widget
set @another_hamster_widget_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @widget_seq);
insert into widget (entity_id, title, url, type, description, author, widget_status, thumbnail_url, screenshot_url, owner_id)
values(@another_hamster_widget_id, 'Herbie Hamster Virtual Pet', 'http://hosting.gmodules.com/ig/gadgets/file/109548057311228444554/hamster.xml', 'OpenSocial', 'A cute little hamster for you to feed and look after. Watch him follow your cursor around. Click on the more tab to treat him to a strawberry. Click him then put him on the wheel and watch him play! ***NEW: make Herbie hamster your very own!', 'Naj', 'PUBLISHED', 'http://sites.google.com/site/najartsist/pets-1/herbiet.png', 'http://sites.google.com/site/najartsist/herbie-hamster/herbie.png', @user_id_1);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @widget_seq;

-- slideshare widget
set @gifts_widget_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @widget_seq);
insert into widget (entity_id, title, url, type, widget_status, owner_id)
values(@gifts_widget_id, 'Gifts', 'http://opensocial-resources.googlecode.com/svn/samples/tutorial/tags/api-0.8/gifts_1_friends.xml', 'OpenSocial', 'PUBLISHED', @user_id_1);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @widget_seq;

-- demo widgets from rave-demos
-- CTSS resource google map
set @ctss_resources_widget_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @widget_seq);
insert into widget (entity_id, title, url, type, description, author, widget_status, thumbnail_url, screenshot_url, owner_id)
values(@ctss_resources_widget_id, 'List of CTSS Resources - Map View', 'http://localhost:8080/demogadgets/CTSSResourcesMapView.xml', 'OpenSocial', 'This is a gadget developed for Teragrid - OGCE project. Used Google gadgets API to retrieve the information from the Information Services REST Web Service and display the information using Google Maps API. This is a list of available CTSS resources and its details', 'Suresh Deivasigamani', 'PUBLISHED', 'http://img695.imageshack.us/img695/2726/ctssresourcesmapviewscr.png', 'http://img704.imageshack.us/img704/444/ctssresourcesmapview.png', @user_id_1);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @widget_seq;

-- Twitter Gadget
set @twitter_widget_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @widget_seq);
insert into widget (entity_id, title, url, type, description, author, widget_status, thumbnail_url, screenshot_url, owner_id)
values(@twitter_widget_id, 'Twitter', 'http://localhost:8080/demogadgets/twitter.xml', 'OpenSocial', 'Fully functional, lightweight, AJAX-based twitter user interface with many configuration options including user specified auto-refresh rate, full timeline, pagination, and more.  Control display elements such as user thumbnails, date stamps, and post source.  Specify gadget size based on availble screen footprint, even incorporate into your Gmail account.  Insert symbols, dingbats and emoticons into your tweets using the TwitterGadget Symbols pulldown menu.', 'LOGIKA Corporation', 'PUBLISHED', 'http://www.twittergadget.com/images/thumbnail2.png', 'http://www.twittergadget.com/images/thumbnail2.png', @user_id_1);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @widget_seq;

-- Youtube Gadget
set @youtube_widget_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @widget_seq);
insert into widget (entity_id, title, url, type, description, author, widget_status, thumbnail_url, screenshot_url, owner_id)
values(@youtube_widget_id, 'Youtube', 'http://localhost:8080/demogadgets/youtubesearch.xml', 'OpenSocial', 'A search module, which searches YouTube by tags like Politics News Life Music Family Photography Art Random Travel Personal Religion Movies Business Thoughts Media Humor Culture Poetry Christmas Writing Books Food Friends.', 'David Olsen', 'PUBLISHED', 'http://www.zytu.com/youtube/youtubesearchthumb.png', 'http://www.zytu.com/youtube/youtubesearchscreen.png', @user_id_1);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @widget_seq;

-- View information
set @gadgetview_widget_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @widget_seq);
insert into widget (entity_id, title, url, type, widget_status, owner_id)
values(@gadgetview_widget_id, 'Gadget View Type', 'http://localhost:8080/demogadgets/canvas-nav.xml', 'OpenSocial', 'PUBLISHED', @user_id_1);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @widget_seq;

-- View information
set @user_prefs_demo_widget_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @widget_seq);
insert into widget (entity_id, title, url, type, widget_status, description, author, owner_id)
values(@user_prefs_demo_widget_id, 'User Prefs Demo', 'http://localhost:8080/demogadgets/user_prefs_demo.xml', 'OpenSocial', 'PUBLISHED', 'An example gadget which demos some of the different capabilities of user preferences.', 'Anthony Carlucci', @user_id_1);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @widget_seq;

-- end widget data ----

-- User layouts

--- Layout for user_id_1 ---
set @page_1_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @page_seq);
INSERT INTO page (entity_id, name, owner_id, render_sequence, page_layout_id)
values (@page_1_id, 'Main', @user_id_1, 1, @two_col_id);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_seq;


set @page_1_region_1 = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_seq);
INSERT INTO region(entity_id, page_id, render_order)
values (@page_1_region_1, @page_1_id, 1);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_seq;

set @page_1_region_2 = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_seq);
INSERT INTO region(entity_id, page_id, render_order)
values (@page_1_region_2, @page_1_id, 2);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_seq;


set @next_region_widget = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_widget_seq);
INSERT INTO region_widget(entity_id, widget_id, region_id, render_order, collapsed)
values (@next_region_widget, @wikipedia_widget_id, @page_1_region_1, 0, FALSE);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_widget_seq;

set @next_region_widget = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_widget_seq);
INSERT INTO region_widget(entity_id, widget_id, region_id, render_order, collapsed)
values (@next_region_widget, @translate_widget_id, @page_1_region_1, 1, FALSE);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_widget_seq;

set @next_region_widget = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_widget_seq);
INSERT INTO region_widget(entity_id, widget_id, region_id, render_order, collapsed)
values (@next_region_widget, @nyt_widget_id, @page_1_region_2, 0, FALSE);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_widget_seq;

set @next_region_widget = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_widget_seq);
INSERT INTO region_widget(entity_id, widget_id, region_id, render_order, collapsed)
values (@next_region_widget, @tabnews_widget_id, @page_1_region_2, 1, FALSE);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_widget_seq;

set @next_widget_rating = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @widget_rating_seq);
INSERT INTO widget_rating(entity_id, widget_id, user_id, score)
values (@next_widget_rating, 1, 1, 0);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @widget_rating_seq;

set @next_widget_rating = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @widget_rating_seq);
INSERT INTO widget_rating(entity_id, widget_id, user_id, score)
values (@next_widget_rating, 2, 1, 10);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @widget_rating_seq;

set @page_2_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @page_seq);
INSERT INTO page (entity_id, name, owner_id, render_sequence, page_layout_id)
values (@page_2_id, 'Social', @user_id_1, 2, @two_col_id);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_seq;

set @page_2_region_1 = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_seq);
INSERT INTO region(entity_id, page_id, render_order)
values (@page_2_region_1, @page_2_id, 1);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_seq;

set @page_2_region_2 = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_seq);
INSERT INTO region(entity_id, page_id, render_order)
values (@page_2_region_2, @page_2_id, 2);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_seq;

set @next_region_widget = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_widget_seq);
INSERT INTO region_widget(entity_id, widget_id, region_id, render_order, collapsed)
values (@next_region_widget, @nyt_widget_id, @page_2_region_1, 0, FALSE);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_widget_seq;

set @next_region_widget = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_widget_seq);
INSERT INTO region_widget(entity_id, widget_id, region_id, render_order, collapsed)
values (@next_region_widget, @translate_widget_id, @page_2_region_1, 1, FALSE);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_widget_seq;

set @next_region_widget = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_widget_seq);
INSERT INTO region_widget(entity_id, widget_id, region_id, render_order, collapsed)
values (@next_region_widget, @wikipedia_widget_id, @page_2_region_2, 0, FALSE);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_widget_seq;

set @next_region_widget = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_widget_seq);
INSERT INTO region_widget(entity_id, widget_id, region_id, render_order, collapsed)
values (@next_region_widget, @tabnews_widget_id, @page_2_region_2, 1, FALSE);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_widget_seq;

set @next_widget_comment = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE Seq_name = @widget_comment_seq);
INSERT INTO widget_comment(entity_id, widget_id, user_id, text)
values (@next_widget_comment, 1, 1, 'test comment');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @widget_comment_seq;


set @next_widget_comment = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE Seq_name = @widget_comment_seq);
INSERT INTO widget_comment(entity_id, widget_id, user_id, text)
values (@next_widget_comment, 1, 1, 'another comment');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @widget_comment_seq;
--- End canonical user_id_1 layout ---

--- Layout for user_id_2 ---
set @page_1_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @page_seq);
INSERT INTO page (entity_id, name, owner_id, render_sequence, page_layout_id)
values (@page_1_id, 'Main', @user_id_2, 1, @two_col_id);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_seq;

set @page_1_region_1 = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_seq);
INSERT INTO region(entity_id, page_id, render_order)
values (@page_1_region_1, @page_1_id, 1);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_seq;

set @page_1_region_2 = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_seq);
INSERT INTO region(entity_id, page_id, render_order)
values (@page_1_region_2, @page_1_id, 2);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_seq;

set @next_region_widget = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_widget_seq);
INSERT INTO region_widget(entity_id, widget_id, region_id, render_order, collapsed)
values (@next_region_widget, @translate_widget_id, @page_1_region_1, 0, FALSE);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_widget_seq;

set @next_region_widget = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_widget_seq);
INSERT INTO region_widget(entity_id, widget_id, region_id, render_order, collapsed)
values (@next_region_widget, @wikipedia_widget_id, @page_1_region_1, 1, FALSE);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_widget_seq;

set @next_region_widget = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_widget_seq);
INSERT INTO region_widget(entity_id, widget_id, region_id, render_order, collapsed)
values (@next_region_widget, @nyt_widget_id, @page_1_region_2, 0, FALSE);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_widget_seq;

set @next_region_widget = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_widget_seq);
INSERT INTO region_widget(entity_id, widget_id, region_id, render_order, collapsed)
values (@next_region_widget, @tabnews_widget_id, @page_1_region_2, 1, FALSE);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_widget_seq;
--- End john.doe user_id_2 layout ---

--- Layout for user_id_3 ---
set @page_1_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @page_seq);
INSERT INTO page (entity_id, name, owner_id, render_sequence, page_layout_id)
values (@page_1_id, 'Main', @user_id_3, 1, @two_col_id);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_seq;

set @page_1_region_1 = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_seq);
INSERT INTO region(entity_id, page_id, render_order)
values (@page_1_region_1, @page_1_id, 1);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_seq;

set @page_1_region_2 = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_seq);
INSERT INTO region(entity_id, page_id, render_order)
values (@page_1_region_2, @page_1_id, 2);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_seq;

set @next_region_widget = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_widget_seq);
INSERT INTO region_widget(entity_id, widget_id, region_id, render_order, collapsed)
values (@next_region_widget, @wikipedia_widget_id, @page_1_region_1, 0, FALSE);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_widget_seq;

set @next_region_widget = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_widget_seq);
INSERT INTO region_widget(entity_id, widget_id, region_id, render_order, collapsed)
values (@next_region_widget, @translate_widget_id, @page_1_region_1, 1, FALSE);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_widget_seq;

set @next_region_widget = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_widget_seq);
INSERT INTO region_widget(entity_id, widget_id, region_id, render_order, collapsed)
values (@next_region_widget, @hamster_widget_id, @page_1_region_2, 0, FALSE);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_widget_seq;

set @next_region_widget = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_widget_seq);
INSERT INTO region_widget(entity_id, widget_id, region_id, render_order, collapsed)
values (@next_region_widget, @tabnews_widget_id, @page_1_region_2, 1, FALSE);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_widget_seq;
--- End jane.doe user_id_3 layout ---

--- Layout for user_id_4 ---
set @page_1_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @page_seq);
INSERT INTO page (entity_id, name, owner_id, render_sequence, page_layout_id)
values (@page_1_id, 'Main', @user_id_4, 1, @two_col_id);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_seq;
--- End user_id_4 layout ---

--- Layout for user_id_5 ---
set @page_1_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @page_seq);
INSERT INTO page (entity_id, name, owner_id, render_sequence, page_layout_id)
values (@page_1_id, 'Main', @user_id_5, 1, @two_col_id);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_seq;
--- End user_id_5 layout ---



--- Layout for user_id_6 ---
set @page_1_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @page_seq);
INSERT INTO page (entity_id, name, owner_id, render_sequence, page_layout_id)
values (@page_1_id, 'Main', @user_id_6, 1, @two_col_id);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_seq;

set @page_1_region_1 = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_seq);
INSERT INTO region(entity_id, page_id, render_order)
values (@page_1_region_1, @page_1_id, 1);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_seq;

set @page_1_region_2 = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_seq);
INSERT INTO region(entity_id, page_id, render_order)
values (@page_1_region_2, @page_1_id, 2);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_seq;

set @next_region_widget = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_widget_seq);
INSERT INTO region_widget(entity_id, widget_id, region_id, render_order, collapsed)
values (@next_region_widget, @wikipedia_widget_id, @page_1_region_1, 0, FALSE);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_widget_seq;

set @next_region_widget = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_widget_seq);
INSERT INTO region_widget(entity_id, widget_id, region_id, render_order, collapsed)
values (@next_region_widget, @translate_widget_id, @page_1_region_1, 1, FALSE);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_widget_seq;

set @next_region_widget = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_widget_seq);
INSERT INTO region_widget(entity_id, widget_id, region_id, render_order, collapsed)
values (@next_region_widget, @hamster_widget_id, @page_1_region_2, 0, FALSE);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_widget_seq;

set @next_region_widget = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_widget_seq);
INSERT INTO region_widget(entity_id, widget_id, region_id, render_order, collapsed)
values (@next_region_widget, @tabnews_widget_id, @page_1_region_2, 1, FALSE);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_widget_seq;
--- End user_id_6 layout ---

--- Layout for user_id_7 ---
set @page_7_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @page_seq);
INSERT INTO page (entity_id, name, owner_id, render_sequence, page_layout_id)
values (@page_7_id, 'Main', @user_id_7, 1, @one_col_id);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_seq;

set @page_7_region_1 = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_seq);
INSERT INTO region(entity_id, page_id, render_order)
values (@page_7_region_1, @page_7_id, 1);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_seq;


set @next_region_widget = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_widget_seq);
INSERT INTO region_widget(entity_id, widget_id, region_id, render_order, collapsed)
values (@next_region_widget, @hamster_widget_id, @page_7_region_1, 0, FALSE);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_widget_seq;

set @next_region_widget = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_widget_seq);
INSERT INTO region_widget(entity_id, widget_id, region_id, render_order, collapsed)
values (@next_region_widget, @tabnews_widget_id, @page_7_region_1, 1, FALSE);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_widget_seq;
--- End asgoyal.one user_id_7 layout ---

--- Layout for user_id_8 ---
set @page_8_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @page_seq);
INSERT INTO page (entity_id, name, owner_id, render_sequence, page_layout_id)
values (@page_8_id, 'Main', @user_id_8, 1, @twown_col_id);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_seq;

set @page_8_region_1 = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_seq);
INSERT INTO region(entity_id, page_id, render_order)
values (@page_8_region_1, @page_8_id, 1);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_seq;

set @page_8_region_2 = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_seq);
INSERT INTO region(entity_id, page_id, render_order)
values (@page_8_region_2, @page_8_id, 2);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_seq;

set @next_region_widget = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_widget_seq);
INSERT INTO region_widget(entity_id, widget_id, region_id, render_order, collapsed)
values (@next_region_widget, @hamster_widget_id, @page_8_region_1, 0, FALSE);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_widget_seq;

set @next_region_widget = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_widget_seq);
INSERT INTO region_widget(entity_id, widget_id, region_id, render_order, collapsed)
values (@next_region_widget, @tabnews_widget_id, @page_8_region_1, 1, FALSE);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_widget_seq;

set @next_region_widget = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_widget_seq);
INSERT INTO region_widget(entity_id, widget_id, region_id, render_order, collapsed)
values (@next_region_widget, @wikipedia_widget_id, @page_8_region_2, 0, FALSE);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_widget_seq;

set @next_region_widget = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_widget_seq);
INSERT INTO region_widget(entity_id, widget_id, region_id, render_order, collapsed)
values (@next_region_widget, @translate_widget_id, @page_8_region_2, 1, FALSE);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_widget_seq;
--- End asgoyal.twown user_id_8 layout ---

--- Layout for user_id_9 ---
set @page_9_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @page_seq);
INSERT INTO page (entity_id, name, owner_id, render_sequence, page_layout_id)
values (@page_9_id, 'Main', @user_id_9, 1, @three_col_id);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_seq;

set @page_9_region_1 = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_seq);
INSERT INTO region(entity_id, page_id, render_order)
values (@page_9_region_1, @page_9_id, 1);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_seq;

set @page_9_region_2 = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_seq);
INSERT INTO region(entity_id, page_id, render_order)
values (@page_9_region_2, @page_9_id, 2);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_seq;

set @page_9_region_3 = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_seq);
INSERT INTO region(entity_id, page_id, render_order)
values (@page_9_region_3, @page_9_id, 3);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_seq;

set @next_region_widget = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_widget_seq);
INSERT INTO region_widget(entity_id, widget_id, region_id, render_order, collapsed)
values (@next_region_widget, @hamster_widget_id, @page_9_region_2, 0, FALSE);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_widget_seq;

set @next_region_widget = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_widget_seq);
INSERT INTO region_widget(entity_id, widget_id, region_id, render_order, collapsed)
values (@next_region_widget, @tabnews_widget_id, @page_9_region_3, 1, FALSE);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_widget_seq;

set @next_region_widget = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_widget_seq);
INSERT INTO region_widget(entity_id, widget_id, region_id, render_order, collapsed)
values (@next_region_widget, @wikipedia_widget_id, @page_9_region_1, 0, FALSE);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_widget_seq;

set @next_region_widget = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_widget_seq);
INSERT INTO region_widget(entity_id, widget_id, region_id, render_order, collapsed)
values (@next_region_widget, @translate_widget_id, @page_9_region_1, 1, FALSE);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_widget_seq;
--- End asgoyal.three user_id_9 layout ---

--- Layout for user_id_10 ---
set @page_10_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @page_seq);
INSERT INTO page (entity_id, name, owner_id, render_sequence, page_layout_id)
values (@page_10_id, 'Main', @user_id_10, 1, @threewn_col_id);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_seq;

set @page_10_region_1 = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_seq);
INSERT INTO region(entity_id, page_id, render_order)
values (@page_10_region_1, @page_10_id, 1);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_seq;

set @page_10_region_2 = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_seq);
INSERT INTO region(entity_id, page_id, render_order)
values (@page_10_region_2, @page_10_id, 2);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_seq;

set @page_10_region_3 = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_seq);
INSERT INTO region(entity_id, page_id, render_order)
values (@page_10_region_3, @page_10_id, 3);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_seq;

set @next_region_widget = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_widget_seq);
INSERT INTO region_widget(entity_id, widget_id, region_id, render_order, collapsed)
values (@next_region_widget, @hamster_widget_id, @page_10_region_2, 0, FALSE);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_widget_seq;

set @next_region_widget = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_widget_seq);
INSERT INTO region_widget(entity_id, widget_id, region_id, render_order, collapsed)
values (@next_region_widget, @tabnews_widget_id, @page_10_region_3, 1, FALSE);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_widget_seq;

set @next_region_widget = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_widget_seq);
INSERT INTO region_widget(entity_id, widget_id, region_id, render_order, collapsed)
values (@next_region_widget, @wikipedia_widget_id, @page_10_region_1, 0, FALSE);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_widget_seq;

set @next_region_widget = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_widget_seq);
INSERT INTO region_widget(entity_id, widget_id, region_id, render_order, collapsed)
values (@next_region_widget, @translate_widget_id, @page_10_region_1, 1, FALSE);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_widget_seq;
--- End asgoyal.threewn user_id_10 layout ---

--- Layout for user_id_11 ---
set @page_11_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @page_seq);
INSERT INTO page (entity_id, name, owner_id, render_sequence, page_layout_id)
values (@page_11_id, 'Main', @user_id_11, 1, @four_col_id);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_seq;

set @page_11_region_1 = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_seq);
INSERT INTO region(entity_id, page_id, render_order)
values (@page_11_region_1, @page_11_id, 1);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_seq;

set @page_11_region_2 = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_seq);
INSERT INTO region(entity_id, page_id, render_order)
values (@page_11_region_2, @page_11_id, 2);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_seq;

set @page_11_region_3 = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_seq);
INSERT INTO region(entity_id, page_id, render_order)
values (@page_11_region_3, @page_11_id, 3);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_seq;

set @page_11_region_4 = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_seq);
INSERT INTO region(entity_id, page_id, render_order)
values (@page_11_region_4, @page_11_id, 4);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_seq;

set @next_region_widget = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_widget_seq);
INSERT INTO region_widget(entity_id, widget_id, region_id, render_order, collapsed)
values (@next_region_widget, @hamster_widget_id, @page_11_region_4, 0, FALSE);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_widget_seq;

set @next_region_widget = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_widget_seq);
INSERT INTO region_widget(entity_id, widget_id, region_id, render_order, collapsed)
values (@next_region_widget, @tabnews_widget_id, @page_11_region_3, 1, FALSE);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_widget_seq;

set @next_region_widget = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_widget_seq);
INSERT INTO region_widget(entity_id, widget_id, region_id, render_order, collapsed)
values (@next_region_widget, @wikipedia_widget_id, @page_11_region_2, 0, FALSE);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_widget_seq;

set @next_region_widget = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_widget_seq);
INSERT INTO region_widget(entity_id, widget_id, region_id, render_order, collapsed)
values (@next_region_widget, @translate_widget_id, @page_11_region_1, 1, FALSE);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_widget_seq;

--- End asgoyal.four user_id_11 layout ---

--- Layout for user_id_12 ---
set @page_12_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @page_seq);
INSERT INTO page (entity_id, name, owner_id, render_sequence, page_layout_id)
values (@page_12_id, 'Main', @user_id_12, 1, @fourwn_col_id);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_seq;

set @page_12_region_1 = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_seq);
INSERT INTO region(entity_id, page_id, render_order)
values (@page_12_region_1, @page_12_id, 1);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_seq;

set @page_12_region_2 = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_seq);
INSERT INTO region(entity_id, page_id, render_order)
values (@page_12_region_2, @page_12_id, 2);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_seq;

set @page_12_region_3 = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_seq);
INSERT INTO region(entity_id, page_id, render_order)
values (@page_12_region_3, @page_12_id, 3);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_seq;

set @page_12_region_4 = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_seq);
INSERT INTO region(entity_id, page_id, render_order)
values (@page_12_region_4, @page_12_id, 4);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_seq;

set @next_region_widget = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_widget_seq);
INSERT INTO region_widget(entity_id, widget_id, region_id, render_order, collapsed)
values (@next_region_widget, @hamster_widget_id, @page_12_region_4, 0, FALSE);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_widget_seq;

set @next_region_widget = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_widget_seq);
INSERT INTO region_widget(entity_id, widget_id, region_id, render_order, collapsed)
values (@next_region_widget, @tabnews_widget_id, @page_12_region_3, 1, FALSE);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_widget_seq;

set @next_region_widget = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_widget_seq);
INSERT INTO region_widget(entity_id, widget_id, region_id, render_order, collapsed)
values (@next_region_widget, @wikipedia_widget_id, @page_12_region_2, 0, FALSE);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_widget_seq;

set @next_region_widget = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_widget_seq);
INSERT INTO region_widget(entity_id, widget_id, region_id, render_order, collapsed)
values (@next_region_widget, @translate_widget_id, @page_12_region_1, 1, FALSE);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_widget_seq;
--- End asgoyal.fourwn user_id_12 layout ---

--- Layout for user_id_13 ---
set @page_13_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @page_seq);
INSERT INTO page (entity_id, name, owner_id, render_sequence, page_layout_id)
values (@page_13_id, 'Main', @user_id_13, 1, @fourwn_col_id);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_seq;

set @page_13_region_1 = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_seq);
INSERT INTO region(entity_id, page_id, render_order)
values (@page_13_region_1, @page_13_id, 1);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_seq;

set @page_13_region_2 = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_seq);
INSERT INTO region(entity_id, page_id, render_order)
values (@page_13_region_2, @page_13_id, 2);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_seq;

set @page_13_region_3 = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_seq);
INSERT INTO region(entity_id, page_id, render_order)
values (@page_13_region_3, @page_13_id, 3);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_seq;

set @page_13_region_4 = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_seq);
INSERT INTO region(entity_id, page_id, render_order)
values (@page_13_region_4, @page_13_id, 4);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_seq;

set @next_region_widget = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_widget_seq);
INSERT INTO region_widget(entity_id, widget_id, region_id, render_order, collapsed)
values (@next_region_widget, @hamster_widget_id, @page_13_region_4, 0, FALSE);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_widget_seq;

set @next_region_widget = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_widget_seq);
INSERT INTO region_widget(entity_id, widget_id, region_id, render_order, collapsed)
values (@next_region_widget, @tabnews_widget_id, @page_13_region_3, 1, FALSE);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_widget_seq;

set @next_region_widget = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_widget_seq);
INSERT INTO region_widget(entity_id, widget_id, region_id, render_order, collapsed)
values (@next_region_widget, @wikipedia_widget_id, @page_13_region_2, 0, FALSE);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_widget_seq;

set @next_region_widget = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_widget_seq);
INSERT INTO region_widget(entity_id, widget_id, region_id, render_order, collapsed)
values (@next_region_widget, @translate_widget_id, @page_13_region_1, 1, FALSE);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_widget_seq;
--- End openid user_id_13 layout ---

--- gadget data ---

-- useless knowledge widget
set @widget_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @widget_seq);
insert into widget (entity_id,title, url, type, widget_status)
values(@widget_id,'Useless Knowledge', 'http://www.great-goofy-gadgets.com/humor/uselessknowledge/uselessknowledge.xml', 'OpenSocial', 'PREVIEW');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @widget_seq;
-- end widget data ----

-- authorities
set @next_authority_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @granted_authority_seq);
insert into granted_authority (entity_id, authority, default_for_new_user)
values (@next_authority_id, 'user', true);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @granted_authority_seq;

set @next_authority_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @granted_authority_seq);
insert into granted_authority (entity_id, authority, default_for_new_user)
values (@next_authority_id, 'manager', false);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @granted_authority_seq;

set @next_authority_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @granted_authority_seq);
insert into granted_authority (entity_id, authority, default_for_new_user)
values (@next_authority_id, 'administrator', false);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @granted_authority_seq;

-- end authorities

-- portal preferences
set @next_portal_preference_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @portal_preference_seq);
INSERT INTO portal_preference (entity_id, preference_key)
values (@next_portal_preference_id, 'color');
INSERT INTO portalpreference_values
values (@next_portal_preference_id, 'red');
INSERT INTO portalpreference_values
values (@next_portal_preference_id, 'yellow');
INSERT INTO portalpreference_values
values (@next_portal_preference_id, 'blue');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @portal_preference_seq;

set @next_portal_preference_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @portal_preference_seq);
INSERT INTO portal_preference (entity_id, preference_key)
values (@next_portal_preference_id, 'title');
INSERT INTO portalpreference_values
values (@next_portal_preference_id, 'Rave');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @portal_preference_seq;
-- end portal preferences