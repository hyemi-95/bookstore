package project.bookstore.mypage;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import project.bookstore.member.dto.MemberListDto;
import project.bookstore.member.entity.Member;
import project.bookstore.member.security.CustomUserDetails;
import project.bookstore.member.service.MemberService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MyPageController {
    private final MemberService memberService;

    @GetMapping
    public String myPageHome(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        String nickname = userDetails.getMember().getNickname();
        model.addAttribute("nickname", nickname);
        return "mypage/myPage";
    }

    @GetMapping("/Myinfo")
    public String myPageinfo(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {

        Member member = userDetails.getMember();
        MemberListDto myInfo = memberService.findMyInfo(member);

        String nickname = member.getNickname();
        model.addAttribute("nickname", nickname);
        model.addAttribute("myInfo", myInfo);
        return "mypage/info";
    }
}
