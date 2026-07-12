# Structory Free

Public repository for the free version of Structory.

## Modules

* `structory-core`: API, type registry, time utilities, and schedulers shared with Structory Premium.
* `structory-free`: free Bukkit/Paper plugin and its related resources.

## Dada Dependencies

The build uses the versions verified on their respective `main` branches:

* DadaGUIRework `2.8.0-SNAPSHOT`
* DadaPlatform `26.1`

Until these artifacts are published to a Maven repository, they must be available in the local Maven repository.

## Build and Testing

```powershell
mvn clean verify
mvn clean install
```

`verify` runs the core and plugin tests. `install` publishes `structory-core` to the local Maven repository, making it available to Structory Premium. The Free version JAR is generated in `structory-free/target`.
