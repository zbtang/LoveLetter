extends Node

var _bgm_player: AudioStreamPlayer


func _ready() -> void:
	_bgm_player = AudioStreamPlayer.new()
	add_child(_bgm_player)
	var stream := load("res://assets/audio/backmusic.wav") as AudioStream
	if stream:
		_bgm_player.stream = stream
		if stream is AudioStreamWAV:
			stream.loop_mode = AudioStreamWAV.LOOP_FORWARD
		elif stream.has_method("set_loop"):
			stream.set_loop(true)


func play_bgm() -> void:
	if not GameState.music_enabled:
		return
	if _bgm_player.playing:
		return
	_bgm_player.play()


func stop_bgm() -> void:
	_bgm_player.stop()


func set_music_enabled(enabled: bool) -> void:
	GameState.music_enabled = enabled
	LevelProgress.music_enabled = enabled
	LevelProgress.save_progress()
	if enabled:
		play_bgm()
	else:
		stop_bgm()


func play_jump() -> void:
	if not GameState.music_enabled:
		return
	var player := AudioStreamPlayer.new()
	add_child(player)
	var stream := load("res://assets/audio/jump.wav") as AudioStream
	if stream:
		player.stream = stream
		player.play()
		player.finished.connect(player.queue_free)
