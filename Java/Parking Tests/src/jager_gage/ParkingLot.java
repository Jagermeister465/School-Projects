/**
 * This class keeps track of the capacity and profit of a parking lot.
 * The class keeps track of the parking lot name, its max capacity,
 * the parking fee of the lot, the number of vehicles currently in the lot,
 * and the number of minutes the lot has been closed.  It also contains
 * an ArrayList of Integers to keep track of when vehicles enter the lot,
 * to accurately calculate the fee when they leave.  The array is indexed by
 * vehicle ID, which is incremented every time a vehicle enters the lot.
 * In addition, the class has a constant variable to know at what threshold
 * the lot is considered closed.
 *
 * @author Gage Jager
 */

package jager_gage;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ParkingLot {

    protected String lotName;
    protected int capacity;
    protected double fee;
    protected int ID;
    protected int vehiclesInLot;
    protected double currentCapacity;
    protected int minSinceOpen;
    protected int minClosed;
    protected boolean closed;
    protected int timeClosed;
    protected double profit;
    protected ArrayList<Integer> vehicleEntryTimes = new ArrayList<Integer>();
    static final int CLOSED_THRESHOLD = 80;
    protected DecimalFormat percent = new DecimalFormat("#.#");
    protected DecimalFormat money = new DecimalFormat("$0.00");

    /**
     * The ParkingLot constructor will all three arguments.
     * This constructor sets all the default values of the class instance,
     * and sets the lot name, capacity, and fee to the inputted values.
     *
     * @param lotName The official name of the lot.
     * @param capacity The maximum number of vehicles the lot can hold.
     * @param fee The hourly fee to park in the lot.
     */
    public ParkingLot(String lotName, int capacity, double fee) {
        this.lotName = lotName;
        this.capacity = capacity;
        this.fee = fee;
        this.ID = -1;
        this.vehiclesInLot = 0;
        this.currentCapacity = 0.0;
        this.minSinceOpen = 0;
        this.minClosed = 0;
        this.closed = false;
        this.timeClosed = 0;
        this.profit = 0.0;
    }

    /**
     * The ParkingLot constructor with only lot_name and capacity.
     * This constructor calls the constructor with three arguments,
     * using a default fee of 1 as well as the inputted name and capacity.
     *
     * @param lotName The official name of the lot.
     * @param capacity The maximum number of vehicles the lot can hold.
     */
    public ParkingLot(String lotName, int capacity) {
        this(lotName, capacity, 1);
    }

    /**
     * The ParkingLot constructor with only capacity and fee.
     * This constructor calls the constructor with three arguments,
     * using a default name of "test" as well as the inputted capacity and fee.
     *
     * @param capacity The maximum number of vehicles the lot can hold.
     * @param fee The hourly fee to park in the lot.
     */
    public ParkingLot(int capacity, double fee) {
        this("test", capacity, fee);
    }

    /**
     * The ParkingLot constructor with only capacity.
     * This constructor calls the constructor with three arguments,
     * using a default name of "test" and default fee of 1,
     * as well as the inputted capacity.
     *
     * @param capacity The maximum number of vehicles the lot can hold.
     */
    //GRADING: CONSTRUCTION
    //For reference, this is the "only capacity" constructor.
    //The rest of the constructors are above, with the full constructor at the top,
    //under the variable declarations.
    public ParkingLot(int capacity){
        this("test", capacity, 1);
    }

    /**
     * The method is called when a vehicle tries to enter the lot.
     * If the lot is not at maximum capacity, assigns the vehicle an ID
     * and logs the time it entered.  If the lot is at maximum capacity,
     * or if the time inputted is before a previous time, instead ignores the vehicle,
     * and returns -1.
     *
     * @param currentTime The time in minutes since the lot opened.
     * @return An integer value corresponding to the ID of the entering vehicle.
     */
    public int markVehicleEntry(int currentTime) {
        if(currentTime < minSinceOpen) {
            //Cannot log event before an event that has already occurred.
            return -1;
        }

        if ( this.isClosed() ) {
            if (currentCapacity == 100) {
                //Update minClosed
                minClosed = minClosed + (currentTime - timeClosed);
                timeClosed = currentTime;

                //Cannot add a vehicle to a full lot.
                return -1;
            }
            //If the lot was already closed, add time to minClosed.
            if (closed) {
                minClosed = minClosed + (currentTime - timeClosed);
                timeClosed = currentTime;
            }
        }

        ID = ID + 1;
        vehiclesInLot = vehiclesInLot + 1;
        vehicleEntryTimes.add(currentTime);
        minSinceOpen = currentTime;

        if ( this.isClosed() ) {
            //If the newly entered car pushed us over the threshold, updated closed and timeClosed
            closed = true;
            timeClosed = currentTime;
        }

        return ID;
    }

    /**
     * This method is called when a vehicle exits the lot.
     * Using the ID, we pull the time that vehicle entered the lot,
     * and find how long the car was in the lot using the currentTime.
     * If the difference is under 15 minutes, no fee is charged.
     * Otherwise, the fee is calculated and added to profit.
     * If the leaving time is before a different event, or if the
     * ID inputted is invalid, the method will return immediately.
     * Additionally, if the lot was closed, updates minClosed, and if
     * the leaving vehicle opens the lot, updates closed to false.
     *
     * @param currentTime The time in minutes since the lot opened.
     * @param ID The ID of the leaving vehicle, used to calculate fee.
     */
    public void markVehicleExit(int currentTime, int ID) {
        if(currentTime < minSinceOpen) {
            //Cannot log event before an event that has already occurred.
            return;
        }

        if( ID < 0 ) {
            //Invalid ID
            return;
        }

        if( vehicleEntryTimes.get(ID) == -1 ) {
            //Invalid vehicle, already left lot.
            return;
        }

        if( this.isClosed() ) {
            //If the lot is closed, update the closed time.
            minClosed = minClosed + (currentTime - timeClosed);
            timeClosed = currentTime;
        }

        //Calculate fee and add to profit.
        int timeInLotMins = currentTime - vehicleEntryTimes.get(ID);
        //Only charge fee if the vehicle was in the lot for more than 15 mins.
        if (timeInLotMins > 15) {
            double timeInLotHrs = timeInLotMins / 60.0;
            double feeCharged = timeInLotHrs * fee;
            profit = profit + feeCharged;
        }


        //Cleanup for leaving vehicle.
        vehicleEntryTimes.set(ID, -1);
        vehiclesInLot = vehiclesInLot - 1;
        minSinceOpen = currentTime;

        if( !this.isClosed() ) {
            //If the vehicle leaving brought us under the threshold,
            //mark the lot as open.
            closed = false;
        }
    }

    /**
     * This method allows outside objects to access the vehiclesInLot variable.
     *
     * @return The number of vehicles currently in the lot.
     */
    public int getVehiclesInLot() {
        return vehiclesInLot;
    }

    /**
     * This method calculates if the lot is closed based on the number of vehicles in the lot
     * and the capacity of the lot specified during construction.
     * It compares the ratio between the number of vehicles and the maximum vehicles to the
     * CLOSED_THRESHOLD constant.
     *
     * @return Boolean value for whether the lot is closed (true) or is not closed (false).
     */
    public boolean isClosed() {
        currentCapacity = ( (double)vehiclesInLot / capacity ) * 100;
        if (currentCapacity >= CLOSED_THRESHOLD) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * This method allows outside objects to access the minClosed variable.
     *
     * @return The time the lot has been closed, in minutes.
     */
    public int getClosedMinutes() {
        return minClosed;
    }

    /**
     * This method allows outside objects to access the profit variable.
     *
     * @return The amount of money collected by the lot, as a double.
     */
    public double getProfit() {
        return profit;
    }

    /**
     * This method formats data about the lot into a status message.
     * The string has two forms - one for a closed lot, and one for an open lot.
     * The distinction is, if the lot is closed, the capacity percent will
     * be CLOSED instead of the true percent.
     *
     * @return A String containing the status of the lot.
     */
    public String toString() {
        if ( this.isClosed() ) {
            return "Status for " +  lotName + " parking lot: "
                    + vehiclesInLot + " vehicles (CLOSED) Money Collected: "
                    + money.format(profit);
        }
        else {
            return "Status for " +  lotName + " parking lot: "
                    + vehiclesInLot + " vehicles (" + percent.format(currentCapacity)
                    + "%) Money Collected: " + money.format(profit);
        }
    }

    /**
     * This method allows outside objects to access the lot_name variable.
     *
     * @return A String containing the name of the lot.
     */
    public String getName() {
        return lotName;
    }
}
