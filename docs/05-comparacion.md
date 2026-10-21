# Comparación y decisiones

## Estilos

| Aspecto | Tiers | Servicios | Microservicios |
|---|---|---|---|
| Unidad de despliegue | Un EAR | Dos WAR en un contenedor | Tres contenedores |
| Acoplamiento | Código compartido por capas | Solo el contrato del mensaje | Solo el contrato del mensaje |
| Datos | Una base | Una base por servicio | Una base por servicio |
| Escalado | Todo junto | Todo junto (mismo contenedor) | Por servicio |
| Fallo de notificaciones | Afecta al mismo proceso | Se aísla por mensajería, pero comparten JVM | Se aísla por completo |
| Complejidad operativa | Baja | Baja | Alta (broker, orquestador) |
| Broker | Artemis embebido | Artemis embebido | RabbitMQ externo |

## Decisiones

**Asíncrono desde la entrada.** La API solo valida y publica. Un pico de reportes se acumula en la cola y no tumba el registro.

**JMS y MDB en Jakarta, RabbitMQ en microservicios.** En WildFly el broker ya viene integrado y el MDB es la forma natural de consumir. En microservicios el broker debe ser independiente de cualquier servidor de aplicaciones, y RabbitMQ es el más simple de operar. Kafka se descartó porque aquí no hace falta retener ni reprocesar historial.

**Idempotencia por `reportId`.** Es la forma más barata de tolerar entregas repetidas.

**Un fallo al notificar no revierte el registro.** El desastre queda guardado aunque un correo falle. El error se registra en el log y se sigue con el siguiente suscriptor.

**Correo real, SMS simulado.** JavaMail contra MailHog muestra el flujo completo sin cuentas externas. El SMS sería otra implementación de la misma interfaz.

**Sin seguridad ni persistencia duradera.** Las bases son H2 en memoria y la API no autentica. Se dejó fuera por alcance.
