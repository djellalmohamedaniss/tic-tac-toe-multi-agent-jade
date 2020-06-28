# TicTacToeMultiAgent
tic-tac-toe game implementation using Java agent developement kit.

for now, the project contains 2 types of agents, a RefereeAgent that manages the game, and a SmartPlayerAgent that gets the game state from the referee and chooses his next move. the SmartPlayerAgent uses the minmax algorithm to get the best move.

both agents where implemented with a  FSMbehaviour. 

the following **diagram** resumes the full execution, different states of agents and the messages sent where the red box represents the refereeAgent, while the blue one represents a instance of SmartPlayerAgent.


![Untitled Diagram(1)](https://user-images.githubusercontent.com/24865594/85948058-d035dd00-b94e-11ea-90c0-7ae3ddc7f6b0.png)

## Future features:

- [ ] add a "RandomPlayAgent" to the project.
- [ ] add a better game display.
- [ ] possibility of playing against the agent.


