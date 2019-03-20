/***********************************************
 *            gameController.java              *
 *                                             *
 *   Handles requests from the client.         *
 *   It takes requests at web addresses        *
 *   specified in GetMapping annotation        *
 *   gets data from the model to return        *
 *                                             *
 * ********************************************/
 
package controller;

import player.*;
import gameloop.*;
import bot.*;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import java.security.Principal;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;


import java.util.Timer;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;



//annotation sets up CORS support, Controller and /api root 
@Controller
@CrossOrigin
public class gameController {
    public int turnLength = 30; //in seconds

	Lobby lobby = new Lobby();
    GameLoop game = new GameLoop();
    BotController bot = new BotController();
    Timer timer = new Timer();

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

	// Start game gets called when lobby list is full
	@MessageMapping("/startGame")
	@SendTo("/topic/startGame")
	public int startGame() {
		ArrayList<String> players = lobby.getPlayerList();
        //ArrayList<String> spectators = lobby.getSpectatorList();
        game.setStarted();
		for (int i = 0; i < 4; i++) {
			game.addPlayer(players.get(i));
		}
        bot.setGameLoop(game);
		messagingTemplate.convertAndSend("/topic/location", game.getAllLocation());
		messagingTemplate.convertAndSend("/topic/client/turn", game.getPlayerTurn());
		messagingTemplate.convertAndSend("/topic/getPlayerStats", game.getPlayerStats());
        messagingTemplate.convertAndSend("/topic/getVisualMap", game.multiplyVisualMap());
        if(game.getVillainId() > -1) {
            System.out.println("Villain startGame is " + game.getVillainName()); 
            messagingTemplate.convertAndSend("/topic/getVillainName", game.getVillainName());
            messagingTemplate.convertAndSend("/topic/getVillainId", game.getVillainId());    
        }
        //timerFunc();
        return 1;
	}

	// Add user to lobby
    @SubscribeMapping("/topic/lobby/add")
    int addPlayerToLobby(Principal principal) {
		// try {
		// 	Thread.sleep(100);
		// } catch (InterruptedException e) {
		// 	//oops
		// }
		int playerId = lobby.addPlayer(principal.getName());
        messagingTemplate.convertAndSend("/topic/lobby/list", lobby.getPlayerList());
        messagingTemplate.convertAndSend("/topic/lobby/spectatorList", lobby.getSpectatorList());
        if (playerId < 0 && game.getHasStarted() == true) {
            startGame();
            messagingTemplate.convertAndSend("/topic/startGame", 1);
        }
        return playerId;
    }

        // Add user to lobby

