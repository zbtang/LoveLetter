#!/usr/bin/env python3
"""Export level data from legacy Constant.java into Godot JSON files."""

from __future__ import annotations

import json
import re
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[2]
CONSTANT_JAVA = ROOT.parent / "src" / "com" / "hustunique" / "utils" / "Constant.java"
OUTPUT_DIR = ROOT / "data" / "levels"

BG_NAMES = [
    "bg_level1", "bg_level2", "bg_level3", "bg_level4", "bg_level5", "bg_level6",
    "bg_level7", "bg_level8", "bg_level9", "bg_level10", "bg_level11", "bg_level12",
    "background", "bg_level14", "bg_level15", "bg_level16", "bg_level17", "bg_level18",
    "bg_level19", "bg_level20", "bg_level21", "bg_level22", "bg_level23", "bg_level24",
    "bg_level25", "bg_level26", "bg_level27", "bg_level28",
]

SPECIAL_BY_INDEX = {
    5: "car",
    6: "gear",
    8: "heads",
    12: "bugs",
    21: "spring_gate",
    27: "hust_unique",
}


def strip_comments(text: str) -> str:
    text = re.sub(r"//.*", "", text)
    text = re.sub(r"/\*.*?\*/", "", text, flags=re.DOTALL)
    return text


def extract_array_block(source: str, name: str) -> str:
    pattern = rf"{name}\s*=\s*\{{"
    match = re.search(pattern, source)
    if not match:
        raise ValueError(f"Array {name} not found")
    start = match.end() - 1
    depth = 0
    for index in range(start, len(source)):
        char = source[index]
        if char == "{":
            depth += 1
        elif char == "}":
            depth -= 1
            if depth == 0:
                return source[start : index + 1]
    raise ValueError(f"Unterminated array {name}")


def java_array_to_python(block: str) -> str:
    converted = block.replace("{", "[").replace("}", "]")
    converted = re.sub(r",\s*]", "]", converted)
    converted = re.sub(r",\s*\)", ")", converted)
    return converted


def parse_array(name: str, source: str):
    block = extract_array_block(source, name)
    pythonish = java_array_to_python(block)
    return eval(pythonish, {"__builtins__": {}})


def build_special(index: int, source: str, parsed: dict) -> dict:
    special_type = SPECIAL_BY_INDEX.get(index, "none")
    if special_type == "none":
        return {"type": "none"}

    if special_type == "car":
        return {
            "type": "car",
            "polygons": parsed["CAR_POSITION_IN_6"],
            "rope": parsed["ROPE_POSITION"],
            "sprite_offset": parsed["PICS_INIT_POSITION"][19],
            "speed": parsed["CAR_SPEED"],
            "left_limit": parsed["CAR_LEFT_LIMIT"],
            "right_limit": parsed["CAR_RIGHT_LIMIT"],
        }

    if special_type == "gear":
        return {
            "type": "gear",
            "big_polygons": parsed["BIG_CHILUN_POSI"],
            "small_polygons": parsed["SMALL_CHILUN_POSI"],
            "big_center": parsed["PICS_INIT_POSITION"][21],
            "small_center": parsed["PICS_INIT_POSITION"][22],
            "big_radius": parsed["BIG_CHILUN_RADIUS"],
            "small_radius": parsed["SMALL_CHILUN_RADIUS"],
            "big_motor_speed": -1.047,
            "small_motor_speed": 1.047,
        }

    if special_type == "heads":
        return {
            "type": "heads",
            "circles": [
                [61, 164, 46],
                [229, 164, 46],
                [396, 159, 46],
                [562, 159, 46],
            ],
        }

    if special_type == "bugs":
        return {
            "type": "bugs",
            "groups": parsed["BUGS_POSI"],
            "sprites": [
                {"texture": f"bug{i}", "position": parsed["BUGS_PICS_POSI"][i - 1]}
                for i in range(1, 6)
            ],
            "cycle_frames": parsed["BUG_TIME_SHOW"],
        }

    if special_type == "spring_gate":
        return {
            "type": "spring_gate",
            "gate_polygon": parsed["GATE_POSI"][0],
            "teleport_velocity_y": -25,
        }

    if special_type == "hust_unique":
        return {
            "type": "hust_unique",
            "polygons": parsed["HUST_UNIQUE"],
            "logo_position": parsed["HUST_PIC_POSI"],
            "ball_reset": parsed["BALL_POSI"],
            "logo_texture": "logo",
            "credits_texture": "the_end",
        }

    return {"type": "none"}


