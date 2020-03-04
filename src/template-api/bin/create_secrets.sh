#!/bin/bash

source bin/util/toolbox.sh
source bin/util/docker-secrets.sh

main () {
  local force=''
  local secrets=()

  # parse args
  while [[ $# -gt 0 ]]; do
    key="$1"
    case $key in
      -h|--help)
        print '[HELP]: not implemented, oopsie doopsie...'
        return 0
      ;;
      -f|--force)
        force='--force'
      ;;
      *)
        secrets+=("$1")
      ;;
    esac
    shift
  done

  for secret in "${secrets[@]}"; do
    create_secret "$secret" "--secret" "$force"
  done
}

main "$@"