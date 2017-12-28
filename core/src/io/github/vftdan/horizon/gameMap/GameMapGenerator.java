package io.github.vftdan.horizon.gameMap;
import static io.github.vftdan.horizon.gameMap.BasicTileType.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Queue;

import io.github.vftdan.horizon.GameSession;

public class GameMapGenerator {
	private int r1, r2;
	//private static final int MIN_NBS = 4, MIN_NBS_NEW = 5;
	private static final CellularAutomataProgram firstAutomata = new CellularAutomataProgram(4, 5);
	private static final CellularAutomataProgram secondAutomata = new CellularAutomataProgram(3, 4);
	public void initRand(GameSession gs, int id) {
		initRand(gs.seed, id);
	}
	public void initRand(long seed, int id) {
		r1 = (int)(seed * 3 + id + 1);
		r2 = (int)(seed >> (id % 24 + 3));
	}
	public void skipRand() {
		r2 ^= r1;
		r1 ^= r1 << 13;
		r1 ^= r1 >> 17;
		r1 ^= r1 << 5;
		r1 += r2;
	}
	public int randInt() {
		skipRand();
		return r1 * r2;
	}
	public boolean randBool() {
		return ((randInt() ^ (r2 >> 2)) & 1) == 1;
	}
	public BasicTileType[][] genBasics(int w, int h) {
		BasicTileType[][] map = new BasicTileType[h][w];
		BasicTileType[][] mapNew = new BasicTileType[h][w];
		BasicTileType[][] mapSwapTemp;
		BasicTileType defa = BasicTileType.OPAQUE;
		int i, j, k;
		//:fill random
		for(i = 0; i < h; i++) {
			for(j = 0; j < w; j++) {
				map[i][j] = create(randBool(), false);
			}
		}
		//:automata iterations
		for(k = 0; k < 4; k++) {
			firstAutomata.automataIter(mapNew, map, defa, w, h);
			mapSwapTemp = map;
			map = mapNew;
			mapNew = mapSwapTemp;
		}
		for(k = 0; k < 4; k++) {
			secondAutomata.automataIter(mapNew, map, defa, w, h);
			mapSwapTemp = map;
			map = mapNew;
			mapNew = mapSwapTemp;
		}
		/*//:borders
		for(i = 0; i < h; i++) {
			map[i][0] = map[i][w - 1] = BasicTileType.OPAQUE;
			//System.out.println(i);
		}
		for(j = 0; j < w; j++) {
			map[0][j] = map[h - 1][j] = BasicTileType.OPAQUE;
			//System.out.println(j);
		}*/
		
		return map;
	}
	public Vector2[] appendPassages(BasicTileType[][] map, int w, int h) {
		ArrayList<ArrayList<Vector2>> rooms = new ArrayList<ArrayList<Vector2>>();
		ArrayList<Vector2> room;
		Set<Vector2> visited = new HashSet<Vector2>();
		Queue<Vector2> bfsq = new Queue<Vector2>();
		Vector2 curVec, vecL, vecR, vecT, vecB;
		int i, j;
		//:find components
		for(i = 0; i < h; i++) {
			for(j = 0; j < w; j++) {
				curVec = new Vector2(j, i);
				if(!map[i][j].isOpaque() && !visited.contains(curVec)) {
					room = new ArrayList<Vector2>();
					bfsq.addLast(curVec);
					while(bfsq.size != 0) {
						curVec = bfsq.removeFirst();
						if(map[(int) curVec.y][(int) curVec.x].isOpaque() || visited.contains(curVec)) continue;
						visited.add(curVec);
						room.add(curVec);
						vecL = curVec.cpy().add(-1, 0);
						vecR = curVec.cpy().add(1, 0);
						vecT = curVec.cpy().add(0, -1);
						vecB = curVec.cpy().add(0, 1);
						if(vecL.x >= 0 && !visited.contains(vecL)) {
							bfsq.addLast(vecL);
						}
						if(vecR.x < w && !visited.contains(vecR)) {
							bfsq.addLast(vecR);
						}
						if(vecT.y >= 0 && !visited.contains(vecT)) {
							bfsq.addLast(vecT);
						}
						if(vecB.y < h && !visited.contains(vecB)) {
							bfsq.addLast(vecB);
						}
					}
					rooms.add(room);
					room = null;
				}
			}
		}
		//:shuffle
		//TODO O(1) instead of O(n * n)
		for(i = 0; i < rooms.size() - 1; i++) {
			for(j = i + 1; j < rooms.size(); j++) {
				if(randBool()) {
					room = rooms.get(i);
					rooms.set(i, rooms.get(j));
					rooms.set(j, room);
				}
			}
		}
		//System.out.println(rooms.size());
		if(rooms.size() == 0) {
			//TODO
		}
		room = null;
		for(ArrayList<Vector2> room2: rooms) {
			if(room != null) {
				putPassage(map
						, room.get(room.size() >> 1)
						, room2.get(room2.size() >> 1)
						);
			}
			room = room2;
		}
		return new Vector2[]{rooms.get(0).get(0), rooms.get(rooms.size() - 1).get(0)};
	}
	public void putPassage(BasicTileType[][] map, Vector2 v1, Vector2 v2) {
		//System.out.println(v1.toString() + " " + v2.toString());
		if(v1.equals(v2)) return;
		int dx = (int)(v2.x - v1.x);
		int dy = (int)(v2.y - v1.y);
		int x, y;
		int xs = dx >= 0 ? 1 : -1
		  , ys = dy >= 0 ? 1 : -1;
		boolean flag = true;
		float k, s = 0;
		dx = Math.abs(dx);
		dy = Math.abs(dy);
		if(dx > dy) {
			k = Math.abs(dy / (float)dx);
			y = (int)v1.y;
			for(x = (int)v1.x; x != v2.x; x += xs) {
				s += 1.0;
				if(map[y][x].isOpaque()) {
					if(flag && ((randInt() & 15) == 0) || ((randInt() & 31) == 0)) {
						flag = false;
						map[y][x] = BasicTileType.ALL_SEMI;
						//System.out.println(new Vector2(x, y));
					} else {
						map[y][x] = BasicTileType.PERMEABLE;
					}
				}
				if(s > k) {
					s -= k;
					y += ys;
					if((v2.y - y) * ys < 0) y -= ys;
					//System.out.println(y);
					map[y][x] = BasicTileType.PERMEABLE;
				}
			}
		} else {
			k = Math.abs(dx / (float)dy);
			x = (int)v1.x;
			for(y = (int)v1.y; y != v2.y; y += ys) {
				s += 1.0;
				if(map[y][x].isOpaque()) {
					if(flag && ((randInt() & 15) == 1) || ((randInt() & 31) == 1)) {
						flag = false;
						map[y][x] = BasicTileType.ALL_SEMI;
						//System.out.println(new Vector2(x, y));
					} else {
						map[y][x] = BasicTileType.PERMEABLE;
					}
				}
				if(s > k) {
					s -= k;
					x += xs;
					if((v2.x - x) * xs < 0) x -= xs;
					//System.out.println(y);
					map[y][x] = BasicTileType.PERMEABLE;
				}
			}
		}
	}
	/*private void basicAutomataIter(BasicTileType[][] mapNew, BasicTileType[][] map, BasicTileType defa, int w, int h) {
		int i, j;
		for(i = 0; i < h; i++) {
			for(j = 0; j < w; j++) {
				mapNew[i][j] = BasicTileType.create(map[i][j].isOpaque()?MIN_NBS <= calcNbs(map, i, j, defa):MIN_NBS_NEW <= calcNbs(map, i, j, defa), false);
				//System.out.println("Tile at: " + j + ", " + i + " is " + (map[i][j].isOpaque()?"Opaque":"Permeable"));
			}
		}
	}*/
	public static int calcNbs(BasicTileType[][] map, int I, int J, BasicTileType defa) {
		int i, j, c = 0, w = map[0].length, h = map.length;
		for(i = -1; i <= 1; i++) {
			for(j = -1; j <= 1; j++) {
				if((i | j) == 0) continue;
				if(I + i < 0 || I + i >= h || J + j < 0 || J + j >= w) {
					if(defa.isOpaque()) c++;
					continue;
				}
				if(map[I + i][J + j].isOpaque()) c++;
			}
		}
		return c;
	}
	private static class CellularAutomataProgram {
		private int MIN_NBS, MIN_NBS_NEW;
		public CellularAutomataProgram(int minNbs, int minNbsNew) {
			MIN_NBS = minNbs;
			MIN_NBS_NEW = minNbsNew;
		}
		public void automataIter(BasicTileType[][] mapNew, BasicTileType[][] map, BasicTileType defa, int w, int h) {
			int i, j;
			for(i = 0; i < h; i++) {
				for(j = 0; j < w; j++) {
					mapNew[i][j] = BasicTileType.create(map[i][j].isOpaque()?MIN_NBS <= calcNbs(map, i, j, defa):MIN_NBS_NEW <= calcNbs(map, i, j, defa), false);
				}
			}
		}
	}
}
