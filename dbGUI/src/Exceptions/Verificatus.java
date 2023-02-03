package Exceptions;

import java.util.stream.Stream;

public class Verificatus {
    /**
     * aspiciansCampos() returns true if all fields are not empty.
     * @param userIDField the user ID field
     * @param locationField the location field
     * @param typeField the type field
     * @param titleField the title field
     * @param startHourBox the start hour box
     * @param startMinuteBox the start minute box
     * @param endHourBox the end hour box
     * @param endMinuteBox the end minute box
     * @return boolean true if all fields are not empty, false otherwise
     * */
    public static boolean aspiciansCampos(String userIDField,
                                          String locationField,
                                          String typeField,
                                          String titleField,
                                          String startHourBox,
                                          String startMinuteBox,
                                          String endHourBox,
                                          String endMinuteBox) throws Exception {
        return Stream.of(userIDField,
                        locationField,
                        typeField,
                        titleField,
                        startHourBox,
                        startMinuteBox,
                        endHourBox,
                        endMinuteBox)
                .anyMatch(field -> !field.isEmpty());
    }
}
