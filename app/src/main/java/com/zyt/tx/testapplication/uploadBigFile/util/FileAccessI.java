package com.zyt.tx.testapplication.uploadBigFile.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by Taxi on 2017/2/6.
 */

public class FileAccessI {

    private RandomAccessFile oSaveFile;

    public FileAccessI() throws Exception {
        this("", 0);
    }

    public FileAccessI(String name, long position) throws Exception {
        oSaveFile = new RandomAccessFile(name, "rw");
        oSaveFile.seek(position);
    }

    public synchronized int write(byte[] b, int start, int len) {
        int n = -1;
        try {
            oSaveFile.write(b, start, len);
            n = len;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return n;
    }

    public synchronized Detail getContent(long start) {
        Detail detail = new Detail();
        detail.b = new byte[1024 * 100];

        try {
            oSaveFile.seek(start);
            detail.len = oSaveFile.read(detail.b);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return detail;
    }

    public long getFileLength() {
        long len = 0l;

        try {
            len = oSaveFile.length();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return len;
    }


    public class Detail {
        public byte[] b;
        public int len;
    }
}
