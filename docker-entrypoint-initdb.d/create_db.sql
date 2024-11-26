DO $$
BEGIN
  IF NOT EXISTS (SELECT 1 FROM pg_catalog.pg_database WHERE datname = 'tmsdb') THEN
    CREATE DATABASE tmsdb;
  END IF;
END $$;

DO $$
BEGIN
  IF NOT EXISTS (SELECT 1 FROM pg_roles WHERE rolname = 'postgres') THEN
    CREATE USER postgres WITH PASSWORD 'postgres';
  END IF;
END $$;

GRANT ALL PRIVILEGES ON DATABASE tms TO postgres;