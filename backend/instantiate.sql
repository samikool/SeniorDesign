DROP TABLE drinks;
DROP TABLE bbqs;
DROP TABLE sides;
DROP TABLE receipts;
DROP TYPE item;


CREATE TYPE item AS(
    NAME TEXT,
    PRICE REAL,
    QUANTITY INT
);

CREATE TABLE bbqs (
    ID SERIAL PRIMARY KEY NOT NULL,
    NAME TEXT NOT NULL,
    PRICE REAL NOT NULL,
    PICTURE BYTEA
);

CREATE TABLE drinks (
    ID SERIAL PRIMARY KEY NOT NULL,
    NAME TEXT NOT NULL,
    PRICE REAL NOT NULL,
    PICTURE BYTEA
);

CREATE TABLE sides (
    ID SERIAL PRIMARY KEY NOT NULL,
    NAME TEXT NOT NULL,
    PRICE REAL NOT NULL,
    PICTURE BYTEA
);

CREATE TABLE receipts (
    ID SERIAL PRIMARY KEY NOT NULL,
    TABLEID INT NOT NULL,
    TOTAL REAl NOT NULL,
    ITEMS item[] NOT NULL
);



INSERT INTO bbqs (NAME, PRICE) VALUES
('samgyupsal', 15.95),
('wanggalbi', 29.95),
('bulgogi', 15.95),
('galbi', 22.95),
('buldak', 15.95),
('chadolbaki', 22.95);

INSERT INTO drinks (NAME, PRICE) VALUES
('water', 0),
('pepsi', 2.5),
('diet_pepsi', 2.5),
('root_beer', 2.5),
('mt._dew', 2.5),
('lemonade', 2.5),
('seirra_mist', 2.5),
('rammune', 2.5),
('white_milk', 2.5),
('chocolate_milk', 1.5),
('strawberry_milk', 1.5);

INSERT INTO sides (NAME, PRICE) VALUES
('kimchi', 1),
('potato_salad', 1),
('black_beans', 1),
('bean_sprouts', 1),
('pink_radish', 1),
('white_radish', 1),
('yellow_radish', 1),
('spinach', 1);

-- INSERT INTO receipts (TABLEID, TOTAL, ITEMS) VALUES
-- (1, 36.9, ARRAY[ROW('samgypsal',15.95,2)::item, ROW('pepsi',2.5,2)::item]),
-- (1, 36.9, ARRAY[ROW('samgypsal',15.95,2)::item, ROW('pepsi',2.5,2)::item]),
-- (1, 36.9, ARRAY[ROW('samgypsal',15.95,2)::item, ROW('pepsi',2.5,2)::item]),
-- (1, 36.9, ARRAY[ROW('samgypsal',15.95,2)::item, ROW('pepsi',2.5,2)::item]);

SELECT * FROM drinks;
SELECT * FROM bbqs;
SELECT * FROM sides;
SELECT * FROM receipts;