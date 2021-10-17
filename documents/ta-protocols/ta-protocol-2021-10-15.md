# Meeting Agenda
Group: OOPP-WITH-THE-BOIS

Date: 2021-10-15

Chair: Pelle

Participants: Martin, Jonathan, Jacob, Mathias

## Objectives 
* No objectives.

## Reports 
* No Reports.

## Discussion items
1. Is it okay for the peer review to be in bullet point format?
1. Is it okay to assume that people understand what a save-file is?
1. Should we have an abstraction package? Or is it ok to depend on model interfaces in view?
1. Do we need to add authors to all java documents? Any specific documentation style?
1. Does depending on a public static final int (primitive) count as a dependency on the class it is in?
1. Is it okay to move the noise source into the terrain enum, or should it stay in a hashmap in the generator, or is there a better place?
1. Planned refactors in SDD? Or only the current state of the project?
1. How do we prioritize the current stories and tasks for next week?
1. Are we expected to implement fixes based on the feedback we get from the peer review?
1. What should we prioritize for the presentation? How important is the presentation?

## Outcomes and assignments 
1. Yes.
1. Explain it (if there is any doubt).
1. Not needed, could be used but not necessary. Just make sure it's not mutable.
1. Use it if someone is responsible for a specific class, like Perlin Noise. Javadoc standard. For everything, interfaces, enums etc. 
1. Use a config class with static methods. The int should still be drawn in UML-diagrams.
1. It's okay to have generation strategy in the enum. (Answer to the question, why should it be the generator's responsibility to keep track of it?)
1. Planned refactors can be in the SDD, like “Further work”.
1. Better code over showing that the project is extendible. But everything in the simulation should have a purpose. 
1. If it is something big, write something about it instead. If it’s not useful, write about that too.
1. Code is not interesting, needs to be extraordinary to be interesting. Explain it more overall. 

(Note: We should document that our code is not thread safe.)   

## Wrap up
* Work on adding more fetures / completing more user stories.
* Next meeting on Friday 15:15
