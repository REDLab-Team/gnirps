#!/bin/bash

source bin/util/toolbox.sh

build_image() {
  error_message="temporary docker image failed to build."
  success_message="temporary docker image successfully built."
  cmd='docker build . -f build.Dockerfile --rm -t tmp:build'
  INFO "build temporary docker image" &&
    eval_command -f "$error_message" -s "$success_message" "$cmd"
}

remove_image() {
  error_message="removal of temporary docker image failed."
  success_message="temporary docker image successfully removed."
  cmd='docker rmi -f tmp:build'
  INFO "remove temporary docker image" &&
    eval_command -f "$error_message" -s "$success_message" "$cmd"
}

main() {
  build_image &&
    docker run --rm -v /var/run/docker.sock:/var/run/docker.sock -e "ARGS=$*" tmp:build &&
    remove_image
}

main "$@"
