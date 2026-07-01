extends RefCounted
class_name InputFactory

static func create() -> InputPort:
	if OS.has_feature("mobile"):
		return AccelerometerInput.new()
	return KeyboardInput.new()
