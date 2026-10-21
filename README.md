# Taller 3: mensajería asíncrona en el sistema de gestión de desastres

Recepción asíncrona de reportes de desastre mediante mensajería. Cada reporte se registra y se notifica a los suscriptores por correo o SMS. El mismo dominio está implementado en tres estilos de arquitectura, todos con Docker.

| Variante | Estilo | Mensajería | Despliegue |
|---|---|---|---|
| `tiers/` | N capas (web, negocio, datos) en un EAR | JMS con MDB (Artemis de WildFly) | Contenedor Jakarta (WildFly) |
| `servicios/` | Dos servicios independientes (registry, notification) | JMS con cola y topic | Contenedor Jakarta (WildFly) |
| `microservicios/` | Tres microservicios (intake, registry, notification) | RabbitMQ | Docker Compose y Kubernetes |

El diseño está en [`docs/`](docs/README.md).

## Requisitos

Docker con Compose. Maven y Java se usan dentro de las imágenes, no hacen falta en la máquina. Para las pruebas e2e se necesita `curl` y `jq`.

## Ejecución

Cada variante se levanta con `docker compose up -d --build` desde su carpeta y se prueba con el mismo script. Los correos se ven en MailHog (http://localhost:8025) y el SMS queda en el log del servicio de notificación.

```bash
# tiers
cd tiers && docker compose up -d --build
../scripts/pruebas-e2e.sh

# servicios
cd servicios && docker compose up -d --build
../scripts/pruebas-e2e.sh http://localhost:8080/registry/api http://localhost:8080/registry/api http://localhost:8080/notification/api

# microservicios
cd microservicios && docker compose up -d --build
../scripts/pruebas-e2e.sh http://localhost:8081/api http://localhost:8082/api http://localhost:8083/api
```

El script recibe cuatro URLs opcionales: envío de desastres, consulta de desastres, suscriptores y MailHog.

### Kubernetes

Los manifiestos están en `microservicios/k8s/`. Con kind o minikube:

```bash
cd microservicios
for s in intake registry notification; do docker build -t dm-$s:latest $s; done
kind load docker-image dm-intake:latest dm-registry:latest dm-notification:latest
kubectl apply -f k8s/namespace.yaml -f k8s/infra.yaml -f k8s/apps.yaml
../scripts/pruebas-e2e.sh http://localhost:30081/api http://localhost:30082/api http://localhost:30083/api http://localhost:30025
```

Con kind hay que mapear los NodePort 30081, 30082, 30083 y 30025 al crear el clúster, o usar `kubectl port-forward`.

## API

| Método | Ruta | Descripción |
|---|---|---|
| POST | `/disasters` | Recibe un desastre (`type`, `location`, `severity` BAJA/MEDIA/ALTA, `description`). Responde 202 con `reportId`. |
| GET | `/disasters` | Lista los desastres registrados. |
| POST | `/subscribers` | Registra un suscriptor (`name`, `channel` EMAIL/SMS, `contact`). |
| GET | `/subscribers` | Lista los suscriptores. |

## Estado de las pruebas

Las tres variantes pasan `scripts/pruebas-e2e.sh` con Docker Compose. Los manifiestos de Kubernetes se validaron contra el esquema con kubeconform, pero no se desplegaron en un clúster.
