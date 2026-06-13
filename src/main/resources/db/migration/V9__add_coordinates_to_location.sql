-- add coordinates to location with latitude and longitude

ALTER TABLE locations
ADD
    Latitude DECIMAL(6, 3) NOT NULL,
    Longitude DECIMAL(6, 3) NOT NULL,

    CONSTRAINT UQ_Coordinate
        UnIQUE (Latitude, Longitude);
