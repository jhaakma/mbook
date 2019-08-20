package mbook;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import mbook.api.controller.ApiController;

/**
 * Separates error handling between web and API. Web returns error page, API
 * returns the error itself.
 * 
 * @author jh1540
 *
 */
public class ErrorAdvice {
    @ControllerAdvice(basePackageClasses = ApiController.class)
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public class ErrorApiAdvice {
        @ExceptionHandler(Throwable.class)
        public ResponseEntity<Object> catchApiExceptions(Throwable e) {
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
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
