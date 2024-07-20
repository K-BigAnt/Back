NAME = gaeme
COMPOSE = docker-compose

all: $(NAME)

$(NAME):
	git pull origin dev
	./gradlew build
	$(COMPOSE) up -d

shell_db:
	$(COMPOSE) exec db /bin/bash

shell_server:
	$(COMPOSE) exec server /bin/bash

clean:
	$(COMPOSE) down
	docker rmi gaeme:0.0.0


re: clean all