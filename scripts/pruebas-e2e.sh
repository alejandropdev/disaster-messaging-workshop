#!/usr/bin/env bash
# Uso: pruebas-e2e.sh [url-envio] [url-consulta] [url-suscriptores] [url-mailhog]
# Sin argumentos apunta a la variante tiers (todo en localhost:8080).
set -u
SEND="${1:-http://localhost:8080/api}"
QUERY="${2:-$SEND}"
SUBSCRIBERS="${3:-$QUERY}"
MAIL="${4:-http://localhost:8025}"
JSON='Content-Type: application/json'
ok=0; fail=0

check() {
  if [ "$2" = "$3" ]; then ok=$((ok+1)); echo "ok    $1"
  else fail=$((fail+1)); echo "FALLA $1 (esperado '$3', obtenido '$2')"; fi
}

wait_for() {
  for _ in $(seq 1 30); do
    [ "$(eval "$1")" = "$2" ] && return 0
    sleep 1
  done
  return 1
}

wait_for "curl -s -o /dev/null -w '%{http_code}' $QUERY/disasters" 200
wait_for "curl -s -o /dev/null -w '%{http_code}' $SUBSCRIBERS/subscribers" 200
curl -s -X DELETE "$MAIL/api/v1/messages" >/dev/null

code=$(curl -s -o /dev/null -w '%{http_code}' -X POST -H "$JSON" "$SUBSCRIBERS/subscribers" \
  -d '{"name":"Cruz Roja","channel":"EMAIL","contact":"cruzroja@example.org"}')
check "registrar suscriptor por correo" "$code" 201

code=$(curl -s -o /dev/null -w '%{http_code}' -X POST -H "$JSON" "$SUBSCRIBERS/subscribers" \
  -d '{"name":"Brigada Norte","channel":"SMS","contact":"+573001112233"}')
check "registrar suscriptor por SMS" "$code" 201

code=$(curl -s -o /dev/null -w '%{http_code}' -X POST -H "$JSON" "$SUBSCRIBERS/subscribers" \
  -d '{"name":"X","channel":"EMAIL","contact":"sin-arroba"}')
check "rechazar correo inválido" "$code" 400

code=$(curl -s -o /dev/null -w '%{http_code}' -X POST -H "$JSON" "$SEND/disasters" \
  -d '{"location":"Bogotá","severity":"ALTA"}')
check "rechazar desastre sin tipo" "$code" 400

resp=$(curl -s -w '\n%{http_code}' -X POST -H "$JSON" "$SEND/disasters" \
  -d '{"type":"Sismo","location":"Bogotá","severity":"ALTA","description":"Magnitud 6.1"}')
check "recibir desastre (202)" "$(echo "$resp" | tail -1)" 202
rid=$(echo "$resp" | head -1 | jq -r .reportId)

wait_for "curl -s $QUERY/disasters | jq -r --arg id $rid '[.[]|select(.reportId==\$id)]|length'" 1
check "desastre registrado" "$(curl -s "$QUERY/disasters" | jq -r --arg id "$rid" '[.[]|select(.reportId==$id)]|length')" 1

wait_for "curl -s $MAIL/api/v2/messages | jq .total" 1
check "un correo entregado" "$(curl -s "$MAIL/api/v2/messages" | jq .total)" 1
check "correo para el suscriptor" \
  "$(curl -s "$MAIL/api/v2/messages" | jq -r '.items[0].To[0] | .Mailbox + "@" + .Domain')" "cruzroja@example.org"

echo "$ok ok, $fail fallas"
[ "$fail" -eq 0 ]
