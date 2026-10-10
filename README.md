# No Lunge Hunger

Fabric mod for Minecraft **1.21.11**. Using the spear's **Lunge** enchantment no longer costs hunger or saturation. Everything else (sprinting, jumping, regeneration, and Lunge's own dash and minimum food requirement) stays vanilla.

Needs Fabric Loader and Fabric API. Works on both client and server (drop the jar in `mods`).

## Toggle

Edit `config/nolungehunger.json` (created on first launch):

```json
{
  "enabled": true
}
```

Set `"enabled": false` to get vanilla Lunge hunger back. The file is re-checked about once a second, so no restart is needed. On a server the setting is the server's config.

## Build the jar with GitHub

1. Upload all files from this folder to a GitHub repository.
2. Add the workflow file separately at `.github/workflows/build.yml`.
3. Open the **Actions** tab and run "Build mod jar" (it also runs on every push).
4. Download the **NoLungeHunger-jar** artifact: it contains `nolungehunger-1.0.0.jar`.

## Build locally

```
./gradlew build
```

The jar is written to `build/libs/`.

## How it works

In 1.21.11 Lunge pays for its dash with the `minecraft:apply_exhaustion` enchantment effect. A small mixin skips that effect while the mod is enabled, so no exhaustion is added and hunger and saturation stay untouched.
