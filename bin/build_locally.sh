#!/bin/bash

source bin/util/toolbox.sh

deploy_root () {
  error_message="parent package failed to build."
  success_message="parent package built and deployed."
  cmd='cd src && mvn clean deploy -N'
  INFO "deploy non-recursively the parent project on maven" &&
  eval_command -f "$error_message" -s "$success_message" "$cmd"
}

deploy_package () {
  error_message="package $1 failed to build."
  success_message="package $1 cleaned, built and deployed."
  cmd="cd src/$1 && mvn clean package deploy"
  INFO "mvn clean package deploy '$1'" &&
  eval_command -f "$error_message" -s "$success_message" "$cmd"
}

build_docker_image () {
  error_message="docker image for package $1 failed to build."
  success_message="docker image for package $1 built."
  cmd="cd src/$1 && mvn docker:build"
  INFO "mvn docker:build '$1'" &&
  eval_command -f "$error_message" -s "$success_message" "$cmd"
}

push_docker_image () {
  error_message="docker image for package $1 couldn't be pushed."
  success_message="docker image for package $1 pushed."
  cmd="cd src/$1 && mvn docker:push"
  INFO "mvn docker:push '$1'" &&
  eval_command -f "$error_message" -s "$success_message" "$cmd"
}

build () {
  declare -a packages=()
  declare -a docker=()
  local var_name="packages"

  # parse args
  if [[ $# -eq 0 ]]; then
    deploy_root
    packages=('commons' 'swagger' 'bash' 'template-api' 'postgresql-database' 'cat-api' 'self-genealogy')
    docker=('swagger' 'template-api' 'cat-api' 'self-genealogy')
  else
    while [[ $# -gt 0 ]]; do
      key="$1"
      case $key in
        gnirps-origin|root)
          deploy_root
        ;;
        -p|--package)
          var_name="packages"
        ;;
        -d|--docker)
          var_name="docker"
        ;;
        *)
          packages+=("$1")
          if [[ "$var_name" == "docker" ]]; then
            docker+=("$1")
          fi
        ;;
      esac
      shift
    done
  fi

  # build packages
  for package in "${packages[@]}"; do
    deploy_package "$package"
  done

  # build docker images
  for package in "${docker[@]}"; do
    build_docker_image "$package"
  done

  # push docker images
#  for package in "${packages[@]}"; do
#    push_docker_image "$package"
#  done
}

build "$@"