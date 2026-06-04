-- Create StarredLocations table

CREATE TABLE UserStarredLocations (
    UserId INT NOT NULL,
    LocationId INT NOT NULL,
    
    CONSTRAINT PK_UserId_LocationId
        PRIMARY KEY (UserId, LocationId),

    CONSTRAINT FK_UserId
        FOREIGN KEY (UserId)
        REFERENCES Users (Id)
        ON DELETE CASCADE,

    CONSTRAINT FK_LocationId
        FOREIGN KEY (LocationId)
        REFERENCES Locations (Id)
        ON DELETE CASCADE
);