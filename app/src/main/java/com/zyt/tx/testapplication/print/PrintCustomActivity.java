package com.zyt.tx.testapplication.print;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.PrintManager;
import android.print.pdf.PrintedPdfDocument;
import android.support.v7.app.AppCompatActivity;

import com.zyt.tx.testapplication.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class PrintCustomActivity extends AppCompatActivity {

    private PrintedPdfDocument mPdfDocument;
    private int totalPages=4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_custom);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnPrintCustom)
    public void onClick() {
        doPrint();
    }

    @TargetApi(19)
    private void doPrint() {
        PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
        String jobName = " document";
        printManager.print(jobName,new MyPrintAdpater(),null);

    }



    @TargetApi(19)
    class MyPrintAdpater extends PrintDocumentAdapter{

        @Override
        public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes, CancellationSignal cancellationSignal, LayoutResultCallback callback, Bundle extras) {
            mPdfDocument = new PrintedPdfDocument(PrintCustomActivity.this, newAttributes);

            // Respond to cancellation request
//            if (cancellationSignal.isCancelled() ) {
//                callback.onLayoutCancelled();
//                return;
//            }

            // Compute the expected number of printed pages
            int pages = computePageCount(newAttributes);

            if (pages > 0) {
                // Return print information to print framework
                PrintDocumentInfo info = new PrintDocumentInfo
                        .Builder("print_output.pdf")
                        .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                        .setPageCount(pages).build();

                // Content layout reflow is complete
                callback.onLayoutFinished(info, true);
            } else {
                // Otherwise report an error to the print framework
                callback.onLayoutFailed("Page count calculation failed.");
            }
        }

        @Override
        public void onWrite(PageRange[] pages, ParcelFileDescriptor destination, CancellationSignal cancellationSignal, WriteResultCallback callback) {
            // Iterate over each page of the document,
            // check if it's in the output range.
//            for (int i = 0; i < totalPages; i++) {
//                // Check to see if this page is in the output range.
//                if (containsPage(pageRanges, i)) {
//                    // If so, add it to writtenPagesArray. writtenPagesArray.size()
//                    // is used to compute the next output page index.
//                    writtenPagesArray.append(writtenPagesArray.size(), i);
//                    PdfDocument.Page page = mPdfDocument.startPage(i);
//
//                    // check for cancellation
//                    if (cancellationSignal.isCancelled()) {
//                        callback.onWriteCancelled();
//                        mPdfDocument.close();
//                        mPdfDocument = null;
//                        return;
//                    }
//
//                    // Draw page content for printing
//                    drawPage(page);
//
//                    // Rendering is complete, so page can be finalized.
//                    mPdfDocument.finishPage(page);
//                }
//            }
//
//            // Write PDF document to file
//            try {
//                mPdfDocument.writeTo(new FileOutputStream(
//                        destination.getFileDescriptor()));
//            } catch (IOException e) {
//                callback.onWriteFailed(e.toString());
//                return;
//            } finally {
//                mPdfDocument.close();
//                mPdfDocument = null;
//            }
//            PageRange[] writtenPages = computeWrittenPages();
//            // Signal the print framework the document is complete
//            callback.onWriteFinished(writtenPages);

        }
    }

    @TargetApi(19)
    private int computePageCount(PrintAttributes printAttributes) {
        int itemsPerPage = 4; // default item count for portrait mode

        PrintAttributes.MediaSize pageSize = printAttributes.getMediaSize();
        if (!pageSize.isPortrait()) {
            // Six items per page in landscape orientation
            itemsPerPage = 6;
        }

        // Determine number of print items
        int printItemCount = getPrintItemCount();

        return (int) Math.ceil(printItemCount / itemsPerPage);
    }

    private int getPrintItemCount() {
        return 0;
    }
}
