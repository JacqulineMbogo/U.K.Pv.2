package com.example.jacquey.ukullimapoultry.myaccount;

import android.print.PrintManager;
import android.support.v7.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.content.Context;
import android.print.PrintDocumentInfo;
import android.print.pdf.PrintedPdfDocument;
import android.graphics.pdf.PdfDocument;
import android.view.View;

import com.example.jacquey.ukullimapoultry.R;


public class CustomPrintActivity extends AppCompatActivity {


    public void printDocument(View view)
    {
        PrintManager printManager = (PrintManager) this
                .getSystemService(Context.PRINT_SERVICE);

        String jobName = this.getString(R.string.app_name) +
                " Document";

        printManager.print(jobName, new
                        MyPrintDocumentAdapter(this),
                null);
    }
}



