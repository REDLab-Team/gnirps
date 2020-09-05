#!/bin/bash

request() {
  local email="address@mail.com"
  if [[ -z "$token" ]]; then
    source ./get_token.sh
  fi

  echo "-- send user request --"
  echo "---- using token: $token"

  local users=$(./get_users.sh)
  local id=$(echo "${users//\{,\}/\\n}" | grep "$email" | cut -d',' -f 7 | cut -c7-42)

  curl -X DELETE "http://localhost:8080/users/$id" -H "accept: */*" -H "$token"
}

request "$@"
