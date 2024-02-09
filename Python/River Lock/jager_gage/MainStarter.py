"""
Grading tags in for all lines marked with *			___yes

Tierless str meets D in SOLID (hidden test)*			___yes
Check if above is done, but not its test was not reached	___test was reached

1. Initial Show system\Got it compiling
Menu\initial system working					___yes
Bad input handled						___yes

2. Add Default
Added and shown properly					___yes
Second+ item ignored						___yes

3. Basic Update (single)
Moves along section						___yes
String format correct						___yes
Iterator used*							___yes (but it's sketchy)

4. Basic Update (multiple)					___yes

5. Multi Update
Updates correctly						___yes
Bad input handled						___yes

6. Show details
Shows details properly 						___yes
Iterator used*							___yes (but again, sketchy. couldn't find a better way to have forwards and backwards capability)

6. Add user specified item
Basic movement still works					___yes
Powered works							___yes
No passing							___yes

7. Tester part 1
Boats works up to second lock 					___yes
Formatting correct 						___yes

8. Tester part 2
Boats works up to end						___yes
Strategy pattern for basic fill*				___yes
Strategy pattern for fast empty*				___yes

9. Custom belt **
String formatting correct					___yes
Everything still works 						___yes
Bad input handled 						___yes
"""

from jager_gage import RiverSystem, River, Boat

import re

"""
description: Strips out whitespace from user input, for consistency.
author: Dr. Lisa Rebenitsch
params:
    prompt: A string containing a prompt to pass to input()
returns:
    result: The stripped user input.
"""
def cleanInput(prompt):
    result = input(prompt)
    # strips out blank lines in input
    while result == '':
        result = input( )

    return result

"""
description: Calls the River update function the specified number of times,
             and prints the river after each update.
author: Gage Jager
params:
    river: The river that we are updating.
    updateNum: A string or int containing the number of updates to perform.
"""
def update(river, updateNum):
    i = 0
    while i < int(updateNum):
        River.River.update(river)
        print( river )
        i += 1

"""
description: Enters a menu loop to create a new custom river.
             The loop will continuously ask the user for data
             on new river parts to add until the user specifies
             they want to stop adding parts.
             When the user is finished, we return the newly assembled river.
author: Gage Jager
returns:
    newRiver: The river we assemble as we get data on new river parts.
"""
def makeCustomRiver():
    isNum = re.compile(r'-?\d+')
    River.River.POSITION = -1
    newRiver = River.River()
    continueSystem = 'y'
    while continueSystem != 'n':
        sectionOrLock = cleanInput("Section (1) or Lock (2):> ")
        if sectionOrLock != '1' and sectionOrLock != '2':
            # Invalid section or lock input.
            if not isNum.fullmatch(sectionOrLock):
                # Was not a number.
                print("Cannot accept value")
            else:
                # Was a number, but was out of range.
                print("Input an option in the range 1-2")
        else:
            if sectionOrLock == '1':
                # Section Input
                length = cleanInput("Length:> ")
                if not isNum.fullmatch(length) or int(length) < 1:
                    # Invalid section length
                    print("Cannot accept value")
                else:
                    flow = cleanInput("Flow:> ")
                    if not isNum.fullmatch(flow) or int(flow) < 0:
                        # Invalid section flow
                        print("Cannot accept value")
                    else:
                        # All inputs were good, so add the section.
                        newRiver.addSection(int(length), int(flow))
            else:
                # Lock Input
                behavior = cleanInput("Fill behavior: None (1), Basic (2), or Fast Empty (3):> ")
                if behavior != '1' and behavior != '2' and behavior != '3':
                    # Invalid lock behavior
                    print("Cannot accept value")
                else:
                    depth = cleanInput("Depth:> ")
                    if not isNum.fullmatch(depth) or int(depth) < 0:
                        # Invalid lock depth
                        print("Cannot accept value")
                    else:
                        # All inputs were good, so add the lock.
                        newRiver.addLock(int(depth), int(behavior))
        continueSystem = cleanInput("\nAdd another component (n to stop):> ")
    return newRiver

"""
description: The Main function handles all user input and passes data to different functions
             depending on user input.  First, the function creates a default river and
             prints it before entering the main menu loop. The menu loop has options 1-7 containing
             various functions of the river system, option 0 exiting the loop, and a hidden
             option -1 to debug the __str__ functions in the various class objects.
author: Dr. Lisa Rebenitsch and Gage Jager
"""
def main( ):
    menu = "\n" \
           "1) Add Default Boat\n" \
           "2) Update One Tick\n" \
           "3) Update X Ticks\n" \
           "4) Show Section Details\n" \
           "5) Add Boat\n" \
           "6) Make Tester\n" \
           "7) Make New Simulator\n" \
           "0) Quit\n"

    placeholder = RiverSystem.RiverSystem()
    river = RiverSystem.RiverSystem.makeStarterRiver(placeholder)
    print(river)

    isNum = re.compile(r'-?\d+')

    choice = -1
    while choice != 0:

        print( menu )
        choice = cleanInput( "Choice:> " )

        # add default boat
        if choice == '1':
            River.River.addDefaultBoat(river)
            print( river )

        # update one time
        elif choice == '2':
            update(river, 1)

        # update X number of times
        elif choice == '3':
            updateNum = cleanInput("How many updates:> ")
            if isNum.fullmatch(updateNum):
                if int(updateNum) < 1:
                    print("Please, input a positive integer")
                else:
                    update(river, updateNum)
            else:
                print("Please, input a positive integer")

        # print out section details
        elif choice == '4':
            River.River.details(river)

        # make a new custom boat
        elif choice == '5':
            enginePower = cleanInput("What engine power:> ")
            if not isNum.fullmatch(enginePower) or int(enginePower) < 1:
                print("Please, input a positive integer")
            else:
                # Behaviors: Steady = 1, Max = 2
                behavior = cleanInput("What travel method. (1) Steady or (2) Max :> ")
                if not isNum.fullmatch(behavior):
                    print("Please, input a positive integer")
                elif int(behavior) < 1 or int(behavior) > 2:
                    print("Input an option in the range 1-2")
                    print(river)
                else:
                    newBoat = Boat.Boat(int(enginePower), int(behavior))
                    river.addBoat(newBoat)
                    print(river)

        # make tester system
        elif choice == '6':
            river = RiverSystem.RiverSystem.makeTesterRiver(placeholder)
            print(river)

        # make new user created system
        elif choice == '7':
            river = makeCustomRiver()
            print(river)

        # debug/check for D in SOLID in __str__
        elif choice == '-1':
            River.River.testing(river)

        elif choice == '0':
            choice = 0
        elif isNum.fullmatch(choice):
            print( "Input an option in the range 0-7" )
        else:
            print( "Please, input a positive integer" )


if __name__ == '__main__':
    main( )
