import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by designer01 on 6/1/17.
 */
public class FetchImage {
    private String localUrl;
    private String remoteUrl;

    public static void main(String[] args) {
        FetchImage fi = new FetchImage("document/images/275357948.jpg", "http://img.alicdn.com/bao/uploaded/i3/275357948/TB2CDw.qYtlpuFjSspoXXbcDpXa_!!275357948.jpg_240x240.jpg");
        fi.run();
    }

    public FetchImage (String localUrl, String remoteUrl) {
        this.localUrl = localUrl;
        this.remoteUrl = remoteUrl;
    }

    public FetchImage () {
    }

    public String getLocalUrl() {
        return localUrl;
    }

    public void setLocalUrl(String localUrl) {
        this.localUrl = localUrl;
    }

    public String getRemoteUrl() {
        return remoteUrl;
    }

    public void setRemoteUrl(String remoteUrl) {
        this.remoteUrl = remoteUrl;
    }

    public void run() {
        URL rmUrl = null;
        InputStream is = null;
        OutputStream os = null;
        try {
            rmUrl = new URL(this.remoteUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            is = rmUrl.openStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        File localFile = new File(this.localUrl);
        try {
            os = new FileOutputStream(localFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        byte[] buff = new byte[1024];
        while (true) {
            int readed = 0;
            try {
                readed = is.read(buff);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (readed == -1) {
                break;
            }

            byte[] temp = new byte[readed];
            System.arraycopy(buff, 0, temp, 0, readed);
            try {
                os.write(temp);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
