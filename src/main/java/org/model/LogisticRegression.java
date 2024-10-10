package org.model;

import weka.classifiers.Classifier;
import weka.classifiers.functions.Logistic;
import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.core.SerializationHelper;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NumericToNominal;

import java.io.File;

public class LogisticRegression {
    public static void main(String[] args) {
        try {

            CSVLoader loader = new CSVLoader();
            loader.setSource(new File("/home/kai/IdeaProjects/LibraryBook/src/main/java/org/model/book_return_data_balanced.csv"));

            // Tải dữ liệu vào một đối tượng Instances
            Instances data = loader.getDataSet();


            NumericToNominal convert = new NumericToNominal();
            convert.setInputFormat(data);
            convert.setAttributeIndices(String.valueOf(data.numAttributes()));
            data = Filter.useFilter(data, convert);


            data.setClassIndex(data.numAttributes() - 1);

            // Xây dựng mô hình Logistic Regression
            Classifier logistic = new Logistic();
            logistic.buildClassifier(data);

            //  mô hình vào file
            String modelFilePath = "/home/kai/IdeaProjects/LibraryBook/src/main/java/org/model/logistic_regression.model";
            SerializationHelper.write(modelFilePath, logistic);

            System.out.println("Mô hình đã được lưu vào: " + modelFilePath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
