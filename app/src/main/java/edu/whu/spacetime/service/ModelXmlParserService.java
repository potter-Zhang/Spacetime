package edu.whu.spacetime.service;

import android.util.Xml;

import com.google.ar.core.common.samplerender.Mesh;
import com.google.ar.core.common.samplerender.Shader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import edu.whu.spacetime.domain.ARModel;

public class ModelXmlParserService {
    public static List<ARModel> getModels(InputStream xml) throws IOException {
        List <ARModel> models = null;
        ARModel arModel = null;
        XmlPullParser pullParser = Xml.newPullParser();
        try{
            //为PULL解析器设置要解析的XML数据
            pullParser.setInput(xml,"UTF-8");
            int event= pullParser.getEventType();
            while(event!= XmlPullParser.END_DOCUMENT){
                switch (event){
                    case XmlPullParser.START_DOCUMENT:
                        models= new ArrayList<ARModel>();
                        break;
                    case XmlPullParser.START_TAG:
                        if("model".equals(pullParser.getName())){
                            arModel = new ARModel();
                        }
                        if("name".equals(pullParser.getName())){
                            String name = pullParser.nextText();
                            arModel.setName(name);
                        }
                        if("preview-path".equals(pullParser.getName())){
                            String previewPath = pullParser.nextText();
                            arModel.setPreviewImgPath(previewPath);
                        }
                        if("obj-path".equals(pullParser.getName())){
                            String objPath = pullParser.nextText();
                            arModel.setObjPath(objPath);
                        }
                        if("texture-path".equals(pullParser.getName())){
                            String texturePath = pullParser.nextText();
                            arModel.setTexturePath(texturePath);
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if("model".equals(pullParser.getName())){
                            models.add(arModel);
                            arModel = null;
                        }
                        break;
                }
                event = pullParser.next();
            }
        }catch(XmlPullParserException e){
            e.printStackTrace();
        }
        return models;
    }
}
