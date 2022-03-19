package events;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameState;
import structures.basic.Card;

/**
 * In the user’s browser, the game is running in an infinite loop, where there is around a 1 second delay 
 * between each loop. Its during each loop that the UI acts on the commands that have been sent to it. A 
 * heartbeat event is fired at the end of each loop iteration. As with all events this is received by the Game 
 * Actor, which you can use to trigger game logic.
 * 
 * { 
 *   String messageType = “heartbeat”
 * }
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class Heartbeat implements EventProcessor{

	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {
//		if (gameState.getStateMark() == 2 && gameState.getHandCardDeal() != 0) {
//			try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
//			if (gameState.getTurnCards().get(gameState.getHandCardDeal()-1).getCardname().equals(gameState.getSelectedCard().getCardname())) {
//				gameState.setHandCardDeal(gameState.getHandCardDeal()-1);
//				System.out.println("The card is deleted");
//				try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
//			} else {
//				System.out.println("delete posiition:"+gameState.getHandCardDeal());
//				BasicCommands.deleteCard(out, gameState.getHandCardDeal());
//				gameState.setHandCardDeal(gameState.getHandCardDeal()-1);
//				try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
//			}
////			int markTimes = 1;
////			for(int i = 0; i < gameState.getTurnCards().size(); i++) {
////				if(gameState.getTurnCards().get(i).getCardname() == gameState.getSelectedCard().getCardname()) {
////					markTimes++;
////					continue;
////				}
////				BasicCommands.deleteCard(out, markTimes);
////				gameState.setHandCardDeal(gameState.getHandCardDeal()-1);
////			}
////			System.out.println("delete position:"+gameState.getHandCardDeal());
////			try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
////			if (gameState.getTurnCards().get(gameState.getHandCardDeal()-1).getCardname() == gameState.getSelectedCard().getCardname()) {
////				gameState.setHandCardDeal(gameState.getHandCardDeal()-1);
////			}else {
////				System.out.println("to delete");
////				BasicCommands.deleteCard(out, gameState.getHandCardDeal());
////				gameState.setHandCardDeal(gameState.getHandCardDeal()-1);
////				try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
////			}
//			
//		}else if (gameState.getStateMark() == 2 && gameState.getHandCardDeal() == 0) {
//			gameState.setStateMark(3);
//		}else if(gameState.getStateMark() == 3){
//			for(int i = 0; i < gameState.getHandCards().size(); i++) {
//				BasicCommands.drawCard(out, gameState.getHandCards().get(i), i+1, 0);
//				try {Thread.sleep(100);} catch (InterruptedException e) {e.printStackTrace();}
//			}
//			gameState.setStateMark(0);
//		}
	}

}
