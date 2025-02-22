INSERT INTO beers.styles (style_id, name)
VALUES (1, 'Pilsner');
INSERT INTO beers.styles (style_id, name)
VALUES (2, 'Pale ale');
INSERT INTO beers.styles (style_id, name)
VALUES (3, 'Red Ale');
INSERT INTO beers.styles (style_id, name)
VALUES (4, 'Porter');
INSERT INTO beers.styles (style_id, name)
VALUES (5, 'Stout');
INSERT INTO beers.styles (style_id, name)
VALUES (6, 'Indian pale ale');
INSERT INTO beers.styles (style_id, name)
VALUES (7, 'Weissbier');
INSERT INTO beers.styles (style_id, name)
VALUES (8, 'Special ale');

INSERT INTO beers.users (user_id, username, password, first_name, last_name, email, is_admin)
VALUES (1, 'todor', 'pass1', 'Todor', 'Andonov', 'todor@telerikacademy.com', true);
INSERT INTO beers.users (user_id, username, password, first_name, last_name, email, is_admin)
VALUES (2, 'vladi', 'pass2', 'Vladi', 'Venkov', 'vladi@telerikacademy.com', false);
INSERT INTO beers.users (user_id, username, password, first_name, last_name, email, is_admin)
VALUES (3, 'pesho', 'pass3', 'Petar', 'Raykov', 'pesho@telerikacademy.com', false);

INSERT INTO beers.beers (beer_id, name, abv, style_id, created_by, photo_url)
VALUES (1, 'Glarus English Ale', 4.6, 8, 1, '/assets/img/beers/glarus-english-ale.jpg');
INSERT INTO beers.beers (beer_id, name, abv, style_id, created_by, photo_url)
VALUES (2, 'Rhombus Porter', 5.0, 4, 1, '/assets/img/beers/rhombus-porter.jpg');
INSERT INTO beers.beers (beer_id, name, abv, style_id, created_by, photo_url)
VALUES (3, 'Opasen char', 6.6, 6, 1, '/assets/img/beers/opasen-char.jpg');
INSERT INTO beers.beers (beer_id, name, abv, style_id, created_by, photo_url)
VALUES (4, 'Ailyak', 6.6, 6, 2, '/assets/img/beers/ailyak.jpg');
INSERT INTO beers.beers (beer_id, name, abv, style_id, created_by, photo_url)
VALUES (5, 'Basi Kefa', 6.7, 6, 2, '/assets/img/beers/basi-kefa.jpg');
INSERT INTO beers.beers (beer_id, name, abv, style_id, created_by, photo_url)
VALUES (6, 'Vitoshko lale', 5.5, 2, 2, '/assets/img/beers/vitoshko-lale.jpg');
INSERT INTO beers.beers (beer_id, name, abv, style_id, created_by, photo_url)
VALUES (7, 'Divo Pivo', 4.5, 2, 2, '/assets/img/beers/divo-pivo.jpg');
INSERT INTO beers.beers (beer_id, name, abv, style_id, created_by, photo_url)
VALUES (8, 'Bloody Muddy', 5.0, 3, 2, '/assets/img/beers/bloody-muddy.jpg');
INSERT INTO beers.beers (beer_id, name, abv, style_id, created_by, photo_url)
VALUES (9, 'Black Head', 5.0, 5, 3, '/assets/img/beers/black-head.jpg');
INSERT INTO beers.beers (beer_id, name, abv, style_id, created_by, photo_url)
VALUES (10, 'Pilsner Urquell', 4.4, 1, 3, '/assets/img/beers/pilsner-urquell.jpg');
INSERT INTO beers.beers (beer_id, name, abv, style_id, created_by, photo_url)
VALUES (11, 'Hills Weizen', 4.7, 7, 1, '/assets/img/beers/default.jpg');
INSERT INTO beers.beers (beer_id, name, abv, style_id, created_by, photo_url)
VALUES (12, 'Hills Pils', 4.8, 1, 2, '/assets/img/beers/default.jpg');

INSERT INTO beers.users_beers (user_id, beer_id, drunk)
VALUES (1, 1, 0);
INSERT INTO beers.users_beers (user_id, beer_id, drunk)
VALUES (1, 2, 0);
INSERT INTO beers.users_beers (user_id, beer_id, drunk)
VALUES (1, 3, 1);
INSERT INTO beers.users_beers (user_id, beer_id, drunk)
VALUES (1, 5, 1);
INSERT INTO beers.users_beers (user_id, beer_id, drunk)
VALUES (2, 3, 0);
INSERT INTO beers.users_beers (user_id, beer_id, drunk)
VALUES (2, 4, 0);
INSERT INTO beers.users_beers (user_id, beer_id, drunk)
VALUES (3, 3, 1);
INSERT INTO beers.users_beers (user_id, beer_id, drunk)
VALUES (3, 5, 1);
INSERT INTO beers.users_beers (user_id, beer_id, drunk)
VALUES (3, 7, 1);
INSERT INTO beers.users_beers (user_id, beer_id, drunk)
VALUES (3, 8, 1);
INSERT INTO beers.users_beers (user_id, beer_id, drunk)
VALUES (3, 10, 1);
INSERT INTO beers.users_beers (user_id, beer_id, drunk)
VALUES (1, 11, 1);
INSERT INTO beers.users_beers (user_id, beer_id, drunk)
VALUES (2, 12, 1);
