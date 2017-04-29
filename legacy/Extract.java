import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;


public class Extract {
	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		System.setIn(new BufferedInputStream(new FileInputStream("testCases/Code")));
		System.setOut(new PrintStream("testCases/checks"));
		ArrayList<String> bails = new ArrayList<String>();
		Scanner in = new Scanner(System.in);
		while(in.hasNextLine()){
			String temp = in.nextLine();
			if(temp.contains("bail") && !temp.contains("void bail")){
				bails.add(temp);
			}
		}
		in.close();
		for(int i=0;i<bails.size();i++){
			System.out.println(bails.get(i).trim());
		}
	}
}
