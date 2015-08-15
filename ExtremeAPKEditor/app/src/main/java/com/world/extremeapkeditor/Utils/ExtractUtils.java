package com.world.extremeapkeditor.Utils;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Formatter;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Created by abdulazizniazi on 7/1/15.
 */
public class ExtractUtils {
    Formatter formatter;
    int BUFFER_SIZE = 1024;
    private String outputXML = "";

    public void unzip(String zipFile, String location) throws IOException {
        int size;
        byte[] buffer = new byte[BUFFER_SIZE];

        try {
            if (!location.endsWith("/")) {
                location += "/";
            }
            File f = new File(location);
            if (!f.isDirectory()) {
                f.mkdirs();
            }
            ZipInputStream zin = new ZipInputStream(new BufferedInputStream(new FileInputStream(zipFile), BUFFER_SIZE));
            try {
                ZipEntry ze;
                while ((ze = zin.getNextEntry()) != null) {
                    String path = location + ze.getName();
                    File unzipFile = new File(path);

                    if (ze.isDirectory()) {
                        if (!unzipFile.isDirectory()) {
                            unzipFile.mkdirs();
                        }
                    } else {
                        // check for and create parent directories if they don't exist
                        File parentDir = unzipFile.getParentFile();
                        if (null != parentDir) {
                            if (!parentDir.isDirectory()) {
                                parentDir.mkdirs();
                            }
                        }

                        // unzip the file
                        FileOutputStream out = new FileOutputStream(unzipFile, false);
                        BufferedOutputStream fout = new BufferedOutputStream(out, BUFFER_SIZE);
                        try {
                            while ((size = zin.read(buffer, 0, BUFFER_SIZE)) != -1) {
                                fout.write(buffer, 0, size);
                            }

                            zin.closeEntry();
                        } finally {
                            fout.flush();
                            fout.close();
                        }
                    }
                }
            } finally {
                zin.close();

            }
        } catch (Exception e) {
            Log.e("Exception", "Unzip exception", e);

        }
    }


