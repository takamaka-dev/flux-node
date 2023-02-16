-- PRODUCTION

CREATE ROLE node_user WITH
  LOGIN
  NOSUPERUSER
  INHERIT
  NOCREATEDB
  NOCREATEROLE
  NOREPLICATION;

COMMENT ON ROLE node_user IS 'blockchain schema role admin';

ALTER ROLE node_user
        PASSWORD 'node_password';

CREATE DATABASE node_db
    WITH 
    OWNER = node_user
    ENCODING = 'UTF8'
    TEMPLATE = template0
    LC_COLLATE = 'en_US.UTF8'
    LC_CTYPE = 'en_US.UTF8'
    CONNECTION LIMIT = -1;

COMMENT ON DATABASE node_db
    IS 'blockchain database';

GRANT ALL ON DATABASE node_db TO node_user WITH GRANT OPTION;


-- change db --

\connect node_db;


CREATE SCHEMA "node_schema"
    AUTHORIZATION node_user;

COMMENT ON SCHEMA "node_schema"
    IS 'takamaka.io blockchain schema node_schema';

GRANT ALL ON SCHEMA "node_schema" TO node_user WITH GRANT OPTION;



-- TEST

CREATE ROLE node_user_test WITH
  LOGIN
  NOSUPERUSER
  INHERIT
  NOCREATEDB
  NOCREATEROLE
  NOREPLICATION;

COMMENT ON ROLE node_user_test IS 'blockchain schema role admin';

ALTER ROLE node_user_test
        PASSWORD 'node_password_test';

CREATE DATABASE node_db_test
    WITH 
    OWNER = node_user_test
    ENCODING = 'UTF8'
    TEMPLATE = template0
    LC_COLLATE = 'en_US.UTF8'
    LC_CTYPE = 'en_US.UTF8'
    CONNECTION LIMIT = -1;

COMMENT ON DATABASE node_db_test
    IS 'blockchain database';

GRANT ALL ON DATABASE node_db_test TO node_user_test WITH GRANT OPTION;


-- change db --

\connect node_db_test;


CREATE SCHEMA "node_schema_test"
    AUTHORIZATION node_user_test;

COMMENT ON SCHEMA "node_schema_test"
    IS 'takamaka.io blockchain schema node_schema_test';

GRANT ALL ON SCHEMA "node_schema_test" TO node_user_test WITH GRANT OPTION;
