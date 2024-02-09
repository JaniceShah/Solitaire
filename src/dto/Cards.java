package dto;
public class Cards {

    private String number;
    private String symbol;

    // To mark the card which is open in position between 1-7 cards stacked
    private boolean isOpen;
    public static String SPADE = "S";
    public static String CLUB = "C";
    public static String DIAMOND = "D";
    public static String HEART = "H";

    public Cards(String number, String symbol, boolean isOpen) {
        this.number = number;
        this.symbol = symbol;
        this.isOpen = isOpen;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    @Override
    public String toString() {
        if(isOpen) {
            return number + symbol;
        }else{
            return "*";
        }
    }
}
