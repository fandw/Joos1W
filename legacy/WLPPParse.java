import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;


public class WLPPParse {

	public static ArrayList<String[]> rules = new ArrayList<String[]>();
	public static ArrayList<String[]> trans = new ArrayList<String[]>();
	public static ArrayList<String[]> steps = new  ArrayList<String[]>();
	public static Stack<String> symbols = new Stack<String>();
	public static Stack<Integer> states = new Stack<Integer>();
	public static String start = "";
	public static HashSet<String> nonterms = new HashSet<String>();
	public static ArrayList<String> newSteps = new  ArrayList<String>();
	public static int rIndex = 0;
	
	public class Tree {
        String rule;
        LinkedList<Tree> children = new LinkedList<Tree>();
    }
	
	public static void main(String[] args) {
		new WLPPParse().go();
	}
	
	public void go(){
		start = "procedure";
		Scanner in = new Scanner(System.in);
		states.push(99);
		fillTrans();
		fillRules();
		fillNonterms();
		if(testSeq(in)){
			
			Tree parsetree = lrdo();
			System.out.println("S BOF procedure EOF");
			System.out.println("BOF BOF");
			traverse(parsetree, 0); // write forward leftmost derivation
			System.out.println("EOF EOF");
			
			/*
			for(int i=0;i<steps.size();i++){
				System.out.println(join(steps.get(i)));
			}
			*/
		}else{
			System.exit(1);
		}
	}
	
