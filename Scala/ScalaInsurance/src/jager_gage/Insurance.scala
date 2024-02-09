package jager_gage

import scala.util.boundary
import boundary.break
import scala.io.StdIn
import scala.xml._

/**
 * The topmost class in the Insurance hierarchy.
 * Extends the traits RDP and COR to ensure many
 * functions are present / enforce homogenization
 * between this and other Insurance-related classes.
 *
 * @author Gage Jager
 */
class Insurance extends RDP, COR {
  private var zips = new Array[Zip](0)

  /**
   * This function takes in an Int and searches
   * the instance's zips Array to see if there is
   * a match present.
   *
   * @author Gage Jager
   * @param zipToFind the zip code we want to search for
   * @return a Boolean representing if we found the zip code
   */
  def searchForZip(zipToFind : Int) : Boolean = {
    boundary:
      for (e <- zips) {
        if (e.zipCode == zipToFind) then break(true)
      }
      return false
  }

  /**
   * This function is utilized in finding and printing all
   * vehicles of a specific make in a specific zip code.
   *
   * This class' implementation asks the user for the zip code,
   * then searches the zips Array for a zip code that matches
   * the user input.  If a match is found, processing continues
   * in that Zip instance's implementation.  If a match is not
   * found, "Vehicles Not Found" is printed instead, and the
   * function call ends.
   *
   * @author Gage Jager
   * @param vehicle a string representing the vehicle make to find
   *                (not used by this class' implementation)
   * @return nothing / Unit
   */
  def findVehicles(vehicle : String) : Unit = {
    print("Zip Code:> ")

    var temp = StdIn.readLine()
    while (temp.isEmpty)
      temp = StdIn.readLine()

    val zip = temp.toInt

    if (searchForZip(zip)) {
      for (e <- zips) {
        if (e.zipCode == zip) {
          e.findVehicles(vehicle)
        }
      }
    }
    else {
      println("Vehicles Not Found")
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
   * on every object in the zips Array, passing in the codeToFind,
   * and substituting the zip with the code of the instance being
   * processed.  If any call to findService returns true, the
   * loop will break and return true early.  If no call returns true,
   * then false is returned.
   *
   * @author Gage Jager
   * @param codeToFind a string representing the service code to find
   * @param zip an Int representing the current zip code
   *            (not used by this class' implementation)
   * @return a Boolean representing if we found the service or not.
   */
  def findService(codeToFind : String, zip : Int) : Boolean = {
    var found = false

    boundary:
      for (e <- zips) {
        found = e.findService(codeToFind, e.zipCode)
        if found then break(true)
      }
      return false
  }

  /**
   * This function is utilized in calculating the total
   * value of all vehicles insured in a specific zip code.
   *
   * This class' implementation first asks the user for the
   * zip code they want to total the value in, then begins a loop
   * where it searches for the specified zip code in the zips Array.
   * If it finds the zip code, it calls the valueInsured function
   * on that Zip object, and stores its return value in a Double.
   * Afterwards, the Double is returned, either containing its
   * initialized value of 0.0 or the calculated value returned
   * by the Zip instance's valueInsured.
   *
   * @author Gage Jager
   * @return a Double containing the totaled value.
   */
  def valueInsured() : Double = {
    print("What Zip Code:> ")

    var temp = StdIn.readLine()
    while (temp.isEmpty)
      temp = StdIn.readLine()

    val zipToTotal = temp.toInt

    var total = 0.0

    for (e <- zips) {
      if (e.zipCode == zipToTotal) {
        total += e.valueInsured()
      }
    }

    return total
  }

  /**
   * This function is utilized in calculating the monthly
   * insurance payment for a specific person in a specific
   * zip code.
   *
   * This class' implementation first asks the user for the
   * zip code they want, then begins a loop where it searches
   * for the specified zip code in the zips Array.
   * If it finds the zip code, it calls the insuranceFor function
   * on that Zip object, and stores its return value in a Double.
   * Afterwards, the Double is returned, either containing its
   * initialized value of 0.0 or the calculated value returned
   * by the Zip instance's insuranceFor.
   *
   * @author Gage Jager
   * @param nameToFind the name of the owner whose insurance we are calculating
   *                   (not used in this class' implementation)
   * @return a Double containing the payment value.
   */
  def insuranceFor(nameToFind : String) : Double = {
    print("What Zip Code:> ")

    var temp = StdIn.readLine()
    while (temp.isEmpty)
      temp = StdIn.readLine()

    val zip = temp.toInt

    var payment = 0.0

    for (e <- zips) {
      if (e.zipCode == zip) {
        payment += e.insuranceFor(nameToFind)
      }
    }

    return payment
  }

  /**
   * This function is adding data to the insurance instance.
   *
   * This class' implementation first asks the user for the
   * zip code they want to add, then searches the zips Array
   * to make sure it doesn't already exist.  If it does, an
   * error message is printed, and the function ends.
   * If it does not exist, a new Zip instance is created,
   * passing in the specified zip code to the constructor.
   * The addData function is called on the new Zip instance,
   * and then it is appended to the zips Array before the function ends.
   *
   * @author Gage Jager
   * @return nothing / Unit
   */
  def addData() : Unit = {
    print("What Zip Code:> ")

    var temp = StdIn.readLine()
    while (temp.isEmpty)
      temp = StdIn.readLine()

    val zip = temp.toInt

    if searchForZip(zip) then {
      println(zip + " is already in the database")
    }
    else {
      var newZip = new Zip(zip)
      newZip.addData()
      zips :+= newZip
    }
  }

  /**
   * This function is removing data from the insurance instance.
   *
   * The function takes in an Int specifying the zip code we want to
   * remove, and performs a filter operation on the zips Array
   * to keep every Zip instance whose zip code is not the specified
   * one.  The filtered Array is reassigned to the zips variable.
   *
   * @author Gage Jager
   * @param zipToRemove an Int specifying the Zip to remove
   * @return nothing / Unit
   */
  def removeData(zipToRemove : Int) : Unit = {
    val tempZips = zips.filter(_.zipCode != zipToRemove)
    zips = tempZips
  }

  /**
   * This function is used to output the insurance instance
   * as a string.
   *
   * This class' implementation first produces the indent string
   * by multiplying "  " by the passed in indentLevel.
   * Then, it loops through every Zip in the zips Array, adding
   * the "Zip Code: xxxxx" and "=======" line to the String representation
   * before calling getData on the specific Zip.
   * Once every Zip has been added to the String, it is returned.
   *
   * @author Gage Jager
   * @param indentLevel an Int specifying the number of indents we should include
   * @return a String representation of the instance
   */
  def getData(indentLevel : Int) : String = {
    var insuranceString = ""
    val indent = "  " * indentLevel
    for (e <- zips) {
      insuranceString = insuranceString + indent + "Zip Code: " + e.zipCode.toString + "\n"
      insuranceString = insuranceString + indent + "======================================================\n"
      insuranceString = insuranceString + indent + e.getData(indentLevel + 1)
    }
    return insuranceString
  }

  /**
   * This function is used to output the insurance instance
   * to an XML file.
   *
   * This class' implementation calls the writeXML function on
   * every object in the zips Array using a map function to
   * create all the children XML data.  Then, it calls
   * the XMLHelper makeNode function, passing in the tag
   * "InsuranceData", null attributes, and the children XML data.
   * The node it generates is then returned.
   *
   * @author Gage Jager
   * @return an XML Elem representation of the instance
   */
  def writeXML() : Elem = {
    val childXML = zips.map(x => x.writeXML())
    val insuranceNode = XMLHelper.makeNode("InsuranceData", null, childXML)
    return insuranceNode
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
   * is not "ZipCode", that child is ignored.  If the tag
   * is "ZipCode", we get the "code" attribute from the
   * child, and provide a default of "1" if the attribute
   * was not included.  We create a new Zip instance
   * using the "code" attribute, then call its readXML
   * function, passing in the current child node.
   * Once it returns, the new Zip is added to the zips Array.
   * Once every child has been processed, the function ends.
   *
   * @author Gage Jager
   * @param node an XML Node containing some data
   * @return nothing / Unit
   */
  def readXML(node : Node) : Unit = {
    val nodeChildren = node.child
    for (child <- nodeChildren) {
      val tag = child.label

      if (tag == "ZipCode") {
        val nodeCode = child.attribute("code").getOrElse("1").toString.toInt
        var newZip = new Zip(nodeCode)
        newZip.readXML(child)
        zips :+= newZip
      }
    }
  }
}
