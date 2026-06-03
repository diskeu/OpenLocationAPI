-- Creates WeatherSnapshots table

CREATE TABLE WeatherSnapshots (
    Id INT IDENTITY(1, 1),
    LocationId INT NOT NULL,
    SnapshotTime DATETIME2 NOT NULL
        CONSTRAINT DF_WeatherSnapshots_SnapshotTime
        DEFAULT SYSUTCDATETIME(),

    /* Most consistent approach considering
    the issues with a trigger or removing it. */
    LocalTime DATETIME2 NOT NULL,
    TemperatureC DECIMAL(4, 1) NOT NULL,
    WindKMH DECIMAL(5, 1) NOT NULL,
    Humidity TINYINT NOT NULL,
    UV TINYINT NOT NULL,
    /* Provides the ability to determinate how accurate
    the snapshot data really was */
    SourceLastUpdated DATETIME2 NOT NULL,
    APIConditionalCode SMALLINT NOT NULL,
    RawJSON NVARCHAR(MAX) NOT NULL,

    CONSTRAINT PK_WeatherSnapshot_Id
        PRIMARY KEY (Id),

    CONSTRAINT FK_WeatherSnapthots_LocationId
        FOREIGN KEY (LocationId)
        REFERENCES Locations (Id)
        ON DELETE CASCADE,

    CONSTRAINT UQ_LocationId_SnapshotTime
        UNIQUE (LocationId, SnapshotTime),
    
    CONSTRAINT CK_Valid_Humidity
        CHECK (Humidity <= 100), -- 0 - 100 implicitly
    
    CONSTRAINT CK_Valid_UV
        CHECK (UV <= 24) -- 0 - 24 implicitly
);

-- Using a Trigger would be inconsistent because you would have to keep track
-- of the current `Timezone offset` and not just the `IANA Time Zone name`.
-- The `Timezone offset` would be instable considering sommer / winter times etc.
-- Not saving any LocalTime value would include an external `IANA Time Zone` Database.
/* CREATE TRIGGER
TR_Insert_Local_Time
ON WeatherSnapshots
AFTER INSERT
AS
BEGIN
    UPDATE ws
    SET ws.LocalTime = DATEADD(HOUR, l.TimeZoneOffset, SYSUTCDATETIME())
    FROM WeatherSnapshots ws
    INNER JOIN inserted i
        ON i.id = ws.id
    INNER JOIN Locations l
        ON ws.LocationId = l.Id
    WHERE ws.LocalTime IS NULL ;
END; */