	public static void fillTrans(){
		trans.add(new String[]{"6","NE","reduce","33"});
		trans.add(new String[]{"90","NE","reduce","32"});
		trans.add(new String[]{"46","NUM","shift","1"});
		trans.add(new String[]{"54","NUM","shift","1"});
		trans.add(new String[]{"32","NUM","shift","1"});
		trans.add(new String[]{"36","NE","reduce","31"});
		trans.add(new String[]{"80","NE","reduce","34"});
		trans.add(new String[]{"80","MINUS","reduce","34"});
		trans.add(new String[]{"30","STAR","shift","2"});
		trans.add(new String[]{"103","RETURN","reduce","8"});
		trans.add(new String[]{"59","EQ","reduce","36"});
		trans.add(new String[]{"12","NE","reduce","28"});
		trans.add(new String[]{"1","NE","reduce","29"});
		trans.add(new String[]{"11","NE","reduce","30"});
		trans.add(new String[]{"53","EQ","reduce","35"});
		trans.add(new String[]{"47","NEW","shift","3"});
		trans.add(new String[]{"48","NEW","shift","3"});
		trans.add(new String[]{"12","MINUS","reduce","28"});
		trans.add(new String[]{"1","MINUS","reduce","29"});
		trans.add(new String[]{"11","MINUS","reduce","30"});
		trans.add(new String[]{"6","MINUS","reduce","33"});
		trans.add(new String[]{"90","MINUS","reduce","32"});
		trans.add(new String[]{"45","EQ","reduce","37"});
		trans.add(new String[]{"36","MINUS","reduce","31"});
		trans.add(new String[]{"101","RETURN","reduce","8"});
		trans.add(new String[]{"100","RETURN","reduce","8"});
		trans.add(new String[]{"34","SEMI","shift","4"});
		trans.add(new String[]{"51","SEMI","shift","5"});
		trans.add(new String[]{"4","WHILE","reduce","6"});
		trans.add(new String[]{"5","WHILE","reduce","5"});
		trans.add(new String[]{"85","STAR","reduce","4"});
		trans.add(new String[]{"67","PRINTLN","reduce","8"});
		trans.add(new String[]{"14","factor","shift","6"});
		trans.add(new String[]{"6","RBRACK","reduce","33"});
		trans.add(new String[]{"90","RBRACK","reduce","32"});
		trans.add(new String[]{"23","GT","shift","7"});
		trans.add(new String[]{"71","RBRACE","shift","8"});
		trans.add(new String[]{"72","RBRACE","shift","9"});
		trans.add(new String[]{"66","RETURN","reduce","9"});
		trans.add(new String[]{"36","RBRACK","reduce","31"});
		trans.add(new String[]{"80","RBRACK","reduce","34"});
		trans.add(new String[]{"73","RBRACE","shift","10"});
		trans.add(new String[]{"12","RBRACK","reduce","28"});
		trans.add(new String[]{"1","RBRACK","reduce","29"});
		trans.add(new String[]{"11","RBRACK","reduce","30"});
		trans.add(new String[]{"4","INT","reduce","6"});
		trans.add(new String[]{"5","INT","reduce","5"});
		trans.add(new String[]{"83","NULL","shift","11"});
		trans.add(new String[]{"13","NULL","shift","11"});
		trans.add(new String[]{"7","ID","shift","12"});
		trans.add(new String[]{"16","ID","shift","12"});
		trans.add(new String[]{"24","ID","shift","12"});
		trans.add(new String[]{"21","ID","shift","12"});
		trans.add(new String[]{"37","ID","shift","12"});
		trans.add(new String[]{"17","ID","shift","12"});
		trans.add(new String[]{"81","BECOMES","reduce","7"});
		trans.add(new String[]{"83","LPAREN","shift","13"});
		trans.add(new String[]{"46","STAR","shift","14"});
		trans.add(new String[]{"54","STAR","shift","14"});
		trans.add(new String[]{"32","STAR","shift","14"});
		trans.add(new String[]{"83","factor","shift","15"});
		trans.add(new String[]{"45","GT","reduce","37"});
		trans.add(new String[]{"13","factor","shift","15"});
		trans.add(new String[]{"59","GT","reduce","36"});
		trans.add(new String[]{"13","LPAREN","shift","13"});
		trans.add(new String[]{"53","GT","reduce","35"});
		trans.add(new String[]{"23","GE","shift","16"});
		trans.add(new String[]{"53","GE","reduce","35"});
		trans.add(new String[]{"59","GE","reduce","36"});
		trans.add(new String[]{"85","PRINTLN","reduce","4"});
		trans.add(new String[]{"45","GE","reduce","37"});
		trans.add(new String[]{"23","EQ","shift","17"});
		trans.add(new String[]{"68","BECOMES","shift","18"});
		trans.add(new String[]{"19","RBRACK","reduce","21"});
		trans.add(new String[]{"78","RBRACK","reduce","22"});
		trans.add(new String[]{"79","RBRACK","reduce","23"});
		trans.add(new String[]{"49","NUM","shift","1"});
		trans.add(new String[]{"18","NUM","shift","1"});
		trans.add(new String[]{"2","LPAREN","shift","13"});
		trans.add(new String[]{"53","PCT","reduce","35"});
		trans.add(new String[]{"27","NUM","shift","1"});
		trans.add(new String[]{"2","STAR","shift","14"});
		trans.add(new String[]{"84","term","shift","19"});
		trans.add(new String[]{"59","PCT","reduce","36"});
		trans.add(new String[]{"45","PCT","reduce","37"});
		trans.add(new String[]{"56","RPAREN","shift","20"});
		trans.add(new String[]{"23","LT","shift","21"});
		trans.add(new String[]{"95","PLUS","shift","22"});
		trans.add(new String[]{"94","PLUS","shift","22"});
		trans.add(new String[]{"93","PLUS","shift","22"});
		trans.add(new String[]{"92","PLUS","shift","22"});
		trans.add(new String[]{"91","PLUS","shift","22"});
		trans.add(new String[]{"96","PLUS","shift","22"});
		trans.add(new String[]{"83","NUM","shift","1"});
		trans.add(new String[]{"47","expr","shift","23"});
		trans.add(new String[]{"48","expr","shift","23"});
		trans.add(new String[]{"59","BECOMES","reduce","36"});
		trans.add(new String[]{"53","BECOMES","reduce","35"});
		trans.add(new String[]{"45","BECOMES","reduce","37"});
		trans.add(new String[]{"84","ID","shift","12"});
		trans.add(new String[]{"47","NULL","shift","11"});
		trans.add(new String[]{"48","NULL","shift","11"});
		trans.add(new String[]{"14","LPAREN","shift","13"});
		trans.add(new String[]{"80","SEMI","reduce","34"});
		trans.add(new String[]{"23","LE","shift","24"});
		trans.add(new String[]{"89","MINUS","shift","25"});
		trans.add(new String[]{"36","SEMI","reduce","31"});
		trans.add(new String[]{"88","MINUS","shift","25"});
		trans.add(new String[]{"13","STAR","shift","14"});
		trans.add(new String[]{"84","STAR","shift","14"});
		trans.add(new String[]{"13","NUM","shift","1"});
		trans.add(new String[]{"83","STAR","shift","14"});
		trans.add(new String[]{"27","term","shift","19"});
		trans.add(new String[]{"49","term","shift","19"});
		trans.add(new String[]{"18","term","shift","19"});
		trans.add(new String[]{"25","AMP","shift","26"});
		trans.add(new String[]{"22","AMP","shift","26"});
		trans.add(new String[]{"67","RETURN","reduce","8"});
		trans.add(new String[]{"10","DELETE","reduce","11"});
		trans.add(new String[]{"105","RBRACE","reduce","10"});
		trans.add(new String[]{"7","STAR","shift","14"});
		trans.add(new String[]{"16","STAR","shift","14"});
		trans.add(new String[]{"24","STAR","shift","14"});
		trans.add(new String[]{"21","STAR","shift","14"});
		trans.add(new String[]{"37","STAR","shift","14"});
		trans.add(new String[]{"17","STAR","shift","14"});
		trans.add(new String[]{"74","RBRACE","reduce","13"});
		trans.add(new String[]{"106","RBRACE","reduce","14"});
		trans.add(new String[]{"66","WHILE","reduce","9"});
		trans.add(new String[]{"9","DELETE","reduce","12"});
		trans.add(new String[]{"9","RBRACE","reduce","12"});
		trans.add(new String[]{"104","RBRACK","shift","27"});
		trans.add(new String[]{"6","SEMI","reduce","33"});
		trans.add(new String[]{"6","STAR","reduce","33"});
		trans.add(new String[]{"90","SEMI","reduce","32"});
		trans.add(new String[]{"90","STAR","reduce","32"});
		trans.add(new String[]{"10","RBRACE","reduce","11"});
		trans.add(new String[]{"12","SEMI","reduce","28"});
		trans.add(new String[]{"12","STAR","reduce","28"});
		trans.add(new String[]{"1","SEMI","reduce","29"});
		trans.add(new String[]{"1","STAR","reduce","29"});
		trans.add(new String[]{"11","SEMI","reduce","30"});
		trans.add(new String[]{"11","STAR","reduce","30"});
		trans.add(new String[]{"53","LE","reduce","35"});
		trans.add(new String[]{"36","STAR","reduce","31"});
		trans.add(new String[]{"80","STAR","reduce","34"});
		trans.add(new String[]{"64","SEMI","shift","28"});
		trans.add(new String[]{"74","PRINTLN","reduce","13"});
		trans.add(new String[]{"106","PRINTLN","reduce","14"});
		trans.add(new String[]{"105","PRINTLN","reduce","10"});
		trans.add(new String[]{"40","PLUS","reduce","25"});
		trans.add(new String[]{"41","PLUS","reduce","26"});
		trans.add(new String[]{"42","PLUS","reduce","27"});
		trans.add(new String[]{"9","PRINTLN","reduce","12"});
		trans.add(new String[]{"2","NUM","shift","1"});
		trans.add(new String[]{"15","PLUS","reduce","24"});
		trans.add(new String[]{"10","PRINTLN","reduce","11"});
		trans.add(new String[]{"27","LPAREN","shift","13"});
		trans.add(new String[]{"88","PLUS","shift","22"});
		trans.add(new String[]{"7","NULL","shift","11"});
		trans.add(new String[]{"16","NULL","shift","11"});
		trans.add(new String[]{"24","NULL","shift","11"});
		trans.add(new String[]{"21","NULL","shift","11"});
		trans.add(new String[]{"37","NULL","shift","11"});
		trans.add(new String[]{"17","NULL","shift","11"});
		trans.add(new String[]{"89","PLUS","shift","22"});
		trans.add(new String[]{"7","NUM","shift","1"});
		trans.add(new String[]{"16","NUM","shift","1"});
		trans.add(new String[]{"24","NUM","shift","1"});
		trans.add(new String[]{"21","NUM","shift","1"});
		trans.add(new String[]{"37","NUM","shift","1"});
		trans.add(new String[]{"17","NUM","shift","1"});
		trans.add(new String[]{"49","LPAREN","shift","13"});
		trans.add(new String[]{"18","LPAREN","shift","13"});
		trans.add(new String[]{"101","STAR","reduce","8"});
		trans.add(new String[]{"100","STAR","reduce","8"});
		trans.add(new String[]{"67","STAR","reduce","8"});
		trans.add(new String[]{"15","SEMI","reduce","24"});
		trans.add(new String[]{"103","STAR","reduce","8"});
		trans.add(new String[]{"39","STAR","shift","29"});
		trans.add(new String[]{"40","SEMI","reduce","25"});
		trans.add(new String[]{"41","SEMI","reduce","26"});
		trans.add(new String[]{"42","SEMI","reduce","27"});
		trans.add(new String[]{"80","RPAREN","reduce","34"});
		trans.add(new String[]{"67","WHILE","reduce","8"});
		trans.add(new String[]{"105","DELETE","reduce","10"});
		trans.add(new String[]{"74","DELETE","reduce","13"});
		trans.add(new String[]{"106","DELETE","reduce","14"});
		trans.add(new String[]{"12","RPAREN","reduce","28"});
		trans.add(new String[]{"1","RPAREN","reduce","29"});
		trans.add(new String[]{"11","RPAREN","reduce","30"});
		trans.add(new String[]{"6","RPAREN","reduce","33"});
		trans.add(new String[]{"90","RPAREN","reduce","32"});
		trans.add(new String[]{"47","STAR","shift","14"});
		trans.add(new String[]{"48","STAR","shift","14"});
		trans.add(new String[]{"36","RPAREN","reduce","31"});
		trans.add(new String[]{"19","LT","reduce","21"});
		trans.add(new String[]{"45","PLUS","reduce","37"});
		trans.add(new String[]{"97","LPAREN","shift","30"});
		trans.add(new String[]{"78","LT","reduce","22"});
		trans.add(new String[]{"79","LT","reduce","23"});
		trans.add(new String[]{"53","PLUS","reduce","35"});
		trans.add(new String[]{"59","PLUS","reduce","36"});
		trans.add(new String[]{"15","NE","reduce","24"});
		trans.add(new String[]{"64","PLUS","shift","22"});
		trans.add(new String[]{"40","NE","reduce","25"});
		trans.add(new String[]{"41","NE","reduce","26"});
		trans.add(new String[]{"42","NE","reduce","27"});
		trans.add(new String[]{"28","RBRACE","shift","31"});
		trans.add(new String[]{"39","ID","reduce","2"});
		trans.add(new String[]{"14","NEW","shift","3"});
		trans.add(new String[]{"30","LPAREN","shift","30"});
		trans.add(new String[]{"80","EQ","reduce","34"});
		trans.add(new String[]{"12","GT","reduce","28"});
		trans.add(new String[]{"1","GT","reduce","29"});
		trans.add(new String[]{"11","GT","reduce","30"});
		trans.add(new String[]{"36","GT","reduce","31"});
		trans.add(new String[]{"6","GT","reduce","33"});
		trans.add(new String[]{"90","GT","reduce","32"});
		trans.add(new String[]{"78","RPAREN","reduce","22"});
		trans.add(new String[]{"79","RPAREN","reduce","23"});
		trans.add(new String[]{"19","RPAREN","reduce","21"});
		trans.add(new String[]{"36","GE","reduce","31"});
		trans.add(new String[]{"80","GE","reduce","34"});
		trans.add(new String[]{"67","ID","reduce","8"});
		trans.add(new String[]{"10","WHILE","reduce","11"});
		trans.add(new String[]{"67","IF","reduce","8"});
		trans.add(new String[]{"66","RBRACE","reduce","9"});
		trans.add(new String[]{"12","GE","reduce","28"});
		trans.add(new String[]{"1","GE","reduce","29"});
		trans.add(new String[]{"11","GE","reduce","30"});
		trans.add(new String[]{"6","GE","reduce","33"});
		trans.add(new String[]{"90","GE","reduce","32"});
		trans.add(new String[]{"78","NE","reduce","22"});
		trans.add(new String[]{"79","NE","reduce","23"});
		trans.add(new String[]{"19","NE","reduce","21"});
		trans.add(new String[]{"4","STAR","reduce","6"});
		trans.add(new String[]{"5","STAR","reduce","5"});
		trans.add(new String[]{"9","WHILE","reduce","12"});
		trans.add(new String[]{"25","NEW","shift","3"});
		trans.add(new String[]{"22","NEW","shift","3"});
		trans.add(new String[]{"74","WHILE","reduce","13"});
		trans.add(new String[]{"106","WHILE","reduce","14"});
		trans.add(new String[]{"105","WHILE","reduce","10"});
		trans.add(new String[]{"101","IF","reduce","8"});
		trans.add(new String[]{"100","IF","reduce","8"});
		trans.add(new String[]{"103","ID","reduce","8"});
		trans.add(new String[]{"73","STAR","shift","2"});
		trans.add(new String[]{"103","IF","reduce","8"});
		trans.add(new String[]{"79","PCT","shift","32"});
		trans.add(new String[]{"78","PCT","shift","32"});
		trans.add(new String[]{"53","RBRACK","reduce","35"});
		trans.add(new String[]{"59","RBRACK","reduce","36"});
		trans.add(new String[]{"45","RBRACK","reduce","37"});
		trans.add(new String[]{"101","ID","reduce","8"});
		trans.add(new String[]{"100","ID","reduce","8"});
		trans.add(new String[]{"3","INT","shift","33"});
		trans.add(new String[]{"71","STAR","shift","2"});
		trans.add(new String[]{"72","STAR","shift","2"});
		trans.add(new String[]{"85","LPAREN","reduce","4"});
		trans.add(new String[]{"46","ID","shift","12"});
		trans.add(new String[]{"54","ID","shift","12"});
		trans.add(new String[]{"32","ID","shift","12"});
		trans.add(new String[]{"66","LPAREN","reduce","9"});
		trans.add(new String[]{"80","GT","reduce","34"});
		trans.add(new String[]{"85","IF","reduce","4"});
		trans.add(new String[]{"43","NULL","shift","34"});
		trans.add(new String[]{"85","ID","reduce","4"});
		trans.add(new String[]{"8","ELSE","shift","35"});
		trans.add(new String[]{"66","DELETE","reduce","9"});
		trans.add(new String[]{"89","RPAREN","shift","36"});
		trans.add(new String[]{"23","NE","shift","37"});
		trans.add(new String[]{"97","WHILE","shift","38"});
		trans.add(new String[]{"19","LE","reduce","21"});
		trans.add(new String[]{"14","AMP","shift","26"});
		trans.add(new String[]{"98","INT","shift","39"});
		trans.add(new String[]{"70","INT","shift","39"});
		trans.add(new String[]{"71","LPAREN","shift","30"});
		trans.add(new String[]{"72","LPAREN","shift","30"});
		trans.add(new String[]{"46","factor","shift","40"});
		trans.add(new String[]{"54","factor","shift","41"});
		trans.add(new String[]{"32","factor","shift","42"});
		trans.add(new String[]{"81","RPAREN","reduce","7"});
		trans.add(new String[]{"13","term","shift","19"});
		trans.add(new String[]{"78","LE","reduce","22"});
		trans.add(new String[]{"79","LE","reduce","23"});
		trans.add(new String[]{"83","term","shift","19"});
		trans.add(new String[]{"73","LPAREN","shift","30"});
		trans.add(new String[]{"58","BECOMES","shift","43"});
		trans.add(new String[]{"80","PCT","reduce","34"});
		trans.add(new String[]{"12","PCT","reduce","28"});
		trans.add(new String[]{"1","PCT","reduce","29"});
		trans.add(new String[]{"11","PCT","reduce","30"});
		trans.add(new String[]{"6","PCT","reduce","33"});
		trans.add(new String[]{"90","PCT","reduce","32"});
		trans.add(new String[]{"36","PCT","reduce","31"});
		trans.add(new String[]{"40","MINUS","reduce","25"});
		trans.add(new String[]{"41","MINUS","reduce","26"});
		trans.add(new String[]{"42","MINUS","reduce","27"});
		trans.add(new String[]{"15","MINUS","reduce","24"});
		trans.add(new String[]{"99","INT","shift","44"});
		trans.add(new String[]{"2","AMP","shift","26"});
		trans.add(new String[]{"15","STAR","reduce","24"});
		trans.add(new String[]{"45","RPAREN","reduce","37"});
		trans.add(new String[]{"7","factor","shift","15"});
		trans.add(new String[]{"16","factor","shift","15"});
		trans.add(new String[]{"24","factor","shift","15"});
		trans.add(new String[]{"21","factor","shift","15"});
		trans.add(new String[]{"37","factor","shift","15"});
		trans.add(new String[]{"17","factor","shift","15"});
		trans.add(new String[]{"9","LPAREN","reduce","12"});
		trans.add(new String[]{"105","LPAREN","reduce","10"});
		trans.add(new String[]{"74","LPAREN","reduce","13"});
		trans.add(new String[]{"106","LPAREN","reduce","14"});
		trans.add(new String[]{"53","RPAREN","reduce","35"});
		trans.add(new String[]{"69","RPAREN","shift","45"});
		trans.add(new String[]{"59","RPAREN","reduce","36"});
		trans.add(new String[]{"84","NUM","shift","1"});
		trans.add(new String[]{"10","LPAREN","reduce","11"});
		trans.add(new String[]{"19","STAR","shift","46"});
		trans.add(new String[]{"79","STAR","shift","46"});
		trans.add(new String[]{"78","STAR","shift","46"});
		trans.add(new String[]{"47","LPAREN","shift","13"});
		trans.add(new String[]{"48","LPAREN","shift","13"});
		trans.add(new String[]{"52","LPAREN","shift","47"});
		trans.add(new String[]{"38","LPAREN","shift","48"});
		trans.add(new String[]{"82","LPAREN","shift","49"});
		trans.add(new String[]{"19","MINUS","reduce","21"});
		trans.add(new String[]{"99","procedure","shift","50"});
		trans.add(new String[]{"78","MINUS","reduce","22"});
		trans.add(new String[]{"79","MINUS","reduce","23"});
		trans.add(new String[]{"36","LT","reduce","31"});
		trans.add(new String[]{"12","LT","reduce","28"});
		trans.add(new String[]{"1","LT","reduce","29"});
		trans.add(new String[]{"11","LT","reduce","30"});
		trans.add(new String[]{"67","INT","shift","39"});
		trans.add(new String[]{"6","LT","reduce","33"});
		trans.add(new String[]{"90","LT","reduce","32"});
		trans.add(new String[]{"80","LT","reduce","34"});
		trans.add(new String[]{"78","GE","reduce","22"});
		trans.add(new String[]{"79","GE","reduce","23"});
		trans.add(new String[]{"43","NUM","shift","51"});
		trans.add(new String[]{"97","IF","shift","52"});
		trans.add(new String[]{"19","GE","reduce","21"});
		trans.add(new String[]{"15","RBRACK","reduce","24"});
		trans.add(new String[]{"36","LE","reduce","31"});
		trans.add(new String[]{"6","LE","reduce","33"});
		trans.add(new String[]{"90","LE","reduce","32"});
		trans.add(new String[]{"23","PLUS","shift","22"});
		trans.add(new String[]{"40","RBRACK","reduce","25"});
		trans.add(new String[]{"41","RBRACK","reduce","26"});
		trans.add(new String[]{"42","RBRACK","reduce","27"});
		trans.add(new String[]{"12","LE","reduce","28"});
		trans.add(new String[]{"1","LE","reduce","29"});
		trans.add(new String[]{"11","LE","reduce","30"});
		trans.add(new String[]{"80","LE","reduce","34"});
		trans.add(new String[]{"97","ID","shift","53"});
		trans.add(new String[]{"47","ID","shift","12"});
		trans.add(new String[]{"48","ID","shift","12"});
		trans.add(new String[]{"78","GT","reduce","22"});
		trans.add(new String[]{"79","GT","reduce","23"});
		trans.add(new String[]{"19","SLASH","shift","54"});
		trans.add(new String[]{"19","GT","reduce","21"});
		trans.add(new String[]{"79","SLASH","shift","54"});
		trans.add(new String[]{"78","SLASH","shift","54"});
		trans.add(new String[]{"84","NULL","shift","11"});
		trans.add(new String[]{"13","AMP","shift","26"});
		//trans.add(new String[]{"31","EOF","reduce","1"});
		trans.add(new String[]{"83","AMP","shift","26"});
		trans.add(new String[]{"40","STAR","reduce","25"});
		trans.add(new String[]{"41","STAR","reduce","26"});
		trans.add(new String[]{"42","STAR","reduce","27"});
		trans.add(new String[]{"67","RBRACE","reduce","8"});
		trans.add(new String[]{"15","EQ","reduce","24"});
		trans.add(new String[]{"40","EQ","reduce","25"});
		trans.add(new String[]{"41","EQ","reduce","26"});
		trans.add(new String[]{"42","EQ","reduce","27"});
		trans.add(new String[]{"27","expr","shift","55"});
		trans.add(new String[]{"49","expr","shift","56"});
		trans.add(new String[]{"18","expr","shift","57"});
		trans.add(new String[]{"103","RBRACE","reduce","8"});
		trans.add(new String[]{"67","dcl","shift","58"});
		trans.add(new String[]{"13","ID","shift","12"});
		trans.add(new String[]{"46","AMP","shift","26"});
		trans.add(new String[]{"54","AMP","shift","26"});
		trans.add(new String[]{"32","AMP","shift","26"});
		trans.add(new String[]{"83","ID","shift","12"});
		trans.add(new String[]{"2","factor","shift","59"});
		trans.add(new String[]{"4","PRINTLN","reduce","6"});
		trans.add(new String[]{"5","PRINTLN","reduce","5"});
		trans.add(new String[]{"98","dcl","shift","60"});
		trans.add(new String[]{"97","STAR","shift","2"});
		trans.add(new String[]{"71","DELETE","shift","61"});
		trans.add(new String[]{"72","DELETE","shift","61"});
		trans.add(new String[]{"80","SLASH","reduce","34"});
		trans.add(new String[]{"70","dcl","shift","62"});
		trans.add(new String[]{"27","ID","shift","12"});
		trans.add(new String[]{"49","ID","shift","12"});
		trans.add(new String[]{"18","ID","shift","12"});
		trans.add(new String[]{"67","type","shift","63"});
		trans.add(new String[]{"36","PLUS","reduce","31"});
		trans.add(new String[]{"6","SLASH","reduce","33"});
		trans.add(new String[]{"90","SLASH","reduce","32"});
		trans.add(new String[]{"36","SLASH","reduce","31"});
		trans.add(new String[]{"80","PLUS","reduce","34"});
		trans.add(new String[]{"67","LPAREN","reduce","8"});
		trans.add(new String[]{"12","SLASH","reduce","28"});
		trans.add(new String[]{"1","SLASH","reduce","29"});
		trans.add(new String[]{"11","SLASH","reduce","30"});
		trans.add(new String[]{"12","PLUS","reduce","28"});
		trans.add(new String[]{"1","PLUS","reduce","29"});
		trans.add(new String[]{"11","PLUS","reduce","30"});
		trans.add(new String[]{"6","PLUS","reduce","33"});
		trans.add(new String[]{"90","PLUS","reduce","32"});
		trans.add(new String[]{"15","GT","reduce","24"});
		trans.add(new String[]{"40","GT","reduce","25"});
		trans.add(new String[]{"41","GT","reduce","26"});
		trans.add(new String[]{"42","GT","reduce","27"});
		trans.add(new String[]{"26","LPAREN","shift","30"});
		trans.add(new String[]{"70","type","shift","63"});
		trans.add(new String[]{"98","type","shift","63"});
		trans.add(new String[]{"23","MINUS","shift","25"});
		trans.add(new String[]{"15","GE","reduce","24"});
		trans.add(new String[]{"84","expr","shift","64"});
		trans.add(new String[]{"40","GE","reduce","25"});
		trans.add(new String[]{"41","GE","reduce","26"});
		trans.add(new String[]{"42","GE","reduce","27"});
		trans.add(new String[]{"45","STAR","reduce","37"});
		trans.add(new String[]{"59","STAR","reduce","36"});
		trans.add(new String[]{"53","STAR","reduce","35"});
		trans.add(new String[]{"73","DELETE","shift","61"});
		trans.add(new String[]{"103","WHILE","reduce","8"});
		trans.add(new String[]{"85","DELETE","reduce","4"});
		trans.add(new String[]{"60","RPAREN","shift","65"});
		trans.add(new String[]{"101","RBRACE","reduce","8"});
		trans.add(new String[]{"100","RBRACE","reduce","8"});
		trans.add(new String[]{"71","statement","shift","66"});
		trans.add(new String[]{"72","statement","shift","66"});
		trans.add(new String[]{"49","factor","shift","15"});
		trans.add(new String[]{"18","factor","shift","15"});
		trans.add(new String[]{"7","AMP","shift","26"});
		trans.add(new String[]{"16","AMP","shift","26"});
		trans.add(new String[]{"24","AMP","shift","26"});
		trans.add(new String[]{"21","AMP","shift","26"});
		trans.add(new String[]{"37","AMP","shift","26"});
		trans.add(new String[]{"17","AMP","shift","26"});
		trans.add(new String[]{"27","factor","shift","15"});
		trans.add(new String[]{"73","statement","shift","66"});
		trans.add(new String[]{"101","WHILE","reduce","8"});
		trans.add(new String[]{"100","WHILE","reduce","8"});
		trans.add(new String[]{"85","dcls","shift","67"});
		trans.add(new String[]{"97","lvalue","shift","68"});
		trans.add(new String[]{"85","WHILE","reduce","4"});
		trans.add(new String[]{"84","NEW","shift","3"});
		trans.add(new String[]{"85","RETURN","reduce","4"});
		trans.add(new String[]{"66","ID","reduce","9"});
		trans.add(new String[]{"66","IF","reduce","9"});
		trans.add(new String[]{"2","NEW","shift","3"});
		trans.add(new String[]{"30","lvalue","shift","69"});
		trans.add(new String[]{"75","LPAREN","shift","70"});
		trans.add(new String[]{"47","factor","shift","15"});
		trans.add(new String[]{"48","factor","shift","15"});
		trans.add(new String[]{"13","NEW","shift","3"});
		trans.add(new String[]{"101","statements","shift","71"});
		trans.add(new String[]{"100","statements","shift","72"});
		trans.add(new String[]{"83","NEW","shift","3"});
		trans.add(new String[]{"103","statements","shift","73"});
		trans.add(new String[]{"27","NEW","shift","3"});
		trans.add(new String[]{"49","NEW","shift","3"});
		trans.add(new String[]{"18","NEW","shift","3"});
		trans.add(new String[]{"25","ID","shift","12"});
		trans.add(new String[]{"22","ID","shift","12"});
		trans.add(new String[]{"26","STAR","shift","2"});
		trans.add(new String[]{"2","ID","shift","12"});
		trans.add(new String[]{"66","STAR","reduce","9"});
		trans.add(new String[]{"103","PRINTLN","reduce","8"});
		trans.add(new String[]{"47","AMP","shift","26"});
		trans.add(new String[]{"48","AMP","shift","26"});
		trans.add(new String[]{"14","ID","shift","12"});
		trans.add(new String[]{"101","PRINTLN","reduce","8"});
		trans.add(new String[]{"100","PRINTLN","reduce","8"});
		trans.add(new String[]{"19","PCT","shift","32"});
		trans.add(new String[]{"4","LPAREN","reduce","6"});
		trans.add(new String[]{"5","LPAREN","reduce","5"});
		trans.add(new String[]{"71","lvalue","shift","68"});
		trans.add(new String[]{"72","lvalue","shift","68"});
		trans.add(new String[]{"73","lvalue","shift","68"});
		trans.add(new String[]{"36","EQ","reduce","31"});
		trans.add(new String[]{"20","SEMI","shift","74"});
		trans.add(new String[]{"12","EQ","reduce","28"});
		trans.add(new String[]{"1","EQ","reduce","29"});
		trans.add(new String[]{"11","EQ","reduce","30"});
		trans.add(new String[]{"49","NULL","shift","11"});
		trans.add(new String[]{"18","NULL","shift","11"});
		trans.add(new String[]{"6","EQ","reduce","33"});
		trans.add(new String[]{"90","EQ","reduce","32"});
		trans.add(new String[]{"27","NULL","shift","11"});
		trans.add(new String[]{"40","LT","reduce","25"});
		trans.add(new String[]{"41","LT","reduce","26"});
		trans.add(new String[]{"42","LT","reduce","27"});
		trans.add(new String[]{"15","LT","reduce","24"});
		trans.add(new String[]{"81","COMMA","reduce","7"});
		trans.add(new String[]{"10","RETURN","reduce","11"});
		trans.add(new String[]{"15","LE","reduce","24"});
		trans.add(new String[]{"97","statement","shift","66"});
		trans.add(new String[]{"44","WAIN","shift","75"});
		trans.add(new String[]{"59","MINUS","reduce","36"});
		trans.add(new String[]{"53","MINUS","reduce","35"});
		trans.add(new String[]{"15","RPAREN","reduce","24"});
		trans.add(new String[]{"45","MINUS","reduce","37"});
		trans.add(new String[]{"40","LE","reduce","25"});
		trans.add(new String[]{"41","LE","reduce","26"});
		trans.add(new String[]{"42","LE","reduce","27"});
		trans.add(new String[]{"105","RETURN","reduce","10"});
		trans.add(new String[]{"40","RPAREN","reduce","25"});
		trans.add(new String[]{"41","RPAREN","reduce","26"});
		trans.add(new String[]{"42","RPAREN","reduce","27"});
		trans.add(new String[]{"7","term","shift","19"});
		trans.add(new String[]{"16","term","shift","19"});
		trans.add(new String[]{"24","term","shift","19"});
		trans.add(new String[]{"21","term","shift","19"});
		trans.add(new String[]{"37","term","shift","19"});
		trans.add(new String[]{"17","term","shift","19"});
		trans.add(new String[]{"74","RETURN","reduce","13"});
		trans.add(new String[]{"106","RETURN","reduce","14"});
		trans.add(new String[]{"7","NEW","shift","3"});
		trans.add(new String[]{"16","NEW","shift","3"});
		trans.add(new String[]{"24","NEW","shift","3"});
		trans.add(new String[]{"21","NEW","shift","3"});
		trans.add(new String[]{"37","NEW","shift","3"});
		trans.add(new String[]{"17","NEW","shift","3"});
		trans.add(new String[]{"9","RETURN","reduce","12"});
		trans.add(new String[]{"66","PRINTLN","reduce","9"});
		trans.add(new String[]{"53","LT","reduce","35"});
		trans.add(new String[]{"59","LT","reduce","36"});
		trans.add(new String[]{"15","PCT","reduce","24"});
		trans.add(new String[]{"47","test","shift","76"});
		trans.add(new String[]{"48","test","shift","77"});
		trans.add(new String[]{"59","LE","reduce","36"});
		trans.add(new String[]{"45","LE","reduce","37"});
		trans.add(new String[]{"9","STAR","reduce","12"});
		trans.add(new String[]{"22","term","shift","78"});
		trans.add(new String[]{"25","term","shift","79"});
		trans.add(new String[]{"40","PCT","reduce","25"});
		trans.add(new String[]{"41","PCT","reduce","26"});
		trans.add(new String[]{"42","PCT","reduce","27"});
		trans.add(new String[]{"10","STAR","reduce","11"});
		trans.add(new String[]{"101","LPAREN","reduce","8"});
		trans.add(new String[]{"100","LPAREN","reduce","8"});
		trans.add(new String[]{"88","RBRACK","shift","80"});
		trans.add(new String[]{"74","STAR","reduce","13"});
		trans.add(new String[]{"106","STAR","reduce","14"});
		trans.add(new String[]{"103","LPAREN","reduce","8"});
		trans.add(new String[]{"45","LT","reduce","37"});
		trans.add(new String[]{"105","STAR","reduce","10"});
		trans.add(new String[]{"97","DELETE","shift","61"});
		trans.add(new String[]{"103","DELETE","reduce","8"});
		trans.add(new String[]{"63","ID","shift","81"});
		trans.add(new String[]{"64","MINUS","shift","25"});
		trans.add(new String[]{"101","DELETE","reduce","8"});
		trans.add(new String[]{"100","DELETE","reduce","8"});
		trans.add(new String[]{"47","term","shift","19"});
		trans.add(new String[]{"48","term","shift","19"});
		trans.add(new String[]{"97","PRINTLN","shift","82"});
		trans.add(new String[]{"71","ID","shift","53"});
		trans.add(new String[]{"72","ID","shift","53"});
		trans.add(new String[]{"45","NE","reduce","37"});
		trans.add(new String[]{"25","NUM","shift","1"});
		trans.add(new String[]{"22","NUM","shift","1"});
		trans.add(new String[]{"71","IF","shift","52"});
		trans.add(new String[]{"72","IF","shift","52"});
		trans.add(new String[]{"59","NE","reduce","36"});
		trans.add(new String[]{"53","NE","reduce","35"});
		trans.add(new String[]{"73","ID","shift","53"});
		trans.add(new String[]{"73","IF","shift","52"});
		trans.add(new String[]{"30","ID","shift","53"});
		trans.add(new String[]{"7","LPAREN","shift","13"});
		trans.add(new String[]{"16","LPAREN","shift","13"});
		trans.add(new String[]{"24","LPAREN","shift","13"});
		trans.add(new String[]{"21","LPAREN","shift","13"});
		trans.add(new String[]{"37","LPAREN","shift","13"});
		trans.add(new String[]{"17","LPAREN","shift","13"});
		trans.add(new String[]{"25","STAR","shift","14"});
		trans.add(new String[]{"22","STAR","shift","14"});
		trans.add(new String[]{"57","PLUS","shift","22"});
		trans.add(new String[]{"56","PLUS","shift","22"});
		trans.add(new String[]{"55","PLUS","shift","22"});
		trans.add(new String[]{"40","SLASH","reduce","25"});
		trans.add(new String[]{"41","SLASH","reduce","26"});
		trans.add(new String[]{"42","SLASH","reduce","27"});
		trans.add(new String[]{"15","SLASH","reduce","24"});
		trans.add(new String[]{"33","LBRACK","shift","83"});
		trans.add(new String[]{"78","PLUS","reduce","22"});
		trans.add(new String[]{"79","PLUS","reduce","23"});
		trans.add(new String[]{"97","RETURN","shift","84"});
		trans.add(new String[]{"55","MINUS","shift","25"});
		trans.add(new String[]{"57","MINUS","shift","25"});
		trans.add(new String[]{"56","MINUS","shift","25"});
		trans.add(new String[]{"19","PLUS","reduce","21"});
		trans.add(new String[]{"67","DELETE","reduce","8"});
		trans.add(new String[]{"46","LPAREN","shift","13"});
		trans.add(new String[]{"54","LPAREN","shift","13"});
		trans.add(new String[]{"32","LPAREN","shift","13"});
		trans.add(new String[]{"45","SEMI","reduce","37"});
		trans.add(new String[]{"59","SEMI","reduce","36"});
		trans.add(new String[]{"53","SEMI","reduce","35"});
		trans.add(new String[]{"49","AMP","shift","26"});
		trans.add(new String[]{"18","AMP","shift","26"});
		trans.add(new String[]{"27","AMP","shift","26"});
		trans.add(new String[]{"95","MINUS","shift","25"});
		trans.add(new String[]{"94","MINUS","shift","25"});
		trans.add(new String[]{"93","MINUS","shift","25"});
		trans.add(new String[]{"92","MINUS","shift","25"});
		trans.add(new String[]{"91","MINUS","shift","25"});
		trans.add(new String[]{"96","MINUS","shift","25"});
		trans.add(new String[]{"65","LBRACE","shift","85"});
		trans.add(new String[]{"71","WHILE","shift","38"});
		trans.add(new String[]{"72","WHILE","shift","38"});
		trans.add(new String[]{"4","DELETE","reduce","6"});
		trans.add(new String[]{"5","DELETE","reduce","5"});
		trans.add(new String[]{"73","WHILE","shift","38"});
		trans.add(new String[]{"22","factor","shift","15"});
		trans.add(new String[]{"25","factor","shift","15"});
		trans.add(new String[]{"47","NUM","shift","1"});
		trans.add(new String[]{"48","NUM","shift","1"});
		trans.add(new String[]{"76","RPAREN","shift","86"});
		trans.add(new String[]{"77","RPAREN","shift","87"});
		trans.add(new String[]{"2","NULL","shift","11"});
		trans.add(new String[]{"26","ID","shift","53"});
		trans.add(new String[]{"46","NEW","shift","3"});
		trans.add(new String[]{"54","NEW","shift","3"});
		trans.add(new String[]{"32","NEW","shift","3"});
		trans.add(new String[]{"19","SEMI","reduce","21"});
		trans.add(new String[]{"83","expr","shift","88"});
		trans.add(new String[]{"13","expr","shift","89"});
		trans.add(new String[]{"4","IF","reduce","6"});
		trans.add(new String[]{"5","IF","reduce","5"});
		trans.add(new String[]{"78","SEMI","reduce","22"});
		trans.add(new String[]{"79","SEMI","reduce","23"});
		trans.add(new String[]{"14","STAR","shift","14"});
		trans.add(new String[]{"26","lvalue","shift","90"});
		trans.add(new String[]{"4","ID","reduce","6"});
		trans.add(new String[]{"5","ID","reduce","5"});
		trans.add(new String[]{"36","BECOMES","reduce","31"});
		trans.add(new String[]{"25","LPAREN","shift","13"});
		trans.add(new String[]{"49","STAR","shift","14"});
		trans.add(new String[]{"18","STAR","shift","14"});
		trans.add(new String[]{"22","LPAREN","shift","13"});
		trans.add(new String[]{"6","BECOMES","reduce","33"});
		trans.add(new String[]{"90","BECOMES","reduce","32"});
		trans.add(new String[]{"12","BECOMES","reduce","28"});
		trans.add(new String[]{"1","BECOMES","reduce","29"});
		trans.add(new String[]{"11","BECOMES","reduce","30"});
		trans.add(new String[]{"27","STAR","shift","14"});
		trans.add(new String[]{"29","ID","reduce","3"});
		trans.add(new String[]{"10","IF","reduce","11"});
		trans.add(new String[]{"10","ID","reduce","11"});
		trans.add(new String[]{"9","IF","reduce","12"});
		trans.add(new String[]{"105","IF","reduce","10"});
		trans.add(new String[]{"74","IF","reduce","13"});
		trans.add(new String[]{"106","IF","reduce","14"});
		trans.add(new String[]{"9","ID","reduce","12"});
		trans.add(new String[]{"14","NULL","shift","11"});
		trans.add(new String[]{"80","BECOMES","reduce","34"});
		trans.add(new String[]{"95","RPAREN","reduce","16"});
		trans.add(new String[]{"94","RPAREN","reduce","17"});
		trans.add(new String[]{"93","RPAREN","reduce","18"});
		trans.add(new String[]{"92","RPAREN","reduce","19"});
		trans.add(new String[]{"91","RPAREN","reduce","20"});
		trans.add(new String[]{"96","RPAREN","reduce","15"});
		trans.add(new String[]{"46","NULL","shift","11"});
		trans.add(new String[]{"54","NULL","shift","11"});
		trans.add(new String[]{"32","NULL","shift","11"});
		trans.add(new String[]{"14","NUM","shift","1"});
		trans.add(new String[]{"7","expr","shift","91"});
		trans.add(new String[]{"16","expr","shift","92"});
		trans.add(new String[]{"24","expr","shift","93"});
		trans.add(new String[]{"21","expr","shift","94"});
		trans.add(new String[]{"37","expr","shift","95"});
		trans.add(new String[]{"17","expr","shift","96"});
		trans.add(new String[]{"4","RETURN","reduce","6"});
		trans.add(new String[]{"5","RETURN","reduce","5"});
		trans.add(new String[]{"67","statements","shift","97"});
		trans.add(new String[]{"74","ID","reduce","13"});
		trans.add(new String[]{"106","ID","reduce","14"});
		trans.add(new String[]{"105","ID","reduce","10"});
		trans.add(new String[]{"62","COMMA","shift","98"});
		//trans.add(new String[]{"0","BOF","shift","99"});
		trans.add(new String[]{"19","EQ","reduce","21"});
		trans.add(new String[]{"78","EQ","reduce","22"});
		trans.add(new String[]{"79","EQ","reduce","23"});
		trans.add(new String[]{"45","SLASH","reduce","37"});
		trans.add(new String[]{"87","LBRACE","shift","100"});
		trans.add(new String[]{"86","LBRACE","shift","101"});
		trans.add(new String[]{"50","EOF","shift","102"});
		trans.add(new String[]{"59","SLASH","reduce","36"});
		trans.add(new String[]{"84","LPAREN","shift","13"});
		trans.add(new String[]{"35","LBRACE","shift","103"});
		trans.add(new String[]{"53","SLASH","reduce","35"});
		trans.add(new String[]{"61","LBRACK","shift","104"});
		trans.add(new String[]{"73","PRINTLN","shift","82"});
		trans.add(new String[]{"57","SEMI","shift","105"});
		trans.add(new String[]{"85","INT","reduce","4"});
		trans.add(new String[]{"55","SEMI","shift","106"});
		trans.add(new String[]{"84","AMP","shift","26"});
		trans.add(new String[]{"71","PRINTLN","shift","82"});
		trans.add(new String[]{"72","PRINTLN","shift","82"});
		trans.add(new String[]{"25","NULL","shift","11"});
		trans.add(new String[]{"84","factor","shift","15"});
		trans.add(new String[]{"22","NULL","shift","11"});

	}
	
