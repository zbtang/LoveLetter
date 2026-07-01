extends Control

var _ocean_offset: float = 0.0
var _touch_times: int = 0
var _pre_touch_time: int = 0
var _dialog: Control = null
var _dialog_type: String = ""


@onready var _background: TextureRect = $Background
@onready var _title: TextureRect = $Title
@onready var _ocean_a: TextureRect = $OceanA
@onready var _ocean_b: TextureRect = $OceanB
@onready var _play_button: TextureButton = $PlayButton
@onready var _help_button: TextureButton = $HelpButton
@onready var _about_button: TextureButton = $AboutButton
@onready var _music_button: TextureButton = $MusicButton
@onready var _toast: Label = $Toast


func _ready() -> void:
	_apply_textures()
	_play_button.pressed.connect(_on_play_pressed)
	_help_button.pressed.connect(_on_help_pressed)
	_about_button.pressed.connect(_on_about_pressed)
	_music_button.pressed.connect(_on_music_pressed)
	_update_music_button()
	AudioManager.play_bgm()
	_toast.visible = false


func _apply_textures() -> void:
	_background.texture = load("res://assets/textures/background.png")
	_title.texture = load("res://assets/textures/title.png")
	var ocean := load("res://assets/textures/ocean.png") as Texture2D
	_ocean_a.texture = ocean
	_ocean_b.texture = ocean
	_play_button.texture_normal = load("res://assets/textures/button_play.png")
	_help_button.texture_normal = load("res://assets/textures/button_help.png")
	_about_button.texture_normal = load("res://assets/textures/about.png")
	_update_music_button()


func _process(_delta: float) -> void:
	_ocean_offset -= GameConfig.OCEAN_SCROLL_SPEED
	var width := 800.0
	if _ocean_a.texture:
		width = float(_ocean_a.texture.get_width())
	var wrap := fmod(_ocean_offset, width)
	if wrap > 0.0:
		wrap -= width
	_ocean_a.position.x = wrap
	_ocean_b.position.x = wrap + width - 1.0


func _unhandled_input(event: InputEvent) -> void:
	if _dialog != null and event is InputEventMouseButton and event.pressed:
		_close_dialog()
		return
	if event is InputEventMouseButton and event.pressed and event.button_index == MOUSE_BUTTON_LEFT:
		if _dialog != null:
			return
		var pos: Vector2 = event.position
		if _is_button_hit(_play_button, pos) or _is_button_hit(_help_button, pos) or _is_button_hit(_about_button, pos) or _is_button_hit(_music_button, pos):
			return
		_register_cheat_tap()


func _is_button_hit(button: TextureButton, pos: Vector2) -> bool:
	var rect := Rect2(button.position, button.texture_normal.get_size() if button.texture_normal else Vector2.ZERO)
	return rect.has_point(pos)


func _register_cheat_tap() -> void:
	var now := Time.get_ticks_msec()
	if _touch_times == 0:
		_pre_touch_time = now
		_touch_times = 1
	elif now - _pre_touch_time < 1000:
		_touch_times += 1
		_pre_touch_time = now
		if _touch_times > 10:
			GameState.activate_cheat_fly_mode()
			LevelProgress.cheat_fly_mode = true
			LevelProgress.save_progress()
			_show_toast("够啦，够啦。恭喜您已进入无敌飞天模式，但小心别飞出天外去啊～")
			_touch_times = 0
	else:
		_pre_touch_time = now
		_touch_times = 0


func _show_toast(text: String) -> void:
	_toast.text = text
	_toast.visible = true
	await get_tree().create_timer(6.0).timeout
	_toast.visible = false


func _on_play_pressed() -> void:
	SceneRouter.start_game(0)


func _on_help_pressed() -> void:
	_show_dialog("res://assets/textures/help_dia.png")


func _on_about_pressed() -> void:
	_show_dialog("res://assets/textures/about_dia.png")


func _on_music_pressed() -> void:
	AudioManager.set_music_enabled(not GameState.music_enabled)
	_update_music_button()


func _update_music_button() -> void:
	var path := "res://assets/textures/button_music_on.png" if GameState.music_enabled else "res://assets/textures/button_music_off.png"
	_music_button.texture_normal = load(path) as Texture2D


func _show_dialog(texture_path: String) -> void:
	_close_dialog()
	_dialog = TextureRect.new()
	_dialog.texture = load(texture_path) as Texture2D
	_dialog.position = Vector2(120, 40)
	add_child(_dialog)


func _close_dialog() -> void:
	if _dialog:
		_dialog.queue_free()
		_dialog = null
