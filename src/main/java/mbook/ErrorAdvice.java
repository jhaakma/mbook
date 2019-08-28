package mbook;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.rest.webmvc.RepositoryRestExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;
import mbook.api.controller.ApiController;

/**
 * Separates error handling between web and api. 
 * Web returns error page, api returns the error itself.
 * 
 * @author jh1540
 *
 */
@Slf4j
@Controller
public class ErrorAdvice {
    @ControllerAdvice(basePackageClasses = ApiController.class)
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public class ErrorApiAdvice {
        @ExceptionHandler(Throwable.class)
        public ResponseEntity<Object> catchApiExceptions(Throwable e) {
            log.info("+++++++Using exception handler");
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @ControllerAdvice
    @Order(Ordered.LOWEST_PRECEDENCE)
    public static class ErrorWebAdvice {
        @ExceptionHandler(Throwable.class)
        public String catchOtherExceptions() {
            return "/error";
        }
    }
}
