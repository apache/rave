INSERT INTO page (id, name, owner_id, render_sequence, page_layout_id) values (set(@page_1_id, next value for page_id_seq), 'main', @user_id_1, 1, @two_col_id);

INSERT INTO region(id, page_id) values (set(@page_1_region_1, next value for region_id_seq), @page_1_id);
INSERT INTO region(id, page_id) values (set(@page_1_region_2, next value for region_id_seq), @page_1_id);

INSERT INTO region_widget(id, widget_id, region_id, render_position, collapsed) values (next value for region_widget_id_seq, @wikipedia_widget_id, @page_1_region_1, 1, 'N');
INSERT INTO region_widget(id, widget_id, region_id, render_position, collapsed) values (next value for region_widget_id_seq, @translate_widget_id, @page_1_region_1, 2, 'N');

INSERT INTO region_widget(id, widget_id, region_id, render_position, collapsed) values (next value for region_widget_id_seq, @nyt_widget_id, @page_1_region_2, 1, 'N');
INSERT INTO region_widget(id, widget_id, region_id, render_position, collapsed) values (next value for region_widget_id_seq, @tabnews_widget_id, @page_1_region_2, 2, 'N');