extends RefCounted
class_name SpringGateBuilder

static func create(world: Node2D, config: Dictionary, ball: RigidBody2D) -> Area2D:
	var gate_vertices: Array = config.get("gate_polygon", [])
	var area := Area2D.new()
	area.name = "SpringGate"
	area.collision_layer = GameConfig.LAYER_SENSOR
	area.collision_mask = GameConfig.LAYER_BALL
	area.monitoring = true
	if not gate_vertices.is_empty():
		var shape := CollisionPolygon2D.new()
		var points := PackedVector2Array()
		for group in gate_vertices:
			for vertex in group:
				points.append(Vector2(float(vertex[0]), float(vertex[1])))
		shape.polygon = points
		area.add_child(shape)
	var script := load("res://scripts/presentation/spring_gate.gd")
	area.set_script(script)
	world.add_child(area)
	area.call("setup", ball, float(config.get("teleport_velocity_y", -25.0)))
	return area
