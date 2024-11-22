#!/bin/bash

# Remove any old error_output_<UUID>.txt files
rm -f nullaway_error_*.txt

# Generate a new UUID for the current error output
uuid=$(uuidgen)

# Run the command, capture the error output, and display it on stdout
./gradlew compileJava 2>&1 >/dev/null | tee error_temp.txt

# Save the first 4 lines to a unique file
head -n 4 error_temp.txt > "nullaway_error_${uuid}.txt"

# Clean up the temporary file
rm error_temp.txt

# print content of nullaway_error_<UUID>.txt
echo "First NullAway error output:"
cat "nullaway_error_${uuid}.txt"
