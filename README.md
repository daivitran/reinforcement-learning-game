# shootingAI
This project is inspired from UC Berkeley's CS 61B second project. In this project, my friend (https://github.com/phuocdo1998) and I built from scratch a world generator, player agent and AI agent with reinforcement learning algorithm.

### Demo
![](game.gif)

### Dependency
  * Java 11
  
### Instruction 
  * Pull or Fork this repo in your local machine
  * In the repo that you just downloaded, open the terminal or command line and run ```java -jar shootingAI.jar```
  
### How to play the game
  * The purpose is to kill all AI agents
  * Launch the game
    * Press 'N' to play a new game, then enter a random number and press 'S'
    * Press 'L' to load a previous game if you have one
    * Press 'Q' to quit the game
  * During the game
    * Press 'W', 'A', 'S', 'D' to move up, left, down, right
    * Press 'K' to shoot
    * Enter ":" and immediately follow by "q" to save and quit the game
    
### A few features that I added to the AI bot (reinforcement learning) to reduce underfitting (trial and error method...)
   * Its current health (from 0 to 5)
   * Negative of its current health times 10 (Not sure why I did this lol)
   * Its current position
   * Distance to nearest bot
   * Average health of all other AI bots and you (the player agent)
   * Distance to the nearest bullet
   * Are there any bullets coming to the bot ?
   * For the reward function: I used a very simple one. I rewarded the bot every time it fires a bullet hitting the player.

### Resources
   * Jus 
   * https://inst.eecs.berkeley.edu/~cs188/sp20/assets/notes/n5.pdt
   * https://inst.eecs.berkeley.edu/~cs188/sp20/assets/notes/n5.pdf

  
  
