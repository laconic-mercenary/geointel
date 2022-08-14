/* 

createdb -E utf8 geointel

psql -U admin geointel

*/

CREATE TABLE repository (
    id                  serial PRIMARY KEY,
    latitude            float(8)                NOT NULL,
    longitude           float(8)                NOT NULL,
    accuracy            integer                 NOT NULL,
    note                VARCHAR(759)            NOT NULL,
    img                 BYTEA                   NOT NULL,
    created             timestamptz             default current_timestamp
);

CREATE ROLE geointelapp WITH PASSWORD 'password';
ALTER ROLE geointelapp WITH LOGIN;
GRANT SELECT, INSERT, UPDATE on repository to geointelapp;
GRANT USAGE, SELECT ON SEQUENCE repository_id_seq TO geointelapp;
