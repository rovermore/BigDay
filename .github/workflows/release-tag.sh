#!/bin/bash
set -x #echo on
# GH Actions Step: release-tag
# Description: Sets release tag in master
# Inputs:
#   RELEASE_VERSION: The release version.

release_tag="v$RELEASE_VERSION"
git tag "$release_tag" HEAD
git push origin "$release_tag"
echo "release_tag = $release_tag"
