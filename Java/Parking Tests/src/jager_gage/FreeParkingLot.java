/**
 * This class keeps track of the capacity of a free parking lot.
 * The class keeps track of the parking lot name, its max capacity,
 * the number of vehicles currently in the lot, and the number of minutes
 * the lot has been closed.  It also contains an ArrayList of Integers
 * to keep track of when vehicles enter the lot.  The array is indexed by
 * vehicle ID, which is incremented every time a vehicle enters the lot.
 * In addition, the class has a constant variable to know at what threshold
 * the lot is considered closed.
 *
 * @author Gage Jager
 */

package jager_gage;

public class FreeParkingLot extends ParkingLot {

    /**
     * The FreeParkingLot constructor with both arguments.
     * This constructor takes in the lot name and capacity,
     * then calls the ParkingLot constructor with a fee of 0.
     * This way, a ParkingLot with a 0 fee is made, which is
     * essentially a FreeParkingLot in all but name.
     *
     * @param lotName The official name of the parking lot.
     * @param capacity The maximum amount of vehicles the lot can hold.
     */
    public FreeParkingLot(String lotName, int capacity) {
        super(lotName, capacity, 0);
    }

    /**
     * The FreeParkingLot constructor with only capacity.
     * This constructor calls the above constructor using
     * "test" as the default name for the lot, should a name
     * not be provided initially.
     *
     * @param capacity The maximum amount of vehicles the lot can hold.
     */
    public FreeParkingLot(int capacity) {
        this("test", capacity);
    }

    /**
     * This method extends the markVehicleExit method to allow for
     * the below method to function.
     * Since the ID of the leaving car does not matter, as long as there is
     * a car in the lot, we remove one car.  We use ID 0 as our default ID
     * to remove a car using the super method.
     *
     * @param currentTime The amount of time since the lot opened, in minutes.
     * @param ID The ID of the vehicle leaving the lot.
     */
    public void markVehicleExit(int currentTime, int ID) {
        if( vehiclesInLot > 0 ) {
            super.markVehicleExit(currentTime, 0);
            vehicleEntryTimes.set(0, 1);
        }

    }

    /**
     * This method allows you to have a vehicle leave a free parking lot
     * without providing an ID.
     * The method calls the above method using a default ID of 0.
     *
     * @param currentTime The amount of time since the lot opened, in minutes.
     */
    public void markVehicleExit(int currentTime) {
        this.markVehicleExit(currentTime, 0);
    }

    /**
     * This method returns a string containing the status of the lot.
     * The method extends the super toString, then takes a substring
     * without the " Money Collected: $0.00".  We know exactly how
     * many characters to leave out because a freeParkingLot's
     * profit will always be $0.00.
     * @return A String containing the status of the lot.
     */
    public String toString() {
        String fullStatus = super.toString();
        return fullStatus.substring(0, (fullStatus.length() - 23) );
    }
}
