PROGRAM chuck

! A program from the text to showcase tuple space

IMPLICIT NONE

! ... Some interfaces and other codeeeee....

blog = linda.universe_rd(("MicroBlog", linda.TupleSpace))[1]

t1 = blog._rd(("bob", "distsys", str)) 
t2 = blog._rd(("alice", "gtcn", str))
t3 = blog._rd(("bob", "gtcn", str)) 

t = blog_rd((str, "distsys", str))

END PROGRAM chuck
