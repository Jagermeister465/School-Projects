"""
description: This class is used to store and manage the data of a river Section.
             It contains variables for length, flow, section position, and a list of size length
             for holding information on the boats in the Section.
             It also has numerous functions pertaining to moving boats, checking where boats are
             and where they can move, and many getter functions for accessing the variables in other classes/functions.
author: Gage Jager
"""
class Section:
    """
    description: The overloaded __init__ function, used to create a new Section instance.
    author: Gage Jager
    parameters:
        self: All class functions must have an instance of themselves as the first argument.
        length: An int specifying the length of the new section.
        flow: An int specifying the flow of the new section.
        position: An int specifying the position of the new section in the overall river.
    """
    def __init__(self, length, flow, position):
        self.__length = length
        self.__flow = flow
        self.__boatStorage = [None] * length
        self.__position = position
        return

    """
    description: The overloaded __str__ function, used to convert the Section's data into a printable string.
    author: Gage Jager
    parameters:
        self: All class functions must have an instance of themselves as the first argument.
    returns:
        str1: A string representation of the section and the boats within it, but only the top
              line (top line referring to the two lines of a river's string representation).
    """
    def __str__(self):
        i = 0
        str1 = ''
        while i < self.__length:
            if self.__boatStorage[i] is not None:
                str1 = str1 + str(self.__boatStorage[i]) + "〜〜"
            else:
                str1 = str1 + "〜〜〜"
            i += 1

        return str1

    """
    description: This function is used by the __str__ function of the River in order to generate
                 the data for its bottom line.  This is a separate function from the above __str__
                 because the second line of data is only needed when outputting the whole River.
    author: Gage Jager
    parameters:
        self: All class functions must have an instance of themselves as the first argument.
    returns:
        str2: A string representation of the section and the boats within it, but only the bottom
              line (bottom line referring to the two lines of a river's string representation).
    """
    def secondStr(self):
        i = 0
        str2 = ''
        while i < self.__length:
            if self.__boatStorage[i] is not None:
                str2 = str2 + str(self.__boatStorage[i].getID())
                while len(str2) < (i * 3 + 3):
                    str2 = str2 + '〜'
            else:
                str2 = str2 + "〜〜〜"
            i += 1

        return str2

    """
    description: The overloaded __iter__ function, which creates an index representing the last
                 space in the current Section.
    author: Gage Jager
    parameters:
        self: All class functions must have an instance of themselves as the first argument.
    returns:
        self: The instance of a Section class that is initializing an iterator.
    """
    def __iter__(self):
        # Index the section backwards, because the furthest boat should move first.
        self.__index = self.__length
        return self

    """
    description: The overloaded __next__ function, which iterates backwards through the Section's
                 boat storage list and returns the index of any position that contains a boat.
    author: Gage Jager
    parameters:
        self: All class functions must have an instance of themselves as the first argument.
    returns:
        Index: The position of a boat in the section, based of of the iterator's current location.
    """
    def __next__(self):
        # Again, should this be prev or something?
        if self.__index > 0:
            self.__index -= 1
            while not self.hasBoatInPos(self.__index) and self.__index > 0:
                self.__index -= 1
            # Check if we exited loop because we have a boat or because we ran out of river.
            if self.hasBoatInPos(self.__index):
                return self.__index
            else: raise StopIteration()
        else:
            raise StopIteration()

    """
    description: This function takes in a the index of a position in the Section, and tells
                 if there is a boat in that position or not.
    author: Gage Jager
    parameters:
        self: All class functions must have an instance of themselves as the first argument.
        position: An int representing the position we want to check for a boat.
    returns:
        True: A boat was found at the specified position.
        False: A boat was not found at the specified position.
    """
    def hasBoatInPos(self, position):
        if self.__boatStorage[position] is not None:
            return True
        else:
            return False

    """
    description: This function takes in a to position and a from position, then moves
                 the boat located at the from position into the to position.
    author: Gage Jager
    parameters:
        self: All class functions must have an instance of themselves as the first argument.
        toPos: An int specifying the position to move the boat to.
        fromPos: An int specifying the position to move the boat from.
    """
    def moveBoat(self, toPos, fromPos):
        boat = self.__boatStorage[fromPos]
        self.__boatStorage[fromPos] = None
        self.__boatStorage[toPos] = boat

    """
    description: This function takes in a Boat and a to position, then moves that boat into
                 the boat storage list at that position.
    author: Gage Jager
    parameters:
        self: All class functions must have an instance of themselves as the first argument.
        boat: The Boat object to move into the section.
        toPos: An int specifying the position to move the boat into.
    """
    def moveBoatIn(self, boat, toPos):
        self.__boatStorage[toPos] = boat

    """
    description: This function takes in an index of a position in boat storage, then removes
                 and returns the Boat at that location.
    author: Gage Jager
    parameters:
        self: All class functions must have an instance of themselves as the first argument.
        index: An int specifying the position to remove a boat from.
    returns:
        boat: The Boat object that is being removed from the section.
    """
    def moveBoatOut(self, index):
        boat = self.__boatStorage[index]
        self.__boatStorage[index] = None
        return boat

    """
    description: Yeah I just realized this is basically identical to hasBoatInPos(). Oops.
                 I know I call both though and it's getting late to refactor though.
                 Sorry for the redundancy.
                 
                 Anyway in this one, we return True if there is no boat, and False if there is a boat.
    author: Gage Jager
    parameters:
        self: All class functions must have an instance of themselves as the first argument.
        position: An int specifying the position we want to check for a boat.
    returns:
        True: We can move a boat into the specified location.
        False: We cannot move a boat into the specified location.
    """
    def canMoveIn(self, position):
        if self.__boatStorage[position] is None:
            return True
        else:
            return False

    """
    description: This function determines how many spaces are between a given position, currPos,
                 and the next boat in the Section.  If there is no next boat in the Section,
                 an extra space is added to the total of spaces to represent that the Boat could
                 theoretically move into the next Section, unimpeded.
    author: Gage Jager
    parameters:
        self: All class functions must have an instance of themselves as the first argument.
        currPos: An int specifying the current position of the boat we want to move.
    returns:
        safeMoves: An int specifying the number of spaces up the river a boat could theoretically move.
    """
    def moveDistance(self, currPos):
        i = currPos + 1
        j = len(self.__boatStorage)
        safeMoves = 0
        while i < j and self.__boatStorage[i] is None:
            safeMoves += 1
            i += 1
        # If we hit the end of the section, not another boat, add another safe move.
        if i == j:
            safeMoves += 1
        return safeMoves

    """
    description: This function takes in a position in the boat storage list, and determines
                 if it is the last space in the Section or not.
    author: Gage Jager
    parameters:
        self: All class functions must have an instance of themselves as the first argument.
        currPos: An int specifying a position on the river.
    returns:
        True: The specified position is the last space in the section.
        False: The specified position is not the last space in the section.
    """
    def atEnd(self, currPos):
        if currPos + 1 == self.__length:
            return True
        else:
            return False

    """
    description: This function takes in a position in the boat storage list, currPos, and returns
                 how many spaces there are in the Section until the boat would reach the end of the Section.
    author: Gage Jager
    parameters:
        self: All class functions must have an instance of themselves as the first argument.
        currPos: An int specifying a position on the river.
    returns:
        toSectionEnd: An int specifying the number of spaces on the river from currPos to its end.
    """
    def toEnd(self, currPos):
        toSectionEnd = 0
        while not self.atEnd(currPos):
            toSectionEnd += 1
            currPos += 1
        return toSectionEnd

    """
    description: A simple getter function for a Section's Position.
    author: Gage Jager
    parameters:
        self: All class functions must have an instance of themselves as the first argument.
    returns:
        __position: An int specifying the section's position in the river as a whole.
    """
    def getPosition(self):
        return self.__position

    """
    description: A simple getter function for a Section's Length.
    author: Gage Jager
    parameters:
        self: All class functions must have an instance of themselves as the first argument.
    returns:
        __length: An int specifying the length of the section.
    """
    def getLength(self):
        return self.__length

    """
    description: A simple getter function for a Section's Flow.
    author: Gage Jager
    parameters:
        self: All class functions must have an instance of themselves as the first argument.
    returns:
        __flow: An int specifying the flow of the section.
    """
    def getFlow(self):
        return self.__flow

    """
    description: A simple getter function for the boat stored at a position in the Section.
    author: Gage Jager
    parameters:
        self: All class functions must have an instance of themselves as the first argument.
        position: An int specifying the position in the section we want to get a boat from.
    returns:
        Boat: A Boat object from the specified location in the Section.
    """
    def getBoat(self, position):
        return self.__boatStorage[position]