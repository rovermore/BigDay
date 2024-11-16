package com.smallworldfs.moneytransferapp.modules.documents.model.server;

import com.smallworldfs.moneytransferapp.modules.common.domain.model.ServerQueryMapRequest;

public class AttachmentDocumentsRequest extends ServerQueryMapRequest {

    public AttachmentDocumentsRequest(String userToken, String documentId, AttachmentDocumentsDocumentRequest front, AttachmentDocumentsDocumentRequest back) {
        put("userToken", userToken);
        put("documentId", documentId);

        put("front", front);
        put("back", back);
    }

}
