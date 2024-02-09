/******************************************************************************
 *   Author: Michael Green
 *   Class: CSC461
 *
 *   This file implements a max Binary Heap data structure in Rust. Supported
 *   operations include push and pop - both in O(lg n).
******************************************************************************/
use crate::test::{SpeedTest, rand_ints};

/*
    Author: Michael Green

    The MaxHeap datastructure makes special use of a vector,
    and tracks its current size.
*/
#[derive(Debug)]
pub struct MaxHeap{
    vals: Vec<i32>,
    size: usize,
}

/*
    Author: Michael Green

    The max Binary Heap Tree implementation contains the following methods:
    new - Rust's convention for a constructor
    push - Inserts an item into the heap
    pop - Returns and removes the top of the heap

    Note: The generic type T requires traits Copy and PartialOrd, so that
    entries can be compared and manually copied in the heap
*/
impl MaxHeap {
    /*
        Author: Michael Green

        Constructs a new MaxHeap

        @return Self - Initialized MaxHeap (empty)
    */
    pub fn new() -> Self {
        Self {
            vals: Vec::<i32>::new(),
            size: 0,
        }
    }

    /*
        Author: Michael Green

        Inserts an item into the heap

        @param val - item to insert
    */
    pub fn push(&mut self, val: i32) {
        // To minimize reallocations, the heap keeps track of its own size
        self.size += 1;
        if self.vals.len() > self.size {
            self.vals[self.size] = val;
        } else {
            while self.vals.len() <= self.size {
                self.vals.push(val);
            }
        }

        // While new entry is greater than parent, push up the heap
        let mut index: usize = self.size;
        while (index > 1) && (self.vals[index] > self.vals[index >> 1]) {
            let temp = self.vals[index >> 1];
            self.vals[index >> 1] = self.vals[index];
            self.vals[index] = temp;
            index = index >> 1;
        }
    }

    /*
        Author: Michael Green

        Removes and returns the top of the heap

        @return val - top of heap (None if heap is empty)
    */
    pub fn pop(&mut self) -> Option<i32> {
        let mut ans = None;
        if self.size > 0 {
            // Pop from the heap removes the first item, and replaces it w/ last item
            ans = Some(self.vals[1]);
            self.vals[1] = self.vals[self.size];
            self.size -= 1;

            // Traverse down the heap while entry is less than either of its children
            let mut index: usize = 1;
            let mut lchild: usize = index << 1;
            let mut rchild: usize = lchild + 1;
            while (lchild <= self.size && self.vals[lchild] > self.vals[index]) || (rchild <= self.size && self.vals[rchild] > self.vals[index]) {
                if rchild <= self.size && self.vals[rchild] > self.vals[lchild] {
                    let temp = self.vals[index];
                    self.vals[index] = self.vals[rchild];
                    self.vals[rchild] = temp;
                    index = rchild;
                    lchild = index << 1;
                    rchild = lchild + 1;
                } else {
                    let temp = self.vals[index];
                    self.vals[index] = self.vals[lchild];
                    self.vals[lchild] = temp;
                    index = lchild;
                    lchild = index << 1;
                    rchild = lchild + 1;
                }
            }
        }
        return ans;
    }
}

/*
    Author: Michael Green

    Implements SpeedTest trait for FibTree - see test.rs
*/
impl SpeedTest for MaxHeap {
    fn timed_test(&mut self, iterations: u32) {
        let vals = rand_ints(iterations, std::i32::MIN, std::i32::MAX);
        let mut i: usize = 0;

        // Performs n insertions/increments, and n range queries
        while i < iterations as usize {
            self.push(vals[i]);
            i += 1;
        }

        while self.pop().is_some() {
        }
    }
}
