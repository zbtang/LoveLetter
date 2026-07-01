extends Node2D

var _ball: RigidBody2D
var _trigger: Area2D
var _logo: Sprite2D
var _credits: Sprite2D
var _config: Dictionary
var _alpha_step: int = 0
var _credits_y: float = 0.0
var _activated: bool = false
var is_end: bool = false


func setup(ball: RigidBody2D, config: Dictionary, trigger: Area2D) -> void:
	_ball = ball
	_config = config
	_trigger = trigger
	_trigger.body_entered.connect(_on_trigger_entered)

	_logo = Sprite2D.new()
	_logo.texture = load("res://assets/textures/logo.png") as Texture2D
	var logo_pos: Array = config.get("logo_position", [253, 135])
	_logo.position = Vector2(float(logo_pos[0]), float(logo_pos[1]))
	_logo.modulate.a = 0.0
	add_child(_logo)

	_credits = Sprite2D.new()
	_credits.texture = load("res://assets/textures/the_end.png") as Texture2D
	_credits.position = Vector2(0, GameConfig.DESIGN_HEIGHT)
	_credits.visible = false
	add_child(_credits)


func _on_trigger_entered(body: Node2D) -> void:
	if _activated or body != _ball:
		return
	_activated = true
	GameState.hust_unique_active = true
	_ball.gravity_scale = 0.0
	_ball.linear_velocity = Vector2.ZERO


func _process(_delta: float) -> void:
	if not _activated:
		return
	_alpha_step += 1
	if _alpha_step < 255:
		_logo.modulate.a = float(_alpha_step) / 255.0
	elif _alpha_step == 255:
		var reset: Array = _config.get("ball_reset", [397, 252])
		_ball.global_position = Vector2(float(reset[0]), float(reset[1]))
		_ball.linear_velocity = Vector2.ZERO
	elif _alpha_step == 333:
		is_end = true
		_credits.visible = true
		_credits_y = float(GameConfig.DESIGN_HEIGHT) * 5.0 / 6.0
		_credits.position.y = _credits_y
	elif _alpha_step > 333 and _credits_y > GameConfig.DESIGN_HEIGHT * 0.25:
		_credits_y -= 1.0
		_credits.position.y = _credits_y
