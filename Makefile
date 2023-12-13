# NOTE: This is NOT a makefile to compile matsim.  Instead, it performs
# certain maintenance helper tasks.
QUICK=-Dmaven.test.skip -Dmaven.javadoc.skip -Dsource.skip -Dassembly.skipAssembly=true -DskipTests --offline
#### Docker Settings ####
ROOT_DIR:=$(shell dirname $(realpath $(firstword $(MAKEFILE_LIST))))
DOCKER_ARGS:=-DskipTests=true -T1C
#### Customize via environments, else defaults below
CONTRIBS?= 
PROJECT?=matsim
#PROJECT:=project
####!Docker Settings ####

.PHONY: hs hybridsim

hs: hybridsim

hybridsim:
	cd matsim ; mvn clean install -DskipTests
	cd contribs/protobuf  ; mvn clean eclipse:clean eclipse:eclipse install
	cd contribs/hybridsim ; mvn clean eclipse:clean eclipse:eclipse install

release:
	mvn clean ; cd matsim ; mvn -f ~/git/matsim/pom.xml --projects playgrounds/kairuns/ --also-make install -DskipTests
	cd playgrounds/kairuns ; mvn clean ; mvn -Prelease -DskipTests=true

matsim-quick:
	cd matsim ; mvn clean install ${QUICK}

quick:
	mvn clean install ${QUICK}

docker-build-base:
	cd matsim ; mvn clean ; mvn install ${DOCKER_ARGS}
####!Docker-Build-Base######

docker-build-project:
	echo "Project: ${PROJECT}\nContribs: ${CONTRIBS}"
####Contribs######
	@for i in $(CONTRIBS); do \
		cd "${ROOT_DIR}/contribs/$$i" ; mvn clean ; mvn install --fail-at-end ${DOCKER_ARGS}; \
	done
####Compile Project######
	mvn -f pom.xml clean package -P docker --projects projects/${PROJECT} ${DOCKER_ARGS} -am -nsu
	echo "Project: ${PROJECT}\nContribs: ${CONTRIBS}" > BANNER.txt
####!Docker-Build-Project######

docker-build: docker-build-base docker-build-project