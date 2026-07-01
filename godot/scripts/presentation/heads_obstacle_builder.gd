extends RefCounted
class_name HeadsObstacleBuilder

static func create(world: Node2D, config: Dictionary) -> Node2D:
	var root := Node2D.new()
	root.name = "HeadsObstacle"
	for circle_data in config.get("circles", []):
		var body := StaticBody2D.new()
		body.collision_layer = GameConfig.LAYER_TERRAIN
		var center := Vector2(float(circle_data[0]), float(circle_data[1]))
		body.position = center
		var shape := CollisionShape2D.new()
		var circle := CircleShape2D.new()
		circle.radius = float(circle_data[2])
		shape.shape = circle
		body.add_child(shape)
		world.add_child(body)
		root.add_child(body)
	return root
