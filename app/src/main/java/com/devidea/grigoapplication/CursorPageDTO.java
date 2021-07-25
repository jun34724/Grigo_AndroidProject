package com.devidea.grigoapplication;

import java.util.ArrayList;

public class CursorPageDTO {
    private ArrayList<PostDTO> postDTOS;
    private Boolean hasNext;

    public ArrayList<PostDTO> getPostDTOS() {
        return postDTOS;
    }

    public void setPostDTOS(ArrayList<PostDTO> postDTOS) {
        this.postDTOS = postDTOS;
    }

    public Boolean getHasNext() {
        return hasNext;
    }

    public void setHasNext(Boolean hasNext) {
        this.hasNext = hasNext;
    }
}
