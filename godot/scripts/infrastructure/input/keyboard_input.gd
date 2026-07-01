extends InputPort
class_name KeyboardInput

func get_horizontal() -> float:
	return Input.get_axis("move_left", "move_right")


func is_jump_just_pressed() -> bool:
	return Input.is_action_just_pressed("jump")
