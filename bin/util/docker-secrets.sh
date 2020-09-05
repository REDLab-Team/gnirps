#!/bin/bash

source bin/util/toolbox.sh
source bin/util/hash_password.sh

create_secret() {
  local name="$1"
  local value=''
  local force=''
  local secret=''
  local hash=''

  # parse args
  while [[ $# -gt 1 ]]; do
    key="$2"
    case $key in
    -f | --force)
      force="$key"
      ;;
    -h | --hash)
      hash='true'
      ;;
    -s | --secret)
      secret="$key"
      ;;
    *)
      value="$key"
      ;;
    esac
    shift
  done

  # remove already existing secret if need be
  if docker secret ls --format="{{ .Name }}" | grep -q "$name"; then
    if [ -z "$force" ]; then
      TRACE "secret $name already exists"
      return 0
    else
      error_message="[Error]: secret $name not removed."
      success_message="secret $name removed."
      cmd="docker secret rm $name"
      eval_command -f "$error_message" -s "$success_message" "$cmd"
    fi
  fi

  # prompt value if need be
  if [ -z "$value" ]; then
    prompt_value -m "enter $name: " -v "value" "$secret"
  fi

  # hash if need be
  if [ -n "$hash" ]; then
    hashed=''
    hash_password "$name" "$value"
    value="$hashed"
  fi

  # create new secret
  success_message="secret $name created with id \$output."
  error_message="[Error]: secret $name not created."
  cmd="printf %s $value | docker secret create $name -"
  eval_command -s "$success_message" -f "$error_message" "$cmd"
}
