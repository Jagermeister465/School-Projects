package jager_gage

/**
 * A trait that all insurance-related classes extend.
 * 
 * Contains an abstract function for finding a service,
 * included both here and in RDP.scala for a reason
 * I explain in Zip.scala's implementation.
 *
 * @author Gage Jager
 */
trait COR {
  def findService(codeToFind : String, zip : Int) : Boolean
}
