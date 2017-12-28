package io.github.vftdan.horizon;

import java.io.Serializable;
import java.util.TreeMap;

public class GameSession implements Serializable {
	
	public static final long serialVersionUID = 1208925000265773001L;
	
	
	public long seed = 0;
	public String[] studyNames;
	public PlayerData playerData;
	
	//public TreeMap<String, Serializable> additData;

}
