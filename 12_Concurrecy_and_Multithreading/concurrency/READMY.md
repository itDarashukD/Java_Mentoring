1. HashMap, synchronizedMap --> performing concurrent operations is not allowed, if we try to update a HashMap while iterating over it, we will receive a ConcurrentModificationException.
2. This is not the case with ConcurrentHashMap - without ConcurrentModificationException. 
3. ConcurrentHashMap performs better than Collections.synchronizedMap()


The Difference between MySimpleMap and MyThreadSafeMap



                           randomReadAndWrite                    randomRead                         randomWrite
                           java 11       java 8            java 11       java 8                java 11       java 8
MySimpleMap             2541191,975   2485393.427         2377592,192  2620323.636            2418382,641  2593673.397



MyThreadSafeMap         2425611,891   2438032.073         2364465,221  2498370.247            2375990,276  2665931.716



ConcurrentHashMap       2383340,451    2433623.708        2383715,435  2236525.438            2358718,521  2331063.589


Assuming:

1. In most cases java 11 is more effective than java 8 , but with little delta
2. ConcurrentHashMap from Java library, is more effective than my own implementations : SimpleMap and MyThreadSafeMap
3. My own implementations : SimpleMap and MyThreadSafeMap work appropriately with same performance 