package project.bookstore.global.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 모든 컨트롤러에서 발생하는 예외를 이곳에서 처리함(view전용)
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    // 1) IllegalArgumentException (ex: orElseThrow 등)
    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgument(IllegalArgumentException e, Model model) {
        model.addAttribute("message", e.getMessage());
        return "error/customError";
    }

    //IllegalStateException
    @ExceptionHandler(IllegalStateException.class)
    public String handleIllegalState(IllegalStateException e, Model model) {
        model.addAttribute("message", e.getMessage());
        return "error/customError";
    }

    // 모든 RuntimeException 처리 (ex: 파일 저장 실패 등)
    @ExceptionHandler(RuntimeException.class)
    public String handleRuntime(RuntimeException e, Model model) {
        model.addAttribute("message", e.getMessage()); // 사용자에게 너무 상세한 시스템 메시지는 노출하지 않는게 안전함
        return "error/customError";
    }


    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDenied(AccessDeniedException e, Model model) {
        model.addAttribute("message", e.getMessage());
        return "error/customError";
    }

}
