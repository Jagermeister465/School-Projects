package jager_gage

import scala.io.StdIn
import scala.xml._
import scala.collection.mutable

/**
 * A subclass in the Insurance hierarchy.
 * Directly under Zip.scala, and sibling to CarShop.scala.
 * Extends the traits RDP and COR to ensure many
 * functions are present / enforce homogenization
 * between this and other Insurance-related classes.
 *
 * @author Gage Jager
 */
class Owner extends RDP, COR {
  var name  = ""
  private var vehicleChildren = new Array[Vehicle](0)
  private var claimChildren = new Array[Claim](0)
  private var inOrderChildren = new Array[RDP](0)

  /**
   * This function is utilized in finding and printing all
   * vehicles of a specific make in a specific zip code.
   *
   * This class' implementation calls the findVehicles function
   * for every object in the vehicleChildren Array, passing in
   * the user's specified vehicle make to each one.
   * I could run this on the full, inOrderChildren Array, but
   * since I made a vehicle-only one additionally for getData(),
   * I figured I may as well use it.
   *
   * @author Gage Jager
   * @param vehicle a string representing the vehicle make to find
   * @return nothing / Unit
   */
  def findVehicles(vehicle : String) : Unit = {
    for (e <- vehicleChildren) {
      e.findVehicles(vehicle)
    }
  }

  /**
   * This function is utilized in finding a specific service code
   * anywhere in the instance.  Only the first zip code
   * containing a shop that has the specified service is printed.
   * Any later zip codes or car shops that also have the service
   * are skipped.
   *
   * This class' implementation can be called by Zip's implementation,
   * but since Owners do not contain data on services, it always
   * returns false
   *
   * @author Gage Jager
   * @param codeToFind a string representing the service code to find
   *                   (not used by this class' implementation)
   * @param zip        an Int representing the current zip code
   *                   (not used by this class' implementation)
   * @return a Boolean representing if we found the service or not.
   */
  def findService(codeToFind: String, zip : Int): Boolean = {
    return false
  }

  /**
   * This function is utilized in calculating the total
   * value of all vehicles insured in a specific zip code.
   *
   * This class' implementation calls the valueInsured function
   * on every object in the vehicleChildren Array (again, I already
   * make it for use in getData(), so why not reuse it if I only
   * want vehicles.  Would work using inOrderChildren anyway.)
   * Their return values are added to a Double, and when the loop
   * is finished, this Double is returned.
   *
   * @author Gage Jager
   * @return a Double containing the totaled value.
   */
  def valueInsured(): Double = {
    var total = 0.0

    for (e <- vehicleChildren) {
      total += e.valueInsured()
    }
    return total
  }

  /**
   * This function is utilized in calculating the monthly
   * insurance payment for a specific person in a specific
   * zip code.
   *
   * This class' implementation declares variables for the owner's
   * vehicles' total value, the number of vehicles, and the number
   * of claims.  First, we loop over the vehicleChildren Array,
   * incrementing the number of vehicles and summing their values by
   * calling valueInsured.  Then, we loop over the claimChildren Array,
   * incrementing the number of claims.  With these three pieces
   * of data, we can use the payment formula to determine the
   * owner's monthly payment.  This calculated value is then returned.
   *
   * @author Gage Jager
   * @param nameToFind the name of the owner whose insurance we are calculating
   * @return a Double containing the payment value.
   */
  def insuranceFor(nameToFind : String): Double = {
    if (name.toLowerCase == nameToFind) {
      var vehicleValue = 0.0
      var vehicleCount = 0
      var claimCount = 0

      for (e <- vehicleChildren) {
        vehicleCount += 1
        vehicleValue += e.valueInsured()
      }
      for (e <- claimChildren) {
        claimCount += 1
      }

      val payment = (vehicleValue * 0.001) + (vehicleCount * 25) + (claimCount * vehicleValue * 0.002)

      return payment
    }
    else return 0.0
  }

