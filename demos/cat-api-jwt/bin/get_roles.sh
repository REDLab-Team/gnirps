#!/bin/bash

request() {
  if [[ -z "$token" ]]; then
    source ./get_token.sh
  fi

  echo "-- send role request --"
  echo "---- using token: $token"
  curl -s \
    -X GET "http://localhost:8080/roles" \
    -H "accept: */*" \
    -H "$token"
}

request "$@"
