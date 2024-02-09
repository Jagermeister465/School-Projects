/**
 * Every Line with a * has its grading tag (if reached): ____yes (but no PARALLEL or INSURANCE tags)____
 *
 *  0. Got it running	6			____yes____
 *
 *  1.	Add + Display*	36
 *  Prompts correct 				___yes___
 *  Adds each item 					___yes___
 *  Above displays correctly formatted 		____yes____
 *
 *
 *  2A) Remove + Display*	10
 *  Prompts correct					___yes___
 *  Removes and displays correctly 			___yes___
 *
 *
 *  2B) Add + XML save*	14
 *  Console added items saved correctly 		___yes___
 *  Console added multiples is saved correctly 	___yes___
 *
 *
 *  2C) XML load + XML save*	14
 *  1 XML file loaded and saved correctly 		___yes___
 *  2+ XML file loaded and saved correctly		___yes___
 *
 *
 *  2D) XML load + Display*	12
 *  1 XML file loaded and displays correctly 	___yes___
 *  2+ XML file loaded and displays correctly	___yes___
 *
 *
 *  2E) XML+ Display with bad file handing	10
 *  All errors handled 				____yes____
 *
 *
 *  3.	Stress test for above*	12
 *  Loads in file, adds data, and displays/saves correctly		___yes___
 *  Appends a file and displays/saves correctly 			___yes___
 *  Removes elements after edits, and displays/saves correctly 	____yes____
 *
 *
 *  4. Find cars*	9
 *  RDP format at least there				___yes___
 *  Lists cars						___yes___
 *  Formatting						___yes___
 *  Handles “not found case”				___yes___
 *
 *
 *  5. Find service*	14
 *  CoR format at least there				___yes____
 *  First item found and output formatted correctly		___yes___
 *  Handles “not found case”				____yes_______
 *
 *
 *  6a.  Total Insured 	7				___yes___
 *  Correct with no claims					___yes___
 *  Correct with claims 					___yes___
 *  Parallelized* 						___NO___
 *
 *
 *  6b.  Insured For 	9				___yes___
 *  Correct with no claims					___yes___
 *  Correct with claims 					___yes___
 *  Parallelized* 						___NO___
 */

package jager_gage

import java.io.FileWriter
import java.text.DecimalFormat
import scala.io.StdIn
import scala.xml.XML

/**
 * Holds the Main function of the program.
 *
 * Main creates a new Insurance instance before entering
 * the menu loop.  Contains functions to Add Data,
 * Display Data, Remove a Zip, Load an XML file,
 * Write an XML file, Find Cars in a Zip, Find a Service code,
 * calculate the Value Insured in a zip, and calculate an
 * Insurance payment for a specific owner.
 *
 * @author Dr. Lisa Rebenitsch and Gage Jager
 */
object MainStarter {
    def main(args: Array[String]): Unit = {
        val menu: String =
            """
              |1) Add Data
              |2) Display Data
              |3) Remove Zip
              |4) Load XML
              |5) Write XML
              |6) Find a Cars of Make in Zip
              |7) Find a Service
              |8) Total Value Insured
              |9) Insurance For
              |0) Quit
              |
              |Choice:> """.stripMargin
        var choice : Any = -1
        var temp = ""

        var insurance = new Insurance()

        while (choice != "0") {
            print(menu)

            //something to strip out empty lines
            temp = StdIn.readLine()
            while (temp.isEmpty)
                temp = StdIn.readLine()

            choice = temp

            if (choice == "1") {
              // GRADING: ADD
              insurance.addData()

            }
            else if (choice == "2") {
              // GRADING: PRINT
              println(insurance.getData(0))
            }
            else if (choice == "3") {
              print("What Zip Code:> ")

              temp = StdIn.readLine()
              while (temp.isEmpty)
                temp = StdIn.readLine()

              val zip = temp.toInt

              if insurance.searchForZip(zip) then {
                insurance.removeData(zip)
                println("Removed " + zip)
              }
              else {
                println("Zip Code not found")
              }
            }
            else if (choice == "4") {
              print("File name:> ")

              temp = StdIn.readLine()
              while (temp.isEmpty)
                temp = StdIn.readLine()

              val fileName = temp

              try {
                val topNode = XML.loadFile(fileName)

                if (topNode.label != "InsuranceData") {
                  println("Invalid XML file. Needs to be an InsuranceData XML file")
                }
                else {
                  // GRADING: READ
                  insurance.readXML(topNode)
                }
              }
              catch {
                case e : java.io.FileNotFoundException => println("Could not open file: " + fileName + " (The system cannot find the file specified)")
              }
            }
            else if (choice == "5") {
              print("File name:> ")

              temp = StdIn.readLine()
              while (temp.isEmpty)
                temp = StdIn.readLine()

              val fileName = temp

              // GRADING: WRITE
              val xmlTree = insurance.writeXML()

              val prettyPrinter = new scala.xml.PrettyPrinter(80, 2)
              val prettyXml = prettyPrinter.format( xmlTree)
              val write = new FileWriter( fileName )
              write.write( prettyXml)
              write.close()
            }
            else if (choice == "6") {
              // GRADING: VEHICLE
              insurance.findVehicles("placeholder")
            }
            else if (choice == "7") {
              print("Car Service:> ")

              var codeToFind = StdIn.readLine()
              while (codeToFind.isEmpty)
                codeToFind = StdIn.readLine()

              // GRADING: SERVICE
              val found = insurance.findService(codeToFind, 0)

              if (!found) {
                println(codeToFind + " not found")
              }
            }
            else if (choice == "8") {
              val pattern = new DecimalFormat("'$'###,###,###,###,###,##0.00")
              val amount = insurance.valueInsured()
              println("Value: " + pattern.format(amount))
            }
            else if (choice == "9") {
              val pattern = new DecimalFormat("'$'###,###,###,###,###,##0.00")
              val amount = insurance.insuranceFor("placeholder")
              println("Monthly payment: " + pattern.format(amount))
            }
        }
    }
}