    public void zip(String[] _files, String zipFileName) {
        try {
            BufferedInputStream origin = null;
            FileOutputStream dest = new FileOutputStream(zipFileName);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(
                    dest));
            byte data[] = new byte[BUFFER_SIZE];

            for (int i = 0; i < _files.length; i++) {
                Log.v("Compress", "Adding: " + _files[i]);
                FileInputStream fi = new FileInputStream(_files[i]);
                origin = new BufferedInputStream(fi, BUFFER_SIZE);

                ZipEntry entry = new ZipEntry(_files[i].substring(_files[i].lastIndexOf("/") + 1));
                out.putNextEntry(entry);
                int count;

                while ((count = origin.read(data, 0, BUFFER_SIZE)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
            }

            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String getIntents(String path, File[] layouts) {
        FileInputStream fileInputStream = null;
        try {
            JarFile jf = new JarFile(path);
            InputStream is = jf.getInputStream(jf.getEntry("AndroidManifest.xml"));
            byte[] xml = new byte[is.available()];
            int br = is.read(xml);
            Log.d("TAG2", ">" + layouts[0].getParentFile().getParentFile().getAbsolutePath());
            //Tree tr = TrunkFactory.newTree();
            decompressXML(xml);
            is.close();
            formatter = new Formatter(layouts[0].getParentFile().getParentFile().getParentFile().getAbsolutePath() + "/AndroidManifest.xml");
            formatter.format(outputXML);
            formatter.close();
            Log.d("TAGTAG", is.available() + "" + layouts[0].getAbsolutePath().substring(layouts[0].getAbsolutePath().lastIndexOf("/") + 1));
//            for (int i=0;i<layouts.length;i++){
//                File file = layouts[i];
//
//                try{
//                    InputStream inputStream= jf.getInputStream(jf.getEntry("res/layout/"+layouts[0].getAbsolutePath().substring(layouts[0].getAbsolutePath().lastIndexOf("/")+1)));
//                    byte[] fi = new byte[inputStream.available()];
//                    int ad = inputStream.read(fi);
//                    decompressXML(fi);
//                    fileInputStream.close();
//                }catch (Exception e){
//                    Log.d("TAG","fileinputStream error",e);
//                    e.printStackTrace();
//                }
//
////                Log.d("TAGy","file >> "+file);
////                Log.d("TAGy","layouts length > "+layouts.length);
////                String pa = layouts[i].getAbsolutePath().substring(layouts[i].getAbsolutePath().lastIndexOf("/")+1);
////                byte[] f = pa.getBytes(Charset.forName("UTF-8"));
////                is = jf.getInputStream(jf.getEntry("/res/layout/"+layouts[0].getAbsolutePath().substring(layouts[0].getAbsolutePath().lastIndexOf("/")+1)));
////                Log.d("TAGy","inputStream iss >> "+is);
////
////                if (is != null){
////                    xml = new byte[is.available()];
////                    int ar = is.read(xml);
////                    decompressXML(xml);
////                    formatter = new Formatter(file);
////                    formatter.format(outputXML);
////                    formatter.close();
////                }
//            }
            //prt("XML\n"+tr.list());
        } catch (Exception ex) {
            System.out.print("getIntents, ex: " + ex);
            ex.printStackTrace();
        }
        return outputXML;
    }

    public String decode(byte[] path) {
        decompressXML(path);
        return outputXML;
    }


    public static int endDocTag = 0x00100101;
    public static int startTag = 0x00100102;
    public static int endTag = 0x00100103;

    public void decompressXML(byte[] xml) {
// Compressed XML file/bytes starts with 24x bytes of data,
// 9 32 bit words in little endian order (LSB first):
//   0th word is 03 00 08 00
//   3rd word SEEMS TO BE:  Offset at then of StringTable
//   4th word is: Number of strings in string table
// WARNING: Sometime I indiscriminently display or refer to word in
//   little endian storage format, or in integer format (ie MSB first).
        int numbStrings = LEW(xml, 4 * 4);

// StringIndexTable starts at offset 24x, an array of 32 bit LE offsets
// of the length/string data in the StringTable.
        int sitOff = 0x24;  // Offset of start of StringIndexTable

// StringTable, each string is represented with a 16 bit little endian
// character count, followed by that number of 16 bit (LE) (Unicode) chars.
        int stOff = sitOff + numbStrings * 4;  // StringTable follows StrIndexTable

// XMLTags, The XML tag tree starts after some unknown content after the
// StringTable.  There is some unknown data after the StringTable, scan
// forward from this point to the flag for the start of an XML start tag.
        int xmlTagOff = LEW(xml, 3 * 4);  // Start from the offset in the 3rd word.
// Scan forward until we find the bytes: 0x02011000(x00100102 in normal int)
        for (int ii = xmlTagOff; ii < xml.length - 4; ii += 4) {
            if (LEW(xml, ii) == startTag) {
                xmlTagOff = ii;
                break;
            }
        } // end of hack, scanning for start of first start tag

// XML tags and attributes:
// Every XML start and end tag consists of 6 32 bit words:
//   0th word: 02011000 for startTag and 03011000 for endTag
//   1st word: a flag?, like 38000000
//   2nd word: Line of where this tag appeared in the original source file
//   3rd word: FFFFFFFF ??
//   4th word: StringIndex of NameSpace name, or FFFFFFFF for default NS
//   5th word: StringIndex of Element Name
//   (Note: 01011000 in 0th word means end of XML document, endDocTag)

// Start tags (not end tags) contain 3 more words:
//   6th word: 14001400 meaning??
//   7th word: Number of Attributes that follow this tag(follow word 8th)
//   8th word: 00000000 meaning??

// Attributes consist of 5 words:
//   0th word: StringIndex of Attribute Name's Namespace, or FFFFFFFF
//   1st word: StringIndex of Attribute Name
//   2nd word: StringIndex of Attribute Value, or FFFFFFF if ResourceId used
//   3rd word: Flags?
//   4th word: str ind of attr value again, or ResourceId of value

// TMP, dump string table to tr for debugging
//tr.addSelect("strings", null);
//for (int ii=0; ii<numbStrings; ii++) {
//  // Length of string starts at StringTable plus offset in StrIndTable
//  String str = compXmlString(xml, sitOff, stOff, ii);
//  tr.add(String.valueOf(ii), str);
//}
//tr.parent();

// Step through the XML tree element tags and attributes
        int off = xmlTagOff;
        int indent = 0;
        int startTagLineNo = -2;
        while (off < xml.length) {
            int tag0 = LEW(xml, off);
            //int tag1 = LEW(xml, off+1*4);
            int lineNo = LEW(xml, off + 2 * 4);
            //int tag3 = LEW(xml, off+3*4);
            int nameNsSi = LEW(xml, off + 4 * 4);
            int nameSi = LEW(xml, off + 5 * 4);

            if (tag0 == startTag) { // XML START TAG
                int tag6 = LEW(xml, off + 6 * 4);  // Expected to be 14001400
                int numbAttrs = LEW(xml, off + 7 * 4);  // Number of Attributes to follow
                //int tag8 = LEW(xml, off+8*4);  // Expected to be 00000000
                off += 9 * 4;  // Skip over 6+3 words of startTag data
                String name = compXmlString(xml, sitOff, stOff, nameSi);
                //tr.addSelect(name, null);
                startTagLineNo = lineNo;

                // Look for the Attributes
                StringBuffer sb = new StringBuffer();
                for (int ii = 0; ii < numbAttrs; ii++) {
                    int attrNameNsSi = LEW(xml, off);  // AttrName Namespace Str Ind, or FFFFFFFF
                    int attrNameSi = LEW(xml, off + 1 * 4);  // AttrName String Index
                    int attrValueSi = LEW(xml, off + 2 * 4); // AttrValue Str Ind, or FFFFFFFF
                    int attrFlags = LEW(xml, off + 3 * 4);
                    int attrResId = LEW(xml, off + 4 * 4);  // AttrValue ResourceId or dup AttrValue StrInd
                    off += 5 * 4;  // Skip over the 5 words of an attribute

                    String attrName = compXmlString(xml, sitOff, stOff, attrNameSi);
                    String attrValue = attrValueSi != -1
                            ? compXmlString(xml, sitOff, stOff, attrValueSi)
                            : "resourceID 0x" + Integer.toHexString(attrResId);
                    sb.append(" " + attrName + "=\"" + attrValue + "\"");
                    //tr.add(attrName, attrValue);
                }
                prtIndent(indent, "<" + name + sb + ">\n");
                indent++;

            } else if (tag0 == endTag) { // XML END TAG
                indent--;
                off += 6 * 4;  // Skip over 6 words of endTag data
                String name = compXmlString(xml, sitOff, stOff, nameSi);

//                prtIndent(indent, "</"+name+">  (line "+startTagLineNo+"-"+lineNo+")");

                prtIndent(indent, "</" + name + ">\n"); //line above this adds (line #) So I commented it out
                //tr.parent();  // Step back up the NobTree

            } else if (tag0 == endDocTag) {  // END OF XML DOC TAG
                break;

            } else {
                Log.d("TAG", "  Unrecognized tag code '" + Integer.toHexString(tag0)
                        + "' at offset " + off);
                break;
            }
        } // end of while loop scanning tags and attributes of XML tree
        Log.d("TAG", "    end at offset " + off);
        Log.d("TAG", outputXML);
    } // end of decompressXML


    public String compXmlString(byte[] xml, int sitOff, int stOff, int strInd) {
        if (strInd < 0) return null;
        int strOff = stOff + LEW(xml, sitOff + strInd * 4);
        return compXmlStringAt(xml, strOff);
    }


    public static String spaces = "                                             ";

    public void prtIndent(int indent, String str) {
        Log.d("TAG123", spaces.substring(0, Math.min(indent * 2, spaces.length())) + str);
        outputXML += spaces.substring(0, Math.min(indent * 2, spaces.length())) + str;
    }


    // compXmlStringAt -- Return the string stored in StringTable format at
// offset strOff.  This offset points to the 16 bit string length, which
// is followed by that number of 16 bit (Unicode) chars.
    public String compXmlStringAt(byte[] arr, int strOff) {
        int strLen = arr[strOff + 1] << 8 & 0xff00 | arr[strOff] & 0xff;
        byte[] chars = new byte[strLen];
        for (int ii = 0; ii < strLen; ii++) {
//            try {

                    chars[ii] = arr[strOff + 2 + ii * 2];

//            } catch (Exception r) {
//                Log.d("TAG0", "char >" + chars.length + " arr > " + arr.length + " val " + strOff + 2 + ii * 2);
//                r.printStackTrace();
//            }
//            }
        }
        return new String(chars);  // Hack, just use 8 byte chars
    } // end of compXmlStringAt


    // LEW -- Return value of a Little Endian 32 bit word from the byte array
//   at offset off.
    public int LEW(byte[] arr, int off) {
        return arr[off + 3] << 24 & 0xff000000 | arr[off + 2] << 16 & 0xff0000
                | arr[off + 1] << 8 & 0xff00 | arr[off] & 0xFF;
    } // end of LEW


}
