import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@WebServlet(value = "/login")
public class LoginServlet extends HttpServlet {
    private static final String PARAM_USERNAME = "login";
    public static final String COOKIE_USER_ID = "password";
    public static final String PARAM_UID = COOKIE_USER_ID;

    private int cookieLifeTime = 30;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter(PARAM_USERNAME);
        String pas = Hasher.encryptPassword(req.getParameter(COOKIE_USER_ID));
        String nameFromPas = GlobalState.getKeyStorage().getUserNameForPassword(pas);
        if (nameFromPas == null) {
            req.setAttribute("errorMsg", "This user doesn't registrate");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        } else if (nameFromPas.equals(name)) {
            Cookie userIdCookie = new Cookie(COOKIE_USER_ID, pas);
            Cookie userNameCookie = new Cookie(PARAM_USERNAME, name);
            userIdCookie.setMaxAge(cookieLifeTime);
            resp.addCookie(userIdCookie);
            resp.addCookie(userNameCookie);
            //System.out.println("!!Success!!");
            resp.sendRedirect("/homepage.html");
        } else {
            req.setAttribute("errorMsg", "Incorrect password or name");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }
}