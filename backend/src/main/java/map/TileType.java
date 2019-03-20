package map;

public class TileType {
	int[][] map = new int[Map.MAP_SIZE][Map.MAP_SIZE];
	
	public static final int UP_DOOR = 1;
	public static final int LEFT_DOOR = 2;
	public static final int RIGHT_DOOR = 3;
	public static final int BOTTOM_DOOR = 4;
	public static final int UP_LEFT_DOOR = 5;
	public static final int UP_RIGHT_DOOR = 6;
	public static final int UP_BOTTOM_DOOR = 7;
	public static final int LEFT_RIGHT_DOOR = 8;
	public static final int LEFT_BOTTOM_DOOR = 9;
	public static final int RIGHT_BOTTOM_DOOR = 10;
	public static final int NONE_UP_DOOR = 11;
	public static final int NONE_LEFT_DOOR = 12;
	public static final int NONE_RIGHT_DOOR = 13;
	public static final int NONE_BOTTOM_DOOR = 14;
	public static final int ALL_DOOR = 15;
	
	public TileType() {
		
	}
	
	public int[][] generateType(int[][] m){
		for(int i = 0; i < m.length; i++) {
			System.arraycopy(m[i], 0, map[i], 0, m[i].length);
		}
		
		for(int i = 1; i < Map.MAP_SIZE-1; i++) {
			for(int j = 1; j < Map.MAP_SIZE-1; j++) {
				if(map[i][j] == 0) {
					continue;
				}
				if(map[i-1][j] == 0) {
					if(map[i][j-1] == 0) {
						if(map[i][j+1] == 0) {
							if(map[i+1][j] == 0) {
								continue;
							} else {
								map[i][j] = BOTTOM_DOOR;
							}
						} else {
							if(map[i+1][j] == 0) {
								map[i][j] = RIGHT_DOOR;
							} else {
								map[i][j] = RIGHT_BOTTOM_DOOR;
							}
						}
					} else {
						if(map[i][j+1] == 0) {
							if(map[i+1][j] == 0) {
								map[i][j] = LEFT_DOOR;
							} else {
								map[i][j] = LEFT_BOTTOM_DOOR;
							}
						} else {
							if(map[i+1][j] == 0) {
								map[i][j] = LEFT_RIGHT_DOOR;
							} else {
								map[i][j] = NONE_UP_DOOR;
							}
						}
					}
				} else {
					if(map[i][j-1] == 0) {
						if(map[i][j+1] == 0) {
							if(map[i+1][j] == 0) {
								map[i][j] = UP_DOOR;;
							} else {
								map[i][j] = UP_BOTTOM_DOOR;
							}
						} else {
							if(map[i+1][j] == 0) {
								map[i][j] = UP_RIGHT_DOOR;
							} else {
								map[i][j] = NONE_LEFT_DOOR;
							}
						}
					} else {
						if(map[i][j+1] == 0) {
							if(map[i+1][j] == 0) {
								map[i][j] = UP_LEFT_DOOR;
							} else {
								map[i][j] = NONE_RIGHT_DOOR;
							}
						} else {
							if(map[i+1][j] == 0) {
								map[i][j] = NONE_BOTTOM_DOOR;
							} else {
								map[i][j] = ALL_DOOR;
							}
						}
					}
				}
			}
		}
		return map;
	}	
}
