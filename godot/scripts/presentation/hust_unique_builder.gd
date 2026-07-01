extends RefCounted
class_name HustUniqueBuilder

static func create(world: Node2D, config: Dictionary, ball: RigidBody2D) -> Node2D:
	var root := Node2D.new()
	root.name = "HustUnique"
	var script := load("res://scripts/presentation/hust_unique_mechanism.gd")
	root.set_script(script)
	world.add_child(root)

	var sensor := Area2D.new()
	sensor.name = "Sensor"
	sensor.collision_layer = GameConfig.LAYER_SENSOR
	sensor.collision_mask = GameConfig.LAYER_BALL
	sensor.monitoring = true
	for polygon_group in config.get("polygons", []):
		var shape := CollisionPolygon2D.new()
		var points := PackedVector2Array()
		for vertex in polygon_group:
			points.append(Vector2(float(vertex[0]), float(vertex[1])))
		shape.polygon = points
		sensor.add_child(shape)
	root.add_child(sensor)

	root.call("setup", ball, config, sensor)
	return root
