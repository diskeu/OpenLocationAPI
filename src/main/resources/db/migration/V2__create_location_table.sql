-- Create location table

CREATE TABLE Locations (
    Id INT IDENTITY(1, 1),
    LocationName VARCHAR(255) NOT NULL,
    TimeZoneOffset SMALLINT NOT NULL,
    
    CONSTRAINT PK_Location_Id
        PRIMARY KEY (Id),
    
    CONSTRAINT UQ_Location_Name
        UNIQUE (LocationName),
    
    CONSTRAINT CK_Valid_TimeZoneOffset
        CHECK (TimeZoneOffset BETWEEN -18 AND 18)
);