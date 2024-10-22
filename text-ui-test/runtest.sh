#!/usr/bin/env bash

cd "${0%/*}"

cd ..
./gradlew clean shadowJar || { echo "Gradle build failed!"; exit 1; }

cd text-ui-test

JAR_PATH=$(find ../build/libs/ -mindepth 1 -name "*.jar" -print -quit)
if [ -z "$JAR_PATH" ]; then
    echo "Error: JAR file not found!"
    exit 1
fi

java -jar "$JAR_PATH" < input.txt > ACTUAL.TXT

if command -v dos2unix &> /dev/null; then
    cp EXPECTED.TXT EXPECTED-UNIX.TXT
    dos2unix EXPECTED-UNIX.TXT ACTUAL.TXT
else
    echo "Warning: dos2unix not found. Skipping conversion."
fi

if diff EXPECTED-UNIX.TXT ACTUAL.TXT; then
    echo "Test passed!"
    exit 0
else
    echo "Test failed! Differences:"
    diff EXPECTED-UNIX.TXT ACTUAL.TXT
    exit 1
fi
