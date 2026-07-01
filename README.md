# LoveLetter

A 2D physics puzzle game — roll the ball across letter-shaped terrain, reach the portal, and complete all 28 levels.

## Rewrite (Godot 4)

The original Android Java + JBox2D project (2013) has been rewritten as a cross-platform **Godot 4** game.

### Run locally

1. Install [Godot 4.3+](https://godotengine.org/download)
2. Open `godot/project.godot` in the Godot editor
3. Press **F5** to run

### Project layout

```
godot/
├── project.godot          # Engine entry
├── scenes/                # Boot, MainMenu, GameRoot
├── scripts/               # domain / application / infrastructure / presentation
├── data/levels/           # 28 JSON level files
├── assets/                # Textures and audio
└── tools/migrate/         # Legacy Constant.java exporter
```

### Regenerate level data

```bash
python3 godot/tools/migrate/export_levels.py
python3 godot/tools/verify_project.py
```

### Controls

| Platform | Move | Jump |
|----------|------|------|
| Desktop / Web | A/D or ←/→ | Space or click |
| Mobile | Accelerometer tilt | Tap |

**Easter egg**: tap the main menu background rapidly (>10 times within 1s) to enable fly mode.

### Export

Use Godot **Project → Export** to build for Android, iOS, Web, Windows, macOS, and Linux.

## Legacy Android project

The original source remains under:

- `src/` — Java source (Ant build, API 7)
- `bin/res/` — compiled assets

See `docs/godot-rewrite-design.md` for the full technical design.
