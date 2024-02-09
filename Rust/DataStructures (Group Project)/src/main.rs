#![allow(non_snake_case)]
use std::io::stdin;
pub mod trie;
use trie::Trie;
pub mod tower;
use tower::Hanoi;
pub mod heap;
use heap::MaxHeap;
pub mod fibtree;
use fibtree::FibTree;
pub mod test;
pub mod linkedlist;
use linkedlist::LinkedListStack;

/*
The main function contains a simple menu loop that branches off into other function calls,
based on user input.

Author: Gage Jager
*/
fn main() {
    // The main menu loop.  I decided to use Rust's "loop" loop because why not.
    loop {
        // First, make a new string and call it line.
        let mut line = String::new();

        // Print the menu options.
        println!("Choose an Option to Use: 
1) Prefix Tree (Trie)
2) Tower of Hanoi
3) Fibonacci Tree
4) Binary Heap
5) Linked List Stack
6) Speedtest
7) Quit");  // It was either forgo indents or have everything on one line with \n separators.  Sorry.

        // Reap a line from the command window.
        stdin().read_line(&mut line).unwrap();
        // Trim the line to remove the user's "Enter" newline.
        let trimmed_line = line.trim();
        
        // Determine what was chosen, and go from there.
        match trimmed_line {
            "1" => trie_menu(),
            "2" => tower_menu(),
            "3" => fibonacci_tree_menu(),
            "4" => binary_menu(),
            "5" => list_stack_menu(),
            "6" => speedtest_menu(),
            "7" => break,
            _   => println!("The option you chose, {}, is not supported.", trimmed_line)
        }
    }
}

/*
This function contains a menu loop to allow a user to
access the main functions of the trie struct.
These include adding words to the trie, seaching the trie,
deleting from the trie, and outputting the contents of the trie.

Author: Gage Jager
*/
fn trie_menu() {
    // First, make an empty trie.
    let mut trie = Trie::new();

    // Enter a secondary menu loop specific to the trie.
    loop {
        // Make a string to read user input into.
        let mut line = String::new();

        // Prompt the user for input.
        println!("What would you like to do with the trie?
1) Add a word to the trie
2) Search the trie for a word
3) Delete the last letter from a word in the trie
4) Output the trie's contents
5) Return to Main Menu");  // Once again, I'm sorry about the lack of indents.

        // Get the user's input and trim off the newline.
        stdin().read_line(&mut line).unwrap();
        let trimmed_line = line.trim();

        // Match the user's input to the menu options.
        match trimmed_line {
            "1" => {
                    println!("What word to you want to add?");
                    let mut line = String::new();
                    stdin().read_line(&mut line).unwrap();
                    let word = line.trim();
                    trie.add_word(word);
                    println!("Added {}.",word);
                   },
            "2" => {
                    println!("What word to you want to search for?");
                    let mut line = String::new();
                    stdin().read_line(&mut line).unwrap();
                    let word = line.trim();
                    let found = trie.find_word(word);
                    if found == true {
                        println!("The word {} was found.", word);
                    }
                    else {
                        println!("The word {} was not found.", word);
                    }
                   },
            "3" => {
                    println!("What word will you delete the last letter of?");
                    let mut line = String::new();
                    stdin().read_line(&mut line).unwrap();
                    let word = line.trim();
                    trie.remove_last_letter(word);
                    println!("Removed the last letter of {}.", word);
                   },
            "4" => {
                    println!("{}", trie);
                   },
            "5" => break,
            _   => println!("The option you chose, {}, is not supported.", trimmed_line)
        }
    }
}

/*
This function asks the user for how many disks they want to
have on the tower, and then solves it, outputting the tower
status with each move.

Author: Gage Jager
*/
fn tower_menu() {
    // Ask the user how many disks are on the tower.
    let mut line = String::new();
    println!("How many disks are on the tower?");
    stdin().read_line(&mut line).unwrap();

    // Trim the line to get the number of discs as a string.
    let trimmed_line = line.trim();

    // Convert the string to an int (u32) so we can use it.
    let num_disks = trimmed_line.parse::<u32>().unwrap();

    // Make a new tower with the specified # of disks.
    let mut tower: tower::Tower = Hanoi::new(num_disks);
    // The moves start at 0.
    let mut move_number = 0;

    // Print initial state
    println!();
    println!("Initial state: ");
    tower.print_state(&["A", "B", "C"], move_number);

    // Solve Tower of Hanoi
    tower.solve(num_disks, 0, 2, 1, &mut move_number);
}

