# observability_demo
observability demo for AlmeriaJs talk

Three services:
- User
- Product
- cart


# Deployment

## Jaeger

```
docker run --rm --name jaeger -e COLLECTOR_OTLP_ENABLED=true -p 16686:16686 -p 4317:4317 jaegertracing/all-in-one:1.35
```
- Open in the browser
http://localhost:16686/

# Dynatrace

See logs https://nnr98912.apps.dynatrace.com

## Otel-collector

- Download
```
cd otel-collector
curl --proto '=https' --tlsv1.2 -fOL https://github.com/open-telemetry/opentelemetry-collector-releases/releases/download/v0.89.0/otelcol_0.89.0_darwin_arm64.tar.gz
tar -xvf otelcol_0.89.0_darwin_arm64.tar.gz
```

- Run the collector
```
export DYNATRACE_TOKEN=<TOKEN>
export GRAFANA_INSTANCE_ID=<ID>
export GRAFANA_TOKEN=<TOKEN>
export GRAFANA_AUTHORIZATION="Basic $(echo -n $GRAFANA_INSTANCE_ID:$GRAFANA_TOKEN | base64)"
./otelcol --config otel-config.yaml
```

## User service
- Build
```
cd user
./gradlew clean assemble
```

- Run user service
```
export OTEL_SERVICE_NAME=user-service
export OTEL_EXPORTER_OTLP_PROTOCOL=http/protobuf
export OTEL_EXPORTER_OTLP_ENDPOINT=http://localhost:55679
export OTEL_TRACES_EXPORTER=otlp
export OTEL_METRICS_EXPORTER=otlp
export OTEL_LOGS_EXPORTER=otlp
java -javaagent:..//opentelemetry-javaagent.jar -jar ./build/libs/user-0.0.1-SNAPSHOT.jar --server.port=8081
```

## Product service
- Build
```
cd product
./gradlew clean assemble
```

- Run product service
```
export OTEL_SERVICE_NAME=product-service
export OTEL_EXPORTER_OTLP_PROTOCOL=http/protobuf
export OTEL_EXPORTER_OTLP_ENDPOINT=http://localhost:55679
export OTEL_TRACES_EXPORTER=otlp
export OTEL_METRICS_EXPORTER=otlp
export OTEL_LOGS_EXPORTER=otlp
java -javaagent:..//opentelemetry-javaagent.jar -jar ./build/libs/product-0.0.1-SNAPSHOT.jar --server.port=8082
```

## Cart service
- Build
```
cd cart
./gradlew clean assemble
```

- Run cart service
```
export OTEL_SERVICE_NAME=cart-service
export OTEL_EXPORTER_OTLP_PROTOCOL=http/protobuf
export OTEL_EXPORTER_OTLP_ENDPOINT=http://localhost:55679
export OTEL_TRACES_EXPORTER=otlp
export OTEL_METRICS_EXPORTER=otlp
export OTEL_LOGS_EXPORTER=otlp
java -javaagent:..//opentelemetry-javaagent.jar -jar ./build/libs/cart-0.0.1-SNAPSHOT.jar --server.port=8083
```

# Runing demo

## User

```
curl --location 'http://localhost:8081/users/1'
```
```
{
    "id": 1,
    "name": "Antonio"
}
```

### Logs

### Trace

## Product

```
curl --location 'http://localhost:8082/products/search' --header 'Content-Type: application/json' --data '[1, 2, 3, ]'
```
```
[
    {
        "id": 1,
        "name": "Water",
        "price": 0.28,
        "description": "Bottle of water",
        "category": "FOOD"
    },
    {
        "id": 2,
        "name": "Pizza",
        "price": 10.5,
        "description": "Piza margarita",
        "category": "FOOD"
    },
    {
        "id": 3,
        "name": "Beer",
        "price": 0.62,
        "description": "Bottle of beer",
        "category": "FOOD"
    }
]
```

### Logs

### Trace

## Cart


```
curl --location 'http://localhost:8083/v1/carts' --header 'Content-Type: application/json' \
--data '{
    "user": 1,
    "products": {
        "1": 18,
        "2": 10,
        "3": 88
    }
}'

```

```
{
    "id": 1,
    "userId": 1,
    "productIdsAndAmount": {
        "1": 18,
        "2": 10,
        "3": 88
    },
    "total": 164.6
}
```


### Logs

### Trace


# Debugging

## Performance investigation

## Failure investigation

## Service map