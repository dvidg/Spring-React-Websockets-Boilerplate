package bot;

import java.util.ArrayList;
import java.util.Random;

public class ClosestTile {
	private ArrayList<int[]> queue;
	private int[][] map;
	private int[][] originalMap;
	private int[][] visited;
	private int counter = 0;
	private Random rd = new Random();
	
	public ClosestTile(int[][] map, int[][] originalMap) {
		queue = new ArrayList<int[]>();
		this.map = map;
		this.originalMap = originalMap;
		visited = new int[400][2];
	}
	
	// get closest new tile of the player
	public int[] getClosestNewTile(int[] position) {
		int[] srcPoint = new int[2];
		int[] target = new int[2];
		srcPoint[0] = position[0];
		srcPoint[1] = position[1];
		queue.add(srcPoint);
		
		
		int rowNum[] = {-1, 0, 0, 1}; 
		int colNum[] = {0, -1, 1, 0}; 

		// recursion until there is a new tile
		while(queue.size() > 0) {
			// check the first point in queue
			if(map[queue.get(0)[0]][queue.get(0)[1]] == 0){

				target[0] = queue.get(0)[0];
				target[1] = queue.get(0)[1];
				queue.clear();

				return target;
			} else {
				visited[counter][0] = queue.get(0)[0];
				visited[counter][1] = queue.get(0)[1];
				counter++;
				
				// add nearby 4 point to queue.
				int offset = rd.nextInt(4);
				
				for(int i = 0; i< 4; i++) {
					int row = queue.get(0)[0] + rowNum[(i + offset) % 4];
					int col = queue.get(0)[1] + colNum[(i + offset) % 4];
					if(isValid(row, col)) {
						queue.add(new int[] {row, col});
					}
				}
				// remove the point that has been checked.
				queue.remove(0);
			}
		}
		queue.clear();
		return null;
	}
	
	// Check that the point has been visited 
	public boolean isValid (int x, int y) {
		for(int i = 0; i < counter; i++) {
			if(x == visited[i][0] && y == visited[i][1]) {
				return false;
			}
		}

		if(originalMap[x][y] == 1) {
			return true;
		}

		return false;	
	}
}
