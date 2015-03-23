# COMP310-Project
This project will entail creating a compiler for the assembly instruction set of the simple 16-bit cpu Professor Joey Lawrance gave us in COMP278.

##The General Idea
We will first go about creating a backtracking parser and lexer for the grammar for assembly. We will then do a conversion to Machine Code (hexadecimal output of the code in a format that can be loaded into a Logisim RAM module) from there. Then we will create another parser and lexer for the actual DSL. Then we will build an AST and verify the syntax of input. Finally we will generate the Assembly instructions from the DSL and pass that back into the original parser and lexer for the assembly code.

## The Grammar for Assembly
	stats           : stat* EOF;
	stat            : (busControlInstr | arithmeticInstr) NWLN;
	busControlInstr : instr reg mmm | instr reg;
	arithmeticInstr : instr reg reg reg;
	mmm             : IMMD;
	reg             : REGISTER COMMA | REGISTER;
	instr           : INSTRUCTION;

## The Grammar for ZMM
	stats           : stat* EOF;
	stat						: assign | comp | while | for | if;
	assign					: ((DATATYPE NAME EQUALS (NAME | VALUE)) | (DATATYPE NAME EQUALS (NAME | VALUE) OP (NAME | VALUE))) SEMI;
	comp						: ((NAME | VALUE) EQUALS EQUALS (NAME | VALUE) SEMI) | ((NAME | VALUE) EQUALS EQUALS (NAME | VALUE));
	while						: WHILE OPAREN stat CPAREN OBRACK (stat)* CBRACK;
	for							: FOR OPAREN stat stat stat CPAREN OBRACK (stat)* CBRACK;
	if							: IF OPAREN stat CPAREN OBRACK (stat)* CBRACK;

## 16-BIT CPU ISA (reference)
	Reverse column order
	Machine code      Assembly           C/C++/Java(Script?) equivalent
	op   Rd  Ra  Rb
	Arithmetic/Logic
	fedcba9876543210
	0000ddddaaaabbbb  ADD  Rd, Ra, Rb    Rd = Ra + Rb; // ADD Rd, Ra, R0 is equivalent to MOV, Rd, Ra
	0001ddddaaaabbbb  SUB  Rd, Ra, Rb    Rd = Ra - Rb;
	0010ddddaaaabbbb  MUL  Rd, Ra, Rb    Rd = Ra * Rb;
	0011ddddaaaabbbb  DIV  Rd, Ra, Rb    Rd = Ra / Rb;
	0100ddddaaaabbbb  AND  Rd, Ra, Rb    Rd = Ra & Rb;
	0101ddddaaaabbbb  OR   Rd, Ra, Rb    Rd = Ra | Rb;
	0110ddddaaaabbbb  XNOR Rd, Ra, Rb    Rd = !(Ra ^ Rb); // Bitwise equality
	0111ddddmmmmmmmm  MOV  Rd, mmm       Rd = mmm;

	Program Control // Note that these are not CMOV
	1000aaaammmmmmmm  JZE  Ra, mmm       if (Ra == 0) goto mmm // JZE R0, mmm is equivalent to goto mmm
	1001aaaammmmmmmm  JZN  Ra, mmm       if (Ra != 0) goto mmm
	1010aaaammmmmmmm  JZG  Ra, mmm       if (Ra > 0) goto mmm
	1011aaaammmmmmmm  JZL  Ra, mmm       if (Ra < 0) goto mmm

	Load (Bus control)
	1100aaaammmmmmmm  LOAD Ra, mmm       Ra = RAM[mmm]; //   Ra = *(mmm);
	1101aaaa00000000  LOAD Ra            Ra = RAM[Ra];  //   Ra = *(Ra);

	Store (TODO) Here be dragons (it doesn't actually work)
	1110aaaammmmmmmm  STOR Ra, mmm       RAM[mmm] = Ra
	1111aaaa00000000  STOR Ra            RAM[Ra & 0xff] = Ra

	Cycle Clock
		0     0    Fetch instruction
		0     1    Fetch instruction
		1     0    Fetch operand / ALU enable
		1     1    Program counter increment, Register write
