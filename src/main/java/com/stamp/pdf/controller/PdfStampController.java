package com.stamp.pdf.controller;

import com.stamp.pdf.model.PDFStamperMainImg;
import com.stamp.pdf.model.PdfStamp;
import com.stamp.pdf.payload.request.PdfStampRequest;
import com.stamp.pdf.payload.request.PdfStampWithImageRequest;
import com.stamp.pdf.service.PdfStampService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("api/pdf")
public class PdfStampController {

    private final PdfStampService pdfStampService;


    @Autowired
    public PdfStampController(PdfStampService pdfStampService) {
        this.pdfStampService = pdfStampService;
    }

    @GetMapping
    public String getPdfStamp() {
        return pdfStampService.dummyService();
    }

    @PostMapping("/stamp")
    public String setPdfStamp(@Valid @RequestBody PdfStampRequest pdfStampRequest) throws IOException {
        PdfStamp pdfStamp = pdfStampRequest.asPdfStamp();
        return pdfStampService.stamp(pdfStamp);
    }

    @PostMapping("/stamp/image")
    public String setPdfStampWithImage(@Valid @RequestBody PdfStampWithImageRequest pdfStampRequest) throws IOException {
        PDFStamperMainImg pdfStamp = pdfStampRequest.asPdfStamp();
        return pdfStampService.stamp(pdfStamp);
    }
}
