# Basilisk  
Basilisk is a wifi based offline multiplayer snake game with point based scoring system as like any other traditional chase the food snake games. The main objective is to mimic the game play as that of Slither.io but based on offline wifi instead of online game.   

The current stable branch is the latest commit in branch ```dev``` and not the ```master``` branch.

This project is done as a part of ECE251 coursework under the guidance of professor Yogananda Isukapalli.

## Contributions:
### Gokul Prasath  
Worked on the Snake Game logic to check for collision and updating the snake display on the Surface View using Paint.  
Updated the fragments to handle message data from thread and to update the enemy snake buffer on each clients and server.  
Developed snake paint draw updates from multiple clients using alternate iterations of the timer thread.  
Created the serializable objects for transferring snake buffer information of each snake (client and snake) and modified the appropriate WiFi thread handlers.  

### Barath Kumar
Identified the initial issues with WiFi Direct option due to peer to peer only support on the most smartphones.  
Helped in setting up the initial Wi-Fi based base code for communicating with multiple clients.  
Participated in integrating the snake game logic with the Wi-Fi base code.  
Created the UI background, icons and initial look for the button and other components.  
Contributed to debugging and working with multiple threads and communicating with multiple clients.
