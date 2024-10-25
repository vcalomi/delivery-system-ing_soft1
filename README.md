# Run using docker compose

1. Create an .env this structure:

    ```dotenv
    POSTGRES_USERNAME={username}
    POSTGRES_PASSWORD={password}
    POSTGRES_DOCKER_URL=jdbc:postgresql://database:{port}/{database}
    POSTGRES_LOCAL_URL=jdbc:postgresql://localhost:{port}/{database}
    POSTGRES_DB={database}
    ```
    Note:

    Default values -> username=postgres, port:5432

2. Then run `docker-compose up --build`
