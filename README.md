# Structory Free

Repository pubblico della versione gratuita di Structory.

## Moduli

- `structory-core`: API e implementazioni realmente condivise con Structory Premium.
- `structory-free`: plugin Bukkit/Paper gratuito e relative risorse.

## Dipendenze Dada

La build usa le versioni verificate sui rispettivi branch `main`:

- DadaGUIRework `2.8.0-SNAPSHOT`
- DadaPlatform `26.1`

Finché questi artefatti non vengono pubblicati in un registry Maven, devono essere presenti nel repository Maven locale.

## Build

```powershell
mvn clean install
```

Il JAR del plugin viene prodotto in `structory-free/target`.
