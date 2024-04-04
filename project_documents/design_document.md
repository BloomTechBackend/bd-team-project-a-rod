# A-Rod Design Document

## Instructions

*Save a copy of this template for your team in the same folder that contains
this template.*

*Replace italicized text (including this text!) with details of the design you
are proposing for your team project. (Your replacement text shouldn't be in
italics)*

*You should take a look at the example design document in the same folder as
this template for more guidance on the types of information to capture, and the
level of detail to aim for.*

## *Project Title* Design

## 1. Problem Statement

*I want to create a convenient means to figure out the standardized score of my golf games. 
Every golf course has a course rating and slope rating that are used in a formula, with your golf score, to determine what is called a "handicap". 
This allows players to determine how well they played regardless of the difficulty of the course they are on.*


## 2. Top Questions to Resolve in Review

*List the most important questions you have about your design, or things that
you are still debating internally that you might like help working through.*

1.   
2.   
3.  

## 3. Use Cases

*This is where we work backwards from the customer and define what our customers
would like to do (and why). You may also include use cases for yourselves, or
for the organization providing the product to customers.*

U1. As a customer, I should be able to make an account, with a username and email, 
to be able to keep track of my scores overtime and retrieve them.
    
U2. As a customer, I want to be able to view the handicap of the golf score I am logging 
in after submitting it.

U3. As a customer, I want to be able to view my overall current handicap when I 
select "view current handicap", which is officially determined by calculating the average 
of the 8 best handicaps from the last 20 played games.

U4. As a customer, I want to view my last 10 handicap scores when I select 
"view latest handicaps".

## 4. Project Scope

I want users to be able to see the handicap of the game they just played, their overall
handicap (best 8 of last 20 games), and their last 10 handicaps.

### 4.1. In Scope

I believe I can solve all parts of the problem that I have mentioned as there isn't much 
extra work involved in each part. 

### 4.2. Out of Scope

I would like to add some analytics, such as a graph to visualize performance over time, 
and this can be expanded further to choose the time horizon that you want to visualize 
your performance such as: last month, last 3 months, 6 months, year, or overall. I would 
also like to add the ability to track performance for each golf course, with a graph 
available for that too. Also, currently users would have to manually figure out and plug
in the course and slope ratings of the course they played at, and in the future I would
like to have that information stored for all golf courses, so that users only have to
worry about plugging in their score, making it easier. I think just simply making the 
website look organized, pretty, and colorful and adding as much cool analytical and visual
features as possible would make my app more fun to use. 

# 5. Proposed Architecture Overview

![img_1.png](img_1.png)
- The User and Score classes represent all the data we will be storing into DynamoDB.
- Will have a UserDao and ScoreDao to GET and POST. 
- Main activities and lambda functions: CreateNewScoreActivity, CreateUserActivity, GetHandicapActivity, and GetLatestGamesActivity.
- For the listed activity classes there will be a Provider, Request, and Result class. 
- UserModel and ScoreModel classes will be used to create independence between what we are storing and what we let users see, and will be part of the Result classes.
- Will need ModelConverter to create Model classes.
- HandicapCalculator class to separate calculation logic of standardizedScore. 
- Dagger Framework will be used to streamline dependency injection.
- Proposed architecture not only accomplishes all use cases, but also creates a very organized, modular architecture, which is the best practice for scalability, testing, and collaboration.

# 6. API

## 6.1. Public Models

*Define the data models your service will expose in its responses via your
*`-Model`* package. These will be equivalent to the *`PlaylistModel`* and
*`SongModel`* from the Unit 3 project.*

- UserModel: userId, email, gamesPlayed.
- ScoreModel: userId, date, courseName, standardizedScore

## 6.2. *First Endpoint*

*Describe the behavior of the first endpoint you will build into your service
API. This should include what data it requires, what data it returns, and how it
will handle any known failure cases. You should also include a sequence diagram
showing how a user interaction goes from user to website to service to database,
and back. This first endpoint can serve as a template for subsequent endpoints.
(If there is a significant difference on a subsequent endpoint, review that with
your team before building it!)*

*(You should have a separate section for each of the endpoints you are expecting
to build...)*

## 6.3 *Second Endpoint*

*(repeat, but you can use shorthand here, indicating what is different, likely
primarily the data in/out and error conditions. If the sequence diagram is
nearly identical, you can say in a few words how it is the same/different from
the first endpoint)*

# 7. Tables

*Define the DynamoDB tables you will need for the data your service will use. It
may be helpful to first think of what objects your service will need, then
translate that to a table structure, like with the *`Playlist` POJO* versus the
`playlists` table in the Unit 3 project.*

# 8. Pages

*Include mock-ups of the web pages you expect to build. These can be as
sophisticated as mockups/wireframes using drawing software, or as simple as
hand-drawn pictures that represent the key customer-facing components of the
pages. It should be clear what the interactions will be on the page, especially
where customers enter and submit data. You may want to accompany the mockups
with some description of behaviors of the page (e.g. “When customer submits the
submit-dog-photo button, the customer is sent to the doggie detail page”)*
