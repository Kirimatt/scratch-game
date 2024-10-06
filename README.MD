Please use this command for running the game from target directory


`java -jar .\scratch-game-1.0-SNAPSHOT-jar-with-dependencies.jar --config .\config_with_4_cells.json --betting-amount 100`


following output:

```
{
"matrix" : [ [ "E", "E", "MISS", "C" ], [ "C", "MISS", "C", "D" ], [ "C", "5x", "D", "D" ], [ "C", "B", "MISS", "+500" ] ],
"reward" : 4750,
"applied_winning_combinations" : {
"C" : [ "same_symbol_3_times", "same_symbol_4_times", "same_symbol_5_times" ],
"D" : [ "same_symbol_3_times" ]
},
"applied_bonus_symbol" : "5x"
}
```

for 3 cells:

`java -jar .\scratch-game-1.0-SNAPSHOT-jar-with-dependencies.jar --config config.json --betting-amount 100`


following output:

```
{
"matrix" : [ [ "+1000", "MISS", "A" ], [ "+1000", "B", "MISS" ], [ "+1000", "A", "F" ] ],
"reward" : 0,
"applied_bonus_symbol" : null
}
```