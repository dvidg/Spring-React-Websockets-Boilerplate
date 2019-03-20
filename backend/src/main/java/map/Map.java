package map;
import java.util.Random;
import java.util.Arrays;

public class Map {
	public static final int MAP_SIZE = 10;
	private RandomWalk rw;
	private ShortestPath sp;
	private int[][] mapType = new int[Map.MAP_SIZE][Map.MAP_SIZE];
	private int[][] originalMap = new int[Map.MAP_SIZE][Map.MAP_SIZE];
	private int[][] binaryMap = new int[Map.MAP_SIZE][Map.MAP_SIZE];
	private int[][] floorMap = new int[Map.MAP_SIZE][Map.MAP_SIZE];	


	public Map() {	
		rw = new RandomWalk();
		rw.randomWalk();
		originalMap = rw.getMap();
		sp = new ShortestPath();
	}
	
	public void initializeMap() {
		TileType tt = new TileType();
		mapType = tt.generateType(rw.map);
	}
	
	// Get the shortest path of two points, -1 means destination point cannot be reached.
	public int shortestPath(int x, int y, int a, int b) {
		if (originalMap[a][b] == 0) {
			return -1;
		} else {
			sp.setMap(binaryMap);
			return sp.BFS(x, y, a, b);
		}
	}
	
	// Updates the binary map by the coordinates 
	public void updateBinaryMap(int x, int y) {
		binaryMap[x][y] = 1;
	}
	
	// get the visual map which includes tile type
	public int[][] getMapType(){
		return mapType;
	}
	
	// get the original map which is just 0 and 1
	public int[][] getOriginalMap(){
		return originalMap;
	}
	
	// get the binary map which shows which tiles have already been reached
	public int[][] getBinaryMap(){
		return binaryMap;
	}

	// initialise the floor map which decides which floor image is on which square	
	public void createFloorMap() {
		int [] floorTypes = {0, 16, 32, 48, 64};
		for (int i = 0; i < MAP_SIZE; i++) {
			for (int j = 0; j < MAP_SIZE; j++) {
				floorMap[i][j] = getRandom(floorTypes);
			}
		}
	}	

	public void swapFloorMap(int difficulty) {
		for (int i = 0; i < MAP_SIZE; i++) {
			for (int j = 0; j < MAP_SIZE; j++) {
				int rnd = getRndInt(0, difficulty);
				if(rnd > 1) {
					floorMap[i][j] = 80;
				}
			}
		}
	}

	public int[][] getFloorMap() {
		return floorMap;
	}
		
	// access random element of an array
	public static int getRandom(int[] array) {
		int rnd = new Random().nextInt(array.length);
		return array[rnd];
	}

	private static int getRndInt(int min, int max) {
		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}

	public int getMAP_SIZE() {
		return MAP_SIZE;
	}

	public boolean getFireTile(int r, int c) {
		if (floorMap[r][c] == 80) {
			return true;
		}
		return false;
	}
}
