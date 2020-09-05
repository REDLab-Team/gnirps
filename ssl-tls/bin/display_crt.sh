#!/bin/bash
if [[ $# -eq 0 ]]; then
  echo "Usage: gen_crt <name>"
else
  openssl x509 -noout -text -in ../out/${1}.crt
fi
