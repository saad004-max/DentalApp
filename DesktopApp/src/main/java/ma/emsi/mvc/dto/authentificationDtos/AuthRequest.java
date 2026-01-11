package ma.emsi.mvc.dto.authentificationDtos;


public record AuthRequest(
        String login,
        String password
) {}
