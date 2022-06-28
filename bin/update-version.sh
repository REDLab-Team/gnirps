#/bin/bash

# parse args
while [[ $# -gt 0 ]]; do
    key="$1"
    case $key in
    -h | --help)
        echo "not implemented, sorry"
        ;;
    --rm)
        rm_backup=1
        ;;
    --from)
        shift
        from="$1"
        ;;
    --from=*)
        from="${key#*=}"
        ;;
    --to)
        shift
        to="$1"
        ;;
    --to=*)
        to="${key#*=}"
        ;;
    *) ;;
    esac
    shift
done

#update version with backups
if [[ -n "$from" && -n "$to" ]]; then
    find . -name "pom.xml" -exec sed -i.bak "s/$from/$to/" {} +
fi

# remove backups
if [[ $rm_backup -eq 1 ]]; then
    find . -name "pom.xml.bak" -exec rm -rf {} \;
fi
