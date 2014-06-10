import mouse.runtime.SourceString;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import mouse.runtime.SourceFile;


class App{
	public static void main(String argv[]) throws Exception{

		myParser parser = new myParser();			// Instantiate Parser+Semantics
		SourceFile src = new SourceFile(argv[0]);	// Wrap the file
		if (!src.created()) return;					// Return if no file
		boolean ok = parser.parse(src);				// Apply parser to it
		System.out.println(ok? "ok":"failed");		// Tell if succeeded

		if(ok){

			mySemantics sem = parser.semantics();	// Access Semantics
			if(sem.tree!=null){
				System.out.println(sem.tree.show());
			}			

		}

//		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
//		while (true){
//			System.out.print("> ");						  // Print prompt
//			String line = in.readLine();					// Read line
//			if (line.length()==0) return;				  // Quit on empty line
//			SourceString src = new SourceString(line); // Wrap up the line
//			boolean ok = parser.parse(src);				// Apply Parser to it
//			if (ok){												// If succeeded:
//				//mySemantics sem = parser.semantics();	 // Access Semantics
//				//System.out.println(sem.tree.show());	  // Get the tree and show it
//			}
//		}
	}
}
