package edu.whu.spacetime.domain;

import com.google.ar.core.common.samplerender.Mesh;
import com.google.ar.core.common.samplerender.Shader;

public class ARModel {
    private String name;

    private String previewImgPath;

    private String objPath;

    private String texturePath;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPreviewImgPath() {
        return previewImgPath;
    }

    public void setPreviewImgPath(String previewImgPath) {
        this.previewImgPath = previewImgPath;
    }

    public String getObjPath() {
        return objPath;
    }

    public void setObjPath(String objPath) {
        this.objPath = objPath;
    }

    public String getTexturePath() {
        return texturePath;
    }

    public void setTexturePath(String texturePath) {
        this.texturePath = texturePath;
    }
}
