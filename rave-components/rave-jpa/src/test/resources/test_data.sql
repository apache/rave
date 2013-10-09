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
set @tag_seq = 'tag';
set @widget_tag_seq = 'widget_tag';
set @category_seq = 'category';
set @page_template_seq = 'page_template';
set @page_template_region_seq = 'page_template_region';
set @page_template_widget_seq = 'page_template_widget';
set @page_user_seq = 'page_user';
set @token_info_seq = 'token_info';
set @oauth_consumer_store_seq = 'oauth_consumer_store';
set @application_data_seq = 'application_data';
set @person_seq = 'person';
set @person_association_seq = 'person_association';
set @groups_seq = 'groups';
set @group_members_seq = 'group_members';
set @person_properties_seq = 'person_properties';

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
INSERT INTO RAVE_PORTAL_SEQUENCES(seq_name, seq_count) values (@tag_seq, 1);
INSERT INTO RAVE_PORTAL_SEQUENCES(seq_name, seq_count) values (@widget_tag_seq, 1);
INSERT INTO RAVE_PORTAL_SEQUENCES(seq_name, seq_count) values (@category_seq, 1);
INSERT INTO RAVE_PORTAL_SEQUENCES(seq_name, seq_count) values (@page_template_seq, 1);
INSERT INTO RAVE_PORTAL_SEQUENCES(seq_name, seq_count) values (@page_template_region_seq, 1);
INSERT INTO RAVE_PORTAL_SEQUENCES(seq_name, seq_count) values (@page_template_widget_seq, 1);
INSERT INTO RAVE_PORTAL_SEQUENCES(seq_name, seq_count) values (@page_user_seq, 1);
INSERT INTO RAVE_PORTAL_SEQUENCES(seq_name, seq_count) values (@person_association_seq, 1);
INSERT INTO RAVE_PORTAL_SEQUENCES(seq_name, seq_count) values (@groups_seq, 1);
INSERT INTO RAVE_PORTAL_SEQUENCES(seq_name, seq_count) values (@group_members_seq, 1);
INSERT INTO RAVE_PORTAL_SEQUENCES(seq_name, seq_count) values (@person_properties_seq, 1);

CREATE TABLE IF NOT EXISTS RAVE_SHINDIG_SEQUENCES (seq_name VARCHAR(255) PRIMARY KEY NOT NULL, seq_count BIGINT(19));
INSERT INTO RAVE_SHINDIG_SEQUENCES(seq_name, seq_count) values (@token_info_seq, 1);
INSERT INTO RAVE_SHINDIG_SEQUENCES(seq_name, seq_count) values (@oauth_consumer_store_seq, 1);
INSERT INTO RAVE_SHINDIG_SEQUENCES(seq_name, seq_count) values (@application_data_seq, 1);


  -- ***********************************************************************************
  -- start page layout data, required to make the portal work ---
set @one_col_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @page_layout_seq);
insert into page_layout (entity_id, code,  number_of_regions, render_sequence, user_selectable)
values (@one_col_id, 'columns_1', 1, 0, true);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_layout_seq;

set @two_col_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @page_layout_seq);
insert into page_layout (entity_id, code,  number_of_regions, render_sequence, user_selectable)
values (@two_col_id, 'columns_2', 2, 1, true);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_layout_seq;

set @twown_col_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @page_layout_seq);
insert into page_layout (entity_id, code,  number_of_regions, render_sequence, user_selectable)
values (@twown_col_id, 'columns_2wn', 2, 2, true);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_layout_seq;

set @three_col_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @page_layout_seq);
insert into page_layout (entity_id, code,  number_of_regions, render_sequence, user_selectable)
values (@three_col_id, 'columns_3', 3, 3, true);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_layout_seq;

set @threewn_col_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @page_layout_seq);
insert into page_layout (entity_id, code,  number_of_regions, render_sequence, user_selectable)
values (@threewn_col_id, 'columns_3nwn', 3, 4, true);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_layout_seq;

set @four_col_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @page_layout_seq);
insert into page_layout (entity_id, code,  number_of_regions, render_sequence, user_selectable)
values (@four_col_id, 'columns_4', 4, 5, true);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_layout_seq;

set @fourwn_col_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @page_layout_seq);
insert into page_layout (entity_id, code,  number_of_regions, render_sequence, user_selectable)
values (@fourwn_col_id, 'columns_3nwn_1_bottom', 4, 6, true);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_layout_seq;

set @person_profile_layout_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @page_layout_seq);
insert into page_layout (entity_id, code,  number_of_regions, render_sequence, user_selectable)
values (@person_profile_layout_id, 'person_profile', 1, 8, false);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_layout_seq;
--- end page layout data ----


  -- ***********************************************************************************
  --- start user data ---
