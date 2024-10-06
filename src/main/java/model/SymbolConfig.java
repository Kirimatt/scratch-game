package model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SymbolConfig {
    @JsonProperty("reward_multiplier")
    public Double rewardMultiplier;
    public String type;
    public Double extra;
    public String impact;
}