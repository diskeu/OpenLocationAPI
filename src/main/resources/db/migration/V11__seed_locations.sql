-- Seed a few well-known locations so that weather and snapshot
-- features are usable out of the box.

IF NOT EXISTS (SELECT 1 FROM Locations WHERE LocationName = 'Berlin')
    INSERT INTO Locations (LocationName, IANAName, Latitude, Longitude)
    VALUES ('Berlin', 'Europe/Berlin', 52.520, 13.405);

IF NOT EXISTS (SELECT 1 FROM Locations WHERE LocationName = 'Vienna')
    INSERT INTO Locations (LocationName, IANAName, Latitude, Longitude)
    VALUES ('Vienna', 'Europe/Vienna', 48.210, 16.363);

IF NOT EXISTS (SELECT 1 FROM Locations WHERE LocationName = 'London')
    INSERT INTO Locations (LocationName, IANAName, Latitude, Longitude)
    VALUES ('London', 'Europe/London', 51.507, -0.128);

IF NOT EXISTS (SELECT 1 FROM Locations WHERE LocationName = 'New York')
    INSERT INTO Locations (LocationName, IANAName, Latitude, Longitude)
    VALUES ('New York', 'America/New_York', 40.713, -74.006);

IF NOT EXISTS (SELECT 1 FROM Locations WHERE LocationName = 'Tokyo')
    INSERT INTO Locations (LocationName, IANAName, Latitude, Longitude)
    VALUES ('Tokyo', 'Asia/Tokyo', 35.676, 139.650);