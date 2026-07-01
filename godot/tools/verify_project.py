#!/usr/bin/env python3
"""Verify LoveLetter Godot project assets and level data."""

from __future__ import annotations

import json
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
LEVELS = ROOT / "data" / "levels"
TEXTURES = ROOT / "assets" / "textures"


def main() -> int:
    errors: list[str] = []

    level_files = sorted(LEVELS.glob("level_*.json"))
    if len(level_files) != 28:
        errors.append(f"Expected 28 level files, found {len(level_files)}")

    for level_file in level_files:
        data = json.loads(level_file.read_text(encoding="utf-8"))
        bg = data.get("background", {}).get("texture", "")
        if bg.startswith("res://assets/textures/"):
            tex = TEXTURES / Path(bg).name
            if not tex.exists():
                errors.append(f"{level_file.name}: missing background {tex.name}")

    required_scripts = [
        "scripts/app/game_state.gd",
        "scripts/application/level_controller.gd",
        "scripts/infrastructure/level_builder.gd",
        "scenes/boot/Boot.tscn",
        "scenes/menu/MainMenu.tscn",
        "scenes/game/GameRoot.tscn",
    ]
    for rel in required_scripts:
        if not (ROOT / rel).exists():
            errors.append(f"Missing required file: {rel}")

    required_textures = ["ball.png", "ocean.png", "next.png", "background.png", "logo.png", "the_end.png"]
    for name in required_textures:
        if not (TEXTURES / name).exists():
            errors.append(f"Missing texture: {name}")

    if errors:
        print("Verification FAILED:")
        for err in errors:
            print(f"  - {err}")
        return 1

    print("Verification passed: 28 levels, core scenes and assets present.")
    return 0


if __name__ == "__main__":
    sys.exit(main())
