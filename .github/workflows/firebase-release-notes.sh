#!/bin/bash

RELEASE_BRANCH=$(git branch --show-current)
MASTER_BRANCH="origin/master"

git_log_output=$(git log --oneline --grep "^SPK" $(git rev-list -n 1 $MASTER_BRANCH).."$RELEASE_BRANCH" | sed 's/^[^S]*SPK/SPK/')
echo "$git_log_output"  > firebase/release-notes.txt

cat firebase/release-notes.txt
