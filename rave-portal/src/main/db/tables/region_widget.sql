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
create table region_widget
(
    id                number(38)  not null,
    widget_id         number(38)  not null,
    region_id         number(38)  not null,
    render_position   number(10)  not null,
    collapsed         varchar2(1) not null,
    constraint region_widget_pk
        primary key (id),
    constraint chk_region_widget_collapsed
        check (collapsed in ('Y','N'))
);

alter table region_widget
add constraint region_widget_region_id_fk
foreign key (region_id)
references region (id);

alter table region_widget
add constraint region_widget_widget_id_fk
foreign key (widget_id)
references widget (id);