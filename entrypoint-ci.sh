#!/bin/bash
set -e

echo "=== Iniciando Socat Proxies ==="
socat TCP-LISTEN:3333,fork,reuseaddr TCP:app:3333 &
socat TCP-LISTEN:3000,fork,reuseaddr TCP:web:3000 &
socat TCP-LISTEN:5432,fork,reuseaddr TCP:database:5432 &

echo "=== Aguardando API (http://app:3333/health) ==="
until curl -s http://app:3333/health; do
  echo "Aguardando API..."
  sleep 2
done

echo "=== Aguardando Frontend (http://web:3000/) ==="
until curl -s http://web:3000/ | grep -q 'Zombie+'; do
  echo "Aguardando UI..."
  sleep 2
done

echo "=== Limpando UTF-8 BOM de arquivos Java ==="
find . -name "*.java" -exec sed -i '1s/^\xEF\xBB\xBF//' {} + 2>/dev/null || true

echo "=== Iniciando Xvfb Virtual Display (1920x1080x24) ==="
export DISPLAY=:99
Xvfb :99 -screen 0 1920x1080x24 -ac +extension GLX +render -noreset &
XVFB_PID=$!
sleep 1

echo "=== Servicos prontos! Executando testes ==="
chmod +x ./gradlew

set +e
./gradlew test --no-daemon --info --stacktrace
TEST_STATUS=$?
echo "=== Gradle test finalizado com status: $TEST_STATUS ==="

echo "=== Gerando relatorio Allure ==="
./gradlew allureReport --no-daemon --info || true

kill $XVFB_PID 2>/dev/null || true

echo "=== Finalizado com exit code: $TEST_STATUS ==="
exit $TEST_STATUS
