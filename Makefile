NAME = gaeme
COMPOSE = docker-compose
OS=window
# os = window, unix

ifeq ($(OS),window)
	BUILD = gradlew
else
	BUILD = ./gradlew
endif


all: $(NAME)

$(NAME):
	git pull origin dev
	git submodule init
	git submodule update
	$(BUILD) build -x test
	$(BUILD) asciidoctor
	$(COMPOSE) up -d

shell_db:
	$(COMPOSE) exec db /bin/bash

shell_server:
	$(COMPOSE) exec server /bin/bash

clean:
	$(COMPOSE) down --rmi local

restdoc:
	$(BUILD) asciidoctor

re: clean all