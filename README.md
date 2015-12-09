SimhashDB
=========

SimhashDB is a fast and scalable framework for searching nearest neighbor documents using [Simhash algorithm](http://www.cs.princeton.edu/courses/archive/spr04/cos598B/bib/CharikarEstim.pdf).

The framework is made of the following submodule

 * Core : SimHash algorithm
 * KNN : In memory database for nearest neighbor search
 * Text transform : Tools to transform text into binary footprint
 * Text transform Terrier: Tools to transform text into binary footprint using the [Terrier IR engine](http://terrier.org/)
 * Examples : some code samples
 * KNN Benchmark : jmh benchmark to assess KNN performance

Basics
------

The core module transforms any Map of key values where keys are String and value are Double into a binary footprint (BitSet).
The KNN module offers the storage layer where binary footprints are inserted and requested. The storage is in-memory only for now. Nearest neighbor documents can be searched with either a top-k or Hamming ball query.

Performance
-----------




Sample use
----------
Clone the git repository and run mvn install 
Alternatively see the Maven Section to use the github hosted repository




Maven
-----
First you need to setup the server in your pom.xml :


    <repositories>
      <repository>
        <id>simhashdb-mvn-repo</id>
        <url>https://raw.github.com/jsubercaze/simhashdb/mvn-repo/</url>
        <snapshots>
            <enabled>true</enabled>
            <updatePolicy>always</updatePolicy>
        </snapshots>
      </repository>
    </repositories>

Then use the following dependency :

    <dependency>
     <groupId>fr.ujm.tse.lt2c.satin</groupId>
      <artifactId>simhashdb</artifactId>
      <version>0.0.2-SNAPSHOT</version>
    </dependency>

Publication
-----------

If you used Simhashdb in a publication please contact me so that I add it to the list
* Julien Subercaze, Christophe Gravier, Frederique Laforest. Real-Time, Scalable, Content-based Twitter users recommendation. Web Intelligence and Agent Systems : An International Journal (WIAS), 2015 [PDF](https://hal.archives-ouvertes.fr/hal-01170244/document)

