#!/bin/bash

request () {
    if [[ -z "$token" ]]; then
    source ./get_token.sh
  fi

  echo "-- send user request --"
  echo "---- using token: $token"
  curl \
    -X POST "http://localhost:8080/users" \
    -H "accept: */*" \
    -H "$token" \
    -H "Content-Type: application/json" \
    -d "{ \"accessLevel\": \"BASIC_USER\", \"description\": \"string\", \"email\": \"address@mail.com\", \"firstName\": \"string\", \"lastName\": \"string\", \"password\": \"string\"}"
}

request "$@"