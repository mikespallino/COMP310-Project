package compiler.ast;
import java.util.ArrayList;
import java.util.List;

import compiler.ast.AST.Node;

public class AST {

	public interface Node {
		public <T> T accept(Visitor<T> visitor);
	}
	
	public interface Visitor<T> {
		public T visit(Program node);
		public T visit(While node);
		public T visit(Decl node);
		public T visit(If node);
		public T visit(Op node);
		public T visit(Variable node);
		public T visit(Value node);
		public T visit(Expr node);
		public T visit(Operand node);
	}
	
	public static class Program implements Node {
		List<Node> children;
		public Program() {
			children = new ArrayList<Node>();
		}
		@Override
		public <T> T accept(Visitor<T> visitor) {
			return visitor.visit(this);
		}
		public void addChild(Node n) {
			children.add(n);
		}
	}
	
	public static class While implements Node {
		Node condition;
		List<Node> children;
		public While(Op condition) {
			this.condition = condition;
			children = new ArrayList<Node>();
		}
		@Override
		public <T> T accept(Visitor<T> visitor) {
			return visitor.visit(this);
		}
	}
	
	public static class If implements Node {
		Node condition;
		List<Node> children;
		Node elseCase;
		public If(Op condition, Node elseCase) {
			this.condition = condition;
			this.elseCase = elseCase;
			children = new ArrayList<Node>();
		}
		@Override
		public <T> T accept(Visitor<T> visitor) {
			return visitor.visit(this);
		}
	}
	
	public static class Expr implements Node {
		ArrayList<Node> values;
		public Expr(ArrayList<Node> values) {
			this.values = values;
		}
		public Expr(Node node) {
			this.values = new ArrayList<>();
			this.values.add(node);
		}
		public Expr() {
			this.values = new ArrayList<>();
		}
		public <T> T accept(Visitor<T> visitor) {
			return visitor.visit(this);
		}
	}
	
	public static class Decl implements Node {
		Node variable;
		public Decl(Variable variable) {
			this.variable = variable;
		}
		@Override
		public <T> T accept(Visitor<T> visitor) {
			return visitor.visit(this);
		}
	}
	
	public static class Op extends Expr implements Node {
		Node operator;
		public Op(ArrayList<Node> nodes, int opIndex) {
			super(nodes);
			operator = nodes.get(opIndex);
		}
		
		@Override
		public <T> T accept(Visitor<T> visitor) {
			return visitor.visit(this);
		}
	}
	
	public static class Variable implements Node {
		String id;
		Expr values;
		public Variable(String id, Expr values) {
			this.id = id;
			this.values = values;
		}
		public Variable(String id) {
			this.id = id;
			values = new Expr();
		}
		@Override
		public <T> T accept(Visitor<T> visitor) {
			return visitor.visit(this);
		}
		
	}
	
	public static class Value implements Node {
		String value;
		public Value(String value) {
			this.value = value;
		}
		@Override
		public <T> T accept(Visitor<T> visitor) {
			return visitor.visit(this);
		}
	}
	
	public static class Operand implements Node {
		String op;
		public Operand(String op) {
			this.op = op;
		}
		@Override
		public <T> T accept(Visitor<T> visitor) {
			return visitor.visit(this);
		}
	}
	
	public static class Visit implements Visitor<Void> {

		@Override
		public Void visit(Program node) {
			System.out.println("START");
			for (Node n: node.children) {
				n.accept(this);
			}
			return null;
		}

		@Override
		public Void visit(While node) {
			System.out.println("While: " + node.condition);
			node.condition.accept(this);
			for(Node n: node.children) {
				n.accept(this);
			}
			return null;
		}

		@Override
		public Void visit(Decl node) {
			System.out.println("\nDecl: " + node.variable);
			node.variable.accept(this);
			return null;
		}

		@Override
		public Void visit(If node) {
			System.out.println("If: " + node.condition);
			node.condition.accept(this);
			for(Node n: node.children) {
				n.accept(this);
			}
			return null;
		}

		@Override
		public Void visit(Op node) {
			System.out.println("Op: " + node.operator);
			for(Node n: node.values) {
				n.accept(this);
			}
			return null;
		}
		
		public Void visit(Variable node) {
			System.out.println("Variable: " + node.id + " = " + node.values);
			node.values.accept(this);
			return null;
		}
		
		public Void visit(Value node) {
			System.out.println("Value: " + node.value);
			return null;
		}
		
		public Void visit(Operand node) {
			System.out.println("Operand: " + node.op);
			return null;
		}

		@Override
		public Void visit(Expr node) {
			if(node.values.size() > 0)
				System.out.print("Expr: ");
				for(Node n: node.values) {
					System.out.print(n + ", ");
					n.accept(this);
				}
			
			return null;
		}
		
	}
	
}
