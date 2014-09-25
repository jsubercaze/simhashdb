///**
// *
// */
//package fr.ujm.tse.lt2c.satin.simhash.examples.news;
//
//import java.io.File;
//import java.nio.charset.Charset;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.commons.io.FileUtils;
//
//import com.google.common.hash.HashFunction;
//import com.google.common.hash.Hashing;
//
//import fr.ujm.tse.lt2c.satin.simhash.knn.ConversionMethod;
//import fr.ujm.tse.lt2c.satin.simhash.knn.InMemorySimHashDB;
//import fr.ujm.tse.lt2c.satin.simhash.text.transform.Conversion;
//import fr.ujm.tse.lt2c.satin.simhash.text.transform.preprocessing.counting.CoOccurrenceThreshold;
//import fr.ujm.tse.lt2c.satin.simhash.text.transform.preprocessing.counting.SimpleCounting;
//import fr.ujm.tse.lt2c.satin.simhash.text.transform.preprocessing.nlp.StandardFrenchStanfordNLPPipeline;
//
///**
// * <h2>Do not forget to set the location of the Stanford Tagger</h2>
// *
// *
// * We compute distances between news articles from 5-7 July 2014 where 3 events
// * overflood the news : sarkozy and the justice, soccer world cup, BNP fees for
// * emnbargo violation. </p>
// * <p>
// * Five documents for each topic. Compute and display P@K for each database
// * </p>
// *
// *
// * @author Julien
// *
// */
//public class FrenchNewsDistance {
//	/**
//	 * Where is the french posmodel ? xxxxx/french.tagger
//	 */
//	private static final String posModel = "c:/libs/posmodels/french.tagger";
//	/**
//	 * To compute P@K
//	 */
//	private static final int K = 4;
//	/**
//	 * Number of relevant documents for each topic (must be balanced)
//	 */
//	private static final int NB_RELEVANT = 4;
//	/**
//	 * List of databases
//	 */
//	private static final List<InMemorySimHashDB> dbs = new ArrayList<>();
//
//	private static final List<ConversionMethod> methods = new ArrayList<ConversionMethod>();
//	private static final List<HashFunction> functions = new ArrayList<HashFunction>();
//
//	public static void main(final String[] args) throws Exception {
//
//		methods.add(new Conversion(new StandardFrenchStanfordNLPPipeline(
//				posModel), new SimpleCounting()));
//		methods.add(new Conversion(new StandardFrenchStanfordNLPPipeline(
//				posModel), new CoOccurrenceThreshold(2)));
//
//		functions.add(Hashing.murmur3_128());
//		functions.add(Hashing.sha256());
//		functions.add(Hashing.sha512());
//
//		// Create the dbs
//		for (final HashFunction func : functions) {
//			for (final ConversionMethod method : methods) {
//				final InMemorySimHashDB db = new InMemorySimHashDB(func);
//				db.setDescription(func.getClass().getSimpleName() + "_"
//						+ func.bits() + "bits & " + method.toString());
//				dbs.add(db);
//
//			}
//		}
//
//		final File dir = new File("docs/news");
//		final File[] files = dir.listFiles();
//		final List<File> news = new ArrayList<>();
//		System.out.println("Adding docs to the " + dbs.size() + " DBs");
//		for (final File file : files) {
//			System.out.println(file.getAbsolutePath());
//			if (file.getAbsolutePath().endsWith(".txt")) {
//				news.add(file);
//				final String content = FileUtils.readFileToString(file,
//						Charset.forName("ISO-8859-1"));
//				int i = 0;
//				for (final ConversionMethod method : methods) {
//					final Map<String, Double> converted = method
//							.convert(content);
//					for (final HashFunction func : functions) {
//
//						dbs.get(i).
//						i++;
//					}
//				}
//
//			}
//		}
//		// Prepare for stats
//		final Precision precision = new Precision(dbs.get(0).size(), dbs.size());
//		System.out.println("Studying variation for " + news.size()
//				+ " documents");
//		for (int i = 0; i < news.size(); i++) {
//			System.out
//			.println("------------------------------------------------------------------------------------");
//			int j = 0;
//			for (final InMemorySimHashDB db : dbs) {
//				System.out.println();
//				System.out
//				.println("---------------------------------------------------");
//				System.out.println(K + "-NN for document "
//						+ news.get(i).getName());
//				System.out.println("DB-" + db.getDescription());
//				final int[] ids = db.kNearestNeighbors(i, K);
//				addStat(news.get(i).getName(), precision, ids, news, j, i);
//				for (int k = 0; k < ids.length; k++) {
//					System.out
//					.println(k + 1 + "-" + news.get(ids[k]).getName());
//				}
//
//				j++;
//			}
//
//		}
//		// Display precision for each DB
//		System.out.println("P@" + K + " for " + dbs.size() + " databases with "
//				+ dbs.get(0).size() + " documents and maximum " + NB_RELEVANT
//				+ " documents");
//
//		final double[] precs = precision.getAveragePrecision();
//		System.out.println("------Average precision per DB-------------------");
//		for (int i = 0; i < dbs.size(); i++) {
//			System.out.println(dbs.get(i).getDescription());
//			System.out.println(dbs.get(i).viewDB());
//			System.out.println(precs[i]);
//			dbs.get(i).saveHashes(new File("c:/libs/gros" + i + ".db"));
//		}
//
//	}
//
//	private static void addStat(final String docname,
//			final Precision precision, final int[] ids, final List<File> docs,
//			final int db, final int doc) {
//		final String threechar = docname.substring(0, 3);
//		int retrieved = 0;
//		for (final int id : ids) {
//			if (docs.get(id).getName().substring(0, 3).equals(threechar)) {
//				retrieved++;
//			}
//		}
//
//		precision.addScore(doc, db, Math.min(K, NB_RELEVANT), retrieved);
//	}
// }
