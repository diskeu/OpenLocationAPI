-- Add Unique Constraint on `IANAName`

ALTER TABLE Locations
    ADD CONSTRAINT UQ_IANA_Name
        UNIQUE (IANAName);