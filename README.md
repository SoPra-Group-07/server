# SoPraFS20 Group 007 Just One
## Introduction
This is an implementation of the Just One is a cooperative party game in which the goal is to discover as many mystery words as possible. 
While one player has the chance to guess the mystery word, which is determined by a card drawn by the player,
the other teammates have to find the best clue to help the player guessing the word. 
So, contrary to most games you know, in this game you have to work together to win the game. 
A game is played over thirteen cards and the score is additionally determined by answer time and more.

## Game rules and additional functionalities
The origin game rules and a short demo of the game can be found here: https://justone-the-game.com/index.php?lang=en

The application contains following additional rules and features:
-	The application has a public leader board, which can be accessed through a button on the overview page, which contains all users that have ever played a game sorted in descending order by highscore. Since this is a highscore, every player only shows up once on the leaderboard. This also means that users that did not play a game yet, do not show up in the leaderboard. 

-	The application has an individual point system that is additionally to the original influenced by following circumstances. 
        -	The application tracks the time a player needs to give a clue/ guess. A player gets +0.3 points for submitting within 15 seconds, +0.2 for submitting within 30 seconds and +0.1 for submitting within 45 seconds. Note that we decided to make the influence of the submission time on points fairly small, since we believe that giving a good clue/guess is more important than the time you need to submit it.
        -	We keep track of how many correct guesses and duplicate clues a player accumulates during a game. A player gets -0.5 points for a duplicate clue. As a guessing player, you get +1.5 points for a correct guess and the clueing players get +1 point if the guessing player guesses correctly. We decided to also give the clueing players points for a right guess because “Just-one” is a cooperative game where the goal is to work together. 
        -   Note that this point system also means that a player can have a negative score if he/she plays very bad and don’t work well together in a team. Like in the original game rules, we also deduct one card from the stack of cards when a player guesses wrong. This means that then the number of total game rounds get decreased by one.

-	In every game there is a bot ready to put into. A player can decide if he/she wants to have a bot in the game or not when creating a new game by switching a toggle button. This bot can be friendly or malicious what is determined randomly. A friendly bot returns a synonym of the mystery word or the statistically most related word of the mystery word as a clue. A malicious bot returns an antonym of the mystery word or an antonym of the adjective that is most used to describe the mysteryword as a clue. We decided to implement it like this since there a often no synonyms and antonyms for the mystery words (e.g. “Elvis”, “Churchil”). This approach pointed out to produce the best results.To get these related words, we make calls to the external API datamuse. Note that the players do not know which player is a bot and if it is friendly or malicious. Otherwise it would be too easy to figure out that a bot is malicious and then turn his clues from antonyms into synonyms by only taking the antonyme of the antonyme. What would break the effect of a malicious bot. 

-	The application has an automatic natural language processing step that analyzes the clues given by players and determines which ones are identical or invalid according the game rules. For that we build in an external API Apache OpenNLP 1.9.2. from which we use the Stemmer to stem the clues so that they can be compared to each other. Like this, clues like “mouse” and “mouses” are detected as identical and are not shown to the guessing player. Further, a player can not give a clue that contains the mystery word. 

-	The application has a built in timer functionality, each player has exactly 60 seconds to hand in a clue or a guess. If he/she does not submit within that time, the application detects that and handles it as not giving a clue or not giving a guess. For a guessing player, nothing bad happens when he does not guess. But when a clueing player does not submit a clue, 0.6 points get deducted from his points. Like this we ensure that the game does not stop when a player does not hand in anything. Further, it pushes the clueing players to always hand in clues, what also makes the game more interesting and enjoyable. 

-	The application has a demo game functionality, which can be accessed by switching a toggle button on the create new game page. This demo game has only three game rounds instead of thirteen. This demo game can be used by users to show the game, his rules and functionalities to other users before playing the actual game. Further, this functionality is very useful to developers to test the functionality of the game. Feel free to use it. 

-	Security is a must, hence no question, our application prevents the from landing on wrong pages or on pages they should not have access to. Therefore, we have implemented the following idea: On each page the URI of the current page and a boolean variable "isValid" are stored in the session storage. If a user now wants to navigate to another page by clicking a button, the "isValid" variable is set equals true. The page guard of the desired page tests, whether the "isValid" variable corresponds to true. If so, you can navigate to the desired page. If not, you will be redirected back to the previous page according to the URI stored in the session storage. The latter case happens exactly when you try to manually navigate to another page, which is now no longer possible. Additionally, if a user types in an invalid URI we have created a „page not found“ site to which the user gets directed.


## Technologies used
Java 13.0.2  
![Java](src/images/Java.png)

GitHub  
![GitHub](src/images/GitHub.png)

REST API  
![REST](src/images/RestAPI.png)

Spring Boot  
![Spring](src/images/SpringBoot.png)

Heroku  
![Heroku](src/images/Heroku.png)

JPA  
![JPA](src/images/JPA.png)

## High-level components

## Launch & Deployment
### Setup this Template with your IDE of choice

Download your IDE of choice: (e.g., [Eclipse](http://www.eclipse.org/downloads/), [IntelliJ](https://www.jetbrains.com/idea/download/)) and make sure Java 13 is installed on your system.

1. File -> Open... -> SoPra Server Template
2. Accept to import the project as a `gradle project`

To build right click the `build.gradle` file and choose `Run Build`

### Building with Gradle

You can use the local Gradle Wrapper to build the application.

Plattform-Prefix:

-   MAC OS X: `./gradlew`
-   Linux: `./gradlew`
-   Windows: `./gradlew.bat`

More Information about [Gradle Wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html) and [Gradle](https://gradle.org/docs/).

#### Build

```bash
./gradlew build
```

#### Run

```bash
./gradlew bootRun
```

#### Test

```bash
./gradlew test
```

#### Development Mode

You can start the backend in development mode, this will automatically trigger a new build and reload the application
once the content of a file has been changed and you save the file.

Start two terminal windows and run:

`./gradlew build --continuous`

and in the other one:

`./gradlew bootRun`

If you want to avoid running all tests with every change, use the following command instead:

`./gradlew build --continuous -xtest`

## Roadmap
If you have ideas to improve our application, feel invited to join us. 
We suggest following additional features for our application:
- It would be nice to have a multimodal UI where players can for example play "just-one" in a pantomime way or
    in giving clues by drawings.
- It would be nice to make the UI customizable in terms of themes that can be selected
- It would be great if users could create a bitmoji that looks just like them, this could then be pulled into the game
- It would be great if users had a way to communicate with each other, maybe a chat function?

## Authors and acknowledgement
- Jason Browne

- Dario Gagulic

- Dave Basler

- Lynn Zumtaugwald

- Piero Neri

Many thanks to the whole SOPRA FS20 Team to support us during the project. 
A special thank goes to our TA, Jenny Schmid. 

## License

See the [LICENSE](LICENSE) file for license rights and limitations (Apache License 2.0).