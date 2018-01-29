# ClearScore Credit Card service

## Build
To prepare the service for deploy/run execute the following:

    $ git clone https://github.com/thenobody/clearscore-creditcards.git
    $ cd clearscore-creditcards
    $ sbt universal:packageBin

After finishing, a self-contained zip archive will become available at:

```
credit-cards-service/target/universal/credit-cards-service-0.0.1.zip
```

### Tests
To run the unit tests for both submodules run

	$ sbt test

## Running

The service can be run after unpacking the zip file mentioned in section [Build](#build).

	$ unzip credit-cards-service/target/universal/credit-cards-service-0.0.1.zip
	$ credit-cards-service-0.0.1/bin/credit-cards-service
	
The service is ready to receive request after the following is printed into the console:

	00:39:30.889 [default-akka.actor.default-dispatcher-5] INFO  n.t.c.creditcards.service.Main$ - Server successfully started on /0:0:0:0:0:0:0:0:8080

## Configuration
The service uses [Lightbend Config](https://github.com/lightbend/config) and as such can be configured externally either by:

### 1. ENV variables
Some paramaters are overridable directly, namely:

* `HTTP_INTEFACE` - host to which the service binds, default `0.0.0.0`
* `HTTP_PORT` - port to which the service binds, default `8080`
* `CSCARDS_ENDPOINT` - the base endpoint of CSCards API, default `https://y4xvbk1ki5.execute-api.us-west-2.amazonaws.com/CS`
* `SCOREDCARDS_ENDPOINT` - the base endpoint of CSCards API, default `https://m33dnjs979.execute-api.us-west-2.amazonaws.com/CS`

To override any of these, simply provide ENV variables when starting the service, e.g.

	$ HTTP_PORT=8081 ./credit-cards-service-0.0.1/bin/credit-cards-service
	
### 2. Config file
An external `application.conf` file 	can be supplied via
	
	./credit-cards-service-0.0.1/bin/credit-cards-service \
	  -Dconfig.file=/path/to/application.conf
The structure of the external configuration file should follow format of [`application.conf`](https://github.com/thenobody/clearscore-creditcards/blob/master/credit-cards-service/src/main/resources/application.conf)

## Implementation notes
The service follows the assignment requiremets specified in the PDF document.
The HTTP service is implemented as a _akka-http_ application with basic usage of _cats_.

The implementation consists for two SBT submodules:

### `credit-cards-core`

The submodule contains model classes, client and service implementations for both credit card providers (_CSCards_ and _ScoredCards_) as well as unit tests.

The structure of submodule follows the separation between _CSCards_-related logic and _ScoredCards_-related logic.
Packges [`net.thenobody.clearscore.creditcards.core.model.cscards`](https://github.com/thenobody/clearscore-creditcards/tree/master/credit-cards-core/src/main/scala/net/thenobody/clearscore/creditcards/core/model/cscards) and [`net.thenobody.clearscore.creditcards.core.model.scoredcards`](https://github.com/thenobody/clearscore-creditcards/tree/master/credit-cards-core/src/main/scala/net/thenobody/clearscore/creditcards/core/model/scoredcards) contain entity classes representing the responses from their respective APIs.

**_Note:_** I've originally attempted to use the classes generated by `swagger-codegen`. However, this even after a substantial effort wasn't feasible as the swagger-generated code depends on spray-http which prevents akka to operate normally. As such, I've opted to extract the generated model classes into these packages and skip generating swagger-code.

Client classes are similarly split across in [`net.thenobody.clearscore.creditcards.core.service.cscards`](https://github.com/thenobody/clearscore-creditcards/tree/master/credit-cards-core/src/main/scala/net/thenobody/clearscore/creditcards/core/service/cscards) and [`net.thenobody.clearscore.creditcards.core.service.scoredcards`](https://github.com/thenobody/clearscore-creditcards/tree/master/credit-cards-core/src/main/scala/net/thenobody/clearscore/creditcards/core/service/scoredcards).

Additionally, collecting the API responses is abstracted into [`CardsService`](https://github.com/thenobody/clearscore-creditcards/blob/master/credit-cards-core/src/main/scala/net/thenobody/clearscore/creditcards/core/service/CardsService.scala) and implementations. These are subsequently used in the main service.

### `credit-cards-service`

The HTTP service logic is defined here as a akka-http service with two HTTP endpoints (defined in [`RootRoute`](https://github.com/thenobody/clearscore-creditcards/blob/master/credit-cards-service/src/main/scala/net/thenobody/clearscore/creditcards/service/route/RootRoute.scala))

`/creditcards` - expects POST requests as specified in the swagger json [Microservice-swagger.json](https://github.com/thenobody/clearscore-creditcards/blob/master/swagger/Microservice-swagger.json). The endpoint responds with

*  `HTTP 200` on valid input
*  `HTTP 400` if the request body does not follow the required schema coupled with a `{ "error": ... }` message
*  `HTTP 500` if there was an error reading from one or more external APIs (or other issues)

`/ping` - alwasys responds with `HTTP 200` and body `pond`