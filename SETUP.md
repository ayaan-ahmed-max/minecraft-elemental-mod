# Setup & How to Play

Everything you need to install **Elemental Powers** and start throwing fireballs.

---

## 1. Install the mod

### What you need
- Minecraft: Java Edition **26.2**
- Forge **65.0.3** (the mod works on any Forge 65.x for 26.2)
- The mod jar: `elementalpowers-1.0.0.jar` (from the GitHub Releases page, or `build/libs/` if you built it yourself)

### Option A — CurseForge launcher (easiest)
1. Open the CurseForge app → Minecraft → **Create Profile**.
2. Set **Minecraft Version** to `26.2`, **Modloader** to `Forge`, version `forge-65.0.3`, then click **Create**.
3. Right-click the new profile → **Open Folder**.
4. Drop `elementalpowers-1.0.0.jar` into the `mods` folder (create it if it doesn't exist).
5. Hit **Play**.

### Option B — Vanilla launcher
1. Download the Forge 26.2-65.0.3 **Installer** from [files.minecraftforge.net](https://files.minecraftforge.net/net/minecraftforge/forge/index_26.2.html) and run it (choose "Install client").
2. Put the mod jar in `~/Library/Application Support/minecraft/mods` (macOS) or `%APPDATA%\.minecraft\mods` (Windows).
3. Launch Minecraft with the **forge** profile.

### Verify it worked
On the title screen click **Mods** — you should see **Elemental Powers** in the list. In-game, the creative inventory has an "Elemental Powers" tab with all five orbs.

### Building from source (optional)
```sh
git clone <your-repo-url>
cd minecraft-elemental-mod
./gradlew build          # needs JDK 25; jar appears in build/libs/
```

---

## 2. Craft your orbs

Every basic orb is a **cross pattern**: 4 essence items around a core, in a crafting table.

```
 . B .        B = blaze powder
 B C B        C = fire charge
 . B .
```

| Orb | Around (×4) | Center | Where to get it |
|---|---|---|---|
| 🔥 **Orb of Fire** | Blaze Powder | Fire Charge | Blazes in nether fortresses |
| 🌊 **Orb of Tides** | Prismarine Shard | Prismarine Crystals | Guardians at ocean monuments |
| ⛰️ **Orb of Stone** | Cobbled Deepslate | Emerald | Deep mining + villager trades |
| ⚡ **Orb of Storms** | Copper Ingot | Lightning Rod | Copper ore (3 ingots = 1 rod) |

Then combine all four around a diamond for the endgame orb:

```
 . F .        F/W/L/E = the four orbs
 W D L        D = diamond
 . E .
```

🌈 **Primal Orb** — fire top, water left, lightning right, earth bottom.

**Suggested progression:** Storms first (copper is everywhere) → Stone → Fire (nether trip) → Tides (ocean monument raid) → Primal.

---

## 3. Use your powers

Hold an orb and **right-click**. The icon greys out while it recharges, just like an ender pearl.

| Orb | Power | What happens | Recharge |
|---|---|---|---|
| 🔥 Fire | **Flame Burst** | Fan of 3 fireballs + you're fireproof for 10s | 5s |
| 🌊 Tides | **Tidal Grace** | Swim like a dolphin 30s, breathe underwater 60s, regen, and douse all fire near you | 6s |
| ⛰️ Stone | **Seismic Slam** | Shockwave knocks back and damages every hostile within 4 blocks + Resistance II for 8s | 8s |
| ⚡ Storms | **Storm Strike** | Lightning hits whatever you're looking at (up to 32 blocks) + Speed II burst | 7s |
| 🌈 Primal | **Elemental Chorus** | Every buff at once for 30s, a bigger slam, and a bolt on the nearest monster | 20s |

### Tips & combos
- **Storm Strike** aims where your crosshair points — snipe creepers before they reach your base (bonus: it makes charged creepers if one survives the hit... risky fun).
- **Seismic Slam** is a panic button: surrounded at night = slam, then run.
- **Tidal Grace** + an ocean monument is basically easy mode — dolphin speed, water breathing, and guardians can't burn you.
- **Flame Burst's** fire resistance means you can walk through the fires you just started.
- Orbs don't stack and never break — one of each is a full kit.

---

## 4. Tune it for your PC

The config file appears after first launch:
- CurseForge: `<profile folder>/config/elementalpowers-common.toml`
- Vanilla: `.minecraft/config/elementalpowers-common.toml`

The one that matters for FPS:

```toml
particleDensity = 0.35   # 0.0 = no ability particles at all, 1.0 = full show
```

Already at a conservative 35% by default. On a really weak machine set it to `0.0` — abilities work identically, just without sparkles. You can also tweak every cooldown, the slam radius, and lightning range in the same file.

The mod adds **zero background load** (nothing runs per-tick), so the usual low-end advice matters more than the mod: render distance 6–8, graphics "Fast", clouds off.

---

## 5. Troubleshooting

| Problem | Fix |
|---|---|
| Mod not in the Mods list | Jar is in the wrong `mods` folder, or the profile isn't Forge 26.2 — check the profile version matches `26.2` + `forge-65.0.3` |
| "Incompatible mod" / version error | The jar only accepts Minecraft 26.2.x — recreate the profile with the right version |
| Game won't launch at all | Remove other mods to rule out a conflict; this mod alone is known-good on a fresh 26.2 profile |
| Orb does nothing on right-click | It's on cooldown (icon greyed out) — wait for the sweep to finish |
| No particles when using abilities | `particleDensity` is set to `0.0` in the config |
