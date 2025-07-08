package project.bookstore.member.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import project.bookstore.member.dto.SignupForm;
import project.bookstore.member.service.MemberService;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /***
     * 회원가입 폼 화면
     * @param model
     * @return
     */
    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("signupForm", new SignupForm());
        return "member/signup";
    }


    /***
     * 회원가입 처리
     * @param form
     * @param result
     * @return
     */
    @PostMapping("/signup")
    public String signup(@Valid SignupForm form, BindingResult result) {
        //유효성 검증 실패 시 다시 폼으로
        if (result.hasErrors()) {
            return "member/signup";
        }

        try {
            memberService.signup(form);
        } catch (IllegalArgumentException e) {
            //이메일 중복 에러 처리
            result.rejectValue("email", "duplicate", e.getMessage());
            return "member/signup";
        }

        return "redirect:/login";
    }

    //로그인
    @GetMapping("/login")
    public String loginForm(HttpServletRequest request, Model model) {
        // 로그인 실패 시 예외 메시지 세션에서 꺼내기
        Exception exception = (Exception) request.getSession().getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        String errorMsg = null;
        if (exception != null) {
            if (exception instanceof BadCredentialsException) {
                errorMsg = "아이디 또는 비밀번호가 잘못되었습니다.";
            } else if (exception instanceof LockedException) {
                errorMsg = "계정이 정지(잠김) 상태입니다. 관리자에게 문의하세요.";
            } else if (exception instanceof DisabledException) {
                errorMsg = "계정이 비활성화되어 있습니다.";
            } else {
                errorMsg = exception.getMessage();
            }
        }
        model.addAttribute("errorMsg", errorMsg);

        return "member/login";
    }
}
