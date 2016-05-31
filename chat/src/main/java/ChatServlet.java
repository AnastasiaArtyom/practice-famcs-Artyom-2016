import com.sun.net.httpserver.HttpExchange;
import org.json.simple.parser.ParseException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@WebServlet(value = "/chat")
public class ChatServlet extends HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        // System.out.println("WOW");
        String query = req.getQueryString();
        //System.out.println(query);
        System.out.println("!!Success!! Get " + query);

        if (query == null) {
            ;
            error400(resp, "Absent query in request");
            //return Response.badRequest("Absent query in request");
        }
        Map<String, String> map = queryToMap(query);

        String token = map.get(Constants.REQUEST_PARAM_TOKEN);
        //loggerFile.info("Token: " + token);
        if (StringUtils.isEmpty(token)) {
            error400(resp, "Token query parameter is required");
            //return Response.badRequest("Token query parameter is required");
        }

        try {

            int index = MessageHelper.parseToken(token);
            if (index > GlobalState.getMessageStorage().size()) {
                error400(resp, "Incorrect token in request");
                //return Response.badRequest(
                //        String.format("Incorrect token in request: %s. Server does not have so many messages", token));
            }
            Portion portion = new Portion(index);
            List<Message> messages = GlobalState.getMessageStorage().getPortion(portion);
            String responseBody = MessageHelper.buildServerResponseBody(messages,
                    GlobalState.getMessageStorage().size());
            PrintWriter out = resp.getWriter();
            out.println(responseBody);
            out.flush();
        } catch (InvalidTokenException e) {
            error400(resp, e.toString());
        }
        catch (IOException e) {
            error400(resp, e.toString());
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {

        try {
            System.out.println("!!Success!! Post");

            Message message = MessageHelper.getPostMessage(req.getInputStream());
            System.out.println("Author = " + message.getAuthor());
            //System.out.println("Hello!!!!!!");
            //logger.info(String.format("Received new message from user: %s", message));
            GlobalState.getMessageStorage().addMessage(message);
            //return Response.ok();
        } catch (ParseException e) {
            //logger.error("Could not parse message.", e);
            error400(resp, "Incorrect request body");
            //return new Response(Constants.RESPONSE_CODE_BAD_REQUEST, "Incorrect request body");
        }
        catch (IOException e){
            error400(resp, e.toString());
        }
    }

    protected Response doPut(HttpExchange httpExchange) {

        try {

            Message message = MessageHelper.getPutMessage(httpExchange.getRequestBody());
            System.out.println("our message " + message.getText());
            //logger.info(String.format("Received new message from user: %s", message));
            GlobalState.getMessageStorage().addMessage(message);
            return Response.ok();
        } catch (ParseException e) {
            //logger.error("Could not parse message.", e);
            return new Response(Constants.RESPONSE_CODE_BAD_REQUEST, "Incorrect request body");
        }
    }

    protected Response doDelete(HttpExchange httpExchange) {
        try {
            Message message = MessageHelper.getDelMessage(httpExchange.getRequestBody());
            //logger.info(String.format("Received new message from user: %s", message));
            GlobalState.getMessageStorage().addMessage(message);
            return Response.ok();
        } catch (ParseException e) {
            //logger.error("Could not parse message.", e);
            return new Response(Constants.RESPONSE_CODE_BAD_REQUEST, "Incorrect request body");
        }
    }

    protected Response doOptions(HttpExchange httpExchange) {
        httpExchange.getResponseHeaders().add(Constants.REQUEST_HEADER_ACCESS_CONTROL_METHODS,
                Constants.HEADER_VALUE_ALL_METHODS);
        return Response.ok();
    }

    private Map<String, String> queryToMap(String query) {
        Map<String, String> result = new HashMap<>();

        for (String queryParam : query.split(Constants.REQUEST_PARAMS_DELIMITER)) {
            String paramKeyValuePair[] = queryParam.split("=");
            if (paramKeyValuePair.length > 1) {
                result.put(paramKeyValuePair[0], paramKeyValuePair[1]);
            } else {
                result.put(paramKeyValuePair[0], "");
            }
        }
        return result;
    }

    private static void error400(HttpServletResponse resp, String msg) {
        try {
            resp.sendError(400, msg);
        }
        catch(IOException _){
            System.out.println("Broken! broken! broken!");
        }
    }

    private static void error400(HttpServletResponse resp) {
        error400(resp, "Bad request");
    }
}
