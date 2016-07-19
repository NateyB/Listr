# Listr
## What is Listr?
Listr is an alpha-stage MIT-Licensed JavaFX application for, you guessed it,
making lists!
These lists can be to-do lists with associated dates, or just lists of things
that you want to do in your free time.

Most importantly, Listr is extremely flexible and usable.

###Rules
Listr's uses "Rules" to organize its tasks. Rules are simply functions that
indicate whether a given task meets their criteria. For example, the rule
"week" will state that any task due before the end of the week meets its
criteria. So, if you wanted to view all of your tasks due this week in Listr,
you'd simply type "week" into the search bar at the top of the app.

A prototypical example of a rule is a tag. Tasks can be given tags when
they're  created (or modified later), and any task which contains a given tag
is said to meet the criteria for that tag, or for that rule. So, if you wanted
to see all of your classwork (or real work!) due, then you could just search
for "coursework" or "work," and the app would show any tasks tagged with those.

What sets Listr apart is its simple and elegant usage of rules. Suppose that
you wanted to see any task due in your coursework, but not anything due after
this week: Simply search for "coursework & week," and Listr will only return
tasks meeting the criteria for both. What if you wanted to see all of your
coursework, and everything else due this week to try to get ahead? Search for
"coursework + week," and Listr will return tasks meeting the criteria for either.

As you can see, Listr uses set theory to combine tasks. Natively, Listr
supports five operations for task combination: Intersection (&), union (+),
inverse (!), difference (-), and symmetric difference/XOR ($).

###Completion Behaviors
Rather than follow the philosophy of most list organizers which contain a
to-do component, Listr tries to allow for as many possible things to happen
when a user clicks that checkmark to indicate that a task is completed. This
functionality can allow for a repeat task due one week later, or simply mark
the task as completed, or play a cute sound (or, because security controls
haven't been implemented, delete the filesystem).

Admittedly, the UI support in this area is lacking, but that is a shortcoming
that will be resolved (one way or another) before beta.

## Extensibility
Listr is highly extensible. The ability to define your own rules and completion
behaviors in Java is built right into the UI!

In order for a class to be counted as a rule, you merely need to extend
[the Rule interface](src/main/java/com/natebeckemeyer/projects/listrgui/core/Rule.java)
and add the class into Listr. (At some point before beta, adding it to the
userrules folder will suffice).

In order for a class to be counted as a completion behavior, you must extend
[the CompletionBehavior abstract class](src/main/java/com/natebeckemeyer/projects/listrgui/core/CompletionBehavior.java)
and add the class into Listr. (At some point before beta, adding it to the
usercompletionbehaviors folder will suffice).

Additionally, the operations for combining rules are very extensible. Simply
creating an enum that extends
[the RuleOperation interface](src/main/java/com/natebeckemeyer/projects/listrgui/core/RuleOperation.java)
and using that class instead will suffice.

Flexibility is held in equal regard to usability, and will continue to be
held that way going forward.