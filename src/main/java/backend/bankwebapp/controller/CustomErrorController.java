package backend.bankwebapp.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestMapping;

@ControllerAdvice
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handle404Error(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null) {
            String statusCode = status.toString();
            if (statusCode.equals("404")) {
                return "error404";
            }
//            else if (statusCode.equals("400")) {
//                return "error400";  // accessDenied handles WebSecurityConfig
            // return "accessDenied";
//            }
        }
        return "defaultError"; // default error page
    }
}

