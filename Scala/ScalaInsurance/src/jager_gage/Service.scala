package jager_gage

import scala.io.StdIn
import scala.xml._
import scala.collection.mutable

/**
 * A subclass in the Insurance hierarchy.
 * Directly under CarShop.scala.
 * Extends the traits RDP and COR to ensure many
 * functions are present / enforce homogenization
 * between this and other Insurance-related classes.
 *
 * @author Gage Jager
 */
class Service extends RDP, COR {
  var code = ""
  private var desc = ""

  /**
   * This function is utilized in finding and printing all
   * vehicles of a specific make in a specific zip code.
   *
   * This class' implementation is not called normally, but if it
   * was, it would just return nothing, because services don't have vehicles.
   *
   * @author Gage Jager
   * @param vehicle a string representing the vehicle make to find
   *                (not used by this class' implementation)
   * @return nothing / Unit
   */
  def findVehicles(vehicle: String): Unit = {
    return
  }

  /**
   * This function is utilized in finding a specific service code
   * anywhere in the instance.  Only the first zip code
   * containing a shop that has the specified service is printed.
   * Any later zip codes or car shops that also have the service
   * are skipped.
   *
   * This class' implementation compares the instance's code to the
   * codeToFind.  If they match, a message is printed saying that the
   * code was found in the zip code passed in, and true is returned.
   * If they do not match, false is returned.
   *
   * @author Gage Jager
   * @param codeToFind a string representing the service code to find
   * @param zip        an Int representing the current zip code
   * @return a Boolean representing if we found the service or not.
   */
  def findService(codeToFind: String, zip : Int): Boolean = {
    if (codeToFind == code) {
      println(codeToFind + " found in " + zip.toString)
      return true
    }
    else return false
  }

  /**
   * This function is utilized in calculating the total
   * value of all vehicles insured in a specific zip code.
   *
   * This class' implementation is not called normally, but if it was,
   * it would just return 0.0, because Services to not contain Vehicle data.
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
   * This class' implementation is not normally called, but if it was,
   * it simply returns 0.0, because Services do not hold data important
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
   * This class' implementation asks the user for the
   * service's code and description, and sets the
   * instance's variables to the user's input.
   *
   * @author Gage Jager
   * @return nothing / Unit
   */
  def addData() : Unit = {
    print("Code:> ")

    var temp = StdIn.readLine()
    while (temp.isEmpty)
      temp = StdIn.readLine()

    code = temp

    print("Description:> ")

    temp = StdIn.readLine()
    while (temp.isEmpty)
      temp = StdIn.readLine()

    desc = temp
  }

  /**
   * This function is used to output the insurance instance
   * as a string.
   *
   * This class' implementation first produces the indent string
   * by multiplying "  " by the passed in indentLevel.
   * Then, creates a String representation of the instance's data.
   * This String is then returned.
   *
   * @author Gage Jager
   * @param indentLevel an Int specifying the number of indents we should include
   * @return a String representation of the instance
   */
  def getData(indentLevel : Int) : String = {
    val indent = "  " * indentLevel
    val serviceString = indent + "(" + code + ") " + desc + "\n"
    return serviceString
  }

  /**
   * This function is used to output the insurance instance
   * to an XML file.
   *
   * This class' implementation first makes a hash map
   * to correlate the instance's code to the "code" attribute,
   * then creates a Text node containing the instance's description.
   * Then, it calls the XMLHelper makeNode function, passing in the tag
   * "CarService", the hash map of attributes, and the Text node data.
   * The node it generates is then returned.
   *
   * @author Gage Jager
   * @return an XML Elem representation of the instance
   */
  def writeXML() : Elem = {
    val attr : mutable.HashMap[String, String] = mutable.HashMap(("code", code))
    val text = Text(desc)
    val serviceNode = XMLHelper.makeNode("CarService", attr, text)
    return serviceNode
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
    val nodeCode = node.attribute("code").getOrElse("").toString
    val nodeDesc = node.text
    code = nodeCode
    desc = nodeDesc
  }
}
