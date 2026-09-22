#!/bin/bash
# Compiles and starts the Car Rental System (Linux / macOS / Git Bash).
# Needs: JDK 11+ and the MySQL JDBC driver jar inside the lib folder.
set -e
cd "$(dirname "$0")"

if ! ls lib/*.jar >/dev/null 2>&1; then
    echo "MySQL JDBC driver not found. Download mysql-connector-j from"
    echo "https://dev.mysql.com/downloads/connector/j/ and put the .jar in the lib folder."
    exit 1
fi

mkdir -p bin
javac -encoding UTF-8 -cp "lib/*" -d bin $(find src -name '*.java')
# "src" is on the classpath so the images next to the source files are found
java -cp "bin:src:lib/*" app.App
