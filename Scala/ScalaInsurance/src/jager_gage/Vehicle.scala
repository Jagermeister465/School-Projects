package jager_gage

import scala.io.StdIn
import scala.xml._
import scala.collection.mutable

/**
 * A subclass in the Insurance hierarchy.
 * Directly under Owner.scala, and sibling to Claim.scala.
 * Extends the traits RDP and COR to ensure many
 * functions are present / enforce homogenization
 * between this and other Insurance-related classes.
 *
 * @author Gage Jager
 */
class Vehicle extends RDP, COR {
  private var make = ""
  private var model = ""
  private var year = ""
  private var value = 0

  /**
   * This function is utilized in finding and printing all
   * vehicles of a specific make in a specific zip code.
   *
   * This class' implementation compares the instance's make
   * to the passed in make.  If the makes match, the getData
   * function is called with an indent of 0 to print the vehicle's
   * data, and if the makes do not match, the function does nothing.
   *
   * @author Gage Jager
   * @param vehicle a string representing the vehicle make to find
   * @return nothing / Unit
   */
  def findVehicles(vehicle : String) : Unit = {
    if (vehicle == make) {
      val vehicleData = getData(0)
      print(vehicleData)
    }
  }

  /**
   * This function is utilized in finding a specific service code
   * anywhere in the instance.  Only the first zip code
   * containing a shop that has the specified service is printed.
   * Any later zip codes or car shops that also have the service
   * are skipped.
   *
   * This class' implementation is not called normally, but if it was,
   * it just returns false, because vehicles do not contain data on
   * services.
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
   * This class' implementation simply returns the instance's value.
   *
   * @author Gage Jager
   * @return a Double containing the totaled value.
   */
  def valueInsured(): Double = {
    return value
  }

  /**
   * This function is utilized in calculating the monthly
   * insurance payment for a specific person in a specific
   * zip code.
   *
   * This class' implementation is not normally called, but if it was,
   * it simply returns 0.0.  This is because while the vehicle value
   * is important to the payment calculation, we already have a function
   * that returns exactly that -- valueInsured.
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
   * This class' implementation asks the user
   * for the make, model, year, and value of the vehicle
   * they want to add, and sets the instance's corresponding
   * variables to the user's input.
   *
   * @author Gage Jager
   * @return nothing / Unit
   */
  def addData() : Unit = {
    print("Make:> ")

    var temp = StdIn.readLine()
    while (temp.isEmpty)
      temp = StdIn.readLine()

    make = temp

    print("Model:> ")

    temp = StdIn.readLine()
    while (temp.isEmpty)
      temp = StdIn.readLine()

    model = temp

    print("Year:> ")

    temp = StdIn.readLine()
    while (temp.isEmpty)
      temp = StdIn.readLine()

    year = temp

    print("Value:> ")

    temp = StdIn.readLine()
    while (temp.isEmpty)
      temp = StdIn.readLine()

    value = temp.toInt
  }

  /**
   * This function is used to output the insurance instance
   * as a string.
   *
   * This class' implementation first produces the indent string
   * by multiplying "  " by the passed in indentLevel.
   * Then, creates a String representation of the instance's data,
   * padding the data fields all to 10 characters.
   * This String is returned after all the data has been appended.
   *
   * @author Gage Jager
   * @param indentLevel an Int specifying the number of indents we should include
   * @return a String representation of the instance
   */
  def getData(indentLevel : Int) : String = {
    val indent = "  " * indentLevel
    val padding = "%1$-10s"
    var vehicleString = indent + "Vehicle: "
    vehicleString = vehicleString + "Make: " + padding.format(make)
    vehicleString = vehicleString + "Model: " + padding.format(model)
    vehicleString = vehicleString + "Year: " + padding.format(year)
    vehicleString = vehicleString + "Value: $" + padding.format(value) + "\n"
    return vehicleString
  }

  /**
   * This function is used to output the insurance instance
   * to an XML file.
   *
   * This class' implementation first makes a hash map
   * to correlate the instance's value to the "value" attribute,
   * its year to the "year" attribute, its model to the "model"
   * attribute, and its make to the "make" attribute.
   * Then, it calls the XMLHelper makeNode function, passing in the tag
   * "Vehicle" and the hash map of attributes.
   * The node it generates is then returned.
   *
   * @author Gage Jager
   * @return an XML Elem representation of the instance
   */
  def writeXML() : Elem = {
    var attr : mutable.HashMap[String, String] = mutable.HashMap(("value", value.toString))
    attr += ("year", year)
    attr += ("model", model)
    attr += ("make", make)
    val vehicleNode = XMLHelper.makeNode("Vehicle", attr)
    return vehicleNode
  }

  /**
   * This function is used to read an insurance instance
   * from an XML file.  If the instance already contains
   * data, the XML file's data is simply appended to the
   * instance, with no checks on if the data already exits.
   *
   * This class' implementation gets all the attributes from
   * the node, then sets the instance's variables to their
   * corresponding attribute's value.
   *
   * @author Gage Jager
   * @param node an XML Node containing some data
   * @return nothing / Unit
   */
  def readXML(node: Node) : Unit = {
    val nodeValue = node.attribute("value").getOrElse("0").toString.toInt
    val nodeYear = node.attribute("year").getOrElse("0").toString
    val nodeModel = node.attribute("model").getOrElse("").toString
    val nodeMake = node.attribute("make").getOrElse("").toString
    make = nodeMake
    model = nodeModel
    year = nodeYear
    value = nodeValue
  }
}
