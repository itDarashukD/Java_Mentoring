indexes for empty table:

                        Indexes size            Creating time 
create b-tree index :   16kb                        81 msec.
create hash   index :   16kb                        71 msec.
create gin    index :   32kb                        121 msec.
create gist   index :   24kb                        106 msec.

In case of creating Indexes for empty table i can see that the index size, and the index creating time are quite low.


indexes for table with 1000_000 entries:
table size 96Mb.

                        Indexes size            Creating time 
create b-tree index :   43Mb                        1 sec 780 msec.
create hash   index :   53Mb                        2 secs 485 msec.
create gin    index :   89Mb                        15 secs 148 msec.
create gist   index :   79kb                        13 secs 554 msec.

In case of creating Indexes for prepopulated, for 1000_000 values, table i can see that the index size far more biggest, and the index creating time is incredibly more bigger against empty table.


                                       time without Indexes             time with Indexes
Find user by name                             408  msec.                   197 msec.
Find user by surname                          409 msec.                    227 msec.        
Find user by phone number                     507 msec.                    267 msec.     
Find user with marks by user surname          539  msec.                   218 msec.

The time to find values from table is two and more times faster with using indexes! So indexing table it's a great idea!



