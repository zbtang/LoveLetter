extends RefCounted
class_name LevelMetadata

var id: int
var index: int
var name: String
var background_texture: String
var background_offset: Vector2
var ball_spawn: Vector2
var next_portal: Dictionary
var terrain_polygons: Array
var world_border: Dictionary
var special: Dictionary
var skip_letters: bool


static func from_dict(data: Dictionary) -> LevelMetadata:
	var meta := LevelMetadata.new()
	meta.id = int(data.get("id", 0))
	meta.index = int(data.get("index", 0))
	meta.name = str(data.get("name", ""))
	var bg: Dictionary = data.get("background", {})
	meta.background_texture = str(bg.get("texture", ""))
	var offset: Array = bg.get("offset", [0, 0])
	meta.background_offset = Vector2(float(offset[0]), float(offset[1]))
	var spawn: Array = data.get("ball_spawn", [100, 100])
	meta.ball_spawn = Vector2(float(spawn[0]), float(spawn[1]))
	meta.next_portal = data.get("next_portal") if data.get("next_portal") != null else {}
	var terrain: Dictionary = data.get("terrain", {})
	meta.terrain_polygons = terrain.get("polygons", [])
	meta.world_border = data.get("world_border", {})
	meta.special = data.get("special", {"type": "none"})
	meta.skip_letters = bool(data.get("skip_letters", false))
	return meta
