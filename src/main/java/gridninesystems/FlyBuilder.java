package gridninesystems;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class FlyBuilder implements FlyingRules {

    protected static Flight createFlight(final LocalDateTime... dates) {
        if ((dates.length % 2) != 0) {
            throw new IllegalArgumentException(
                    "you must pass an even number of dates");
        }
        List<Segment> segments = new ArrayList<>(dates.length / 2);
        for (int i = 0; i < (dates.length - 1); i += 2) {
            segments.add(new Segment(dates[i], dates[i + 1]));
        }
        return new Flight(segments);
    }

    public FlyBuilder(LocalDateTime... dates) throws IllegalArgumentException {
        Flight testTrip = createFlight(dates);
        this.construcor(testTrip);
    }

    public static long flyingTime(Segment seg) {
        LocalDateTime dep = seg.getDepartureDate();
        LocalDateTime arr = seg.getArrivalDate();
        Duration dur = Duration.between(dep, arr);
        long millis = dur.toMillis();
        long result = TimeUnit.MILLISECONDS.toMinutes(millis);
        return result;
    }



    public boolean construcor(Flight flight) throws IllegalArgumentException {
        List<Segment> tripls = flight.segments;
        LocalDateTime xHour = LocalDateTime.now().minusMinutes(1);
        for (int i = 0; i < tripls.size(); i++) {
            Segment seg = tripls.get(i);
           pastDepart(seg);
           departAfterArrive(seg);
           if (i != 0) {
               waitingTime(xHour, seg);
               overlapDepart(xHour, seg);
            }
            xHour = seg.getArrivalDate();

        }
        System.out.println("Приятного полета");
        return true;
    }


    public static void main(String[] args) {
      /*  DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime dep = LocalDateTime.parse("2021-07-06T10:22", dateTimeFormatter);
        LocalDateTime arr = LocalDateTime.parse("2021-07-06T17:22", dateTimeFormatter);

        Segment segment1 = new Segment(dep,
                arr);
        Duration dur = Duration.between(dep, arr);
        long millis = dur.toMillis();
        long result = TimeUnit.MILLISECONDS.toMinutes(millis);

       */

        LocalDateTime threeDaysPlus = LocalDateTime.now().plusDays(3);
        List<LocalDateTime> flyingTime = new ArrayList<>();
        flyingTime.add(threeDaysPlus);
        flyingTime.add(threeDaysPlus.plusHours(2));
        Flight testTrip = createFlight(threeDaysPlus.minusWeeks(1), threeDaysPlus.plusHours(2),
                threeDaysPlus, threeDaysPlus.plusHours(14));
        FlyBuilder builder = new FlyBuilder();
        builder.construcor(testTrip);

        System.out.println();
    }

    @Override
    public boolean departAfterArrive(Segment segment) throws IllegalArgumentException {
        if (flyingTime(segment) < 0) {
            System.out.println("Error input - arrival befoure depurtcher");
            throw new IllegalArgumentException();
        }

        return true;
    }

    @Override
    public long waitingTime(LocalDateTime land, Segment segment) {
        Duration dur = Duration.between(land, segment.departureDate);
        long millis = dur.toMillis();
        long result = TimeUnit.MILLISECONDS.toMinutes(millis);
        if (result >= 120) {
            System.out.println("Время ожидания в аэропорту более 2 часов между рейсами");
            System.exit(0);
        } else {
            System.out.println("время ожидания следующего рейса " + result + " минут");
        }
        return result;
    }

        @Override
    public boolean pastDepart(Segment segment) throws IllegalArgumentException {
        if (segment.getDepartureDate().isBefore(LocalDateTime.now())) {
            System.out.println(segment + " - рейс невозможен - время вылета в прошлом," +
                    " если у Вас билет возвратный ты счастливчик");

            throw new IllegalArgumentException();
        }

        return true;
    }

    @Override
    public boolean overlapDepart(LocalDateTime land, Segment segment) throws IllegalArgumentException {
        if (segment.getDepartureDate().isBefore(land)) {
            System.out.println(segment.toString() + " - этот рейс осуществить невозможно, так как "
                    + "отправление раньше прилета предыдущего");
            throw new IllegalArgumentException();
        }

        return true;
    }


    public static class Segment {
        private final LocalDateTime departureDate;

        private final LocalDateTime arrivalDate;

        Segment(final LocalDateTime dep, final LocalDateTime arr) {
            departureDate = Objects.requireNonNull(dep);
            arrivalDate = Objects.requireNonNull(arr);
        }

        LocalDateTime getDepartureDate() {
            return departureDate;
        }

        LocalDateTime getArrivalDate() {
            return arrivalDate;
        }

        @Override
        public String toString() {
            Duration dur = Duration.between(departureDate, arrivalDate);
            long millis = dur.toMillis();
            long result = TimeUnit.MILLISECONDS.toMinutes(millis);
            DateTimeFormatter fmt =
                    DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            return " Полет - [" + departureDate.format(fmt) + '|' + arrivalDate.format(fmt)
                    + "] Длительность полета: " + result
                    + " минут";
        }
    }

    static class Flight {
        private final List<Segment> segments;

        Flight(final List<Segment> segs) {
            segments = segs;
        }

        List<Segment> getSegments() {
            return segments;
        }

        @Override
        public String toString() {
            String speach = "Рейс № ";
            final int[] i = {1};
            String collect = segments.stream().map(
                    ret -> speach + (i[0]++)
                    + ret.toString()
            ).collect(Collectors.joining(" \n"));
            return collect;
        }
    }
}
