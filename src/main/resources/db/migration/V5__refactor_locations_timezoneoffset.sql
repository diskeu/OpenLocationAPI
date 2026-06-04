/* Refactor Locations table TimeZoneOffset
to IANA names instead of `SMALLINT` to prevent
mismatches due summer / winter times etc. */

ALTER TABLE Locations
    DROP CONSTRAINT CK_Valid_TimeZoneOffset;

ALTER TABLE Locations
    DROP COLUMN TimeZoneOffset;

ALTER TABLE Locations
    ADD IANAName VARCHAR(48) NOT NULL;