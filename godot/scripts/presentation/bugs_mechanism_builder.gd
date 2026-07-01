extends RefCounted
class_name BugsMechanismBuilder

static func create(world: Node2D, config: Dictionary) -> Node2D:
	var node := Node2D.new()
	node.name = "BugsMechanism"
	node.set_script(load("res://scripts/presentation/bugs_mechanism.gd"))
	world.add_child(node)
	node.setup(config.get("groups", []), config.get("sprites", []), int(config.get("cycle_frames", GameConfig.BUG_CYCLE_FRAMES)))
	return node
