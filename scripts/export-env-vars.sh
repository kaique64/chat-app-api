#!/bin/bash

if [ "$1" = "DEVELOPMENT" ]; then
    env_file=".env.dev"
elif [ "$1" = "QA" ]; then
    env_file=".env.qa"
elif [ "$1" = "PROD" ]; then
    env_file=".env.prod"
else
    env_file=".env"
fi

if [ ! -f "$env_file" ]; then
    echo "Error: $env_file file not found"
    exit 1
fi

export $(cat $env_file | xargs)
if [ -z "$1" ]; then
    echo "Exported LOCAL"
else
  echo "Exported $1=$env_file"
fi
