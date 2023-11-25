# observability_demo
Observability demo.

Three services:
- User
- Product
- cart

Each service is been auto instrumented using https://opentelemetry.io/docs/instrumentation/java/automatic/.
They are sending telemetry data (logs, traces and metrics) to OpenTelmetry collector.
The collector is sending the telemetry data to different observability backend:
- Metrics -> Grafana
- Logs -> Dynatrace
- Traces -> Jaeger

![Demo obesvability.jpeg](img%2FDemo%20obesvability.jpeg)

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

#### Logs
![Screenshot 2023-11-25 at 12.54.28.png](img%2Frunning-demo%2FScreenshot%202023-11-25%20at%2012.54.28.png)

#### Trace
![Screenshot 2023-11-25 at 12.56.08.png](img%2Frunning-demo%2FScreenshot%202023-11-25%20at%2012.56.08.png)

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

#### Logs
![Screenshot 2023-11-25 at 12.58.12.png](img%2Frunning-demo%2FScreenshot%202023-11-25%20at%2012.58.12.png)

#### Trace

![Screenshot 2023-11-25 at 12.58.36.png](img%2Frunning-demo%2FScreenshot%202023-11-25%20at%2012.58.36.png)

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


#### Logs

![Screenshot 2023-11-25 at 12.59.49.png](img%2Frunning-demo%2FScreenshot%202023-11-25%20at%2012.59.49.png)

#### Trace

![Screenshot 2023-11-25 at 13.00.41.png](img%2Frunning-demo%2FScreenshot%202023-11-25%20at%2013.00.41.png)

# Debugging

## Performance investigation

- Discover we are querying `product-service` for each product:
![Screenshot 2023-11-25 at 13.56.59.png](img%2Fdebug%2Fperformance%2FScreenshot%202023-11-25%20at%2013.56.59.png)
- Instead, we can query `product-service` and passing all the products `http://localhost:8082/products/search`
![Screenshot 2023-11-25 at 14.00.12.png](img%2Fdebug%2Fperformance%2FScreenshot%202023-11-25%20at%2014.00.12.png)

- We can now compare the traces and see the different:
![Screenshot 2023-11-25 at 14.01.53.png](img%2Fdebug%2Fperformance%2FScreenshot%202023-11-25%20at%2014.01.53.png)

As you can see we have move from 338ms to 137ms in that request. Also we have reduce the number of spans from 39 to 33.

## Failure investigation

## Service map

![Screenshot 2023-11-25 at 13.58.19.png](img%2Fdebug%2Fservice-map%2FScreenshot%202023-11-25%20at%2013.58.19.png)