  /**
   * This function is adding data to the insurance instance.
   *
   * This class' implementation first asks the user for the
   * name of the Owner, then sets the instance's name to the
   * user's entry.  Then, a loop is entered which asks what
   * element the user wants to add to the owner.  If they
   * enter "c" or "claim", any casing, a new Claim instance
   * is created.  The addData function is called on the new Claim,
   * and the Claim is added to both the claimChildren Array and the
   * inOrderChildren Array.  This is because writeXML() wants
   * all the children in order, even if there are mixed classes,
   * but getData() wants all Vehicles first, then all Claims.
   * A similar process is performed if the user enters "v" or
   * "vehicle", any casing, but instead of a new Claim instance,
   * we create a new Vehicle instance, and instead of adding it
   * to the claimChildren Array, we add it to the vehicleChildren
   * Array.  Regardless of the user's entry, they are next
   * asked if they want to add another element, and the loop
   * conditionally continues, so long as they enter anything but
   * "n" or "N".
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
      print("\nWhat Element (Vehicle, Claim):> ")

      temp = StdIn.readLine()
      while (temp.isEmpty)
        temp = StdIn.readLine()

      val choice = temp.toLowerCase

      if (choice == "c" || choice == "claim") then {
        var newClaim = new Claim
        newClaim.addData()
        claimChildren :+= newClaim
        inOrderChildren :+= newClaim
        println("Added Claim")
      }
      else if (choice == "v" || choice == "vehicle") then {
        var newVehicle = new Vehicle
        newVehicle.addData()
        vehicleChildren :+= newVehicle
        inOrderChildren :+= newVehicle
        println("Added Vehicle")
      }

      print("Add another Owner element (y/n):> ")

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
   * Then, it adds a starting "*******" line to the String representation
   * before calling the getData function on every Vehicle in the
   * vehicleChildren Array, then on every Claim in the claimChildren Array,
   * passing in an incremented indentLevel to each one.  Once every
   * Vehicle and Claim has been added to the String representation,
   * that String has a closing "******" line appended, before being returned.
   *
   * @author Gage Jager
   * @param indentLevel an Int specifying the number of indents we should include
   * @return a String representation of the instance
   */
  def getData(indentLevel : Int) : String = {
    val indent = "  " * indentLevel
    var ownerString = indent + "*****************************************************\n"
    ownerString = ownerString + indent + name + "\n"
    ownerString = ownerString + indent + "Vehicle(s)" + "\n"

    if (vehicleChildren.isEmpty) {
      ownerString = ownerString + "\n"
    }
    else {
      for (e <- vehicleChildren) {
        ownerString = ownerString + e.getData(indentLevel + 1)
      }
    }

    ownerString = ownerString + indent + "Claim(s)" + "\n"
    
    if (claimChildren.isEmpty) {
      ownerString = ownerString + "\n"
    }
    else {
      for (e <- claimChildren) {
        ownerString = ownerString + e.getData(indentLevel + 1)
      }
    }
    
    ownerString = ownerString + indent + "*****************************************************\n"
    return ownerString
  }

  /**
   * This function is used to output the insurance instance
   * to an XML file.
   *
   * This class' implementation first makes a hash map
   * to correlate the instance's name to the "name" attribute,
   * then calls the writeXML function on every object
   * in the vehicleChildren Array and the claimChildren Array
   * using a map function to create all the children XML data.
   * The two sets of XML data are appended to create the full
   * children XML data.  Then, it calls the XMLHelper makeNode
   * function, passing in the tag "Owner", the hash map of attributes,
   * and the children XML data.
   * The node it generates is then returned.
   *
   * @author Gage Jager
   * @return an XML Elem representation of the instance
   */
  def writeXML() : Elem = {
    val attr : mutable.HashMap[String, String] = mutable.HashMap(("name", name))
    val vehicleChildXML = vehicleChildren.map(x => x.writeXML())
    val claimChildXML = claimChildren.map(x => x.writeXML())
    val childXML = vehicleChildXML ++ claimChildXML
    val ownerNode = XMLHelper.makeNode("Owner", attr, childXML)
    return ownerNode
  }

  /**
   * This function is used to read an insurance instance
   * from an XML file.  If the instance already contains
   * data, the XML file's data is simply appended to the
   * instance, with no checks on if the data already exits.
   *
   * This class' implementation first gets this node's
   * "name" attribute, and sets the instance's name varible
   * to the attribute's value.  Then, we get all the node's
   * children using the node.child function.  Then, for
   * every child, we first obtain its tag.  If the tag
   * is not "Claim" or "Vehicle", that child is ignored.  If the tag
   * is either of those, we create a new instance of the
   * corresponding type, then call its readXML
   * function, passing in the current child node.
   * Once it returns, the new instance is added to both the
   * inOrderChildren Array and the corresponding typeChildren Array.
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

      if (tag == "Claim") {
        var newClaim = new Claim
        newClaim.readXML(child)
        claimChildren :+= newClaim
        inOrderChildren :+= newClaim
      }
      else if (tag == "Vehicle") {
        var newVehicle = new Vehicle
        newVehicle.readXML(child)
        vehicleChildren :+= newVehicle
        inOrderChildren :+= newVehicle
      }
    }
  }
}
