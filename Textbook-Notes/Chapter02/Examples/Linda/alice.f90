PROGRAM alice

! A program from the text to showcase tuple space

IMPLICIT NONE

! ... Some interfaces and other codeeeee....

blog = linda.universe_rd(("MicroBlog", linda.TupleSpace))[1]

blog._out(("alice", "gtcn", "This graph theory stuff is not easy"))
blog._out(("alice", "distsys", "I like systems more than graphs"))

END PROGRAM alice
