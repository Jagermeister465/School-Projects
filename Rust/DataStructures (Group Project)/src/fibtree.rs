/******************************************************************************
 *   Author: Michael Green
 *   Class: CSC461
 *
 *   This file implements a Fibonacci tree data structure in Rust. Supported
 *   operations include range queries (range sum or frequencies - depending on
 *   what the underlying data structure represents), and point updates both within
 *   O(lg n) time. Dynamic rank search (if the array is a frequency table) is
 *   also supported within O(lg^2 n) time.
******************************************************************************/
use crate::test::{SpeedTest, rand_ints};

/*
    Author: Michael Green

    The Fibonacci Tree struct contains a vector of integer values
*/
#[derive(Debug)]
pub struct FibTree{
    vals: Vec<i32>,
}

/*
    Author: Michael Green

    The Fibonacci Tree implementation contains the following methods:
    new - Rust's convention for a constructor
    cumulative_sum - Net sum over a range from 1..i
    range_sum - Net sum over a range a..b
    increment - Increments the value of a tree entry by a specified amount
    search - Finds the dynamic rank of a value assuming the tree is a frequency table
    LSOne - Extracts least significant '1' bit in an integer
*/
impl FibTree {
    /*
        Author: Michael Green

        Constructs a new FibTree

        @param size - Size of FibTree (1 indexed based)
        @return Self - Initialized FibTree
    */
    pub fn new(size : usize) -> Self {
        Self {
            vals: vec![0; size+1],
        }
    }

    /*
        Author: Michael Green

        Calculates and returns cumulative sum of the tree from 1..index

        @param index - Stop index of sum
        @return sum - Net sum over range
    */
    pub fn cumulative_sum(&self, mut index: usize) -> i64 {
        let mut sum : i64 = 0;
        while index > 0 && index < self.vals.len() {
            sum += self.vals[index] as i64;
            index -= FibTree::LSOne(index);
        }
        return sum;
    }

    /*
        Author: Michael Green

        Calculates and returns cumulative sum of the tree over a range

        @param start - Start index of sum
        @param end - Stop index of sum
        @return sum - Net sum over range
    */
    pub fn range_sum(&self, start: usize, end: usize) -> i64 {
        self.cumulative_sum(end) - self.cumulative_sum(start)
    }

    /*
        Author: Michael Green

        Increments an entry in the FibTree by the specified amount

        @param index - Index of item to increment
        @param offset - Increment amount (signed)
    */
    pub fn increment(&mut self, mut index: usize, offset: i32) {
        while index > 0 && index < self.vals.len() {
            self.vals[index] += offset;
            index += FibTree::LSOne(index);
        }
    }

    /*
        Author: Michael Green

        Finds the lowest entry in the FibTree with the given rank

        @param rank - Requested rank of item
        @return high - Index (value) of item w/ requested rank
    */
    pub fn search(&self, rank: usize) -> usize {
        let mut low: usize = 1;
        let mut high: usize = self.vals.len() - 1;

        if rank < low || rank > high {
            return 0;
        }
        while high - low > 1 {
            let sum = self.cumulative_sum((low + high) / 2);
            if sum >= 0 && (sum as usize) < rank {
                low = (low + high) / 2;
            } else {
                high = (low + high) / 2;
            }
        }
        return high;
    }

    /*
        Author: Michael Green

        Extracts the least significant '1' bit of an int

        @param n - Unsigned int to extract bit from
        @return i - Unsigned value of extracted bit
    */
    fn LSOne(n: usize) -> usize {
        let i = n as i32;
        (i & (-i)) as usize
    }
}

/*
    Author: Michael Green

    Implements SpeedTest trait for FibTree - see test.rs
*/
impl SpeedTest for FibTree {
    fn timed_test(&mut self, iterations: u32) {
        let indicies = rand_ints(iterations, 1, (self.vals.len() - 1) as i32);
        let vals = rand_ints(iterations, -50000000, 50000000);
        let mut i: usize = 0;

        // Performs n insertions/increments, and n range queries
        while i < iterations as usize {
            self.increment(indicies[i] as usize, vals[i]);
            self.cumulative_sum(indicies[i] as usize);
            i += 1;
        }
    }
}
