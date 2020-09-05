#!/bin/bash

if [[ $# -eq 0 ]]; then
  echo "Usage: rm_crt <name>"
else
  rm -r "../out/$1"
fi
