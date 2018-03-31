args <- commandArgs(TRUE)
paste(c("I", "like", args[1], "and", args[2], "!"), collapse = " ")
print(args)