package ma.emsi.mvc.dto.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Wrapper générique pour les réponses API.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseWrapper<T> {
    
    private boolean success;
    private String message;
    private T data;
    private String errorCode;
    
    public static <T> ResponseWrapper<T> success(T data) {
        return new ResponseWrapper<>(true, "Opération réussie", data, null);
    }
    
    public static <T> ResponseWrapper<T> success(T data, String message) {
        return new ResponseWrapper<>(true, message, data, null);
    }
    
    public static <T> ResponseWrapper<T> error(String message) {
        return new ResponseWrapper<>(false, message, null, null);
    }
    
    public static <T> ResponseWrapper<T> error(String message, String errorCode) {
        return new ResponseWrapper<>(false, message, null, errorCode);
    }
}
