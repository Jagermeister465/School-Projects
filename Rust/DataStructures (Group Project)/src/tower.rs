/*

Author: Ryan Sime

The Tower struct contains the pegs which are Vectors of Vectors. 
In its current state, three peg vectors are initialized inside the tower vector.

*/
pub struct Tower {
    pegs: Vec<Vec<u32>>,
}




/*

Author: Ryan Sime

The Hanoi trait is an abstract class for defining the methods that will be implemented in tower

*/
pub trait Hanoi {
        
    // Constructor to create a Tower with a specified number of disks
    fn new(num_disks: u32) -> Self;

    // Recursive function to solve the Tower of Hanoi problem
    fn solve(&mut self, n: u32, source: usize, target: usize, auxiliary: usize, move_number: &mut u32);

    // Prints the current state of the Tower
    fn print_state(&self, peg_labels: &[&str], move_number: u32);

    // Prints details about the moves
    fn print_move(&self, move_number: u32, disk: u32, source: usize, target: usize, peg_labels: &[&str]);
}






/*

Author: Ryan Sime

This is the implementation for the methods defined in the trait. 

There is four methods in this implementation: new, solve, print_state, and print_move

The 'new' method is a constructor for the tower that initializes the vectors for pegs.

The 'solve' method is the typical recursive solution for the Tower of Hanoi problem.

The 'print_state' method handles outputting the current state of the pegs and the disks on those pegs.

The 'print_move' method handles all of the output by printing the move number and the move information as 
well as calling the print_state method.

*/
impl Hanoi for Tower {




    /*

    Author: Ryan Sime

    The 'new' method is a constructor for the tower that initializes a vector with a size of 3, which 
    will be the 3 vectors that represent the 3 pegs. This also fills the source peg with as many disks as
    are specified by the user.

    */
    fn new(num_disks: u32) -> Self {
        let mut pegs = Vec::with_capacity(3);
        for _ in 0..3 {
            pegs.push(Vec::new());
        }

        // Fill the source peg with disks
        for disk in (1..=num_disks).rev() {
            pegs[0].push(disk);
        }

        Tower { pegs }
    }



    /*

    Author: Ryan Sime

    The 'solve' method is a recursive function to solve the Tower of Hanoi problem. 

    The first recursive call solves the problem for n-1 disks and moves those disks from the source to the auxiliary.
    Note that the auxiliary is the target parameter and the target is the auxiliary paramter

    After the first recursive call, the function is at the largest disk at the bottom, and moves the disk to the target peg.

    With each of these moves, the print_move function is used to display information about the move 
    as well as how many moves have taken place so far. Inside that function, it also prints the state of the pegs
    which shows the 3 pegs with the disks stacked above them in a cleanly formatted output.

    With the second recursive call, it again solves the problem for n-1 disks using the auxiliary peg as the source and 
    the source peg becoming the target.

    The recursive calls continue moving disks from source to auxiliary to target until all disks reach the original target.

    */
    fn solve(&mut self, n: u32, source: usize, target: usize, auxiliary: usize, move_number: &mut u32) {

        // Base case check
        if n > 0 {

            // First recursive call that sets source as source, auxiliary as the target, and the target as the auxiliary
            self.solve(n - 1, source, auxiliary, target, move_number);

            // Move disk from source to target
            let disk = self.pegs[source].pop().unwrap();
            self.pegs[target].push(disk);

            // Print state after move with move details
            self.print_move(*move_number, disk, source, target, &["A", "B", "C"]);
            *move_number += 1;
            
            // Second recursive call that sets auxiliary as source, target as target, and the source as target
            self.solve(n - 1, auxiliary, target, source, move_number);
        }
    }





    /*

    Author: Ryan Sime

    The 'print_state' method is a function that handles the outputs for the current state of the tower after the move. 
    
    It organizes vertical columns that represent the pegs in the problem. 
    
    It also checks to see if there are disks on the peg and places them in the column in their respective places.

    It also prints out a separator as well as the labels for the pegs below the separator.

    */    
    fn print_state(&self, peg_labels: &[&str], _move_number: u32) {
        let peg_a_len = self.pegs[0].len();
        let peg_b_len = self.pegs[1].len();
        let peg_c_len = self.pegs[2].len();
        let mut largest_len = peg_a_len;
        if largest_len < peg_b_len {
            largest_len = peg_b_len;
        }
        if largest_len < peg_c_len {
            largest_len = peg_c_len;
        }

        for level in (0..largest_len).rev() {
            for (_i, peg) in self.pegs.iter().enumerate() {
                if let Some(disk) = peg.get(level) {
                    print!("|{: ^4}|", disk);
                } else {
                    print!("|{: ^4}|", "");
                }
            }
            println!();
        }

        for _ in 0..(self.pegs.len() * 6) {
            print!("-");
        }
        println!();

        for (i, _) in self.pegs.iter().enumerate() {
            print!(" Peg {}", peg_labels[i]);
        }

        println!("\n\n");
    }




    /*

    Author: Ryan Sime

    The 'print_move' method is a function that handles the output statements. It prints the current move number as well as which disk is moving 
    from what peg it is currently on to the peg it is moved to. It also calls the print_state method to show the current state of the pegs after that move

    */    
    fn print_move(&self, move_number: u32, disk: u32, source: usize, target: usize, peg_labels: &[&str]) {
        println!("Move {}: \nMove disk {} from Peg {} to Peg {}", move_number+1, disk, peg_labels[source], peg_labels[target]);
        self.print_state(peg_labels, move_number);
    }

}


// fn main() {
//     let num_disks = 5;
//     let mut tower = Tower::new(num_disks);
//     let mut move_number = 0;

//     // Print initial state
//     println!();
//     println!("Initial state: ");
//     tower.print_state(&["A", "B", "C"], move_number);

//     // Solve Tower of Hanoi
//     tower.solve(num_disks, 0, 2, 1, &mut move_number);
// }
