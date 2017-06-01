import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by designer01 on 6/1/17.
 */
public class WriteFile {
    private String file;
    private String data;

    public static void main(String[] args) {
    }

    public WriteFile (String file, String data) {
        this.file = file;
        this.data = data;
    }

    public WriteFile() {
    };

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "WriteFile{" +
                "file='" + file + '\'' +
                ", data='" + data + '\'' +
                '}';
    }

    public void run () {
        File f = null;
        try {
            f = new File(this.file);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        try {
            FileUtils.writeStringToFile(f, this.data, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
