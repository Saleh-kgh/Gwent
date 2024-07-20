#Gwent

Gwent is a card game from "The Witcher 3: Wild Hunt", which became the project for an AP course at Sharif University of Technology in 2024.

##Description

This card game is originally single-player, played with in-game NPCs only. However, I have made it an online multi-player game using Java sockets. The game itself, unlike most mini-games, is fully designed by CD Projekt Red designers and also has an online version completely different from the version inside "The Witcher 3: Wild Hunt". Every in-game mechanic is almost the same as the ones in the original card game, and it also contains every unit card and special card usable in the game. If it is hard for you to run the game with the code inside the repository, you can check the link below, which is another replica of the classic Gwent game:
https://www.arunsundaram.com/gwent-classic-app

As with every other AP project, this project was also developed by a team of 3, whose names are listed below. I developed most of the frontend, including game logic and graphics.

Saleh Tavili (me)
MohamadReza Javadi
Omid Shafaat

##Installation

1.Clone the repository
2.Navigate to the project directory
3.Run the Server main from src\main\java\controller\Server.java
4.Run two clients by running src\main\java\Main.java

You should also add Gson libraries to your project and add the com.sun.mail (a dependency in pom.xml) required library if you don't have it.
