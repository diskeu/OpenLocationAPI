-- Create User Table

CREATE TABLE Users (
    Id INT IDENTITY(1, 1),
    UserName VARCHAR(64) NOT NULL,
    UserEmail VARCHAR(255) NOT NULL,
    UserPasswordHash VARBINARY(255) NOT NULL,

    CONSTRAINT CK_Users_UserName_Length
        CHECK (LEN(UserName) >= 4),

    CONSTRAINT PK_Users
        PRIMARY KEY (Id),
    
    CONSTRAINT UQ_Users_UserName
        UNIQUE (UserName),

    CONSTRAINT UQ_Users_UserEmail
        UNIQUE (UserEmail)
);