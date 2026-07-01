extends RefCounted
class_name LevelLoader

static func load_level(index: int) -> LevelMetadata:
	var path := "res://data/levels/level_%03d.json" % (index + 1)
	var file := FileAccess.open(path, FileAccess.READ)
	if file == null:
		push_error("Missing level file: %s" % path)
		return null
	var parsed: Variant = JSON.parse_string(file.get_as_text())
	file.close()
	if typeof(parsed) != TYPE_DICTIONARY:
		push_error("Invalid level JSON: %s" % path)
		return null
	return LevelMetadata.from_dict(parsed)