/*
This function contains a menu loop to allow a user to
access the main functions of the FibTree struct.
These include incrementing values in the tree and
performing summation queries on a range of the tree,
both from beginning to a specified end, and from a
specified beginning to a specified end.

Author: Gage Jager
*/
fn fibonacci_tree_menu() {
    // First, make an empty fibonacci tree of a specified size.
    println!("What size would you like the fibonacci tree to be?");
    let mut line = String::new();
    stdin().read_line(&mut line).unwrap();
    let trimmed_line = line.trim();
    let mut fibtree = FibTree::new(trimmed_line.parse::<usize>().unwrap());

    // Enter a secondary menu loop specific to the fibonacci tree.
    loop {
        // Make a string to read user input into.
        let mut line = String::new();

        // Prompt the user for input.
        println!("What would you like to do with the fibonacci tree?
1) Increment an item
2) Query the sum from beginning to specified end
3) Query the sum from specified beginning to specified end
4) Return to Main Menu");  // Once again, I'm sorry about the lack of indents.

        // Get the user's input and trim off the newline.
        stdin().read_line(&mut line).unwrap();
        let trimmed_line = line.trim();

        // Match the user's input to the menu options.
        match trimmed_line {
            "1" => {
                    // Ask the user for a index to increment.
                    println!("What index will you increment?");
                    let mut line = String::new();
                    stdin().read_line(&mut line).unwrap();
                    let index = line.trim();
                    
                    // Ask the user how much to increment it.
                    let mut line = String::new();
                    println!("How much will you increment it by?");
                    stdin().read_line(&mut line).unwrap();
                    let amount = line.trim();
                    
                    // Now, increment the specified value.
                    fibtree.increment(index.parse::<usize>().unwrap(), amount.parse::<i32>().unwrap());
                    println!("Incremented {} by {}.", index, amount);
                   },
            "2" => {
                    // Ask the user for the end index.
                    println!("What should the ending index be?");
                    let mut line = String::new();
                    stdin().read_line(&mut line).unwrap();
                    let end_index = line.trim();
                    
                    // Perform the summation.
                    let sum = fibtree.cumulative_sum(end_index.parse::<usize>().unwrap());
                    
                    // Output the sum.
                    println!("The sum of the range from 1 to {} is {}.", end_index, sum);
                   },
            "3" => {
                    // Ask the user for the start index.
                    println!("What should the starting index be?");
                    let mut line = String::new();
                    stdin().read_line(&mut line).unwrap();
                    let start_index = line.trim();

                    // Ask the user for the end index.
                    println!("What should the ending index be?");
                    let mut line = String::new();
                    stdin().read_line(&mut line).unwrap();
                    let end_index = line.trim();
                    
                    // Perform the summation.
                    let sum = fibtree.range_sum(start_index.parse::<usize>().unwrap(), end_index.parse::<usize>().unwrap());
                    
                    // Output the sum.
                    println!("The sum of the range from {} to {} is {}.", start_index, end_index, sum);
                   },
            "4" => break,
            _   => println!("The option you chose, {}, is not supported.", trimmed_line)
        }
    }
}

/*
This function contains a menu loop to allow a user to
access the main functions of the MaxHeap struct.
These include pushing values onto the heap and popping the
max value from the heap.

Author: Gage Jager
*/
fn binary_menu() {
    // First, make an empty binary heap.
    // In this case, it is a heap of i32 values.
    let mut bheap = MaxHeap::new();

    // Enter a secondary menu loop specific to the binary heap.
    loop {
        // Make a string to read user input into.
        let mut line = String::new();

        // Prompt the user for input.
        println!("What would you like to do with the binary heap?
1) Push a value onto the heap
2) Pop a value from the heap
3) Return to Main Menu");  // Once again, I'm sorry about the lack of indents.

        // Get the user's input and trim off the newline.
        stdin().read_line(&mut line).unwrap();
        let trimmed_line = line.trim();

        // Match the user's input to the menu options.
        match trimmed_line {
            "1" => {
                    // Ask the user for a value to add, then push it on.
                    println!("What value to you want to add?");
                    let mut line = String::new();
                    stdin().read_line(&mut line).unwrap();
                    let val = line.trim();
                    bheap.push(val.parse::<i32>().unwrap());
                    println!("Added {}.",val);
                   },
            "2" => {
                    // Pop a value from the heap and output it.
                    let popped_value = bheap.pop();
                    match popped_value {
                        None => println!("The heap was already empty."),
                        _ => println!("Popped the value {}.", popped_value.unwrap())
                    }
                   },
            "3" => break,
            _   => println!("The option you chose, {}, is not supported.", trimmed_line)
        }
    }
}

