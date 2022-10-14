###Algo in Scala

FindMaxDiff from a List with constraint:
 - Bigger number should have bigger index in the List

iterate from the second last element of the List, with the last one as the initial diff, keeps track 
of the max number, whenever find a number before it is bigger, then make it as the max, and no other 
changes, keep iteration, if the number is not bigger thant max, then calculate the new diff using max
and the number, compare the new diff with prev-diff, keep the bigger one, and keep iteration with the
remaining numbers in the List

