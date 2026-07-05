# Structory Free

Repository pubblico della versione gratuita di Structory.

## Moduli

- `structory-core`: API, registro dei tipi, utilità temporali e scheduler condivisi con Structory Premium.
- `structory-free`: plugin Bukkit/Paper gratuito e relative risorse.

## Dipendenze Dada

La build usa le versioni verificate sui rispettivi branch `main`:

- DadaGUIRework `2.8.0-SNAPSHOT`
- DadaPlatform `26.1`

Finché questi artefatti non vengono pubblicati in un registry Maven, devono essere presenti nel repository Maven locale.

## Build e test

```powershell
mvn clean verify
mvn clean install
```

`verify` esegue i test di core e plugin. `install` pubblica `structory-core` nel repository Maven locale, rendendolo disponibile alla Premium. Il JAR della Free viene prodotto in `structory-free/target`.
