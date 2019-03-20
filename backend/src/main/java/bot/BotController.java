package bot;

import java.util.Arrays;
import java.util.Random;
import bot.*;
import lobby.*;
import gameloop.GameLoop;
import player.Player;
import controller.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
@CrossOrigin
public class BotController {
	private GameLoop gl;
	private ClosestTile ct;
	private ClosestPlayer cp;
	private Random rd = new Random();
	private int botNumber = 0;
	private int[] botID;
	private boolean phaseTwo = false;
	private int endGame;
	private int[][] binaryMap;
	private Player[] players;
	private String eventResult;
	private String description;

	public BotController() { 
		botID = new int[3];
		for(int i = 0; i < 3; i++) {
			botID[i] = -1;
		}
	}
	
	public boolean check() {
		for(int i = 0; i < botID.length; i++) {
			if(gl.getTotalTurn() % 4 == botID[i]) {
				turnActive(players[botID[i]]);
				return true;
			}
		}
		return false;
		
	}
	
	public String getBotName(int inputBotID) {
		botID[botNumber] = inputBotID;
		botNumber++;
		String botName = "bot" + inputBotID;
		return botName;
	}

	public void turnActive(Player p) {
		description = null;
		eventResult = null;
		System.out.println(p.getName() + ": " + p.getHp() + "HP");

		if(p.getHp() == 0) {
			gl.nextTurn();
			return;
		}

		if(!phaseTwo) {
			int[] newPosition = new int[2];
			newPosition = ct.getClosestNewTile(p.getLocation());
			if(newPosition != null){
				gl.move(newPosition[0], newPosition[1]);
				eventResult = gl.processEvent(rd.nextInt(6)+1);
			}
		} else {
			switch(endGame) {
			case 0:
				if(p.getId() == gl.getVillainId()) {
					if(Arrays.equals(p.getLocation(), cp.getClosestPlayer(p).getLocation())) {
						description = gl.attack(cp.getClosestPlayer(p).getName(), rd.nextInt(6)+1, rd.nextInt(6)+1, p.getName());
						break;
					}
					
					moveTowards(cp.getClosestPlayer(p).getLocation(), p);

					if(Arrays.equals(p.getLocation(), cp.getClosestPlayer(p).getLocation())) {
						description = gl.attack(cp.getClosestPlayer(p).getName(), rd.nextInt(6)+1, rd.nextInt(6)+1, p.getName());
						break;
					}
					
				} else {
					int[] target = new int[2];
					target[0] = players[gl.getVillainId()].getLocation()[0];
					target[1] = players[gl.getVillainId()].getLocation()[1];			
					
					if(Arrays.equals(p.getLocation(), target)) {
						description = gl.attack(players[gl.getVillainId()].getName(), rd.nextInt(6)+1, rd.nextInt(6)+1, p.getName());
						break;
					}
							
					moveTowards(target, p);
					
					if(Arrays.equals(p.getLocation(), target)) {
						gl.attack(players[gl.getVillainId()].getName(), rd.nextInt(6)+1, rd.nextInt(6)+1, p.getName());
					}
				}
				
			case 1:
				if(Arrays.equals(p.getLocation(), cp.getClosestPlayer(p).getLocation())) {
					description = gl.attack(cp.getClosestPlayer(p).getName(), rd.nextInt(6)+1, rd.nextInt(6)+1, p.getName());
					break;
				}

				if(p.getId() == gl.getVillainId()) {   
					moveTowards(cp.getClosestPlayer(p).getLocation(), p);

					if(Arrays.equals(p.getLocation(), cp.getClosestPlayer(p).getLocation())) {
						description = gl.attack(cp.getClosestPlayer(p).getName(), rd.nextInt(6)+1, rd.nextInt(6)+1, p.getName());
						break;
					}
				} else {
					if(gl.getKey()) {
						moveTowards(gl.getKeyPosition(), p);
					} else {
						moveTowards(gl.getEscapePosition(), p);
					}
				}
				
			case 2:
				if(Arrays.equals(p.getLocation(), cp.getClosestPlayer(p).getLocation())) {
					description = gl.attack(cp.getClosestPlayer(p).getName(), rd.nextInt(6)+1, rd.nextInt(6)+1, p.getName());
					break;
				}
		
				moveTowards(cp.getClosestPlayer(p).getLocation(), p);

				if(Arrays.equals(p.getLocation(), cp.getClosestPlayer(p).getLocation())) {
					description = gl.attack(cp.getClosestPlayer(p).getName(), rd.nextInt(6)+1, rd.nextInt(6)+1, p.getName());
					break;
				}
				
			default:
				break;
			}
			
			
		}
		
		gl.nextTurn();
		
		phaseTwo = gl.getPhaseTwo();
		endGame = gl.getEndGame();
	}

	public void moveTowards(int[] target, Player p) {
		System.out.println(target[0] + " " + target[1]);
		System.out.println(p.getLocation()[0] + " " + p.getLocation()[1]);

		if(!gl.move(target[0], target[1])) {
			while(!gl.move(target[0], target[1])) {
				if(Math.abs(target[0] - p.getLocation()[0]) > Math.abs(target[1] - p.getLocation()[1])) {
					if(target[0] - p.getLocation()[0] > 0) {
						target[0] = target[0] - 1;
					} else {
						target[0] = target[0] + 1;
					}
				} else {
					if(target[1] - p.getLocation()[1] > 0) {
						target[1] = target[1] - 1;
					} else {
						target[1] = target[1] + 1;
					}
				}
			}
		}
	}

	public String getEventResult() {
		return eventResult;
	}

	public String isFight() {
		return description;
	}

	public void setGameLoop(GameLoop gl) {
		this.gl = gl;
		binaryMap = gl.getBinaryMap();
		ct = new ClosestTile(binaryMap, gl.getOriginalMap());
		players = gl.getPlayers();
		cp = new ClosestPlayer(players);
	}

	public void reset() {
		for(int i = 0; i < 3; i++) {
            botID[i] = -1;
        }
		botNumber = 0;
		phaseTwo = false;
	}
}
