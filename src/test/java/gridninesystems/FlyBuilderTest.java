package gridninesystems;

import org.junit.Test;

import java.time.LocalDateTime;

import static gridninesystems.FlyBuilder.createFlight;
import static org.junit.Assert.*;
import static org.hamcrest.core.Is.is;

public class FlyBuilderTest {

    @Test
    public void waitingTimeTest() {
        LocalDateTime threeDaysPlus = LocalDateTime.now().plusDays(3);
        LocalDateTime land = threeDaysPlus.minusHours(1);
        FlyBuilder.Flight testTrip = createFlight(threeDaysPlus, threeDaysPlus.plusHours(2));
        FlyBuilder builder = new FlyBuilder();
        long res = builder.waitingTime(land, testTrip.getSegments().get(0));
        assertThat(res, is(Long.valueOf(60)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void pastDepartTest() {
        LocalDateTime threeDaysPlus = LocalDateTime.now().plusDays(3);
        FlyBuilder builder = new FlyBuilder(threeDaysPlus.minusWeeks(1), threeDaysPlus.plusHours(2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void departAfterArriveTest() {
        LocalDateTime threeDaysPlus = LocalDateTime.now().plusDays(3);
        FlyBuilder builder = new FlyBuilder(threeDaysPlus.plusHours(5), threeDaysPlus);
    }

    @Test(expected = IllegalArgumentException.class)
    public void overlapDepartTest() {
        LocalDateTime threeDaysPlus = LocalDateTime.now().plusDays(3);
        FlyBuilder builder = new FlyBuilder(threeDaysPlus, threeDaysPlus.plusHours(5),
                threeDaysPlus.plusHours(4), threeDaysPlus.plusHours(6));
    }


}