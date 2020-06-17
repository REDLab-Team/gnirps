#!/bin/bash

request () {
  if [[ -z "$token" ]]; then
    source ./get_token.sh
  fi

  echo "-- send user request --"
  echo "---- using token: $token"
  curl -s \
    -X GET "http://localhost:8080/cats?direction=asc&page=0&size=10" \
    -H "accept: */*" \
    -H "$token"
}

request "$@"