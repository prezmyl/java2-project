#!/bin/bash
# Spustit z rootu projektu!

# Sestavení cesty ke knihovnám
M2_REPO="$HOME/.m2/repository"
GAME_LIBS="project-game/target/libs"

MODULE_PATH="$GAME_LIBS"
MODULE_PATH+=":$M2_REPO/org/openjfx/javafx-controls/23/javafx-controls-23-linux.jar"
MODULE_PATH+=":$M2_REPO/org/openjfx/javafx-fxml/23/javafx-fxml-23-linux.jar"
MODULE_PATH+=":$M2_REPO/org/openjfx/javafx-base/23/javafx-base-23-linux.jar"
MODULE_PATH+=":$M2_REPO/org/openjfx/javafx-graphics/23/javafx-graphics-23-linux.jar"

# + Hibernate & JPA moduly (nezbytné)
MODULE_PATH+=":$M2_REPO/org/hibernate/orm/hibernate-core/6.6.11.Final/hibernate-core-6.6.11.Final.jar"
MODULE_PATH+=":$M2_REPO/jakarta/persistence/jakarta.persistence-api/3.2.0/jakarta.persistence-api-3.2.0.jar"
MODULE_PATH+=":$M2_REPO/jakarta/transaction/jakarta.transaction-api/2.0.1/jakarta.transaction-api-2.0.1.jar"
MODULE_PATH+=":$M2_REPO/jakarta/annotation/jakarta.annotation-api/2.1.1/jakarta.annotation-api-2.1.1.jar"

# + log4j (nebo cokoliv dalšího co používáš ručně)
MODULE_PATH+=":$M2_REPO/org/apache/logging/log4j/log4j-api/2.23.1/log4j-api-2.23.1.jar"
MODULE_PATH+=":$M2_REPO/org/apache/logging/log4j/log4j-core/2.23.1/log4j-core-2.23.1.jar"

# + compiled moduly projektu
MODULE_PATH+=":project-data/target/classes"
MODULE_PATH+=":project-storage/target/classes"
MODULE_PATH+=":project-game/target/classes"

# A spustíme
java \
  --module-path "$MODULE_PATH" \
  -m cz.vsb.fei.project.game/cz.vsb.fei.project.game.AppGame
