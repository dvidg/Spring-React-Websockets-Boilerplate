package map;

import java.util.ArrayList;

public class ShortestPath {		
	private int[][] map;
	Point srcPoint;
	Point desPoint;
	ArrayList<Point> queue;
	int[][] visited;  
	int counter;
		
	public ShortestPath() {
		map = new int[Map.MAP_SIZE][Map.MAP_SIZE];
		queue  = new ArrayList<Point>();
		visited = new int[400][2];
	}
	
	// BFS to get shortest path. Return -1 if the destination cannot be reached.
	public int BFS (int x, int y, int a, int b) {
		srcPoint = new Point(x, y, 0);
		desPoint = new Point(a, b, -1);
		queue.add(srcPoint);
		map[a][b] = 1;
		
		counter = 0;
		int rowNum[] = {-1, 0, 0, 1}; 
		int colNum[] = {0, -1, 1, 0}; 

		// recursion until distance > 11 or queue is empty
		while(queue.size() > 0 && queue.get(0).getDistance() < 11) {
			// check destination point
			if(queue.get(0).getX() == desPoint.getX() && (queue.get(0)).getY() == desPoint.getY()){
				if(queue.get(0).getDistance() > 0) {
					int distance = queue.get(0).getDistance();
					queue.clear();

					return distance;
				} else {
					break;
				}
				
			} else {
				visited[counter][0] = queue.get(0).getX();
				visited[counter][1] = queue.get(0).getY();
				counter++;
				
				// add nearby 4 point to queue.
				for(int i = 0; i< 4; i++) {
					int row = queue.get(0).getX() + rowNum[i];
					int col = queue.get(0).getY() + colNum[i];
					if(isValid(row, col)) {
						if(map[row][col] == 1) {
							queue.add(new Point(row, col, queue.get(0).getDistance() + 1));
						}
					}
				}
				// remove the point that has been checked.
				queue.remove(0);
			}
		}
		queue.clear();
		return -1;
	}
	
	// Check that the point has been visited 
	public boolean isValid (int x, int y) {
		for(int i = 0; i < counter; i++) {
			if(x == visited[i][0] && y == visited[i][1]) {
				return false;
			}
		}
		return true;
	}
	
	public void setMap(int[][] map) {
		for(int i = 0; i < map.length; i++) {
			System.arraycopy(map[i], 0, this.map[i], 0, map[i].length);
		}
	}
}
