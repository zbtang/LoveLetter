extends Node

var current_level_index: int = 0
var cheat_fly_mode: bool = false
var music_enabled: bool = true
var is_playing: bool = false
var hust_unique_active: bool = false


func reset_run() -> void:
	current_level_index = 0
	hust_unique_active = false
	is_playing = false


func activate_cheat_fly_mode() -> void:
	cheat_fly_mode = true
	EventBus.cheat_activated.emit()
