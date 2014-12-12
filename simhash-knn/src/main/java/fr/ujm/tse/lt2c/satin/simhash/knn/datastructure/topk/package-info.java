/** Various implementations of top-k datastructure
 *
 * <ul>
 *
 * <li>{@link fr.ujm.tse.lt2c.satin.simhash.knn.datastructure.topk.ArrayListTopK} : stores all distance in array list,
 * sort and extract top-k. Simple and slow for large <code>n</code>.
 * </li>
 *<li>{@link fr.ujm.tse.lt2c.satin.simhash.knn.datastructure.topk.GuavaMinMaxTopK} : min-max heap, with Autoboxing. Fast for large <code>n</code></li>
 *</ul>
 *
 * @author Julien
 *
 */
package fr.ujm.tse.lt2c.satin.simhash.knn.datastructure.topk;