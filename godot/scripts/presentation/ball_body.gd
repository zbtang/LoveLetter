extends RigidBody2D

var ground_contacts: int = 0


func _ready() -> void:
	contact_monitor = true
	max_contacts_reported = 8
	body_entered.connect(_on_body_entered)
	body_exited.connect(_on_body_exited)
	add_to_group("ball")


func is_on_ground() -> bool:
	return ground_contacts > 0


func _on_body_entered(body: Node) -> void:
	if body is StaticBody2D or body is RigidBody2D or body is AnimatableBody2D:
		if body != self:
			ground_contacts += 1


func _on_body_exited(body: Node) -> void:
	if body is StaticBody2D or body is RigidBody2D or body is AnimatableBody2D:
		if body != self:
			ground_contacts = max(0, ground_contacts - 1)
