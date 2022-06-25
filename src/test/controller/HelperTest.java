/**
 * COMMENTED OUT TO PREVENT MOCKITO PACKAGE FROM PREVENTING PROJECT BEING BUILT
 * If you want to test these, Mockito is required.
 * */

//package test.controller;
//
//import controller.Helper;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.sql.SQLException;
//import java.sql.Timestamp;
//import java.time.LocalDateTime;
//import java.time.Month;
//import java.time.ZoneId;
//import java.util.Locale;
//
//import static org.mockito.Mockito.*;
//
//class HelperTest {
//    @Mock
//    ZoneId timezone;
//    @InjectMocks
//    Helper helper;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.initMocks(this);
//    }
//
//    @Test
//    void testGetLanguage() {
//        String result = Helper.getLanguage();
//        Assertions.assertEquals("replaceMeWithExpectedResult", result);
//    }
//
//    @Test
//    void testGetCountry() {
//        String result = Helper.getCountry();
//        Assertions.assertEquals("replaceMeWithExpectedResult", result);
//    }
//
//    @Test
//    void testGetCountryFromId() {
//        String result = Helper.getCountryFromId(0);
//        Assertions.assertEquals("replaceMeWithExpectedResult", result);
//    }
//
//    @Test
//    void testGetUsernameFromId() {
//        String result = Helper.getUsernameFromId(0);
//        Assertions.assertEquals("replaceMeWithExpectedResult", result);
//    }
//
//    @Test
//    void testGetLocale() {
//        Locale result = Helper.getLocale();
//        Assertions.assertEquals(null, result);
//    }
//
//    @Test
//    void testErrorDialog() {
//        Helper.errorDialog("error");
//    }
//
//    @Test
//    void testNoticeDialog() {
//        Helper.noticeDialog("notice");
//    }
//
//    @Test
//    void testReportDialog() {
//        Helper.reportDialog("reportType", "reportBlurb", "reportBody");
//    }
//
//    @Test
//    void testGetTimeZone() {
//        Helper.getTimeZone();
//    }
//
//    @Test
//    void testToLocal() {
//        Timestamp result = Helper.toLocal(null);
//        Assertions.assertEquals(null, result);
//    }
//
//    @Test
//    void testToUTC() {
//        Timestamp result = Helper.toUTC(null);
//        Assertions.assertEquals(null, result);
//    }
//
//    @Test
//    void testLocalToEST() {
//        Timestamp result = Helper.localToEST(null);
//        Assertions.assertEquals(null, result);
//    }
//
//    @Test
//    void testMainCheck() {
//        boolean result = Helper.mainCheck(null, null);
//        Assertions.assertEquals(true, result);
//    }
//
//    @Test
//    void testAddAppointmentCheck() throws SQLException {
//        boolean result = Helper.addAppointmentCheck(null, null, 0);
//        Assertions.assertEquals(true, result);
//    }
//
//    @Test
//    void testUpdateAppointmentCheck() throws SQLException {
//        boolean result = Helper.updateAppointmentCheck(0, null, null, 0);
//        Assertions.assertEquals(true, result);
//    }
//
//    @Test
//    void testOverlapCheck() {
//        boolean result = Helper.overlapCheck(LocalDateTime.of(2022, Month.JUNE, 25, 5, 36, 12), LocalDateTime.of(2022, Month.JUNE, 25, 5, 36, 12), LocalDateTime.of(2022, Month.JUNE, 25, 5, 36, 12), LocalDateTime.of(2022, Month.JUNE, 25, 5, 36, 12), 0);
//        Assertions.assertEquals(true, result);
//    }
//
//    @Test
//    void testCheckSpecialCharacters() {
//        boolean result = Helper.checkSpecialCharacters("str");
//        Assertions.assertEquals(true, result);
//    }
//}