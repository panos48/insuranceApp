import java.io.File;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

//class' purpose is to create an outputFile
//and with .writing method to write inside the exportData
public class ParserWriter {
    //create a file and write inside the exportData
    public void writing(String exportData){
        try {
            //Whatever the file path is.
            File statText = new File("outputFile.csv");
            FileOutputStream is = new FileOutputStream(statText,true);
            OutputStreamWriter osw = new OutputStreamWriter(is);
            Writer w = new BufferedWriter(osw);

            w.write(exportData + "\n");
            w.flush();
            w.close();
        } catch (IOException e) {
            System.err.println("Problem writing to the file CSV.csv");
        }
    }
}