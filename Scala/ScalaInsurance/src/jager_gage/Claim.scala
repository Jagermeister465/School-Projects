package jager_gage

import scala.io.StdIn
import scala.xml._

/**
 * A subclass in the Insurance hierarchy.
 * Directly under Owner.scala, and sibling to Vehicle.scala.
 * Extends the traits RDP and COR to ensure many
 * functions are present / enforce homogenization
 * between this and other Insurance-related classes.
 *
 * @author Gage Jager
 */
class Claim extends RDP, COR {
  private var date = ""

  /**
   * This function is utilized in finding and printing all
   * vehicles of a specific make in a specific zip code.
   *
   * This class' implementation is not called normally, but if it
   * was, it would just return nothing, because claims don't have vehicles.
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
   * This class' implementation is not called normally, but if it was,
   * it just returns false, because claims do not contain data on
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
   * This class' implementation is not called normally, but if it was,
   * it would just return 0.0, because Claims to not contain Vehicle data.
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
   * it simply returns 0.0, because a claim does not hold any data
   * important to calculating the payment, we only care about how many
   * claims there are total.
   *
   * @author Gage Jager
   * @param nameToFind the name of the owner whose insurance we are calculating
   *                   (not used in this class' implementation)
   * @return a Double containing the payment value.
   */
  def insuranceFor(nameToFind: String): Double = {
    return 0.0
  }

  /**
   * This function is adding data to the insurance instance.
   *
   * This class' implementation asks the user for the
   * date the claim was filed, and sets the instance's
   * date variable to the user's input.
   *
   * @author Gage Jager
   * @return nothing / Unit
   */
  def addData() : Unit = {
    print("Date:> ")

    var temp = StdIn.readLine()
    while (temp.isEmpty)
      temp = StdIn.readLine()

    date = temp
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
    val claimString = indent + "Claim: " + date + "\n"
    return claimString
  }

  /**
   * This function is used to output the insurance instance
   * to an XML file.
   *
   * This class' implementation first makes a Text node containing
   * the instance's date data.  It then calls the XMLHelper
   * makeNode function, passing in the tag "date", null attributes,
   * and the text node, to create the child XML data.
   * Then, it calls the XMLHelper makeNode function, passing in the tag
   * "Claim", null attributes, and the children XML data.
   * The node it generates is then returned.
   *
   * @author Gage Jager
   * @return an XML Elem representation of the instance
   */
  def writeXML() : Elem = {
    val text = Text(date)
    val childXML = XMLHelper.makeNode("date", null, text)
    val claimNode = XMLHelper.makeNode("Claim", null, childXML)
    return claimNode
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
   * is not "date", that child is ignored.  If the tag
   * is "date", we obtain the text contained in the "date" tag,
   * and set this instance's date variable to the text.
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

      if (tag == "date") {
        val nodeText = child.text
        date = nodeText
      }
    }
  }
}
