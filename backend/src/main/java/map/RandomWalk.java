/**************************** 
 * Random Walk generation algorithm
 * Date: 6/11/2018
 * Created by Jiacheng and Abraham
*/
package map;

import java.util.Random;

public class RandomWalk {
	
	Random rd = new Random();
	int[][] map;
	int[]position;
	
	
	public RandomWalk () {
		map = new int[Map.MAP_SIZE][Map.MAP_SIZE];
		
		// the position of the starting tile, Finds the middle position to start
		position = new int[2];
		position[0] = Map.MAP_SIZE/2;    
		position[1] = Map.MAP_SIZE/2;
		map[position[0]][position[1]] = 1;
	}
	
	// do random walk
	public void randomWalk() {
		
		int index = 0;
		/*The loop telling the random walk will stop a the percent stated below and the steps
		will be less than one million*/	
		while(mapSize() < 0.3 * Map.MAP_SIZE * Map.MAP_SIZE && index < 1000000) {
	
			//When the random walk reaches the map size, it will go back to the middle
			if(index % Map.MAP_SIZE == 0) {
				position[0] = Map.MAP_SIZE/2;
				position[1] = Map.MAP_SIZE/2;
			}
			
			//switch case to randomly use the plan of the walk.
			int plan = rd.nextInt(4);
			
			switch(plan) {
			case 0:
				planA();
				break;
			case 1:
				planB();
				break;
			case 2:
				planC();
				break;
			case 3:
				planD();
				break;
			default:
				break;
			}
			
			index++;
		}		
	}
	
	//Different plans of random walks. There is a 40% chance of going up and 20% of other sides
	public void planA() {
		int a = rd.nextInt(10);
		
		if(a < 4 && position[0] > 1) {
			map[position[0]-1][position[1]] = 1;
			position[0] = position[0]-1;
		} else if(a < 6 && position[1] > 1) {
			map[position[0]][position[1]-1] = 1;
			position[1] = position[1]-1;
		} else if(a < 8 && position[1] < Map.MAP_SIZE - 2) {
			map[position[0]][position[1]+1] = 1;
			position[1] = position[1]+1;
		}else if (position[0] < Map.MAP_SIZE - 2){
			map[position[0]+1][position[1]] = 1;
			position[0] = position[0]+1;
		}
	}
	
	public void planB() {
		int a = rd.nextInt(10);
		
		if(a < 2 && position[0] > 1) {
			map[position[0]-1][position[1]] = 1;
			position[0] = position[0]-1;
		} else if(a < 6 && position[1] > 1) {
			map[position[0]][position[1]-1] = 1;
			position[1] = position[1]-1;
		} else if(a < 8 && position[1] < Map.MAP_SIZE - 2) {
			map[position[0]][position[1]+1] = 1;
			position[1] = position[1]+1;
		}else if (position[0] < Map.MAP_SIZE - 2){
			map[position[0]+1][position[1]] = 1;
			position[0] = position[0]+1;
		}
	}
	
	public void planC() {
		int a = rd.nextInt(10);
		
		if(a < 2 && position[0] > 1) {
			map[position[0]-1][position[1]] = 1;
			position[0] = position[0]-1;
		} else if(a < 4 && position[1] > 1) {
			map[position[0]][position[1]-1] = 1;
			position[1] = position[1]-1;
		} else if(a < 8 && position[1] < Map.MAP_SIZE - 2) {
			map[position[0]][position[1]+1] = 1;
			position[1] = position[1]+1;
		}else if (position[0] < Map.MAP_SIZE - 2) {
			map[position[0]+1][position[1]] = 1;
			position[0] = position[0]+1;
		}
	}
	
	public void planD() {
		int a = rd.nextInt(10);
		
		if(a < 2  && position[0] > 1) {
			map[position[0]-1][position[1]] = 1;
			position[0] = position[0]-1;
		} else if(a < 4 && position[1] > 1) {
			map[position[0]][position[1]-1] = 1;
			position[1] = position[1]-1;
		} else if(a < 6 && position[1] < Map.MAP_SIZE - 2) {
			map[position[0]][position[1]+1] = 1;
			position[1] = position[1]+1;
		}else if (position[0] < Map.MAP_SIZE - 2){
			map[position[0]+1][position[1]] = 1;
			position[0] = position[0]+1;
		}
	}
	
	//Returns how many tiles have been filled
	public int mapSize() {
		int mapSize = 0;
		
		for (int i = 0; i < Map.MAP_SIZE; i++){
			for(int j = 0; j < Map.MAP_SIZE; j++) {
				if(map[i][j] == 1) {
					mapSize++;
				}
			}
		}
		return mapSize;
	}
	
	//Returns the whole map
	public int[][] getMap(){
		return map;
	}
}
