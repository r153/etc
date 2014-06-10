import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jp.rn.project.sbv.BuildType;
import jp.rn.project.sbv.SuccinctBitVector;

public class WaveletMatrix {
	
	private String mStr;
	
	private int mStrCodePointCount;
	
	private int mBVNum;
	
	/**
	 * 
	 * <p>
	 * ウェーブレット行列
	 * </p>
	 * 
	 */
	private List<SuccinctBitVector> mWaveletMatrix;
	
	/**
	 * 
	 * <p>
	 * コンストラクタ
	 * </p>
	 * 
	 */
	public WaveletMatrix( final String pStr ) {
		this.mStr = pStr;
		this.mStrCodePointCount = this.mStr.codePointCount(0, this.mStr.length());
		
		// 文字の最大ビット数を調べる。
		this.mBVNum = 0;
		for ( int i = 0; i < this.mStrCodePointCount; i++ ) {
			int codePoint = this.mStr.codePointAt(i);
			int msb = WMUtility.getMSB(codePoint);
			
			if ( this.mBVNum < msb ) {
				this.mBVNum = msb;
			}
		}
		
		this.mWaveletMatrix = new ArrayList<SuccinctBitVector>(this.mBVNum);
		for ( int i = 0; i < this.mBVNum; i++ ) {
			this.mWaveletMatrix.add(i, new SuccinctBitVector( this.mStrCodePointCount ));
		}
	}
	
	private String buildBitVector (
			final int pBitPos,
			final String pCurStr
			) throws Exception {
		
		assert( 0 < pBitPos );
		
		SuccinctBitVector prevSBV = this.mWaveletMatrix.get(pBitPos - 1);
		SuccinctBitVector curSBV = this.mWaveletMatrix.get(pBitPos);
		
		long boundaryIndex = prevSBV.getRank(this.mStrCodePointCount, 0);
		
		long leftIndex = 0;
		long rightIndex = 0;
		StringBuffer leftNextStr = new StringBuffer();
		StringBuffer rightNextStr = new StringBuffer();
		
		for ( int i = 0; i < this.mStrCodePointCount; i++ ) {
			
			// 直前のビットベクトルでのビットを調べる。
			int prevBit = prevSBV.getBit( i );
			
			int codePoint = pCurStr.codePointAt(i);
			int bit = (codePoint >> pBitPos) & 1;
			
			if ( 0 == prevBit ) {
				// 左側に分類する。
				curSBV.setBit(leftIndex, bit);
				leftNextStr.appendCodePoint(codePoint);
				leftIndex++;
			} else {
				curSBV.setBit(rightIndex + boundaryIndex, bit);
				rightNextStr.appendCodePoint(codePoint);
				rightIndex++;
			}
		}
		
		assert( (long) this.mStrCodePointCount == (leftIndex + rightIndex) );
		
		
		curSBV.build( BuildType.SIMPLE );
		
		leftNextStr.append( rightNextStr );
		
		return leftNextStr.toString();
		
	}
	
	public void build() throws Exception {
		
		// 1ビット目を分類する。
		SuccinctBitVector sbv = this.mWaveletMatrix.get(0);
		for ( int i = 0; i < this.mStrCodePointCount; i++ ) {
			int codePoint = this.mStr.codePointAt( i );
			int bit = codePoint & 1;
			sbv.setBit(i, bit);
		}
		sbv.build( BuildType.SIMPLE );
		
		// 2ビット目以降は前回のSBVを使って分類する。
		String nextStr = this.mStr;
		for ( int bitPos = 1; bitPos < this.mBVNum; bitPos++ ) {
			nextStr = buildBitVector(bitPos, nextStr);
		}
		
		return;
	}
	
	public int access( final int pPos ) {
		
		int codePoint = 0;
		
		int sbvPos = pPos;
		for ( int bitPos = 0; bitPos < this.mBVNum; bitPos++ ) {
			SuccinctBitVector sbv = this.mWaveletMatrix.get(bitPos);
			
			int bit = sbv.getBit( sbvPos );
			codePoint |= (bit << bitPos);
			
			sbvPos = (int) sbv.getRank(sbvPos, bit);
			if ( 0 != bit ) {
				// 次回のビット位置を計算する。
				int boundaryIndex = (int) sbv.getRank(this.mStrCodePointCount, 0);
				sbvPos += boundaryIndex;
			}
			
		}
		
		return codePoint;
		
	}
	public int select(  ) {
		return 0;
	}	
	public int getRank( final int pPos , final int pCodePoint) {
		
		int rank = 0;
		
		int start = 0;
		int end = pPos;
		for ( int bitPos = 0; bitPos < this.mBVNum; bitPos++ ) {
			SuccinctBitVector sbv = this.mWaveletMatrix.get(bitPos);
			
			int bit = (pCodePoint >> bitPos) & 1;
			
			start = (int) sbv.getRank(start, bit);
			end = (int) sbv.getRank(end, bit);
			
			if ( 0 != bit ) {
				int boundaryIndex = (int) sbv.getRank(this.mStrCodePointCount, 0);
				start += boundaryIndex;
				end += boundaryIndex;
			}
			
			if ( (this.mBVNum - 1) == bitPos ) {
				int rank1 = (int) sbv.getRank(start, bit);
				int rank2 = (int) sbv.getRank(end, bit);
				
				assert( rank1 <= rank2 );
				rank = rank2 - rank1;
			}
			
		}
		
		return rank;
		
	}
	public int getRankLessThan( final int pCodePoint) {

		byte[] ary=mStr.getBytes();
		Arrays.sort(ary);
		byte tmp=-1;
		int cnt=0;
		for(byte b:ary){
			if(b==tmp) ary[cnt]=-1; 
			tmp=b;cnt++;
		}
		Arrays.sort(ary);
		int cdp=pCodePoint;
		int sum=0;
		for(int n=0;n<ary.length;n++){
			if(ary[n]<0) continue;
			if(ary[n]>=cdp) break;
			sum += getRank(mStr.codePointCount(0, mStr.length()), ary[n]);
		}
		return sum;
	}
	
}