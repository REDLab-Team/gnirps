#!/bin/bash

source bin/util/toolbox.sh

hash_password() {
  if [ -z "$(command -v htpasswd)" ]; then
    ERROR "missing command htpasswd."
    TRACE "Install apache2-utils package to get it."
    exit 1
  fi

  local username="$1"
  local password="$2"

  full=$(htpasswd -nb "$username" "$password")
  export full

  hashed="${full#"$username:"}"
  export hashed
}
