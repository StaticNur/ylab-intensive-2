package com.ylab.intensive.service;

public interface TrainingTypeService {
    void addTrainingType(String typeName);
    void deleteTrainingType(String typeName);
    void editTrainingType(String oldTypeName, String newTypeName);
}
