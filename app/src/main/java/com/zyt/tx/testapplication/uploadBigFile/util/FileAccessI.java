package com.zyt.tx.testapplication.uploadBigFile.util;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

/**
 * Created by Taxi on 2017/2/6.
 */

public class FileAccessI {

    public FileAccessI() throws Exception {
        this("", 0);
    }

    public FileAccessI(String name, long position) throws Exception {
        RandomAccessFile oSaveFile = new RandomAccessFile(name, "rw");
        oSaveFile.seek(position);
    }
}
