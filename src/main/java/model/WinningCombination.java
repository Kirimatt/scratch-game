package model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class WinningCombination {
    @JsonProperty("reward_multiplier")
    public Double rewardMultiplier;
    public String when;
    public Integer count;
    public String group;
    @JsonProperty("covered_areas")
    public List<List<String>> coveredAreas;
}
