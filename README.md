# Discrete ZOO website

For the live website code, see the branch `live`.

## Running the application

In the folder `zooapp`:
```shell
$ sbt
> jetty:start
```
To automatically restart the application on code changes:
```shell
$ sbt
> ~;jetty:stop;jetty:start
```
