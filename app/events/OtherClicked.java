package events;

import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameState;
import structures.basic.Tile;
import utils.BasicObjectBuilders;

/**
 * Indicates that the user has clicked an object on the game canvas, in this case
 * somewhere that is not on a card tile or the end-turn button.
 * 
 * { 
 *   messageType = “otherClicked”
 * }
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class OtherClicked implements EventProcessor{

	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {
		
		// drawTile
		BasicCommands.addPlayer1Notification(out, "drawTile[3,2]", 2);
		Tile tile = BasicObjectBuilders.loadTile(3, 2);
		BasicCommands.drawTile(out, tile, 0);
		try {Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}
	}

}


