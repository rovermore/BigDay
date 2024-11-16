#!/bin/bash

# Constants
echo $(pwd)
PROPERTIES="gradle.properties"
KEY_ALIAS="$1"
RESULT_NAME="$2"
#Run
result=$(grep "$KEY_ALIAS" $PROPERTIES | awk '{print $NF}' | sed -r "s/$KEY_ALIAS//g")
if [ -z "$result" ]; then
  echo "result is empty"
  exit 1
fi
echo "$RESULT_NAME=$result" >> $GITHUB_OUTPUT
