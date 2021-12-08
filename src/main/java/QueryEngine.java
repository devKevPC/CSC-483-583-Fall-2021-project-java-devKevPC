package edu.arizona.cs;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class QueryEngine {
    boolean indexExists=false;
    String inputFilePath ="";
    
    Directory index;
    
    StandardAnalyzer analyzer;
	IndexWriterConfig config;
	IndexWriter w;

    public QueryEngine(String inputFile) throws java.io.FileNotFoundException,java.io.IOException {
        inputFilePath = "src/main/resources/input.txt";
        index = new RAMDirectory();
        
        analyzer = new StandardAnalyzer();
    	config = new IndexWriterConfig(analyzer);
    	w = new IndexWriter(index, config);
        
        buildIndex();
    }
    
    private static void addDoc(IndexWriter w, String text, String docName) throws IOException {
  	  Document doc = new Document();
  	  doc.add(new TextField("text", text, Field.Store.YES));
  	  doc.add(new StringField("docName", docName, Field.Store.YES));
  	  w.addDocument(doc);
    }

    private void buildIndex() throws java.io.FileNotFoundException,java.io.IOException {
        //Get file from resources folder
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(inputFilePath);
        
        try (Scanner inputScanner = new Scanner(file)) {
            while (inputScanner.hasNextLine()) {
                String str = inputScanner.nextLine();
                String separate[] = str.split(" ", 2);
                
                addDoc(w, separate[1], separate[0]);
                 
            }
            inputScanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        indexExists = true;
        
    	
    	
    }

    public static void main(String[] args ) {
        try {
            String fileName = "input.txt";
            System.out.println("********Welcome to  Homework 3!");
            String[] query13a = {"information", "retrieval"};
            QueryEngine objQueryEngine = new QueryEngine(fileName);
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    private List<ResultClass> querySearch(String[] query) throws java.io.FileNotFoundException,java.io.IOException, org.apache.lucene.queryparser.classic.ParseException {
       	String s = "";
       	
       	for(int i = 0; i < query.length; i++) {
       		s += query[i] + " ";
       	}
    	
    	Query q = new QueryParser("text", analyzer).parse(s);
    	
    	//System.out.println(q);
    	
    	IndexReader reader = DirectoryReader.open(index);
    	//System.out.println(reader);
    	
    	IndexSearcher searcher = new IndexSearcher(reader);
    	TopDocs docs = searcher.search(q, 1);
    	ScoreDoc[] hits = docs.scoreDocs;
    	
    	/*System.out.println("ggggfvfdvfdv");
    	System.out.println("Found " + hits.length + " hits.");
    	System.out.println("ggdfvdfgbbbgg");*/
    	for(int i=0;i<hits.length;++i) {
    	    int docId = hits[i].doc;
    	    Document d = searcher.doc(docId);
    	    System.out.println((i + 1) + ". " + d.get("docName") + "\t" + d.get("text"));
    	}
    	
    	List<ResultClass>  ans=new ArrayList<ResultClass>();
        ans =returnDummyResults(2);
        return ans;
    }

    public List<ResultClass> runQ1_1(String[] query) throws java.io.FileNotFoundException,java.io.IOException, org.apache.lucene.queryparser.classic.ParseException {
        if(!indexExists) {
            buildIndex();
        }
        querySearch(query);
        
        List<ResultClass>  ans=new ArrayList<ResultClass>();
        ans =returnDummyResults(2);
        return ans;
    }

    public List<ResultClass> runQ1_2_a(String[] query) throws java.io.FileNotFoundException,java.io.IOException {
        if(!indexExists) {
            buildIndex();
        }
        List<ResultClass>  ans=new ArrayList<ResultClass>();
        ans =returnDummyResults(2);
        return ans;
    }

    public List<ResultClass> runQ1_2_b(String[] query) throws java.io.FileNotFoundException,java.io.IOException {
        if(!indexExists) {
            buildIndex();
        }
        List<ResultClass>  ans=new ArrayList<ResultClass>();
        ans =returnDummyResults(0);
        return ans;
    }

    public List<ResultClass> runQ1_2_c(String[] query) throws java.io.FileNotFoundException,java.io.IOException {
        if(!indexExists) {
            buildIndex();
        }
        List<ResultClass>  ans=new ArrayList<ResultClass>();
        ans =returnDummyResults(1);
        return ans;
    }

    public List<ResultClass> runQ1_3(String[] query) throws java.io.FileNotFoundException,java.io.IOException {

        if(!indexExists) {
            buildIndex();
        }
        StringBuilder result = new StringBuilder("");
        List<ResultClass>  ans=new ArrayList<ResultClass>();
        ans =returnDummyResults(2);
        return ans;
    }


    private  List<ResultClass> returnDummyResults(int maxNoOfDocs) {

        List<ResultClass> doc_score_list = new ArrayList<ResultClass>();
            for (int i = 0; i < maxNoOfDocs; ++i) {
                Document doc = new Document();
                doc.add(new TextField("title", "", Field.Store.YES));
                doc.add(new StringField("docid", "Doc"+Integer.toString(i+1), Field.Store.YES));
                ResultClass objResultClass= new ResultClass();
                objResultClass.DocName =doc;
                doc_score_list.add(objResultClass);
            }

        return doc_score_list;
    }

}
