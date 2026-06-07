/* Change `userPasswordHash` to VARCHAR(255)
to provide better compatibility for java encryption */

ALTER TABLE Users
    ALTER COLUMN userPasswordHash VARCHAR(255);
