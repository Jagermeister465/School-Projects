/******************************************************************************
 *   Author: Michael Green
 *   Class: CSC461
 *
 *   This file implements the 'SpeedTest' trait, which can be used to start a
 *   timed test on a datastructure for n operatoins
******************************************************************************/
use std::time::Instant;
use rand::Rng;

pub trait SpeedTest {
    fn timed_test(&mut self, iterations: u32);
}

/*
    Author: Michael Green

    Performs a timed test on a datastructure for a number of iterations

    @param d - Data structure w/ SpeedTest trait to test
    @param iterations - Number of iterations for test
*/
pub fn timed_test<T: SpeedTest>(d : &mut T, iterations: u32) {

    let start = Instant::now();
    d.timed_test(iterations);
    let duration = start.elapsed().as_secs();
    println!("Test Size: {}, Duration: {}", iterations, duration);
}


/*
    Author: Michael Green

    Constructs a vector with n random integers with a specified vlaue range

    @param n - Number of rand ints to construct
    @param min - Min value of rand ints
    @param max - Max value of rand ints
    @return v - Vector of random integers
*/
pub fn rand_ints(mut n: u32, min: i32, max: i32) -> Vec<i32> {
    let mut rng = rand::thread_rng();
    let mut v = Vec::new();
    while n > 0 {
        v.push(rng.gen_range(min, max));
        n -= 1;
    }
    return v;
}
