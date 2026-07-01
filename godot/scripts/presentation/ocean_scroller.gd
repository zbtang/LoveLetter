extends Node2D

var scroll_speed: float = GameConfig.OCEAN_SCROLL_SPEED
var _offset: float = 0.0
var _texture_width: float = 800.0


@onready var _sprite_a: Sprite2D = $SpriteA
@onready var _sprite_b: Sprite2D = $SpriteB


func _ready() -> void:
	var tex := load("res://assets/textures/ocean.png") as Texture2D
	if tex:
		_texture_width = float(tex.get_width())
		_sprite_a.texture = tex
		_sprite_b.texture = tex
	position = Vector2(0, 366)


func _process(_delta: float) -> void:
	_offset -= scroll_speed
	var wrap := fmod(_offset, _texture_width)
	if wrap > 0.0:
		wrap -= _texture_width
	_sprite_a.position.x = wrap
	_sprite_b.position.x = wrap + _texture_width - 1.0
