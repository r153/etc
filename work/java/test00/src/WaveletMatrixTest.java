import java.util.Arrays;
import java.util.HashSet;

public class WaveletMatrixTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		try{
			//int [] codePointArray = new int [] { 1, 7, 3, 5, 6, 2, 4, 0, 4, 1, 4, 7 };
			//String str = new String( codePointArray, 0, codePointArray.length );
			//String str = "abracadabra";
			//String str = "ababac";
			String str = "c\0bbaaa"; 
			//String str = "ipssm\0pissii"; 
			
			// Wavelet Matrix
			WaveletMatrix wm = new WaveletMatrix( str );
			wm.build();

			// access, rank
			int codePointCount = str.codePointCount(0, str.length());
			for ( int i = 0; i < codePointCount; i++ ) {

				// access
				int codePoint = wm.access(i);
				System.out.println( "access( " + i + " )" + " : " + codePoint + ", " + new String( Character.toChars(codePoint) ) );

				// rank
				int rank = wm.getRank(codePointCount, str.codePointAt(i));
				System.out.println( "rank(" + codePointCount + ", " + new String( Character.toChars(str.codePointAt(i)) ) + ") : " + rank );

				System.out.println( "rankresthan:" + wm.getRankLessThan( str.codePointAt(i)) );
				System.out.println( "xxxxxxxxxxx:" + ( LF(wm,codePointCount, str.codePointAt(i))) ) ;
			}
			String ss="";
			 int ch = "\0".codePointAt(0);
			 int i  = str.indexOf(ch);
			 for(;;) {
				//文字列入れる　アタマに加えてる、ってことか。
				  ss=new String( Character.toChars(ch))+ss;
			    i = LF(wm, i, ch);
			    ch=wm.access(i);
			   if(ch=="\0".codePointAt(0)){
				   break;
			   }
			  }
			 System.out.println(ss);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
	}

	public static int LF(WaveletMatrix wa, int codePointCount, int codepoint) {
		  return wa.getRankLessThan(codepoint) + wa.getRank(codePointCount, codepoint);
	}
	
}