	public static void fillRules(){
		//rules.add(new String[]{"S","BOF","procedure","EOF"});
		rules.add(new String[]{"procedure","INT","WAIN","LPAREN","dcl","COMMA","dcl","RPAREN","LBRACE","dcls","statements","RETURN","expr","SEMI","RBRACE"});
		rules.add(new String[]{"type","INT"});
		rules.add(new String[]{"type","INT","STAR"});
		rules.add(new String[]{"dcls"});
		rules.add(new String[]{"dcls","dcls","dcl","BECOMES","NUM","SEMI"});
		rules.add(new String[]{"dcls","dcls","dcl","BECOMES","NULL","SEMI"});
		rules.add(new String[]{"dcl","type","ID"});
		rules.add(new String[]{"statements"});
		rules.add(new String[]{"statements","statements","statement"});
		rules.add(new String[]{"statement","lvalue","BECOMES","expr","SEMI"});
		rules.add(new String[]{"statement","IF","LPAREN","test","RPAREN","LBRACE","statements","RBRACE","ELSE","LBRACE","statements","RBRACE"});
		rules.add(new String[]{"statement","WHILE","LPAREN","test","RPAREN","LBRACE","statements","RBRACE"});
		rules.add(new String[]{"statement","PRINTLN","LPAREN","expr","RPAREN","SEMI"});
		rules.add(new String[]{"statement","DELETE","LBRACK","RBRACK","expr","SEMI"});
		rules.add(new String[]{"test","expr","EQ","expr"});
		rules.add(new String[]{"test","expr","NE","expr"});
		rules.add(new String[]{"test","expr","LT","expr"});
		rules.add(new String[]{"test","expr","LE","expr"});
		rules.add(new String[]{"test","expr","GE","expr"});
		rules.add(new String[]{"test","expr","GT","expr"});
		rules.add(new String[]{"expr","term"});
		rules.add(new String[]{"expr","expr","PLUS","term"});
		rules.add(new String[]{"expr","expr","MINUS","term"});
		rules.add(new String[]{"term","factor"});
		rules.add(new String[]{"term","term","STAR","factor"});
		rules.add(new String[]{"term","term","SLASH","factor"});
		rules.add(new String[]{"term","term","PCT","factor"});
		rules.add(new String[]{"factor","ID"});
		rules.add(new String[]{"factor","NUM"});
		rules.add(new String[]{"factor","NULL"});
		rules.add(new String[]{"factor","LPAREN","expr","RPAREN"});
		rules.add(new String[]{"factor","AMP","lvalue"});
		rules.add(new String[]{"factor","STAR","factor"});
		rules.add(new String[]{"factor","NEW","INT","LBRACK","expr","RBRACK"});
		rules.add(new String[]{"lvalue","ID"});
		rules.add(new String[]{"lvalue","STAR","factor"});
		rules.add(new String[]{"lvalue","LPAREN","lvalue","RPAREN"});

	}
	