set @user_id_1 = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @user_seq);
insert into person (entity_id, username, password, expired, locked, enabled, email, default_page_layout_id, dtype, family_name, given_name, status)
values (@user_id_1, 'canonical', '$2a$10$TkEgze5kLy9nRlfd8PT1zunh6P1ND8WPjLojFjAMNgZMu1D9D1n4.', FALSE, FALSE, TRUE,'canonical@example.com', @three_col_id, 'User', 'Canonical', 'Paul', 'Single');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @user_seq;

set @user_id_2 = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @user_seq);
insert into person (entity_id, username, password, expired, locked, enabled, email, default_page_layout_id, dtype, family_name, given_name, status)
values (@user_id_2, 'john.doe', '$2a$10$8Dir7boy3UyVqy6erfj6WuQXUTf.ejTldPSsVIty7.pPT3Krkly26', FALSE, FALSE, TRUE,'john.doe@example.com', @three_col_id, 'User', 'Doe', 'John', 'Single');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @user_seq;

set @user_id_3 = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @user_seq);
insert into person (entity_id, username, password, expired, locked, enabled, email, default_page_layout_id, dtype, family_name, given_name, status)
values (@user_id_3, 'jane.doe', '$2a$10$YP9cjZEA.gG/ng2YwTBIyucMpuiQ7Fvz0K8rOt14rIBhVwlOrh1tu', FALSE, FALSE, TRUE,'jane.doe@example.net', @three_col_id, 'User', 'Doe', 'Jane', 'Single');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @user_seq;

set @user_id_4 = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @user_seq);
insert into person (entity_id, username, password, expired, locked, enabled, email, default_page_layout_id, dtype, family_name, given_name, status, forgotPasswordHash)
values (@user_id_4, 'george.doe', '$2a$10$0bcOUkQgAwE/qmdc1NcUveNzx/IYIcOUu4ydyT8DEicTCxGJF/vcW', FALSE, FALSE, TRUE,'george.doe@example.org', @three_col_id, 'User', 'Doe', 'George', 'Single', 'ABC123');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @user_seq;

set @user_id_5 = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @user_seq);
insert into person (entity_id, username, password, expired, locked, enabled, email, default_page_layout_id, dtype, family_name, given_name, status)
values (@user_id_5,'mario.rossi', '$2a$10$HZ6WHAKQCs8waLooL98l6.fLzwh3D8u/V0.UebIjojawfXJhX1DQ2', FALSE, FALSE, TRUE,'mario.rossi@example.com', @three_col_id, 'User', 'Rossi', 'Mario', 'Single');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @user_seq;

set @user_id_6 = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @user_seq);
insert into person (entity_id, username, password, expired, locked, enabled, email, default_page_layout_id, dtype, family_name, given_name, status)
values (@user_id_6, 'maija.m', '$2a$10$3feYdjrW40hkqP4/xupKP.YMgdYmDsZZus./vK4FbBs9QZG2.FuNC', FALSE, FALSE, TRUE,'maijam@example.com', @three_col_id, 'User', 'M', 'Maija', 'Single');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @user_seq;

set @user_id_7 = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @user_seq);
insert into person (entity_id, username, password, expired, locked, enabled, email, default_page_layout_id, dtype, family_name, given_name, status)
values (@user_id_7, 'one.col', '$2a$10$5VqE2YEqT75pCVjKqjP2b.gNGly9fsTVUOMQR/JEjkHSbqvA3A6IO', FALSE, FALSE, TRUE,'one.col@example.com', @three_col_id, 'User', 'Column', 'One', 'Single');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @user_seq;

set @user_id_8 = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @user_seq);
insert into person (entity_id, username, password, expired, locked, enabled, email, default_page_layout_id, dtype, family_name, given_name, status)
values (@user_id_8, 'twown.col', '$2a$10$Inpufv82TRUGYoPuXhYXVuMCKHkhLz44W6FijxW2e9n3T1hgyxcVq', FALSE, FALSE, TRUE,'twown.col@example.com', @three_col_id, 'User', 'Column', 'Two', 'Single');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @user_seq;

set @user_id_9 = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @user_seq);
insert into person (entity_id, username, password, expired, locked, enabled, email, default_page_layout_id, dtype, family_name, given_name, status)
values (@user_id_9, 'three.col', '$2a$10$ImRXq4gFC9teBstOBdQrZeEwBkCAJ0S6.CwI9/9r7fxWKTZ30pgVC', FALSE, FALSE, TRUE,'three.col@example.com', @three_col_id, 'User', 'Column', 'Three', 'Single');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @user_seq;

set @user_id_10 = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @user_seq);
insert into person (entity_id, username, password, expired, locked, enabled, email, default_page_layout_id, dtype, family_name, given_name, status)
values (@user_id_10, 'threewn.col', '$2a$10$LLYTJoK6MCBpeDBbmdt7tu1LNt7Eenqe1IpMlfem8xVjzynn.HpxW', FALSE, FALSE, TRUE,'threewn.col@example.com', @three_col_id, 'User', 'Column2', 'Three', 'Single');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @user_seq;

