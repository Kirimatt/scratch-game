package service;

import model.CellProbability;
import model.Config;

import java.util.*;
import model.*;

public class GameMatrixService {
    private final int rows;
    private final int columns;
    private final Symbol[][] matrix;
    private final Config config;
    private final Map<String, Symbol> symbols;
    private final Random random = new Random();

    public GameMatrixService(Config config) {
        this.config = config;
        this.rows = config.rows;
        this.columns = config.columns;
        this.matrix = new Symbol[rows][columns];
        this.symbols = new HashMap<>();
        for (Map.Entry<String, SymbolConfig> entry : config.symbols.entrySet()) {
            symbols.put(entry.getKey(), new Symbol(entry.getKey(), entry.getValue()));
        }
    }

    public void generateMatrix() {
        // Generate standard symbols
        for (CellProbability cellProb : config.probabilities.standardSymbols) {
            int row = cellProb.row;
            int col = cellProb.column;
            matrix[row][col] = getRandomSymbol(cellProb.symbols, "standard");
        }
        // Place bonus symbols randomly
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (matrix[i][j] == null || matrix[i][j].type.equals("standard")) {
                    if (random.nextBoolean()) { // 50% chance to place a bonus symbol
                        Symbol bonusSymbol = getRandomSymbol(config.probabilities.bonusSymbols.symbols, "bonus");
                        matrix[i][j] = bonusSymbol;
                    }
                }
            }
        }
        // Fill any remaining cells with standard symbols
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (matrix[i][j] == null) {
                    CellProbability cellProb = findCellProbability(i, j);
                    matrix[i][j] = getRandomSymbol(cellProb.symbols, "standard");
                }
            }
        }
    }

    private CellProbability findCellProbability(int row, int col) {
        for (CellProbability cellProb : config.probabilities.standardSymbols) {
            if (cellProb.row == row && cellProb.column == col) {
                return cellProb;
            }
        }
        throw new IllegalArgumentException("No probability defined for cell (" + row + ", " + col + ")");
    }

    private Symbol getRandomSymbol(Map<String, Integer> symbolProbabilities, String type) {
        int totalWeight = symbolProbabilities.values().stream().mapToInt(Integer::intValue).sum();
        int randomInt = random.nextInt(totalWeight) + 1;
        int cumulativeWeight = 0;
        for (Map.Entry<String, Integer> entry : symbolProbabilities.entrySet()) {
            cumulativeWeight += entry.getValue();
            if (randomInt <= cumulativeWeight) {
                Symbol symbol = symbols.get(entry.getKey());
                if (symbol.type.equals(type)) {
                    return symbol;
                }
            }
        }
        // Should not reach here
        return null;
    }

    public Symbol[][] getMatrix() {
        return matrix;
    }
}
