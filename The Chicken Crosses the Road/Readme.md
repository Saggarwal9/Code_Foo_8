# Prompt : THE CHICKEN CROSSES THE ROAD  
Henny Penny, the chicken, wants to cross the road to go see his favorite historical documentary, Chicken Little. Although, the road often has potholes created by falling sky pieces. Chicken City doesn’t have enough corn in the budget to fix all the potholes, so Henny needs to be careful not to trip when crossing.

Create a program that will count how many paths Henny can take to cross the road with these rules:

A road is defined as a 4 x 4 Coordinate Grid
Any coordinate on the grid can contain a pothole
The grid should have at least one pothole
Henny randomly starts on a coordinate of the Zero Y-edge of the Coordinate Grid — so the left side [0]∪[0, 3]
The starting coordinate cannot have a pothole
Henny can only move right (+X), down (-Y), and up (+Y). Henny cannot move left (-X) or diagonally
Henny cannot backtrack to a coordinate that was already used
Henny cannot travel through a pothole, and must go around
A path is complete once the right side is reached [3]∪[0, 3]
Henny might not be able to reach the other side (no ticket refunds)
Solve for at least one randomly generated starting point, you do not need to solve for all starting positions
Display AT LEAST the grid, the valid paths, and the final answer.
Example where O denotes a coordinate and X denotes a pothole:  
O X O X  
O O X O  
X O O O  
O O O O  
Valid paths Henny can take if starting at (0, 3):  
(0, 3) -> (0, 2) -> (1, 2) -> (1, 1) -> (2, 1) -> (3, 1)  
(0, 3) -> (0, 2) -> (1, 2) -> (1, 1) -> (1, 0) -> (2, 0) -> (3, 0)  
(0, 3) -> (0, 2) -> (1, 2) -> (1, 1) -> (1, 0) -> (2, 0) -> (2, 1) -> (3, 1)  
(0, 3) -> (0, 2) -> (1, 2) -> (1, 1) -> (2, 1) -> (2, 0) -> (3, 0)  
INVALID path examples if starting at (0, 3):  
(0, 3) -> (1, 3) -> (2, 3) -> (3, 3)  
(0, 3) -> (1, 2) -> (2, 1) -> (3, 0)  
(0, 3) -> (0, 2) -> (0, 1) -> (0, 0) -> (1, 0) -> (2, 0) -> (3, 0)  
(0, 3) -> (0, 2) -> (1, 2) -> (1, 1) -> (2, 1) -> (1, 1) -> (2, 1) -> (3, 1)  
Answer: Total valid paths from starting point (0, 3) is 4  

Explain how you implemented the solution. Does your solution work with larger grids?
-------------------------------------------------------------------------------------------------------------------------------------

Answer: Yes, it works with larger grid.  

Approach: 

Solution is programmed in Java. There are three main classes: Location, Road, Chicken.

1) Location:
   Class used to encapsulate X,Y Coordinates within an object for a Linked-List. This Linked-List is used to track all the moves made by    the chicken from start location to the end. 
   
2) Road:
    Class used to instantiate the road by randomly assigning the potholes( Maximum Width * Maximum Height Potholes).
    Maximum Width and Height are constant variables. These variables can be re-assigned to generate a larger
    grid and the solution will still work.
    If there are no valid starting locations or goal locations, randomly generate new locations and re-assign it.
    The state of the road is stored in a 2D char array(potholes[MAX_Y_WIDTH][MAX_X_WIDTH])
    The road generated might not have a valid path to reach the goal. The starting position might also be 
    generated from which the goal might be unreachable.
    Road Format:  
    |(0,0), (1,0), (2,0), (3,0)|  
    |(0,1), (1,1), (2,1), (3,1)|  
    |(0,2), (1,2), (2,2), (3,2)|  
    |(0,3), (1,3), (2,3), (3,3)|  
    
3) Chicken:
    Main class for the chicken agent. 
    Gets a random starting location using the helper function in the Road class.
    Uses recursion to traverse the path. 
    Makes use of a 2D array marking the visited coordinates to prevent back-tracking.
    Store the location every iteration and add it to a Location linked-list. 
    When goal is reached, print the linked-list and remove the last element. 
    When a dead-end is reached, remove the dead-end coordinate from the linked-list.
    When we reach end of recursive function, mark the current node unvisited to allow chicken to access that coordinate while following
    another path. 
    
# Running the Program
Chicken is the main class.   
Requires no arguments to run.  
On an IDE, can be compiled directly by building and executing chicken.java.   
To compile on a shell, navigate to the base folder. Compile using 'javac \*.java' without quotes, followed by java chicken.   
To change the grid-size, alter the MAX_Y_WIDTH (Length of the Grid) or MAX_X_WIDTH (Width of the Grid) in the Road class.  
    
# Sample outputs: (* Marks the starting Coordinates)

1) 
   O X X O  
   X X O O  
   O O X X  
   O* X X X  
Answer: Total valid paths from starting point (0,3) is 0.  

2)
   O O X X  
   O X O O  
   X X O X  
   O* O O X
