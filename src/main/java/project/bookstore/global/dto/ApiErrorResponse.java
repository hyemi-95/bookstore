package project.bookstore.global.dto;

//공동 API에러 응답 DTO
public class ApiErrorResponse {

        private final String message;

    public ApiErrorResponse(String message) {

        this.message = message;
    }

    public String getMessage() {//응답을 직렬화할떄 사용됨

        return message;
    }

}