set @user_id_11 = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @user_seq);
insert into person (entity_id, username, password, expired, locked, enabled, email, default_page_layout_id, dtype, family_name, given_name, status)
values (@user_id_11, 'four.col', '$2a$10$tZgWcaG2EJPLtseZ339n7uTu3GZn31h3iTr20orwgbbRAI15uoIFK', FALSE, FALSE, TRUE,'four.col@example.com', @three_col_id, 'User', 'Column', 'Four', 'Single');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @user_seq;

set @user_id_12 = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @user_seq);
insert into person (entity_id, username, password, expired, locked, enabled, email, default_page_layout_id, dtype, family_name, given_name, status)
values (@user_id_12, 'fourwn.col', '$2a$10$4kPYhgowurWqXGVDigxOxOVj/M.rqLRwqbn0kT/OD4pISL6pDG/c2', FALSE, FALSE, TRUE,'fourwn.col@example.com', @three_col_id, 'User', 'Column2', 'Four', 'Single');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @user_seq;

-- duplicate user id!!
set @user_id_13 = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @user_seq);
insert into person (entity_id, username, password, expired, locked, enabled, email, default_page_layout_id, dtype, family_name, given_name, status, openid)
values (@user_id_13, 'rave2011.myopenid.com', '$2a$10$dML97.rnOn4.iSlEEdju8OCB2NckuKw0Ki5yMVzzMmWQsWMvym3qC', FALSE, FALSE, TRUE,'rave2011_openid@example.org', @three_col_id, 'User', 'Openid', 'Rave', 'Single', 'http://rave2011.myopenid.com/');
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
insert into widget (entity_id, title, url, type, widget_status, owner_id,featured)
values(@nyt_widget_id, 'NYTimes.com - Top Stories', 'http://widgets.nytimes.com/packages/html/igoogle/topstories.xml', 'OpenSocial', 'PUBLISHED', @user_id_1,true );
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

-- my activity
set @my_activity_widget_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @widget_seq);
insert into widget (entity_id, title, url, type, widget_status, description, author, owner_id)
values(@my_activity_widget_id, 'My Activity', 'http://localhost:8080/demogadgets/my_activity.xml', 'OpenSocial', 'PUBLISHED', 'Static widget of activities for demoing on the Person Profile page', 'Anthony Carlucci', @user_id_1);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @widget_seq;

-- schedule
set @schedule_widget_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @widget_seq);
insert into widget (entity_id, title, url, type, widget_status, description, author, owner_id)
values(@schedule_widget_id, 'Current Schedule', 'http://localhost:8080/demogadgets/schedule.xml', 'OpenSocial', 'PUBLISHED', 'Static widget of a schedule for demoing on the Person Profile page', 'Anthony Carlucci', @user_id_1);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @widget_seq;

-- favorite websites
set @favorite_websites_widget_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @widget_seq);
insert into widget (entity_id, title, url, type, widget_status, description, author, owner_id)
values(@favorite_websites_widget_id, 'Favorite Websites', 'http://localhost:8080/demogadgets/favorite_websites.xml', 'OpenSocial', 'PUBLISHED', 'Static widget of favorite websites for demoing on the Person Profile page', 'Anthony Carlucci', @user_id_1);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @widget_seq;

-- my groups
set @my_groups_widget_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @widget_seq);
insert into widget (entity_id, title, url, type, widget_status, description, author, owner_id)
values(@my_groups_widget_id, 'My Groups', 'http://localhost:8080/demogadgets/my_groups.xml', 'OpenSocial', 'PUBLISHED', 'Static widget of groups for demoing on the Person Profile page', 'Anthony Carlucci', @user_id_1);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @widget_seq;

-- work experience
set @work_experience_widget_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @widget_seq);
insert into widget (entity_id, title, url, type, widget_status, description, author, owner_id)
values(@work_experience_widget_id, 'Work Experience', 'http://localhost:8080/demogadgets/work_experience.xml', 'OpenSocial', 'PUBLISHED', 'Static widget of work experience for demoing on the Person Profile page', 'Anthony Carlucci', @user_id_1);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @widget_seq;


-- end widget data ----

-- User layouts

--- Layout for user_id_1 ---
set @page_1_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @page_seq);
INSERT INTO page (entity_id, name, owner_id, context_id, parent_page_id, page_layout_id, page_type)
values (@page_1_id, 'Main', @user_id_1, 'foo', null, @two_col_id, 'USER');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_seq;

--Set up page user data
set @page_user_id =(SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @page_user_seq);
insert into page_user (entity_id, page_id, user_id, editor, render_sequence, page_status)
values (@page_user_id, @page_1_id, @user_id_1, true, 1, 'OWNER');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_user_seq;
--end page user data

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

