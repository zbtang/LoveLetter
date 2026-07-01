extends RefCounted
class_name GameRules

static func can_jump(is_on_ground: bool, cheat_fly_mode: bool) -> bool:
	return is_on_ground or cheat_fly_mode


static func should_complete_level(is_hust_unique_active: bool) -> bool:
	return not is_hust_unique_active
