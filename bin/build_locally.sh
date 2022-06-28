#!/bin/bash

source bin/util/toolbox.sh

find_modules() {
  local modules=()
  for dir in $(ls); do
    if [[ -f "$dir/pom.xml" ]]; then
      modules+=("$(basename "$dir")")
    fi
  done
  echo "${modules[@]}"
}

find_docker() {
  local docker=()
  for dir in $(ls); do
    if [[ -f "$dir/pom.xml" && -f "$dir/Dockerfile" ]]; then
      docker+=("$(basename "$dir")")
    fi
  done
  echo "${docker[@]}"
}

deploy_root() {
  error_message="parent package failed to build."
  success_message="parent package built and deployed."
  cmd="mvn clean deploy $1-N"
  INFO "deploy non-recursively the parent project on maven" &&
    eval_command -f "$error_message" -s "$success_message" "$cmd"
}

deploy_package() {
  error_message="package $1 failed to build."
  success_message="package $1 cleaned, built and deployed."
  cmd="cd $1 && mvn clean package deploy"
  INFO "mvn clean package deploy $2 '$1'" &&
    eval_command -f "$error_message" -s "$success_message" "$cmd"
}

build_docker_image() {
  error_message="docker image for package $1 failed to build."
  success_message="docker image for package $1 built."
  cmd="cd $1 && mvn docker:build"
  INFO "mvn $2 docker:build '$1'" &&
    eval_command -f "$error_message" -s "$success_message" "$cmd"
}

push_docker_image() {
  error_message="docker image for package $1 couldn't be pushed."
  success_message="docker image for package $1 pushed."
  cmd="cd $1 && mvn docker:push"
  INFO "mvn docker:push '$1'" &&
    eval_command -f "$error_message" -s "$success_message" "$cmd"
}

build() {
  declare -a packages=()
  declare -a docker=()
  local var_name="packages"
  local update=""
  local push=""

  if [[ $# -eq 0 ]] || [[ $# -eq 1 && "$1" == -U ]]; then
    if [[ "$1" == "-U" ]]; then
      update="-U"
    fi
    deploy_root "$update"
    IFS=" " read -r -a packages <<<"$(find_modules)"
    IFS=" " read -r -a docker <<<"$(find_docker)"
  else
    while [[ $# -gt 0 ]]; do
      key="$1"
      case $key in
      -U)
        update="-U "
        ;;
      --push)
        push="-p"
        ;;
      gnirps | root | parent)
        deploy_root "$update"
        ;;
      -p | --package)
        var_name="packages"
        ;;
      -d | --docker)
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

  for package in "${packages[@]}"; do
    deploy_package "$package" "$update"
  done

  for package in "${docker[@]}"; do
    build_docker_image "$package"
  done

  if [[ -n "$push" ]]; then
    for package in "${packages[@]}"; do
      push_docker_image "$package"
    done
  fi
}

build "$@"
