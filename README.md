# OpenLocationAPI

OpenLocationAPI is a Spring Boot backend application that allows users to track weather data for saved locations,
search historical weather snapshots (a feature often missing from conventional weather apps), and access a 7-day weather forecast.
<img width="3456" height="1990" alt="image" src="https://github.com/user-attachments/assets/a9cdffa7-0ec1-4534-a500-04547c94a30d" />
**OpenLocationAPI tested with vibe coded frontend by passing deepseek the swagger ui documentation**
## Database
The default database engine is `Microsoft SQL Server` but through hibernates dialect it can be easily switched to another engine.
- There is also a docker compose file available to easily set up a MSSQL.
- migrations are maintained via flyway

If you want to execute sql in docker, there is already a small shell script mounted to the container.
Normally you would have to type `/opt/mssql-tools18/bin/sqlcmd -S localhost -U $MSSQL_USER -P $MSSQL_PASSWORD -C ...`
to just execute a query. With the Script, just type `sqlcmd {Query}`.

## Underlying API
To get the weather data the Backend uses the free [Weather API](https://www.weatherapi.com/).

## API Overview

The Open Location API is a REST API for managing users, locations, weather data and saved locations.
It uses JSON with `Jackson` for data exchange.

**Base URL:** `http://localhost:8080`

### Endpoints

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/auth/login` | Authenticate a user |
| POST | `/api/users` | Create a user account |
| GET | `/api/users/me` | Get current user |
| PUT | `/api/users/me` | Update username |
| DELETE | `/api/users/me` | Delete current user |
| PUT | `/api/users/me/password` | Change password |
| PUT | `/api/users/me/email` | Change email |
| GET | `/api/users/{id}` | Get user by ID |
| GET | `/api/users/search` | Search users by username |
| GET | `/api/users/me/starred` | Get starred locations |
| POST | `/api/users/me/starred/{locationId}` | Star a location |
| DELETE | `/api/users/me/starred/{locationId}` | Remove a starred location |
| GET | `/api/locations` | Get all locations |
| GET | `/api/locations/{id}` | Get location by ID |
| GET | `/api/locations/search` | Search locations by name |
| GET | `/api/locations/nearby` | Find nearby locations |
| GET | `/api/locations/iana/{ianaName}` | Find location by IANA timezone |
| GET | `/api/locations/{locationId}/weather` | Get current weather |
| GET | `/api/locations/{locationId}/weather/forecast` | Get weather forecast |
| GET | `/api/snapshots` | Search and filter weather snapshots |
| GET | `/api/snapshots/{id}` | Get snapshot by ID |

### Features

- User registration and authentication
- User profile managment
- Ability to update password email and username
- Location search and discovery
- Nearby location search using haversine function directly implemented into the sql query
- Favorite location management
- Current weather and forecasts (up to 7 day detailed forecast)
- Historical weather snapshots with bunch of filters
- the location search and snapshot endpoints support pagination

### API Documentation
Interactive API documentation is available through Swagger UI (Port
