#!/bin/bash

# Constants
echo $(pwd)
PROPERTIES="gradle.properties"
VERSION_NAME="VERSION_NAME="
VERSION_NUMBER="VERSION_NUMBER="
echo $PROPERTIES
# Run
version=$(grep "$VERSION_NAME" $PROPERTIES | awk '{print $NF}' | sed -r "s/$VERSION_NAME//g")
echo "version = $version"
build=$(grep "$VERSION_NUMBER" $PROPERTIES | awk '{print $NF}' | sed -r "s/$VERSION_NUMBER//g")
echo "build = $build"
newBuildDate=$(date +"%y%m%d")
dateMatchingBuild=$(echo $build | grep "$newBuildDate")

# Build number
newBuild=''

if [ -z "$dateMatchingBuild" ]
then
newBuild="${newBuildDate}01"
else
newBuild="$(($build+1))"
fi
echo $newBuild
buildString="${VERSION_NUMBER}${build}"
newBuildString="${VERSION_NUMBER}${newBuild}"

sed -i "s/$buildString/$newBuildString/g" "$PROPERTIES"
# Version
if [ "$version" != "$1" ]
then
versionString="${VERSION_NAME}${version}"
newVersionString="${VERSION_NAME}${1}"
sed -i "s/$versionString/$newVersionString/g" "$PROPERTIES"
fi
echo "new version string = $newVersionString"
echo "new build string = $newBuildString"
