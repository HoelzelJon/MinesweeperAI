# Minesweeper AI

The goal of this project is to make a good AI for the game Minesweeper.

## Running the program
After running the `Main::main` method, you will be prompted with some configuration choices:
- Board Difficulty:
    - EASY: 9x9, 10 mines
    - MED: 16x16, 40 mines
    - HARD: 30x16, 99 mines,
    - EXTREME: 30x24, 180 mines
- Print Mode:
    - ALWAYS: Print the board every time an action is taken
    - THINK: Print out the board each time the AI has to think of a new set of actions to perform
    - NONE: Don't print out the board at all
- Backup Player: What to do when the AI can no longer confidently make a decision what to do
    - HUMAN: Prompt the user for a decision (described below)
    - UNIFORM: Choose a unknown cell uniformly at random
    - GROUP / AVERAGE: described in more detail in `RandomAI.java`
- Delay (int): milliseconds to wait between each of the AI's moves
- Iterations (int): number of games to play

### The Board
The board is currently printed out to the command line. Here is an example board:
```
Mines Remaining: 6
         1  .  .  .  .  .  
         1  .  .  .  .  .  
         1  2  .  .  .  .  
1  1  1     1  .  .  .  .  
1  |  1     1  .  .  .  .  
1  1  1  1  2  .  .  .  .  
         1  |  .  .  .  .  
   1  1  3  2  .  .  .  .  
   1  |  2  |  .  .  .  .  
```
- A number in a cell means that that cell has been clicked, and did not have a mine.
In this case, the number indicates the number of neighboring cells that have mines in them (including diagonals).
- A blank cell is basically the same as a `0`
- A `.` means that a cell has not been clicked and is not flagged
- A `|` means a cell has been flagged as having a mine

### User-Prompted Action
- First, you will be given the option to do a random click
- If this is not chosen, you will choose between doing a Click(C) or a Flag(F) action
- Then, you provide the x and y coordinates of the cell to click/flag.
Cells are zero-indexed, with (0,0) being the bottom-left of the board

# Future Extensions
- Better probabilistic methods
- Figuring out what part of the board is best to click initially
- Adding graphics (and interactivity?)