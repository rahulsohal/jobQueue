    graph TD
        A[User] --> B(Web Application);
        B --> C{API Gateway};
        C --> D[Microservice 1];
        C --> E[Microservice 2];
        D --> F[Database];
        E --> F;
        B --> G[Load Balancer];
        G --> H[Server Instance 1];
        G --> I[Server Instance 2];
