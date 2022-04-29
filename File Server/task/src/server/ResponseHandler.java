package server;

//import org.apache.commons.lang3.RandomStringUtils;

import util.StringGenerator;

import java.io.IOException;
import java.util.Map;

import static util.Constants.*;

public class ResponseHandler {
    private final FileStorage fileStorage;

    public ResponseHandler(FileStorage fileStorage) {
        this.fileStorage = fileStorage;
    }

    public String processPutRequest(String request, byte[] fileBytes) throws IOException {
        String[] requestParts = request.split(SPACE);
        String fileName;
        if (requestParts[1].isEmpty()) {
            //fileName = RandomStringUtils.randomAlphanumeric(10) + requestParts[2];
            fileName = StringGenerator.generate(10) + requestParts[2];
        } else {
            fileName = requestParts[1];
        }

        if (fileStorage.contains(fileName)) {
            return FORBIDDEN;
        }
        int id = fileStorage.addFile(fileName, fileBytes);
        return SUCCESS + SPACE + id;
    }

    public Map<String, byte[]> processGetRequest(String request) throws IOException {
        String[] requestData = request.split(SPACE);
        String option = requestData[1];
        byte[] fileBytes;
        switch (option) {
            case BY_ID:
                fileBytes = fileStorage.getFileById(Integer.parseInt(requestData[2]));
                break;
            case BY_NAME:
                fileBytes = fileStorage.getFileByName(requestData[2]);
                break;
            default:
                throw new IllegalArgumentException(option + " option is not supported");
        }
        if (fileBytes != null) {
            return Map.of(SUCCESS, fileBytes);
        }
        return Map.of(NOT_FOUND, new byte[0]);
    }

    public String processDeleteRequest(String request) throws IOException {
        String[] requestData = request.split(SPACE);
        String option = requestData[1];
        boolean isDeleted;
        switch (option) {
            case BY_ID:
                isDeleted = fileStorage.deleteFileById(Integer.parseInt(requestData[2]));
                break;
            case BY_NAME:
                isDeleted = fileStorage.deleteFileByName(requestData[2]);
                break;
            default:
                throw new IllegalArgumentException(option + " option is not supported");
        }

        if (isDeleted) {
            return SUCCESS;
        }
        return NOT_FOUND;
    }

}