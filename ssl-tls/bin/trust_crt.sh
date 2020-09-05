#!/bin/bash

if ! [[ $# -gt 1 ]]; then
  echo "Usage: trust_crt <believer-name> <trusted-name> [<other-trusted-name>]"
else
  if ! [[ -e "../out/${1}/trust-store.jks" ]]; then
    cp ../out/ca/trust-store.jks "../out/$1/trust-store.jks"
  fi
  echo -e "--> importing $2..." &&
    keytool -import -alias "$2" -file "../out/$2/$2.crt" -storetype JKS -keystore "../out/$1/trust-store.jks"
fi

if [[ $# -gt 2 ]]; then
  ./trust_crt.sh "$1" "${@:3}"
fi
