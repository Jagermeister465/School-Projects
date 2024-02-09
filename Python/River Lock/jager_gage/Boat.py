"""
description: This class is used to store and manage the data of a Boat on the river.
             It has variables for the engine power, the boat ID, and the engine behavior of the boat,
             which can be either "Steady" or "Max".
             It also has functions for getting the next boat ID, returning the boat ID to an outside function,
             and for performing the boat behaviors.
author: Gage Jager
"""
class Boat:
    currentID = 0

    """
    description: The overloaded __init__ function, used to create a new Boat instance.
    author: Gage Jager
    parameters:
        self: All class functions must have an instance of themselves as the first argument.
        power: An int specifying the engine power of the new Boat.
        behavior: An int specifying the engine behavior of the new Boat. 1 represents "Steady",
                  while 2 represents "Max".
    """
    def __init__(self, power, behavior):
        self.__ID = self.nextID()
        self.__power = power
        if behavior == 1:
            self.__behavior = self.steady
        else:
            self.__behavior = self.max
        return

    """
    description: The overloaded __str__ function, used to convert a boat into a printable string.
    author: Gage Jager
    parameters:
        self: All class functions must have an instance of themselves as the first argument.
    returns:
        ⛴: The character representing a boat.
    """
    def __str__(self):
        return "⛴"

    """
    description: This function increments the class variable currentID by one, then returns its value.
    author: Gage Jager
    parameters:
        self: All class functions must have an instance of themselves as the first argument.
    returns:
        currentID: An int representing a new Boat's ID.
    """
    def nextID(self):
        Boat.currentID += 1
        return Boat.currentID

    """
    description: A simple getter function for a Boat's ID.
    author: Gage Jager
    parameters:
        self: All class functions must have an instance of themselves as the first argument.
    returns:
        __ID: The ID of the Boat instance.
    """
    def getID(self):
        return self.__ID

    """
    description: This is one of the possible behavioral functions of the Boat.
                 On update, the Boat's behavior is called.  If this is the Boat's behavior,
                 it returns a maximum travel distance of 1 space, as the boat moves
                 steadily down the river.
    author: Gage Jager
    parameters:
        self: All class functions must have an instance of themselves as the first argument.
        section: A redundant variable, used for parity with the other behavioral function.
    returns:
        1: The maximum travel distance of the boat.  Always 1.
    """
    # GRADING: STEADY_TRAVEL
    def steady(self, section):
        # Calculates the distance a steady boat can travel.
        # Oh wait, steady is always 1 space max.
        return 1

    """
    description: This is one of the possible behavioral functions of the Boat.
                 On update, the Boat's behavior is called.  If this is the Boat's behavior,
                 it returns a maximum travel distance of engine power minus section flow.
                 If the flow is greater than the engine power, the maximum travel distance will be 1 instead.
    author: Gage Jager
    parameters:
        self: All class functions must have an instance of themselves as the first argument.
        section: The section the boat is located in.  Used to access the section's flow.
    returns:
        maxMoves: The maximum travel distance of the boat.
        1: The maximum travel distance of the boat, if flow > engine power.
    """
    # GRADING: MAX_TRAVEL
    def max(self, section):
        # Calculates the distance a max boat can travel.
        # At least 1 space, at most power - flow spaces
        maxMoves = self.__power - section.getFlow()
        if maxMoves < 1:
            return 1
        else:
            return maxMoves

    """
    description: On update, this function is called for every boat, which in turn calls
                 the instance's behavior function, either "Steady" or "Max".
                 It will return the maximum number of spaces a boat could move, which was returned
                 from the behavior function.
    author: Gage Jager
    parameters:
        self: All class functions must have an instance of themselves as the first argument.
        section: The section the boat is located in.  Used by one behavior function to access the section's flow.
    returns:
        __behavior(): Calls the Boat's behavior function, and returns what it returns (the max travel distance).
    """
    def callBehavior(self, section):
        return self.__behavior(section)