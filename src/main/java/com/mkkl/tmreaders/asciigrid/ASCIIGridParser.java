package com.mkkl.tmreaders.asciigrid;

import com.mkkl.tmreaders.ParsingException;

import java.io.*;

public class ASCIIGridParser {

    public static ASCIIGridData parse(InputStream stream) throws IOException {
        return parse(new InputStreamReader(stream));
    }

    public static ASCIIGridData parse(String file_path) throws IOException {
        return parse(new FileReader(file_path));
    }

    public static ASCIIGridData parse(File file) throws IOException {
        return parse(new FileReader(file));
    }

    public static ASCIIGridData parse(Reader reader) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(reader);
        ASCIIGridData asciiGridData = parse(bufferedReader);
        bufferedReader.close();
        return asciiGridData;
    }

    public static ASCIIGridData parse(BufferedReader reader) throws IOException {
        ASCIIGridData asciiGridData = new ASCIIGridData();
        //Reading attributes
        String attrline;

        while((attrline = reader.readLine()) != null && !attrline.startsWith(" ")) {
            if (attrline.length() < 3) throw new ParsingException("Attribute was too short");
            int keyend = attrline.indexOf(' ');
            String key = attrline.substring(0, keyend);
            String value = attrline.substring(keyend + 1).replace(" ", "");
            asciiGridData.addAttr(key, value);
        }

        //Initializing grid array
        asciiGridData.setGridSize(asciiGridData.getAttrInt("ncols"), asciiGridData.getAttrInt("nrows"));

        //Read height map data
        int row = 0;
        parseGridLine(attrline, row, asciiGridData);
        row++;

        while((attrline = reader.readLine()) != null) {
            parseGridLine(attrline, row, asciiGridData);
            row++;
        }
        return asciiGridData;

    }

    private static void parseGridLine(String line, int row, ASCIIGridData asciiGridData) throws ParsingException {
        if (!line.startsWith(" ")) throw new ParsingException("Height data line didn't start with ' '");
        String[] cols = line.substring(1).split(" ");
        for (int column = 0; column < cols.length; column++) {
            asciiGridData.setGridPoint(column,row, Float.parseFloat(cols[column]));
        }
    }


}
