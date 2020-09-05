#! /bin/bash

gen_csr() {
    export COMMON_NAME="$1"
    export SUBJECT_ALT_NAMES="$2"

    openssl req \
        -config ../resources/openssl.cnf \
        -newkey rsa -nodes -keyout "../out/$1/$1.key" \
        -out "../out/$1/$1.csr" \
        -extensions v3_req

    chmod 600 "../out/$1/$1.key"
}

sign_csr() {
    if ! [[ -e ../resources/index.txt ]]; then touch ../resources/index.txt; fi
    if ! [[ -e ../resources/serial ]]; then echo "01" > ../resources/serial; fi

    export COMMON_NAME="$1"
    export SUBJECT_ALT_NAMES="$2"

    openssl ca \
        -out "../out/$1/$1.crt" -config ../resources/openssl.cnf \
        -notext -extensions v3_req \
        -infiles "../out/$1/$1.csr"
}

gen_jks() {
    cat "../out/$1/$1.key" "../out/$1/$1.crt" |
        openssl pkcs12 -export -out "../out/$1/key-store.p12" -name "$1" &&
        keytool -importkeystore \
            -srckeystore "../out/$1/key-store.p12" -srcstoretype pkcs12 \
            -destkeystore "../out/$1/key-store.jks"
#        rm "../out/$1/$1.p12"
}

if [[ $# -eq 0 ]]; then
    echo "Usage: gen_crt <name> [\"<DNS:san1,DNS:san2,IP:san3>\"]"
else
    mkdir -p "../out/$1" &&
    gen_csr "$1" "$2" &&
    sign_csr "$1" "$2" &&
    gen_jks "$1"
fi
