package org.example.utils;

import org.example.model.GanttChart;

import java.util.List;

public class GanttChartPrinter {
    public void printGanttChart(List<GanttChart> ganttData) {
        if (ganttData == null || ganttData.isEmpty()) {
            System.out.println("\nNo Gantt Chart data to display.");
            return;
        }

        System.out.println("\n--- Gantt Chart ---");
        System.out.println();

        // Print process segments
        StringBuilder processesLine = new StringBuilder("|");
        StringBuilder timelineLine = new StringBuilder("0");
        StringBuilder timeMarkersLine = new StringBuilder("0");

        int lastEndTime = 0;

        for (GanttChart entry : ganttData) {
            int startTime = entry.getStartTime();
            int endTime = entry.getEndTime();
            int duration = endTime - startTime;
            String processLabel = " P" + entry.getProcessID() + " ";
            int labelLength = processLabel.length();

            // Handle idle time
            if (startTime > lastEndTime) {
                int idleDuration = startTime - lastEndTime;
                String idleLabel = " Idle ";
                processesLine.append(idleLabel);
                processesLine.append(" ".repeat(idleDuration)); // pad
                processesLine.append("|");

                timelineLine.append("-".repeat(idleLabel.length() + idleDuration - 1));
                timelineLine.append(">");

                timeMarkersLine.append(" ".repeat(idleLabel.length() + idleDuration - String.valueOf(startTime).length()));
                timeMarkersLine.append(startTime);

                lastEndTime = startTime;
            }

            // Add process
            processesLine.append(processLabel);
            processesLine.append(" ".repeat(duration - 1)); // Adjust space if duration > label length
            processesLine.append("|");

            timelineLine.append("-".repeat(duration + labelLength - 1));
            timelineLine.append(">");

            timeMarkersLine.append(" ".repeat(duration + labelLength - String.valueOf(endTime).length()));
            timeMarkersLine.append(endTime);

            lastEndTime = endTime;
        }


        System.out.println(processesLine.toString());
        System.out.println(timelineLine.toString());
        System.out.println(timeMarkersLine.toString());

    }
}
