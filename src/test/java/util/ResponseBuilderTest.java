package util;

import controller.HttpMethod;
import controller.HttpStatusCode;
import data.CookieData;
import data.RequestData;
import data.Response;
import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

public class ResponseBuilderTest {
    @Test
    void buildResponse_shouldBuildCorrectResponse() throws IOException {
        // Given
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        String expectedResponseString = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/html;charset=utf-8\r\n" +
                "Content-Length: 6913\r\n" +
                "Set-Cookie: sid=userId; Max-Age=123; Path=/\r\n" +
                "\r\n" +
                "<!DOCTYPE html>\n" +
                "<html lang=\"kr\">\n" +
                "	<head>\n" +
                "		<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">\n" +
                "		<meta charset=\"utf-8\">\n" +
                "		<title>SLiPP Java Web Programming</title>\n" +
                "		<meta name=\"viewport\" content=\"width=device-width, initial-scale=1, maximum-scale=1\">\n" +
                "		<link href=\"css/bootstrap.min.css\" rel=\"stylesheet\">\n" +
                "		<!--[if lt IE 9]>\n" +
                "			<script src=\"//html5shim.googlecode.com/svn/trunk/html5.js\"></script>\n" +
                "		<![endif]-->\n" +
                "		<link href=\"css/styles.css\" rel=\"stylesheet\">\n" +
                "	</head>\n" +
                "	\n" +
                "	<body>\n" +
                "<nav class=\"navbar navbar-fixed-top header\">\n" +
                " \t<div class=\"col-md-12\">\n" +
                "        <div class=\"navbar-header\">\n" +
                "\n" +
                "            <a href=\"index.html\" class=\"navbar-brand\">SLiPP</a>\n" +
                "          <button type=\"button\" class=\"navbar-toggle\" data-toggle=\"collapse\" data-target=\"#navbar-collapse1\">\n" +
                "          <i class=\"glyphicon glyphicon-search\"></i>\n" +
                "          </button>\n" +
                "      \n" +
                "        </div>\n" +
                "        <div class=\"collapse navbar-collapse\" id=\"navbar-collapse1\">\n" +
                "          <form class=\"navbar-form pull-left\">\n" +
                "              <div class=\"input-group\" style=\"max-width:470px;\">\n" +
                "                <input type=\"text\" class=\"form-control\" placeholder=\"Search\" name=\"srch-term\" id=\"srch-term\">\n" +
                "                <div class=\"input-group-btn\">\n" +
                "                  <button class=\"btn btn-default btn-primary\" type=\"submit\"><i class=\"glyphicon glyphicon-search\"></i></button>\n" +
                "                </div>\n" +
                "              </div>\n" +
                "          </form>\n" +
                "          <ul class=\"nav navbar-nav navbar-right\">             \n" +
                "             <li>\n" +
                "                <a href=\"#\" class=\"dropdown-toggle\" data-toggle=\"dropdown\"><i class=\"glyphicon glyphicon-bell\"></i></a>\n" +
                "                <ul class=\"dropdown-menu\">\n" +
                "                  <li><a href=\"https://slipp.net\" target=\"_blank\">SLiPP</a></li>\n" +
                "                  <li><a href=\"https://facebook.com\" target=\"_blank\">Facebook</a></li>\n" +
                "                </ul>\n" +
                "             </li>\n" +
                "             <li><a href=\"./user/list.html\"><i class=\"glyphicon glyphicon-user\"></i></a></li>\n" +
                "           </ul>\n" +
                "        </div>\t\n" +
                "     </div>\t\n" +
                "</nav>\n" +
                "<div class=\"navbar navbar-default\" id=\"subnav\">\n" +
                "    <div class=\"col-md-12\">\n" +
                "        <div class=\"navbar-header\">\n" +
                "            <a href=\"#\" style=\"margin-left:15px;\" class=\"navbar-btn btn btn-default btn-plus dropdown-toggle\" data-toggle=\"dropdown\"><i class=\"glyphicon glyphicon-home\" style=\"color:#dd1111;\"></i> Home <small><i class=\"glyphicon glyphicon-chevron-down\"></i></small></a>\n" +
                "            <ul class=\"nav dropdown-menu\">\n" +
                "                <li><a href=\"user/profile.html\"><i class=\"glyphicon glyphicon-user\" style=\"color:#1111dd;\"></i> Profile</a></li>\n" +
                "                <li class=\"nav-divider\"></li>\n" +
                "                <li><a href=\"#\"><i class=\"glyphicon glyphicon-cog\" style=\"color:#dd1111;\"></i> Settings</a></li>\n" +
                "            </ul>\n" +
                "            \n" +
                "            <button type=\"button\" class=\"navbar-toggle\" data-toggle=\"collapse\" data-target=\"#navbar-collapse2\">\n" +
                "            	<span class=\"sr-only\">Toggle navigation</span>\n" +
                "            	<span class=\"icon-bar\"></span>\n" +
                "            	<span class=\"icon-bar\"></span>\n" +
                "            	<span class=\"icon-bar\"></span>\n" +
                "            </button>            \n" +
                "        </div>\n" +
                "        <div class=\"collapse navbar-collapse\" id=\"navbar-collapse2\">\n" +
                "            <ul class=\"nav navbar-nav navbar-right\">\n" +
                "                <li class=\"active\"><a href=\"index.html\">Posts</a></li>\n" +
                "                <li><a href=\"user/login.html\" role=\"button\">로그인</a></li>\n" +
                "                <li><a href=\"user/form.html\" role=\"button\">회원가입</a></li>\n" +
                "                <!--\n" +
                "                <li><a href=\"#loginModal\" role=\"button\" data-toggle=\"modal\">로그인</a></li>\n" +
                "                <li><a href=\"#registerModal\" role=\"button\" data-toggle=\"modal\">회원가입</a></li>\n" +
                "                -->\n" +
                "                <li><a href=\"/user/logout\" role=\"button\">로그아웃</a></li>\n" +
                "                <li><a href=\"#\" role=\"button\">개인정보수정</a></li>\n" +
                "            </ul>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</div>\n" +
                "\n" +
                "<div class=\"container\" id=\"main\">\n" +
                "   <div class=\"col-md-12 col-sm-12 col-lg-10 col-lg-offset-1\">\n" +
                "      <div class=\"panel panel-default qna-list\">\n" +
                "          <ul class=\"list\">\n" +
                "              <li>\n" +
                "                  <div class=\"wrap\">\n" +
                "                      <div class=\"main\">\n" +
                "                          <strong class=\"subject\">\n" +
                "                              <a href=\"./qna/show.html\">국내에서 Ruby on Rails와 Play가 활성화되기 힘든 이유는 뭘까?</a>\n" +
                "                          </strong>\n" +
                "                          <div class=\"auth-info\">\n" +
                "                              <i class=\"icon-add-comment\"></i>\n" +
                "                              <span class=\"time\">2016-01-15 18:47</span>\n" +
                "                              <a href=\"./user/profile.html\" class=\"author\">자바지기</a>\n" +
                "                          </div>\n" +
                "                          <div class=\"reply\" title=\"댓글\">\n" +
                "                              <i class=\"icon-reply\"></i>\n" +
                "                              <span class=\"point\">8</span>\n" +
                "                          </div>\n" +
                "                      </div>\n" +
                "                  </div>\n" +
                "              </li>\n" +
                "              <li>\n" +
                "                  <div class=\"wrap\">\n" +
                "                      <div class=\"main\">\n" +
                "                          <strong class=\"subject\">\n" +
                "                              <a href=\"./qna/show.html\">runtime 에 reflect 발동 주체 객체가 뭔지 알 방법이 있을까요?</a>\n" +
                "                          </strong>\n" +
                "                          <div class=\"auth-info\">\n" +
                "                              <i class=\"icon-add-comment\"></i>\n" +
                "                              <span class=\"time\">2016-01-05 18:47</span>\n" +
                "                              <a href=\"./user/profile.html\" class=\"author\">김문수</a>\n" +
                "                          </div>\n" +
                "                          <div class=\"reply\" title=\"댓글\">\n" +
                "                              <i class=\"icon-reply\"></i>\n" +
                "                              <span class=\"point\">12</span>\n" +
                "                          </div>\n" +
                "                      </div>\n" +
                "                  </div>\n" +
                "              </li>\n" +
                "          </ul>\n" +
                "          <div class=\"row\">\n" +
                "              <div class=\"col-md-3\"></div>\n" +
                "              <div class=\"col-md-6 text-center\">\n" +
                "                  <ul class=\"pagination center-block\" style=\"display:inline-block;\">\n" +
                "                      <li><a href=\"#\">«</a></li>\n" +
                "                      <li><a href=\"#\">1</a></li>\n" +
                "                      <li><a href=\"#\">2</a></li>\n" +
                "                      <li><a href=\"#\">3</a></li>\n" +
                "                      <li><a href=\"#\">4</a></li>\n" +
                "                      <li><a href=\"#\">5</a></li>\n" +
                "                      <li><a href=\"#\">»</a></li>\n" +
                "                </ul>\n" +
                "              </div>\n" +
                "              <div class=\"col-md-3 qna-write\">\n" +
                "                  <a href=\"./qna/form.html\" class=\"btn btn-primary pull-right\" role=\"button\">질문하기</a>\n" +
                "              </div>\n" +
                "          </div>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</div>\n" +
                "\n" +
                "<!-- script references -->\n" +
                "<script src=\"js/jquery-2.2.0.min.js\"></script>\n" +
                "<script src=\"js/bootstrap.min.js\"></script>\n" +
                "<script src=\"js/scripts.js\"></script>\n" +
                "	</body>\n" +
                "</html>";

        HttpStatusCode statusCode = HttpStatusCode.OK;
        String path = "/index.html";
        CookieData cookieData = new CookieData("userId", 123);

        Map<String, String> headers = new HashMap<>();
        headers.put("Cookie", "sid=userId");
        RequestData requestData = new RequestData(HttpMethod.GET, "/index.html", "HTTP/1.1", headers, true);

        Response response = new Response(statusCode, path, cookieData);

        // When
        DataOutputStream actualDos = ResponseBuilder.buildResponse(byteArrayOutputStream, response, requestData);

        // Then
        assertThat(actualDos).isNotNull();

        String actualDosStr = byteArrayOutputStream.toString();

        assertThat(actualDosStr).isEqualTo(expectedResponseString);
    }
}
