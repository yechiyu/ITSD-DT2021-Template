package events;

import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameState;
import structures.basic.Card;
import structures.basic.Unit;
import utils.BasicObjectBuilders;

/**
 * Indicates that the user has clicked an object on the game canvas, in this case
 * the end-turn button.
 * 
 * { 
 *   messageType = “endTurnClicked”
 * }
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class EndTurnClicked implements EventProcessor{

	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {
		if (gameState.getPlayers()[0].getMana() <= 9) {
			gameState.getPlayers()[0].setMana(gameState.getPlayers()[0].getMana()+1);
			BasicCommands.setPlayer1Mana(out, gameState.getPlayers()[0]);
		}
		if(gameState.getPlayers()[1].getMana() <= 9){
			gameState.getPlayers()[1].setMana(gameState.getPlayers()[1].getMana()+1);
			BasicCommands.setPlayer2Mana(out, gameState.getPlayers()[1]);
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
		
		for(int i = 0; i < gameState.getHandCards().size(); i++) {
			BasicCommands.drawCard(out, gameState.getHandCards().get(i), i+1, 0);
		}
		
		if (gameState.getHandCards().size() < 6) {

			System.out.println("hand card size:"+ gameState.getHandCards().size());
			int cardIndex1 = (int)(Math.random()*gameState.getCards().size());
			String cardName1 = gameState.getCards().get(cardIndex1);
			Card cardInitia1 = new Card();
			if(cardName1.equals("truestrike") || cardName1.equals("sundrop_elixir")) {
				cardInitia1 = BasicObjectBuilders.loadCard("conf/gameconfs/cards/1_c_s_"+cardName1+".json", 0, Card.class);
			}else {
				cardInitia1 = BasicObjectBuilders.loadCard("conf/gameconfs/cards/1_c_u_"+cardName1+".json", 0, Card.class);
			}
			gameState.addHandCard(cardInitia1);
			BasicCommands.drawCard(out, cardInitia1, gameState.getHandCards().size(), 0);
			gameState.removeCard(gameState.getCards().get(cardIndex1));
			if(!(cardName1.equals("truestrike") || cardName1.equals("sundrop_elixir"))) {
				Unit unit3 = BasicObjectBuilders.loadUnit("conf/gameconfs/units/"+cardName1+".json", 0, Unit.class);
				gameState.addHandUnits(unit3);
			}
			try {Thread.sleep(100);} catch (InterruptedException e) {e.printStackTrace();}
			gameState.getTurnCards().clear();
			for(int i = 0; i < gameState.getHandCards().size(); i++) {
				gameState.addTurnCard(gameState.getHandCards().get(i));
			}
		}
		
	}

}
