
CREATE TABLE green_tripdata_staging (
  id serial primary key,
  vendor_id text,
  lpep_pickup_datetime text,
  lpep_dropoff_datetime text,
  store_and_fwd_flag text,
  rate_code_id text,
  pickup_longitude numeric,
  pickup_latitude numeric,
  dropoff_longitude numeric,
  dropoff_latitude numeric,
  passenger_count text,
  trip_distance text,
  fare_amount text,
  extra text,
  mta_tax text,
  tip_amount text,
  tolls_amount text,
  ehail_fee text,
  improvement_surcharge text,
  total_amount text,
  payment_type text,
  trip_type text,
  pickup_location_id text,
  dropoff_location_id text,
  congestion_surcharge text,
  junk1 text,
  junk2 text
);
/*
N.B. junk columns are there because some tripdata file headers are
inconsistent with the actual data, e.g. header says 20 or 21 columns per row,
but data actually has 22 or 23 columns per row, which COPY doesn't like.
junk1 and junk2 should always be null
*/

CREATE TABLE yellow_tripdata_staging (
  id serial primary key,
  vendor_id text,
  tpep_pickup_datetime text,
  tpep_dropoff_datetime text,
  passenger_count text,
  trip_distance text,
  pickup_longitude numeric,
  pickup_latitude numeric,
  rate_code_id text,
  store_and_fwd_flag text,
  dropoff_longitude numeric,
  dropoff_latitude numeric,
  payment_type text,
  fare_amount text,
  extra text,
  mta_tax text,
  tip_amount text,
  tolls_amount text,
  improvement_surcharge text,
  total_amount text,
  pickup_location_id text,
  dropoff_location_id text,
  congestion_surcharge text,
  junk1 text,
  junk2 text
);