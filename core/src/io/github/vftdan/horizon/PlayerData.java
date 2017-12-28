package io.github.vftdan.horizon;

import java.io.Serializable;

public class PlayerData implements Serializable {
	private static final long serialVersionUID = 1208925001265773001L;
	public float health = 0;
	public long score;
	public int level;
	public SerializablePair<Integer, Integer> position;
	public SerializablePair<Integer, Integer>[] opened;
}
