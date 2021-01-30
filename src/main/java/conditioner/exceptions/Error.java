package conditioner.exceptions;

public class Error {
    private String errCode;
    private String errDesc;

    public Error(String errCode, String errDesc) {
        this.errCode = errCode;
        this.errDesc = errDesc;
    }
}