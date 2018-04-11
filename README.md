# WebWalls #

Network applications development in C and Java languages.

## Project Summary ##

This project aims at implementing a walls service, each wall is a board
where users can freely add plain text only messages. The project is
designed for a friendly working environment without security concerns.
No user authentication or authorization is enforced, to add a message,
the only requirement is knowing the wall’s name. However, walls’ names
are not publicly available, so they can be also regarded as access keys.
Messages in a wall are publicly available and may be removed by any user,
again, as far at the wall’s name is known.
Two applications are to be developed, the main one is the server, it
manages the walls and it’s in essence an HTTP server, it provides a web
users’ frontend, REST web services and also a low level UDP messages
interface.
The second application is a simple UDP client that allows the direct
posting of messages in a wall by using the mentioned low level UDP
messages interface.
Applications may be developed either in C or Java language, regarding the
web frontend, JavaScript will also be required.

### Building the wheel ###

* Summary of set up
* Configuration
* Dependencies
* Database configuration
* How to run tests
* Deployment instructions

### Contacts ###

* [André Oliveira](1040862@isep.ipp.pt)
* [Raúl Correia](1090657@isep.ipp.pt)
* [Rui Ribeiro](1150344@isep.ipp.pt)
* [Mário Dias](1151708@isep.ipp.pt)
* [Miguel Santos](1161386@isep.ipp.pt)
