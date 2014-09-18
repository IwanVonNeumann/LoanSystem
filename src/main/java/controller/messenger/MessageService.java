package controller.messenger;

import org.springframework.stereotype.Component;

/**
 * Created by Iwan on 14.09.2014
 */

@Component
public class MessageService {

    private  String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
