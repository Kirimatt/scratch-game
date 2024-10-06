package service;

import model.Config;
import model.Symbol;
import model.WinningCombination;

import java.util.*;

public class GameResultService {
    private final Symbol[][] matrix;
    private final double betAmount;
    private final Config config;
    private final Map<String, List<String>> appliedWinningCombinations = new HashMap<>();
    private String appliedBonusSymbol = null;
    private double reward = 0.0;
    private final Map<String, Double> symbolRewards = new HashMap<>();
    private final Map<String, Double> winningCombinationMultipliers = new HashMap<>();
    private final Map<String, String> bonusSymbols = new HashMap<>();

    public GameResultService(Symbol[][] matrix, double betAmount, Config config) {
        this.matrix = matrix;
        this.betAmount = betAmount;
        this.config = config;
    }

    public void calculateReward() {
        Map<String, Integer> symbolCounts = new HashMap<>();
        // Count standard symbols
        for (Symbol[] row : matrix) {
            for (Symbol symbol : row) {
                if (symbol.type.equals("standard")) {
                    symbolCounts.put(symbol.name, symbolCounts.getOrDefault(symbol.name, 0) + 1);
                } else if (symbol.type.equals("bonus")) {
                    bonusSymbols.put(symbol.name, symbol.name);
                }
            }
        }
        // Apply winning combinations
        for (Map.Entry<String, Integer> entry : symbolCounts.entrySet()) {
            String symbolName = entry.getKey();
            int count = entry.getValue();
            List<String> appliedCombinations = new ArrayList<>();
            double symbolReward = betAmount * config.symbols.get(symbolName).rewardMultiplier;
            double combinationMultiplier = 1.0;
            for (Map.Entry<String, WinningCombination> winEntry : config.winCombinations.entrySet()) {
                WinningCombination wc = winEntry.getValue();
                if (wc.when.equals("same_symbols") && count >= wc.count) {
                    combinationMultiplier *= wc.rewardMultiplier;
                    appliedCombinations.add(winEntry.getKey());
                } else if (wc.when.equals("linear_symbols")) {
                    for (List<String> area : wc.coveredAreas) {
                        boolean matches = true;
                        for (String pos : area) {
                            String[] indices = pos.split(":");
                            int row = Integer.parseInt(indices[0]);
                            int col = Integer.parseInt(indices[1]);
                            if (!matrix[row][col].name.equals(symbolName)) {
                                matches = false;
                                break;
                            }
                        }
                        if (matches) {
                            combinationMultiplier *= wc.rewardMultiplier;
                            appliedCombinations.add(winEntry.getKey());
                            break;
                        }
                    }
                }
            }
            if (!appliedCombinations.isEmpty()) {
                symbolRewards.put(symbolName, symbolReward);
                winningCombinationMultipliers.put(symbolName, combinationMultiplier);
                appliedWinningCombinations.put(symbolName, appliedCombinations);
            }
        }
        // Calculate total reward
        for (String symbolName : symbolRewards.keySet()) {
            reward += symbolRewards.get(symbolName) * winningCombinationMultipliers.get(symbolName);
        }
        // Apply bonus symbol if any
        if (!bonusSymbols.isEmpty() && reward > 0) {
            for (String bonusSymbolName : bonusSymbols.keySet()) {
                Symbol bonusSymbol = new Symbol(bonusSymbolName, config.symbols.get(bonusSymbolName));
                appliedBonusSymbol = bonusSymbolName;
                if (bonusSymbol.impact.equals("multiply_reward")) {
                    reward *= bonusSymbol.rewardMultiplier;
                } else if (bonusSymbol.impact.equals("extra_bonus")) {
                    reward += bonusSymbol.extra;
                }
                break; // Only one bonus symbol is applied
            }
        }
    }

    public Map<String, Object> toResultMap() {
        Map<String, Object> result = new LinkedHashMap<>();
        List<List<String>> matrixStrings = new ArrayList<>();
        for (Symbol[] row : matrix) {
            List<String> rowStrings = new ArrayList<>();
            for (Symbol symbol : row) {
                rowStrings.add(symbol.name);
            }
            matrixStrings.add(rowStrings);
        }
        result.put("matrix", matrixStrings);
        result.put("reward", (int) reward);
        if (!appliedWinningCombinations.isEmpty()) {
            result.put("applied_winning_combinations", appliedWinningCombinations);
        }
        result.put("applied_bonus_symbol", appliedBonusSymbol != null ? appliedBonusSymbol : null);
        return result;
    }
}
