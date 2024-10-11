package org.model;

import weka.classifiers.Classifier;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.core.SerializationHelper;

import java.io.File;

public class LoadModelAndPredict {
    public double predict(int totalPages, int readingHours) {
        try {

            String relativeCSVPath = "src/main/java/org/model/book_return_data_balanced.csv";
            CSVLoader loader = new CSVLoader();
            loader.setSource(new File(System.getProperty("user.dir"), relativeCSVPath));

            // Đọc dữ liệu từ file CSV
            Instances data = loader.getDataSet();
            data.setClassIndex(data.numAttributes() - 1);


            String relativeModelPath = "src/main/java/org/model/logistic_regression.model";
            String modelFilePath = new File(System.getProperty("user.dir"), relativeModelPath).getAbsolutePath();

            // Tải mô hình đã lưu
            Classifier logistic = (Classifier) SerializationHelper.read(modelFilePath);
            System.out.println("Mô hình đã được tải từ: " + modelFilePath);

            // Tạo một instance mới cho dự đoán
            double[] values = new double[data.numAttributes()];
            values[0] = readingHours; // Giả sử thuộc tính đầu tiên là giờ đọc
            values[1] = totalPages;    // Giả sử thuộc tính thứ hai là tổng số trang
            values[2] = 0;

            Instance newInstance = new DenseInstance(1.0, values);
            newInstance.setDataset(data);

            // Dự đoán lớp cho instance mới
            double predictedClass = logistic.classifyInstance(newInstance);
            return predictedClass;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
}
