##README

Instruction:
The SimCity.java file located inside the SimCity folder is the file to be used to run the project. There are 5 different types of buildings: Restaurant, bank, resident, apartment, and market. All 5 of these buildings work perfectly.There will be many personAgents traveling around SimCity and accessing certain buildings and performing activities associated with that architecture. The clock displays SimCity time in a fast manner so that testing becomes much easier. The cars are above the homes and will only be used if the distance between the personAgent's current position and the destination is beyond 400 unit pixels. For delivery from markets, the truck will perform that action by moving to the restaurants.The grader can see all of these actions happening at once because time in SimCity moves rather quickly. In order to see the activities associated with a particular building, the grader can click on that building and observe the panel on the right side of the screen. 

Instructions on how to load images correctly:

Step1: download our project to local. 
Step2: You can see a folder named Images under src folder, which has a few image files inside.  
Step3: Create a source folder under the PROJECT named images, NOT under src/! the created source folder should be parallel to src/. 
Step4: copy and paste all the images from the previous folder to the new folder. Note: only copy the images, NOT the entire folder. 









Full disclosure:

Bach Dinh: General animation and gui, person agent, general integration, restaurant and producer consumer model.

Shu (Rancho) Zhou: Resident and landlord and their guis, Home gui and apartment gui, and general integration of front end and back end components. And I did the unit-testing for norms for ResidentRole and Landlord. Right now, since we didn't do the situation switch between different situations. I always call msgGotHungry() after a person get back home. He can decide whether to cook at home or go out to eat. It's both 50% chance. One thing I didn't do is that the apartment right now didn't open the 3 other rooms. It's an easy setup. You can see that an apartment resident will only do activities inside his own cell, not the whole apartment building, which is different from home resident.

Amy Lee: Market agents, person agent. Had to leave and couldn't make several meetings due to family emergencies, but everything should be working.

Sid Menon: Bank agents. The Bank is not fully integrated into SimCity, in that personAgents do not presently enter the bank and perform the correct actions. However, the bankAgents have been tested, and each bank in v1 is pre-loaded with 2 customers, 1 host, and 1 teller, who will perform the normative scenario of depositing cash into their accounts. Since there's no economy that's been implemented yet, the Bank remains more or less isolated from SimCity v1 as a whole.

Andrew Lee: Worked on Bus Stop and general bugfixing. The Agent interaction should be working, but there isn't a system in place right now for it to maintain itself travelling between the outside world GUI (which I didn't work on at all) and the inside building GUI. In other words, **it doesn't work**, like the public transit system where I'm from. This is partly the result of a late redesign; the initial one did not account for money transactions and GUI interactions. The framework for unit testing has been set up, but no tests have been written.

The design docs and interaction diagrams are mostly not updated from the time of submission. The Resident/Market interaction is not required anymore according to Wilczynski's updated requirements, so that portion isn't implemented at all for V1.
