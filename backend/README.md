# Dungeon-Game
Start webapp with npm start command from webapp folder - instructions in webapp README.

Kick off backend from backend folder with command: mvn package && java -jar target/gs-spring-boot-0.1.0.jar



#Mac Install Instructions
1) cd ~/Documents/GitHub/Dungeon-Game (your directory for dungeon-game)
2) git pull to get latest version
3) cd backend/
4) brew install maven (first go to homebrew and install homebrew)
5) mvn package && java -jar target/gs-spring-boot-0.1.0.jar
6) if it does not auto-download maven, try mvn package
7) open a new terminal tab
8) cd ~/Documents/GitHub/Dungeon-Game/webapp
9) brew install npm
10) npm install
11) npm i stompjs
12) npm install --save react react-dom
13) npm install --save react-bootstrap
14) npm install --save react-tabs
15) npm start
16) ctrl + c to stop


#Mac Run Instructions
1) cd Dungeon-Game (your directory)
2) cd backend/
3) mvn package && java -jar target/gs-spring-boot-0.1.0.jar
4) new tab (cmd T)
5) cd ..
6) cd webapp/
7) npm start


backend = java files of player/map/etc
frontend = javascript/CSS formatting & rendering

#To stop
1) ctrl c on front or backend tab in terminal

So maven manages the backend, react (webapp) is the front end
