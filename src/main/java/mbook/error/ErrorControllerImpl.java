package mbook.error;

import java.util.Locale;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
import mbook.web.AbstractWebController;

@Controller
@Slf4j
class ErrorControllerImpl extends AbstractWebController implements ErrorController  {
    
    @Autowired
    private MessageSource messageSource;
    
    
    @RequestMapping("/error/api")
    public String handeApiError(Throwable e) {
        log.info("API controller triggered!!!!!!!!!!!!!!!");
        return "error";
    }
    
    @RequestMapping("/error")
    public String handleError(
        HttpServletRequest request,
        Model model
    ) {
        Locale locale = request.getLocale();
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
         
        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());
         
            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                model.addAttribute("message", messageSource.getMessage("errorPage.404", null, locale));
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                model.addAttribute("message", messageSource.getMessage("errorPage.500", null, locale));
            } else {
                model.addAttribute("message", messageSource.getMessage("errorPage.message", null, locale));
            }
        } else {
            model.addAttribute("message", messageSource.getMessage("errorPage.message", null, locale));
        }
        return "error";
    }
 
    @Override
    public String getErrorPath() {
        return "/error";
    }
}
