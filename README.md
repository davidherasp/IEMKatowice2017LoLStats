# IEM Katowice 2017 LoL Stats
Java application to convert game stats into CSV and then read that stats from software like Spotfire or Tableau and visualize the data.

[![Video of the demo](http://img.youtube.com/vi/BSF6WKHXRO4/0.jpg)](https://www.youtube.com/watch?v=BSF6WKHXRO4)

## What you will find
You will find all the classes needed to generate CSV files with the following data:
* Player name
* Team name
* Country of the team
* Lane of the player
* Number of game/Date of creation
* KDA
* Kills
* Deaths
* Assists
* Gold earned 
* Minions killed AKA CS
* Wards placed
* Total damage dealt to champions

## Where does the data come from?
Well, since it is something that has been done in order to visualize some statistics of the european teams that participated in the IEM Katowice 2017 LoL tournament, the data comes from two sources. One is the last 10 games played per player and the other one is the games the teams played in LCS in spring split of 2017 before the tournament. 

The first one is data that is downloaded in the same moment you run the application so everytime you run it it might change and it might not work as well because the players may change their names in the servers. 

The second one is data that is collected from the URLs stored in the CSV files that you can find in the *csv* folder that's in the repo. That data is static and is the real data of the matches played from the 19/01/2017 to the 17/02/2017.

## What do I need?
You will need the Java classes, the CSV files and the following libraries:
* [Orianna API](https://github.com/meraki-analytics/Orianna)
* [Open CSV](http://opencsv.sourceforge.net/)
* [Javax JSON](https://repo1.maven.org/maven2/org/glassfish/javax.json/1.0.4/)
* [Joinery](https://github.com/cardillo/joinery)

## Thanks to
* Thanks to the guys at [elometer.info](http://elometer.info/) ([Twitter](https://twitter.com/elometer))! They helped me to get started and answered very fast to my questions. 10/10 persons. Visit their page to see the ELO ranking of the EU LCS, EU CS, SLO and more leagues!
* [Gamepedia](http://lol.gamepedia.com/League_of_Legends_Wiki) - Info about the players and teams nationalities.
* [OP.GG](https://euw.op.gg/ranking/ladder/) - Some of the summoner names of the players
