#!/bin/bash

# Check if the correct number of arguments are provided
if [ "$#" -ne 1 ]; then
    echo "Usage: $0 <environment>"
    exit 1
fi

# Check if the environment is DEVELOPMENT
if [ "$1" = "DEVELOPMENT" ]; then
    env_file=".env.dev"
elif [ "$1" = "QA" ]; then
    env_file=".env.qa"
elif [ "$1" = "PROD" ]; then
    env_file=".env.prod"
else
    env_file=".env"
fi

# Check if the .env file exists
if [ ! -f "$env_file" ]; then
    echo "Error: $env_file file not found"
    exit 1
fi

export $1="$env_file"
echo "Exported $1=$env_file"
