# Variante tiers

Una sola aplicación (EAR) con tres capas que solo se llaman hacia abajo. Corre en un contenedor WildFly.

```mermaid
flowchart LR
    C[Cliente] --> W

    subgraph EAR [WildFly - disaster-management.ear]
        subgraph web [dr-web]
            W[REST: DisasterResource<br/>SubscriberResource]
        end
        subgraph biz [dr-business]
            I[DisasterIntakeBean]
            L[DisasterListener MDB]
            G[DisasterRegistryBean]
            S[SubscriberDirectoryBean]
            E[EmailNotifier]
            M[SmsNotifier]
        end
        subgraph data [dr-data]
            D[Repositorios JPA]
        end
        DM[dr-domain<br/>entidades, interfaces]
    end

    W --> I
    W --> G
    W --> S
    I -- JMS --> Q[(Cola DisasterReceived)]
    Q --> L
    L --> G
    G --> D
    S --> D
    G --> E
    G --> M
    D --> DB[(H2)]
    E --> MH[MailHog]
```

## Módulos

- `dr-domain`: entidades, mensaje, interfaces de servicios y repositorios. Lo usan todas las capas.
- `dr-data`: repositorios JPA. No conoce la capa de negocio.
- `dr-business`: EJB de intake, registro y suscriptores, el MDB y los notificadores.
- `dr-web`: recursos REST. Solo hablan con las interfaces del dominio.
- `dr-ear`: empaqueta todo. Las librerías compartidas van en `lib/`.

## Mensajería

La cola `DisasterReceived` vive en el Artemis embebido de WildFly (`standalone-full.xml`) y se crea en `docker/wildfly/configurar.cli`. El MDB procesa cada mensaje dentro de una transacción. Si el registro falla, el mensaje se reentrega.
