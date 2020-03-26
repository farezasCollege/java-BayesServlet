/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Fareza Adityanto
 */
import java.util.Scanner;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
//import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FilenameUtils;
//import org.apache.tomcat.util.buf.StringUtils;
import weka.classifiers.bayes.NaiveBayes;
//import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.*;
//import weka.core.converters.ArffLoader.ArffReader;
//import weka.filters.Filter;

public class weka_nBayes {
    public static String setQuestionMark(String path, String filename) 
    throws FileNotFoundException, IOException{
        
        String convertedFile = "test_converted.csv";
        String destpath = path+"/"+convertedFile;
        String sourcepath = path+"/"+filename;
        ArrayList<String> list = null;
        BufferedReader br = null;
        BufferedWriter bw = null;
        String delimiter = ",";
        String line;
        int x;
        
        br = new BufferedReader(new FileReader(sourcepath)); //read from file
        bw = new BufferedWriter(new FileWriter(destpath)); //write to file
        String[] header = br.readLine().split(delimiter);
        bw.write(Arrays.toString(header).substring(1, 
            Arrays.toString(header).lastIndexOf("]"))+"\n");
        
        while((line = br.readLine()) != null){
            String[] temp = line.split(delimiter);
            int idx_selisih = (header.length-1) - (temp.length-1);
            list = new ArrayList<>(Arrays.asList(temp));
            
            //set kalo null value di tengah
            for(x=0;x<list.size();x++){
                if(list.get(x).equalsIgnoreCase("")){
                    list.set(x, "?");
                }
            }
            
            //set kalo null value di akhir
            while(idx_selisih > 0){
                list.add("?");
                idx_selisih--;
            }
            
            bw.write(list.toString().substring(1, 
                list.toString().length()-1)+"\n");
        }   
        br.close();
        bw.close();
        
        return convertedFile;
    }
    
    public static String Convert(String path,String sourceFileName, 
    String destFileName) throws Exception{
        
        String sourcepath = path+"/"+sourceFileName;
        String destpath = path+"/"+destFileName;
        
        CSVLoader loader = new CSVLoader();
        loader.setSource(new File(sourcepath));
        Instances data = loader.getDataSet();
        
        // save as ARFF
        BufferedWriter writer = new BufferedWriter(new FileWriter(destpath));
        writer.write(data.toString());
        writer.flush();
        writer.close();
        
        return destpath;
    }
    
    public static Instances getDataSet(String fileName) throws Exception {
        
        BufferedReader reader = null;
        reader = new BufferedReader(new FileReader(fileName));
        
        Instances data = new Instances (reader);
        data.setClassIndex(data.numAttributes() - 1);
        
        reader.close();
        return data;
    }
    
    public static void Bayes(Instances trainingDataSet, Instances testDataSet) 
    throws Exception {
        
        NaiveBayes naiveB = new NaiveBayes();
        naiveB.buildClassifier(trainingDataSet);
        
        System.out.println("============ Hasil prediksi =============");
//        Attribute test = testDataSet.instance(i);
        int i;
        for(i = 0; i<testDataSet.numInstances();i++){
            
            Instance test = testDataSet.instance(i);
//            System.out.println(test);
            double predNb = naiveB.classifyInstance(test);
//            System.out.println(predNb);
            double id = test.value(test.attribute(0));
            String pred = trainingDataSet.classAttribute().value((int) predNb);
            System.out.println((int) id +": "+pred);
        }
//        System.out.println(i);
    }
    
    public static void mainMachine() throws Exception{
        Scanner in = new Scanner(System.in);
        System.out.println("Masukkan path,<sourceTest>.csv,<DestTest>.arff"
                + ",<sourceTrain>.csv"
                + ",<destTrain>.arff jika tipe data csv");
        System.out.println("Masukkan path/<testData>.arff"
                + ",path/<trainData>.arff jika tipe file arff");
        
        String a = in.nextLine();
        //isi array kalo csv: path,sourceTest,DestTest,sourceTrain,destTrain
        //isi array kalo pake arff: path/SourceFile,path/DestFile
        String[] s = a.split(",");
        
        Instances testData;
        Instances trainData;
        boolean tipeFile = true;
        
        switch (s.length) {
            case 5:
                for(int i=1;i+1<s.length;i+=2){
                    if(!FilenameUtils.getExtension(s[i]).
                            equalsIgnoreCase("csv") && 
                        !FilenameUtils.getExtension(s[i+1])
                                .equalsIgnoreCase("arff")){
                        tipeFile=false;
                        break;
                    }
                }   
                
                if(tipeFile) {
                    testData = getDataSet(Convert(s[0] ,
                            setQuestionMark(s[0],s[1]) , s[2]));
                    trainData = getDataSet(Convert(s[0],s[3],s[4]));
                    Bayes(trainData, testData);
                } else {
                    System.out.println("tipe file tidak sesuai");
                }   
                break;
                
            case 2:
                for(int i=1;i<s.length;i++){
                    if(!FilenameUtils.getExtension(s[i]).
                            equalsIgnoreCase("arff")){
                        tipeFile=false;
                        break;
                    }
                }   
                
                if(tipeFile) {
                    testData = getDataSet(s[0]);
                    trainData = getDataSet(s[1]);
                    Bayes(trainData, testData);
                } else {
                    System.out.println("tipe file tidak sesuai");
                }
                break;
            default:
                System.out.println("File kurang atau lebih");
                break;
        }        
    }
    
    public static void main(String[] args) {
        try {
            mainMachine();            
        } catch (Exception ex) {
            Logger.getLogger(weka_nBayes.class.getName()).log(Level.SEVERE, 
                    null, ex);
        }
    }
}
