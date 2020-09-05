#!/bin/bash

gen_jks() {
    cat ../out/ca/private.key ../out/ca/ca.crt |
        openssl pkcs12 -export -out ../out/ca/ca.p12 -name ca -password pass:tmp &&
        keytool -importkeystore \
            -srckeystore ../out/ca/ca.p12 -srcstoretype pkcs12 -srcstorepass tmp \
            -destkeystore ../out/ca/trust-store.jks
#        rm ../out/ca/ca.p12
}

if ! [[ $# -eq 0 ]]; then
    echo "Usage: gen_crt"
else
    mkdir -p ../out/ca || exit $?
    export COMMON_NAME="REDLab Certification Authority"
    export SUBJECT_ALT_NAMES="DNS:empty"
    openssl req -new -x509 -days 730 -extensions v3_ca \
        -keyout ../out/ca/private.key -out ../out/ca/ca.crt \
        -config ../resources/openssl.cnf &&
        chmod 0600 ../out/ca/private.key &&
        gen_jks
fi
