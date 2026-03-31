# Last Tick Jump

A Fabric client-side mod for Minecraft 1.20.1 that automatically jumps at the last tick before the player walks off an edge while sprinting. Designed for parkour to optimize edge jumps without manual timing.

## Features

- Automatically triggers a jump when the player is about to step off an edge while sprinting
- Preserves sprint momentum through the jump
- Toggle on/off with **J** (configurable)
- No effect on servers — purely client-side input

## Requirements

- Minecraft 1.20.1
- Fabric Loader 0.16.10+
- Fabric API 0.90.7+1.20.1

## Installation

1. Install [Fabric Loader](https://fabricmc.net/use/) for Minecraft 1.20.1
2. Download [Fabric API](https://modrinth.com/mod/fabric-api)
3. Place both JARs in your `.minecraft/mods` folder

## Building from source

```
./gradlew build
```

The output JAR will be at `build/libs/lasttickjump-1.0.0.jar`.

## License

MIT
