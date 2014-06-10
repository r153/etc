public class WMUtility {
	
	// インスタンス化の抑制を行う。
	private WMUtility() {}
	
	/** 1バイトの値のrank値 */
	static final int RANK_TABLE[];
	static {
		RANK_TABLE = new int [] {
				0, 1, 1, 2, 1, 2, 2, 3,
				1, 2, 2, 3, 2, 3, 3, 4,
				1, 2, 2, 3, 2, 3, 3, 4,
				2, 3, 3, 4, 3, 4, 4, 5,
				1, 2, 2, 3, 2, 3, 3, 4,
				2, 3, 3, 4, 3, 4, 4, 5,
				2, 3, 3, 4, 3, 4, 4, 5,
				3, 4, 4, 5, 4, 5, 5, 6,
				1, 2, 2, 3, 2, 3, 3, 4,
				2, 3, 3, 4, 3, 4, 4, 5,
				2, 3, 3, 4, 3, 4, 4, 5,
				3, 4, 4, 5, 4, 5, 5, 6,
				2, 3, 3, 4, 3, 4, 4, 5,
				3, 4, 4, 5, 4, 5, 5, 6,
				3, 4, 4, 5, 4, 5, 5, 6,
				4, 5, 5, 6, 5, 6, 6, 7,
				1, 2, 2, 3, 2, 3, 3, 4,
				2, 3, 3, 4, 3, 4, 4, 5,
				2, 3, 3, 4, 3, 4, 4, 5,
				3, 4, 4, 5, 4, 5, 5, 6,
				2, 3, 3, 4, 3, 4, 4, 5,
				3, 4, 4, 5, 4, 5, 5, 6,
				3, 4, 4, 5, 4, 5, 5, 6,
				4, 5, 5, 6, 5, 6, 6, 7,
				2, 3, 3, 4, 3, 4, 4, 5,
				3, 4, 4, 5, 4, 5, 5, 6,
				3, 4, 4, 5, 4, 5, 5, 6,
				4, 5, 5, 6, 5, 6, 6, 7,
				3, 4, 4, 5, 4, 5, 5, 6,
				4, 5, 5, 6, 5, 6, 6, 7,
				4, 5, 5, 6, 5, 6, 6, 7,
				5, 6, 6, 7, 6, 7, 7, 8
		};
	}
	
	/**
	 * 
	 * <p>
	 * 32bitのWORDから1のrank値を計算するメソッドである。
	 * </p>
	 * 
	 * @param pWord 32bitのWORD
	 * @return 32bitのWORD内の1のrank値
	 */
	static int getWordRankFromTable( final int pWord ) {
		// 32bit分のrank値を計算する。
		return RANK_TABLE[ (pWord >> 24) & 0xff ]
				+ RANK_TABLE[ (pWord >> 16) & 0xff ]
				+ RANK_TABLE[ (pWord >> 8) & 0xff ]
				+ RANK_TABLE[ pWord & 0xff ];
	}
	
	/**
	 * 
	 * <p>
	 * MSBを求めるメソッドである。
	 * </p>
	 * 
	 * @param pValue 整数値
	 * @return 指定された整数値のMSB
	 */
	static int getMSB( final int pValue ) {
		int rank = 0;
		int value = pValue;
		if ( 0 < pValue ) {
			value |= (value >> 1);
			value |= (value >> 2);
			value |= (value >> 4);
			value |= (value >> 16);
			value |= (value >> 32);
			
			// MSB以下は、すべて1が立っているので、その数を数える。
			rank = getWordRankFromTable(value);
		}
		return rank;
	}

}