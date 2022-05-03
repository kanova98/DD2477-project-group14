package com.tianyu.pojo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;

import com.tianyu.service.Tokenizer;

public class User {

    private int ID;
    private ArrayList<BookContent> readBooks;
    private HashSet<String> stopWords;

    public User(int ID) throws IOException{
        this.ID = ID;
        this.readBooks = new ArrayList<BookContent>();
        this.stopWords = readStopWordsFromFile();

    }

    private HashSet<String> readStopWordsFromFile() throws IOException{

        BufferedReader reader = new BufferedReader(new FileReader("/Users/filipkana/Documents/Skolarbete/DD2477/irProject/code/tianyu-es-bs/src/main/resources/stopWords.txt"));
        HashSet<String> stopWords = new HashSet<>();
        String line;
        while((line = reader.readLine()) != null){
            stopWords.add(line.replace("\n", ""));
            System.out.println("Read stopword: "+ line);
        }

        return stopWords;
    }

    public int getID() {
        return ID;
    }

    public void addBook(BookContent book) {
        this.readBooks.add(book);
    }

    public ArrayList<BookContent> getReadBooks() {
        return readBooks;
    }

    /*
     * Uses the books that the reader has read to compute a string with boosted
     * terms according to their frequency in the abstracts of the books that the user has read
     * //TODO fix so that method only returns a string of a given maximum length in some way
     */
    public String computeAbstractCentroid() throws IOException {

        HashMap<String,Integer> wordFrequencies = new HashMap<>();
        // Iterate through each book
        for(BookContent book : readBooks){
            String removedCitation = book.getAbstractForBook().replaceAll("\"", "");
            StringReader reader = new StringReader(removedCitation);
            Tokenizer tokenizer = new Tokenizer(reader, true, false, true, "/Users/filipkana/Documents/Skolarbete/DD2477/irProject/code/tianyu-es-bs/src/main/resources/patterns.txt");
            while (tokenizer.hasMoreTokens()){
                String term = tokenizer.nextToken();
                if(!stopWords.contains(term)){
                    if(!wordFrequencies.containsKey(term)){
                        wordFrequencies.put(term,0);
                    }
                    wordFrequencies.put(term,(wordFrequencies.get(term) + 1));
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        float nrOfReadBooks = readBooks.size();
        int count = 0;
        for(Map.Entry<String, Integer> entry : wordFrequencies.entrySet()){
            float termVal = entry.getValue() / nrOfReadBooks;
            sb.append(entry.getKey());
            sb.append("^");
            sb.append(termVal);
            sb.append(" ");
            count += 1;
            if(count > 1500){
                break;
            }
            System.out.println(count);
        }
        System.out.println(sb.toString());
        return sb.toString();

    }
}
