use std::fmt;
use std::collections::HashMap;

/*
The Node struct is the heart of the Trie.
It contains a HashMap of chars and Nodes, being the children of the Node and their values.
It also holds a boolean value to represent if the Node is the last letter in a word.
This is because there are slightly less ways to destroy the entire Trie
if we only delete things that are at the end of a word.

Author: Gage Jager
*/
#[derive(Debug, Default)]
struct Node{
    children: HashMap<char, Node>,
    end_of_word: bool
}

/*
I only needed to implement delete into Node for the Trie to function.

Author: Gage Jager
*/
impl Node {
    /*
    The implementation of delete is a very silly recursive function
    that splits the word we're looking for into the first character and
    what's left, then either recurses into the next node and tries to
    delete the rest of the word there, or if there is no rest of the word,
    removes a pair from the Node's HashMap corresponding to the word's
    last character.  Additionally it sets the parent node of the one we just
    removed to be and "end_of_word".  This is so we could then delete it
    from the trie if we wished.  This can allow you to through branches of the
    trie into the void though, since if you have both "help" and "held" in the
    trie, then delete "help" and then "hel",  the "d" in "held" would be lost
    when you delete the "l" in "hel", leaving you with only "he" when "held"
    should still be included.

    Author: Gage Jager
    */
    pub fn delete(&mut self, word: &str) -> Option<i32> {
        // Get the first char of the word.
        let first_char = word.chars().next()?;
        
        // Get the rest of the word.
        let rest = &word[first_char.len_utf8()..];
        
        // Delete if on last char, or recurse.
        if rest.is_empty() {
            // Last character, need to remove.
            self.children.remove(&first_char)?;
            // Set parent to be end_of_word.
            self.end_of_word = true;
            return None;
        } else {
            // Otherwise, recurse.
            let node = self.children.get_mut(&first_char)?;
            node.delete(rest)
        }
    }
}

/*
The Trie struct is very simple.
It simply contains a Node designated as the root.
The root has no character associated with it,
it can only add children into its HashMap.

Author: Gage Jager
*/
#[derive(Debug, Default)]
pub struct Trie {
    root: Node
}

/*
We ended up needing to implement 4 functions
into the Trie struct for it to work.
First, we have a new function, which makes a new
Trie object, and sets its root Node to the Default
Node.
Next, we have add_word, which iterates over every char
in a word and adds the letters into the Trie structure
if they do not exist.
Next, we have find_word, which again iterates over
every char in a word, but instead looks for each letter
in the Trie before continuing.  It returns a boolean value
that tells us if the full word was detected in the Trie, and
its last character is end_of_word or not.
Lastly, we have remove_last_letter.  This is the delete function,
though we only delete one letter at a time, to disturb the Trie
structure less.

Author: Gage Jager
*/
impl Trie {
    /*
    Makes and returns a new Trie object with an empty root.

    Author: Gage Jager
    */
    pub fn new() -> Self {
        Trie {
            root: Node::default()
        }
    }

    /*
    This function adds the letters of a word into the Trie, if they are not
    already present, then sets the end_of_word of the last letter's Node to true.

    Author: Gage Jager
    */
    pub fn add_word(&mut self, word: &str) {
        let mut current_node = &mut self.root;

        // Loop over every letter in the word.
        for c in word.chars() {
            // Add each letter to the children HashMap, if it doesn't already exist
            // then set that newly created node to the current node to continue.
            current_node = current_node.children.entry(c).or_default();
        }

        // Set the last Node in the word to be the end of the word.
        current_node.end_of_word = true;
    }

    /*
    Find word loops over every character in the word, then
    tries to match the current Node's children to the characters.
    If we ever have a character that is not a child, we return false.
    If we get through every character and they do exist, we return the
    current Node's end_of_word variable, since we only want to
    count the word as found if it exists wholly in the Trie.
    This prevents false positives for words like "am" when "amoeba"
    is in the Trie, unless of course "am" itself was also added to the Trie.

    Author: Gage Jager
    */
    pub fn find_word(&self, word: &str) -> bool {
        let mut current_node = &self.root;

        for c in word.chars() {
            match current_node.children.get(&c) {
                Some(node) => current_node = node,
                None => return false
            }
        }

        return current_node.end_of_word
    }

    /*
    This is the "delete" function.  If it finds the word in the trie,
    and the last letter is the end_of_word, then we will pass the word
    onto the Nodes to delete the last letter.  For why we only delete
    the last letter, please see Node's "delete".

    Author: Gage Jager
    */
    pub fn remove_last_letter(&mut self, word: &str) -> bool {    
        if self.find_word(&word) {
            self.root.delete(&word);
            return true;
        }
        else {
            return false;
        }
    }
}

/*
Below are the implementations of the {} (Display) function
for both the Trie and Node structs.  They allow me
to print the Trie to the screen in a decently readable fashion.

Author: Gage Jager
*/
impl fmt::Display for Trie {
    fn fmt(&self, f: &mut fmt::Formatter) -> fmt::Result {
        write!(f, "{}", self.root)
    }
}

#[allow(unused_must_use)]
impl fmt::Display for Node {
    fn fmt(&self, f: &mut fmt::Formatter) -> fmt::Result {
        for (key, value) in &self.children {
            write!(f, "{}{}", key, value);
        }
        write!(f, " ")
    }
}