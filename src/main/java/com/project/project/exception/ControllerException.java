package com.project.project.exception;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
public class ControllerException extends RuntimeException{

    private static final long serialVersionUID = 1L;
    private String errorcode;
    private String errormessage;

    public String getErrorcode() {
        return errorcode;
    }
    public String getErrormessage() {
        return errormessage;
    }
    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public void setErrorcode(String errorcode) {
        this.errorcode = errorcode;
    }

    public void setErrormessage(String errormessage) {
        this.errormessage = errormessage;
    }

    public ControllerException(String errorcode, String errormessage) {
        this.errorcode = errorcode;
        this.errormessage = errormessage;
    }

    public ControllerException(){

    }

}
