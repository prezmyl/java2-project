#!/bin/bash
# Spouštěcí skript z kořenového adresáře projektu

java \
  --module-path project-game/target/project-game-1.0-SNAPSHOT.jar:project-game/target/libs \
  -m cz.vsb.fei.project.game/cz.vsb.fei.project.game.AppGame

