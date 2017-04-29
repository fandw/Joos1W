// TYPE_LINKING
/**
 * TypeLinking
 * - Tests whether a simple typename is linked to the correct type in
 * the same package, in the case where other packages are also defined
 * within the program.
 * Specifically, the type name Bar in Compiler.Foo should link to Compiler.Bar.
 */
public class Main{

    public Main(){}

    public static int test(){
	return Test.Foo.test();
    }

    public static void main(String[] args){
	System.out.println(Main.test());
    }
    
}
