package su.grinev.FileUploader.dto;

public class JsonResponse {
    String message;

    public JsonResponse() {
    }

    public JsonResponse(String message) {
        super();
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}