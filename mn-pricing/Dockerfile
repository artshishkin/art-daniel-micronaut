FROM ghcr.io/graalvm/graalvm-ce:java11-21.1.0 AS graalvm
RUN gu install native-image

COPY . /home/app/mn-pricing
WORKDIR /home/app/mn-pricing

RUN native-image --no-server -cp build/libs/mn-pricing-*-all.jar

FROM frolvlad/alpine-glibc:alpine-3.12
RUN apk update && apk add libstdc++
# No rest api, so no port to expose
# EXPOSE 8080
COPY --from=graalvm /home/app/mn-pricing/mn-pricing /app/mn-pricing
ENTRYPOINT ["/app/mn-pricing"]