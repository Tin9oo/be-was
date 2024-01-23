package util;

import controller.HttpStatusCode;
import controller.ResourceMapping;
import data.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ResponseBuilder {

    private static final Logger logger = LoggerFactory.getLogger(ResponseBuilder.class);

    public static void buildResponse(OutputStream out, Response response) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);

        HttpStatusCode statusCode = response.getStatus();
        String targetPath = response.getPath();
        String cookie = response.getCookie();

        String extension = RequestParserUtil.getFileExtension(targetPath);
        String contentType = ResourceMapping.valueOf(extension.toUpperCase()).getContentType();

        byte[] body;

        if(statusCode == HttpStatusCode.OK) {
            body = ResourceLoader.loadResource(targetPath);
            response200Header(dos, body.length, contentType);
            responseBody(dos, body);
        } else if(statusCode == HttpStatusCode.FOUND) {
            response302Header(dos, targetPath, cookie);
        } else if (statusCode == HttpStatusCode.BAD_REQUEST) {
            body = ResourceLoader.loadResource(targetPath);
            response400Header(dos);
            responseBody(dos, body);
        } else if (statusCode == HttpStatusCode.NOT_FOUND) {
            body = ResourceLoader.loadResource(targetPath);
            response404Header(dos);
            responseBody(dos, body);
        } else {
            logger.error("지원하지 않는 상태 코드입니다.");
            return;
        }
    }
    private static void response200Header(DataOutputStream dos, int lengthOfBodyContent, String contentType) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + contentType + ";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private static void response302Header(DataOutputStream dos, String redirectLocation) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found\r\n");
            dos.writeBytes("Location: " + redirectLocation + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private static void response302Header(DataOutputStream dos, String redirectLocation, String cookieHeader) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found\r\n");
            dos.writeBytes("Location: " + redirectLocation + "\r\n");

            if(cookieHeader != null && !cookieHeader.isEmpty()) {
                dos.writeBytes("Set-Cookie: sid=" + cookieHeader + "Path=/" + "\r\n");
            }

            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private static void response400Header(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 400 Bad Request\r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private static void response404Header(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 404 Not Found\r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
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