	public static void fillNonterms(){
		nonterms.add("AMP");
		nonterms.add("BECOMES");
		nonterms.add("BOF");
		nonterms.add("COMMA");
		nonterms.add("DELETE");
		nonterms.add("ELSE");
		nonterms.add("EOF");
		nonterms.add("EQ");
		nonterms.add("GE");
		nonterms.add("GT");
		nonterms.add("ID");
		nonterms.add("IF");
		nonterms.add("INT");
		nonterms.add("LBRACE");
		nonterms.add("LBRACK");
		nonterms.add("LE");
		nonterms.add("LPAREN");
		nonterms.add("LT");
		nonterms.add("MINUS");
		nonterms.add("NE");
		nonterms.add("NEW");
		nonterms.add("NULL");
		nonterms.add("NUM");
		nonterms.add("PCT");
		nonterms.add("PLUS");
		nonterms.add("PRINTLN");
		nonterms.add("RBRACE");
		nonterms.add("RBRACK");
		nonterms.add("RETURN");
		nonterms.add("RPAREN");
		nonterms.add("SEMI");
		nonterms.add("SLASH");
		nonterms.add("STAR");
		nonterms.add("WAIN");
		nonterms.add("WHILE");
		nonterms.add("S");
		nonterms.add("dcl");
		nonterms.add("dcls");
		nonterms.add("expr");
		nonterms.add("factor");
		nonterms.add("lvalue");
		nonterms.add("procedure");
		nonterms.add("statement");
		nonterms.add("statements");
		nonterms.add("term");
		nonterms.add("test");
		nonterms.add("type");
	}
	
	
	public static boolean testSeq(Scanner in){
		int state = 99;
		int counter = 0;
		while(in.hasNext()){
			//read in the next symbol
			String symbol = in.next();
			String actual = in.next();
			state = process(state, symbol);
			if(state==-1){
				System.err.println("ERROR at "+(counter+1));
				return false;
			}
			steps.add(new String[]{symbol, actual});
			counter++;
		}
		if(checkIdentical()){
			return true;
		}else{
			System.err.println("ERROR at "+counter);
			return false;
		}
	}
	
