#!/bin/bash

source bin/util/toolbox.sh

deploy_package() {
  error_message="package failed to build."
  success_message="package cleaned, built and deployed."
  cmd="mvn clean package deploy"
  INFO "mvn clean package deploy $1" &&
    eval_command -f "$error_message" -s "$success_message" "$cmd"
}

build_docker_image() {
  error_message="docker image failed to build."
  success_message="docker image built."
  cmd="mvn docker:build"
  INFO "mvn $1docker:build" &&
    eval_command -f "$error_message" -s "$success_message" "$cmd"
}

push_docker_image() {
  error_message="docker image couldn't be pushed."
  success_message="docker image pushed."
  cmd="mvn docker:push"
  INFO "mvn docker:push" &&
    eval_command -f "$error_message" -s "$success_message" "$cmd"
}

build() {
  local update=""
  local push=""

  while [[ $# -gt 0 ]]; do
    key="$1"
    case $key in
    -U)
      update='-U '
      ;;
    --push)
      push='-p'
      ;;
    *) ;;

    esac
    shift
  done

  deploy_package "$update"

  if [[ -f "Dockerfile" ]]; then
    build_docker_image "$update"
    if [[ -n "$push" ]]; then
      push_docker_image
    fi
  fi
}

build "$@"
