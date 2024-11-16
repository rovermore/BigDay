package com.smallworldfs.moneytransferapp.modules.documents.model.server;

import com.smallworldfs.moneytransferapp.modules.common.domain.model.ServerQueryMapRequest;

public class AttachSelfieRequest extends ServerQueryMapRequest {

    public AttachSelfieRequest(String userToken, String userId, String type, String extension, String mimeType, String documentFile, String attachmentFile, String documentId, String title) {
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
