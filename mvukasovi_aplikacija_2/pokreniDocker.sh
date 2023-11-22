#!/bin/bash

echo "Zaustavi"
docker stop mvukasovi_payara_micro
echo "Obrisi"
docker rm mvukasovi_payara_micro
echo "Pripremi"
./scripts/pripremiSliku.sh
echo "Pokreni"
./scripts/pokreniSliku.sh
