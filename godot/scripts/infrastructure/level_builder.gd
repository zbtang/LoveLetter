extends Node2D
class_name LevelBuilder

signal built(root: Node2D, ball: RigidBody2D)


static func build(parent: Node2D, metadata: LevelMetadata) -> Dictionary:
	var world := Node2D.new()
	world.name = "LevelWorld"
	parent.add_child(world)

	var background := _create_background(metadata)
	world.add_child(background)

	var terrain_root := Node2D.new()
	terrain_root.name = "Terrain"
	world.add_child(terrain_root)

	if not metadata.skip_letters:
		for poly_data in metadata.terrain_polygons:
			var vertices: Array = poly_data.get("vertices", [])
			if vertices.is_empty():
				continue
			terrain_root.add_child(_create_static_polygon(vertices))

	_create_world_borders(world, metadata, background)

	var ball := _create_ball(metadata.ball_spawn)
	world.add_child(ball)

	var ocean := _create_ocean(world)
	world.add_child(ocean)

	var dead_line := _create_dead_line()
	world.add_child(dead_line)

	var next_portal: Area2D = null
	if not metadata.next_portal.is_empty():
		next_portal = _create_next_portal(metadata.next_portal)
		world.add_child(next_portal)

	var special_nodes := _build_special(world, metadata, ball)

	return {
		"world": world,
		"ball": ball,
		"ocean": ocean,
		"dead_line": dead_line,
		"next_portal": next_portal,
		"background": background,
		"special": special_nodes,
	}


static func _create_background(metadata: LevelMetadata) -> Sprite2D:
	var sprite := Sprite2D.new()
	sprite.name = "Background"
	sprite.texture = load(metadata.background_texture) as Texture2D
	sprite.position = metadata.background_offset
	sprite.centered = false
	return sprite


static func _create_static_polygon(vertices: Array) -> StaticBody2D:
	var body := StaticBody2D.new()
	body.collision_layer = GameConfig.LAYER_TERRAIN
	body.collision_mask = 0
	var shape := CollisionPolygon2D.new()
	var points: PackedVector2Array = PackedVector2Array()
	for vertex in vertices:
		points.append(Vector2(float(vertex[0]), float(vertex[1])))
	shape.polygon = points
	body.add_child(shape)
	return body


static func _create_world_borders(world: Node2D, metadata: LevelMetadata, background: Sprite2D) -> void:
	var bg_width := GameConfig.DESIGN_WIDTH
	if background.texture:
		bg_width = float(background.texture.get_width())
	var right_x := bg_width - float(metadata.world_border.get("right_offset_from_bg", 4))
	_add_border_wall(world, float(metadata.world_border.get("left", 4)), "LeftBorder")
	_add_border_wall(world, right_x, "RightBorder")


static func _add_border_wall(world: Node2D, x: float, label: String) -> void:
	var body := StaticBody2D.new()
	body.name = label
	body.collision_layer = GameConfig.LAYER_TERRAIN
	var shape := CollisionShape2D.new()
	var rect := RectangleShape2D.new()
	rect.size = Vector2(10.0, GameConfig.DESIGN_HEIGHT)
	shape.shape = rect
	shape.position = Vector2(x, GameConfig.DESIGN_HEIGHT * 0.5)
	body.add_child(shape)
	world.add_child(body)


static func _create_ball(spawn: Vector2) -> RigidBody2D:
	var ball_scene := preload("res://scenes/game/Ball.tscn")
	var ball: RigidBody2D = ball_scene.instantiate()
	ball.name = "Ball"
	ball.position = spawn
	return ball


static func _create_ocean(world: Node2D) -> Node2D:
	var ocean_scene := preload("res://scenes/game/Ocean.tscn")
	var ocean: Node2D = ocean_scene.instantiate()
	ocean.name = "Ocean"
	return ocean


static func _create_dead_line() -> Area2D:
	var area := Area2D.new()
	area.name = "DeadLine"
	area.collision_layer = GameConfig.LAYER_SENSOR
	area.collision_mask = GameConfig.LAYER_BALL
	area.monitoring = true
	var shape := CollisionShape2D.new()
	var rect := RectangleShape2D.new()
	rect.size = Vector2(GameConfig.DESIGN_WIDTH, 4.0)
	shape.shape = rect
	shape.position = Vector2(
		GameConfig.DESIGN_WIDTH * 0.5,
		GameConfig.DESIGN_HEIGHT - GameConfig.OCEAN_SURFACE_LEVEL
	)
	area.add_child(shape)
	area.add_to_group("dead_line")
	return area


static func _create_next_portal(portal: Dictionary) -> Area2D:
	var area := Area2D.new()
	area.name = "NextPortal"
	area.collision_layer = GameConfig.LAYER_SENSOR
	area.collision_mask = GameConfig.LAYER_BALL
	area.monitoring = true
	var pos: Array = portal.get("position", [650, 240])
	var half: Array = portal.get("half_size", [45, 27])
	area.position = Vector2(float(pos[0]), float(pos[1]))
	var shape := CollisionShape2D.new()
	var rect := RectangleShape2D.new()
	rect.size = Vector2(float(half[0]) * 2.0, float(half[1]) * 2.0)
	shape.shape = rect
	area.add_child(shape)
	var sprite := Sprite2D.new()
	var tex_path := str(portal.get("texture", "res://assets/textures/next.png"))
	sprite.texture = load(tex_path) as Texture2D
	sprite.centered = true
	area.add_child(sprite)
	area.add_to_group("next_portal")
	return area


static func _build_special(world: Node2D, metadata: LevelMetadata, ball: RigidBody2D) -> Node:
	var special: Dictionary = metadata.special
	var special_type := str(special.get("type", "none"))
	match special_type:
		"car":
			return CarPlatformBuilder.create(world, special)
		"gear":
			return GearMechanismBuilder.create(world, special)
		"heads":
			return HeadsObstacleBuilder.create(world, special)
		"bugs":
			return BugsMechanismBuilder.create(world, special)
		"spring_gate":
			return SpringGateBuilder.create(world, special, ball)
		"hust_unique":
			return HustUniqueBuilder.create(world, special, ball)
		_:
			return null
