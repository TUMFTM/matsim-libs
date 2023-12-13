FROM maven:3.8.6-openjdk-18-slim AS build
ARG APP_DIR=/opt/matsim
WORKDIR ${APP_DIR}

RUN apt-get update && apt-get install -y \
    figlet build-essential\
    && rm -rf /var/lib/apt/lists/*

ARG MATSIM_PROJECT=matsim
ARG MATSIM_CONTRIBS=

COPY ./matsim ./matsim
COPY ./benchmark ./benchmark
COPY ./* ./
RUN make docker-build-base

COPY ./projects ./projects
COPY ./contribs ./contribs
COPY ./examples ./examples
RUN make docker-build-project CONTRIBS="$MATSIM_CONTRIBS" PROJECT="$MATSIM_PROJECT" \
    && echo "$(mvn -q help:evaluate -Dexpression=project.version -DforceStdout=true)" > VERSION.txt \
    && figlet -f slant "MATSim $(cat VERSION.txt)" >> BANNER.txt \
    && echo "Image build date: $(date --iso-8601=seconds)" >> BANNER.txt


FROM openjdk:18-slim
ARG APP_DIR=/opt/matsim
WORKDIR ${APP_DIR}
USER root

ARG MATSIM_PROJECT=matsim
ARG MATSIM_CONTRIBS=

COPY ./docker-entrypoint.sh ./docker-entrypoint.sh 
COPY --from=build ${APP_DIR}/*.txt ./resources/
COPY --from=build ${APP_DIR}/target/matsim-bundle.jar ./matsim.jar
COPY --from=build ${APP_DIR}/projects ./projects

RUN chmod +x ./matsim.jar && chmod +x ./docker-entrypoint.sh
ENV MATSIM_HOME="${APP_DIR}" \
    MATSIM_INPUT="${APP_DIR}/data/input" \
    MATSIM_OUTPUT="${APP_DIR}/data/output"

ARG COMMIT
ENV COMMIT ${COMMIT}
RUN apt-get update && apt-get install -y \
    libfreetype6 \
    libfontconfig1 \
    && rm -rf /var/lib/apt/lists/* \
    && mkdir -p ${MATSIM_INPUT} \
    && mkdir -p ${MATSIM_OUTPUT}
VOLUME ${APP_DIR}/data

ENV MATSIM_PROJECT="${MATSIM_PROJECT}" \
    MATSIM_CONTRIBS="${MATSIM_CONTRIBS}" \
    APP_DIR="${APP_DIR}" \
    INIRAM='-Xms8g' \
    MAXRAM='-Xmx8g'

ENTRYPOINT ["./docker-entrypoint.sh"]
