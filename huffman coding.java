package huffman;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;

import org.w3c.dom.Node;

/**
 * This class contains methods which, when used together, perform the
 * entire Huffman Coding encoding and decoding process
 * 
 * @author Ishaan Ivaturi
 * @author Prince Rawal
 */
public class HuffmanCoding {
    private String fileName;
    private ArrayList<CharFreq> sortedCharFreqList;
    private TreeNode huffmanRoot;
    private String[] encodings;

    /**
     * Constructor used by the driver, sets filename
     * DO NOT EDIT
     * @param f The file we want to encode
     */
    public HuffmanCoding(String f) { 
        fileName = f; 
    }

    /**
     * Reads from filename character by character, and sets sortedCharFreqList
     * to a new ArrayList of CharFreq objects with frequency > 0, sorted by frequency
     */
    public void makeSortedList() {
        StdIn.setFile(fileName);
        int[] huff = new int[128];
        sortedCharFreqList = new ArrayList<>();
        double dan = 0;

        while(StdIn.hasNextChar()){
            char num = StdIn.readChar();
            huff[num]++;
            dan++;
        }
        for(int i = 0; i< huff.length;i++){
            if(huff[i] > 0){
                double dan1 = huff[i]/dan;
                char converter = (char)i;
                CharFreq cardan = new CharFreq(converter, dan1);
                sortedCharFreqList.add(cardan);
        }        
        }
        if(sortedCharFreqList.size()==1){
            int b = sortedCharFreqList.get(0).getCharacter()+1;
            char c = (char)b;
            CharFreq d = new CharFreq(c,0);
            sortedCharFreqList.add(d);
        }
        Collections.sort(sortedCharFreqList);
    }

    /**
     * Uses sortedCharFreqList to build a huffman coding tree, and stores its root
     * in huffmanRoot
     * @return 
     */
    public TreeNode makeTree() {
            Queue <TreeNode> source = new Queue<>();
            Queue <TreeNode> target = new Queue<>();
            Queue<TreeNode> nqueue = new Queue<>();
            for(int i = 0; i<sortedCharFreqList.size(); i++){
                source.enqueue(new TreeNode(sortedCharFreqList.get(i), null, null));
            }
            double dann1 = 0;
            double dann2 = 0;

            TreeNode low = new TreeNode();
            TreeNode slow = new TreeNode();


            while(!source.isEmpty()   ||   target.size()  !=1  ){
                while(nqueue.size()<2){
                    if(target.isEmpty()){
                        nqueue.enqueue(source.dequeue());
                    }else{
                         if(!source.isEmpty()){
                            if(  source.peek().getData().getProbOcc()   <=   target.peek().getData().getProbOcc()){
                                nqueue.enqueue(source.dequeue());
                        }else{
                            nqueue.enqueue(target.dequeue());
                        }
                    }else{
                        nqueue.enqueue(target.dequeue());
                    }
                }
            }
                
            if(nqueue!=null){
                low = nqueue.dequeue();
                dann1 = low.getData().getProbOcc();
            }
            if(nqueue!=null){
                slow=nqueue.dequeue();
                dann2=slow.getData().getProbOcc(); 

                }
            double total = dann1+ dann2;
            TreeNode ntree  = new TreeNode(new CharFreq(null,total),low, slow);
            target.enqueue((ntree));
            }

            huffmanRoot=target.dequeue();



            return huffmanRoot; 
    }
        
        
    
        


    /**
     * Uses huffmanRoot to create a string array of size 128, where each
     * index in the array contains that ASCII character's bitstring encoding. Characters not
     * present in the huffman coding tree should have their spots in the array left null.
     * Set encodings to this array.
     */
    public void makeEncodings() {
 
        encodings = new String[128];
        
        ArrayList<String>  attar = new    ArrayList<>();
        ArrayList<Character>  attar1 = new  ArrayList<>();
        
        TreeNode  lion =   new TreeNode();

        lion =   huffmanRoot;
        String   str ="";
        gothrough(lion,   str,    attar,     attar1);

        for(int p = 0; p<attar.size(); p++){
            int k= (int)attar1.get(p);
            encodings[k]=attar.get(p);
            
            
        }
        
        }
    
    
    private void gothrough(TreeNode imp, String str, ArrayList a1, ArrayList a2){

        if(imp !=   null&& imp.getLeft()   ==  null   && imp.getRight()   ==   null   &&imp.getData().getCharacter()   !=null   ){
            a1.add(str);
            a2.add(imp.getData().getCharacter());
            return;
        }
        if(imp !=   null){
            gothrough(imp.getLeft(),str+"0",    a1,    a2);
            gothrough(imp.getRight(),str+ "1",  a1,      a2);

        }
        
        
    
        
      
    }




