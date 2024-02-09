/**
 * Author: Gage Jager
 * Class: CSC 461 Fall 2023
 * Description: This class is capable of handling multiple
 * ParkingLot's and FreeParkingLot's at the same time, and
 * provides a way to manipulate a collection of lots in
 * a single object.
 * The class includes an ArrayList for holding the parking lots,
 * and variables concerning the ID numbers of the lots,
 * the number of vehicles in the district, the time the district
 * closed, if the district is currently closed, and how long the
 * district has been closed.
 * The methods inside include a default constructor,
 * vehicle entry and exit methods, a way to add new lots and
 * get their indexes, and various statistic displaying methods,
 * such as ones to find the time the lots are closed, if the lots
 * are currently closed, the profit of the lots, and the status
 * of each individual lot.
 * Last Tier Passed: 11
 */

/*

Additional OOP  requirements
    toString properly extended				__yes__
    Constructors properly handled			__yes__
    Access properly handled (code style requirement)	__yes__

Last tier completed: ____11_(all)____

 */

package jager_gage;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class District {

    private int lotID;
    private int vehiclesInDistrict;
    private int minSinceOpen;
    private int minClosed;
    private boolean closed;
    private int timeClosed;
    private ArrayList<ParkingLot> listOfLots = new ArrayList<ParkingLot>();


    /**
     * This is the default District constructor.
     * This sets the starting values of the class variables.
     * Since it has no inputs, there are only default empty Districts.
     */
    public District() {
        this.lotID = -1;
        this.vehiclesInDistrict = 0;
        this.minSinceOpen = 0;
        this.minClosed = 0;
        this.closed = false;
        this.timeClosed = 0;
    }

    /**
     * This method marks a vehicle's entry to a lot.
     * The method takes in an int representing the ID of the lot,
     * and the time at which the vehicle is entering.
     * It then calls the entry method of the lot type
     * associated with the ID.
     * Keep in mind that the FreeParkingLot entry method
     * is exactly the same as the ParkingLot version.
     *
     * @param lot An int identifying which lot the vehicle is entering.
     * @param currentTime The time in minutes since the district opened.
     * @return An int corresponding to the ID of the entered vehicle in the corresponding lot.
     */
    public int markVehicleEntry(int lot, int currentTime) {
        if(currentTime < minSinceOpen) {
            //Cannot log event before an event that has already occurred.
            return -1;
        }

        //If the district is currently and was previously closed,
        //add time to minClosed.
        if( this.isClosed() && closed ) {
            minClosed = minClosed + (currentTime - timeClosed);
            timeClosed = currentTime;
        }
        //Actually adding the vehicle (or at least attempting to).
        int vehicleID = listOfLots.get(lot).markVehicleEntry(currentTime);

        if (vehicleID != -1) {
            vehiclesInDistrict = vehiclesInDistrict + 1;
        }

        //If the district became closed from this entry, mark the district closed.
        if( this.isClosed() ) {
            timeClosed = currentTime;
            closed = true;
        }

        return vehicleID;
    }

    /**
     * This method marks a vehicle's exit of a lot.
     * The method takes in the lot and vehicle IDs as well as
     * the current time, then passes the data to the ParkingLot
     * or FreeParkingLot exit method.
     *
     * @param lot The ID of the lot the vehicle is exiting.
     * @param currentTime The time in minutes since the district opened.
     * @param ID The ID of the vehicle that is leaving the lot.
     */
    public void markVehicleExit(int lot, int currentTime, int ID) {
        if(currentTime < minSinceOpen) {
            //Cannot log events before an event that has already occurred.
            return;
        }

        if( this.isClosed() ) {
            //Update the time closed before removing the vehicle.
            minClosed = minClosed + (currentTime - timeClosed);
            timeClosed = currentTime;
        }

        //This is scuffed, but I'm not thinking of a better way
        //to figure out if a vehicle successfully left or not.
        int preVehicleCount = listOfLots.get(lot).getVehiclesInLot();

        listOfLots.get(lot).markVehicleExit(currentTime, ID);

        //Part 2 of the scuff.
        if( preVehicleCount != listOfLots.get(lot).getVehiclesInLot() ) {
            vehiclesInDistrict = vehiclesInDistrict - 1;
        }

        if( !this.isClosed() ) {
            //If the exiting vehicle made the district open again,
            //mark it as not closed.
            closed = false;
        }
    }

    /**
     * This method is used to determine if the district is closed.
     * The method asks each lot individually if it is closed, and if
     * they are all closed, the district will update itself to be closed.
     *
     * @return A boolean value denoting if the district is closed or not.
     */
    public boolean isClosed() {
        boolean allLotsClosed = true;
        int i = 0;

        while(allLotsClosed && i <= lotID) {
            allLotsClosed = listOfLots.get(i).isClosed();
            i++;
        }

        return allLotsClosed;
    }

    /**
     * This method allows outside access to the minClosed variable.
     *
     * @return The time, in minutes, the district has been closed.
     */
    public int getClosedMinutes() {
        return minClosed;
    }

    /**
     * This method adds a ParkingLot to the district.
     * It takes in a ParkingLot and adds it to the next spot in
     * the listOfLots.
     *
     * @param newLot A new lot to be added to the district.
     * @return An int corresponding to the ID of the added lot.
     */
    public int add(ParkingLot newLot) {
        lotID = lotID + 1;

        listOfLots.add(newLot);

        return lotID;
    }

    /**
     * This method adds a FreeParkingLot to the district.
     * It takes in a FreeParkingLot and adds it to the next
     * spot in the listOfLots.
     *
     * @param newFreeLot A new free lot to be added to the district.
     * @return An int corresponding to the ID of the added lot.
     */
    public int add(FreeParkingLot newFreeLot) {
        lotID = lotID + 1;

        listOfLots.add(newFreeLot);

        return lotID;
    }

    /**
     * This method allows outside access to a lot in the district.
     *
     * @param index The ID of the lot we want returned.
     * @return The lot at the provided ID.
     */
    public ParkingLot getLot(int index) {
        return listOfLots.get(index);
    }

    /**
     * This method allows outside access to the vehiclesInDistrict variable.
     *
     * @return The number of vehicles parked in the district.
     */
    public int getVehiclesParkedInDistrict() {
        return vehiclesInDistrict;
    }

    /**
     * This method calculates the total profit of the district.
     *
     * @return The total profit from the district.
     */
    public double getTotalMoneyCollected() {
        double totalProfit = 0.0;
        int i = 0;

        while(i <= lotID) {
            totalProfit = totalProfit + listOfLots.get(i).getProfit();
            i++;
        }
        return totalProfit;
    }

    /**
     * This method provides the status of the district to outside objects.
     * The method simply concatenates the statuses of each lot
     * in the district together to form one message.
     *
     * @return A String containing the status of the district.
     */
    public String toString() {
        String districtStatus = "District status:\n";
        int i = 0;

        while(i <= lotID) {
            districtStatus = districtStatus + listOfLots.get(i).toString() + "\n";
            i++;
        }

        return districtStatus;
    }
}
