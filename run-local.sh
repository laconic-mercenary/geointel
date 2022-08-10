#!/bin/bash

docker build . -t=repository:local

docker run --rm \
    -p 31999:8080 \
    repository:local

echo "curl -v http://localhost:31999/"
