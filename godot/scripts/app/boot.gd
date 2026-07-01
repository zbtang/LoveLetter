extends Control

func _ready() -> void:
	LevelProgress.load_progress()
	await get_tree().create_timer(0.3).timeout
	SceneRouter.go_to_main_menu()
