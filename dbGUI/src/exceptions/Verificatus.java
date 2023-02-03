package exceptions;

import javafx.scene.control.TextField;

import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Verificatus {

    /**
     * aspiciansCampos() returns true if all fields are not empty.
     * @param field String... field variable length argument, makes the method more flexible
     * @return boolean true if all fields are not empty, false otherwise
     * */
    public static boolean stringCheck(String... field) throws Exception {
        return Stream.of(field)
                .anyMatch(f -> !f.isEmpty()); // returns true if any fields are empty
    }

    /**
     * numberCheck() returns true if any of the values are null.
     * @param values int... values variable length argument, makes the method more flexible
     * @return boolean true if any of the values are null, false otherwise
     * */
    public static boolean numberCheck(int... values) throws Exception {
        return Stream.of(values)
                .anyMatch(Objects::isNull); // returns true if any of the values are null
    }

    /**
     * fieldCheck() returns true if any of the fields are empty.
     * @param field TextField... field variable length argument, makes the method more flexible
     * @return boolean true if any of the fields are empty, false otherwise
     * */
    public static boolean fieldCheck(TextField... field) throws Exception {
        return Stream.of(field)
                .anyMatch(f -> f.getText().isEmpty()); // returns true if any fields are empty
    }

    /**
     * dataCheck() returns true if any of the functions return true.
     * */
    @SafeVarargs
    public static boolean dataCheck(Supplier<Boolean>... functions) throws Exception {
        return Stream.of(functions)
                .map(Supplier::get)
                .anyMatch(result -> result);
    }
}

//        return stringCheck(strings) || numberCheck(ints) || fieldCheck(fields);

