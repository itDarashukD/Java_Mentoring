  

1. time executing for Recursion search method: 

Average:

Benchmark                                                      Mode  Cnt   Score    Error  Units
RecursiveBinarySearchTest.doBenchmarkMeasurementsForRecursion  avgt    9  588,245 ± 229,022  ns/op

when key is first in List                                                 515,055 ns/op
when key is in the middle of the list                                     543,180 ns/op
when key is last in List                                                  651,940 ns/op   

Result "doBenchmarkMeasurementsForRecursion":
588,245 ±(99.9%) 229,022 ns/op [Average]
(min, avg, max) = (495,183, 588,245, 930,315), stdev = 136,288
CI (99.9%): [359,222, 817,267] (assumes normal distribution)

2. time executing for Iterative search method:

Average:

Benchmark                                                      Mode  Cnt    Score     Error  Units
IterativeBinarySearchTest.doBenchmarkMeasurementsForIterative  avgt    9  581,568 ± 114,559  ns/op

when key is first in List                                                 568,958 ns/op
when key is in the middle of the list                                     540,203 ns/op
when key is last in List                                                  569,373 ns/op  

2. time executing for Iterative search method:
Result "doBenchmarkMeasurementsForIterative":
581,568 ±(99.9%) 114,559 ns/op [Average]
(min, avg, max) = (515,392, 581,568, 701,674), stdev = 68,172
CI (99.9%): [467,009, 696,126] (assumes normal distribution)

Summary:

in the situation where :

key is first in List  :                                     recursively algorithm a little faster
key is in the middle of the list :                          iterative algorithm a little faster
key is last in List :                                       iterative algorithm faster


3. time executing for Merge sort algorithm:

Benchmark                        Mode  Cnt     Score     Error  Units
MergeSortTest.benchmarkMeasures  avgt    9  1209,989 ± 257,170  ns/op


Result "org.example.extramile.algorithms.MergeSortTest.benchmarkMeasures":
1209,989 ±(99.9%) 257,170 ns/op [Average]
(min, avg, max) = (1063,665, 1209,989, 1440,483), stdev = 153,038
CI (99.9%): [952,819, 1467,158] (assumes normal distribution)