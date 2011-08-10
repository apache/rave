INSERT INTO person(id, username, display_name, gender, status, profile_url, build, eye_color, hair_color,family_name, given_name )
VALUES (set(@person_id_1, next value for person_id_seq), 'canonical', 'Canonical User', 'male', 'I am alive', 'http://rave.rocks.org/profile', 'skinny', 'orange', 'blue', 'User', 'Canonical');

INSERT INTO person(id, username, display_name)
VALUES (set(@person_id_2, next value for person_id_seq), 'john.doe', 'John Doe');


INSERT INTO person(id, username, display_name)
VALUES (set(@person_id_3, next value for person_id_seq), 'jane.doe', 'Jane Doe');


INSERT INTO person(id, username, display_name)
VALUES (set(@person_id_4, next value for person_id_seq), 'george.doe', 'George Doe');

INSERT INTO person(id, username, display_name)
VALUES (set(@person_id_5, next value for person_id_seq), 'mario.rossi', 'Mario Rossi');

INSERT INTO person_association(id, follower_id, followed_id)
VALUES (next value for person_association_id_seq, @person_id_1, @person_id_2);

INSERT INTO person_association(id, follower_id, followed_id)
VALUES (next value for person_association_id_seq, @person_id_1, @person_id_3);

INSERT INTO person_association(id, follower_id, followed_id)
VALUES (next value for person_association_id_seq, @person_id_2, @person_id_4);
