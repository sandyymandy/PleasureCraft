# PleasureCraft

**PleasureCraft** is an adult-themed mod for **Minecraft 1.21.1+** built using the **Fabric Mod Loader**. It’s a full remake and modern reimagining of the old 1.12.2 **FapCraft** mod made by *Schnurri_tv*, rebuilt from the ground up to support current versions of Minecraft using modern tools and practices.

---

## Current Features

### Modular Girl Entities
- Lucy and Bia are currently implemented, with a modular system allowing more girls to be added easily.
- Girls are **tameable** with specific items and **only respond to their owner**.
- Shift right-clicking opens a custom **owner-only GUI** with:
    - Interaction buttons (`Sit`, `Follow`, `Talk`, `Strip/Dress`, etc.)
    - 4x3 **inventory** 
    - Option to set a **home position** as a respawn point.

### Scene System (WIP)
- Starting a **"Talk"** interaction triggers an **interactive sex scene system**:
    - Player **rides** the girl entity with custom positioning.
    - Player is **invisible** and placed at a **specific model bone** (e.g., `"hips"`).
    - Scene progress will eventually include **keybind input**, **progress bars**, and **transitions** (in development).

### Animation & Jiggle Physics
- Spring-damper-based **jiggle physics** implemented on selected bones like breasts and hair.
- Eventually Fully configurable using external `.json` files (in development).
- Jiggle responds to:
    - Movement inertia
    - Rotation changes
    - Bone-specific parameters (stiffness, damping, mass)

---

[//]: # (## Experimental / In Progress)

[//]: # ()
[//]: # (- Scene advancement system with player-controlled **progress bar**, **GUI triggers**, and **camera focus on bones**.)

[//]: # (- Config-driven rendering and animation setups.)

[//]: # (- Armor/equipment support with visible changes on the model.)

[//]: # (- Player skin overlay support on the girl models.)

[//]: # ()
[//]: # (---)

## Roadmap

### In Progress
- [x] Multiple girl entities with modular code
- [x] GUI with custom inventory and buttons
- [x] Strip/Dress-up toggle with model bone visibility
- [x] Freeze/Unfreeze system (prevents movement during scenes)
- [x] Custom passenger position via model bone
- [x] Configurable jiggle physics system

### Planned
- [ ] Full scene interaction system with progress and climax stages
- [ ] Scene camera that follows animation bone
- [ ] Keybinds for advancing and exiting scenes
- [ ] Player model integration with girl's scene pose
- [ ] Armor and equipment rendering
- [ ] Better AI (pathfinding, behavior trees, etc.)
- [ ] JSON-driven entity definition (animations, stats, behavior)

---

## Requirements

- [Fabric Loader](https://fabricmc.net/)
- [Fabric API](https://modrinth.com/mod/fabric-api)
- [GeckoLib 4.5.7](https://modrinth.com/mod/geckolib)
- Minecraft **1.21.1**

---

## Disclaimer

This mod contains **adult content** and is intended for **mature audiences only**.  
Use responsibly and respect Minecraft’s community guidelines, platform policies, and age requirements.

> PleasureCraft is **heavily inspired** by SexCraft+ (by VyP3X) and FapCraft (by Schnurri_tv), but is an **independent project** with original implementation and goals.
