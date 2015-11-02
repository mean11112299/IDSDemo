package com.imark.nghia.idscore.network.webservices.models;

/**
 * Created by Nghia-PC on 9/18/2015.
 */
public class PostCommentWSResult extends BaseWSResult {
    private long CommentID;

    public long getCommentID() {
        return CommentID;
    }

    public void setCommentID(long commentID) {
        CommentID = commentID;
    }
}
