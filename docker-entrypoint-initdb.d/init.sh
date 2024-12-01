#!/bin/bash
set -e
export PGPASSWORD=$POSTGRES_PASSWORD
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
CREATE USER postgres WITH PASSWORD 'postgres';
CREATE DATABASE tmsdb;
GRANT ALL PRIVILEGES ON DATABASE tmsdb TO postgres;
\connect tmsdb postgres
BEGIN;
COMMIT;
EOSQL