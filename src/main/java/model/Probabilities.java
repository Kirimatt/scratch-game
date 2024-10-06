package model;

import com.fasterxml.jackson.annotation.JsonProperty;
import model.BonusProbability;
import model.CellProbability;

import java.util.List;

public class Probabilities {
    @JsonProperty("standard_symbols")
    public List<CellProbability> standardSymbols;
    @JsonProperty("bonus_symbols")
    public BonusProbability bonusSymbols;
}