1. (0, 3)->(1, 3)->(2, 3)->(2, 2)->(2, 1)->(3, 1).  
Answer: Total valid paths from starting point (0,3) is 1.  

3)
   X O X O   
   O O O O  
   O* O O O  
   O O O O
1. (0, 2)->(1, 2)->(2, 2)->(3, 2).  
2. (0, 2)->(1, 2)->(2, 2)->(2, 1)->(3, 1).  
3. (0, 2)->(1, 2)->(2, 2)->(2, 3)->(3, 3).  
4. (0, 2)->(1, 2)->(1, 1)->(2, 1)->(3, 1).  
5. (0, 2)->(1, 2)->(1, 1)->(2, 1)->(2, 2)->(3, 2).  
6. (0, 2)->(1, 2)->(1, 1)->(2, 1)->(2, 2)->(2, 3)->(3, 3).  
7. (0, 2)->(1, 2)->(1, 3)->(2, 3)->(3, 3).  
8. (0, 2)->(1, 2)->(1, 3)->(2, 3)->(2, 2)->(3, 2).  
9. (0, 2)->(1, 2)->(1, 3)->(2, 3)->(2, 2)->(2, 1)->(3, 1).  
10. (0, 2)->(0, 1)->(1, 1)->(2, 1)->(3, 1).  
11. (0, 2)->(0, 1)->(1, 1)->(2, 1)->(2, 2)->(3, 2).  
12. (0, 2)->(0, 1)->(1, 1)->(2, 1)->(2, 2)->(2, 3)->(3, 3).  
13. (0, 2)->(0, 1)->(1, 1)->(1, 2)->(2, 2)->(3, 2).  
14. (0, 2)->(0, 1)->(1, 1)->(1, 2)->(2, 2)->(2, 1)->(3, 1).  
15. (0, 2)->(0, 1)->(1, 1)->(1, 2)->(2, 2)->(2, 3)->(3, 3).  
16. (0, 2)->(0, 1)->(1, 1)->(1, 2)->(1, 3)->(2, 3)->(3, 3).  
17. (0, 2)->(0, 1)->(1, 1)->(1, 2)->(1, 3)->(2, 3)->(2, 2)->(3, 2).  
18. (0, 2)->(0, 1)->(1, 1)->(1, 2)->(1, 3)->(2, 3)->(2, 2)->(2, 1)->(3, 1).  
19. (0, 2)->(0, 3)->(1, 3)->(2, 3)->(3, 3).  
20. (0, 2)->(0, 3)->(1, 3)->(2, 3)->(2, 2)->(3, 2).  
21. (0, 2)->(0, 3)->(1, 3)->(2, 3)->(2, 2)->(2, 1)->(3, 1).  
22. (0, 2)->(0, 3)->(1, 3)->(1, 2)->(2, 2)->(3, 2).  
23. (0, 2)->(0, 3)->(1, 3)->(1, 2)->(2, 2)->(2, 1)->(3, 1).  
24. (0, 2)->(0, 3)->(1, 3)->(1, 2)->(2, 2)->(2, 3)->(3, 3).  
25. (0, 2)->(0, 3)->(1, 3)->(1, 2)->(1, 1)->(2, 1)->(3, 1).  
26. (0, 2)->(0, 3)->(1, 3)->(1, 2)->(1, 1)->(2, 1)->(2, 2)->(3, 2).  
27. (0, 2)->(0, 3)->(1, 3)->(1, 2)->(1, 1)->(2, 1)->(2, 2)->(2, 3)->(3, 3).  
Answer: Total valid paths from starting point (0,2) is 27.  

4)
   O X O X  
   X X X X  
   O* O O O  
   O O O O
1. (0, 2)->(1, 2)->(2, 2)->(3, 2). 
2. (0, 2)->(1, 2)->(2, 2)->(2, 3)->(3, 3). 
3. (0, 2)->(1, 2)->(1, 3)->(2, 3)->(3, 3). 
4. (0, 2)->(1, 2)->(1, 3)->(2, 3)->(2, 2)->(3, 2). 
5. (0, 2)->(0, 3)->(1, 3)->(2, 3)->(3, 3). 
6. (0, 2)->(0, 3)->(1, 3)->(2, 3)->(2, 2)->(3, 2). 
7. (0, 2)->(0, 3)->(1, 3)->(1, 2)->(2, 2)->(3, 2). 
8. (0, 2)->(0, 3)->(1, 3)->(1, 2)->(2, 2)->(2, 3)->(3, 3).   
Answer: Total valid paths from starting point (0,2) is 8. 
    
5)
   O O O O  
   O* X O O  
   X O O X  
   O O O O
1. (0, 1)->(0, 0)->(1, 0)->(2, 0)->(3, 0).  
2. (0, 1)->(0, 0)->(1, 0)->(2, 0)->(2, 1)->(3, 1).  
3. (0, 1)->(0, 0)->(1, 0)->(2, 0)->(2, 1)->(2, 2)->(2, 3)->(3, 3).  
Answer: Total valid paths from starting point (0,1) is 3.  

----------------------------------------------------------------------------------------------------------------------------------