set @next_widget_rating = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @widget_rating_seq);
INSERT INTO widget_rating(entity_id, widget_id, user_id, score)
values (@next_widget_rating, 2, 2, 0);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @widget_rating_seq;

set @page_2_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @page_seq);
INSERT INTO page (entity_id, name, owner_id, parent_page_id, page_layout_id, page_type)
values (@page_2_id, 'Social', @user_id_1, null, @two_col_id, 'USER');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_seq;

--Set up page user data
set @page_user_id =(SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @page_user_seq);
insert into page_user (entity_id, page_id, user_id, editor, render_sequence, page_status)
values (@page_user_id, @page_2_id, @user_id_1, true, 2, 'OWNER');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_user_seq;
--end page user data

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

-- person profile page for user 1
set @person_profile_page_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @page_seq);
INSERT INTO page (entity_id, name, owner_id, parent_page_id, page_layout_id, page_type)
values (@person_profile_page_id , 'Person Profile', @user_id_1, null, @person_profile_layout_id, 'PERSON_PROFILE');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_seq;

--Set up page user data--
set @page_user_id =(SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @page_user_seq);
insert into page_user (entity_id, page_id, user_id, editor, render_sequence, page_status)
values (@page_user_id, @person_profile_page_id, @user_id_1, true, 1, 'OWNER');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_user_seq;
--end page user data--

set @person_profile_page_region_1 = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_seq);
INSERT INTO region(entity_id, page_id, render_order)
values (@person_profile_page_region_1, @person_profile_page_id, 1);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_seq;

set @next_region_widget = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_widget_seq);
INSERT INTO region_widget(entity_id, widget_id, region_id, render_order, collapsed)
values (@next_region_widget, @my_groups_widget_id, @person_profile_page_region_1, 0, FALSE);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_widget_seq;

set @next_region_widget = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_widget_seq);
INSERT INTO region_widget(entity_id, widget_id, region_id, render_order, collapsed)
values (@next_region_widget, @work_experience_widget_id, @person_profile_page_region_1, 1, FALSE);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_widget_seq;

-- sub pages for profile page for user 1
set @sub_page_1_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @page_seq);
INSERT INTO page (entity_id, name, owner_id, parent_page_id, page_layout_id, page_type)
values (@sub_page_1_id , 'About', @user_id_1, @person_profile_page_id, @one_col_id, 'SUB_PAGE');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_seq;

--Set up page user data--
set @page_user_id =(SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @page_user_seq);
insert into page_user (entity_id, page_id, user_id, editor, render_sequence, page_status)
values (@page_user_id, @sub_page_1_id, @user_id_1, true, 1, 'OWNER');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_user_seq;
--end page user data--
set @sub_page_1_region_1 = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_seq);
INSERT INTO region(entity_id, page_id, render_order)
values (@sub_page_1_region_1, @sub_page_1_id, 1);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_seq;

set @next_region_widget = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_widget_seq);
INSERT INTO region_widget(entity_id, widget_id, region_id, render_order, collapsed)
values (@next_region_widget, @favorite_websites_widget_id , @sub_page_1_region_1, 0, FALSE);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_widget_seq;

set @next_region_widget = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_widget_seq);
INSERT INTO region_widget(entity_id, widget_id, region_id, render_order, collapsed)
values (@next_region_widget, @schedule_widget_id, @sub_page_1_region_1, 1, FALSE);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_widget_seq;

set @sub_page_2_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @page_seq);
INSERT INTO page (entity_id, name, owner_id, parent_page_id, page_layout_id, page_type)
values (@sub_page_2_id , 'My Activity', @user_id_1, @person_profile_page_id, @one_col_id, 'SUB_PAGE');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_seq;

--Set up page user data--
set @page_user_id =(SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @page_user_seq);
insert into page_user (entity_id, page_id, user_id, editor, render_sequence, page_status)
values (@page_user_id, @sub_page_2_id, @user_id_1, true, 2, 'OWNER');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_user_seq;
--end page user data--
set @sub_page_2_region_1 = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_seq);
INSERT INTO region(entity_id, page_id, render_order)
values (@sub_page_2_region_1, @sub_page_2_id, 1);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_seq;

set @next_region_widget = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @region_widget_seq);
INSERT INTO region_widget(entity_id, widget_id, region_id, render_order, collapsed)
values (@next_region_widget, @my_activity_widget_id, @sub_page_2_region_1, 0, FALSE);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @region_widget_seq;
--- End canonical user_id_1 layout ---

--- Layout for user_id_2 ---
set @page_1_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @page_seq);
INSERT INTO page (entity_id, name, owner_id, parent_page_id, page_layout_id, page_type)
values (@page_1_id, 'Main', @user_id_2, null, @two_col_id, 'USER');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_seq;

