import java.util.Arrays;
import java.util.BitSet;

public class SuffixArray {
	
	/**
	 * Suffix Arrayの対象となるテキストである。
	 */
	protected String mText;
	protected int[] mSuffixArray;
	
	/**
	 * 
	 * <p>
	 * コンストラクタ
	 * </p>
	 * 
	 * @param pText テキスト
	 */
	public SuffixArray( final String pText ) {
		this.mText = pText;
	}
	
	public int at( final int pIndex ) {
		return this.mSuffixArray[ pIndex ];
	}
	
	public int getSize() {
		return this.mSuffixArray.length;
	}
	
	public int [] getSuffixArray() {
		return this.mSuffixArray;
	}
	
	public void print() {
		System.out.println( "Suffix Array:" );
		System.out.println( "index\t\t\tSA[i]\t\t\tT[SA[i]]" );
		for ( int i = 0; i < this.mSuffixArray.length; i++ ) {
			System.out.print( String.format("%d\t\t\t\t%d", i, this.mSuffixArray[i] ) );
			System.out.print( "\t\t\t" );
			System.out.println( Character.toChars(this.mText.codePointAt(this.mSuffixArray[i])) );
		}
	}
	
	private int getMaxCodePoint(
			final String pText,
			final int pTextCodePointCount
			) {
		
		// 最後の位置は最小のコードポイントが格納されているので、無視する。
		int maxCodePoint = pText.codePointAt(pTextCodePointCount - 2);
		
		for ( int i = pTextCodePointCount - 3; i >= 0; i-- ) {
			// 最大のコードポイントを決定する。
			int codePoint = this.mText.codePointAt(i);
			if ( maxCodePoint < codePoint ) {
				maxCodePoint = codePoint;
			}
		}
		
		return maxCodePoint;
		
	}
	
	private void setType(
			final String pText,
			final BitSet pType,
			final int pTextCodePointCount
			) {
		
		// Typeを決定する。
		// 終端は、Type-S
		pType.set(pTextCodePointCount - 1, true);
		// 次は、Type-L
		pType.set(pTextCodePointCount - 2, false);
		
		// 残りのTypeを決定する。
		for ( int i = pTextCodePointCount - 3; i >= 0; i-- ) {
			if ( pText.codePointAt(i) < pText.codePointAt(i + 1)
					|| (pText.codePointAt(i) == pText.codePointAt(i + 1) && true == pType.get(i + 1) ) ) {
				// Type-S
				pType.set(i, true);
			} else {
				// Type-L
				pType.set(i, false);
			}
		}
	}
	
	private void setBucket(
			final String pText,
			final int [] pBucket,
			final int pTextCodePointCount,
			final boolean pEnd
			) {
		
		// バケツを初期化する。
		Arrays.fill(pBucket, 0);
		
		// 各文字のバケツを初期化する。
		for ( int i = 0; i < pTextCodePointCount; i++ ) {
			pBucket[ pText.codePointAt( i ) ]++;
		}
		
		// 2種類のバケツを取得する。
		int sum = 0;
		for ( int i = 0; i < pBucket.length; i++ ) {
			sum += pBucket[i];
			
			if ( pEnd ) {
				// 指定された文字より小さい文字の頻度と指定された文字の頻度の合計を取得する。
				// Type-Sで利用する。
				pBucket[i] = sum;
			} else {
				// 指定された文字より小さい文字の頻度を取得する。
				// Type-Lで利用する。
				pBucket[i] = sum - pBucket[i];
			}
		}
		
	}
	
	private boolean isLMS(
			final BitSet pType,
			final int pIndex
			) {
		if ( 0 < pIndex &&
				pType.get(pIndex) &&
				!pType.get(pIndex - 1) ) {
			return true;
		}
		return false;
	}
	
