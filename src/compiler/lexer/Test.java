package compiler.lexer;


public class Test {

	public static void main(String[] args) {
		ZMMLexer lexer = new ZMMLexer("int x = 4;\nwhile(x<6)[x = x + 1;]");
		try {
			Token t = lexer.nextToken();
			while(t.type != ZMMLexer.EOF_TYPE) {
				System.out.println(t);
				t = lexer.nextToken();
			}
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
}
