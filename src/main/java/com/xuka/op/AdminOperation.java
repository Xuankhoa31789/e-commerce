package com.xuka.op;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import com.xuka.op.CustomerOperation;

public class AdminOperation {
    private static AdminOperation instance = null;

    private AdminOperation() {
    }
    public static AdminOperation getInstance() {
        if (instance == null) {
            instance = new AdminOperation();
        }
        return instance;
    }

    //TODO: Implement RegisterAdmin method eventhough idk what the fuck it do
}
