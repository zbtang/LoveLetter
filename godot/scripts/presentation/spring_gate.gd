extends Area2D

var _ball: RigidBody2D
var _impulse_y: float = -25.0


func setup(ball: RigidBody2D, impulse_y: float) -> void:
	_ball = ball
	_impulse_y = impulse_y
	body_entered.connect(_on_body_entered)


func _on_body_entered(body: Node2D) -> void:
	if body != _ball:
		return
	if not _ball.is_on_ground():
		return
	_ball.linear_velocity = Vector2(0.0, _impulse_y * 30.0 if absf(_impulse_y) < 100.0 else _impulse_y)
