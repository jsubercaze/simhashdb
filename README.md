Simhashdb
=========

Scalabale and fast framework for simhash similarity search.
 * Core : Support for Simhash
 * KNN : Database for fast knn search using binary footprint
 * Text transform : Tools to transform text into binary footprint
 * Examples : some code samples


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



