package jager_gage

import scala.util.boundary
import boundary.break
import scala.io.StdIn
import scala.xml._
import scala.collection.mutable

/**
 * A subclass in the Insurance hierarchy.
 * Directly under Insurance.scala.
 * Extends the traits RDP and COR to ensure many
 * functions are present / enforce homogenization
 * between this and other Insurance-related classes.
 *
 * @author Gage Jager
 */
class Zip(zip : Int) extends RDP, COR {
  var zipCode : Int = zip
  var children = new Array[RDP](0)

  /**
   * This function is utilized in finding and printing all
   * vehicles of a specific make in a specific zip code.
   *
   * This class' implementation asks the user for the vehicle
   * make, then calls findVehicles on every object in its
   * children Array, passing the user's specified vehicle in.
   *
   * @author Gage Jager
   * @param vehicle a string representing the vehicle make to find
   *                (not used by this class' implementation)
   * @return nothing / Unit
   */
  def findVehicles(vehicle : String) : Unit = {
    print("Vehicle:> ")

    var userVehicle = StdIn.readLine()
    while (userVehicle.isEmpty)
      userVehicle = StdIn.readLine()

    for (e <- children) {
      e.findVehicles(userVehicle)
    }
  }

  /**
   * This function is utilized in finding a specific service code
   * anywhere in the instance.  Only the first zip code
   * containing a shop that has the specified service is printed.
   * Any later zip codes or car shops that also have the service
   * are skipped.
   *
   * This class' implementation calls the findService function
   * on every object in the children Array, passing in the codeToFind
   * and the zip.  If any call to findService returns true, the
   * loop will break and return true early.  If no call returns true,
   * then false is returned.
   *
   * @author Gage Jager
   * @param codeToFind a string representing the service code to find
   * @param zip        an Int representing the current zip code
   * @return a Boolean representing if we found the service or not.
   */
  def findService(codeToFind: String, zip : Int): Boolean = {
    var found = false

    boundary:
      for (e <- children) {
        found = e.findService(codeToFind, zip)
        if found then break(true)
      }
      return false
  }

  /**
   * This function is utilized in calculating the total
   * value of all vehicles insured in a specific zip code.
   *
   * This class' implementation calls the valueInsured function
   * on every object in the children Array, and adds their
   * return values to a Double, which was initialized to 0.0.
   * After the loop concludes, the Double is returned.
   *
   * @author Gage Jager
   * @return a Double containing the totaled value.
   */
  def valueInsured(): Double = {
    var total = 0.0

    for (e <- children) {
      total += e.valueInsured()
    }
    return total
  }

  /**
   * This function is utilized in calculating the monthly
   * insurance payment for a specific person in a specific
   * zip code.
   *
   * This class' implementation first asks the user for the
   * name of the owner they want, then begins a loop where it
   * calls the insuranceFor function on every object in the
   * children Array, passing in the specified name,
   * and adds their return values in a Double.
   * Afterwards, the Double is returned.
   *
   * @author Gage Jager
   * @param nameToFind the name of the owner whose insurance we are calculating
   * @return a Double containing the payment value.
   */
  def insuranceFor(nameToFind : String): Double = {
    print("What Owner:> ")

    var temp = StdIn.readLine()
    while (temp.isEmpty)
      temp = StdIn.readLine()

    val name = temp.toLowerCase

    var payment = 0.0

    for (e <- children) {
      payment += e.insuranceFor(name)
    }

    return payment
  }

  /**
   * This function is adding data to the insurance instance.
   *
   * This class' implementation first enters a loop where it
   * asks the user for what element they wish to add.
   * If they enter "c" or "car shop", any casing, a new
   * CarShop instance is created.  The addData function is
   * called on the new CarShop, and it is appended to the
   * children Array, before printing a message confirming
   * its addition.  If they enter "o" or "owner", any casing,
   * a new Owner instance is created.  The addData function is
   * called on the new Owner, and it is appended to the children
   * Array, before printing a message confirming its addition.
   * In either case, the user is then asked if they want to add
   * another element.  If they enter anything but "N" or "n",
   * the loop continues.  Upon entering "N" or "n", the loop ends.
   *
   * @author Gage Jager
   * @return nothing / Unit
   */
  def addData() : Unit = {
    var cont = ""

    while (cont != "n") {
      print("What Element (Owner, Car Shop ):> ")

      var temp = StdIn.readLine()
      while (temp.isEmpty)
        temp = StdIn.readLine()

      val choice = temp.toLowerCase

      if (choice == "c" || choice == "car shop") then {
        var newShop = new CarShop
        newShop.addData()
        children :+= newShop
        println("Added Car Shop")
      }
      else if (choice == "o" || choice == "owner") then {
        var newOwner = new Owner
        newOwner.addData()
        children :+= newOwner
        println("Added Owner")
      }

      print("Add another Zip Code element (y/n):> ")

      temp = StdIn.readLine()
      while (temp.isEmpty)
        temp = StdIn.readLine()

      cont = temp.toLowerCase()

      print("\n")
    }
  }

  /**
   * This function is used to output the insurance instance
   * as a string.
   *
   * This class' implementation loops through every object in
   * the children Array, calling getData on every object, and passing
   * in indentLevel to each one.  The returned String is added to the
   * instance's String representation, and once every object has been
   * added, the String representation is returned.
   *
   * @author Gage Jager
   * @param indentLevel an Int specifying the number of indents we should include
   * @return a String representation of the instance
   */
  def getData(indentLevel : Int) : String = {
    var zipString = ""
    for (e <- children) {
      zipString = zipString + e.getData(indentLevel)
    }
    return zipString
  }

  /**
   * This function is used to output the insurance instance
   * to an XML file.
   *
   * This class' implementation first makes a hash map
   * to correlate the instance's zipCode to the "code" attribute,
   * then calls the writeXML function on every object
   * in the children Array using a map function to
   * create all the children XML data.  Then, it calls
   * the XMLHelper makeNode function, passing in the tag
   * "ZipCode", the hash map of attributes, and the children XML data.
   * The node it generates is then returned.
   *
   * @author Gage Jager
   * @return an XML Elem representation of the instance
   */
  def writeXML() : Elem = {
    val attr : mutable.HashMap[String, String] = mutable.HashMap(("code", zipCode.toString))
    val childXML = children.map(x => x.writeXML())
    val zipNode = XMLHelper.makeNode("ZipCode", attr, childXML)
    return zipNode
  }

  /**
   * This function is used to read an insurance instance
   * from an XML file.  If the instance already contains
   * data, the XML file's data is simply appended to the
   * instance, with no checks on if the data already exits.
   *
   * This class' implementation first gets all the node's
   * children using the node.child function.  Then, for
   * every child, we first obtain its tag.  If the tag
   * is not "CarShop" or "Owner", that child is ignored.
   * If the tag is one of those two, we create a new instance
   * of the corresponding type, then call its readXML
   * function, passing in the current child node.
   * Once it returns, the new instance is added to the children Array.
   * Once every child has been processed, the function ends.
   *
   * @author Gage Jager
   * @param node an XML Node containing some data
   * @return nothing / Unit
   */
  def readXML(node: Node) : Unit = {
    val nodeChildren = node.child
    for (child <- nodeChildren) {
      val tag = child.label

      if (tag == "CarShop") {
        var newShop = new CarShop
        newShop.readXML(child)
        children :+= newShop
      }
      else if (tag == "Owner") {
        var newOwner = new Owner
        newOwner.readXML(child)
        children :+= newOwner
      }
    }
  }
}
