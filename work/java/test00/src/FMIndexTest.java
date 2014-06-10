public class FMIndexTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try{

		String text = "mississippi\0";	
		//String text = "ababac\0";
		//String text = "abracadabra_abracadabra_abracadabra\0";
		String keyword = "si";
		
		FMIndex fmi = new FMIndex( text );
		fmi.build();
		Integer first=new Integer(0);
		Integer last=new Integer(0);

		int count=fmi.getRows(keyword,first,last);
		
		System.out.println( "Hit Count : " + count );
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

}