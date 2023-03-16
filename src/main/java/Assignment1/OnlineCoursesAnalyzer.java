package Assignment1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OnlineCoursesAnalyzer {

    private final List<course> courses = new ArrayList<>();

    private final Supplier<Stream<course>> streamSupplier = courses::stream;

    public OnlineCoursesAnalyzer(String datasetPath) {
        try (BufferedReader bfr = new BufferedReader(
            new FileReader(datasetPath, StandardCharsets.UTF_8))) {
            // skip the first line
            String line = bfr.readLine();
            while ((line = bfr.readLine()) != null) {
                String[] result = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                for (int i = 0; i < result.length; i++) {
                    if (result[i].startsWith("\"")) {
                        result[i] = result[i].substring(1);
                    }
                    if (result[i].endsWith("\"")) {
                        result[i] = result[i].substring(0, result[i].length() - 1);
                    }
                }
                courses.add(new course(result[0], result[1],
                    LocalDate.parse(result[2], DateTimeFormatter.ofPattern("MM/dd/yyyy")),
                    result[3], result[4], result[5], Integer.parseInt(result[6]),
                    Integer.parseInt(result[7]), Integer.parseInt(result[8]),
                    Integer.parseInt(result[9]), Integer.parseInt(result[10]),
                    Double.parseDouble(result[11]), Double.parseDouble(result[12]),
                    Double.parseDouble(result[13]), Double.parseDouble(result[14]),
                    Double.parseDouble(result[15]), Double.parseDouble(result[16]),
                    Double.parseDouble(result[17]), Double.parseDouble(result[18]),
                    Double.parseDouble(result[19]), Double.parseDouble(result[20]),
                    Double.parseDouble(result[21]), Double.parseDouble(result[22])));
                // System.out.println(result.length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Integer> getPtcpCountByInst() {

        Map<String, Integer> rawMap = streamSupplier.get().collect(
            Collectors.groupingBy(course::getInstitution, Collectors.summingInt(course::getCount)));
        // use TreeMap to initiate a treeMap, which sorted by natural order
        return new TreeMap<>(rawMap);
    }

    public Map<String, Integer> getPtcpCountByInstAndSubject() {
        // distinct key is guaranteed here
        Map<String, Integer> rawMap = streamSupplier.get().collect(
            Collectors.groupingBy(c -> c.getInstitution() + "-" + c.getCourseSubject(),
                Collectors.summingInt(course::getCount)));
        Map<String, Integer> result = new TreeMap<>((s1, s2) -> (Objects.equals(rawMap.get(s1),
            rawMap.get(s2)))?s1.compareTo(s2):rawMap.get(s1) - rawMap.get(s2));
        return result;
    }

    public Map<String, List<List<String>>> getCourseListOfInstructor() {
        Map<String, List<course>> instructor_course = streamSupplier.get()
            .collect(Collectors.groupingBy(course::getInstructors));
        Map<String, List<List<String>>> result = new HashMap<>();
        instructor_course.forEach((s, l) -> {
            // no ","
            if (!s.contains(",")) {
                // key doesn't exist
                if (!result.containsKey(s)) {
                    List<List<String>> lls = new ArrayList<>();
                    lls.add(new ArrayList<>());
                    lls.add(new ArrayList<>());
                    result.put(s, lls);
                }
                // add the course title
                for (course c : l) {
                    result.get(s).get(0).add(c.getCourseTitle());
                }
            }
            // have ","
            else {
                String[] profs = s.split(", ");
                for (String p : profs) {
                    // key doesn't exist
                    if (!result.containsKey(p)) {
                        List<List<String>> lls = new ArrayList<>();
                        lls.add(new ArrayList<>());
                        lls.add(new ArrayList<>());
                        result.put(p, lls);
                    }
                    // add the course title
                    for (course c : l) {
                        result.get(p).get(1).add(c.getCourseTitle());
                    }
                }
            }
        });
        result.forEach((s, lls) -> {
            lls.get(0).sort(Comparator.naturalOrder());
            lls.get(0).sort(Comparator.naturalOrder());
        });
        return result;
    }

    public List<String> getCourses(int topK, String by) {
        if (by.equals("hours")) {
            // ascending
            List<course> lc = streamSupplier.get().sorted((c1, c2) -> {
                if (c1.getTotalCourseHours() - c2.getTotalCourseHours() > 0) {
                    return 1;
                } else if (c1.getTotalCourseHours() == c2.getTotalCourseHours()) {
                    return c1.getCourseTitle().compareTo(c2.getCourseTitle());
                } else {
                    return -1;
                }
            }).toList();
            // find topK from the rear
            List<String> ls = new ArrayList<>();
            for (int i = lc.size() - 1; i >= 0 && i >= lc.size() - topK; i--) {
                ls.add(lc.get(i).getCourseTitle());
            }
            return ls;
        } else {
            // ascending
            List<course> lc = streamSupplier.get().sorted((c1, c2) -> {
                if (c1.getParticipants() - c2.getParticipants() > 0) {
                    return 1;
                } else if (c1.getParticipants() == c2.getParticipants()) {
                    return c1.getCourseTitle().compareTo(c2.getCourseTitle());
                } else {
                    return -1;
                }
            }).toList();
            // find topK from the rear
            List<String> ls = new ArrayList<>();
            for (int i = lc.size() - 1; i >= 0 && i >= lc.size() - topK; i--) {
                ls.add(lc.get(i).getCourseTitle());
            }
            return ls;
        }
    }

    public List<String> searchCourses(String courseSubject, double percentAudited,
        double totalCourseHours) {
        return streamSupplier.get().filter(course -> Pattern
                .compile(".*" + courseSubject + ".*", Pattern.CASE_INSENSITIVE)
                .matcher(course.getCourseSubject()).matches())
            .filter(c -> c.getAuditedPercent() >= percentAudited)
            .filter(c -> c.getTotalCourseHours() <= totalCourseHours)
            .map(course::getCourseTitle)
            .sorted(Comparator.naturalOrder())
            .toList();
    }

    public List<String> recommendCourses(int age, int gender, int isBachelorOrHigher) {
        // groupingBy course number
        Map<String, Double> medianAges = streamSupplier.get().collect(
            Collectors.groupingBy(course::getCourseNumber,
                Collectors.averagingDouble(course::getMedianAge)));
        Map<String, Double> MalePercents = streamSupplier.get().collect(
            Collectors.groupingBy(course::getCourseNumber,
                Collectors.averagingDouble(course::getMalePercent)));
        Map<String, Double> BachelorOrHigherPercents = streamSupplier.get().collect(
            Collectors.groupingBy(course::getCourseNumber,
                Collectors.averagingDouble(course::getBachelorDegreeOrHigher)));
        Map<String, List<course>> courses = streamSupplier.get()
            .collect(Collectors.groupingBy(course::getCourseNumber));
        List<String> courseNumber = streamSupplier.get().map(course::getCourseNumber).distinct()
            .toList();
        Map<String, Double> similarity = new HashMap<>();
        courseNumber.forEach(num -> {
            double similarityValue = (age - medianAges.get(num)) * (age - medianAges.get(num))
                + (gender * 100 - MalePercents.get(num)) * (gender * 100 - MalePercents.get(num))
                + (isBachelorOrHigher * 100 - BachelorOrHigherPercents.get(num)) * (
                isBachelorOrHigher * 100 - BachelorOrHigherPercents.get(num));
            similarity.put(num, similarityValue);
        });
        List<Entry<String, Double>> l = similarity.entrySet().stream().sorted((e1, e2) ->
                (e1.getValue() - e2.getValue() > 0) ? 1 :
                    (e1.getValue() - e2.getValue() == 0) ? e1.getKey().compareTo(e2.getKey()) : -1)
            .toList();
        List<String> titles = new ArrayList<>();
        l.forEach(e ->
            titles.add(courses.get(e.getKey()).stream().max((c1, c2) -> {
                if (c1.getLaunchDate().isBefore(c2.getLaunchDate())) {
                    return -1;
                } else if (c1.getLaunchDate().isEqual(c2.getLaunchDate())) {
                    return 0;
                } else {
                    return 1;
                }
            }).get().getCourseTitle()) // we don't need to check isPresent() because it must exist
        );
        return titles.stream().distinct().limit(10).toList();
    }


    public static void main(String[] args) {
//        OnlineCoursesAnalyzer oca = new OnlineCoursesAnalyzer("./src/local.csv");
//        System.out.println(oca.searchCourses("scienCe", 1.0, 500));

        // 8 digit, begin with 1, 0-9
        Scanner s = new Scanner(System.in);
        String s1 = s.next();
        System.out.println(s1.matches("1[0-9]{7}"));

    }
}

class course {

    // online course holders
    private final String Institution;
    // the unique id of each course
    private final String CourseNumber;
    // the launch date of each course
    private final LocalDate LaunchDate;
    // the title of each course
    private final String CourseTitle;
    // the instructors of each course
    private final String Instructors;
    // the subject of each course
    private final String CourseSubject;
    //the last time of each course
    private final int Year;
    // with (1), without (0).
    private final int HonorCodeCertificates;
    // the number of participants who have accessed the course
    private final int Participants;
    // the number of participants who have audited more than 50% of the course
    private final int Audited;
    // Total number of votes
    private final int Certified;
    // the percent of the audited
    private final double AuditedPercent;
    // the percent of the certified
    private final double CertifiedPercent;
    // the percent of the certified with accessing the course more than 50%
    private final double CertifiedOfPercent;
    // the percent of playing video
    private final double PlayedVideoPercent;
    // the percent of posting in forum
    private final double PostedInForumPercent;
    // the percent of grade higher than zero
    private final double GradeHigherThanZeroPercent;
    // total course hours(per 1000)
    private final double TotalCourseHours;
    // median hours for certification
    private final double MedianHoursForCertification;
    // median age of the participants
    private final double MedianAge;
    // the percent of the male
    private final double MalePercent;
    // the percent of the female
    private final double FemalePercent;
    // the percent of bachelor's degree of higher
    private final double BachelorDegreeOrHigher;


    // self-define field
    private final int count = 1;

    public course(String Institution, String CourseNumber, LocalDate LaunchDate, String CourseTitle,
        String Instructors, String CourseSubject, int Year, int HonorCodeCertificates,
        int Participants, int Audited, int Certified, double AuditedPercent,
        double CertifiedPercent,
        double CertifiedOfPercent, double PlayedVideoPercent, double PostedInForumPercent,
        double GradeHigherThanZeroPercent,
        double TotalCourseHours, double MedianHoursForCertification, double MedianAge,
        double MalePercent, double FemalePercent, double BachelorDegreeOrHigher) {
        this.Institution = Institution;
        this.CourseNumber = CourseNumber;
        this.LaunchDate = LaunchDate;
        this.CourseTitle = CourseTitle;
        this.Instructors = Instructors;
        this.CourseSubject = CourseSubject;
        this.Year = Year;
        this.HonorCodeCertificates = HonorCodeCertificates;
        this.Participants = Participants;
        this.Audited = Audited;
        this.Certified = Certified;
        this.AuditedPercent = AuditedPercent;
        this.CertifiedPercent = CertifiedPercent;
        this.CertifiedOfPercent = CertifiedOfPercent;
        this.PlayedVideoPercent = PlayedVideoPercent;
        this.PostedInForumPercent = PostedInForumPercent;
        this.GradeHigherThanZeroPercent = GradeHigherThanZeroPercent;
        this.TotalCourseHours = TotalCourseHours;
        this.MedianHoursForCertification = MedianHoursForCertification;
        this.MedianAge = MedianAge;
        this.MalePercent = MalePercent;
        this.FemalePercent = FemalePercent;
        this.BachelorDegreeOrHigher = BachelorDegreeOrHigher;
    }

    public String getInstitution() {
        return Institution;
    }

    public String getCourseNumber() {
        return CourseNumber;
    }

    public LocalDate getLaunchDate() {
        return LaunchDate;
    }

    public String getCourseTitle() {
        return CourseTitle;
    }

    public String getInstructors() {
        return Instructors;
    }

    public String getCourseSubject() {
        return CourseSubject;
    }

    public int getYear() {
        return Year;
    }

    public int getHonorCodeCertificates() {
        return HonorCodeCertificates;
    }

    public int getParticipants() {
        return Participants;
    }

    public int getAudited() {
        return Audited;
    }

    public int getCertified() {
        return Certified;
    }

    public double getAuditedPercent() {
        return AuditedPercent;
    }

    public double getCertifiedPercent() {
        return CertifiedPercent;
    }

    public double getCertifiedOfPercent() {
        return CertifiedOfPercent;
    }

    public double getPlayedVideoPercent() {
        return PlayedVideoPercent;
    }

    public double getPostedInForumPercent() {
        return PostedInForumPercent;
    }

    public double getGradeHigherThanZeroPercent() {
        return GradeHigherThanZeroPercent;
    }

    public double getTotalCourseHours() {
        return TotalCourseHours;
    }

    public double getMedianHoursForCertification() {
        return MedianHoursForCertification;
    }

    public double getMedianAge() {
        return MedianAge;
    }

    public double getMalePercent() {
        return MalePercent;
    }

    public double getFemalePercent() {
        return FemalePercent;
    }

    public double getBachelorDegreeOrHigher() {
        return BachelorDegreeOrHigher;
    }

    public int getCount() {
        return count;
    }
}
