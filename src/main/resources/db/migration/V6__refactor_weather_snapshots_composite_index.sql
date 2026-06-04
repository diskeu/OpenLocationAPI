/* Refactor WeatherSnapshots UNIQUE Constraint
`UQ_LocationId_SnapshotTime` to composite UNIQUE Index */

ALTER TABLE WeatherSnapshots
    DROP CONSTRAINT UQ_LocationId_SnapshotTime;

CREATE UNIQUE INDEX IX_LocationId_SnapshotTime
    ON WeatherSnapshots (LocationId, SnapshotTime);