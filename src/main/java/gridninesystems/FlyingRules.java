package gridninesystems;

import java.time.LocalDateTime;

import static gridninesystems.FlyBuilder.*;

public interface FlyingRules {
    boolean departAfterArrive(Segment segment);
    long waitingTime(LocalDateTime land, Segment segment);
    boolean pastDepart(Segment segment);
    boolean overlapDepart(LocalDateTime land, Segment segment);

}
