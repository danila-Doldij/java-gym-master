package ru.yandex.practicum.gym;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

public class TimetableTest {

    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        List<TrainingSession> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        Assertions.assertEquals(1, mondaySessions.size(),
                "Ошибка: в понедельник должна быть ровно 1 тренировка, а найдено " + mondaySessions.size());

        TrainingSession foundSession = mondaySessions.get(0);
        Assertions.assertEquals(13, foundSession.getTimeOfDay().getHours(),
                "Ошибка: тренировка должна начинаться в 13:00");
        Assertions.assertEquals(0, foundSession.getTimeOfDay().getMinutes(),
                "Ошибка: минуты должны быть 00");

        List<TrainingSession> tuesdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        Assertions.assertTrue(tuesdaySessions.isEmpty(),
                "Ошибка: во вторник тренировок быть не должно, но список не пустой");
    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        Timetable timetable = new Timetable();
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));

        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);
        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        List<TrainingSession> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        Assertions.assertEquals(1, mondaySessions.size());
        Assertions.assertEquals(13, mondaySessions.get(0).getTimeOfDay().getHours());

        List<TrainingSession> thursdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);
        Assertions.assertEquals(2, thursdaySessions.size());

        TrainingSession firstSession = thursdaySessions.get(0);
        TrainingSession secondSession = thursdaySessions.get(1);

        Assertions.assertEquals(13, firstSession.getTimeOfDay().getHours());
        Assertions.assertEquals(20, secondSession.getTimeOfDay().getHours());

        List<TrainingSession> tuesdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        Assertions.assertTrue(tuesdaySessions.isEmpty());
    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = new Timetable();
        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        TrainingSession session13 = new TrainingSession(group, coach, DayOfWeek.MONDAY,
                new TimeOfDay(13, 0));
        TrainingSession session14 = new TrainingSession(group, coach, DayOfWeek.MONDAY,
                new TimeOfDay(14, 0));

        timetable.addNewTrainingSession(session13);
        timetable.addNewTrainingSession(session14);

        List<TrainingSession> sessionsAt13 = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY,
                new TimeOfDay(13, 0));
        Assertions.assertEquals(1, sessionsAt13.size());
        Assertions.assertEquals(13, sessionsAt13.get(0).getTimeOfDay().getHours());

        List<TrainingSession> sessionsAt14 = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY,
                new TimeOfDay(14, 0));
        Assertions.assertEquals(1, sessionsAt14.size());
        Assertions.assertEquals(14, sessionsAt14.get(0).getTimeOfDay().getHours());

        List<TrainingSession> sessionsAt15 = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY,
                new TimeOfDay(15, 0));
        Assertions.assertTrue(sessionsAt15.isEmpty());
    }

    @Test
    void testGetCountByCoaches() {
        Timetable timetable = new Timetable();
        Coach coachA = new Coach("Иванов", "Иван", "Иванович");
        Coach coachB = new Coach("Петров", "Петр", "Петрович");

        Group group = new Group("Йога", Age.ADULT, 60);

        timetable.addNewTrainingSession(new TrainingSession(group, coachA, DayOfWeek.MONDAY,
                new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coachA, DayOfWeek.MONDAY,
                new TimeOfDay(11, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coachA, DayOfWeek.TUESDAY,
                new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coachB, DayOfWeek.MONDAY,
                new TimeOfDay(12, 0)));

        List<Timetable.CounterOfTrainings> stats = timetable.getCountByCoaches();

        Assertions.assertEquals(2, stats.size());

        Assertions.assertEquals(coachA, stats.get(0).getCoach());
        Assertions.assertEquals(3, stats.get(0).getCount());

        Assertions.assertEquals(coachB, stats.get(1).getCoach());
        Assertions.assertEquals(1, stats.get(1).getCount());
    }

    @Test
    void testMultipleGroupsAtSameTime() {
        Timetable timetable = new Timetable();
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupKids = new Group("Акробатика для детей", Age.CHILD, 60);
        Group groupAdults = new Group("Акробатика для взрослых", Age.ADULT, 90);

        TrainingSession kidsSession = new TrainingSession(groupKids, coach, DayOfWeek.MONDAY,
                new TimeOfDay(13, 0));
        TrainingSession adultsSession = new TrainingSession(groupAdults, coach, DayOfWeek.MONDAY,
                new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(kidsSession);
        timetable.addNewTrainingSession(adultsSession);

        List<TrainingSession> allMonday = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        Assertions.assertEquals(2, allMonday.size(),
                "В понедельник должно быть 2 тренировки (дети и взрослые в одно время)");

        boolean hasKids = allMonday.stream()
                .anyMatch(s -> s.getGroup().getTitle().equals("Акробатика для детей"));

        boolean hasAdults = allMonday.stream()
                .anyMatch(s -> s.getGroup().getTitle().equals("Акробатика для взрослых"));

        Assertions.assertTrue(hasKids, "Не найдена тренировка для детей");
        Assertions.assertTrue(hasAdults, "Не найдена тренировка для взрослых");
    }

    @Test
    void testEmptyScheduleReturnsEmptyList() {
        Timetable timetable = new Timetable();

        List<TrainingSession> sessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);

        Assertions.assertNotNull(sessions, "Метод не должен возвращать null, даже если расписание пустое");

        Assertions.assertTrue(sessions.isEmpty(), "Список должен быть пустым, если тренировок нет");

        List<TrainingSession> sessionsAtTime = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY,
                new TimeOfDay(10, 0));
        Assertions.assertNotNull(sessionsAtTime, "Метод не должен возвращать null");
        Assertions.assertTrue(sessionsAtTime.isEmpty(), "Список должен быть пустым");

        List<Timetable.CounterOfTrainings> stats = timetable.getCountByCoaches();
        Assertions.assertNotNull(stats, "Метод статистики не должен возвращать null");
        Assertions.assertTrue(stats.isEmpty(), "Список статистики должен быть пустым");
    }

    @Test
    void testGetCountByCoaches_MergesSameCoaches() {
        Timetable timetable = new Timetable();
        Group group = new Group("Йога", Age.ADULT, 60);

        Coach coach1 = new Coach("Иванов", "Иван", "Иванович");
        Coach coach2 = new Coach("Иванов", "Иван", "Иванович");

        Assertions.assertNotSame(coach1, coach2, "Это должны быть разные объекты в памяти (разные ссылки)");

        Assertions.assertEquals(coach1, coach2, "Но по содержанию (ФИО) они должны быть равны");

        timetable.addNewTrainingSession(new TrainingSession(group, coach1, DayOfWeek.MONDAY,
                new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach2, DayOfWeek.TUESDAY,
                new TimeOfDay(10, 0)));

        List<Timetable.CounterOfTrainings> stats = timetable.getCountByCoaches();

        Assertions.assertEquals(1, stats.size(),
                "Должен быть только один тренер в списке, так как ФИО совпадают");
        Assertions.assertEquals(2, stats.get(0).getCount(),
                "Количество тренировок должно быть суммировано (2)");
        Assertions.assertEquals(coach1, stats.get(0).getCoach(), "Тренер должен совпадать");
    }

    @Test
    void testGetCountByCoaches_SortsEqualCountsCorrectly() {
        Timetable timetable = new Timetable();
        Group group = new Group("Бокс", Age.ADULT, 60);

        Coach coachA = new Coach("А", "А", "А");
        Coach coachB = new Coach("Б", "Б", "Б");
        Coach coachC = new Coach("В", "В", "В");

        timetable.addNewTrainingSession(new TrainingSession(group, coachA, DayOfWeek.MONDAY,
                new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coachA, DayOfWeek.TUESDAY,
                new TimeOfDay(10, 0)));

        timetable.addNewTrainingSession(new TrainingSession(group, coachB, DayOfWeek.MONDAY,
                new TimeOfDay(11, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coachB, DayOfWeek.WEDNESDAY,
                new TimeOfDay(11, 0)));

        timetable.addNewTrainingSession(new TrainingSession(group, coachC, DayOfWeek.MONDAY,
                new TimeOfDay(12, 0)));

        List<Timetable.CounterOfTrainings> stats = timetable.getCountByCoaches();

        Assertions.assertEquals(3, stats.size(),
                "В списке должны быть все три тренера");

        Assertions.assertEquals(2, stats.get(0).getCount(),
                "Первый тренер должен иметь 2 тренировки");
        Assertions.assertEquals(2, stats.get(1).getCount(),
                "Второй тренер тоже должен иметь 2 тренировки");

        Assertions.assertEquals(1, stats.get(2).getCount(),
                "Третий тренер должен иметь только 1 тренировку");

        int countLeader1 = stats.get(0).getCount();
        int countLeader2 = stats.get(1).getCount();
        int countLast = stats.get(2).getCount();

        Assertions.assertTrue(countLeader1 >= countLast,
                "Лидеры должны иметь тренировок >= чем аутсайдер");
        Assertions.assertTrue(countLeader2 >= countLast,
                "Лидеры должны иметь тренировок >= чем аутсайдер");
    }

    @Test
    void testGetCountByCoaches_EdgeCases() {
        Timetable timetable = new Timetable();
        Group group = new Group("Плавание", Age.CHILD, 45);
        Coach singleCoach = new Coach("Одинокий", "Тренер", "Одинокович");

        List<Timetable.CounterOfTrainings> emptyStats = timetable.getCountByCoaches();

        Assertions.assertNotNull(emptyStats, "Метод не должен возвращать null для пустого расписания");
        Assertions.assertTrue(emptyStats.isEmpty(), "Для пустого расписания список должен быть пустым");

        timetable.addNewTrainingSession(new TrainingSession(group, singleCoach, DayOfWeek.MONDAY,
                new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, singleCoach, DayOfWeek.TUESDAY,
                new TimeOfDay(10, 0)));

        List<Timetable.CounterOfTrainings> singleStats = timetable.getCountByCoaches();

        Assertions.assertEquals(1, singleStats.size(),
                "Должен быть только один тренер в списке");

        Assertions.assertEquals(2, singleStats.get(0).getCount(),
                "У единственного тренера должно быть 2 тренировки");

        Assertions.assertEquals(singleCoach, singleStats.get(0).getCoach(),
                "Тренер в статистике должен совпадать с добавленным");
    }
}
