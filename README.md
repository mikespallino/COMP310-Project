# COMP310-Project
This project will entail creating a compiler for the assembly instruction set of the simple 16-bit cpu Professor Joey Lawrance gave us in COMP278.

## The Grammar
	stats           : stat stat* EOF;
	stat            : busControlInstr | arithmaticInstr;
	busControlInstr : instr reg mmm | instr reg;
	arithmaticInstr : instr reg reg reg;
	mmm             : IMMD NWLN;
	reg             : REGISTER COMMA | REGISTER NWLN; 
	instr           : INSTRUCTION;

##The General Idea
We will first go about creating a backtracking parser and lexer for the grammar. Then we will build an AST and verify the syntax of input. Finally we will generate the hexadecimal output of the code in a format that can be loaded into a Logisim RAM module.
