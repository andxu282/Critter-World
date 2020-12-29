package ast;

/** A factory that produces the public static Mutation objects corresponding to each mutation */
public class MutationFactory {
    public static Mutation getRemove() {
        return new Remove();
    }

    public static Mutation getSwap() {
        return new Swap();
    }

    public static Mutation getReplace() {
    	return new Replace();
    }

    public static Mutation getTransform() {
       return new Transform();
    }

    public static Mutation getInsert() {
    	return new Insert();
    }

    public static Mutation getDuplicate() {
        return new Duplicate();
    }

}