--Set up page user data--
set @page_user_id =(SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @page_user_seq);
insert into page_user (entity_id, page_id, user_id, editor, render_sequence, page_status)
values (@page_user_id, @page_1_id, @user_id_2, true, 1, 'OWNER');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_user_seq;
--end page user data--

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
INSERT INTO page (entity_id, name, owner_id, parent_page_id, page_layout_id, page_type)
values (@page_1_id, 'Main', @user_id_3, null, @two_col_id, 'USER');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_seq;

--Set up page user data--
set @page_user_id =(SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @page_user_seq);
insert into page_user (entity_id, page_id, user_id, editor, render_sequence, page_status)
values (@page_user_id, @page_1_id, @user_id_3, true, 1, 'OWNER');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_user_seq;
--end page user data--

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
INSERT INTO page (entity_id, name, owner_id, parent_page_id, page_layout_id, page_type)
values (@page_1_id, 'Main', @user_id_4, null, @two_col_id, 'USER');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_seq;

--Set up page user data--
set @page_user_id =(SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @page_user_seq);
insert into page_user (entity_id, page_id, user_id, editor, render_sequence, page_status)
values (@page_user_id, @page_1_id, @user_id_4, true, 1, 'OWNER');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_user_seq;
--end page user data--

--- End user_id_4 layout ---

--- Layout for user_id_5 ---
set @page_1_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @page_seq);
INSERT INTO page (entity_id, name, owner_id, parent_page_id, page_layout_id, page_type)
values (@page_1_id, 'Main', @user_id_5, null, @two_col_id, 'USER');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_seq;

--Set up page user data--
set @page_user_id =(SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @page_user_seq);
insert into page_user (entity_id, page_id, user_id, editor, render_sequence, page_status)
values (@page_user_id, @page_1_id, @user_id_5, true, 1, 'OWNER');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_user_seq;
--end page user data--

--- End user_id_5 layout ---



--- Layout for user_id_6 ---
set @page_1_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @page_seq);
INSERT INTO page (entity_id, name, owner_id, parent_page_id, page_layout_id, page_type)
values (@page_1_id, 'Main', @user_id_6, null, @two_col_id, 'USER');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_seq;

--Set up page user data--
set @page_user_id =(SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @page_user_seq);
insert into page_user (entity_id, page_id, user_id, editor, render_sequence, page_status)
values (@page_user_id, @page_1_id, @user_id_6, true, 1, 'OWNER');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_user_seq;
--end page user data--

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
INSERT INTO page (entity_id, name, owner_id, parent_page_id, page_layout_id, page_type)
values (@page_7_id, 'Main', @user_id_7, null, @one_col_id, 'USER');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_seq;

--Set up page user data--
set @page_user_id =(SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @page_user_seq);
insert into page_user (entity_id, page_id, user_id, editor, render_sequence, page_status)
values (@page_user_id, @page_7_id, @user_id_7, true, 1, 'OWNER');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_user_seq;
--end page user data--

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
INSERT INTO page (entity_id, name, owner_id, parent_page_id, page_layout_id, page_type)
values (@page_8_id, 'Main', @user_id_8, null, @twown_col_id, 'USER');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_seq;

--Set up page user data--
set @page_user_id =(SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @page_user_seq);
insert into page_user (entity_id, page_id, user_id, editor, render_sequence, page_status)
values (@page_user_id, @page_8_id, @user_id_8, true, 1, 'OWNER');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_user_seq;
--end page user data--

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
INSERT INTO page (entity_id, name, owner_id, parent_page_id, page_layout_id, page_type)
values (@page_9_id, 'Main', @user_id_9, null, @three_col_id, 'USER');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_seq;

--Set up page user data--
set @page_user_id =(SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @page_user_seq);
insert into page_user (entity_id, page_id, user_id, editor, render_sequence, page_status)
values (@page_user_id, @page_9_id, @user_id_9, true, 1, 'OWNER');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_user_seq;
--end page user data--

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
INSERT INTO page (entity_id, name, owner_id, parent_page_id, page_layout_id, page_type)
values (@page_10_id, 'Main', @user_id_10, null, @threewn_col_id, 'USER');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_seq;

--Set up page user data--
set @page_user_id =(SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @page_user_seq);
insert into page_user (entity_id, page_id, user_id, editor, render_sequence, page_status)
values (@page_user_id, @page_10_id, @user_id_10, true, 1, 'OWNER');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_user_seq;
--end page user data--

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
INSERT INTO page (entity_id, name, owner_id, parent_page_id, page_layout_id, page_type)
values (@page_11_id, 'Main', @user_id_11, null, @four_col_id, 'USER');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_seq;

