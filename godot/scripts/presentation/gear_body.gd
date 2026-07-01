extends RigidBody2D

var _motor_speed: float = 0.0


func configure(motor_speed: float) -> void:
	_motor_speed = motor_speed


func _physics_process(_delta: float) -> void:
	angular_velocity = _motor_speed