/*
This function contains a menu loop to allow a user to
access the main functions of the LinkedListStack struct.
These include pushing values onto the list, popping the
most recent value from the list, peeking at the topmost value
of the list, checking if the list is empty, and outputting
the contents of the list, in order.

Author: Gage Jager
*/
fn list_stack_menu() {
    // First, make an empty linked list.
    // In this case, we'll use i32's.
    let mut list = LinkedListStack::<i32>::new();

    // Enter a secondary menu loop specific to the linked list stack.
    loop {
        // Make a string to read user input into.
        let mut line = String::new();

        // Prompt the user for input.
        println!("What would you like to do with the linked list stack?
1) Push a value onto the list
2) Pop a value from the top of the list
3) Peek at the value on top of the list
4) Check if the list is empty
5) Output the list's contents
6) Return to Main Menu");  // Once again, I'm sorry about the lack of indents.

        // Get the user's input and trim off the newline.
        stdin().read_line(&mut line).unwrap();
        let trimmed_line = line.trim();

        // Match the user's input to the menu options.
        match trimmed_line {
            "1" => {
                    // Ask the user for a value to add, then push it on.
                    println!("What value to you want to add?");
                    let mut line = String::new();
                    stdin().read_line(&mut line).unwrap();
                    let val = line.trim();
                    list.push(val.parse::<i32>().unwrap());
                    println!("Added {}.",val);
                   },
            "2" => {
                    // Pop a value from the list and output it.
                    let popped_value = list.pop();
                    match popped_value {
                        None => println!("The list was already empty."),
                        _ => println!("Popped the value {}.", popped_value.unwrap())
                    }
                   },
            "3" => {
                    // Peep at the top value from the list and output it.
                    let peeked_value = list.peek();
                    match peeked_value {
                        None => println!("The list was empty."),
                        _ => println!("Peeked at the value {}.", peeked_value.unwrap())
                    }
                   },
            "4" => {
                    // Check to see if the list is empty.
                    let empty = list.is_empty();
                    if empty {
                        println!("The list is currently empty.");
                    }
                    else {
                        println!("The list is not empty.");
                    }
                   },
            "5" => {
                    // Output the contents of the list.
                    list.print_all();
                   },
            "6" => break,
            _   => println!("The option you chose, {}, is not supported.", trimmed_line)
        }
    }
}

/*
This function contains a menu loop to allow a user to
access the speed test functionality of the FibTree and MaxHeap
structs.  The user tells us the struct they would like
to test, and provides a number representing how
many tests to perform.

Author: Gage Jager
*/
fn speedtest_menu() {
    // Enter a secondary menu loop for performing speed tests.
    loop{
        // Prompt the user for input.
        println!("Which structure would you like to test?
1) Fibonacci Tree
2) Binary Heap
3) Return to Main Menu");   // Sorry about the indents, for the last time.
        let mut line = String::new();
        stdin().read_line(&mut line).unwrap();
        let structure = line.trim();

        // Break early if they chose "3".
        if structure == "3" {break}

        // Ask for the number of test iterations.
        println!("How many test iterations would you like to perform?");
        let mut line = String::new();
        stdin().read_line(&mut line).unwrap();
        let iterations = line.trim();

        // Match the input to a structure, create that structure, and test it.
        match structure {
            "1" => {
                    let mut fibtree = FibTree::new(iterations.parse::<usize>().unwrap());
                    test::timed_test(&mut fibtree, iterations.parse::<u32>().unwrap());
                   },
            "2" => {
                    let mut bheap = MaxHeap::new();
                    test::timed_test(&mut bheap, iterations.parse::<u32>().unwrap());
                   },
            _   => println!("Sorry, structure {} was not a valid option.", structure)
        }
    }
}