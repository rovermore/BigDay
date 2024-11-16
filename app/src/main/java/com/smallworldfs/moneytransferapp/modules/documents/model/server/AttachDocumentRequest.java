package com.smallworldfs.moneytransferapp.modules.documents.model.server;

import com.smallworldfs.moneytransferapp.modules.common.domain.model.ServerQueryMapRequest;

/**
 * Created by pedro del castillo on 20/9/17.
 */

public class AttachDocumentRequest extends ServerQueryMapRequest {

    public AttachDocumentRequest(String userToken, String userId, String documentId, String type, String extension, String mimeType, String documentFile, String attachmentFile, String title) {
        put("userToken", userToken);
        put("userId", userId);
        put("documentId", documentId);
        put("type", type);
        put("extension", extension);
        put("mimeType", mimeType);
        put("file", documentFile);
        put("attachmentType", attachmentFile);
        put("title", title);
    }
}
