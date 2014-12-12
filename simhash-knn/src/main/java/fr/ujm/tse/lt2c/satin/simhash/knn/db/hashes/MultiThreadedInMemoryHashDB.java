/**
 *
 */
package fr.ujm.tse.lt2c.satin.simhash.knn.db.hashes;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import fr.ujm.tse.lt2c.satin.simhash.knn.datastructure.LongDistanceDocumentIterator;
import fr.ujm.tse.lt2c.satin.simhash.knn.datastructure.storage.HashStore;
import fr.ujm.tse.lt2c.satin.simhash.knn.datastructure.topk.TopK;
import fr.ujm.tse.lt2c.satin.simhash.knn.datastructure.topk.TopKI;
import fr.ujm.tse.lt2c.satin.simhash.knn.db.freshness.FreshnessMode;

/**
 * Same principle as {@link SimpleInMemoryHashDB}, however top-k request are
 * multithreaded for sufficiently large <code>n</code>.
 *
 * Top-k results are processed in a map-reduce manner.
 *
 *
 * @author Julien
 *
 */
public class MultiThreadedInMemoryHashDB extends SimpleInMemoryHashDB {
	/**
	 * Over this value, top-k request are map-reduced
	 */
	private int multithread_threshold = 150_000;
	/**
	 * A thread is worth spawning for at least this value;
	 */
	private int min_chunk_size = 75_000;

	/**
	 * @param multithread_threshold
	 *            the multithread_threshold to set
	 */
	public final void setMultithread_threshold(final int multithread_threshold) {
		this.multithread_threshold = multithread_threshold;
	}

	/**
	 * @param min_chunk_size
	 *            the min_chunk_size to set
	 */
	public final void setMin_chunk_size(final int min_chunk_size) {
		this.min_chunk_size = min_chunk_size;
	}

	/**
	 * @return the multithread_threshold
	 */
	public final int getMultithread_threshold() {
		return multithread_threshold;
	}

	/**
	 * @return the min_chunk_size
	 */
	public final int getMin_chunk_size() {
		return min_chunk_size;
	}

	public MultiThreadedInMemoryHashDB(final int hashsize) {
		super(hashsize);
	}

	public MultiThreadedInMemoryHashDB(final int hashsize, final HashStore store) {
		super(hashsize, store);

	}

	public MultiThreadedInMemoryHashDB(final int hashsize,
			final HashStore store, final TopK topk) {
		super(hashsize, store, topk);
	}

	public MultiThreadedInMemoryHashDB(final int hashsize,
			final HashStore store, final TopK topk, final FreshnessMode mode) {
		super(hashsize, store, topk, mode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.ujm.tse.lt2c.satin.simhash.knn.db.hashes.HashDBI#kNN(long[], int)
	 */
	@Override
	public String[] kNNSorted(final long[] query, final int k) {
		if (storage.size() > multithread_threshold) {
			return multithreadedKnnSorted(query, k);
		}
		return super.kNNSorted(query, k);
	}

	/**
	 * @param query
	 * @param k
	 * @return
	 */
	private String[] multithreadedKnnSorted(final long[] query, final int k) {
		return multiThreadedKnn(query, k, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.ujm.tse.lt2c.satin.simhash.knn.db.hashes.HashDBI#kNNUnSorted(long[],
	 * int)
	 */
	@Override
	public String[] kNNUnSorted(final long[] query, final int k) {
		if (storage.size() > multithread_threshold) {
			return multithreadedKnnUnSorted(query, k);
		}
		return super.kNNUnSorted(query, k);
	}

	/**
	 * @param query
	 * @param k
	 * @return
	 */
	private String[] multithreadedKnnUnSorted(final long[] query, final int k) {
		return multiThreadedKnn(query, k, false);
	}

	private String[] multiThreadedKnn(final long[] query, final int k,
			final boolean sorted) {
		final int cpus = Runtime.getRuntime().availableProcessors();
		// Split arrays
		final LongDistanceDocumentIterator[] iterators = spliterator(cpus,
				query);
		// Map Phase
		final ExecutorService service = Executors.newFixedThreadPool(cpus);
		final List<Future<Worker>> tasks = new ArrayList<Future<Worker>>();
		for (final LongDistanceDocumentIterator iterator : iterators) {
			final Worker e = new Worker(iterator, k, sorted,
					iterator.getStartIndex());
			final Future<Worker> future = service.submit(e, e);
			tasks.add(future);
		}
		service.shutdown();
		try {
			service.awaitTermination(10, TimeUnit.SECONDS);
		} catch (final InterruptedException e1) {
			throw new RuntimeException(e1);
		}
		// Reduce
		final long[][] results = new long[iterators.length][];
		int i = 0;
		try {
			for (final Future<Worker> future : tasks) {
				final Worker worker = future.get();
				results[i] = worker.getTopk();
				i++;
			}

			final TopKI joiner = TopK.newTopK(topk, null, k, mode);
			final long[] result = joiner.joinTopKs(results);

			return convertDistAndDocsToDocIDS(result);
		} catch (final Exception e) {

		}
		return null;
	}

	/**
	 * Split query into several iterators
	 *
	 * @param cpus
	 * @return
	 */
	protected LongDistanceDocumentIterator[] spliterator(final int cpus,
			final long[] query) {
		// Compute the size
		final int splits = (int) Math.ceil(storage.size() / min_chunk_size);
		// n-1 storage of this size, last takes the rest
		final int splitsize = storage.size() / splits;
		final LongDistanceDocumentIterator[] iterators = new LongDistanceDocumentIterator[splits];
		for (int i = 0; i < splits; i++) {

			final int start = i * splitsize;
			final int end = (i + 1) * splitsize;

			iterators[i] = storage.getDistanceIterator(query, start, end);
		}
		// Last one takes the rest

		iterators[splits - 1] = storage.getDistanceIterator(query, (splits - 1)
				* splitsize, storage.size() + 1);
		return iterators;
	}

	/**
	 * Compute a map iteration, get top-k on a subpart of the DB
	 *
	 * @author Julien
	 *
	 */
	class Worker implements Runnable {

		final LongDistanceDocumentIterator iterator;
		long[] localtopk;
		final private int k;
		final boolean sorted;
		/**
		 * Where we start in the table
		 */
		final int startIndex;

		public Worker(final LongDistanceDocumentIterator iterator, final int k,
				final boolean sorted, final int startIndex) {

			this.iterator = iterator;
			this.k = k;
			this.sorted = sorted;
			this.startIndex = startIndex;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			final TopKI topkExec = TopK.newTopK(topk, iterator, k, mode);
			topkExec.setStart(startIndex);
			if (sorted) {
				localtopk = topkExec.getTopKSorted();
			} else {
				localtopk = topkExec.getTopKUnSorted();
			}
		}

		public long[] getTopk() {
			return localtopk;
		}
	}
}
