extends Node2D

var _groups: Array = []
var _sprites: Array = []
var _bodies: Array = []
var _cycle_frames: int = GameConfig.BUG_CYCLE_FRAMES
var _frame: int = 0


func setup(groups: Array, sprites: Array, cycle_frames: int) -> void:
	_groups = groups
	_sprites = sprites
	_cycle_frames = cycle_frames
	_build_bodies()


func _build_bodies() -> void:
	for group_index in range(_groups.size()):
		var group: Array = _groups[group_index]
		var group_root := Node2D.new()
		group_root.name = "BugGroup%d" % group_index
		add_child(group_root)
		var bodies: Array = []
		for poly_index in range(group.size()):
			var vertices: Array = group[poly_index]
			var body := StaticBody2D.new()
			body.collision_layer = GameConfig.LAYER_TERRAIN
			var shape := CollisionPolygon2D.new()
			var points := PackedVector2Array()
			for vertex in vertices:
				points.append(Vector2(float(vertex[0]), float(vertex[1])))
			shape.polygon = points
			body.add_child(shape)
			group_root.add_child(body)
			bodies.append(body)
		_bodies.append({"root": group_root, "bodies": bodies})


func _process(_delta: float) -> void:
	_frame += 1
	var phase := (_frame / _cycle_frames) % 4
	for index in range(_bodies.size()):
		var active := index == phase or index == phase + 1
		var entry: Dictionary = _bodies[index]
		entry["root"].visible = active
		for body in entry["bodies"]:
			body.set_deferred("collision_layer", GameConfig.LAYER_TERRAIN if active else 0)
	_draw_active_sprites(phase)


func _draw_active_sprites(phase: int) -> void:
	for child in get_children():
		if child is Sprite2D:
			child.queue_free()
	if phase >= _sprites.size() - 1:
		return
	for sprite_index in [phase, phase + 1]:
		if sprite_index >= _sprites.size():
			continue
		var data: Dictionary = _sprites[sprite_index]
		var sprite := Sprite2D.new()
		sprite.texture = load("res://assets/textures/%s.png" % data.get("texture", "bug1")) as Texture2D
		var pos: Array = data.get("position", [0, 0])
		sprite.position = Vector2(float(pos[0]), float(pos[1]))
		sprite.centered = false
		add_child(sprite)
