# Variante servicios

Dos servicios con responsabilidades separadas, desplegados como WAR independientes en el mismo contenedor WildFly. No comparten código ni base de datos. Se hablan solo por mensajes.

```mermaid
flowchart LR
    C[Cliente] -->|POST /registry/api/disasters| RS
    C -->|POST /notification/api/subscribers| NS

    subgraph WF [Contenedor WildFly]
        subgraph registry [registry.war]
            RS[REST + intake]
            RL[MDB DisasterListener]
            RG[DisasterRegistry]
        end
        subgraph notification [notification.war]
            NS[REST suscriptores]
            NL[MDB DisasterEventListener]
            NO[NotificationService]
        end
        Q[(Cola DisasterReceived)]
        T[(Topic DisasterRegistered)]
        RDB[(RegistryDS)]
        NDB[(NotificationDS)]
    end

    RS --> Q --> RL --> RG
    RG --> RDB
    RG --> T --> NL --> NO
    NS --> NDB
    NO --> NDB
    NO --> MH[MailHog]
```

## Contrato entre servicios

El topic `DisasterRegistered` transporta un JSON con `reportId`, `type`, `location`, `severity` y `description`. Cada servicio define su propia clase para ese JSON. Si registry agrega campos, notification los ignora.

## Mensajería

- Cola `DisasterReceived`: un solo consumidor (el registro).
- Topic `DisasterRegistered`: el MDB de notification usa suscripción durable. Los eventos publicados mientras el servicio está caído se entregan al volver.
