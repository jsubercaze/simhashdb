/**
 * 
 */
package fr.ujm.tse.lt2c.satin.simhash.text.transform.terrier;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.terrier.indexing.Collection;
import org.terrier.indexing.CollectionDocumentList;
import org.terrier.indexing.Document;
import org.terrier.indexing.FileDocument;
import org.terrier.indexing.tokenisation.EnglishTokeniser;
import org.terrier.matching.models.WeightingModel;
import org.terrier.structures.Index;
import org.terrier.structures.indexing.Indexer;

import com.google.common.hash.HashFunction;

import fr.ujm.tse.lt2c.satin.simhash.knn.ConversionMethod;
import fr.ujm.tse.lt2c.satin.simhash.knn.SimHashKNNDB;

/**
 * Abstract class to implements a {@link ConversionMethod} using Terrier.
 * 
 * Principle is the following :
 * 
 * I- Create both direct and indirect index
 * <ol>
 * <li>Add the collection of documents to compute freqencies, inverse document
 * frequencies so on</li>
 * <li>For each document, get term list from direct index,
 * {@link WeightingModel} for each terma</li>
 * <li>For each term query with {@link WeightingModel#score(double, double)} to
 * get its value</li>
 * </li>
 * <li>Build the <code>Map<String,Integer></code></li>
 * </ol>
 * 
 * 
 * @author Julien
 * 
 */
public class TerrierBaseDB {

	private Indexer indexer;

	public TerrierBaseDB() throws Exception {
		super();
		// Initialize terrier
		final String[] docnos = null;
		final String[] documents = null;
		final Document[] sourceDocs = new Document[docnos.length];
		for (int i = 0; i < docnos.length; i++) {
			final Map<String, String> docProperties = new HashMap<String, String>();
			docProperties.put("filename", docnos[i]);
			docProperties.put("docno", docnos[i]);
			sourceDocs[i] = new FileDocument(new ByteArrayInputStream(
					documents[i].getBytes()), docProperties,
					new EnglishTokeniser());
		}
		final Collection col = makeCollection(docnos, documents);
		indexer.index(new Collection[] { col });
		final Index index = Index.createIndex("", "");

	}

	/**
	 * 
	 * @param textualDocuments
	 * @return a {@link SimHashKNNDB} in which all the documents have been added
	 */
	public SimHashKNNDB[] processDocuments(final List<String> textualDocuments,
			final HashFunction hashFunction, final WeightingModel[] models) {
		// Initialize simhash DBs
		final SimHashKNNDB[] simhashDBs = new SimHashKNNDB[models.length];
		for (SimHashKNNDB db : simhashDBs) {
			db = new SimHashKNNDB(hashFunction);
		}
		// Clear terrier index
		clearIndex();
		// Add all the documents to the index
		addDocumentToIndex(textualDocuments);
		// For each model, populate the DB
		for (final WeightingModel model : models) {
			// For each document, get the terms and query the weighting model
			for (int i = 0; i < textualDocuments.size(); i++) {
				final String doc = textualDocuments.get(i);
			}
		}
		return simhashDBs;
	}

	/**
	 * @param textualDocuments
	 */
	private void addDocumentToIndex(final List<String> textualDocuments) {
		// TODO Auto-generated method stub

	}

	/**
	 * 
	 */
	private void clearIndex() {
		// TODO Auto-generated method stub

	}

	public Collection makeCollection(final String[] docnos,
			final String[] documents) throws Exception {

		final Document[] sourceDocs = new Document[docnos.length];
		for (int i = 0; i < docnos.length; i++) {
			final Map<String, String> docProperties = new HashMap<String, String>();
			docProperties.put("filename", docnos[i]);
			docProperties.put("docno", docnos[i]);
			sourceDocs[i] = new FileDocument(new ByteArrayInputStream(
					documents[i].getBytes()), docProperties,
					new EnglishTokeniser());
		}
		final Collection col = new CollectionDocumentList(sourceDocs,
				"filename");
		return col;
	}

}
