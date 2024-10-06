package model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

public class Config {

    public int columns;
    public int rows;
    public Map<String, SymbolConfig> symbols;
    public Probabilities probabilities;
    @JsonProperty("win_combinations")
    public Map<String, WinningCombination> winCombinations;
}
