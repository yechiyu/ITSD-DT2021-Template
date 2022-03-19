package events;

import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameState;
import structures.basic.Tile;
import structures.basic.Unit;
import structures.basic.UnitAnimationType;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

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
		for(int i = 0; i < 5; i++) {
			outer1: for(int j = 0; j < 9; j++) {
				for(int k = 0; k < gameState.getTileUnits().size(); k++) {
					
					if (j == gameState.getTileUnits().get(k).getPosition().getTilex() && i == gameState.getTileUnits().get(k).getPosition().getTiley()) {
						continue outer1;
					}
				}
				BasicCommands.drawTile(out, gameState.getGridTile()[j][i], 0);
			}
			try {Thread.sleep(10);} catch (InterruptedException e) {e.printStackTrace();}
		}
		
		for(int i = 0; i < gameState.getHandCards().size(); i++) {
			BasicCommands.drawCard(out, gameState.getHandCards().get(i), i+1, 0);
		}
		System.out.println("system condition:"+gameState.getStateMark());
		
	}

}