	public static String[] searchForTransition(int state, String symbol){
		for(int i=0;i<trans.size();i++){
			String[] temp = trans.get(i);
			if(temp[0].equals(Integer.toString(state)) && temp[1].equals(symbol)){
				return temp;
			}
		}
		return null;
	}
	
	public static int process(int state, String symbol){
		while (true) {
			String[] transition = searchForTransition(state, symbol);
			if (transition != null) {
				if (transition[2].equals("shift")) {
					symbols.push(symbol);
					states.push(Integer.parseInt(transition[3]));
					return Integer.parseInt(transition[3]);
				} else if (transition[2].equals("reduce")) {
					state = reduce(Integer.parseInt(transition[3])-1);
					
				}
			}else{
				return -1;
			}
		}
	}
	
	public static int reduce(int ruleNum){
		String[] rule = rules.get(ruleNum);
		for(int i=1;i<rule.length;i++){
			symbols.pop();
			states.pop();
		}
		steps.add(rule);
		return process(states.peek(), rule[0]);
	}
	
	public static boolean checkIdentical(){
		for(int i=0;i<rules.size();i++){
			String[] temp = rules.get(i);
			if(temp[0].equals(start)){
				if(temp.length-1 != symbols.size()){
					return false;
				}
				for(int j=temp.length-1;j>0;j--){
					if(!temp[j].equals(symbols.pop())){
						return false;
					}
				}
				steps.add(temp);
				return true;
			}
		}
		assert(false);//you should not be here
		return false;
	}
	
