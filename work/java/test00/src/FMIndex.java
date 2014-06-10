public class FMIndex {
	
	private String mText;
	private int mTextCodePointCount;
	
	private WaveletMatrix mWM;
	
	public FMIndex( final String pText ) {
		this.mText = pText;
		this.mTextCodePointCount = this.mText.codePointCount(0, this.mText.length());
	}
	
	public void build() throws Exception {
		
		// BWT
		BWT bwt = new BWT( this.mText );
		bwt.build();
		
		String bwtText = bwt.getBWT();
		
		
		// Wavelet Matrix
		this.mWM = new WaveletMatrix( bwtText );
		this.mWM.build();
		
		// テキストは不要である。
		this.mText = null;
		
	}

	/**
	 * 
	 * <p>
	 * キーワードのヒット数を返すメソッドである。
	 * </p>
	 * 
	 * @param pKeyword キーワード
	 * @return ヒット数
	 */
	public int getRows(String key, Integer first,Integer last) throws Exception {

			int i = key.codePointCount(0,key.length())-1;
			first = this.mWM.getRankLessThan(key.codePointAt(i)) + 1;
			last  = this.mWM.getRankLessThan(key.codePointAt(i)+1);

		  while ((first <= last) && i > 0) {
		    i--;
		    int c = this.mWM.getRankLessThan(key.codePointAt(i));
		    first = c + this.mWM.getRank(first - 1,key.codePointAt(i)) + 1;
		    last  = c + this.mWM.getRank(last,key.codePointAt(i));
		  }
		  first--;
		  last--;
		  if (last < first) {
			  return 0;
		  }
		  return last - first + 1;
		
	}

}