#!/bin/sh

/opt/mssql-tools18/bin/sqlcmd -S localhost -U $MSSQL_USER -P $MSSQL_PASSWORD -C "$@"
