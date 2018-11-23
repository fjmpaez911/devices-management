CREATE TABLE IF NOT EXISTS devices (
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    name VARCHAR(128) NOT NULL,
    brand VARCHAR(64) NOT NULL,
    model VARCHAR(64) NOT NULL,
    is_booked TINYINT(1) NOT NULL DEFAULT 0,
    created_on TIMESTAMP NOT NULL DEFAULT now(),
  PRIMARY KEY (id),
  KEY model_index (model),
  KEY brand_index (brand)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS book_history (
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    device_id INT UNSIGNED NOT NULL,
    user_name VARCHAR(64) NOT NULL,
    booked_timestamp TIMESTAMP NOT NULL DEFAULT now(),
    returned_timestamp TIMESTAMP NOT NULL DEFAULT now(),
  PRIMARY KEY (id),
  KEY device_id_index (device_id),
  KEY user_name_index (user_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO devices (name, brand, model) VALUES ('Samsung Galaxy S9', 'samsung', 'galaxy s9');
INSERT INTO devices (name, brand, model) VALUES ('Samsung Galaxy S8', 'samsung', 'galaxy s8');
INSERT INTO devices (name, brand, model) VALUES ('Samsung Galaxy S7', 'samsung', 'galaxy s7');
INSERT INTO devices (name, brand, model) VALUES ('Motorola Nexus 6', 'motorola', 'nexus 6');
INSERT INTO devices (name, brand, model) VALUES ('LG Nexus 5X', 'lg', 'nexus 5x');
INSERT INTO devices (name, brand, model) VALUES ('Apple iPhone X', 'apple', 'iphone x');
INSERT INTO devices (name, brand, model) VALUES ('Apple iPhone 8', 'apple', 'iphone 8');
INSERT INTO devices (name, brand, model) VALUES ('Apple iPhone 4s', 'apple', 'iphone 4s');
INSERT INTO devices (name, brand, model) VALUES ('Nokia 3310', 'nokia', '3310');
INSERT INTO devices (name, brand, model) VALUES ('Nokia 3310', 'nokia', '3310');


