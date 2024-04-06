package edu.whu.spacetime.service;

import android.content.Context;
import android.graphics.pdf.PdfDocument;


import java.io.File;
import java.io.IOException;

import com.tom_roush.pdfbox.android.PDFBoxResourceLoader;
import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.text.PDFTextStripper;

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
}