	private void induceSuffixArrayTypeL(
			final BitSet pType,
			final int [] pSuffixArray,
			final String pText,
			final int [] pBucket,
			final int pTextCodePointCount,
			final boolean pEnd
			) {
		
		// バケツを取得する。
		setBucket(pText, pBucket, pTextCodePointCount, pEnd);
		
		// 先頭からType-LのSuffix Arrayの位置を決定する。
		for ( int i = 0; i < pTextCodePointCount; i++ ) {
			
			// ソートされているType-S*の直前のType-Lを取得する。
			// すでに決定したType-Lの直前のType-Lも取得して、位置を決定する。
			int j = pSuffixArray[i] - 1;
			
			if ( 0 <= j && !pType.get(j) ) {
				// まだ決定していないType-Lの文字の中で最小のSuffixなので、バケツの先頭に追加する。
				pSuffixArray[ pBucket[ pText.codePointAt(j) ]++ ] = j;
			}
		}
		
	}
	
	private void induceSuffixArrayTypeS(
			final BitSet pType,
			final int [] pSuffixArray,
			final String pText,
			final int [] pBucket,
			final int pTextCodePointCount,
			final boolean pEnd
			) {
		
		// バケツを取得する。
		setBucket(pText, pBucket, pTextCodePointCount, pEnd);
		
		// 最後からType-SのSuffix Arrayの位置を決定する。
		for ( int i = pTextCodePointCount - 1; i >= 0; i-- ) {
			
			// ソートされているType-Lの位置からType-Sの位置を決定する。
			// すでに決定したType-Sの直前のType-Sも取得して、位置を決定する。
			int j = pSuffixArray[i] - 1;
			
			if ( 0 <= j && pType.get(j) ) {
				// まだ決定していないType-S(Type-S*も含む)の文字の中で最大のSuffixなので、バケツの最後に追加する。
				pSuffixArray[ --pBucket[ pText.codePointAt(j) ] ] = j;
			}
		}
		
	}
	