def export_levels() -> int:
    source = strip_comments(CONSTANT_JAVA.read_text(encoding="utf-8"))
    parsed = {
        "BODY_POINT": parse_array("BODY_POINT", source),
        "NEXT_POSITION": parse_array("NEXT_POSITION", source),
        "POSITION_AND_SPEED_OF_BALL": parse_array("POSITION_AND_SPEED_OF_BALL", source),
        "PICS_INIT_POSITION": parse_array("PICS_INIT_POSITION", source),
        "CAR_POSITION_IN_6": parse_array("CAR_POSITION_IN_6", source),
        "ROPE_POSITION": parse_array("ROPE_POSITION", source),
        "BIG_CHILUN_POSI": parse_array("BIG_CHILUN_POSI", source),
        "SMALL_CHILUN_POSI": parse_array("SMALL_CHILUN_POSI", source),
        "BUGS_POSI": parse_array("BUGS_POSI", source),
        "BUGS_PICS_POSI": parse_array("BUGS_PICS_POSI", source),
        "GATE_POSI": parse_array("GATE_POSI", source),
        "HUST_UNIQUE": parse_array("HUST_UNIQUE", source),
        "HUST_PIC_POSI": parse_array("HUST_PIC_POSI", source),
        "BALL_POSI": parse_array("BALL_POSI", source),
        "CAR_SPEED": 0.9375,
        "CAR_LEFT_LIMIT": 0.0625,
        "CAR_RIGHT_LIMIT": -5.7,
        "BIG_CHILUN_RADIUS": 43.4375,
        "SMALL_CHILUN_RADIUS": 30.625,
        "BUG_TIME_SHOW": 200,
    }

    OUTPUT_DIR.mkdir(parents=True, exist_ok=True)
    level_count = len(parsed["BODY_POINT"])

    for index in range(level_count):
        polygons = parsed["BODY_POINT"][index]
        terrain = {"polygons": [{"vertices": poly} for poly in polygons]} if polygons else {"polygons": []}
        has_next = index != 27
        level = {
            "id": index + 1,
            "index": index,
            "name": f"Level {index + 1}",
            "background": {
                "texture": f"res://assets/textures/{BG_NAMES[index]}.png",
                "offset": [0, 0],
            },
            "ball_spawn": list(parsed["POSITION_AND_SPEED_OF_BALL"][index]),
            "next_portal": None
            if not has_next
            else {
                "position": list(parsed["NEXT_POSITION"][index]),
                "half_size": [45, 27],
                "texture": "res://assets/textures/next.png",
            },
            "terrain": terrain if index != 12 else {"polygons": []},
            "world_border": {"left": 4, "right_offset_from_bg": 4},
            "special": build_special(index, source, parsed),
            "skip_letters": index == 12,
        }
        path = OUTPUT_DIR / f"level_{index + 1:03d}.json"
        path.write_text(json.dumps(level, indent=2), encoding="utf-8")

    print(f"Exported {level_count} levels to {OUTPUT_DIR}")
    return level_count


if __name__ == "__main__":
    try:
        count = export_levels()
        sys.exit(0 if count == 28 else 1)
    except Exception as exc:
        print(f"Export failed: {exc}", file=sys.stderr)
        sys.exit(1)
