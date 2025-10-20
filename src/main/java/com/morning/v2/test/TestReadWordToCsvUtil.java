package com.morning.v2.test;

import com.morning.v2.common.GetCsvFilesUtil;
import org.junit.Test;

public class TestReadWordToCsvUtil {
    @Test
    public void testReadWord(){
        String docxFolderUrl = "src/main/TestExample/";
        String resultFolderUrl = "src/main/TestExample/result/";

        GetCsvFilesUtil.getCSV(docxFolderUrl,resultFolderUrl);
    }
}
