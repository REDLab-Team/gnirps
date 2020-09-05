#!/bin/bash

json_path="/tmp/realm-test.json"

docker exec -it cat-api-keycloak_sso_1 bash \
    /opt/jboss/keycloak/bin/standalone.sh\
    -Djboss.socket.binding.port-offset=100\
    -Dkeycloak.migration.action=import\
    -Dkeycloak.profile.feature.upload_scripts=enabled\
    -Dkeycloak.migration.provider=singleFile\
    -Dkeycloak.migration.file=$json_path
