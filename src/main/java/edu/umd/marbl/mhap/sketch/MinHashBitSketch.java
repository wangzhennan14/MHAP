package edu.umd.marbl.mhap.sketch;

public final class MinHashBitSketch extends AbstractBitSketch<MinHashBitSketch>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -44448450811302477L;

	private final static long[] getAsBits(String seq, int nGramSize, int numWords)
	{
		int[] minHashes = MinHashSketch.computeNgramMinHashesWeightedIntSuper(seq, nGramSize, numWords*64, null, null, true);
		
		//now convert them to bits
		long[] bits = new long[numWords];
		
		//take only the last bit
		long mask = 0b1;
		
		int bitCount = 0;
		int wordCount = 0;
		for (int word = 0; word<numWords; word++)
		{
			long currWord = 0b0;
			
			for (int bit=0; bit<64; bit++)
			{
				currWord = (currWord << 1) | (minHashes[bitCount] & mask);				
						
				bitCount++;
			}
			
			bits[wordCount] = currWord;			
			wordCount++;
		}

		return bits;
	}
	
	public MinHashBitSketch(String seq, int kmerSize, int numWords)
	{
		super(getAsBits(seq, kmerSize, numWords));
	}
	
	public final double jaccard(final MinHashBitSketch sh)
	{
		double jaccard = (similarity(sh) - 0.5) * 2.0;

		return Math.max(0.0, jaccard);
	}
}