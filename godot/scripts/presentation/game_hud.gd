extends CanvasLayer

var _overlay: ColorRect
var _panel: TextureRect
var _quit_button: TextureButton
var _retry_button: TextureButton


func _ready() -> void:
	_build_ui()
	hide_death_dialog()


func _build_ui() -> void:
	_overlay = ColorRect.new()
	_overlay.color = Color(0, 0, 0, 0.4)
	_overlay.set_anchors_preset(Control.PRESET_FULL_RECT)
	add_child(_overlay)

	_panel = TextureRect.new()
	_panel.texture = load("res://assets/textures/dead_bg.png") as Texture2D
	_panel.position = Vector2(105, 60)
	add_child(_panel)

	_quit_button = _make_button("res://assets/textures/btn_quit.png", Vector2(193, 317), _on_quit_pressed)
	_retry_button = _make_button("res://assets/textures/btn_retry.png", Vector2(393, 317), _on_retry_pressed)
	add_child(_quit_button)
	add_child(_retry_button)


func _make_button(texture_path: String, pos: Vector2, callback: Callable) -> TextureButton:
	var button := TextureButton.new()
	button.texture_normal = load(texture_path) as Texture2D
	button.position = pos
	button.pressed.connect(callback)
	return button


func show_death_dialog() -> void:
	_overlay.visible = true
	_panel.visible = true
	_quit_button.visible = true
	_retry_button.visible = true


func hide_death_dialog() -> void:
	_overlay.visible = false
	_panel.visible = false
	_quit_button.visible = false
	_retry_button.visible = false


func _on_quit_pressed() -> void:
	get_parent().quit_to_menu()


func _on_retry_pressed() -> void:
	hide_death_dialog()
	get_parent().retry_level()
