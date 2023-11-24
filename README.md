# observability_demo
observability demo for AlmeriaJs talk

Three services:
- User
- Product
- cart


# Running

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

## User
- Build
```
cd user
./gradlew assemble
```

- Run user service
```
export OTEL_SERVICE_NAME=user-service
export OTEL_EXPORTER_OTLP_PROTOCOL=http/protobuf
export OTEL_EXPORTER_OTLP_ENDPOINT=http://localhost:55679
export OTEL_TRACES_EXPORTER=otlp
export OTEL_METRICS_EXPORTER=otlp
export OTEL_LOGS_EXPORTER=otlp
java -javaagent:..//opentelemetry-javaagent.jar -jar ./build/libs/user-0.0.1-SNAPSHOT.jar
```