--Set up page user data--
set @page_user_id =(SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @page_user_seq);
insert into page_user (entity_id, page_id, user_id, editor, render_sequence, page_status)
values (@page_user_id, @page_11_id, @user_id_11, true, 1, 'OWNER');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_user_seq;
--end page user data--

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
INSERT INTO page (entity_id, name, owner_id, parent_page_id, page_layout_id, page_type)
values (@page_12_id, 'Main', @user_id_12, null, @fourwn_col_id, 'USER');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_seq;

--Set up page user data--
set @page_user_id =(SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @page_user_seq);
insert into page_user (entity_id, page_id, user_id, editor, render_sequence, page_status)
values (@page_user_id, @page_12_id, @user_id_12, true, 1, 'OWNER');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_user_seq;
--end page user data--

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
INSERT INTO page (entity_id, name, owner_id, parent_page_id, page_layout_id, page_type)
values (@page_13_id, 'Main', @user_id_13, null, @fourwn_col_id, 'USER');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_seq;

--Set up page user data--
set @page_user_id =(SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @page_user_seq);
insert into page_user (entity_id, page_id, user_id, editor, render_sequence, page_status)
values (@page_user_id, @page_13_id, @user_id_13, true, 1, 'OWNER');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_user_seq;
--end page user data--

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
INSERT INTO portal_preference_values
values (@next_portal_preference_id, 'red');
INSERT INTO portal_preference_values
values (@next_portal_preference_id, 'yellow');
INSERT INTO portal_preference_values
values (@next_portal_preference_id, 'blue');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @portal_preference_seq;

set @next_portal_preference_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @portal_preference_seq);
INSERT INTO portal_preference (entity_id, preference_key)
values (@next_portal_preference_id, 'title');
INSERT INTO portal_preference_values
values (@next_portal_preference_id, 'Rave');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @portal_preference_seq;
-- end portal preferences

set @tag_1_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @tag_seq);
insert into tag (entity_id, keyword)
values (@tag_1_id, 'news');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @tag_seq;

set @tag_2_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @tag_seq);
insert into tag (entity_id, keyword)
values (@tag_2_id, 'wikipedia');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @tag_seq;

set @tag_3_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @tag_seq);
insert into tag (entity_id, keyword)
values (@tag_3_id, 'misc');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @tag_seq;


set @next_widget_tag_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @widget_tag_seq);
insert into widget_tag (entity_id, widget_id, tag_id)
values (@next_widget_tag_id, 3,1);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @widget_tag_seq;

set @next_widget_tag_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @widget_tag_seq);
insert into widget_tag (entity_id, widget_id, tag_id)
values (@next_widget_tag_id, 1,2);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @widget_tag_seq;

-- category
set @category_id1 = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @category_seq);
insert into category (entity_id, text, created_user_id, created_date, last_modified_user_id, last_modified_date)
values (@category_id1, 'Sample Category', @user_id_1, '2012-01-19', @user_id_2, '2012-01-22');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @category_seq;

set @category_id2 = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @category_seq);
insert into category (entity_id, text, created_user_id, created_date, last_modified_user_id, last_modified_date)
values (@category_id2, 'AAA Category', @user_id_1, '2012-01-19', @user_id_2, '2012-01-19');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @category_seq;

set @category_id3 = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @category_seq);
insert into category (entity_id, text, created_user_id, created_date, last_modified_user_id, last_modified_date)
values (@category_id3, 'News Category', @user_id_1, '2012-01-19', @user_id_2, '2012-01-19');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @category_seq;

set @category_id4 = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @category_seq);
insert into category (entity_id, text, created_user_id, created_date, last_modified_user_id, last_modified_date)
values (@category_id4, 'Technology Category', @user_id_1, '2012-01-19', @user_id_2, '2012-01-19');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @category_seq;

set @category_id5 = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @category_seq);
insert into category (entity_id, text, created_user_id, created_date, last_modified_user_id, last_modified_date)
values (@category_id5, 'no people', null, '2012-01-19', null, '2012-01-19');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @category_seq;

set @category_id6 = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @category_seq);
insert into category (entity_id, text, created_user_id, created_date, last_modified_user_id, last_modified_date)
values (@category_id6, 'Cat99', @user_id_5, '2012-01-19', @user_id_1, '2012-01-19');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @category_seq;

-- widget category
insert into widget_category (widget_id, category_id)
values (@wikipedia_widget_id, @category_id4);
insert into widget_category (widget_id, category_id)
values (@wikipedia_widget_id, @category_id2);

----------------------------------------
-- person profile parent page templates
----------------------------------------
-- page
set @person_profile_page_template_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @page_template_seq);
insert into page_template (entity_id, page_type, page_layout_id, name, description, parent_page_template_id, render_sequence, default_template)
values (@person_profile_page_template_id, 'PERSON_PROFILE', @person_profile_layout_id, 'Person Profile', 'Template for person profile pages', null, 0, true);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_template_seq;

