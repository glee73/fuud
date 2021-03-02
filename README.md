# cs0320 Term Project 2021

**Team Members:** Ben Fiske, Casey Kim, Ethan Chung, Grace Lee

**Team Strengths and Weaknesses:**
* Ethan: 
  * Strengths - backend, familiarity with graphs
  * Weaknesses - frontend, javascript, html
* Ben: 
  * Strengths - backend (AI/ML)
  * Weaknesses - frontend, javascript, html
* Casey:
  * Strengths - front end
  * Weaknesses - complex javascript, 
* Grace:
  * Strengths - html/css, figma, interested in fullstack
  * Weaknesses - JavaScript, lack of familiarity with backend

**Project Idea(s):** _Fill this in with three unique ideas! (Due by March 1)_
### Idea 1 - Restaurant Review/Food pic sharing Social Network
Social networks like facebook and instagram are great for sharing photos, life updates, 
and more, but they aren't really efficient outlets for sharing food pictures and restaurant
experiences and reviews. We plan on building a social network where users can upload photos
of their experiences at restaurants, reviews on the food, and any recommendations on what
their friends should order. The idea behind this app is to let your friends know which 
restaurants are worth trying and what's worth getting there. Attached to each post will
be some data about the location of the restaurant. Users can add restaurants that they want
to try to their To Do List of restaurants they want to try, and they can search through
posts in their feed by restaurant to see what their friends recommended.

We plan on implementing 2 graph algorithms for the user's benefit:
1) Dijkstra's algorithm - to generate the shortest paths from a start location to
an end restaurant destination.

2) Prim's algorithm - to generate a minimum spanning tree on the selected restaurants that
the user wants to visit in one food crawl (https://definithing.com/food-crawl/).

Necessary features:
-Adding friends
-Generating a feed page containing posts from your friends/the community
-Making a post
-Searching posts by restaurant


### Idea 2 - AI/ML Game Competition
Artificial intelligence and machine learning have gained prominence in recent years not just for their increased use in industry, but their larger place in popular knowledge and culture. We propose created a platform on which users can learn fundamental artificial intelligence methods and algorithms in the context of building bots that can play one or more games (such as connect4, checkers, chess, etc.), and then competing against other users in those games. For users that are just getting into coding, we could offer an experience where they assemple different pieces and tune parameters to build their algorithm, while more experienced programmers could write their own code but take advantage of our platform for fun and for experimenting with different ideas.

We would need to include the following featues:
* *Integration and Ability to Play Multiple Games* - for variety purposes, we would want our bots to be able to complete in more than one game. We would have to set up basic engines to run all games theat we want to include, and build the infrastructure to be able to connect in AI players. This might be challenging because of our lack of experience in setting up these kinds of strutures, and just the amount of work required if we want to handle more than a couple games.
* *A Way to Keep Track of Users and Competition Results with a Robust Backend* - we would need to have a database structure for keeping track of users, their different algorithms, and their results against each other. We could potentially use a system like Elo to rank how bots compete against each other. This might be challenging because the backend could end up being quite complex, with a large number of backend-frontend connections required to keep all elements of the site running smoothly.
* *An Interface to Allow for Algorithm Creation and Testing* - To fulfill our aim of helping new and novice programmers learn about AI and machine learning, we'd need a way for them to construct their bots without getting too deep into the details of the algorithms (we could leave this as an option to more advanced programmers). One way to do this could be to allow users to select what type of algorithm they want to use (e.g. just a search, reinforcement learning that learns a policy, etc.) and then allow them to fine-tune the parameters, neural network setup if applicable, or heuristic if doing a search. We could find ways to do this where they wouldn't have to write too much actual, or at least complex, code, but introduce them to the ideas. The main challenge of our project lies here, and we would both need to be familiar and comfortable with ML methods ourselves, since we would have to write the base algorithms, and generate some good ideas for how to convey them to newer users.

### Idea 3 - Spotify
Especially during COVID, people have been looking for ways to connect. Spotify's
community page has a lot of people requesting more social-media-like
features, so we thought it would be a good idea to create a social network
based off of Spotify data and make the listening experience more
interactive. This would have to be a separate web-app where each profile
displays a user's top genres/songs/playlists and users can interact with each
other to recommend songs and create playlists off of those recommendations.

The following features would be necessary:
* *Integration with Spotify Web API* - this allows the webapp to access user data
  so that a users' top genres/songs/playlists can be shown on their profile;
  this way, a web player could also be embedded. We anticipate that learning
  to use the Spotify Web API and querying the data will be the most challenging
  part of this feature. Ideally, any playlists created on this app would also
  appear in a user's Spotify library.
* *An accessible user interface with a backend database of users* - as this is a
  social app, it definitely needs to be fun and intuitive to use. The front end
  can be challenging depending on how many features we want to implement, and
  the backend complexity would depend on whether we want our app to scalable.
* *Music recommender* - if we want to create playlists off of the recommendations,
  we probably need to implement a few algorithms. A few that Spotify already 
  uses includes collaborative filtering, natural language processing, and 
  convolutional neural networks. This part would be the hardest to implement
  because we might not have the depth of knowledge in ML needed.

### Idea 4


**Mentor TA:** _Put your mentor TA's name and email here once you're assigned one!_

## Meetings
_On your first meeting with your mentor TA, you should plan dates for at least the following meetings:_

**Specs, Mockup, and Design Meeting:** _(Schedule for on or before March 15)_

**4-Way Checkpoint:** _(Schedule for on or before April 5)_

**Adversary Checkpoint:** _(Schedule for on or before April 12 once you are assigned an adversary TA)_

## How to Build and Run
_A necessary part of any README!_
