package pl.szlify.atipera.exception.handling;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ApiError {
    private int status;
    private String message;
}
