#!/bin/bash

source bin/util/toolbox.sh

prompt_secrets () {
  DEBUG "not implemented yet"
}

main () {
  prompt_secrets "$@"

  local stack_name="$1"
  if [[ -z "$stack_name" ]]; then
    stack_name='kotlin-api-stack'
  fi
  docker stack deploy -c stack.yml "$stack_name" --with-registry-auth
}

main "$@"