extends Node2D

var _body: AnimatableBody2D
var _direction: float = -1.0
var _speed: float = 28.0
var _left_limit: float = 2.0
var _right_limit: float = -170.0
var _start_x: float = 0.0


func setup(body: AnimatableBody2D, speed: float, left_limit: float, right_limit: float) -> void:
	_body = body
	_speed = speed
	_left_limit = left_limit
	_right_limit = right_limit
	_start_x = body.position.x


func _physics_process(delta: float) -> void:
	if _body == null:
		return
	var offset := _body.position.x - _start_x
	if offset < _right_limit:
		_direction = 1.0
	elif offset > _left_limit:
		_direction = -1.0
	_body.position.x += _direction * _speed * delta