	private void sort(
			final String pText,
			final int[] pSuffixArray,
			final int pTextCodePointCount,
			final int pMaxCodePoint
			) {
		
		// Typeを格納する領域を取得する。
		BitSet type = new BitSet( pTextCodePointCount );
		
		// Typeを決定する。
		setType( pText, type, pTextCodePointCount );
		
		// バケツを取得する。
		int [] bucket = new int [ pMaxCodePoint + 1 ];
		
		// バケツを初期化する。
		setBucket(pText, bucket, pTextCodePointCount, true);
		
		// Suffix Arrayを初期化する。
		Arrays.fill(pSuffixArray, -1);
		
		// Type-S*(LMS)のソートは、1文字目だけで行う。
		// Type-S*のSuffixとしてのソートはこの段階では行わないので任意で構わない。
		// つまり、この段階では、Type-S*の順序が決定できないが、1文字目の位置に格納しておくと、それ以外のType-SとType-Lの位置を決定できる。
		for ( int i = 1; i < pTextCodePointCount; i++ ) {
			if ( isLMS(type, i) ) {
				// Type-S*は、Type-Sなので、後ろから格納する。
				pSuffixArray[ --bucket[ pText.codePointAt(i) ] ] = i;
			}
		}
		
		// Type-S*を使って、Type-Lのソートを行う。
		induceSuffixArrayTypeL( type, pSuffixArray, pText, bucket, pTextCodePointCount, false );
		
		// Type-Lを使って、Type-S(Type-S*)のソートを行う。
		induceSuffixArrayTypeS( type, pSuffixArray, pText, bucket, pTextCodePointCount, true );
		
		// Type-S*の部分文字列単位でしか扱っていないので、この時点ではSuffixが決定できない場合がある。
		// Type-S*の部分文字列の順序を決定するために再帰的に適用する。
		
		// Type-S*の数を数える。
		int n1 = 0;
		for ( int i = 0; i < pTextCodePointCount; i++ ) {
			if ( isLMS( type, pSuffixArray[i] ) ) {
				pSuffixArray[n1] = pSuffixArray[i];
				n1++;
			}
		}
		
		// Type-S*でない部分は、-1で初期化する。
		Arrays.fill(pSuffixArray, n1, pTextCodePointCount, -1);
		
		int name = 0;
		int prev = -1;
		// ユニークなType-S*部分文字列を決定する。
		// Type-Sのソートは終わっているので、同じ部分文字列は並んでいる。
		for ( int i = 0; i < n1; i++ ) {
			
			// ソート済みのType-S*部分文字列の位置を取得する。
			int pos = pSuffixArray[i];
			
			// Type-S*部分文字列が同一か？
			boolean diff = false;
			for ( int d = 0; d < pTextCodePointCount; d++ ) {
				
				if ( -1 == prev ||
						pText.codePointAt(pos + d) != pText.codePointAt(prev + d) ||
						type.get(pos + d) != type.get(prev + d) ) {
					diff = true;
					break;
				} else if ( 0 < d &&
						( isLMS(type, pos + d) || isLMS(type, prev + d) ) ) {
					// Type-S*の場合は、ユニークでないType-S*部分文字列だった。
					break;
				}
			}
			
			// Type-S*部分文字列がユニークか？
			if ( diff ) {
				name++;
				prev = pos;
			}
			
			if ( 0 == (pos % 2) ) {
				pos = pos / 2;
			} else {
				pos = (pos - 1) / 2;
			}
			
			// Type-S*部分文字列の出現位置を保ったまま、ユニークな値を割り当てる。
			pSuffixArray[ n1 + pos ] = name - 1;
		}
		
		// 後方の位置に一旦、集める。
		int lastIndex = pTextCodePointCount - 1;
		for ( int i = pTextCodePointCount - 1; i >= n1; i-- ) {
			if ( 0 <= pSuffixArray[i] ) {
				pSuffixArray[ lastIndex-- ] = pSuffixArray[i];
			}
		}
		
		// 元のテキストの出現順序を維持しながら、Type-S*部分文字列にユニークな値を割り当てた新しいテキストを生成する。
		StringBuffer newTextBuffer = new StringBuffer();
		for ( int i = lastIndex + 1; i < pTextCodePointCount; i++ ) {
			newTextBuffer.appendCodePoint( pSuffixArray[i] );
		}
		
		// 再帰的にソートする。
		// Type-S*部分文字列がすべてユニークか？
		if ( name < n1 ) {
			// 現時点でType-S*部分文字列の順序が決定できていないので、再帰的にソートを実行して決定する。
			sort( newTextBuffer.toString(), pSuffixArray, n1, name - 1 );
		} else {
			// ユニークな場合は、これ以上ソートする必要がなく、すべての順序が決定しているので再帰実行を終了する。
			for ( int i = 0; i < n1; i++ ) {
				pSuffixArray[ newTextBuffer.toString().codePointAt(i) ] = i;
			}
		}
		
		// バケツを初期化する。
		setBucket(pText, bucket, pTextCodePointCount, true);
		
		int [] LMSIndex = new int [n1];
		int index = 0;
		for ( int i = 1; i < pTextCodePointCount; i++ ) {
			if ( isLMS(type, i) ) {
				LMSIndex[ index ] = i;
				index++;
			}
		}
		
		for ( int i = 0; i < n1; i++ ) {
			pSuffixArray[i] = LMSIndex[ pSuffixArray[i] ];
		}
		
		Arrays.fill(pSuffixArray, n1, pTextCodePointCount, -1);
		
		// ソートされたType-S*をSuffix Arrayに配置する。
		for ( int i = n1 - 1; i >= 0; i-- ) {
			int j = pSuffixArray[i];
			pSuffixArray[i] = -1;
			pSuffixArray[ --bucket[ pText.codePointAt(j) ] ] = j;
		}
		
		// Type-S*を使って、Type-Lのソートを行う。
		induceSuffixArrayTypeL( type, pSuffixArray, pText, bucket, pTextCodePointCount, false );
		
		// Type-Lを使って、Type-S(Type-S*)のソートを行う。
		induceSuffixArrayTypeS( type, pSuffixArray, pText, bucket, pTextCodePointCount, true );
		
	}
	
	/**
	 * 
	 * <p>
	 * コードポイント単位でソートを実行して、Suffix Arrayを構築する。
	 * </p>
	 * 
	 */
	public void build() {
		
		int textCodePointCount = this.mText.codePointCount(0, this.mText.length());
		
		// 最大文字のコードポイントを取得する。
		mMaxCodePoint = getMaxCodePoint( this.mText, textCodePointCount );
		
		this.mSuffixArray = new int [ textCodePointCount ];
		
		// Suffix Arrayをソートする。
		sort( this.mText, this.mSuffixArray, textCodePointCount, mMaxCodePoint );
		
	}

	protected int mMaxCodePoint;

	public void main(){
		
	}
}
