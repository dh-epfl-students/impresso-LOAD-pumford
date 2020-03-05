# impresso-LOAD

Project Context: Digitized newspapers constitute an extraordinary goldmine of information about our past, and historians are among those who can most benefit from it. Impresso, an ongoing, collaborative research project based at the DHLAB, has been building a large-scale corpus of digitized newspapers: it currently contains 76 newspapers from Switzerland and Luxembourg (written in French, German and Luxembourgish) for a total of 12 billion tokens. 
This corpus was enriched with several layers of semantic information such as topics, text reuse and named entities (persons, locations, dates and organizations). 
The latter are particularly useful for historians as co-occurrences of named entities often indicate (and help to identify) historical events.
The impresso corpus currently contains some 164 million entity mentions, linked to 500 thousand entities from DBpedia (partly mapped to Wikidata).

Yet, making use of such a large knowledge graph in an interactive tool for historians — such as the tool impresso has been developing — requires an underlying document model that facilitates the retrieval of entity relations, contexts, and related events from the documents effectively and efficiently. 
This is where [LOAD](https://dbs.ifi.uni-heidelberg.de/resources/load/) comes into play, which is a graph-based document model, developed by Andreas Spitz, that supports browsing, extracting and summarizing real world events in large collections of unstructured text based on named entities such as Locations, Organizations, Actors and Dates.

Project Description: The goal of this project is to apply the LOAD model to the impresso corpus, adapting the LOAD implementation for the specific needs of the impresso corpus.
This adaptation includes the following changes: the addition of different entity types, sources of different languages(German, French and Luxembourgish), changing the context weight relation from sentence based to word distance based and adapting the database from MongoDB to Solr/MySQL.