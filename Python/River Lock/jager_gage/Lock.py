"""
description: This class is used to store and manage the data of a river Lock.
             It contains variables for depth, behavior, and section position, as well as
             variables for the current water level and if it is currently holding a boat.
             It has functions for moving boats in/out, checking if boats can move in/out, and
             for the three fill/empty behaviors, No Fill, Basic Fill, and Fast Empty.
author: Gage Jager
"""
class Lock:
    """
    description: The overloaded __init__ function, used to create a new Lock instance.
    author: Gage Jager
    parameters:
        self: All class functions must have an instance of themselves as the first argument.
        depth: An int specifying the depth of the new Lock.
        behavior: An int specifying the fill/empty behavior of the new Lock.  1 represents "No Fill",
                  2 represents "Basic Fill", and 3 represents "Fast Empty".
        position: An int specifying the position of the new Lock in the overall river.
    """
    def __init__(self, depth, behavior, position):
        self.__depth = depth
        self.__currentLevel = 0
        self.__heldBoat = None
        self.__position = position
        if behavior == 1:
            self.__behavior = self.noFill
        elif behavior == 2:
            self.__behavior = self.basicFill
        else:
            self.__behavior = self.fastEmpty
        return

    """
    description: The overloaded __str__ function, used to convert the Lock's data into a printable string.
    author: Gage Jager
    parameters:
        self: All class functions must have an instance of themselves as the first argument.
    returns:
        str1: A string representation of the lock and the boat within it, but only the top
              line (top line referring to the two lines of a river's string representation).
    """
    def __str__(self):
        # Probably have to deal with 1 digit vs 2 digit level.
        if self.__heldBoat is not None:
            str1 = "_" + str(self.__heldBoat) + "( {})_".format(str(self.__currentLevel))
        else:
            str1 = "_X( {})_".format(str(self.__currentLevel))
        return str1

    """
    description: This function is used by the __str__ function of the River in order to generate
                 the data for its bottom line.  This is a separate function from the above __str__
                 because the second line of data is only needed when outputting the whole River.
    author: Gage Jager
    parameters:
        self: All class functions must have an instance of themselves as the first argument.
    returns:
        str2: A string representation of the lock and the boat within it, but only the bottom
              line (bottom line referring to the two lines of a river's string representation).
    """
    def secondStr(self):
        if self.__heldBoat is not None:
            str2 = str(self.__heldBoat.getID())
            while len(str2) < 7:
                str2 = str2 + '.'
        else:
            str2 = "......."
        return str2

    """
    description: The overloaded __iter__ function, which creates an index representing the only space
                 in the current lock.
                 You may call this the saddest iterator in existence.
    author: Gage Jager
    parameters:
        self: All class functions must have an instance of themselves as the first argument.
    returns:
        self: The instance of a Lock class that is initializing an iterator.
    """
    def __iter__(self):
        # Actually the saddest iterator in existence.
        self.__index = 0
        self.__timesIterated = 0
        return self

    """
    description: The overloaded __next__ function, which iterates through the Lock's only position,
                 and returns the value of the index if a boat is in that position.
                 A very sad iterator that actually has to ensure it only runs once, because
                 otherwise the for loop utilizing this iterator will just keep checking the single
                 space forever otherwise.
    author: Gage Jager
    parameters:
        self: All class functions must have an instance of themselves as the first argument.
    returns:
        Index: The position of a boat in the Lock, based of of the iterator's current location.
    """
    def __next__(self):
        # A lock can only hold one boat, so if we have iterated once, we are done.
        # Need this because the way moveBoatOut works, it causes an infinite loop of checking the same boat
        # because I don't stop iterating if I don't move the boat.
        if self.__timesIterated == 0:
            self.__timesIterated += 1
            if self.__heldBoat is not None:
                return self.__index
            else:
                raise StopIteration()
        else:
            raise StopIteration()
        # Like I said, the saddest iterator.

    """
    description: This function takes in a Boat, and stores it in the __heldBoat variable.
    author: Gage Jager
    parameters:
        self: All class functions must have an instance of themselves as the first argument.
        boat: A boat object we are moving into the Lock.
        toPos: A redundant argument, included for parity with Section's moveBoatIn function,
               so either could be called without specifically knowing what type the section is.
    """
    def moveBoatIn(self, boat, toPos):
        self.__heldBoat = boat

    """
    description: This function removes and returns the Boat that was stored in __heldBoat.
    author: Gage Jager
    parameters:
        self: All class functions must have an instance of themselves as the first argument.
        index: A redundant argument, included for parity with Section's moveBoatOut.
    returns:
        boat: The Boat object that was stored in the Lock.
    """
    def moveBoatOut(self, index):
        boat = self.__heldBoat
        self.__heldBoat = None
        return boat

    """
    description: This function determines if a boat is able to move into the lock, by checking
                 if there is no boat already stored and that the water level is at its lowest point, 0.
    author: Gage Jager
    parameters:
        self: All class functions must have an instance of themselves as the first argument.
        position: A redundant argument, included for parity with Section's canMoveIn.
    returns:
        True: A boat can move into the Lock
        False: A boat cannot move into the Lock
    """
    def canMoveIn(self, position):
        if self.__heldBoat is None and self.__currentLevel == 0:
            return True
        else:
            return False

    """
    description: This function determines if a boat is able to leave the lock, by checking if
                 there is, in fact, a boat in the lock, and that the water level is at its highest point,
                 where it is equal to depth.
    author: Gage Jager
    parameters:
        self: All class functions must have an instance of themselves as the first argument.
        position: A redundant variable, used for parity with a removed Section function.
                  I do not want to refactor this close to submission time, however,
                  so please excuse it.
    returns:
        True: The boat can leave the Lock.
        False: The boat cannot leave the Lock.
    """
    def canMoveOut(self, position):
        if self.__heldBoat is not None and self.__currentLevel == self.__depth:
            return True
        else:
            return False

    """
    description: A simple getter function for a Lock's position in the river.
    author: Gage Jager
    parameters:
        self: All class functions must have an instance of themselves as the first argument.
    returns:
        __position: The Lock's position in the overall river.
    """
    def getPosition(self):
        return self.__position

    """
    description: This is one of the possible behavioral functions of the Lock.
                 On update, the Lock's behavior is called.  If this is the Lock's behavior,
                 nothing happens, because the Lock allows pass-through.
    author: Gage Jager
    parameters:
        self: All class functions must have an instance of themselves as the first argument.
    returns:
        None: Metaphorically nothing, but technically something denoting nothing.
    """
    def noFill(self):
        # No fill will never change.
        return None

    """
    description: This is one of the possible behavioral functions of the Lock.
                 On update, the Lock's behavior is called.  If this is the Lock's behavior,
                 the lock will first check if it contains a boat.
                 If it contains no boat, and the water level is not at its lowest, 0, lower the level by 1.
                 If it contains a boat, and the water level is not at its highest, depth, raise the level by 1.
    author: Gage Jager
    parameters:
        self: All class functions must have an instance of themselves as the first argument.
    """
    # GRADING: BASIC_FILL
    def basicFill(self):
        # Basic fill will always fill and raise by one.
        if self.__heldBoat is None:
            # If there is no boat, see if we need to lower the water level.
            if self.__currentLevel > 0:
                # Lower the water by one.
                self.__currentLevel -= 1
        else:
            # If there is a boat, see if we need to raise the water level.
            if self.__currentLevel < self.__depth:
                # Raise the water by one.
                self.__currentLevel += 1

    """
    description: This is one of the possible behavioral functions of the Lock.
                 On update, the Lock's behavior is called.  If this is the Lock's behavior,
                 the lock will first check if it contains a boat.
                 If it contains no boat, and the water level is not at its lowest, 0, lower the level by 1 or 2,
                 if 2 is possible without going below 0.
                 If it contains a boat, and the water level is not at its highest, depth, raise the level by 1.
    author: Gage Jager
    parameters:
        self: All class functions must have an instance of themselves as the first argument.
    """
    # GRADING: FAST_EMPTY
    def fastEmpty(self):
        # Fast empty will fill by one, but empty by two, if possible.
        if self.__heldBoat is None:
            # If there is no boat, see if we need to lower the water level.
            if self.__currentLevel > 0:
                # Lower the level by two, if possible, or by one.
                if self.__currentLevel > 1:
                    # Level is at least two, so we can empty by two.
                    self.__currentLevel -= 2
                else:
                    # Level is at one, so only empty by one.
                    self.__currentLevel -= 1
        else:
            # If there is a boat, see if we need to raise the water level.
            if self.__currentLevel < self.__depth:
                # Raise the water by one.
                self.__currentLevel += 1

    """
    description: On update, this function is called for every lock, which in turn calls
                 the instance's behavior function, either "No Fill", "Basic Fill", or "Fast Empty".
    author: Gage Jager
    parameters:
        self: All class functions must have an instance of themselves as the first argument.
    returns:
        __behavior(): Calls the Lock's behavior function, and returns what it returns (which is nothing)
    """
    def callBehavior(self):
        return self.__behavior()