package events;

import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameState;
import structures.basic.Card;
import structures.basic.Tile;
import structures.basic.Unit;
import utils.BasicObjectBuilders;
import java.util.Arrays;


/**
 * Indicates that the user has clicked an object on the game canvas, in this case a card.
 * The event returns the position in the player's hand the card resides within.
 * 
 * { 
 *   messageType = “cardClicked”
 *   position = <hand index position [1-6]>
 * }
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class CardClicked implements EventProcessor{

	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {
		
		int handPosition = message.get("position").asInt();
		
		if (gameState.getStateMark() == 1) {
			for(int i = 0; i < gameState.getHandCards().size(); i++) {
				BasicCommands.drawCard(out, gameState.getTurnCards().get(i), i+1, 0);
			}
			
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
			
			for(int k = 0; k < gameState.getTileUnits().size(); k++) {

				for(int i = gameState.getTileUnits().get(k).getPosition().getTiley()-2; i <= gameState.getTileUnits().get(k).getPosition().getTiley()+2; i++) {
					if (i < 0 || i > 4) {
						continue;
					}
					outer1: for(int j = gameState.getTileUnits().get(k).getPosition().getTilex()-2; j <= gameState.getTileUnits().get(k).getPosition().getTilex()+2; j++) {
						if (j < 0 || j > 8) {
							continue outer1;
						}
						if ((i == gameState.getTileUnits().get(k).getPosition().getTiley() || j == gameState.getTileUnits().get(k).getPosition().getTilex()) || (Math.abs(gameState.getTileUnits().get(k).getPosition().getTiley()-i) == 1 && Math.abs(gameState.getTileUnits().get(k).getPosition().getTilex()-j) == 1)) {
							for(int l = 0; l < gameState.getTileUnits().size(); l++) {
								
								if (j == gameState.getTileUnits().get(l).getPosition().getTilex() && i == gameState.getTileUnits().get(l).getPosition().getTiley()) {
									continue outer1;
								}
							}
							
							BasicCommands.drawTile(out, gameState.getGridTile()[j][i], 1);
						}
					}
					try {Thread.sleep(10);} catch (InterruptedException e) {e.printStackTrace();}
				}
			}
		}else if (gameState.getStateMark() == 0) {
			selectCard(out, gameState, message, handPosition);
		} else if (gameState.getStateMark() == 2) {
			for(int i = 0; i < gameState.getHandCards().size(); i++) {
				BasicCommands.drawCard(out, gameState.getHandCards().get(i), i+1, 0);
			}
			selectCard(out, gameState, message, handPosition);
		} else {
			
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
			for(int k = 0; k < gameState.getTileUnits().size(); k++) {

				for(int i = gameState.getTileUnits().get(k).getPosition().getTiley()-2; i <= gameState.getTileUnits().get(k).getPosition().getTiley()+2; i++) {
					if (i < 0 || i > 4) {
						continue;
					}
					outer1: for(int j = gameState.getTileUnits().get(k).getPosition().getTilex()-2; j <= gameState.getTileUnits().get(k).getPosition().getTilex()+2; j++) {
						if (j < 0 || j > 8) {
							continue outer1;
						}
						if ((i == gameState.getTileUnits().get(k).getPosition().getTiley() || j == gameState.getTileUnits().get(k).getPosition().getTilex()) || (Math.abs(gameState.getTileUnits().get(k).getPosition().getTiley()-i) == 1 && Math.abs(gameState.getTileUnits().get(k).getPosition().getTilex()-j) == 1)) {
							for(int l = 0; l < gameState.getTileUnits().size(); l++) {
								
								if (j == gameState.getTileUnits().get(l).getPosition().getTilex() && i == gameState.getTileUnits().get(l).getPosition().getTiley()) {
									continue outer1;
								}
							}
							
							BasicCommands.drawTile(out, gameState.getGridTile()[j][i], 1);
						}
					}
					try {Thread.sleep(10);} catch (InterruptedException e) {e.printStackTrace();}
				}
			}
			
			for(int i = 0; i < gameState.getHandCards().size(); i++) {
				BasicCommands.drawCard(out, gameState.getHandCards().get(i), i+1, 0);
			}
			
			selectCard(out, gameState, message, handPosition);
		}
		
	}
	
	public void selectCard(ActorRef out, GameState gameState, JsonNode message, int handPosition) {

		BasicCommands.drawCard(out, gameState.getTurnCards().get(handPosition-1), handPosition, 1);
		
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
		
		for(int k = 0; k < gameState.getTileUnits().size(); k++) {

			for(int i = gameState.getTileUnits().get(k).getPosition().getTiley()-2; i <= gameState.getTileUnits().get(k).getPosition().getTiley()+2; i++) {
				if (i < 0 || i > 4) {
					continue;
				}
				outer1: for(int j = gameState.getTileUnits().get(k).getPosition().getTilex()-2; j <= gameState.getTileUnits().get(k).getPosition().getTilex()+2; j++) {
					if (j < 0 || j > 8) {
						continue outer1;
					}
					if ((i == gameState.getTileUnits().get(k).getPosition().getTiley() || j == gameState.getTileUnits().get(k).getPosition().getTilex()) || (Math.abs(gameState.getTileUnits().get(k).getPosition().getTiley()-i) == 1 && Math.abs(gameState.getTileUnits().get(k).getPosition().getTilex()-j) == 1)) {
						for(int l = 0; l < gameState.getTileUnits().size(); l++) {
							
							if (j == gameState.getTileUnits().get(l).getPosition().getTilex() && i == gameState.getTileUnits().get(l).getPosition().getTiley()) {
								continue outer1;
							}
						}
						
						BasicCommands.drawTile(out, gameState.getGridTile()[j][i], 1);
					}
				}
				try {Thread.sleep(10);} catch (InterruptedException e) {e.printStackTrace();}
			}
		}
		gameState.setStateMark(1);

		
		if (gameState.getHandUnits().size() > 0) {
			int[] markUnitNum = new int[6];
			Arrays.fill(markUnitNum, 1);
			for(int i = 0; i < gameState.getTurnCards().size(); i++) {
				if (gameState.getTurnCards().get(i).getCardname().equals("Sundrop Elixir") || gameState.getTurnCards().get(i).getCardname().equals("Truestrike")) {
					markUnitNum[i] = 0;
				}
			}
			if (!(gameState.getTurnCards().get(handPosition-1).getCardname().equals("Truestrike") || gameState.getTurnCards().get(handPosition-1).getCardname().equals("Sundrop Elixir"))) {
				System.out.println("cardName:"+gameState.getTurnCards().get(handPosition-1).getCardname());
				if (markUnitNum[handPosition-1] == 1) {
					int handUnitNum = 0;
					for(int i = 0; i < handPosition; i++) {
						if (markUnitNum[i] == 1) {
							handUnitNum++;
						}
					}
					gameState.setSelectedUnit(gameState.getHandUnits().get(handUnitNum-1));
					gameState.setSelectedCard(gameState.getTurnCards().get(handPosition-1));
					gameState.setSelectPosition(handPosition);
					gameState.setStateMark(2);
				}
			}
		}
	}

}
