name: build
on:
  push:
  workflow_dispatch:
jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version: 17
      - uses: gradle/actions/setup-gradle@v3
        with:
          gradle-version: 8.5
      - run: gradle build
      - name: Проверка, что в моде есть все нужные файлы
        run: |
          JAR=build/libs/arcane-1.2.0.jar
          for f in \
            assets/arcane/textures/entity/sair_costume.png \
            assets/arcane/textures/entity/eiro_overlay.png \
            assets/arcane/textures/item/eiro_heart.png \
            assets/arcane/textures/item/arcane_shard.png \
            assets/arcane/textures/item/arcane_crystal.png \
            assets/arcane/textures/item/sair_katana.png \
            assets/arcane/models/item/eiro_heart.json \
            assets/arcane/lang/ru_ru.json \
            arcane.mixins.json \
            com/arcane/Forms.class \
            com/arcane/mixin/PlayerEntityRendererMixin.class ; do
            if ! unzip -l "$JAR" | grep -q "$f"; then
              echo "::error::В МОДЕ НЕТ ФАЙЛА: $f (он не загрузился на GitHub)"
              exit 1
            fi
          done
          echo "Все файлы на месте"
      - uses: actions/upload-artifact@v4
        with:
          name: arcane-mod
          path: build/libs/arcane-1.2.0.jar