	public void traverse(Tree t, int d) {
        //for(int i = 0; i < d; i++) System.out.print(" ");
        System.out.println(t.rule); // print root
        for(Tree c : t.children) {  // print all subtrees
            traverse(c, d+1);
        }
    }
	
	public Tree lrdo() {
        Stack<Tree> stack = new Stack<Tree>();
        String l; // lhs symbol
        do {
            String f = join(steps.get(rIndex));
            rIndex++;
            List<String> r = new ArrayList<String>(); // rhs symbols

            Scanner sc = new Scanner(f);
            l = sc.next(); // lhs symbol
            while(sc.hasNext()) {
                String s = sc.next();
                if(nonterms.contains(s)){
                	if(l.equals("ID") || l.equals("NULL")){
                		//do nothing
                	}else{
                		r.add(s); // only non-terminals
                	}
                	
                }
            }
            sc.close();
            popper(stack, r, f); // reduce rule
        } while(!start.equals(l));
        return stack.peek();
    }
	
	public void popper(Stack<Tree> stack, List<String> rhs, String rule) {
        Tree n = new Tree();
        n.rule = rule;
        for(String s : rhs) {
            n.children.addFirst(stack.pop());
        }
        stack.push(n);
    }
	
	public static String join(String[] a){
		String r="";
		int i=0;
		for(i=0;i<a.length-1;i++){
			r += a[i]+" ";
		}
		r+=a[i];
		return  r;
	}
	
}
