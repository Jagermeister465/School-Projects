package jager_gage

import scala.util.boundary
import boundary.break
import scala.io.StdIn
import scala.xml._
import scala.collection.mutable

/**
 * A subclass in the Insurance hierarchy.
 * Directly under Zip.scala, and sibling to Owner.scala.
 * Extends the traits RDP and COR to ensure many
 * functions are present / enforce homogenization
 * between this and other Insurance-related classes.
 *
 * @author Gage Jager
 */
class CarShop extends RDP, COR {
  var name = ""
  var children = new Array[Service](0)

  /**
   * This function is utilized in finding and printing all
   * vehicles of a specific make in a specific zip code.
   *
   * This class' implementation can be called from Zip's implementation,
   * but since CarShops do not have vehicle information, it simply returns
   * nothing.
   *
   * @author Gage Jager
   * @param vehicle a string representing the vehicle make to find
   *                (not used by this class' implementation)
   * @return nothing / Unit
   */
  def findVehicles(vehicle : String) : Unit = {
    return
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
   * This class' implementation can be called by Zip's implementation,
   * but because CarShops do not hold Vehicle data, it just returns 0.0
   *
   * @author Gage Jager
   * @return a Double containing the totaled value.
   */
  def valueInsured(): Double = {
    return 0.0
  }

  /**
   * This function is utilized in calculating the monthly
   * insurance payment for a specific person in a specific
   * zip code.
   *
   * This class' implementation can be called by Zip's implementation,
   * but it simply returns 0.0, because CarShops do not hold data important
   * to calculating the insurance payment.
   *
   * @author Gage Jager
   * @param nameToFind the name of the owner whose insurance we are calculating
   *                   (not used in this class' implementation)
   * @return a Double containing the payment value.
   */
  def insuranceFor(nameToFind : String): Double = {
    return 0.0
  }

  /**
   * This function is adding data to the insurance instance.
   *
   * This class' implementation first asks the user for
   * the name of the Car Shop they want to add, and sets
   * the instance's name variable to that input.
   * Then, it enters a loop where it creates a new
   * Service instance, calls addData on it, and adds
   * it to the children Array.  It then asks the user
   * if they want to add another element, and will
   * continue looping until they enter "n" or "N".
   *
   * @author Gage Jager
   * @return nothing / Unit
   */
  def addData() : Unit = {
    print("Name:> ")

    var temp = StdIn.readLine()
    while (temp.isEmpty)
      temp = StdIn.readLine()

    name = temp

    var cont = ""

    while (cont != "n") {
      var newService = new Service
      newService.addData()
      children :+= newService
      print("Add another element (y/n):> ")

      temp = StdIn.readLine()
      while (temp.isEmpty)
        temp = StdIn.readLine()

      cont = temp.toLowerCase()
    }
  }

  /**
   * This function is used to output the insurance instance
   * as a string.
   *
   * This class' implementation first produces the indent string
   * by multiplying "  " by the passed in indentLevel.
   * Then, it adds a starting "......" line to the String representation
   * before calling the getData function on every Service in the
   * children Array, passing in an incremented indentLevel to each one.
   * Once every Service has been added to the String representation,
   * that String has a closing "......" line appended, before being returned.
   *
   * @author Gage Jager
   * @param indentLevel an Int specifying the number of indents we should include
   * @return a String representation of the instance
   */
  def getData(indentLevel : Int) : String = {
    val indent = "  " * indentLevel
    var carShopString = indent + ".....................................................\n"
    carShopString = carShopString + indent + "Car Shop: " + name + "\n"
    for (e <- children) {
      carShopString = carShopString + e.getData(indentLevel + 1)
    }
    carShopString = carShopString + indent + ".....................................................\n"
    return carShopString
  }

  /**
   * This function is used to output the insurance instance
   * to an XML file.
   *
   * This class' implementation first makes a hash map
   * to correlate the instance's name to the "name" attribute,
   * then calls the writeXML function on every object
   * in the children Array using a map function to
   * create all the children XML data.  Then, it calls
   * the XMLHelper makeNode function, passing in the tag
   * "CarShop", the hash map of attributes, and the children XML data.
   * The node it generates is then returned.
   *
   * @author Gage Jager
   * @return an XML Elem representation of the instance
   */
  def writeXML() : Elem = {
    val attr : mutable.HashMap[String, String] = mutable.HashMap(("name", name))
    val childXML = children.map(x => x.writeXML())
    val shopNode = XMLHelper.makeNode("CarShop", attr, childXML)
    return shopNode
  }

  /**
   * This function is used to read an insurance instance
   * from an XML file.  If the instance already contains
   * data, the XML file's data is simply appended to the
   * instance, with no checks on if the data already exits.
   *
   * This class' implementation first gets the "name"
   * attribute from the node, and sets the instance's
   * name variable to the attribute's value.  Then,
   * we get all the node's children using the
   * node.child function.  Then, for every child,
   * we first obtain its tag.  If the tag
   * is not "CarService", that child is ignored.  If the tag
   * is "CarService", we create a new Service instance,
   * then call its readXML function, passing in the current child node.
   * Once it returns, the new Service is added to the children Array.
   * Once every child has been processed, the function ends.
   *
   * @author Gage Jager
   * @param node an XML Node containing some data
   * @return nothing / Unit
   */
  def readXML(node: Node) : Unit = {
    val nodeName = node.attribute("name").getOrElse("").toString
    name = nodeName

    val nodeChildren = node.child
    for (child <- nodeChildren) {
      val tag = child.label

      if (tag == "CarService") {
        var newService = new Service
        newService.readXML(child)
        children :+= newService
      }
    }
  }
}