-- regions
set @person_profile_page_template_region_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @page_template_region_seq);
insert into page_template_region (entity_id, render_sequence, page_template_id, locked)
values (@person_profile_page_template_region_id, 0, @person_profile_page_template_id, true);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_template_region_seq;

-- widgets
set @next_person_profile_page_template_widget_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @page_template_widget_seq);
insert into page_template_widget (entity_id, page_template_region_id, render_sequence, widget_id, locked)
values (@next_person_profile_page_template_widget_id, @person_profile_page_template_region_id, 0, @my_groups_widget_id, true);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_template_widget_seq;

set @next_person_profile_page_template_widget_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @page_template_widget_seq);
insert into page_template_widget (entity_id, page_template_region_id, render_sequence, widget_id, locked)
values (@next_person_profile_page_template_widget_id, @person_profile_page_template_region_id, 1, @work_experience_widget_id, true);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_template_widget_seq;

------------------------------------------
-- person profile about sub page templates
------------------------------------------
-- page
set @person_profile_subpage1_template_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @page_template_seq);
insert into page_template (entity_id, page_type, page_layout_id, name, description, parent_page_template_id, render_sequence, default_template)
values (@person_profile_subpage1_template_id, 'SUB_PAGE', @one_col_id, 'About', 'Template for the About sub page for the person profile', @person_profile_page_template_id, 0, false);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_template_seq;

-- regions
set @person_profile_subpage1_template_region_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @page_template_region_seq);
insert into page_template_region (entity_id, render_sequence, page_template_id, locked)
values (@person_profile_subpage1_template_region_id, 0, @person_profile_subpage1_template_id, true);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_template_region_seq;

-- widgets
set @next_person_profile_subpage1_template_widget_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @page_template_widget_seq);
insert into page_template_widget (entity_id, page_template_region_id, render_sequence, widget_id, locked)
values (@next_person_profile_subpage1_template_widget_id, @person_profile_subpage1_template_region_id, 0, @favorite_websites_widget_id, true);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_template_widget_seq;

set @next_person_profile_subpage1_template_widget_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @page_template_widget_seq);
insert into page_template_widget (entity_id, page_template_region_id, render_sequence, widget_id, locked)
values (@next_person_profile_subpage1_template_widget_id, @person_profile_subpage1_template_region_id, 1, @schedule_widget_id, true);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_template_widget_seq;

--------------------------------------------------
-- person profile my activities sub page templates
--------------------------------------------------
-- page
set @person_profile_subpage2_template_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @page_template_seq);
insert into page_template (entity_id, page_type, page_layout_id, name, description, parent_page_template_id, render_sequence, default_template)
values (@person_profile_subpage2_template_id, 'SUB_PAGE', @one_col_id, 'My Activity', 'Template for the My Activity sub page for the person profile', @person_profile_page_template_id, 1, false);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_template_seq;

-- regions
set @person_profile_subpage2_template_region_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @page_template_region_seq);
insert into page_template_region (entity_id, render_sequence, page_template_id, locked)
values (@person_profile_subpage2_template_region_id, 0, @person_profile_subpage2_template_id, true);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_template_region_seq;

-- widgets
set @next_person_profile_subpage2_template_widget_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @page_template_widget_seq);
insert into page_template_widget (entity_id, page_template_region_id, render_sequence, widget_id, locked)
values (@next_person_profile_subpage2_template_widget_id, @person_profile_subpage2_template_region_id, 0, @my_activity_widget_id, true);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_template_widget_seq;

----------------------------------------
-- secondary person profile parent page templates
----------------------------------------
-- page
set @person_profile_page_template_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @page_template_seq);
insert into page_template (entity_id, page_type, page_layout_id, name, description, parent_page_template_id, render_sequence, default_template)
values (@person_profile_page_template_id, 'PERSON_PROFILE', @person_profile_layout_id, 'Person Profile 2', 'Template for person profile pages', null, 0, false);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_template_seq;

-- regions
set @person_profile_page_template_region_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @page_template_region_seq);
insert into page_template_region (entity_id, render_sequence, page_template_id, locked)
values (@person_profile_page_template_region_id, 0, @person_profile_page_template_id, true);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_template_region_seq;

-- widgets
set @next_person_profile_page_template_widget_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @page_template_widget_seq);
insert into page_template_widget (entity_id, page_template_region_id, render_sequence, widget_id, locked)
values (@next_person_profile_page_template_widget_id, @person_profile_page_template_region_id, 0, @my_groups_widget_id, true);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_template_widget_seq;

set @next_person_profile_page_template_widget_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @page_template_widget_seq);
insert into page_template_widget (entity_id, page_template_region_id, render_sequence, widget_id, locked)
values (@next_person_profile_page_template_widget_id, @person_profile_page_template_region_id, 1, @work_experience_widget_id, true);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_template_widget_seq;


