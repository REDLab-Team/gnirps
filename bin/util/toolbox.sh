#!/bin/bash

source bin/util/b-log.sh && LOG_LEVEL_ALL

prompt_value () {
  local secret=0
  local message=''
  local prefix=''
  local var_name=''
  value=''

  # parse args
  while [[ $# -gt 0 ]]; do
    key="$1"
    case $key in
      -h|--help)
        print '[HELP]: not implemented, oopsie doopsie...'
        return 0
      ;;
      -s|--secret)
        secret=1
      ;;
      -m|--message)
        shift
        message="$1"
      ;;
      -m=*|--message=*)
        message="${key#*=}"
      ;;
      -p|--prefix)
        shift
        prefix="$1"
      ;;
      -p=*|--prefix=*)
        prefix="${key#*=}"
      ;;
      -v|--var_name)
        shift
        var_name="$1"
      ;;
      -v=*|--var_name=*)
        var_name="${key#*=}"
      ;;
      *)
      ;;
    esac
    shift
  done

  # ensure variable's name is present
  if [ -z "$var_name" ]; then
    ERROR "missing variable 'var_name' in prompt"
    return 1
  fi

  # print prefix
  if [ -z "$prefix" ]; then
    echo -en "$prefix"
  fi

  # prompt value depending on the shell used
  case $SHELL in
    */zsh)
      if [ "$secret" -ne 0 ]; then
        read -sr "value?$prefix$message"
        echo
      else
        read -r "value?$prefix$message"
      fi
    ;;
    */bash)
      if [ "$secret" -ne 0 ]; then
        read -sp "$prefix$message" -r value
        echo
      else
        read -p "$prefix$message" -r value
      fi
    ;;
    *)
      if [ "$secret" -ne 0 ]; then
        read -sp "$prefix$message" -r value
        echo
      else
        read -p "$prefix$message" -r value
      fi
  esac

  # create variable with required name and value
  eval "$var_name=$value"
}

trim () {
  local s2 s="$*"
  until s2="${s#[\ \t]}"; [ "$s2" = "$s" ]; do s="$s2"; done
  until s2="${s%[\ \t]]}"; [ "$s2" = "$s" ]; do s="$s2"; done
  echo "$s"
}

print_lines () {
  while read -r line; do
      if [ -n "$(trim "$line")" ]; then
        TRACE "$line";
      fi
    done <<< "$2"
}

eval_command () {
  local success_msg=""
  local error_msg=""
  local prefix=""
  local default_output=false

  while [[ $# -gt 1 ]]; do
    key="$1"
    case $key in
      -s|--success)
        success_msg="$2"
        shift
      ;;
      -f|--failure)
        error_msg="$2"
        shift
      ;;
      -p|--prefix)
        prefix=$(echo -en "$2")
        shift
      ;;
      -o|--output)
        default_output=true
      ;;
      *)
        WARN "unknown argument $key passed to eval_command."
      ;;
    esac
    shift
  done

  if output=$(eval "$1 2>&1"); then
    if [ -n "$success_msg" ]; then
      msg=${success_msg/\$output/$output}
      TRACE "$msg"
    fi
    if $default_output; then print_lines "$prefix" "$output"; fi
  else
    ERROR "$error_msg"
    print_lines "$prefix" "$output"
    return 1;
  fi
}