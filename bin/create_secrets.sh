#!/bin/bash

source bin/util/toolbox.sh
source bin/util/docker-secrets.sh

main () {
  local force=''

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
      ;;
    esac
    shift
  done

  secrets=('cat-db-name' 'cat-db-user' 'cat-db-password')
  for secret in "${secrets[@]}"; do
    create_secret "$secret" "-s" "$force"
  done
}

main "$@"