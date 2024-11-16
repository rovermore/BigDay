#!/bin/bash

# GH Actions Step: rc-gen
# Description: Generates the appropraite tag for a new release candidate build.
# Outputs:
#   rc_tag - The tag associated with the new RC.

# -- FUNCTIONS ----------------------------------------------------------------
# -----------------------------------------------------------------------------

function release_candidate_tags() {
  git tag | grep -e "^v$INPUT_VERSION\-rc[0-9]\{2\}\$"
}

# -- RUN ----------------------------------------------------------------------
# -----------------------------------------------------------------------------

current_rc_tag="$(release_candidate_tags | sort -r | head -1)"
current_rc_number="00"

[[ -n "$current_rc_tag" ]] && current_rc_number="${current_rc_tag:(-2)}"

new_rc_number="$(printf "%02d" $((10#$current_rc_number+1)))"
echo "rc_tag=v$INPUT_VERSION-rc$new_rc_number" >> $GITHUB_OUTPUT
