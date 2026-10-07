package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {

    private Map<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> timetable;

    public Timetable() {
        this.timetable = new HashMap<>();
    }

    public void addNewTrainingSession(TrainingSession trainingSession) {
        DayOfWeek day = trainingSession.getDayOfWeek();
        TimeOfDay time = trainingSession.getTimeOfDay();

        TreeMap<TimeOfDay, List<TrainingSession>> dayMap = timetable.computeIfAbsent(day,
                k -> new TreeMap<>());
        List<TrainingSession> sessionsAtTime = dayMap.computeIfAbsent(time, k -> new ArrayList<>());
        sessionsAtTime.add(trainingSession);
    }

    public List<TrainingSession> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        TreeMap<TimeOfDay, List<TrainingSession>> dayMap = timetable.get(dayOfWeek);

        if (dayMap == null) {
            return Collections.emptyList();
        }

        List<TrainingSession> result = new ArrayList<>();

        for (List<TrainingSession> sessions : dayMap.values()) {
            result.addAll(sessions);
        }

        return result;
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        TreeMap<TimeOfDay, List<TrainingSession>> dayMap = timetable.get(dayOfWeek);

        if (dayMap == null) {
            return Collections.emptyList();
        }

        List<TrainingSession> sessionsAtTime = dayMap.get(timeOfDay);

        if (sessionsAtTime == null) {
            return Collections.emptyList();
        }

        return new ArrayList<>(sessionsAtTime);
    }

    public static class CounterOfTrainings {
        private final Coach coach;
        private final int count;

        public CounterOfTrainings(Coach coach, int count) {
            this.coach = coach;
            this.count = count;
        }

        public Coach getCoach() {
            return coach;
        }

        public int getCount() {
            return count;
        }
    }
    
    public List<CounterOfTrainings> getCountByCoaches() {
        Map<Coach, Integer> coachCounts = new HashMap<>();

        for (TreeMap<TimeOfDay, List<TrainingSession>> dayMap : timetable.values()) {

            for (List<TrainingSession> sessionsAtTime : dayMap.values()) {

                for (TrainingSession session : sessionsAtTime) {
                    Coach coach = session.getCoach();

                    int currentCount = coachCounts.getOrDefault(coach, 0);
                    coachCounts.put(coach, currentCount + 1);
                }
            }
        }

        List<CounterOfTrainings> result = new ArrayList<>();
        for (Map.Entry<Coach, Integer> entry : coachCounts.entrySet()) {
            result.add(new CounterOfTrainings(entry.getKey(), entry.getValue()));
        }

        result.sort((a, b) -> Integer.compare(b.getCount(), a.getCount()));

        return result;
    }
}
