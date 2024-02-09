package jager_gage

import scala.xml._

/**
 * A trait that all insurance-related classes extend.
 * 
 * Contains abstract definitions for functions to
 * find specific vehicle types, find a service (included here
 * and in COR.scala for a reason I explain in Zip.scala's implementation),
 * calculate the value of insured vehicles, calculate the insurance
 * payment for an owner, add data, get data (which functions similarly to
 * a toString override), write data to an XML file, and read data from an
 * XML file.
 *
 * @author Gage Jager
 */
trait RDP {
  def findVehicles(vehicle : String) : Unit
  def findService(codeToFind: String, zip : Int) : Boolean
  def valueInsured() : Double
  def insuranceFor(nameToFind : String) : Double
  def addData() : Unit
  def getData(indentLevel : Int) : String
  def writeXML() : Elem
  def readXML(node : Node) : Unit
}
