/*

Author: Ryan Sime

A Node struct for the linked list that contains the data and the next node

*/
struct Node<T> {
    data: T,
    next: Option<Box<Node<T>>>,
}


/*

Author: Ryan Sime

A LinkedListStack struct that contains an generic type optional valued (Some or None) head pointer 
allocated onto the heap 

*/
pub struct LinkedListStack<T> {
    head: Option<Box<Node<T>>>,
}



/*

Author: Ryan Sime

The implementation for the methods of the Linked List Stack.

The 'new' method initializes a new linked list stack.

The 'push' method pushes a value onto the top of the stack by taking the current top and setting it as the next for the node and making that new node the head.

The 'pop' method takes the value (Some or None) from the Option and maps the following operations if the take returned a Some as opposed to a None:
    - removes the head of the stack
    - makes the next node the new head
    - returns the data of the removed node

The 'is_empty' method simply checks if there is a head.

The 'peek' method applies the map on a reference to the head if the Option returns a value. The map is simply grabbing the data without removing it.

The 'print_all' method iterates down the stack and prints all of the values with some extras and formatting to make it look nice.

*/
impl<T: std::fmt::Display> LinkedListStack<T> {

    // Create a new empty stack
    pub fn new() -> Self {
        LinkedListStack { head: None }
    }

    // Push a value onto the stack
    pub fn push(&mut self, data: T) {
        let new_node = Box::new(Node {
            data,
            next: self.head.take(),
        });

        self.head = Some(new_node);
    }

    // Pop a value from the stack
    pub fn pop(&mut self) -> Option<T> {
        self.head.take().map(|node| {
            self.head = node.next;
            node.data
        })
    }

    // Check if the stack is empty
    pub fn is_empty(&self) -> bool {
        self.head.is_none()
    }

    // Peek at the top value of the stack without removing it
    pub fn peek(&self) -> Option<&T> {
        self.head.as_ref().map(|node| &node.data)
    }

    // Print all values in the stack
    pub fn print_all(&self) {
        let mut current = &self.head;
        println!("Values in stack: ");
   
        while let Some(node) = current {
            print!("{}", node.data);
            if node.next.is_some() {
                print!(" --> ");
            }
            current = &node.next;
        }
    
        println!();
        println!();
    }
}
