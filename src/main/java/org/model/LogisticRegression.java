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

            String relativeCSVPath = "src/main/java/org/model/book_return_data_balanced.csv";


            File csvFile = new File(System.getProperty("user.dir"), relativeCSVPath);

            // Tải file CSV với đường dẫn tương đối
            CSVLoader loader = new CSVLoader();
            loader.setSource(csvFile);

            // Tải dữ liệu vào một đối tượng Instances
            Instances data = loader.getDataSet();

            // Chuyển đổi các thuộc tính số cuối cùng thành dạng danh mục
            NumericToNominal convert = new NumericToNominal();
            convert.setInputFormat(data);
            convert.setAttributeIndices(String.valueOf(data.numAttributes()));
            data = Filter.useFilter(data, convert);

            // Đặt thuộc tính cuối cùng làm nhãn lớp (class)
            data.setClassIndex(data.numAttributes() - 1);

            // Xây dựng mô hình Logistic Regression
            Classifier logistic = new Logistic();
            logistic.buildClassifier(data);


            String relativeModelPath = "src/main/java/org/model/logistic_regression.model";
            String modelFilePath = new File(System.getProperty("user.dir"), relativeModelPath).getAbsolutePath();

            // Lưu mô hình vào file với đường dẫn tương đối
            SerializationHelper.write(modelFilePath, logistic);

            System.out.println("Mô hình đã được lưu vào: " + modelFilePath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