    @MessageMapping("/addBot")
    @SendTo("/topic/addBot")
    void addBotToLobby() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            //oops
        }
        int inputBotID = lobby.getHighestPlayerNumber();
        String botName = bot.getBotName(inputBotID);
        int botID = lobby.addPlayer(botName);
        messagingTemplate.convertAndSend("/topic/lobby/list", lobby.getPlayerList());
        messagingTemplate.convertAndSend("/topic/lobby/spectatorList", lobby.getSpectatorList());
        if (botID < 0 && game.getHasStarted() == true) {
            startGame();
            messagingTemplate.convertAndSend("/topic/startGame", 1);
        }
    }

    //handles all location updates from clicks
    @MessageMapping("/sendLocation")
    @SendTo("/topic/location")
    public void validClick(String value, Principal principal) throws Exception {
        
		JSONObject temp = new JSONObject(value);
        JSONArray val = temp.getJSONArray("coord");
        int[] coord = {val.getInt(0), val.getInt(1)};
        if (movePlayer(principal, coord)) {
            messagingTemplate.convertAndSendToUser(principal.getName(), "/topic/event", game.getEventDescription());
		}	
    }

    void runNextTurn() {
        String[] hasWon = game.nextTurn();
        if (hasWon == null) {
            messagingTemplate.convertAndSend("/topic/getPlayerStats", game.getPlayerStats());
            messagingTemplate.convertAndSend("/topic/client/turn", game.getPlayerTurn());

            while(bot.check() && !game.getHasWon()) {
                messagingTemplate.convertAndSend("/topic/getVisualMap", game.multiplyVisualMap());
                messagingTemplate.convertAndSend("/topic/location", game.getAllLocation());
                if(bot.getEventResult() != null) {
                    messagingTemplate.convertAndSend("/topic/event", bot.getEventResult());
                }

                messagingTemplate.convertAndSend("/topic/client/turn", game.getPlayerTurn());  

                String fight = bot.isFight();
                if(fight != null) {
                    messagingTemplate.convertAndSend("/topic/event", fight);
                }
            }
        }      
            
        if (game.getHasWon()) {
            messagingTemplate.convertAndSend("/topic/endGame", game.nextTurn());
            game.reset();
			lobby.reset();
			bot.reset();
		}
    }

	//front end sends message when a player end its turn
    //this method jump to next turn and sends back the end game message
    @MessageMapping("/endTurn")
    @SendTo("/topic/client/turn")
    public void nextTurn() {
        // Timer myTimer = new Timer();
        // Thread thread1 = new Thread(myTimer, "thread1");
        // thread1.start();
        runNextTurn();
    }

    // class Timer implements Runnable {
    //     private volatile boolean exit = false; 

    //     public void run() { 
    //         long start = System.currentTimeMillis();
    //         long end = start + turnLength*1000;
    //         boolean isYourTurn = true;
    //         while(!exit) { 
    //             if (System.currentTimeMillis() > end) {
    //                     isYourTurn = false;
    //                     nextTurn();
    //             }
    //         } 

    //         System.out.println("Server is stopped...."); 
    //     } 

    //     public void stop(){ 
    //         exit = true; 
    //     }
    // }

    //void timerFunc() {
    //     Thread t1 = new Thread(new Runnable() {
    //         public void run() {
    //             long start = System.currentTimeMillis();
    //             long end = start + turnLength*1000;
    //             boolean isYourTurn = true;
    //             while(isYourTurn) {
    //                 if (System.currentTimeMillis() > end) {
    //                     isYourTurn = false;
    //                     nextTurn();
    //                 }
    //             }
    //         }
    //     });
    //     t1.start();
    // }

    @MessageMapping("/difficulty")
    @SendTo("/topic/changeDifficulty")
    public void changeDifficulty(String response) {
        JSONObject temp = new JSONObject(response);
        int difficulty = temp.getInt("localDiff");
        game.setDifficulty(difficulty);
        messagingTemplate.convertAndSend("/topic/changeDifficulty", difficulty);
    }

    @MessageMapping("/turnLength")
    @SendTo("/topic/changeTurnLength")
    public void changeTurnLength(String response) {
        JSONObject temp = new JSONObject(response);
        turnLength = temp.getInt("localTurnLength");
        System.out.println("length " + turnLength);
        messagingTemplate.convertAndSend("/topic/changeTurnLength", turnLength);
    }
    
    //front end sends message when phase two start
    //this method sends back the description of phase two
    @MessageMapping("/startPhaseTwo")
    @SendTo("/topic/phaseTwoResponse")
    public String startPhaseTwo() {
        String phaseTwoType = game.startPhaseTwo();
        System.out.println("Phase two");
        if(game.getVillainId() > -1) {
            System.out.println("Villain is " + game.getVillainName()); 
            messagingTemplate.convertAndSend("/topic/getVillainName", game.getVillainName());
            messagingTemplate.convertAndSend("/topic/getVillainId", game.getVillainId());    
        }
        messagingTemplate.convertAndSend("/topic/getPlayerStats", game.getPlayerStats());
        messagingTemplate.convertAndSend("/topic/getVisualMap", game.multiplyVisualMap());
        return phaseTwoType;
    }

    //Trigger event
    //Return event result to frontend.
    @MessageMapping("/event/response")
    @SendTo("/topic/event")
    public void eventResponse(String response) {

		JSONObject temp = new JSONObject(response);
        int diceRoll = temp.getInt("diceRoll");

		String eventResult = game.processEvent(diceRoll);

		messagingTemplate.convertAndSend("/topic/event", eventResult);
    }

	// Forward an attack to the defender so they can respond
	@MessageMapping("/attack")
	public void attackResponse( String response) {
		
		// Parse response
		JSONObject temp = new JSONObject(response); 
        String playerName = temp.getString("playerName"); //defending playerName
        String tempString = temp.getString("attackerPlayerName"); //attacking playerName
		String attackerPlayerName = tempString.substring(1, tempString.length()-1); //remove ""

        int diceRoll = temp.getInt("diceRoll");

		//Generate dice roll for defending player - simulate attack
		Random rd = new Random();
		String description = game.attack(playerName, diceRoll, rd.nextInt(6), attackerPlayerName);
        System.out.println(description);
		
		//send result to to user in pop up and event to append to event history
        messagingTemplate.convertAndSend("/topic/getPlayerStats", game.getPlayerStats());
		messagingTemplate.convertAndSendToUser(playerName, "/topic/attack", description);
		messagingTemplate.convertAndSend("/topic/event", description);
        messagingTemplate.convertAndSend("/topic/location", game.getAllLocation());
        messagingTemplate.convertAndSend("/topic/getVisualMap", game.multiplyVisualMap());
        
	}

    public boolean movePlayer(Principal principal, int[] coord) {
		System.out.println(coord[0] + " " +  coord[1]);
		if (game.move(coord[0], coord[1])) {
            int[][] playerLocations = game.getAllLocation();
            messagingTemplate.convertAndSend("/topic/getVisualMap", game.multiplyVisualMap());
	    	messagingTemplate.convertAndSend("/topic/location", playerLocations);
            messagingTemplate.convertAndSend("/topic/getPlayerStats", game.getPlayerStats());
            return true;
        } 
        else {
            messagingTemplate.convertAndSendToUser(principal.getName(), "/topic/error", "Invalid Move");
            int[][] outOfBound = {{99,99},{99,99},{99,99},{99,99}};
            messagingTemplate.convertAndSend("/topic/location", outOfBound);
	    	return false;
        }
    }

    void updateMap() {
	   messagingTemplate.convertAndSend("/topic/getVisualMap", game.multiplyVisualMap());
	}

	@MessageMapping("/ping")
    public void ping(String response) {
		//accept ping to keep WS open
    }

}
