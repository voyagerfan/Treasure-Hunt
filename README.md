# Treasure-Hunt
Welcome to my mobile application!

## Table of Contents


- [Treasure-Hunt](#treasure-hunt)
  - [Table of Contents](#table-of-contents)
  - [Overview](#overview)
  - [Features](#features)
  - [Screenshots](#screenshots)
    - [Location permission Screen](#location-permission-screen)
    - [Rule Screen](#rule-screen)
    - [First Clue Screen](#first-clue-screen)
    - [Success](#success)
    - [Try Again](#try-again)
    - [Calculating location Screen](#calculating-location-screen)
    - [Final Clue complete](#final-clue-complete)
  - [Technologies Used](#technologies-used)
  - [Installation](#installation)


## Overview

The Teasure Hunt program mimics a geochacing application allowing a user to search for a specific location given a clue and subsequent hints if desired. Once a user believes they are at location in question, they are able to confirm if they are correct or not. 

## Features

This application includes the following features: 
* A location persmission request screen for different levels of location accuracy. 
* A list of rules for the game after permissions 
* Two (2) different locations with 1 clue and 1 optional hint per location. Hints are may be toggled between hidden and exposed.
* Confirmation of a location completed my clicking "Found It". 
* Congratulatory screens are presented when a location is found "retry screens" are presented when the user location is >0.25Km from the actual location (determined by the haversine formula).
* A cumulative timer is available to keep track of the total time spent in searching for all locations. The timer only advances when a clue is actively being searched for. The time is reset when the game is restarted.

## Screenshots
### Location permission Screen
![](./Treasure-Hunt_Screenshots/location_permission.png)
### Rule Screen
![](./Treasure-Hunt_Screenshots/rule_screen.png)
### First Clue Screen
![](./Treasure-Hunt_Screenshots/clue1.png)
### Success
![](./Treasure-Hunt_Screenshots/success1.png)
### Try Again
![](./Treasure-Hunt_Screenshots/try_again.png)
### Calculating location Screen
![](./Treasure-Hunt_Screenshots/pending_location.png)
### Final Clue complete
![](./Treasure-Hunt_Screenshots/game_complete.png)

## Technologies Used

- **Programming Languages:** Kotlin, XML
- **Frameworks:** Android SDK, Jetpack Compose
- **IDE:** Android Studio


## Installation
*to include shell scripts for windows and mac systems soon*

