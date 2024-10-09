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
            // Đọc dữ liệu từ file CSV
            CSVLoader loader = new CSVLoader();
            loader.setSource(new File("/home/kai/IdeaProjects/LibraryBook/src/main/java/org/model/book_return_data_balanced.csv"));

            // Tải dữ liệu vào một đối tượng Instances
            Instances data = loader.getDataSet();

            // Đặt thuộc tính mục tiêu (lớp) cho mô hình
            data.setClassIndex(data.numAttributes() - 1);

            // Tải mô hình đã lưu từ file
            String modelFilePath = "/home/kai/IdeaProjects/LibraryBook/src/main/java/org/model/logistic_regression.model";
            Classifier logistic = (Classifier) SerializationHelper.read(modelFilePath);

            System.out.println("Mô hình đã được tải từ: " + modelFilePath);

            // Tạo một instance mới cho dự đoán
            double[] values = new double[data.numAttributes()];
            values[0] = readingHours; // Giả sử chỉ số 0 là 'Reading_Hours'
            values[1] = totalPages; // Giả sử chỉ số 1 là 'Total_Pages'
            values[2] = 0; // Placeholder cho cột lớp, sẽ không được sử dụng trong dự đoán

            Instance newInstance = new DenseInstance(1.0, values);
            newInstance.setDataset(data);

            // Dự đoán lớp cho instance mới
            double predictedClass = logistic.classifyInstance(newInstance);

            // Chuyển đổi predictedClass thành int và trả về
            return  predictedClass;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1; // Hoặc bạn có thể chọn trả về một giá trị mặc định khác
    }
}
