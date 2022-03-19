package events;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import commands.BasicCommands;
import demo.CommandDemo;
import structures.GameState;
import structures.basic.Card;
import structures.basic.Player;
import structures.basic.Round;
import structures.basic.Tile;
import structures.basic.Unit;
import structures.basic.UnitAnimationType;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

/**
 * Indicates that both the core game loop in the browser is starting, meaning
 * that it is ready to recieve commands from the back-end.
 * 
 * { 
 *   messageType = “initalize”
 * }
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class Initalize implements EventProcessor{

	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {
		Player humanPlayer = new Player(20, 0);
		BasicCommands.setPlayer1Health(out, humanPlayer);
		Player aiPlayer = new Player(20, 0);
		BasicCommands.setPlayer2Health(out, aiPlayer);
		int manaNums = 0;
		gameState.initialize(humanPlayer, aiPlayer);
		for (int m = 0; m < gameState.getTurnTimes(); m++) {
			humanPlayer.setMana(m);
			BasicCommands.setPlayer1Mana(out, humanPlayer);
			aiPlayer.setMana(m);
			BasicCommands.setPlayer2Mana(out, aiPlayer);
		}
//		Tile tile = BasicObjectBuilders.loadTile(2, 2);
		BasicCommands.drawTile(out, gameState.getGridTile()[2][2], 0);
		Unit unit = BasicObjectBuilders.loadUnit(StaticConfFiles.humanAvatar, 0, Unit.class);
		gameState.addTileUnit(unit);
		unit.setPositionByTile(gameState.getGridTile()[2][2]); 
//		int[] unitStates = {2,20};
		List<String> unitsStates = new ArrayList<>();
		unitsStates.add("2");
		unitsStates.add("20");
		gameState.addTurnTileUnits(unit, unitsStates);
		BasicCommands.drawUnit(out, unit, gameState.getGridTile()[2][2]);
		try {Thread.sleep(150);} catch (InterruptedException e) {e.printStackTrace();}
		BasicCommands.setUnitAttack(out, unit, 2);
		try {Thread.sleep(150);} catch (InterruptedException e) {e.printStackTrace();}
		BasicCommands.setUnitHealth(out, unit, 20);
		System.out.println("add unit id:"+gameState.getTileUnits().get(0).getId());
		try {Thread.sleep(100);} catch (InterruptedException e) {e.printStackTrace();}
		
//		Tile tile1 = BasicObjectBuilders.loadTile(6, 2);
		BasicCommands.drawTile(out, gameState.getGridTile()[6][2], 0);
		Unit unit1 = BasicObjectBuilders.loadUnit(StaticConfFiles.aiAvatar, 0, Unit.class);
//		gameState.addTileUnit(unit1);
		unit1.setPositionByTile(gameState.getGridTile()[6][2]); 
		
		gameState.addTileAIUnits(unit1); 
		List<String> unitAIStates = new ArrayList<>();
		unitAIStates.add("2");
		unitAIStates.add("20");
		gameState.addTurnTileAIUnits(unit1, unitAIStates);
		BasicCommands.drawUnit(out, unit1, gameState.getGridTile()[6][2]);
		try {Thread.sleep(150);} catch (InterruptedException e) {e.printStackTrace();}
		BasicCommands.setUnitAttack(out, unit1, 2);
		try {Thread.sleep(100);} catch (InterruptedException e) {e.printStackTrace();}
		BasicCommands.setUnitHealth(out, unit1, 20);
		try {Thread.sleep(100);} catch (InterruptedException e) {e.printStackTrace();}
		
		int cardIndex1 = (int)(Math.random()*gameState.getCards().size());
		String cardName1 = gameState.getCards().get(cardIndex1);
		Card cardInitia1 = new Card();
		if(cardName1.equals("truestrike") || cardName1.equals("sundrop_elixir")) {
			cardInitia1 = BasicObjectBuilders.loadCard("conf/gameconfs/cards/1_c_s_"+cardName1+".json", 0, Card.class);
		}else {
			cardInitia1 = BasicObjectBuilders.loadCard("conf/gameconfs/cards/1_c_u_"+cardName1+".json", 0, Card.class);
		}
		gameState.addHandCard(cardInitia1);
		BasicCommands.drawCard(out, cardInitia1, 1, 0);
		gameState.removeCard(gameState.getCards().get(cardIndex1));
		if(!(cardName1.equals("truestrike") || cardName1.equals("sundrop_elixir"))) {
			Unit unit3 = BasicObjectBuilders.loadUnit("conf/gameconfs/units/"+cardName1+".json", 0, Unit.class);
			gameState.addHandUnits(unit3);
		}
		try {Thread.sleep(100);} catch (InterruptedException e) {e.printStackTrace();}
		
		int cardIndex2 = (int)(Math.random()*gameState.getCards().size());
		String cardName2 = gameState.getCards().get(cardIndex2);
		Card cardInitia2 = new Card();
		if(cardName2.equals("truestrike") || cardName2.equals("sundrop_elixir")) {
			cardInitia2 = BasicObjectBuilders.loadCard("conf/gameconfs/cards/1_c_s_"+cardName2+".json", 0, Card.class);
		}else {
			cardInitia2 = BasicObjectBuilders.loadCard("conf/gameconfs/cards/1_c_u_"+cardName2+".json", 0, Card.class);
		}
		gameState.addHandCard(cardInitia2);
		BasicCommands.drawCard(out, cardInitia2, 2, 0);
		gameState.removeCard(gameState.getCards().get(cardIndex2));
		try {Thread.sleep(100);} catch (InterruptedException e) {e.printStackTrace();}
		if(!(cardName2.equals("truestrike") || cardName2.equals("sundrop_elixir"))) {
			Unit unit4 = BasicObjectBuilders.loadUnit("conf/gameconfs/units/"+cardName2+".json", 0, Unit.class);
			gameState.addHandUnits(unit4);
		}
		
		int cardIndex3 = (int)(Math.random()*gameState.getCards().size());
		String cardName3 = gameState.getCards().get(cardIndex3);
		Card cardInitia3 = new Card();
		if(cardName3.equals("truestrike") || cardName3.equals("sundrop_elixir")) {
			cardInitia3 = BasicObjectBuilders.loadCard("conf/gameconfs/cards/1_c_s_"+cardName3+".json", 0, Card.class);
		}else {
			cardInitia3 = BasicObjectBuilders.loadCard("conf/gameconfs/cards/1_c_u_"+cardName3+".json", 0, Card.class);
		}
		gameState.addHandCard(cardInitia3);
		BasicCommands.drawCard(out, cardInitia3, 3, 0);
		gameState.removeCard(gameState.getCards().get(cardIndex3));
		try {Thread.sleep(100);} catch (InterruptedException e) {e.printStackTrace();}
		if(!(cardName3.equals("truestrike") || cardName3.equals("sundrop_elixir"))) {
			Unit unit5 = BasicObjectBuilders.loadUnit("conf/gameconfs/units/"+cardName3+".json", 0, Unit.class);
			gameState.addHandUnits(unit5);
		}
		for(int i = 0; i < gameState.getHandCards().size(); i++) {
			gameState.addTurnCard(gameState.getHandCards().get(i));
		}
//		CommandDemo.executeDemo(out); // this executes the command demo, comment out this when implementing your solution
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
	}

}


