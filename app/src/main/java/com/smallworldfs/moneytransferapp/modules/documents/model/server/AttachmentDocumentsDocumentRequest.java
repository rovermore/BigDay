package com.smallworldfs.moneytransferapp.modules.documents.model.server;

import com.smallworldfs.moneytransferapp.modules.common.domain.model.ServerQueryMapRequest;

public class AttachmentDocumentsDocumentRequest extends ServerQueryMapRequest {

    public AttachmentDocumentsDocumentRequest(String type, String extension, String mimeType, String file, String attachmentType, String title) {
        put("type", type);
        put("extension", extension);
        put("mimeType", mimeType);
        put("attachmentType", attachmentType);
        put("title", title);
        put("file", file);
    }

}