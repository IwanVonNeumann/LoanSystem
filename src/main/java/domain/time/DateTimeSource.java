package domain.time;

import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

/**
 * Created by Iwan on 20.09.2014
 */

@Component
public class DateTimeSource {

    public DateTime getCurrentTime() {
        return new DateTime();
    }
}