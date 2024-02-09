package service;

import dto.Cards;

import java.util.*;

public class RandomizeCards {

    public static ArrayList<ArrayList<Cards>> randomizeCardLocation1_7(List<Cards> cardsList){
        Random ran = new Random();
        ArrayList<ArrayList<Cards>> cardsIn1_7 = new ArrayList<>();

        for(int i=1;i<8;i++){
            ArrayList<Cards> postitionAtI = new ArrayList<>();
            for(int j=1;j<=i;j++){
                int cardCurrentPosition = ran.nextInt(cardsList.size());
                Cards tempCard = cardsList.remove(cardCurrentPosition);
                if(j==i){
                    tempCard.setOpen(true);
                }

                postitionAtI.add(tempCard);

            }
            cardsIn1_7.add(postitionAtI);
        }
        return cardsIn1_7;
    }

    public static List<Cards> randomizePile(List<Cards> cardsList, ArrayList<ArrayList<Cards>> cardsIn1_7){
        cardsList.removeAll(cardsIn1_7.stream().toList());
        for(Cards card: cardsList){
            card.setOpen(true);
        }
        // Shuffling the cards present in the pile
        Collections.shuffle(cardsList);
        return cardsList;
    }
}
