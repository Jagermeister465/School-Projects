from jager_gage import River

"""
description: Gonna be real, the OOP diagram having two "RiverSystem"s was a bit confusing to me.
             And then, as I come to write these description blocks, I go check and see the diagram
             on D2L was updated, so my screenshot I had been using was out of date for who knows how
             long.
             From the new diagram, it seems I've basically implemented "RiverSystem" through my River.py.
             And, given this is due in around 16 hours, I don't feel like refactoring everything.
             Sorry this isn't the way it should be, but I don't like my odds of fixing it.
             
             Anyway, this class only has two functions.  One makes the default starting river
             when the program is starter, and the other makes the tester river for menu option 6.
author: Gage Jager
"""
class RiverSystem:

    """
    description: Yeah the class has no variables to initialize and this realistically doesn't need to exist.
                 Again, sorry for not checking for an updated diagram and structuring the project wrong.
    author: Gage Jager
    parameters:
        self: All class functions must have and instance of themselves as the first argument.
    """
    def __init__(self):
        return

    """
    description: This function first resets the River class' section position variable,
                 so that the newly created sections are numbered properly.
                 Then, the functions to make the default starting river are called, with the in-progress
                 river stored in tempRiver.  This tempRiver is returned to give the caller access to 
                 the created river's data and functions.
    author: Gage Jager
    parameters:
        self: All class functions must have an instance of themselves as the first argument.
    returns:
        tempRiver: A temporary River object to hold the river as we construct it.
    """
    def makeStarterRiver(self):
        River.River.POSITION = -1
        tempRiver = River.River()
        tempRiver.addSection(6, 0)
        tempRiver.addLock(0, 1)
        tempRiver.addSection(3, 1)
        return tempRiver

    """
    description: This function first resets the River class' section position variable,
                 so that the newly created sections are numbered properly.
                 Then, the functions to make the testing river from menu option 6 are called,
                 with the in-progress river stored in tempRiver.  This tempRiver is returned
                 to give the caller access to the created river's data and functions.
    author: Gage Jager
    parameters:
        self: All class functions must have an instance of themselves as the first argument.
    returns:
        tempRiver: A temporary River object to hold the river as we construct it.
    """
    def makeTesterRiver(self):
        River.River.POSITION = -1
        tempRiver = River.River()
        tempRiver.addSection(5, 0)
        tempRiver.addLock(0, 1)
        tempRiver.addSection(6, 2)
        tempRiver.addLock(2, 2)
        tempRiver.addSection(3, 3)
        tempRiver.addLock(5, 3)
        return tempRiver