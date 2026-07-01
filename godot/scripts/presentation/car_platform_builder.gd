extends RefCounted
class_name CarPlatformBuilder

static func create(world: Node2D, config: Dictionary) -> Node2D:
	var root := Node2D.new()
	root.name = "CarPlatform"
	root.set_script(load("res://scripts/presentation/car_platform.gd"))

	var polygons: Array = config.get("polygons", [])
	var car_body := _make_kinematic(polygons, config.get("sprite_offset", [473, 112]))
	root.add_child(car_body)

	var rope_polys: Array = config.get("rope", [])
	if not rope_polys.is_empty():
		world.add_child(_make_static(rope_polys[0]))

	world.add_child(root)
	root.setup(car_body, float(config.get("speed", 28.0)), float(config.get("left_limit", 2.0)), float(config.get("right_limit", -170.0)))
	return root


static func _make_static(vertices_nested: Array) -> StaticBody2D:
	var vertices: Array = []
	for group in vertices_nested:
		for vertex in group:
			vertices.append(vertex)
	var body := StaticBody2D.new()
	body.collision_layer = GameConfig.LAYER_TERRAIN
	var shape := CollisionPolygon2D.new()
	var points := PackedVector2Array()
	for vertex in vertices:
		points.append(Vector2(float(vertex[0]), float(vertex[1])))
	shape.polygon = points
	body.add_child(shape)
	return body


static func _make_kinematic(polygons_nested: Array, sprite_offset: Array) -> AnimatableBody2D:
	var body := AnimatableBody2D.new()
	body.collision_layer = GameConfig.LAYER_DYNAMIC
	body.collision_mask = GameConfig.LAYER_TERRAIN
	for group in polygons_nested:
		var points := PackedVector2Array()
		for vertex in group:
			points.append(Vector2(float(vertex[0]), float(vertex[1])))
		var shape := CollisionPolygon2D.new()
		shape.polygon = points
		body.add_child(shape)
	var sprite := Sprite2D.new()
	sprite.texture = load("res://assets/textures/car.png") as Texture2D
	sprite.position = Vector2(float(sprite_offset[0]), float(sprite_offset[1])) - body.position
	sprite.centered = false
	body.add_child(sprite)
	return body
