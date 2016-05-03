package sample;

import javafx.scene.control.Alert;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.List;
import java.util.Map;


public class HttpAnalyzer {
    private HttpURLConnection oHttpConnection;
    private URL url;
    Map<String, List<String>> hdrs;
    public HttpAnalyzer(URL _url) throws IOException {
        try {
            this.url = _url;
            oHttpConnection = (HttpURLConnection) url.openConnection();
            oHttpConnection.setRequestProperty("Connection", "keep-alive");
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Error Dialog");
            alert.setContentText("Ooops, there was an error!");
            alert.showAndWait();
        }
    }
    public StringBuilder getServerInfo() {
        StringBuilder oQ1 = new StringBuilder();
        oQ1.append("Q1 : \n");
        oQ1.append("     Server : " + oHttpConnection.getHeaderField("Server") + "\n");
        return oQ1;
    }
    public StringBuilder getServersMethods() throws IOException {
        boolean optionisImplemented = false;
        Socket s = new Socket(InetAddress.getByName(url.getHost()), 80);
        DataOutputStream pw = new DataOutputStream(s.getOutputStream());
        pw.writeBytes("OPTIONS * HTTP/1.1\n\nHost: " + url.getHost());
        BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
        String t;
        String tt = br.readLine();
        StringBuilder oQ2 = new StringBuilder();
        oQ2.append("Q2 : \n     ");
        if (tt.equals("HTTP/1.1 200 OK")) {
            optionisImplemented = true;
            oQ2.append(tt + "\n");
        }
        while (((t = br.readLine()) != null) && optionisImplemented) {
            oQ2.append("     " + t + "\n");
        }
        if (!optionisImplemented)
            oQ2.append(tt + "\n" + "     OPTIONS isn't implemented by server ... !");
        br.close();
        return oQ2;
    }
    public StringBuilder getCookieInfo() throws IOException {
        URL oUrl = new URL("http://" + url.getHost());
        CookieManager oCookieManager = new CookieManager();
        CookieHandler.setDefault(oCookieManager);
        URLConnection connection = oUrl.openConnection();
        connection.getContent();
        CookieStore oCookieStore = oCookieManager.getCookieStore();
        List<HttpCookie> oListOfCookies = oCookieStore.getCookies();
        StringBuilder oQ3 = new StringBuilder();
        oQ3.append("Q3 :\n");
        for (HttpCookie oCookie : oListOfCookies) {
            oQ3.append("    name : " + oCookie.getName() + "\n");
            oQ3.append("    Domain : " + oCookie.getDomain() + "\n");
            oQ3.append("    path : " + oCookie.getPath() + "\n");
            oQ3.append("    Version : " + oCookie.getVersion() + "\n");
            oQ3.append("    value : " + oCookie.getValue() + "\n");
            oQ3.append("    max_age : " + oCookie.getMaxAge() + "\n");
            oQ3.append("    is secure : " + oCookie.getSecure() + "\n");
            oQ3.append("    http only : " + oCookie.isHttpOnly() + "\n\n");
        }
        return oQ3;
    }
    public StringBuilder getCacheInfo() throws IOException {
        hdrs = oHttpConnection.getHeaderFields();
        StringBuilder oQ4 = new StringBuilder();
        oQ4.append("Q4 :\n");
        oQ4.append("    Expires : " + hdrs.get("Expires") + "\n");
        oQ4.append("    Last-Modified : " + hdrs.get("Last-Modified") + "\n");
        oQ4.append("    Cache-Control : " + hdrs.get("Cache-Control") + "\n");
        oQ4.append("    ETag : " + hdrs.get("ETag") + "\n");
        return oQ4;
    }
    public StringBuilder getAuthenticationInfo() throws IOException {
        hdrs = oHttpConnection.getHeaderFields();
        StringBuilder oQ5 = new StringBuilder();
        oQ5.append("Q5 :\n");
        oQ5.append("      WWW-Authenticate : " + hdrs.get("Proxy-Authenticate") + "\n");
        oQ5.append("      Proxy-Authenticate : " + hdrs.get("Proxy-Authenticate") + "\n");
        return oQ5;
    }
    public StringBuilder getStatusCode() throws IOException {
        StringBuilder q6 = new StringBuilder();
        q6.append("Q6 : \n");
        q6.append("     " + oHttpConnection.getResponseCode() + "  " +
                oHttpConnection.getResponseMessage());
        return q6;
    }

}
