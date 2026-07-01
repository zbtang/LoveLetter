extends RefCounted
class_name InputController

var _input_port: InputPort
var _jump_requested: bool = false


func _init(input_port: InputPort = null) -> void:
	_input_port = input_port if input_port != null else InputFactory.create()


func notify_jump_request() -> void:
	_jump_requested = true


func apply_to_ball(ball: RigidBody2D, is_on_ground: bool) -> void:
	if ball == null:
		return
	var horizontal := _input_port.get_horizontal()
	if absf(horizontal) > 0.01:
		ball.linear_velocity.x = horizontal * GameConfig.VELOCITY_RATE
	var wants_jump := _jump_requested or _input_port.is_jump_just_pressed()
	_jump_requested = false
	if wants_jump and GameRules.can_jump(is_on_ground, GameState.cheat_fly_mode):
		ball.linear_velocity.y = GameConfig.JUMP_IMPULSE
		AudioManager.play_jump()
