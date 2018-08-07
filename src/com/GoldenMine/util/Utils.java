package com.GoldenMine.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class Utils {

    public static String loadResource(String fileName) throws Exception {
        /*String result;
        try (InputStream in = Class.forName(Utils.class.getName()).getResourceAsStream(fileName);
             Scanner scanner = new Scanner(in, "UTF-8")) {
            result = scanner.useDelimiter("\\A").next();
        }
        return result;*/

        StringBuilder result = new StringBuilder();

        try {
            Reader reader = new InputStreamReader(new FileInputStream(new File(fileName)));

            char[] buffer = new char[1024];

            int len = 1024;

            while(len!=-1) {
                len = reader.read(buffer, 0, len);

                if(len>0)
                    result.append(buffer, 0, len);
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        return result.toString();
    }

}
