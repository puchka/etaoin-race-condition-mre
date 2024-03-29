# README

You need to have geckodriver in your path.

Steps to install it can be found in the Dockerfile for Linux systems.

For other systems, the binaries can be download from here:
https://github.com/mozilla/geckodriver/releases/

You need to have JRE, and Clojure installed [1] on your system or docker if
you use the Dockerfile. [2]

## How to run

Running locally

```
clojure -X core/-main
```

Using docker

```
docker build -t etaoin-race-condition-mre .
docker run -it -v $PWD/docker-data:/tmp/data etaoin-race-condition-mre sh
```

## References

- [1] https://clojure.org/guides/install_clojure
- [2] https://docs.docker.com/engine/install/
