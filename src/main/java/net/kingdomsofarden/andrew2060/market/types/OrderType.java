package net.kingdomsofarden.andrew2060.market.types;

public enum OrderType {
    
    BUY("Buy"),
    SELL("Sell"),
    AUCTION("Auction");
    
    String name;
    
    OrderType(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        return this.name;
    }
    
    public static OrderType fromString(String name) {
        for(OrderType oT : OrderType.values()) {
            if(oT.toString().equals(name)) {
                return oT;
            }
        }
        return null;
    }
    
}
