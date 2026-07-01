extends RefCounted
class_name GearMechanismBuilder

static func create(world: Node2D, config: Dictionary) -> Node2D:
	var root := Node2D.new()
	root.name = "GearMechanism"
	_add_gear(world, config, true)
	_add_gear(world, config, false)
	return root


static func _add_gear(world: Node2D, config: Dictionary, is_big: bool) -> void:
	var center_data: Array = config.get("big_center" if is_big else "small_center", [0, 0])
	var center := Vector2(float(center_data[0]), float(center_data[1]))
	var polygons: Array = config.get("big_polygons" if is_big else "small_polygons", [])
	var texture_path := "res://assets/textures/big_chilun.png" if is_big else "res://assets/textures/small_chilun.png"
	var motor_speed := float(config.get("big_motor_speed" if is_big else "small_motor_speed", 1.0))

	var anchor := StaticBody2D.new()
	anchor.position = center
	anchor.collision_layer = 0
	world.add_child(anchor)

	var gear := RigidBody2D.new()
	gear.position = center
	gear.gravity_scale = 0.0
	gear.collision_layer = GameConfig.LAYER_DYNAMIC
	gear.collision_mask = GameConfig.LAYER_BALL | GameConfig.LAYER_TERRAIN
	gear.lock_rotation = false
	for group in polygons:
		var points := PackedVector2Array()
		for vertex in group:
			points.append(Vector2(float(vertex[0]), float(vertex[1])) - center)
		var shape := CollisionPolygon2D.new()
		shape.polygon = points
		gear.add_child(shape)
	var sprite := Sprite2D.new()
	sprite.texture = load(texture_path) as Texture2D
	sprite.centered = true
	gear.add_child(sprite)
	world.add_child(gear)

	var joint := PinJoint2D.new()
	joint.position = center
	joint.node_a = anchor.get_path()
	joint.node_b = gear.get_path()
	world.add_child(joint)

	gear.set_script(load("res://scripts/presentation/gear_body.gd"))
	gear.call("configure", motor_speed)
