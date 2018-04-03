# Oware Game Playing A.I.

## Description
Oware is an ashanti abstract strategy game among the Mancala family, played worldwide with slight variations as to the layout of the game, number of players and strategy of play (Wikipedia).  

The **game-playing A.I.** is programmed using **optimized mini-max** or **alpha-beta pruning** method.  

The default **depth** of the A.I is **15** which can be lowered to decrease the difficulty or increased to increased the difficulty.  
Depth here means the extent to which the alpha-beta pruning recursives to predict the main player's best choice of action. 

## Compilation
You can compile on the command prompt using this command java Awari Humanplayer AI setup.txt \[maxDepth\] , where maxDepth is an integer.
Recommended command:  
a) java Awari AI AI setup.txt 15 (AI VS AI).  
b) java awari HumanPlayer AI setup.txt 15 (Player vs AI)  

**setup.txt** contains the initial board configuration. Can be changed to check how dynamic the AI is under different situations.

## Rules 
(Modified from http://www.joansala.com/auale/rules/en/)

#### Equipment and Initial Setup
To play oware you will need a game board and forty-eight game pieces, which are so-called seeds (stones in this program). Usually the board consists of two rows of six holes located at opposite sides. Two larger holes on the sides of the board are used to store the stones players capture during the match. It is said that the bottom row belongs to the player who moves first, named south, and the upper row to the second player or north. Each of the playing holes contains exactly four balls.

#### Goal of the game
The goal of oware is to capture the greatest amount of stones as possible. To do so, players make moves in alternate turns until one of them has captured more than 24 seeds. The player who captured more seeds than his opponent when the game ends wins the match. It may also happen that both players have captured the same amount of seeds at the end. In this case, neither player wins the game and the match is said to have ended in a draw.

#### Playing the game
Each move of the game is done in three phases: collecting, sowing and capturing. During the sowing a player distributes the collected stones along the board, and during the capture phase the player takes, if possible, the stones found in the pits of the opponent.

###### Collecting Seeds(Stones)
In the first phase of a movement, the player who is to move chooses one of the holes on his own side of the board and collects all the seeds on it, leaving the hole empty. Subsequently, these stone will be distributed on the board during the sowing phase.

A player may collect the stones from any of the holes that belong to him if it contains one or more seeds, only with the exception that after making the move his opponent must be able to play. Therefore, a move that would leave all the holes empty on the opponent's side is not legal.

###### Sowing Seeds(Stones)
During sowing, the player distributes the stones collected in the first phase along the board in a counterclockwise direction; dropping one stone in each of the playing holes until all the seeds are distributed. A player will never sow on the holes used for storage.  

After sowing the stones, the hole from which the player has collected seeds will be empty. It may well be the case that the player sows twelve or more stones, in which case the player will sow them going round around the board, dropping one stone in each hole in every round, but never dropping a stone in the hole from which the seeds were collected.

###### Capturing Seeds(Stones)
When the last sown stone is dropped in one of the holes belonging to the opponent, and after dropping the stone the hole contains exactly two or three stones, the player will capture them. Taking all the stones from the hole and saving them in his own store.  

When the hole immediately to the right of the last pit from which seeds were captured contains also two or three stones, the player will capture them too. And so on until the player cannot capture more stones, always taking into account that players can only capture stones from their opponents holes and never from their own holes.  

Note that a player can never capture all the seeds of the adversary. If a player makes a move that would capture all the stones on the opponent's side, that player will sow normally but will not capture any stones.  

#### End of the Game
Typically, the game is over when one of the players has captured more than 24 seeds or when both of the players have captured 24 seeds. It may also happen that a player cannot make any legal move on his turn, in such a case, each player captures the remaining seeds on their side of the board and the match ends.
