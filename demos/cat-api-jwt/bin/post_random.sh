#!/bin/bash

request() {
  if [[ -z "$token" ]]; then
    source ./get_token.sh
  fi

  echo "-- send user request --"
  echo "---- using token: $token"
  curl -s \
    -X POST "http://localhost:8080/cats/random" \
    -H "accept: */*" \
    -H "$token"
}

request "$@"
