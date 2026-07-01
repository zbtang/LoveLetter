extends Node

enum State { LOADING, PLAYING, DEAD, COMPLETING }

var _state: State = State.LOADING
var _metadata: LevelMetadata
var _built: Dictionary = {}
var _input_controller := InputController.new()
var _touch_jump_pending: bool = false
var _ending_triggered: bool = false


@onready var _world_container: Node2D = $WorldContainer
@onready var _hud: CanvasLayer = $HUD


func _ready() -> void:
	EventBus.player_died.connect(_on_player_died)
	EventBus.level_completed.connect(_on_level_completed)
	_load_level(GameState.current_level_index)
	AudioManager.play_bgm()


func _load_level(index: int) -> void:
	_state = State.LOADING
	GameState.hust_unique_active = false
	_clear_world()
	_metadata = LevelLoader.load_level(index)
	if _metadata == null:
		push_error("Failed to load level %d" % index)
		SceneRouter.go_to_main_menu()
		return
	_built = LevelBuilder.build(_world_container, _metadata)
	_connect_triggers()
	_state = State.PLAYING
	EventBus.level_started.emit(index)


func _clear_world() -> void:
	for child in _world_container.get_children():
		child.queue_free()
	_built.clear()


func _connect_triggers() -> void:
	var dead_line: Area2D = _built.get("dead_line")
	if dead_line:
		dead_line.body_entered.connect(_on_dead_line_entered)
	var next_portal: Area2D = _built.get("next_portal")
	if next_portal:
		next_portal.body_entered.connect(_on_next_portal_entered)


func _physics_process(_delta: float) -> void:
	if _state != State.PLAYING:
		return
	var ball: RigidBody2D = _built.get("ball")
	if ball == null:
		return
	_check_ending()
	if GameState.hust_unique_active:
		return
	var on_ground: bool = ball.is_on_ground() if ball.has_method("is_on_ground") else false
	if _touch_jump_pending:
		_input_controller.notify_jump_request()
		_touch_jump_pending = false
	_input_controller.apply_to_ball(ball, on_ground)


func _check_ending() -> void:
	if _ending_triggered or GameState.current_level_index != 27:
		return
	var special: Node = _built.get("special")
	if special == null or not ("is_end" in special) or not special.is_end:
		return
	_ending_triggered = true
	LevelProgress.mark_level_completed(27)
	get_tree().create_timer(4.0).timeout.connect(func() -> void: SceneRouter.go_to_main_menu())


func _unhandled_input(event: InputEvent) -> void:
	if _state != State.PLAYING:
		return
	if event is InputEventScreenTouch and event.pressed:
		_touch_jump_pending = true
	elif event is InputEventMouseButton and event.pressed and event.button_index == MOUSE_BUTTON_LEFT:
		_touch_jump_pending = true


func _on_dead_line_entered(body: Node2D) -> void:
	if _state != State.PLAYING:
		return
	if body != _built.get("ball"):
		return
	_state = State.DEAD
	EventBus.player_died.emit()


func _on_next_portal_entered(body: Node2D) -> void:
	if _state != State.PLAYING:
		return
	if body != _built.get("ball"):
		return
	if not GameRules.should_complete_level(GameState.hust_unique_active):
		return
	if not body.is_on_ground() and not GameState.cheat_fly_mode:
		return
	_state = State.COMPLETING
	EventBus.level_completed.emit()


func _on_player_died() -> void:
	_state = State.DEAD
	var ball: RigidBody2D = _built.get("ball")
	if ball:
		ball.linear_velocity = Vector2.ZERO
		ball.set_sleeping(true)
	if _hud.has_method("show_death_dialog"):
		_hud.show_death_dialog()


func _on_level_completed() -> void:
	LevelProgress.mark_level_completed(GameState.current_level_index)
	if GameState.current_level_index >= 26:
		if GameState.current_level_index == 26:
			SceneRouter.next_level()
		return
	SceneRouter.next_level()


func retry_level() -> void:
	SceneRouter.retry_level()


func quit_to_menu() -> void:
	SceneRouter.go_to_main_menu()
