PROGRAM bob

! A program from the text to showcase tuple space

IMPLICIT NONE

! ... Some interfaces and other codeeeee....

blog = linda.universe_rd(("MicroBlog", linda.TupleSpace))[1]

blog._out(("bob", "distsys", "I am studying chapter 2"))
blog._out(("bob", "distsys", "The linda example's pretty simple"))
blog._out(("bob", "gtcn", "Cool book!"))

END PROGRAM bob
