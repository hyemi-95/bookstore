package project.bookstore.member.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public String loginForm() {
        return "member/login";
    }
}
