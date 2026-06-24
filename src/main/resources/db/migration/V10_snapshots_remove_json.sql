/* removes the `rawJson` field from the `WeatherSnapshots` table */

ALTER TABLE WeatherSnapshots
    DROP COLUMN IF EXISTS RawJSON;
