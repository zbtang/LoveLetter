extends Node

const SAVE_PATH := "user://save.json"

var max_level_unlocked: int = 0
var music_enabled: bool = true
var cheat_fly_mode: bool = false


func _ready() -> void:
	load_progress()


func load_progress() -> void:
	if not FileAccess.file_exists(SAVE_PATH):
		return
	var file := FileAccess.open(SAVE_PATH, FileAccess.READ)
	if file == null:
		return
	var parsed: Variant = JSON.parse_string(file.get_as_text())
	file.close()
	if typeof(parsed) != TYPE_DICTIONARY:
		return
	max_level_unlocked = int(parsed.get("max_level_unlocked", 0))
	music_enabled = bool(parsed.get("music_enabled", true))
	cheat_fly_mode = bool(parsed.get("cheat_fly_mode", false))
	GameState.music_enabled = music_enabled
	GameState.cheat_fly_mode = cheat_fly_mode


func save_progress() -> void:
	var data := {
		"version": 1,
		"max_level_unlocked": max_level_unlocked,
		"music_enabled": GameState.music_enabled,
		"cheat_fly_mode": GameState.cheat_fly_mode,
	}
	var file := FileAccess.open(SAVE_PATH, FileAccess.WRITE)
	if file == null:
		return
	file.store_string(JSON.stringify(data))
	file.close()


func mark_level_completed(index: int) -> void:
	if index >= max_level_unlocked:
		max_level_unlocked = index + 1
	save_progress()
