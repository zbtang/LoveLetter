extends InputPort
class_name AccelerometerInput

var _use_x_axis: bool = true


func _init() -> void:
	pass


func get_horizontal() -> float:
	var accel := Input.get_accelerometer()
	if accel == Vector3.ZERO:
		return Input.get_axis("move_left", "move_right")
	var tilt := accel.x if _use_x_axis else accel.y
	if absf(tilt) < GameConfig.SENSOR_THRESHOLD:
		return 0.0
	return -tilt


func is_jump_just_pressed() -> bool:
	return Input.is_action_just_pressed("jump")
