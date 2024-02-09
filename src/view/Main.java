package view;

import dto.Cards;
import service.RandomizeCards;
import service.ShiftCardRules;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        // Set all the cards
        List<Cards> cardsList = new LinkedList<>();
        List<String> cardSymbols = Arrays.asList(Cards.SPADE, Cards.CLUB, Cards.DIAMOND, Cards.HEART);

        Map<String, List<Cards>> cardsInDeck=new HashMap<>();
        cardsInDeck.put(Cards.CLUB, new ArrayList<>());
        cardsInDeck.put(Cards.DIAMOND, new ArrayList<>());
        cardsInDeck.put(Cards.HEART, new ArrayList<>());
        cardsInDeck.put(Cards.SPADE, new ArrayList<>());

        // Storing cards from 1-7 in the 2D arrayList
        ArrayList<ArrayList<Cards>> cardsIn1_7;

        for(String cardSymbol: cardSymbols){
            for(int i=1;i<14;i++){
                switch (i) {
                    case 1-> cardsList.add(new Cards("A",cardSymbol,false));
                    case 11-> cardsList.add(new Cards("J",cardSymbol,false));
                    case 12-> cardsList.add(new Cards("Q",cardSymbol,false));
                    case 13-> cardsList.add(new Cards("K",cardSymbol,false));
                    default -> cardsList.add(new Cards(Integer.toString(i),cardSymbol,false));
                }
            }
        }

        cardsIn1_7 = RandomizeCards.randomizeCardLocation1_7(cardsList);
        List<Cards> cardsInPile = RandomizeCards.randomizePile(cardsList, cardsIn1_7);

        // Since the variable is no longer in use we will mark it as null for garbage collection.
        cardsList = null;

        Scanner sc = new Scanner(System.in);
        printCardsEachStep(cardsInPile, cardsIn1_7, ShiftCardRules.getCurrentPilePos(), cardsInDeck);
        String command = "";
        while(!command.equals("Q")){
            System.out.println("Please enter the commands: Q if quitting, D for drawing from pile, others to play the game ");
            command = sc.nextLine();
            switch (command){
                case "Q"->System.out.println("Quitting the Game");
                case "D"->ShiftCardRules.uncoverFromPile(cardsInPile.size());
                default ->{
                    if(command.length()>1 && command.length()<=4){
                        // When user types P1 it will indicate to shift from the Pile to the location
                        if(command.charAt(0)=='P'){
                            String toCommand = String.valueOf(command.charAt(1));
                            //If the user wants to shift from the Pile directly to Suite
                            // We are checking if the suite symbol added is valid
                            if(cardSymbols.contains(toCommand)){
                                Cards removeCard = ShiftCardRules.shiftFromPileToDeck(cardsInDeck, cardsInPile, toCommand);
                                if(removeCard !=null){
                                    cardsInDeck.get(toCommand).add(removeCard);
                                    cardsInPile.remove(removeCard);
                                }
                            }
                            // If not shifting to suite we will check if its being shifted to lanes
                            else{
                                try{
                                    // If the second character is not a number the exception will be caught and error will be shown
                                    int toPos = Integer.parseInt(toCommand)-1;
                                    if(toPos>6){
                                        System.out.println("Please put a valid command");
                                        continue;
                                    }
                                    cardsIn1_7 = ShiftCardRules.shiftFromPile(cardsIn1_7, cardsInPile, toPos);
                                }catch (NumberFormatException e){
                                    System.out.println("Please put a valid command");
                                }
                            }
                        }
                        // If its not being shifted from the pile i.e. from the lanes
                        else {
                            // The first charcter must be a number from which we want to move card
                            String fromCommand = String.valueOf(command.charAt(0));
                            int fromPos, toPos, numberOfCards;
                            try{
                                fromPos = Integer.parseInt(fromCommand)-1;
                                // There are only 7 lanes we wont allow user to add any other number greater than that
                                if(fromPos>6){
                                    System.out.println("Please put a valid command");
                                    continue;
                                }
                            }catch (NumberFormatException e){
                                System.out.print("Please put a valid command");
                                continue;
                            }
                            String toCommand = String.valueOf(command.charAt(1));
                            try{
                                // Checking if the second position is a number
                                toPos = Integer.parseInt(toCommand)-1;
                                if(toPos>6){
                                    System.out.println("Please put a valid command");
                                    continue;
                                }
                                if(command.length()==4){
                                    // If it is we will get the number of cards to be shifted which can be greater than 10
                                    try{
                                        numberOfCards = Integer.parseInt(String.valueOf(command.charAt(2)))*10+
                                                Integer.parseInt(String.valueOf(command.charAt(3)));
                                    }catch (NumberFormatException e){
                                        System.out.println("Please put a valid command");
                                        continue;
                                    }
                                }else if(command.length()==3){
                                    try{
                                        numberOfCards = Integer.parseInt(String.valueOf(command.charAt(2)));
                                    }catch (NumberFormatException e){
                                        System.out.println("Please put a valid command");
                                        continue;
                                    }
                                }else{
                                    // If the number of cards to be shifted is not defined we will consider the default to be 1
                                    numberOfCards = 1;
                                }
                                cardsIn1_7 = ShiftCardRules.shiftCards1_7(cardsIn1_7,fromPos,toPos,numberOfCards);
                            }catch (NumberFormatException e){
                                //If the second character is not a number we will check if it's the symbol to be shifted to suite
                                if(cardSymbols.contains(toCommand)){
                                    cardsIn1_7 = ShiftCardRules.shiftFrom1_7ToDeck(cardsIn1_7, fromPos, cardsInDeck, toCommand);
                                }else{
                                    System.out.println("Please put a valid command");
                                    continue;
                                }
                            }
                        }
                    } else{
                        System.out.println("Please put a valid input");
                    }
                }
            }
            if(!command.equals("Q")){
                printCardsEachStep(cardsInPile, cardsIn1_7, ShiftCardRules.getCurrentPilePos(), cardsInDeck);
            } if((cardsInDeck.get(Cards.SPADE).size()+cardsInDeck.get(Cards.DIAMOND).size()+cardsInDeck.get(Cards.CLUB).size()
                +cardsInDeck.get(Cards.HEART).size())==52){
                // Checking if all cards have shifted.
                System.out.println("Game Over!!!");
                break;
            }
        }
    }

    static void printCardsEachStep(List<Cards> cardsList, List<ArrayList<Cards>> cardsIn1_7,
                                   int currentPilePos, Map<String, List<Cards>> cardsInDeck){
        //Printing the cards in the lane
        for (int j = 0; j < 7; j++) {
            System.out.println(cardsIn1_7.get(j));
        }
        // Printing the card in pile
        if(currentPilePos>=0) {
            System.out.println("The top card from the pile is: " + cardsList.get(currentPilePos).getNumber() +
                    cardsList.get(currentPilePos).getSymbol());
        }

        // Printing the score
        System.out.println("The current score is: "+ShiftCardRules.getScorePlayer());

        // Printing the cards in suite
        System.out.println(cardsInDeck);
    }


}