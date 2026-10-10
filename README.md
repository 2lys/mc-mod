# Brobyggerlogen (Fabric, Minecraft 26.3)

Rename any helmet to `brobyggerlogen` in an anvil -> you vanish from the
locator bar (like a carved pumpkin). Take it off / rename it -> visible again.

## Build (needs JDK 25 + Gradle 9.6.0)
    gradle build
Jar: build/libs/brobyggerlogen-1.1.0.jar

No PC setup? Push this folder to a GitHub repo; the included Actions workflow
builds the jar (Actions tab -> latest run -> Artifacts).

## Install on the server
Upload the jar AND Fabric API (26.3 build) to the server's mods folder.
Server-side only: players don't need the mod.
