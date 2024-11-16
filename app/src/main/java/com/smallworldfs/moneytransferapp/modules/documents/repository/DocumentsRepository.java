package com.smallworldfs.moneytransferapp.modules.documents.repository;

import java.io.File;
import java.util.Objects;

public class DocumentsRepository {

    public static DocumentsRepository sInstance = null;

    private DocumentsRepository() {
    }

    public static DocumentsRepository getInstance() {
        if (sInstance == null) {
            sInstance = new DocumentsRepository();
        }
        return sInstance;
    }

    public void removeAllImages(File imagesStorage) {
        remove(imagesStorage);
    }

    private void remove(File file) {
        if (file != null) {
            if (file.isDirectory()) {
                if (file.listFiles() != null && Objects.requireNonNull(file.listFiles()).length > 0) {
                    for (File child : Objects.requireNonNull(file.listFiles())) {
                        remove(child);
                    }
                }
            }
            file.delete();
        }
    }

    public int getDocumentRequirements() {
        return 0;
    }
}
