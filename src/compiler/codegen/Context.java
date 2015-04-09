package compiler.codegen;

/**
 * Created by Zach on 4/9/2015.
 */
public class Context
{
    private Token t1;
    private Token t2;
    private Char type;
    private int count;

    public Context(Token t1, Token t2, Char type)
    {
        this.t1 = t1;
        this.t2 = t2;
        this.type = type;
    }

    public Context(Token t1, Token t2, Char type, int count)
    {
        this.t1 = t1;
        this.t2 = t2;
        this.type = type;
        this.count = count;
    }

    public Token getTokOne()
    {
        return t1;
    }

    public Token getTokTwo()
    {
        return t2;
    }

    public Char getType()
    {
        return type;
    }

    public int getCount()
    {
        return count;
    }

    public void setTokOne(Token t1)
    {
        this.t1 = t1;
    }

    public void setTokTwo(Token t2)
    {
        this.t2 = t2;
    }

    public void setType(Char type)
    {
        this.type = type;
    }

    public void setCount(int count)
    {
        this.count = count;
    }
}