    /**
     * Using encodings and filename, this method makes use of the writeBitString method
     * to write the final encoding of 1's and 0's to the encoded file.
     * 
     * @param encodedFile The file name into which the text file is to be encoded
     */
    public void encode(String encodedFile) {
          StdIn.setFile(fileName);
          String   compressed =  "";
          while(StdIn.hasNextChar()){
            char reading =   StdIn.readChar();
            String dan = encodings[reading];
            compressed +=  dan;

          }
          writeBitString(encodedFile, compressed);
        
    }
    /**
     * Writes a given string of 1's and 0's to the given file byte by byte
     * and NOT as characters of 1 and 0 which take up 8 bits each
     * DO NOT EDIT
     * 
     * @param filename The file to write to (doesn't need to exist yet)
     * @param bitString The string of 1's and 0's to write to the file in bits
     */
    public static void writeBitString(String filename, String bitString) {
        byte[] bytes = new byte[bitString.length() / 8 + 1];
        int bytesIndex = 0, byteIndex = 0, currentByte = 0;

        // Pad the string with initial zeroes and then a one in order to bring
        // its length to a multiple of 8. When reading, the 1 signifies the
        // end of padding.
        int padding = 8 - (bitString.length() % 8);
        String pad = "";
        for (int i = 0; i < padding-1; i++) pad = pad + "0";
        pad = pad + "1";
        bitString = pad + bitString;

        // For every bit, add it to the right spot in the corresponding byte,
        // and store bytes in the array when finished
        for (char c : bitString.toCharArray()) {
            if (c != '1' && c != '0') {
                System.out.println("Invalid characters in bitstring");
                return;
            }

            if (c == '1') currentByte += 1 << (7-byteIndex);
            byteIndex++;
            
            if (byteIndex == 8) {
                bytes[bytesIndex] = (byte) currentByte;
                bytesIndex++;
                currentByte = 0;
                byteIndex = 0;
            }
        }
        
        // Write the array of bytes to the provided file
        try {
            FileOutputStream out = new FileOutputStream(filename);
            out.write(bytes);
            out.close();
        }
        catch(Exception e) {
            System.err.println("Error when writing to file!");
        }
    }

    /**
     * Using a given encoded file name, this method makes use of the readBitString method 
     * to convert the file into a bit string, then decodes the bit string using the 
     * tree, and writes it to a decoded file. 
     * 
     * @param encodedFile The file which has already been encoded by encode()
     * @param decodedFile The name of the new file we want to decode into
     */
    public void decode(String encodedFile,String decodedFile) {
        
        StdOut.setFile(decodedFile);
        char dan = ' ';
          String   reader =   readBitString(encodedFile);
          //decodedFile = "   ";
          while(reader.length()  != 0){
                TreeNode dantree = huffmanRoot;
                int dan2 = 0;
            while(dantree.getData().getCharacter() == null){
                
                  if(reader.substring(dan2, dan2+1).equals("0")){
                    dantree = dantree.getLeft();
                }
                  else{
                    dantree = dantree.getRight();
                }
                  dan2++;

            }
            
            reader = reader.substring(dan2);

            StdOut.print(dantree.getData().getCharacter()); 
            
          }
          

          


}

    /**
     * Reads a given file byte by byte, and returns a string of 1's and 0's
     * representing the bits in the file
     * DO NOT EDIT
     * 
     * @param filename The encoded file to read from
     * @return String of 1's and 0's representing the bits in the file
     */
    public static String readBitString(String filename) {
        String bitString = "";
        
        try {
            FileInputStream in = new FileInputStream(filename);
            File file = new File(filename);

            byte bytes[] = new byte[(int) file.length()];
            in.read(bytes);
            in.close();
            
            // For each byte read, convert it to a binary string of length 8 and add it
            // to the bit string
            for (byte b : bytes) {
                bitString = bitString + 
                String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
            }

            // Detect the first 1 signifying the end of padding, then remove the first few
            // characters, including the 1
            for (int i = 0; i < 8; i++) {
                if (bitString.charAt(i) == '1') return bitString.substring(i+1);
            }
            
            return bitString.substring(8);
        }
        catch(Exception e) {
            System.out.println("Error while reading file!");
            return "";
        }
    }

    /*
     * Getters used by the driver. 
     * DO NOT EDIT or REMOVE
     */

    public String getFileName() { 
        return fileName; 
    }

    public ArrayList<CharFreq> getSortedCharFreqList() { 
        return sortedCharFreqList; 
    }

    public TreeNode getHuffmanRoot() { 
        return huffmanRoot; 
    }

    public String[] getEncodings() { 
        return encodings; 
    }
}
