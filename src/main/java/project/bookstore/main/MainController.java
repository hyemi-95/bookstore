package project.bookstore.main;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import project.bookstore.member.dto.LoginUserDto;
import project.bookstore.member.security.CustomUserDetails;

@Controller
@RequiredArgsConstructor
public class MainController {

    /***
     * 로그인 성공 시 진입할 에인 페이지
     */
    @GetMapping("/main")
    public String main(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        LoginUserDto loginUserDto =null;
        boolean isAdmin =false;
        boolean isSeller =false;

        if(userDetails != null) {
            loginUserDto = new LoginUserDto(userDetails.getMember());
            isAdmin = userDetails.getMember().isAdmin();
            isSeller = userDetails.getMember().isSeller();
        }
        model.addAttribute("user", loginUserDto);
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("isSeller", isSeller);
        return "main/main";
    }
}
