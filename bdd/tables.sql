
CREATE TABLE TRIP_DATA(
   medallion varchar (32),
   hack_license varchar (32),
   vendor_id varchar (3),
   rate_code int2,
   store_and_fwd_flag varchar (1),
   pickup_datetime TIMESTAMP,
   dropoff_datetime TIMESTAMP,
   passenger_count int2,
   trip_time_in_secs int,
   trip_distance real,
   pickup_longitude real,
   pickup_latitude real,
   dropoff_longitude real,
   dropoff_latitude real
);

CREATE TABLE TRIP_FARE(
    medallion varchar(32),
    hack_license varchar (32),
    vendor_id varchar (3),
    pickup_datetime TIMESTAMP,
    payment_type varchar(3),
    fare_amount real,
    surcharge real,
    mta_max real,
    tip_amount real,
    tolls_amount real,
    total_amount real
);

CREATE TABLE DRIVERS ( 
    hack_license VARCHAR (32),
    name VARCHAR (127),
    surname VARCHAR (127) 
);