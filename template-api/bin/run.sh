#!/bin/bash

source bin/util/toolbox.sh

main() {
  local secrets=()

  # choose stack name here
  local name="gnirpstack"

  # list required secrets here
  local default_secrets=()

  while [[ $# -gt 0 ]]; do
    key="$1"
    case $key in
    -h | --help)
      print '[HELP]: not implemented, oopsie doopsie...'
      return 0
      ;;
    -n | --name)
      shift
      name="$1"
      ;;
    -n=* | --name=*)
      name="${key#*=}"
      ;;
    -f | --force)
      force='--force'
      ;;
    *)
      secrets+=("$1")
      ;;
    esac
    shift
  done

  secrets=("${secrets[@]:-${default[@]}}")
  source bin/create_secrets.sh "$force" "${default_secrets[@]}"

  docker stack deploy -c stack.yml "$name" --with-registry-auth
}

main "$@"
