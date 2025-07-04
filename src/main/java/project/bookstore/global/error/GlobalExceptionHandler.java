package project.bookstore.global.error;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 모든 컨트롤러에서 발생하는 예외를 이곳에서 처리함
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    // 1) IllegalArgumentException (ex: orElseThrow 등)
    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgument(IllegalArgumentException e, Model model) {
        model.addAttribute("message", e.getMessage());
        return "error/customError"; // resources/templates/error/customError.html
    }

    //IllegalStateException
    @ExceptionHandler(IllegalStateException.class)
    public String handleIllegalState(IllegalStateException e, Model model) {
        model.addAttribute("message", e.getMessage());
        return "error/customError"; // resources/templates/error/customError.html
    }

}
