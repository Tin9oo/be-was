package util;

import controller.HttpStatusCode;
import controller.ResourceMapping;
import data.CookieData;
import data.RequestData;
import data.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ResponseBuilder {

    private static final Logger logger = LoggerFactory.getLogger(ResponseBuilder.class);

    public static DataOutputStream buildResponse(OutputStream out, Response response, RequestData requestData) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        HttpStatusCode statusCode = response.getStatus();

        byte[] body = buildResponseHeader(dos, response, requestData);
        buildResponseBody(dos, statusCode, body);

        return dos;
    }

    private static byte[] buildResponseHeader(DataOutputStream dos, Response response, RequestData requestData) throws IOException {
        writeResponseStatus(dos, response.getStatus());
        byte[] body = writeContentInfo(dos, response.getPath(), requestData);
        if (response.getStatus() == HttpStatusCode.FOUND) writeRedirectLocation(dos, response.getPath());
        if (response.getCookie() != null || !requestData.isLoggedIn()) writeCookieHeader(dos, response.getCookie(), requestData);
        dos.writeBytes("\r\n"); // eof
        return body;
    }

    private static void writeResponseStatus(DataOutputStream dos, HttpStatusCode statusCode) throws IOException {
        dos.writeBytes("HTTP/1.1 " + statusCode.getStatusCode() + "\r\n");
    }

    private static byte[] writeContentInfo(DataOutputStream dos, String targetPath, RequestData requestData) throws IOException {
        String extension = RequestParserUtil.getFileExtension(targetPath);
        String contentType = ResourceMapping.valueOf(extension.toUpperCase()).getContentType();
        byte[] body = ResourceLoader.loadResource(targetPath, requestData);

        dos.writeBytes("Content-Type: " + contentType + ";charset=utf-8\r\n");
        dos.writeBytes("Content-Length: " + body.length + "\r\n");

        // Cache-Control 헤더 추가
        if (ResourceMapping.HTML != ResourceMapping.valueOf(extension.toUpperCase())) {
            dos.writeBytes("Cache-Control: public, max-age=" + 1 * 60 + "\r\n");
        }

        return body;
    }

    private static void writeRedirectLocation(DataOutputStream dos, String targetPath) throws IOException {
        dos.writeBytes("Location: " + targetPath + "\r\n");
    }

    private static void writeCookieHeader(DataOutputStream dos, CookieData cookieData, RequestData requestData) throws IOException {
        if (cookieData != null) dos.writeBytes("Set-Cookie: " + cookieData.toString() + " Path=/" + "\r\n");
        if (!requestData.isLoggedIn()) dos.writeBytes("Set-Cookie: " + requestData.getHeaderValue("Cookie") + "; Max-Age=0; Path=/;\r\n");
    }

    private static void buildResponseBody(DataOutputStream dos, HttpStatusCode statusCode, byte[] body) throws IOException {
        if (statusCode != HttpStatusCode.FOUND) {
            responseBody(dos, body);
        }
    }

    private static void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }


}
