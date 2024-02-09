package service;

import dto.Cards;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShiftCardRules {

    private static int currentPilePos=0;
    private static int scorePlayer=0;

    public static int getCurrentPilePos() {
        return currentPilePos;
    }

    public static int getScorePlayer() { return scorePlayer; }


    private static void setScorePlayer(int scorePlayer, int points) {
        ShiftCardRules.scorePlayer = scorePlayer + points;
    }

    public static boolean checkRules1_7(Cards fromCard, Cards toCard){

        if(toCard==null && fromCard.getNumber().equals("K")){
            return true;
        } else if (toCard==null) {
            return false;
        }

        boolean result = false;
        switch (toCard.getNumber()){
            case "A"-> result = fromCard.getNumber().equals("2");
            case "J"-> result = fromCard.getNumber().equals("10");
            case "Q"-> result = fromCard.getNumber().equals("J");
            case "K"-> result = fromCard.getNumber().equals("Q");
            case "2","3","4","5","6","7","8","9","10" -> {
                if((Integer.parseInt(toCard.getNumber())-Integer.parseInt(fromCard.getNumber()))==1){
                    result = true;
                }
            }
        }

        if(result){
            if(toCard.getSymbol().equals(Cards.SPADE) || toCard.getSymbol().equals(Cards.CLUB)){
                return fromCard.getSymbol().equals(Cards.DIAMOND) || fromCard.getSymbol().equals(Cards.HEART);
            } else{
                return fromCard.getSymbol().equals(Cards.SPADE) || fromCard.getSymbol().equals(Cards.CLUB);
            }
        }
        return false;
    }

    public static boolean checkRulesDeck(Cards fromCard, Map<String, List<Cards>> cardsInDeck, String symbol){
        if (!symbol.equals(fromCard.getSymbol())) {
            return false;
        }
        Cards toCard = cardsInDeck.get(symbol).isEmpty()?null:cardsInDeck.get(symbol).getLast();
        if (toCard==null && fromCard.getNumber().equals("A")){
            return true;
        }else if(toCard!=null){
            boolean result = false;
            switch (toCard.getNumber()){
                case "A"-> result = fromCard.getNumber().equals("2");
                case "10"-> result = fromCard.getNumber().equals("J");
                case "J"-> result = fromCard.getNumber().equals("Q");
                case "Q"-> result = fromCard.getNumber().equals("K");
                case "2","3","4","5","6","7","8","9" -> {
                    if((Integer.parseInt(fromCard.getNumber())-Integer.parseInt(toCard.getNumber()))==1){
                        result = true;
                    }
                }
            }
            return result;
        }else{
            return false;
        }
    }

    public static ArrayList<ArrayList<Cards>> shiftCards1_7(ArrayList<ArrayList<Cards>> cardsIn1_7,
                                                            int fromCardsPos, int toCardsPos, int numberTobeShifted){
        ArrayList<Cards> fromCards = cardsIn1_7.get(fromCardsPos);
        ArrayList<Cards> toCards = cardsIn1_7.get(toCardsPos);

        if(fromCards.size()-numberTobeShifted<0){
            System.out.println("The number of cards selected to shift is not valid");
            return cardsIn1_7;
        }
        List<Cards> cardsToBeShifted = fromCards.subList(fromCards.size()-numberTobeShifted, fromCards.size());
        if(!cardsToBeShifted.getFirst().isOpen()){
            cardsToBeShifted = null;
        }
        else if(!checkRules1_7(cardsToBeShifted.get(0),toCards.isEmpty()?null:toCards.getLast())){
            cardsToBeShifted=null;
        }
        else {
            toCards.addAll(cardsToBeShifted);
            fromCards.removeAll(cardsToBeShifted);
            if(!fromCards.isEmpty()){
                Cards openLastCard = fromCards.getLast();
                openLastCard.setOpen(true);
                fromCards.set(fromCards.size()-1, openLastCard);
            }
            cardsIn1_7.set(fromCardsPos, fromCards);
            cardsIn1_7.set(toCardsPos, toCards);
            setScorePlayer(scorePlayer, 5);
        }

        if(cardsToBeShifted==null){
            System.out.println("Cards cant be shifted");
        }
        return cardsIn1_7;
    }

    public static void uncoverFromPile(int listSize){
        if(listSize==0){
            System.out.println("Empty Pile");
        }else {
            currentPilePos = ++currentPilePos % listSize;
        }
    }

    public static void removeFromPile(List<Cards> cardsList){
        currentPilePos--;
        if(currentPilePos<0 && !cardsList.isEmpty()){
            currentPilePos=0;
        }else if(currentPilePos<0){
            currentPilePos=-1;
        }
    }

    public static ArrayList<ArrayList<Cards>> shiftFromPile(ArrayList<ArrayList<Cards>> cardsIn1_7, List<Cards> cardsList,
                                                            int toCardsPos){
        Cards fromCard = cardsList.get(currentPilePos);
        ArrayList<Cards> toCards = cardsIn1_7.get(toCardsPos);
        if(toCards.isEmpty() || checkRules1_7(fromCard,toCards.getLast())){
            toCards.add(fromCard);
            cardsList.remove(currentPilePos);
            cardsIn1_7.set(toCardsPos,toCards);
            removeFromPile(cardsList);
        }
        return cardsIn1_7;
    }

    public static Cards shiftFromPileToDeck(Map<String, List<Cards>> cardsInDeck, List<Cards> cardsList, String symbol){
        if(currentPilePos<0){
            System.out.println("No card in Pile");
        }
        Cards selectedCard = cardsList.get(currentPilePos);
        System.out.println("Inside shiftFromPileToDeck");
        if(checkRulesDeck(selectedCard, cardsInDeck,symbol)){
            cardsList.remove(currentPilePos);
            setScorePlayer(scorePlayer, 10);
            return selectedCard;
        }else{
            System.out.println("Shifting the card to deck is not possible as it violets the rules");
        }
        return null;
    }

    public static ArrayList<ArrayList<Cards>> shiftFrom1_7ToDeck(ArrayList<ArrayList<Cards>> cardsIn1_7,
                                                                 int fromCardsPos, Map<String, List<Cards>> cardsInDeck, String symbol){
        System.out.println("Inside shiftFrom1_7ToDeck");
        if(cardsIn1_7.get(fromCardsPos).isEmpty()){
            System.out.println("Please select a valid card to shift to deck");
            return cardsIn1_7;
        }

        if(checkRulesDeck(cardsIn1_7.get(fromCardsPos).getLast(), cardsInDeck, symbol)){
            cardsInDeck.get(symbol).add(cardsIn1_7.get(fromCardsPos).removeLast());
            setScorePlayer(scorePlayer, 20);
            if(!cardsIn1_7.get(fromCardsPos).isEmpty())
                cardsIn1_7.get(fromCardsPos).getLast().setOpen(true);
        }else{
            System.out.println("Shifting the card to deck is not possible as it violets the rules");
        }
        return cardsIn1_7;
    }
}
