#!/bin/bash

request () {
  local username="${1:-admin@b.c}"
  local password="${2:-admin}"

  echo "-- send token request --"
  token=$(curl -si \
    -X POST "http://localhost:8080/login" \
    -H "Content-Type: application/json" \
    -d "{ \"password\": \"$password\", \"username\": \"$username\"}" |
    grep "Authorization")

  export token="$token"
  echo "--- exported token: $token"
}

request "$@"