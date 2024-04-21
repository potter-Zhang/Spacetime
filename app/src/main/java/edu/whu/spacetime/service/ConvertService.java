package edu.whu.spacetime.service;

import android.content.Context;

import com.tom_roush.pdfbox.android.PDFBoxResourceLoader;
import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.text.PDFTextStripper;

import org.apache.poi.hslf.usermodel.HSLFSlide;
import org.apache.poi.hslf.usermodel.HSLFSlideShow;
import org.apache.poi.hwpf.extractor.Word6Extractor;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.sl.extractor.SlideShowExtractor;
import org.apache.poi.sl.usermodel.SlideShow;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * PDF、PPT、图片转文字
 */
public class ConvertService {

    public ConvertService(Context context) {
        PDFBoxResourceLoader.init(context);
    }

    public String pdf2Text(File file) throws IOException {
        PDDocument document = PDDocument.load(file);
        PDFTextStripper stripper = new PDFTextStripper();
        String result = stripper.getText(document);
        return result;
    }

    public String ppt2Text(File file) throws IOException {
        String result = "";
        String filePath = file.getPath();
        String suffix = filePath.substring(filePath.lastIndexOf('.'));
        try(FileInputStream inputStream = new FileInputStream(file)){
            SlideShow slideShow;
            if (suffix.equals(".ppt")) {
                slideShow = new HSLFSlideShow(inputStream);
            } else {
                slideShow = new XMLSlideShow(inputStream);
            }
            SlideShowExtractor extractor = new SlideShowExtractor(slideShow);
            result = extractor.getText();
        }
        return result;
    }

    public String word2Text(File file) throws IOException {
        StringBuilder result = new StringBuilder();
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            XWPFDocument document = new XWPFDocument(fileInputStream);
            for (XWPFParagraph p : document.getParagraphs()) {
                result.append(p.getText());
            }
        }
        return result.toString();
    }
}
