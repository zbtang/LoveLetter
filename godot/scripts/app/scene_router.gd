extends Node

const BOOT := "res://scenes/boot/Boot.tscn"
const MAIN_MENU := "res://scenes/menu/MainMenu.tscn"
const GAME_ROOT := "res://scenes/game/GameRoot.tscn"


func go_to_boot() -> void:
	get_tree().change_scene_to_file(BOOT)


func go_to_main_menu() -> void:
	GameState.reset_run()
	get_tree().change_scene_to_file(MAIN_MENU)


func start_game(level_index: int = 0) -> void:
	GameState.current_level_index = level_index
	GameState.is_playing = true
	get_tree().change_scene_to_file(GAME_ROOT)


func retry_level() -> void:
	start_game(GameState.current_level_index)


func next_level() -> void:
	start_game(GameState.current_level_index + 1)