----------------------------------------
-- user page template
----------------------------------------
-- page
set @user_profile_page_template_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @page_template_seq);
insert into page_template (entity_id, page_type, page_layout_id, name, description, parent_page_template_id, render_sequence, default_template)
values (@user_profile_page_template_id, 'USER', @person_profile_layout_id, 'User Profile', 'User profile pages', null, 0, true);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_template_seq;

-- region1
set @user_profile_page_template_region_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @page_template_region_seq);
insert into page_template_region (entity_id, render_sequence, page_template_id, locked)
values (@user_profile_page_template_region_id, 0, @user_profile_page_template_id, true);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_template_region_seq;

-- widgets
set @next_user_profile_page_template_widget_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @page_template_widget_seq);
insert into page_template_widget (entity_id, page_template_region_id, render_sequence, widget_id, locked)
values (@next_user_profile_page_template_widget_id, @user_profile_page_template_region_id, 0, @my_groups_widget_id, true);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_template_widget_seq;

set @next_user_profile_page_template_widget_id = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @page_template_widget_seq);
insert into page_template_widget (entity_id, page_template_region_id, render_sequence, widget_id, locked)
values (@next_user_profile_page_template_widget_id, @user_profile_page_template_region_id, 1, @work_experience_widget_id, true);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @page_template_widget_seq; set @token_info_seq = 'token_info';


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

set @next_person_association = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @person_association_seq);
INSERT INTO person_association(entity_id, follower_id, followedby_id, status)
VALUES (@next_person_association, @user_id_1, @user_id_2, 'ACCEPTED');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @person_association_seq;

set @next_person_association = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @person_association_seq);
INSERT INTO person_association(entity_id, follower_id, followedby_id, status)
VALUES (@next_person_association, @user_id_1, @user_id_3, 'ACCEPTED');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @person_association_seq;

set @next_person_association = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @person_association_seq);
INSERT INTO person_association(entity_id, follower_id, followedby_id, status)
VALUES (@next_person_association, @user_id_1, @user_id_4, 'ACCEPTED');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @person_association_seq;

set @next_person_association = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @person_association_seq);
INSERT INTO person_association(entity_id, follower_id, followedby_id, status)
VALUES (@next_person_association, @user_id_2, @user_id_1, 'ACCEPTED');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @person_association_seq;

set @next_person_association = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @person_association_seq);
INSERT INTO person_association(entity_id, follower_id, followedby_id, status)
VALUES (@next_person_association, @user_id_2, @user_id_3, 'ACCEPTED');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @person_association_seq;

set @next_person_association = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @person_association_seq);
INSERT INTO person_association(entity_id, follower_id, followedby_id, status)
VALUES (@next_person_association, @user_id_2, @user_id_4, 'ACCEPTED');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @person_association_seq;

set @next_person_association = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @person_association_seq);
INSERT INTO person_association(entity_id, follower_id, followedby_id, status)
VALUES (@next_person_association, @user_id_2, @user_id_5, 'ACCEPTED');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @person_association_seq;

set @next_person_association = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @person_association_seq);
INSERT INTO person_association(entity_id, follower_id, followedby_id, status)
VALUES (@next_person_association, @user_id_3, @user_id_1, 'ACCEPTED');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @person_association_seq;

set @next_person_association = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @person_association_seq);
INSERT INTO person_association(entity_id, follower_id, followedby_id, status)
VALUES (@next_person_association, @user_id_3, @user_id_2, 'ACCEPTED');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @person_association_seq;

set @next_person_association = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @person_association_seq);
INSERT INTO person_association(entity_id, follower_id, followedby_id, status)
VALUES (@next_person_association, @user_id_4, @user_id_1, 'ACCEPTED');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @person_association_seq;

set @next_person_association = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @person_association_seq);
INSERT INTO person_association(entity_id, follower_id, followedby_id, status)
VALUES (@next_person_association, @user_id_4, @user_id_2, 'ACCEPTED');
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @person_association_seq;

set @next_person_association = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @person_association_seq);
INSERT INTO person_association(entity_id, follower_id, followedby_id, status)
VALUES (@next_person_association, @user_id_5, @user_id_2, 'ACCEPTED');
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
VALUES (@group_id_1, @user_id_1);

INSERT INTO group_members(group_id, person_id)
VALUES (@group_id_1, @user_id_5);

set @next_person_property = (SELECT seq_count FROM RAVE_PORTAL_SEQUENCES WHERE seq_name = @person_properties_seq);
INSERT INTO person_property(entity_id, type, value, person_id)
VALUES (@next_person_property, 'foo', 'bar', @user_id_1);
UPDATE RAVE_PORTAL_SEQUENCES SET seq_count = (seq_count + 1) WHERE seq_name = @person_properties_seq;
