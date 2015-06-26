package compiler.ast;
import java.util.ArrayList;
import java.util.List;

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
		public T visit(Var node);
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
	
	public static class Decl implements Node {
		Node variable, value;
		public Decl(Var variable, Node value) {
			this.variable = variable;
			this.value = value;
		}
		@Override
		public <T> T accept(Visitor<T> visitor) {
			return visitor.visit(this);
		}
	}
	
	public static class Op implements Node {
		Node[] op = new Node[3];
		public Op(Var left, Node operand, Var right) {
			op[0] = left;
			op[1] = operand;
			op[2] = right;
		}
		
		@Override
		public <T> T accept(Visitor<T> visitor) {
			return visitor.visit(this);
		}
	}
	
	public static class Var implements Node {
		String id, value;
		public Var(String id, String value) {
			this.id = id;
			this.value = value;
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
			System.out.println("Decl: " + node.variable + "= " + node.value);
			node.value.accept(this);
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
			System.out.println("Op: " + node.op[0] + ", " + node.op[1] + ", " + node.op[2]);
			for(Node n: node.op) {
				n.accept(this);
			}
			return null;
		}

		@Override
		public Void visit(Var node) {
			System.out.println("Var: " + node.id + " = " + node.value);
			return null;
		}
		
	}
	
}
