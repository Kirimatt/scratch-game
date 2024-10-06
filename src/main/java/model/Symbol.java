package model;

import model.SymbolConfig;

public class Symbol {
    public String name;
    public Double rewardMultiplier;
    public String type;
    public Double extra;
    public String impact;

    public Symbol(String name, SymbolConfig config) {
        this.name = name;
        this.rewardMultiplier = config.rewardMultiplier;
        this.type = config.type;
        this.extra = config.extra;
        this.impact = config.impact;
    }
}
