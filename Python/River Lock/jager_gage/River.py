from jager_gage import Section, Lock, Boat

"""
description: This class is used to store and manage data on the river as a whole.
             This is accomplished mainly through the __sectionList variable
             and the update() function, which iterates through the entire river
             in order to move boats along the river.
author: Gage Jager
"""
class River:
    POSITION = -1

    """
    description: The overloaded __init__ function, used to create an empty list to hold river sections
                 as well as set the default iterator behavior to 0, which represents backwards traversal.
    author: Gage Jager
    parameters:
        self: All class functions must have an instance of themselves as the first argument.
    """
    def __init__(self):
        self.__sectionList = list()
        self.__iterBehavior = 0
        return

    """
    description: The overloaded __str__ function, used to convert a River into a printable string.
                 Because the representation of a river has two lines, we call the default __str__
                 function of sections/locks to get the top line representation, and we call the
                 respective secondStr() function to get the bottom line representation of a section/lock.
                 Once we have the full string of both the top and bottom line data, we combine the two
                 with a newline in between before returning.
    author: Gage Jager
    parameters:
        self: All class functions must have an instance of themselves as the first argument.
    returns:
        Output String: A string containing a river object expressed as a string.
    """
    def __str__(self):
        sectionCount = len(self.__sectionList)
        topLine = ''
        bottomLine = ''
        i = 0
        while i < sectionCount:
            topTemp = self.__sectionList[i].__str__()
            bottomTemp = self.__sectionList[i].secondStr()
            topLine = topLine + topTemp
            bottomLine = bottomLine + bottomTemp
            i += 1
        return "{}\n{}".format(topLine, bottomLine)

    """
    description: The overloaded __call__ function, used to change the River iterator behavior
                 between forwards and backwards before creating/using the iterator.
    author: Gage Jager
    parameters:
        self: All class functions must have an instance of themselves as the first argument.
        iterBehavior: An int representing the choice of iterator behavior, either 0 for
                      backwards or any other value for forwards.
    returns:
        self: The instance of the River class that was called.
    """
    def __call__(self, iterBehavior):
        self.__iterBehavior = iterBehavior
        return self

    """
    description: The overloaded __iter__ function, which creates an index variable at the
                 beginning or end of the section list, depending on if we are iterating forwards
                 or backwards.
                 It's a bit clunky having both forwards and backwards iterators in one but
                 I wasn't finding a better way to get it done, at least not in Python.
    author: Gage Jager
    parameters:
        self: All class functions must have an instance of themselves as the first argument.
    returns:
        self: The instance of the River class that is initializing an iterator.
    """
    def __iter__(self):
        # Determine forwards vs. backwards
        if self.__iterBehavior == 0:
            # GRADING: ITER_ALL
            # Section iterator is from river's end to beginning
            self.__sectionIndex = len(self.__sectionList)
            return self
        else:
            # GRADING: ITER_RESTRICT
            # Section iterator is from river's beginning to end
            self.__sectionIndex = -1
            return self

    """
    description: The overloaded __next__ function, which iterates through the sections of the
                 river and returns them sequentially.  Can iterate both forwards and backwards
                 along the sections.
                 Again, a bit clunky, but I didn't find a better way to get it done in Python.
    author: Gage Jager
    parameters:
        self: All class functions must have an instance of themselves as the first argument.
    returns:
        Section: A section, indexed from the instance's __sectionList.
    """
    def __next__(self):
        # Determine forwards vs. backwards
        if self.__iterBehavior == 0:
            # Section iterator is from river's end to beginning
            if self.__sectionIndex > 0:
                self.__sectionIndex -= 1
                return self.__sectionList[self.__sectionIndex]
            else:
                raise StopIteration()
        else:
            # Section iterator is from river's beginning to end
            if self.__sectionIndex < len(self.__sectionList) - 1:
                self.__sectionIndex += 1
                return self.__sectionList[self.__sectionIndex]
            else:
                raise StopIteration()

    """
    description: This function creates a new Section and adds it to the section list of the river.
                 It uses the passed in length and flow as well as the class variable POSITION
                 to create the new Section.
    author: Gage Jager
    parameters:
        self: All class functions must have an instance of themselves as the first argument.
        length: An int specifying the length of the section to add.
        flow: An int specifying the flow of the section to add.
    """
    def addSection(self, length, flow):
        River.POSITION += 1
        self.__sectionList.append( Section.Section(length, flow, River.POSITION) )

    """
    description: This function creates a new Lock and adds it to the section list of the river.
                 It uses the passed in depth and behavior as well as the class variable POSITION
                 to create the new Lock.
    author: Gage Jager
    parameters:
        self: All class functions must have an instance of themselves as the first argument.
        depth: An int specifying the depth of the lock to add.
        behavior: An int specifying the intended behavior of the lock to add.
    """
    def addLock(self, depth, behavior):
        River.POSITION += 1
        self.__sectionList.append( Lock.Lock(depth, behavior, River.POSITION) )

    """
    description: This function creates a new boat with 1 power and "Steady" behavior,
                 and tries to add the new boat to the start of the river.
                 Used by Menu Option 1.
    author: Gage Jager
    parameters:
        self: All class functions must have an instance of themselves as the first argument.
    """
    def addDefaultBoat(self):
        newBoat = Boat.Boat(1, 1)
        self.addBoat(newBoat)

    """
    description: This function takes in a boat and checks to see if it can be added to the start
                 of the river.  If the boat cannot be added to the first position of the first part,
                 Section or Lock, the boat is simply ignored, although the boat ID is not reset.
    author: Gage Jager
    parameters:
        self: All class functions must have an instance of themselves as the first argument.
        newBoat: A Boat object to be added to the section.
    """
    def addBoat(self, newBoat):
        if not self.__sectionList[0].hasBoatInPos(0):
            self.__sectionList[0].moveBoatIn(newBoat, 0)

    """
    description: This function iterates forwards through the section list and prints the flow
                 and number of boats in each Section of the river, sequentially.  Any Locks, and
                 the boats they contain, are skipped over and no data is output from them.
    author: Gage Jager
    parameters:
        self: All class functions must have an instance of themselves as the first argument.
    """
    def details(self):
        sectionCount = 1
        # GRADING: LOOP_RESTRICT
        for section in self(1):
            if isinstance(section, Section.Section):
                print("Section " + str(sectionCount))
                sectionFlow = section.getFlow()
                boatCount = 0
                for boat in section:
                    boatCount += 1
                print('Boats: {} Flow: {}'.format(boatCount, sectionFlow))
                sectionCount += 1

    """
    description: This function is used to demonstrate that the different overloaded __str__ functions
                 follow the D in SOLID, and function independently when removed from the context of printing
                 the whole river.
                 To do this, we create a section, a lock, and three boats.  One boat goes in the Section,
                 one in the Lock, and the last in the current full river.
                 We then try to print just a boat, just the section with a boat, just the lock with a boat,
                 and then lastly the full river with a boat.
    author: Gage Jager
    parameters:
        self: All class functions must have an instance of themselves as the first argument.
    """
    def testing(self):
        sectionBoat = Boat.Boat(1, "steady, when implemented")
        loneSection = Section.Section(1, 0, 10)
        lockBoat =  Boat.Boat(1, "steady, when implemented")
        loneLock = Lock.Lock(0, "lol not yet", 20)
        riverBoat = Boat.Boat(1, "steady, when implemented")
        loneSection.moveBoatIn(sectionBoat, 0)
        loneLock.moveBoatIn(lockBoat, 0)
        self.addBoat(riverBoat)

        # GRADING: TO_STR
        print(sectionBoat)
        print('')       # Skip a line for readability.
        print(loneSection)
        print('')       # Skip a line for readability.
        print(loneLock)
        print('')       # Skip a line for readability.
        print(self)

    """
    description: This function handles all the logic for updating the river by one tick.
                 The outer loop handles iterating through each part of the river, from end to beginning.
                 If the current part is a Lock, we update the lock immediately before trying to move any boats in/out.
                 The inner loop handles iterating through each boat in a part of the river, from end to beginning.
                 On the inside, the first if-else separates a case into a boat in a Section, or a boat in a Lock.
                 
                 In the case of a boat in a Section, we find out the maximum number of safe moves the boat
                 could go theoretically, and how many moves there are until the end of the section.
                 If the boat can theoretically leave the Section, we move it at most until the end of the Section,
                 and if the boat is prevented from doing so by another boat, we can move it at most until that next boat.
                 Once we know if the boat is moving towards the end, or towards another boat, we calculate the amount
                 of spaces the boat can actually travel, and move it the lesser of the two amounts
                 (actual travel spaces VS. spaces to end/next boat).
                 However, if the boat is already at the end of the Section, we check if we can move it either
                 into the next Section/Lock, or if there is no next part, we remove it from the river.
                 
                 In the case of a boat in a Lock, we first check if the Lock is at the correct water level
                 to allow the boat to leave.  If it is, we check if the next Section/Lock can take the boat.
                 If the boat can leave the Lock and can enter the next Section/Lock, we move it there.
                 If there is no next part and the boat can leave the Lock, we remove it from the river instead.
                 
                 Once we have iterated across every section in the river and handled every boat in those sections,
                 we have finished the update tick.
    author: Gage Jager
    parameters:
        self: All class functions must have an instance of themselves as the first argument.
    """
    def update(self):
        # GRADING: LOOP_ALL
        for riverPart in self(0):
            # If the riverPart is a lock, update its water level right away.
            if isinstance(riverPart, Lock.Lock):
                riverPart.callBehavior()

            for boatLocation in riverPart:
                # Moving a boat is different if it is in a Section or a Lock.
                if isinstance(riverPart, Section.Section):
                    # Move the boat forward in the section/out of the section.
                    # We will need to account for boat behavior here.
                    # Two cases: boat moves within section, boat moves out of section.
                    # Find out how far the boat can move in its section.
                    safeMoves = riverPart.moveDistance(boatLocation)
                    # We either are only good to move part of the section, or move out of it.
                    # Determine which case we are, and if we move out of the section, determine
                    # if we can move into the next section.
                    toSectionEnd = riverPart.toEnd(boatLocation)
                    if safeMoves > toSectionEnd:
                        # We can theoretically move out of the section.  Is the next section a lock or another section?
                        sectionPosition = riverPart.getPosition()
                        sectionPosition += 1
                        # Is there even a next section?
                        if len(self.__sectionList) == sectionPosition:
                            # No next section exists.
                            # Remove the boat if it is at the end, or move it as far as it can.
                            if riverPart.atEnd(boatLocation):
                                # The boat is at the end.  We can remove it.
                                riverPart.moveBoatOut(boatLocation)
                            else:
                                # The boat is not at the end.  Move it as far as it can
                                boatMoves = riverPart.getBoat(boatLocation).callBehavior(riverPart)
                                if boatMoves <= toSectionEnd:
                                    # Boat can go to the end or fewer spaces.  Move it as far as it can go.
                                    riverPart.moveBoat(boatLocation + boatMoves, boatLocation)
                                else:
                                    # Boat can move beyond the end, so just move it to the end.
                                    riverPart.moveBoat(boatLocation + toSectionEnd, boatLocation)
                        else:
                            # We have to wait in front of locks and other sections.
                            # If we are not at the end of the section, we cannot move out of the section.
                            # If we are at the end, we can move in if the lock/next section is ready.
                            if riverPart.atEnd(boatLocation):
                                # The boat is at the end.  See if the lock is ready.
                                if self.__sectionList[sectionPosition].canMoveIn(boatLocation):
                                    # Can move in, so do so.
                                    boat = riverPart.moveBoatOut(boatLocation)
                                    self.__sectionList[sectionPosition].moveBoatIn(boat, 0)
                            else:
                                # The boat is not at the end. Move as far as we can in the current section.
                                boatMoves = riverPart.getBoat(boatLocation).callBehavior(riverPart)
                                if boatMoves <= toSectionEnd:
                                    # Boat can go to the end or less.  So, move as far as it can.
                                    riverPart.moveBoat(boatLocation + boatMoves, boatLocation)
                                else:
                                    # Boat can go past end.  Just move it to the end.
                                    riverPart.moveBoat(boatLocation + toSectionEnd, boatLocation)
                    else:
                        # The boat's maximum safe moves keeps it inside the section.
                        # So, move as far as the boat can go, up to the number of safe moves.
                        boatMoves = riverPart.getBoat(boatLocation).callBehavior(riverPart)
                        if boatMoves <= safeMoves:
                            # The boat can move the amount of safe moves or less, so move as far as it can go.
                            riverPart.moveBoat(boatLocation + boatMoves, boatLocation)
                        else:
                            # The boat can move beyond the safe moves, so move it the number of safe moves.
                            riverPart.moveBoat(boatLocation + safeMoves, boatLocation)
                else:
                    # Move the boat out of the lock, if possible.
                    # First, see if the boat can exit the lock (water level is right).
                    if riverPart.canMoveOut(boatLocation):
                        # Next, is the next section able to take the boat?
                        sectionPosition = riverPart.getPosition()
                        sectionPosition += 1
                        # Is there a next section? Or can the boat leave the river?
                        if len(self.__sectionList) == sectionPosition:
                            # No next section, remove the boat and you're done.
                            riverPart.moveBoatOut(boatLocation)
                        else:
                            # There is a section, see if we can move into it.
                            if self.__sectionList[sectionPosition].canMoveIn(0):
                                # We can move in to the next section, so do so.
                                boat = riverPart.moveBoatOut(boatLocation)
                                self.__sectionList[sectionPosition].moveBoatIn(boat, 0)
        return